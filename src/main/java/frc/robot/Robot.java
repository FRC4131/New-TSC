/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;


import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;

public class Robot extends IterativeRobot {
  final int pcm = 61;
  
  int barrel = 0;

  boolean hasFired = false;

  Timer cannonTimer = new Timer();

  Spark l1 = new Spark(0);
  Spark l2 = new Spark(1);
  Spark r3 = new Spark(2);
  Spark r4 = new Spark(3);
  Spark turret = new Spark(4);
  
  SpeedControllerGroup R = new SpeedControllerGroup(r3,r4);
  SpeedControllerGroup L = new SpeedControllerGroup(l1,l2);
  
  DifferentialDrive myDrive = new DifferentialDrive(L,R);
  
  Hand LeftHand = GenericHID.Hand.kLeft;
  Hand RightHand = GenericHID.Hand.kRight;
  
  XboxController Controller = new XboxController(0);

  Solenoid barrelZero = new Solenoid(pcm, 0);
  Solenoid barrelOne = new Solenoid(pcm, 1);
  Solenoid barrelTwo = new Solenoid(pcm, 2);
  Solenoid barrelThree = new Solenoid(pcm, 3);
  Solenoid barrelFour = new Solenoid(pcm, 4);
  Solenoid barrelFive = new Solenoid(pcm, 5);

  Solenoid[] solenoidArray = {barrelZero, barrelOne, barrelTwo, barrelThree, barrelFour, barrelFive};

  @Override
  public void robotInit() {
    
  }

  @Override
  public void robotPeriodic() {
  }

  /**
   * This autonomous (along with the chooser code above) shows how to select
   * between different autonomous modes using the dashboard. The sendable
   * chooser code works with the Java SmartDashboard. If you prefer the
   * LabVIEW Dashboard, remove all of the chooser code and uncomment the
   * getString line to get the auto name from the text box below the Gyro
   *
   * <p>You can add additional auto modes by adding additional comparisons to
   * the switch structure below with additional strings. If using the
   * SendableChooser make sure to add them to the chooser code above as well.
   */
  @Override
  public void autonomousInit() {

  }

  /**
   * This function is called periodically during autonomous.
   */
  @Override
  public void autonomousPeriodic() {

  }

  /**
   * This function is called periodically during operator control.
   */
  @Override
  public void teleopPeriodic() {
    myDrive.arcadeDrive(Controller.getY(LeftHand), Controller.getX(RightHand));
    turretRotate();
    fire();
  }
  
  public void turretRotate() {
    if (Controller.getPOV() == 90) {
      turret.set(0.2);
    } else if (Controller.getPOV() == 270) {
      turret.set(-0.2);
    } else {
      turret.set(0);
    }
  }
  public void fire() {
    if (Controller.getBumper(RightHand) && !hasFired && cannonTimer.get() < 0.25) {
      solenoidArray[barrel].set(true);
      cannonTimer.start();
      hasFired = true;
    } else if (cannonTimer.get() >= 0.25) {
      cannonTimer.stop();
      cannonTimer.reset();
      hasFired = false;
      solenoidArray[barrel].set(false);
      barrel++;
    } else {
      return;
    }
   }

  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {
  }
}
