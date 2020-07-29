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
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends TimedRobot {

    final int pcm = 61;

    int barrel = 0;

    boolean hasFired = false;

    Timer cannonTimer = new Timer();

    Spark l1 = new Spark(2);
    Spark l2 = new Spark(5);
    Spark r1 = new Spark(3);
    Spark r2 = new Spark(4);
    Spark turret = new Spark(1);

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

    AnalogInput pressureSensor = new AnalogInput(0);//this is the pressure sensor -andrew

    @Override
    public void robotInit() {
    }

    @Override
    public void teleopInit() {
        cannonTimer.start();
    }

    @Override
    public void teleopPeriodic() {
        drive();

        turretRotate();

        fire();
    }

    public void drive() {
        double straight = -controller.getY(leftHand);
        double rotate = controller.getX(rightHand);

        l1.set(rotate + straight);
        l2.set(rotate + straight);

        r1.set(rotate - straight);
        r2.set(rotate - straight);
        SmartDashboard.putNumber("Pressure Sensor:", 50*pressureSensor.getValue()-25);//put val to smdb
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
        if (controller.getBumper(rightHand) && !hasFired && cannonTimer.get() > 1) {
            cannonTimer.stop();
            cannonTimer.reset();
            solenoidArray[barrel].set(true);
            cannonTimer.start();
            hasFired = true;
        } else if (cannonTimer.get() >= 0.25 && hasFired) {
            cannonTimer.stop();
            cannonTimer.reset();
            hasFired = false;
            solenoidArray[barrel].set(false);
            if (barrel == 5) {
                barrel = 0;
            } else {
                barrel++;
            }
            cannonTimer.start();
        } else {
            return;
        }
    }
}
