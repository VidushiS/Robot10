package Team4450.Robot10;

import edu.wpi.first.wpilibj.Talon;
import Team4450.Lib.*;
import edu.wpi.first.wpilibj.smartdashboard.*;

public class GearLifter {
	private final Robot				robot;
	private final Teleop			teleop;
	
	private final Talon			    gearPickupMotor = new Talon(1);
	private final ValveDA			gearLifter = new ValveDA(6);
	private final ValveDA			gearDeploy = new ValveDA(1,0); //in PCM 1 ports
	
	boolean gearDown = false;
	boolean gearIn = false;
	
	public GearLifter(Robot robot, Teleop teleop){
		this.robot = robot;
		this.teleop = teleop;
		
	}
	public void Dispose(){
		Util.consoleLog();
		if(gearPickupMotor != null) gearPickupMotor.free();
		if(gearLifter != null ) gearLifter.dispose();
		if(gearDeploy != null) gearDeploy.dispose();
		
	}
	public void startGearIn(){
		Util.consoleLog();
		
		gearPickupMotor.set(.5);
	}
	public void startGearOut(){
		Util.consoleLog();
		
		gearPickupMotor.set(-.2);
		
	}
	
	public void GearIn(){
		if(gearIn = false){
			Util.consoleLog();
			gearDeploy.SetA();
			
			gearIn = true;
		}
	}
	
    public void GearOut(){
		if(gearIn = true){
			Util.consoleLog();
			gearDeploy.SetB();
			
			gearIn = false;
		}
	}
	public void GearDown(){
		if(gearDown = false){
			Util.consoleLog();
			gearLifter.SetB();
			
			gearDown = true;
		}
	
		}
	
	public void GearUp(){
		if(gearDown = true){
			Util.consoleLog();
			gearLifter.SetA();
			
			gearDown = false;
		}
		
	}
	
}
