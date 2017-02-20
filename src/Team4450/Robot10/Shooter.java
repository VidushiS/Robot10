package Team4450.Robot10;

import Team4450.Lib.*;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.smartdashboard.*;

public class Shooter {
	private Robot			robot;
	
	private Talon			shooterMotor1 = new Talon(2);
	private Talon			shooterMotor2 = new Talon(3);
	
	public boolean			shooterOn = false;
	
	public Shooter(Robot robot){
		Util.consoleLog();
		this.robot = robot;
	}
	public void dispose(){
		Util.consoleLog();
		
		if(shooterMotor1 != null) shooterMotor1.free();
		if(shooterMotor2 != null) shooterMotor2.free();
	}
	public void shooterStart(){
		Util.consoleLog();
		
		shooterMotor1.set(.5);
		shooterMotor2.set(.5);
		
		shooterOn = true;
		
		SmartDashboard.putBoolean("Shooter Motor", shooterOn);
	}
	public void shooterStop(){
		Util.consoleLog();
		
		shooterMotor1.set(0);
		shooterMotor2.set(0);
		
		shooterOn = false;
		
		SmartDashboard.putBoolean("Shooter Motor", shooterOn);
	}
	public void shooterCheck(){
		
		if(shooterOn = true){
			Util.consoleLog("The Shooter Motor is on");
			return;
		}
		else if(shooterOn = false){
			Util.consoleLog("The Shooter Motor is off");
			return;
		}
	}
}
