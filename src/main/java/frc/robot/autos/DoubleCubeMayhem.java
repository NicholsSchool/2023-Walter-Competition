package frc.robot.autos;

import java.util.HashMap;
import org.photonvision.PhotonCamera;
import com.pathplanner.lib.PathPlanner;
import com.pathplanner.lib.PathPlannerTrajectory;
import com.pathplanner.lib.auto.PIDConstants;
import com.pathplanner.lib.auto.SwerveAutoBuilder;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.SwerveDrive;
import frc.robot.Constants.IntakeConstants;
import frc.robot.Constants.SwerveDriveConstants;
import frc.robot.commands.*;
import frc.robot.subsystems.*;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.RunCommand;
import com.pathplanner.lib.PathConstraints;

public class DoubleCubeMayhem extends SequentialCommandGroup {

  PhotonCamera camera;
  SwerveDrive swerveDrive;
  Intake intake;
  Uprighter uprighter;
  Gripper gripper;
  Arm arm;

  public DoubleCubeMayhem(SwerveDrive _swerveDrive, Intake _intake, Uprighter _uprighter, Gripper _gripper, Arm _arm) {

    swerveDrive = _swerveDrive;
    intake = _intake;
    uprighter = _uprighter;
    gripper = _gripper;
    arm = _arm;

    PathPlannerTrajectory path = PathPlanner.loadPath("MayhemForward", new PathConstraints(4, 3));
    PathPlannerTrajectory back = PathPlanner.loadPath("MayhemBackward", new PathConstraints(4, 3));


    SwerveAutoBuilder autoBuilder = new SwerveAutoBuilder(_swerveDrive::getPose, _swerveDrive::resetOdometry,
        SwerveDriveConstants.SWERVE_DRIVE_KINEMATICS, new PIDConstants(3.0, 0.0, 0.0), new PIDConstants(0.5, 0.0, 0.0),
        _swerveDrive::setModuleStates, new HashMap<String, Command>(), true, _swerveDrive);

    addRequirements(swerveDrive, intake, gripper, arm, uprighter);

    addCommands(new RunCommand(() -> uprighter.spinOut(), intake).withTimeout(0.5),
      new OuttakeCube( intake, uprighter, gripper, IntakeConstants.OUTTAKE_HIGH_SPEED ).withTimeout( 0.5 ) ); 
    addCommands(autoBuilder.resetPose(path));
    addCommands(autoBuilder.followPath(path));
    addCommands( new RotateRobot(_swerveDrive, 180.0 ).withTimeout(1));
    addCommands( new MLPickup(_swerveDrive).withTimeout(1).raceWith(new DeployIntake(_intake, _uprighter)));
    addCommands(new RotateRobot(_swerveDrive, 0.0 ).withTimeout(3));
    addCommands(autoBuilder.resetPose(back));
    addCommands(autoBuilder.followPath(back));
    addCommands(new ApriltagAlign(_swerveDrive).withTimeout(2));
    addCommands( new OuttakeCube(intake, uprighter, gripper, IntakeConstants.OUTTAKE_LOW_SPEED).withTimeout( 2 ) );
  


  }
}