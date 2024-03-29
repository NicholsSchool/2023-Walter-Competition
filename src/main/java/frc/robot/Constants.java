package frc.robot;

import com.revrobotics.CANSparkMax.IdleMode;

import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.trajectory.TrapezoidProfile.Constraints;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.controller.ArmFeedforward;
import edu.wpi.first.math.util.Units;
import java.lang.Math;

public final class Constants {

  // Intake/Lifter
  public static final class IntakeConstants {
    public static final double INTAKE_SPEED = 0.6;
    public static final double OUTTAKE_HIGH_SPEED = 1.0;
    public static final double OUTTAKE_LOW_SPEED = 0.6;
    public static final int INTAKE_PISTON_SOLENOID_CHANNEL = 1;
    public static final int LIFTER_PISTON_SOLENOID_CHANNEL = 2;
  }

  // Uprighter
  public static final class UprighterConstants {
    public static final double UPRIGHTER_SPEED = 0.6;
  }

  // Arm (Manipulator)
  public static final class ArmConstants {
    public static final double POSITION_00 = 0.0; // home position
    public static final double POSITION_01 = 0.83; // test position (parallel to ground)
    public static final double POSITION_02 = 3.14; // high scoring position
    public static final double POSITION_03 = 3.45; // human player position

    public static final double SOFT_LIMIT_REVERSE = -0.1;
    public static final double SOFT_LIMIT_FORWARD = 4.5;

    public static final int ARM_LEFT_LIMIT_SWITCH_DIO_CHANNEL = 0;
    public static final int ARM_RIGHT_LIMIT_SWITCH_DIO_CHANNEL = 2;

    public static final int ARM_CURRENT_LIMIT = 34;
    public static final double ARM_MANUAL_SCALED = 0.42;

    public static final double ARM_GEAR_RATIO = 1.0 / (224); // 1:224 planetary gear ratio
    public static final double POSITION_CONVERSION_FACTOR = ARM_GEAR_RATIO * 2.0 * Math.PI;
    public static final double VELOCITY_CONVERSION_FACTOR = ARM_GEAR_RATIO * 2.0 * Math.PI / 60.0;
    public static final double ARM_FREE_SPEED = 5676.0 * VELOCITY_CONVERSION_FACTOR;
    public static final double ARM_ZERO_COSINE_OFFSET = -Math.PI / 6;
    public static final ArmFeedforward ARM_FF = new ArmFeedforward(0.0, 0.4, 12.0 / ARM_FREE_SPEED, 0.0);
    public static final double ARM_DEFAULT_P = 0.79;
    public static final double ARM_DEFAULT_I = 0.02;
    public static final double ARM_DEFAULT_D = 0.0;
    public static final Constraints ARM_MOTION_CONSTRAINTS = new Constraints(0.66, 0.66);
  }

  // Gripper/Pincher (End Effector)
  public static final class GripperConstants {
    public static final double GRIPPER_SPEED = 0.45;
    public static final double GRIPPER_SPEED_OFFSET = 0.33;
    public static final int PINCHER_SOLENOID_CHANNEL = 3;
    public static final int GRIPPER_LIMIT_SWITCH_DIO_CHANNEL = 1;
  }

  // CAN IDs (Controller Area Network)
  public static final class CANID {
    public static final int REAR_RIGHT_DRIVING_SPARKMAX = 10;
    public static final int REAR_RIGHT_TURNING_SPARKMAX = 11;
    public static final int FRONT_RIGHT_DRIVING_SPARKMAX = 12;
    public static final int FRONT_RIGHT_TURNING_SPARKMAX = 13;
    public static final int FRONT_LEFT_DRIVING_SPARKMAX = 14;
    public static final int FRONT_LEFT_TURNING_SPARKMAX = 15;
    public static final int REAR_LEFT_DRIVING_SPARKMAX = 16;
    public static final int REAR_LEFT_TURNING_SPARKMAX = 17;
    public static final int LEFT_INTAKE_SPARKMAX = 20;
    public static final int RIGHT_INTAKE_SPARKMAX = 21;
    public static final int LEFT_UPRIGHTER_SPARKMAX = 22;
    public static final int RIGHT_UPRIGHTER_SPARKMAX = 23;
    public static final int GRIPPER_SPARKMAX = 24;
    public static final int ARM_SPARKMAX = 26;
  }

  // Swerve Drive (Drive Train)
  public static final class SwerveDriveConstants {
    public static final double DRIVETRAIN_WIDTH = Units.inchesToMeters(26.5);
    public static final double DRIVETRAIN_LENGTH = Units.inchesToMeters(26.5);

    public static final double VIRTUAL_LOW_GEAR_RATE = 0.66;
    public static final double VIRTUAL_HIGH_GEAR_RATE = 1.0;

    public static final double MAX_METERS_PER_SECOND = 4.8;
    public static final double MAX_ANGULAR_SPEED = 2 * Math.PI;

    public static final double ROTATIONAL_SLEW_RATE = 2.0; // percent per second (1 = 100%)

    public static final SwerveDriveKinematics SWERVE_DRIVE_KINEMATICS = new SwerveDriveKinematics(
        new Translation2d(DRIVETRAIN_LENGTH / 2, DRIVETRAIN_WIDTH / 2),
        new Translation2d(DRIVETRAIN_LENGTH / 2, -DRIVETRAIN_WIDTH / 2),
        new Translation2d(-DRIVETRAIN_LENGTH / 2, DRIVETRAIN_WIDTH / 2),
        new Translation2d(-DRIVETRAIN_LENGTH / 2, -DRIVETRAIN_WIDTH / 2));
  }

  // REV MAXSwerve Modules
  public static final class SwerveModuleConstants {
    public static final int DRIVING_MOTOR_PINION_TEETH = 12; // 12T, 13T, or 14T
    public static final double DRIVING_MOTOR_FREE_SPIN_RPM = 5676; // NEO 550s max RPM
    public static final double WHEEL_DIAMETER_IN_METERS = 0.0762; // 3 inch wheels

    public static final double DRIVING_P = 0.04;
    public static final double DRIVING_I = 0.0;
    public static final double DRIVING_D = 0.0;
    public static final double DRIVING_FF = 1.0; // later offset by free spin rate

    public static final double TURNING_P = 1.0;
    public static final double TURNING_I = 0.0;
    public static final double TURNING_D = 0.0;
    public static final double TURNING_FF = 0.0;

    public static final IdleMode DRIVING_MOTOR_IDLE_MODE = IdleMode.kBrake;
    public static final IdleMode TURNING_MOTOR_IDLE_MODE = IdleMode.kBrake;

    public static final int DRIVING_MOTOR_CURRENT_LIMIT = 24; // amps
    public static final int TURNING_MOTOR_CURRENT_LIMIT = 12; // amps
  }

  // Autonomous Constants
  public static final class AutoDistanceConstants {
    public static final double APRILTAG_TO_CHARGE_STATION_METERS = 2.65;
    public static final double APRILTAG_TO_END_OF_COMMUNITY_METERS = 3.9;
  }

}
