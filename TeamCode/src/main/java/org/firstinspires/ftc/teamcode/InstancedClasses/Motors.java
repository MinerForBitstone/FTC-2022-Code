package org.firstinspires.ftc.teamcode.InstancedClasses;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.hardware.bosch.BNO055IMU;


public class Motors {
  
  private final double clawClosed = .43;
  private final double clawOpen = .05;
  
  public DcMotor motorFL;
  public DcMotor motorFR;
  public DcMotor motorRL;
  public DcMotor motorRR;
  public DcMotor arm;
  public int[] heights;
  public Servo claw;
  public BNO055IMU imu;
  public DcMotor[] driveMotors;
  private int aveEnc = 0;
  
  BNO055IMU.Parameters params;
  
  /* This class handles all motion of the robot */
  
  public Motors(DcMotor motorFL, DcMotor motorFR, DcMotor motorRL, DcMotor motorRR, BNO055IMU imu, int[] heights, DcMotor arm, Servo claw) {
    this.motorFL = motorFL; this.motorFR = motorFR; this.motorRL = motorRL; this.motorRR = motorRR; this.arm = arm; this.heights = heights; this.imu =imu; this.claw = claw; 
    driveMotors = new DcMotor[]{motorFL, motorFR, motorRL, motorRR};
    
    for (DcMotor motor : driveMotors) motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    
    params = new BNO055IMU.Parameters();
    params.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
    imu.initialize(params);
  }
  
  public void resetEncoders() {
    for (DcMotor motor : driveMotors) motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
  }
  
  public double getAngle() {
    return imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES).firstAngle;
  }
  
  // Drive the robot with angle correction.  Also used for steering.
  public int drive(double targetAng, double fwdPower, double sidePower, boolean sideways) {
    
    for (DcMotor motor : driveMotors) motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    if (sidePower > 1) sidePower = 1;
    if (fwdPower > 1) fwdPower = 1;

    DcMotor[] driveConfig;

    if (sideways) {
      driveConfig = new DcMotor[]{motorFR, motorRR, motorFL, motorRL};
    } else {
      driveConfig = new DcMotor[]{motorFL, motorFR, motorRL, motorRR};
    }
    
    //for (DcMotor motor : driveMotors) motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    
    DcMotor[] motorsLeft = new DcMotor[]{driveConfig[0], driveConfig[2]};
    DcMotor[] motorsRight = new DcMotor[]{driveConfig[1], driveConfig[3]};
    
    for (DcMotor motor : motorsLeft) motor.setDirection(DcMotor.Direction.REVERSE);
    for (DcMotor motor : motorsRight) motor.setDirection(DcMotor.Direction.FORWARD);
    
    
    // Get current robot angle
    double zAngle = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES).firstAngle;
     
     // Find the shortest path to the angle
    while (Math.abs(zAngle - targetAng) > 180) {
      if (zAngle - targetAng > 180) {
        targetAng += 360;
      } else if (zAngle - targetAng < -180) {
        targetAng -= 360;
      }
    }
    
    // Move the robot
    double rotPower = (targetAng - zAngle) / 10;
    
    motorFL.setPower(fwdPower - sidePower - rotPower);
    motorFR.setPower(fwdPower + sidePower + rotPower);
    motorRL.setPower(fwdPower + sidePower - rotPower);
    motorRR.setPower(fwdPower - sidePower + rotPower);
    for (DcMotor motor : driveMotors) aveEnc += motor.getCurrentPosition();
    aveEnc /= 4;
    return aveEnc;
    
  }
  
  public int drive(double targetAng, double fwdPower, double sidePower) {
    return drive(targetAng, fwdPower, sidePower, false);
  }
  
  public void initArm(Gamepad gamepad, TouchSensor limit) {
    claw.setPosition(clawClosed);
    
    arm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    while (!limit.isPressed() && !gamepad.a) {
      arm.setPower(-0.5);
    }
    
    arm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    arm.setTargetPosition(0);
    arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    arm.setPower(1);
    
    
  }
  
  // Interact with the robot arm. Use initArm to calibrate first
  boolean oldGrab = false;
  public int moveArm(int pos, boolean grab, int maxNoDropPos, int pos1Override) {
    // Stop it from crashing if pos is out of bounds
    if (pos > heights.length - 1) pos = heights.length - 1;
    if (pos < 0) pos = 0;
    
    int offset = 0;
    if (pos1Override < 0) pos1Override = 0;
    
    if (!grab) claw.setPosition(clawClosed); else claw.setPosition(clawOpen);
    
    boolean drop = !grab && oldGrab;
    oldGrab = grab;
    
    // Move the arm to a preset position from the heights array
    if (drop && (pos > maxNoDropPos)) offset = 700;
    if (pos != 1) arm.setTargetPosition(heights[pos] - offset);
    else arm.setTargetPosition(pos1Override);
    int timer = 0;
    if (drop) while ((arm.getCurrentPosition() > arm.getTargetPosition()) && (timer < 5000)) timer++; 
    //arm.setTargetPosition(heights[pos]);
    return pos;
  }
}
