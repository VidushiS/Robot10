package Team4450.Robot10;

import Team4450.Lib.*;
import edu.wpi.first.wpilibj.smartdashboard.*;

public class GearBox {
	private final Robot			robot;
	private final Teleop		teleop;
	
	
	private final ValveDA		shifter = new ValveDA(0);
	private final ValveDA		neutral = new ValveDA(4);// It is both a single and double action, consider calling ValveSA as well
	private final ValveDA		PTOValve = new ValveDA(2);
	
	public boolean lowGear = false; 
	public boolean highGear = false;
	public boolean neutralGear = false;
	public boolean PTO = false;
	
	public void GearBox(Robot robot, Teleop teleop){
		this.robot = robot;
		this.teleop = teleop;
		
		
	}
	public void highGear(){

		if (lowGear) {
			shifter.SetB();
			neutral.SetB();
			
			lowGear = false;
			highGear = true;
		}
		else if (highGear){
			
			Util.consoleLog("Already in High Gear");
			return;
		}
		else if (neutralGear){
			Util.consoleLog();
			shifter.SetA();
			neutral.SetA();
			
			neutralGear = false;
			highGear = true;
		}
			
			
		}
	
	public void lowGear(){


		if (lowGear){ 
			Util.consoleLog("Already in Low Gear!!!");
			return;
			
		}		

		if (highGear){
			Util.consoleLog();

			PTOValve.SetB();
			
			shifter.SetA();
			neutral.SetB();
			
			highGear = false;
			lowGear = true;
		}

		if (neutralGear){
			PTOValve.Close();
			
			shifter.SetA();
			neutral.SetA();
			
			neutralGear = false;
			lowGear = true;
		}

	}
	public void neutralGear(){

		if (lowGear){
			Util.consoleLog();
			PTOValve.Close();
			
			shifter.SetB();
			neutral.SetB();
			shifter.SetA();
		
			
			lowGear = false;
			neutralGear = true;
		}

		if (highGear){
			Util.consoleLog();
			neutral.SetB();
			shifter.SetA();
			
			highGear = false;
			neutralGear = true;
		}

		if (neutralGear){
			Util.consoleLog("Already in neutral gear!!!");
			return;
		}
	}

	public void EnablePTO(){
		Util.consoleLog();
		neutralGear();
		PTOValve.SetA();
		PTO = true;
		SmartDashboard.putBoolean("PTO", PTO);
		}
	public void ClosePTO(){
		Util.consoleLog();
		lowGear();
		PTOValve.SetB();
		PTO = false;
		SmartDashboard.putBoolean("PTO", PTO);
		}
	
		}


	



