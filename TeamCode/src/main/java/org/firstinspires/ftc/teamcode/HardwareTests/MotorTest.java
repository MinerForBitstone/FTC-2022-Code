package org.firstinspires.ftc.teamcode.HardwareTests;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.InstancedClasses.*;


@TeleOp

public class MotorTest extends LinearOpMode {
    
    private BNO055IMU imu;
    private DcMotor wheelFL;
    private DcMotor wheelFR;
    private DcMotor wheelRL;
    private DcMotor wheelRR;
    private DcMotor arm;
    
    private TouchSensor limit;
    
    @Override
    public void runOpMode() {
        
        imu = hardwareMap.get(BNO055IMU.class, "imu");
        wheelFL = hardwareMap.get(DcMotor.class, "wheelFL");
        wheelFR = hardwareMap.get(DcMotor.class, "wheelFR");
        wheelRL = hardwareMap.get(DcMotor.class, "wheelRL");
        wheelRR = hardwareMap.get(DcMotor.class, "wheelRR");
        arm = hardwareMap.get(DcMotor.class, "spool");

        
        
        limit = hardwareMap.get(TouchSensor.class, "calibrateSwitch");
        
        Motors Motors = new Motors(wheelFL, wheelFR, wheelRL, wheelRR, imu,  new int[]{0,100,200,300}, arm, null);
        
        Motors.initArm(gamepad1, limit);
        
        imu.initialize(new BNO055IMU.Parameters());
        
        telemetry.addData("Status", "Initialized");
        telemetry.update();
        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            if (gamepad1.dpad_up) wheelFL.setPower(1); else wheelFL.setPower(0);
            if (gamepad1.dpad_right) wheelFR.setPower(1); else wheelFR.setPower(0);
            if (gamepad1.dpad_down) wheelRR.setPower(1); else wheelRR.setPower(0);
            if (gamepad1.dpad_left) wheelRL.setPower(1); else wheelRL.setPower(0);
            
            
            telemetry.addData("Status", "Running");
            telemetry.update();

        }
    }
}
