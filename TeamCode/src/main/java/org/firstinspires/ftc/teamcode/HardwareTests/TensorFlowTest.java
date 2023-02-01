/*
Copyright 2022 FIRST Tech Challenge Team 14683

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
associated documentation files (the "Software"), to deal in the Software without restriction,
including without limitation the rights to use, copy, modify, merge, publish, distribute,
sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial
portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
package org.firstinspires.ftc.teamcode.HardwareTests;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.Blinker;
import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.InstancedClasses.*;

@Autonomous

public class TensorFlowTest extends LinearOpMode {
    private Blinker control_Hub;
    private Blinker expansion_Hub_1;
    private HardwareDevice webcam_1;
    private TouchSensor calibrateSwitch;
    private Servo claw;
    private DistanceSensor distanceRL;
    private DistanceSensor distanceRR;
    private BNO055IMU imu;
    private DcMotor arm;
    private DcMotor wheelFL;
    private DcMotor wheelFR;
    private DcMotor wheelRL;
    private DcMotor wheelRR;


    @Override
    public void runOpMode() {
        control_Hub = hardwareMap.get(Blinker.class, "Control Hub");
        expansion_Hub_1 = hardwareMap.get(Blinker.class, "Expansion Hub 1");
        webcam_1 = hardwareMap.get(HardwareDevice.class, "Webcam 1");
        calibrateSwitch = hardwareMap.get(TouchSensor.class, "calibrateSwitch");
        claw = hardwareMap.get(Servo.class, "claw");
        distanceRL = hardwareMap.get(DistanceSensor.class, "distanceRL");
        distanceRR = hardwareMap.get(DistanceSensor.class, "distanceRR");
        imu = hardwareMap.get(BNO055IMU.class, "imu");
        arm = hardwareMap.get(DcMotor.class, "spool");
        wheelFL = hardwareMap.get(DcMotor.class, "wheelFL");
        wheelFR = hardwareMap.get(DcMotor.class, "wheelFR");
        wheelRL = hardwareMap.get(DcMotor.class, "wheelRL");
        wheelRR = hardwareMap.get(DcMotor.class, "wheelRR");
        
        imu.initialize(new BNO055IMU.Parameters());
        
        Motors Motors = new Motors(wheelFL, wheelFR, wheelRL, wheelRR, imu,  new int[]{0,2600,4600,6600}, arm, null);
        
        TensorVuforia tv = new TensorVuforia(hardwareMap.get(WebcamName.class, "Webcam 1"), true);
        
        Button A = new Button();
        
        telemetry.addData("Status", "Initialized");
        telemetry.update();
        
        //tv.toggle();
        
        //boolean tfActive = true;
        
        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        double angle = 0;
        String[] targets = new String[]{
            "Red Audience Wall",
            "Blue Audience Wall",
            "Red Rear Wall",
            "Blue Rear Wall"
        };
        
        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            if (A.buttonDown(gamepad1.a)) tv.toggle();
            telemetry.addData("Status", "Running");
            telemetry.addData("TFod", tv.tensorIsActive);
            telemetry.addData("Image", tv.tfid());
            for (String target:targets) {
                telemetry.addData(target, 
                    tv.vutrack(target)[0] + ", " +
                    tv.vutrack(target)[1] + ", " + 
                    tv.vutrack(target)[2] + ", " +
                    tv.vutrack(target)[3]); 
            }
            telemetry.update();

        }
    }
}
