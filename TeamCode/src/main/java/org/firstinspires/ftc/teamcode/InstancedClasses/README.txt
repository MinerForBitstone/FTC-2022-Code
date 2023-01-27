// How to use these classes:

// YOU MUST IMPORT USING
import org.firstinspires.ftc.teamcode.InstancedClasses.*;

// Initialization:

Motors Motors = new Motors(DcMotor motorLeft, DcMotor motorRight, DcMotor arm, int[] heights, BNO055IMU imu, Servo clawLeft, Servo clawRight);
// Use null for nonexistent hardware.  IMU necessary for drive().

Button Name = new Button();

// Methods:

Motors.drive(double targetAngle, double forwardPower);
Motors.initArm(/* Gamepad gamepad */); // Calibration is currently manual.  Triggers for up/down, A to finish.
// Use call initArm during initialization phase before using moveArm
Motors.moveArm(int pos);
Button.buttonDown(boolean gamepad_buttonid); 
// Returns true only the moment the button is pressed


