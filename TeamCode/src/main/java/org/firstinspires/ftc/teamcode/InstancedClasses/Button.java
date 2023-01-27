package org.firstinspires.ftc.teamcode.InstancedClasses;

import com.qualcomm.robotcore.hardware.Gamepad;

/* 
This class processes button presses (rising/falling edge of button presses, etc.)
These require the target button return value to be passed into its methods (boolean buttonPressed).  
Create a new instance of this class for every button.
*/

public class Button {
  
  private boolean oldPressed = false;
  
  public boolean buttonDown(boolean buttonPressed) {
    boolean passed = (buttonPressed && !oldPressed);
    oldPressed = buttonPressed;
    return passed;
  }
  
  public boolean buttonUp(boolean buttonPressed) {
    boolean passed = (!buttonPressed && oldPressed);
    oldPressed = buttonPressed;
    return passed;
  }
}
