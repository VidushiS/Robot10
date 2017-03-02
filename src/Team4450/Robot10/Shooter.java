package Team4450.Robot10;

import Team4450.Lib.*;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.smartdashboard.*;
import com.ctre.CANTalon;


public class Shooter {
	private Robot			robot;
	
	private Talon			 powerMotor = new Talon(1);
	private Talon			 shooterMotor = new Talon(2);
	private Talon			 regulatorMotor= new Talon(3);
	
	public boolean			shooterOn = false;
	public boolean			feederOn = false;
	
	public Shooter(Robot robot){
		Util.consoleLog();
		this.robot = robot;
	}
	public void dispose(){
		Util.consoleLog();
		
		if(powerMotor != null) powerMotor.free();
		if(shooterMotor != null) shooterMotor.free();
		if(regulatorMotor != null) regulatorMotor.free();
	}
	public void shooterStart(){
		Util.consoleLog();
		
		powerMotor.set(.8);
		
		shooterOn = true;
		
		SmartDashboard.putBoolean("Shooter Motor", shooterOn);
	}
	public void shooterStop(){
		Util.consoleLog();
		
		powerMotor.set(0);
		
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
	public void Feeder(){
		Util.consoleLog();
		shooterMotor.set(.3);
		regulatorMotor.set(-.5);
		
		feederOn = true;
	}
	public void feedReverse(){
		Util.consoleLog();
		shooterMotor.set(-.2);
		
		feederOn = true;
	}
	public void stopFeeding(){
		Util.consoleLog();
		
		shooterMotor.set(0);
		regulatorMotor.set(0);
		
		feederOn = false;
	}
	public void feederCheck(){
		if(feederOn = true){
			Util.consoleLog("The feeder is on");
		}
		else Util.consoleLog("The feeder is off");
	}
	}

