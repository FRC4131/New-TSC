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
    boolean forward;
    boolean backward;

    Timer cannonTimer = new Timer();

    Spark l1 = new Spark(2);
    Spark l2 = new Spark(5);
    Spark r1 = new Spark(3);
    Spark r2 = new Spark(4);
    Spark turretRotation = new Spark(1);
    Spark turretElev = new Spark(6);

    DigitalInput forwardStop = new DigitalInput(0);
    DigitalInput backwardStop = new DigitalInput(1);

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

    AnalogInput pressureSensor = new AnalogInput(0);

    @Override
    public void robotInit() {
    }

    @Override
    public void teleopInit() {
        cannonTimer.start();
        turretElev.set(0);
        turretRotation.set(0);
    }

    @Override
    public void teleopPeriodic() {
        System.out.println(forward);
        System.out.println(backward);

        drive();
        turretRotate();
        elevation();
        fire();
    }

    public void drive() {
        double straight = -controller.getY(leftHand);
        double rotate = controller.getX(rightHand);
        forward = forwardStop.get();
        backward = backwardStop.get();

        l1.set(rotate + straight);
        l2.set(rotate + straight);

        r1.set(rotate - straight);
        r2.set(rotate - straight);
        SmartDashboard.putNumber("Pressure Sensor:", 50*pressureSensor.getVoltage()-24);//put val to smdb
    }

    public void elevation() {
        if (controller.getPOV() == 0 && backward){
            turretElev.set(0.6);
        } else if (controller.getPOV() == 180 && forward){
            turretElev.set(-0.6);
        } else {
            turretElev.set(0);
        }
    }
    public void turretRotate() {
        if (controller.getPOV() == 90) {
            turretRotation.set(0.8); //turret rather than turretrotation for old version
        } else if (controller.getPOV() == 270) {
            turretRotation.set(-0.8);
        } else {
            turretRotation.set(0);
        }
    }
    public void fire() {
        if (controller.getBumper(rightHand) && !hasFired && cannonTimer.get() > 1) { //make sure reloaded, hasn't just fired, and has had time to swivel/reload
            cannonTimer.stop();
            cannonTimer.reset();
            solenoidArray[barrel].set(true);
            cannonTimer.start();
            hasFired = true;
        } else if (cannonTimer.get() >= 0.95 && hasFired) { //has had time to fire, so switch barrel
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
            return; //stop doing things
        }
    }
}
