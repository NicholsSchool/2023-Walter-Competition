package frc.robot.commands;

import org.photonvision.PhotonCamera;
import org.photonvision.targeting.PhotonPipelineResult;
import org.photonvision.targeting.PhotonTrackedTarget;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.*;

public class BalanceRobot extends CommandBase {

  SwerveDrive swerveDrive;
  PhotonCamera camera;

  double prevDistance = 0.0;
  boolean firstPassCompleted = false; 

  //Change this for balancing
  private static double APRILTAG_TO_CHARGE_STATION_METERS = 2.54; 
  private static double APRILTAG_TO_END_OF_COMMUNITY_METERS = 3.5; //Just picked this value. Need to adjust with tests

  


  public BalanceRobot(SwerveDrive _swerveDrive, boolean _isFacingChargeStation) {
    swerveDrive = _swerveDrive;
    addRequirements(swerveDrive);
  }

  @Override
  public void initialize() {
    camera = new PhotonCamera( "Microsoft_LifeCam_HD-3000" );
  }


  /**
   * Gets the distance of the robot from an apriltag
   * 
   * @return the distance of the robot from the apriltag. If no Apriltag found returns prev distance
   */
  public double getDistance()
  {
    PhotonPipelineResult result = camera.getLatestResult(); 
    if( result.hasTargets() )
    {
      PhotonTrackedTarget target = result.getBestTarget();
      double x = target.getBestCameraToTarget().getX(); //Not sure if it's x
      prevDistance = x;
      return x; 
    }
    return prevDistance; 
  }



  @Override
  public void execute() {
    double distance = getDistance();

    if( !firstPassCompleted && distance < APRILTAG_TO_END_OF_COMMUNITY_METERS )
    {
      swerveDrive.drive( -0.6, 0, 0, true ); 
    }
    else if( !firstPassCompleted )
    {
      firstPassCompleted = true; 
      new RotateRobot(swerveDrive, 0.0).withTimeout(2); 
    }

    if( firstPassCompleted )
    {
      PIDController pidApriltag = new PIDController( 1, 0, 0); 

      double powerApriltag = pidApriltag.calculate( distance, APRILTAG_TO_CHARGE_STATION_METERS);
      powerApriltag = powerApriltag / 10 * 6.25; 
      pidApriltag.setTolerance( 0.1 );


      System.out.println("Power:" + powerApriltag);
      System.out.println("Distnace:" + distance); //Useful for Calibration
      System.out.println("At Point:" + pidApriltag.atSetpoint());


      if( pidApriltag.atSetpoint() )
      {
        pidApriltag.close();
        swerveDrive.setWheelsToXFormation();
      }
      else if( distance < APRILTAG_TO_CHARGE_STATION_METERS && !pidApriltag.atSetpoint() )
      {
        swerveDrive.drive( -powerApriltag, 0, 0, true);
      }
      else if( distance > APRILTAG_TO_CHARGE_STATION_METERS && !pidApriltag.atSetpoint() )
      {
        swerveDrive.drive( -powerApriltag, 0, 0, true);
      }
    }
  }

  @Override
  public void end(boolean interrupted) {
    swerveDrive.drive( 0, 0, 0, true);
  }

  @Override
  public boolean isFinished() {
    return false;
  }

}