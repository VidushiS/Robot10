package Team4450.Robot10;

import Team4450.Lib.*;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.smartdashboard.*;


public class BallPickupMotor {
	private	Robot			robot;
	private Spark			ballPickupSpark = new Spark(0);
	
	public boolean motorOn = false;
	
	public BallPickupMotor(Robot robot){ //when creating a constructor method make sure the name is the same as the class
		Util.consoleLog();
		this.robot = robot;
		
	}
	public void Dispose(){
		Util.consoleLog();
		
		if(ballPickupSpark != null) ballPickupSpark.free();
	}
	public void shooterStart(){
		Util.consoleLog();
		ballPickupSpark.set(-.5);
		
		motorOn = true;
		SmartDashboard.putBoolean("Ball Pickup Motor", motorOn);
	}
	public void shooterStop(){
		Util.consoleLog();
		ballPickupSpark.set(0);
		
		motorOn = false;
		SmartDashboard.putBoolean("Ball Pickup Motor", motorOn);
	}
	public void shooterCheck(){
		Util.consoleLog();
		
		if(motorOn = true){
			Util.consoleLog("The Ball Pickup Motor is on");
			return;
		}
		else Util.consoleLog("The Ball Pickup Motor is off");
			 return;
	}
	
}
