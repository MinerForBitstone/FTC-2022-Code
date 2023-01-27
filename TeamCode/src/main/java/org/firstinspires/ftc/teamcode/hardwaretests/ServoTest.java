package org.firstinspires.ftc.teamcode.hardwaretests;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

@TeleOp

public class ServoTest extends LinearOpMode {

    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();
    private Servo claw;

    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        // Initialize the hardware variables. Note that the strings used here as parameters
        // to 'get' must correspond to the names assigned during the robot configuration
        // step (using the FTC Robot Controller app on the phone).
        claw  = hardwareMap.get(Servo.class, "claw");

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();
        
        double servoPos = 0;

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            
            claw.setPosition(servoPos);
            
            if (gamepad1.dpad_up) servoPos += 0.0001;
            if (gamepad1.dpad_down) servoPos -= 0.0001;
            
            if (servoPos > 1) servoPos = 1;
            if (servoPos < 0) servoPos = 0;
            
            // Show the elapsed game time and wheel power.
            telemetry.addData("Servo", servoPos);
            telemetry.update();
        }
    }
}
