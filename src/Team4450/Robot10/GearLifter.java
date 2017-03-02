package Team4450.Robot10;

import com.ctre.CANTalon;
import edu.wpi.first.wpilibj.Timer;
import Team4450.Lib.*;
import edu.wpi.first.wpilibj.smartdashboard.*;
import java.io.IOException;
import Team4450.Lib.LCD;
import Team4450.Lib.Util;

public class GearLifter {
	private final Robot				robot;
	private final Teleop			teleop;

	private final CANTalon			gearPickupMotor = new CANTalon(1);
	private final ValveDA			gearLifter = new ValveDA(6);
	private final ValveDA			gearDeploy = new ValveDA(1,0); //in PCM 1 ports
	private Team4450.Robot10.GearLifter.AutoGearPickup					autoPickupThread;

	boolean gearMotor = false;
	boolean gearDown = false;
	boolean gearIn = true;
	boolean autoGear = false;

	public GearLifter(Robot robot, Teleop teleop){
		Util.consoleLog();
		this.robot = robot;
		this.teleop = teleop;

	}
	public void Dispose(){
		Util.consoleLog();
		if(gearPickupMotor != null) gearPickupMotor.delete();
		if(gearLifter != null ) gearLifter.dispose();
		if(gearDeploy != null) gearDeploy.dispose();

	}
	public void startGearIn(){
		Util.consoleLog();

		gearPickupMotor.set(.5);
		gearMotor = true;
	}
	public void startGearOut(){
		Util.consoleLog();

		gearPickupMotor.set(-.5);
		gearMotor = true;
	}
	public void stopGear(){
		Util.consoleLog();

		gearPickupMotor.set(0.0);
		gearMotor = false;
	}

	public void GearCheck(){
		if(gearMotor = true){
			Util.consoleLog("The Gear Pickup Motor is on.");
			return;
		}
		else Util.consoleLog("The Gear Pickup Motor is off.");
		return;
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
	public void AutoGearPickup(){
		Util.consoleLog();
		if (autoPickupThread != null){
			return;
		}
		autoPickupThread = new AutoGearPickup();
		autoPickupThread.run();
	}
	public void StopAutoGearPickup(){

		if (autoPickupThread != null) autoPickupThread.interrupt();

		autoPickupThread = null;
	}
	private class AutoGearPickup extends Thread
	{ 
		AutoGearPickup()
		{
			Util.consoleLog();
			this.setName("AutoGearPickup");
		}

		public void run(){
			Util.consoleLog();

			try{
				GearDown();
				Timer.delay(.5);
				GearOut();
				Timer.delay(.5);
				GearIn();
				Timer.delay(.5);
				GearUp();
				autoGear = true;

				while(!isInterrupted() & gearPickupMotor.getOutputCurrent() < 5.0){
					Timer.delay(.5);
				}
			}
			catch(Throwable e){e.printStackTrace(Util.logPrintStream);
			}

			autoPickupThread = null;
		}

	}
}




