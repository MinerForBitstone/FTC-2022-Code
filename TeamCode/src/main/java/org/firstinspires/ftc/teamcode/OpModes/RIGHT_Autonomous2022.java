package org.firstinspires.ftc.teamcode.OpModes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous

public class RIGHT_Autonomous2022 extends Autonomous2022 {
    @Override
    public void Run() {
        Move(0.2, false, 0, 0, true);
        Move(-1.2, true, 0, 1, true);
        Move(1.2, false, 0, 4, true);
        
        Move(0, false, 45, 4, true);
        Move(.37, false, 0, 4, true); 
        Move(0, false, 0, 4, false);
        Move(-.37, false, 0, 4, false);
        
        if          (signal == 1) {
            Move(0, false, -45, 0, false);
            Move(0.5, false, 0, 0, false);
            
        } else if   (signal == 2) {
            Move(0, false, 45, 0, false);
            Move(-1, false, 0, 0, false);
            Move(0, false, -90, 0, false);
            Move(0.5, false, 0, 0, false);
            
        } else if   (signal == 3) {
            Move(0, false, 45, 0, false);
            Move(-2, false, 0, 0, false);
            Move(0, false, -90, 0, false);
            Move(0.5, false, 0, 0, false);
        }
    }
}