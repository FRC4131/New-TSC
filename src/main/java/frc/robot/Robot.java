/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;

public class Robot extends TimedRobot {
    final int pcm = 61;
  
    int barrel = 0;

      boolean hasFired = false;

    Timer cannonTimer = new Timer();

    Spark l1 = new Spark(0);
    Spark l2 = new Spark(1);
    Spark r3 = new Spark(2);
    Spark r4 = new Spark(3);
    Spark turret = new Spark(4);
  
    SpeedControllerGroup r = new SpeedControllerGroup(r3,r4);
    SpeedControllerGroup l = new SpeedControllerGroup(l1,l2);
  
    DifferentialDrive drive = new DifferentialDrive(l,r);
  
    Hand leftHand = GenericHID.Hand.kLeft;
    Hand rightHand = GenericHID.Hand.kRight;
  
    XboxController controller = new XboxController(0);

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
    public void teleopInit() {
    }
    
    @Override
    public void teleopPeriodic() {
        drive.arcadeDrive(controller.getY(leftHand), controller.getX(rightHand));
        
        turretRotate();
        
        fire();
    }
  
    public void turretRotate() {
        if (controller.getPOV() == 90) {
            turret.set(0.2);
        } else if (controller.getPOV() == 270) {
            turret.set(-0.2);
        } else {
            turret.set(0);
        }
    }
  
    public void fire() {
        if (controller.getBumper(rightHand) && !hasFired && cannonTimer.get() < 0.25) {
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
}