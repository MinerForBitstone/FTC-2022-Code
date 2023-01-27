package org.firstinspires.ftc.teamcode.InstancedClasses;

import org.firstinspires.ftc.robotcore.external.tfod.Tfod;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaBase;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import java.util.List;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaCurrentGame;


public class TensorVuforia {
  private Tfod tfod;
  private VuforiaCurrentGame vuforiaPOWERPLAY;
  public boolean tensorIsActive;
  
  public TensorVuforia(WebcamName webcam, boolean startWithVu) {
    Recognition recognition;
    tfod = new Tfod();
    vuforiaPOWERPLAY = new VuforiaCurrentGame();
    tfod.useDefaultModel();
    vuforiaPOWERPLAY.initialize(
        "", // vuforiaLicenseKey
        webcam, // cameraName
        "", // webcamCalibrationFilename
        false, // useExtendedTracking
        true, // enableCameraMonitoring
        VuforiaLocalizer.Parameters.CameraMonitorFeedback.AXES, // cameraMonitorFeedback
        0, // dx
        0, // dy
        0, // dz
        AxesOrder.XZY, // axesOrder
        90, // firstAngle
        90, // secondAngle
        0, // thirdAngle
        true); // useCompetitionFieldTargetLocations
    tfod.initialize(vuforiaPOWERPLAY, (float) 0.4, true, true);
    tensorIsActive = !startWithVu;
    if (startWithVu) vuforiaPOWERPLAY.activate(); 
    else tfod.activate();
  }
  
  public boolean toggle() {
    if (tensorIsActive) {
      tfod.deactivate();
      vuforiaPOWERPLAY.activate();
    } else {
      vuforiaPOWERPLAY.deactivate();
      tfod.activate();
    }
    tensorIsActive = !tensorIsActive;
    return tensorIsActive;
  }
  
  public String tfid() {
    String label = "";
    if (tensorIsActive) {
      float confidence = 0;
      List<Recognition> recognitions = tfod.getRecognitions();
      int i = 0;
      for (Recognition recognition : recognitions) {
        if (recognition.getConfidence() > confidence) {
          label = recognition.getLabel();
          confidence = recognition.getConfidence();
        }
      }
    } else label = "TFOD is inactive!";
    return label;
  }
  
  public Object[] vutrack(String target) {
    VuforiaBase.TrackingResults mark = vuforiaPOWERPLAY.track(target);
    return(new Object[]{mark.isVisible, mark.x, mark.y, mark.z});
  }
}
