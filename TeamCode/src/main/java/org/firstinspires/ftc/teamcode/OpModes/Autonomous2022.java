package org.firstinspires.ftc.teamcode.OpModes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.InstancedClasses.*;

public class Autonomous2022 extends LinearOpMode {
    private HardwareDevice webcam_1;
    
    private BNO055IMU imu;
    private DcMotor wheelFL;
    private DcMotor wheelFR;
    private DcMotor wheelRL;
    private DcMotor wheelRR;
    private DcMotor arm;
    private Servo claw; 
    private TouchSensor limit;
    
    public double fwdPower;
    String signal;
    int tol = 5;
    double angTol = .5;
    int enc = 0;
    int angle = 0;

    Motors Motors;

    public void Run() {
        // Override this method to run the commands below.
    }
    // The below methods are behavior commands.
    public void Move(double tiles, boolean sideways, int adjAngle, int pos, boolean grab) {
        int target = (int) Math.round(tiles * 5000);
        angle += adjAngle;
        while (Math.abs(angle) > 180) {
            if (angle > 180) {
                angle -= 360;
            } else if (angle < -180) {
                angle += 360;
            }
        }

        // Run until position criteria are within tolerances of target(s)
        while (!((target - tol) < enc && enc < (target + tol) && (angle - angTol) < Motors.getAngle() && Motors.getAngle() < (angle + angTol)) && opModeIsActive()) {
            fwdPower = Double.valueOf(target - enc) / 1000;
            
            Motors.moveArm(pos, grab, 1, 500);
            enc =
            Motors.drive(angle, fwdPower, 0, sideways);
            
            telemetry.addData("Encoder", target - enc);
            telemetry.addData("Angle", Motors.getAngle());
            telemetry.update();
            
            
        }
        
        Motors.resetEncoders();
        enc = 0;
    }
    // End of commands list

    @Override
    public void runOpMode() {
        
        telemetry.addData("Status", "INITIALIZING: PLEASE WAIT");
        telemetry.update();
        

        imu = hardwareMap.get(BNO055IMU.class, "imu");
        wheelFL = hardwareMap.get(DcMotor.class, "wheelFL");
        wheelFR = hardwareMap.get(DcMotor.class, "wheelFR");
        wheelRL = hardwareMap.get(DcMotor.class, "wheelRL");
        wheelRR = hardwareMap.get(DcMotor.class, "wheelRR");
        arm = hardwareMap.get(DcMotor.class, "spool");
        claw = hardwareMap.get(Servo.class, "claw");
        limit = hardwareMap.get(TouchSensor.class, "calibrateSwitch");
        
        Motors = new Motors(wheelFL, wheelFR, wheelRL, wheelRR, imu,  new int[]{0,500,2700,4600,6500}, arm, claw);
        TensorVuforia tv = new TensorVuforia(hardwareMap.get(WebcamName.class, "Webcam 1"), false);
        
        Motors.initArm(gamepad1, limit);
        
        while(opModeInInit()) {
            telemetry.addData("Status", "Initialized. Press Play to begin");
            telemetry.addData("Signal", tv.tfid());
            telemetry.update();
        }
        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        telemetry.addData("Status", "Running");
        
        signal = tv.tfid();
        String found = "";
        
        // run until the end of the match (driver presses STOP)
        Run();
    }
}
