/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.cscore.VideoSink;
import edu.wpi.cscore.VideoSource.ConnectionStrategy;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PWMVictorSPX;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;

/**
 * This is just a simple prototype based on the Arcade Drive example. For the future
 * we will use the Command Robot.
 */
public class Robot extends TimedRobot {

  // Actuators
  private final SpeedControllerGroup m_leftMotor = new SpeedControllerGroup(new PWMVictorSPX(0), new PWMVictorSPX(1));
  private final SpeedControllerGroup m_rightMotor = new SpeedControllerGroup(new PWMVictorSPX(2), new PWMVictorSPX(3));
  private final DifferentialDrive m_robotDrive = new DifferentialDrive(m_leftMotor, m_rightMotor);
  private final PWMVictorSPX m_conveyorMotor = new PWMVictorSPX(4);
  private final PWMVictorSPX m_grappleMotor = new PWMVictorSPX(5);
  private final Servo m_doorServo = new Servo(6);
  private final Servo m_conveyorReleaseServo = new Servo(7);

  // Controls
  private final Joystick m_stick = new Joystick(0);
  JoystickButton AButton;
  JoystickButton BButton;
  JoystickButton XButton;
  JoystickButton YButton;

  // Video
  UsbCamera camera1;
  UsbCamera camera2;
  VideoSink server;

  @Override
  public void robotInit() {
    AButton = new JoystickButton(m_stick, 0);
    BButton = new JoystickButton(m_stick, 1);
    XButton = new JoystickButton(m_stick, 2);
    YButton = new JoystickButton(m_stick, 3);


    camera1 = CameraServer.getInstance().startAutomaticCapture(0);
    camera2 = CameraServer.getInstance().startAutomaticCapture(1);
    server = CameraServer.getInstance().getServer();
    camera1.setConnectionStrategy(ConnectionStrategy.kKeepOpen);
    camera2.setConnectionStrategy(ConnectionStrategy.kKeepOpen);
  }

  @Override
  public void teleopInit() {
    // Drop conveyor
    m_conveyorReleaseServo.setPosition(1.0);

    // Start conveyor
    m_conveyorMotor.setSpeed(0.2);

    super.teleopInit();
  }

  @Override
  public void disabledInit() {
    // Stop Grapple
    m_grappleMotor.setSpeed(0.0);

    // Stop conveyor
    m_conveyorMotor.setSpeed(0.0);

    // Lock conveyor?
    //m_conveyorReleaseServo.setPosition(0.0);

    super.disabledInit();
  }

  @Override
  public void teleopPeriodic() {
    if(AButton.get()) {
      // Door open
      m_doorServo.setPosition(1.0);
    }

    if(BButton.get()) {
      // Door close
      m_doorServo.setPosition(0.0);
    }

    if(YButton.get()) {
      // Extend
      m_grappleMotor.setSpeed(0.5);
    }

    if(XButton.get()) {
      // Retract
      m_grappleMotor.setSpeed(-0.5);
    }

    // Drive with arcade drive.
    // That means that the Y axis drives forward
    // and backward, and the X turns left and right.
    m_robotDrive.arcadeDrive(m_stick.getY(), m_stick.getX());
  }

  @Override
  public void autonomousPeriodic() {
    // Drive forward slowly
    m_robotDrive.arcadeDrive(0.1, 0.0);  
  }
}
