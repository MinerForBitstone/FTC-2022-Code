package org.firstinspires.ftc.teamcode.OpModes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
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


@TeleOp

public class Tele2022 extends LinearOpMode {
    
    private BNO055IMU imu;
    private DcMotor wheelFL;
    private DcMotor wheelFR;
    private DcMotor wheelRL;
    private DcMotor wheelRR;
    private DcMotor arm;
    private Servo claw;
    
    private TouchSensor limit;
    
    @Override
    public void runOpMode() {
        
        telemetry.addData("Status", "INITIALIZING: PLEASE WAIT");
        telemetry.update();
        
        int angle = 0;
        int pos = 0;
        int overrideHeight = 500;
        

        imu = hardwareMap.get(BNO055IMU.class, "imu");
        wheelFL = hardwareMap.get(DcMotor.class, "wheelFL");
        wheelFR = hardwareMap.get(DcMotor.class, "wheelFR");
        wheelRL = hardwareMap.get(DcMotor.class, "wheelRL");
        wheelRR = hardwareMap.get(DcMotor.class, "wheelRR");
        arm = hardwareMap.get(DcMotor.class, "spool");
        claw = hardwareMap.get(Servo.class, "claw");
        
        claw.scaleRange(0, 1);
        
        limit = hardwareMap.get(TouchSensor.class, "calibrateSwitch");

        // Motor Management and Driving
        Motors Motors = new Motors(wheelFL, wheelFR, wheelRL, wheelRR, imu,  new int[]{0,500,2700,4600,6500}, arm, claw);

        // Advanced Button Control
        Button RB = new Button();
        Button LB = new Button();
        Button A = new Button();
        Button B = new Button();
        Button X = new Button();
        Button Y = new Button();
        Button Dup = new Button();
        Button Ddown = new Button();
        
        Motors.initArm(gamepad1, limit);
        
        telemetry.addData("Status", "Initialized. Press Play to begin");
        telemetry.update();
        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            if (RB.buttonDown(gamepad1.right_bumper)) angle -= 45;
            if (LB.buttonDown(gamepad1.left_bumper)) angle += 45;
            angle -= gamepad1.right_stick_x;
            
            if (B.buttonDown(gamepad1.b)) pos = 0;
            if (A.buttonDown(gamepad1.a)) pos++;
            if (X.buttonDown(gamepad1.x)) pos--;
            
            if (Dup.buttonDown(gamepad1.dpad_up)) overrideHeight += 500;
            if (Ddown.buttonDown(gamepad1.dpad_down)) overrideHeight -= 500;
            
            Motors.moveArm(pos, gamepad1.right_trigger > 0.1, 1, overrideHeight);

            // Make sure the bottom position is correct. Probably not necessary.
            if ((pos == 0) && (!limit.isPressed())) {
                arm.setTargetPosition(arm.getTargetPosition() - 1);
            }

            double fwdPower = -gamepad1.left_stick_y;
            double sidePower = -gamepad1.left_stick_x;
            
            telemetry.addData("X", sidePower);
            telemetry.addData("Y", fwdPower);
            Motors.drive(angle, fwdPower, sidePower);
            
            telemetry.addData("Status", "Running");
            telemetry.addData("Arm Position", pos);
            telemetry.update();

        }
    }
}
