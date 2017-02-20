package Team4450.Robot10;

import Team4450.Lib.*;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.smartdashboard.*;


public class BallPickupMotor {
	private	Robot			robot;
	private final Talon		ballPickupMotor = new Talon(0);
	
	public boolean motorOn = false;
	
	public BallPickupMotor(Robot robot){
		Util.consoleLog();
		this.robot = robot;
		
	}
	public void Dispose(){
		Util.consoleLog();
		
		if(ballPickupMotor != null) ballPickupMotor.free();
	}
	public void shooterStart(){
		Util.consoleLog();
		ballPickupMotor.set(.5);
		
		motorOn = true;
		SmartDashboard.putBoolean("Ball Pickup Motor", motorOn);
	}
	public void shooterStop(){
		Util.consoleLog();
		ballPickupMotor.set(0);
		
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
