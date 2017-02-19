package org.usfirst.frc.team4528.robot;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Ultrasonic;
import edu.wpi.cscore.AxisCamera;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;

public class Robot extends SampleRobot {
	
	private static final int LEFT_FRONT_DRIVE = 5;
	private static final int LEFT_REAR_DRIVE = 3;
	private static final int RIGHT_FRONT_DRIVE = 2;
	private static final int RIGHT_REAR_DRIVE = 0;
	private static final int CLIMBER_PART1 = 6;
	private static final int CLIMBER_PART2 = 7;
	private static final int GEAR_BOX_PART1 = 3;
	private static final int GEAR_BOX_PART2 = 4;
	private static final int ENHANCED_LEFT_DRIVE = 4;
	private static final int ENHANCED_RIGHT_DRIVE = 1;
	
	private final String pos1 = "left"; //autonomous chooser
	private final String pos2 = "center";
	private final String pos3 = "right";


                static Victor driveLeftFront, driveLeftRear, driveRightFront, driveRightRear,enhancedDriveLeft,enhancedDriveRight,climber1,climber2;
                Joystick stick;

                RobotDrive Skylar,OptimusPrime;
                DoubleSolenoid gearBox;
                static Ultrasonic vexSensorBackLeft, vexSensorBackRight, vexSensorFrontLeft,vexSensorFrontRight;
                Boolean slowMode = false;
                double leftDrive, rightDrive;
                long timer;
                UsbCamera msLifeCam;
                SendableChooser<String> chooser= new SendableChooser<>();
       
                public Robot() {
                	
                                stick = new Joystick(0);
                                driveLeftFront = new Victor(LEFT_FRONT_DRIVE);
                                driveLeftRear = new Victor(LEFT_REAR_DRIVE);
                                driveRightFront = new Victor(RIGHT_FRONT_DRIVE);
                                driveRightRear = new Victor(RIGHT_REAR_DRIVE);
                                enhancedDriveLeft = new Victor(ENHANCED_LEFT_DRIVE);
                                enhancedDriveRight = new Victor(ENHANCED_RIGHT_DRIVE);
                                gearBox = new DoubleSolenoid(GEAR_BOX_PART1,GEAR_BOX_PART2); 
                                climber1= new Victor(CLIMBER_PART1);
                                climber2= new Victor(CLIMBER_PART2);
                                vexSensorBackLeft = new Ultrasonic(0,1);
                                vexSensorBackRight = new Ultrasonic(2,3);
                                vexSensorFrontLeft = new Ultrasonic(4,5);
                                vexSensorFrontRight = new Ultrasonic(6,7);
                                
                                Skylar = new RobotDrive(driveLeftFront, driveLeftRear, driveRightFront, driveRightRear);
                                OptimusPrime = new RobotDrive(enhancedDriveLeft,enhancedDriveRight);
                                
                                
                }

               
                public void robotInit() {
                	Skylar.setExpiration(0.5);
                	OptimusPrime.setExpiration(0.5);
                    vexSensorBackLeft.setEnabled(true);
                    vexSensorBackLeft.setAutomaticMode(true);
                    vexSensorBackRight.setEnabled(true);
                    vexSensorBackRight.setAutomaticMode(true);
                    vexSensorFrontLeft.setEnabled(true);
                    vexSensorFrontLeft.setAutomaticMode(true);
                    vexSensorFrontRight.setEnabled(true);
                    vexSensorFrontRight.setAutomaticMode(true);
                    
                    
                    chooser.addDefault(pos2, pos2);
                    chooser.addObject(pos1, pos1);
                    chooser.addObject(pos3, pos3);
                	
                    SmartDashboard.putData("Auto Mode", chooser);
                    
                }
                public void autonomousInit(){
                
                }
      
 
       public void autonomous() {  
    	   String choose = chooser.getSelected();
    	   Skylar.setSafetyEnabled(false);
    	   OptimusPrime.setSafetyEnabled(false);
    	   switch(choose){
    	   case pos1:
    		   
    	   case pos2:
    		   while(isAutonomous() && isEnabled()){
    			   timer = System.currentTimeMillis();
    			   while(System.currentTimeMillis() - timer <= 2000){
    				   adjustedDrive(0.5,0.5);
    			   }
    			   smashDrive(0,0);
    			   getSensorData();
    			   gearBox.set(Value.kReverse);
    			   Timer.delay(2);
    			   timer = System.currentTimeMillis();
    			   while(System.currentTimeMillis() - timer <= 1000){
    				   adjustedDrive(-0.5,-0.5);
    			   }
    			   smashDrive(0,0);
    			   getSensorData();
    			   gearBox.set(Value.kForward);
    		   }
    			   break;   
    	   case pos3:
    	   }	   
       }  
    	  
       
       public void operatorControl() {
    	  
             Skylar.setSafetyEnabled(true);
             OptimusPrime.setSafetyEnabled(true);
        
             while (isOperatorControl() && isEnabled()) { 
             climb();  //a to climb, b to release
             slowMode();//x to enter, y to quit // slowMode 
             gearBox(); //left bumper to open, right bumper to close
             drive(); 
			 getSensorData();
			 SmartDashboard.putNumber("SpeedLeftFront", driveLeftFront.getSpeed());
			 SmartDashboard.putNumber("SpeedRightFront", driveRightFront.getSpeed());
			 Timer.delay(0.001);
              }
       }
       public void drive(){
    	   if(slowMode==false){
               leftDrive = stick.getRawAxis(1) ;
               rightDrive = stick.getRawAxis(5);
               smashDrive(-leftDrive, -rightDrive);
               }
    	   else{
      	       leftDrive = stick.getRawAxis(1) ;
               rightDrive = stick.getRawAxis(5);
               smashDrive(-leftDrive * 1.5, -rightDrive * 1.5);
        }     
       }
       public void gearBox(){
    	   if(stick.getRawButton(5)){
    		   gearBox.set(Value.kReverse);
    	   }
    	   if(stick.getRawButton(6)){
    		   gearBox.set(Value.kForward);
    	   }
       }
       public void climb(){
    	  
    	   if(stick.getRawButton(1)==true){ //climb
    		  climber1.set(1.0);
         	  climber2.set(1.0);
         	  
           }
           else if(stick.getRawButton(2)==true){ //release
        	   climber1.set(-1.0);
        	   climber2.set(-1.0);
        	           	   
           }
           else 
           {
         	  climber1.set(0); //release button to stop climb
         	  climber2.set(0);
           }
       }
       public void slowMode(){
    	   if(stick.getRawButton(3)==true){
          	  slowMode = true;
            }
            else if(stick.getRawButton(4)==true){
          	  slowMode = false;
       }
       
}
       public void getSensorData(){
    	      SmartDashboard.putNumber("SensorBackLeft", vexSensorBackLeft.getRangeInches());
			  SmartDashboard.putNumber("SensorBackRight",vexSensorBackRight.getRangeInches());
			  SmartDashboard.putNumber("SensorFrontLeft",vexSensorFrontLeft.getRangeInches());
			  SmartDashboard.putNumber("SensorFrontRight",vexSensorFrontRight.getRangeInches());
       }
       public void smashDrive(double left, double right){
    	  Skylar.tankDrive(left, right);
    	  OptimusPrime.tankDrive(left, right);
       }
       public void adjustedDrive(double left, double right){
    	   while(isAutonomous() && isEnabled()){
    		   if(vexSensorBackLeft.getRangeInches() - vexSensorBackRight.getRangeInches() >= 2){
    			   smashDrive(left,right*1.2);
    		   }
    		   smashDrive(left,right*1.1);
    	   }
       }
}

