package frc.robot.tests;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.SwerveDrive;
import java.util.HashMap;
import com.pathplanner.lib.PathConstraints;
import com.pathplanner.lib.PathPlanner;
import com.pathplanner.lib.PathPlannerTrajectory;
import com.pathplanner.lib.auto.PIDConstants;
import com.pathplanner.lib.auto.SwerveAutoBuilder;
import frc.robot.subsystems.*;
import static frc.robot.Constants.SwerveDriveConstants.*;

// TVR Path Planner (Used during TVR finals 2023)
public class TestPP extends SequentialCommandGroup {

  SwerveDrive swerveDrive;
  Intake intake;
  Uprighter uprighter;
  Gripper gripper;
  Arm arm;

  public TestPP(SwerveDrive _swerveDrive, Intake _intake, Uprighter _uprighter, Gripper _gripper,
      Arm _arm) {

    swerveDrive = _swerveDrive;
    intake = _intake;
    uprighter = _uprighter;
    gripper = _gripper;
    arm = _arm;

    PathPlannerTrajectory traj = PathPlanner.loadPath("PathTVR", new PathConstraints(3.0, 2.0));

    SwerveAutoBuilder autoBuilder = new SwerveAutoBuilder(_swerveDrive::getPose, _swerveDrive::resetOdometry,
        SWERVE_DRIVE_KINEMATICS, new PIDConstants(3.0, 0.0, 0.0), new PIDConstants(0.5, 0.0, 0.0),
        _swerveDrive::setModuleStates, new HashMap<String, Command>(), true, _swerveDrive);

    // Create config for trajectory
    // TrajectoryConfig config = new TrajectoryConfig(Math.PI,
    // Math.PI).setKinematics(SWERVE_DRIVE_KINEMATICS);

    // All X/Y positions are relative to the robot. Auto is not field orientated!
    // Positive X is forward, positive Y is left, positive rotation is clockwise.
    // Trajectory trajectory01 = TrajectoryGenerator.generateTrajectory(
    // // Zero the starting pose of the trajectory.
    // new Pose2d(0, 0, new Rotation2d(0)),
    // List.of(
    // new Translation2d(-0.75, 0.33)
    // // Add interior waypoints to the list above.
    // ),
    // // Final X/Y position in meters and rotation in radians.
    // new Pose2d(-3.5, 0.33, new Rotation2d(0)), config);

    // Trajectory trajectory02 = TrajectoryGenerator.generateTrajectory(
    // // Zero the starting pose of the trajectory.
    // new Pose2d(0, 0, new Rotation2d(0)),
    // List.of(
    // new Translation2d(0.33, 0.33)
    // // Add interior waypoints to the list above.
    // ),
    // // Final X/Y position in meters and rotation in radians.
    // new Pose2d(0.44, 0.55, new Rotation2d(0)), config);

    // Create a PID controller for the robot's translation and rotation.
    // var thetaController = new ProfiledPIDController(1.0, 0, 0, new
    // Constraints(Math.PI, Math.PI));
    // thetaController.enableContinuousInput(-Math.PI, Math.PI);

    // SwerveControllerCommand swerveCC01 = new SwerveControllerCommand(
    // trajectory01,
    // swerveDrive::getPose,
    // SWERVE_DRIVE_KINEMATICS,
    // new PIDController(1.0, 0, 0),
    // new PIDController(1.0, 0, 0),
    // thetaController,
    // swerveDrive::setModuleStates,
    // swerveDrive);

    // SwerveControllerCommand swerveCC02 = new SwerveControllerCommand(
    // trajectory02,
    // swerveDrive::getPose,
    // SWERVE_DRIVE_KINEMATICS,
    // new PIDController(1.0, 0, 0),
    // new PIDController(1.0, 0, 0),
    // thetaController,
    // swerveDrive::setModuleStates,
    // swerveDrive);

    // Reset odometry to the starting pose of the trajectory.
    // swerveDrive.resetOdometry(trajectory01.getInitialPose());

    // Add all the requirements
    addRequirements(swerveDrive, intake, gripper, arm, uprighter);

    // Run path following command, then stop at the end.

    // Question: Can I spin the robot in place using the trajectory planner?
    // Answer: 45 degres points up and to the left

    addCommands(
        // 1.08 seconds - Initial shooting cube is optimal.
        autoBuilder.resetPose(traj),
        new RunCommand(() -> intake.close(), intake).withTimeout(0.5),
        new RunCommand(() -> uprighter.spinOut(), intake).withTimeout(0.65),
        new RunCommand(() -> uprighter.stop(), intake).withTimeout(0.0),
        new RunCommand(() -> intake.spinOut(), intake).withTimeout(0.5),
        new RunCommand(() -> intake.open(), intake).withTimeout(0.5),
        new InstantCommand(() -> intake.stop(), intake));

    // addCommands(new NudgeRobot(swerveDrive, "NUDGE BACKWARD",
    // true).withTimeout(0.5));

    // Swerve Drive Auto
    addCommands(autoBuilder.followPath(traj));

    // Move -90 degrees
    // addCommands(new RotateRobot(swerveDrive, 90.0).withTimeout(0.88));

    // Move -90 degress and reset gyro
    // addCommands(new RotateRobot(swerveDrive, 180.0).withTimeout(0.88)
    // .andThen(() -> swerveDrive.resetGyro(), swerveDrive));

    // addCommands(swerveCC02.raceWith(new DeployIntake(intake,
    // uprighter)).withTimeout(2.75));

  }

}
