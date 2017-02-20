package Team4450.Robot10;

import Team4450.Lib.*;

public class GearBox {
	private final Robot			robot;
	private final Teleop		teleop;
	
	
	private final ValveDA		DblActVlvRS = new ValveDA(2);
	private final ValveDA		DblActVlvLS = new ValveDA(3);// It is both a single and double action, consider calling ValveSA as well
	private final ValveDA		PTOValve = new ValveDA(4);
	
	public boolean lowGear = false; 
	public boolean highGear = false;
	public boolean neutralGear = false;
	
	public void GearBox(Robot robot, Teleop teleop){
		this.robot = robot;
		this.teleop = teleop;
		
		
	}
	public void highGear(){

		if (lowGear) {
			DblActVlvLS.SetB();
			PTOValve.Close();
			
			
			DblActVlvRS.SetB();
			lowGear = false;
			highGear = true;
		}
		else if (highGear){
			
			Util.consoleLog("Already in High Gear");
			return;
		}
		else if (neutralGear){
			Util.consoleLog();
			PTOValve.Close();
			
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

			PTOValve.Open();
			
			DblActVlvLS.SetA();
			
			
			DblActVlvRS.SetA();
			
			highGear = false;
			lowGear = true;
		}

		if (neutralGear){
			PTOValve.Open();
			
			neutralGear = false;
			lowGear = true;
		}

	}
	public void neutralGear(){

		if (lowGear){
			Util.consoleLog();
			PTOValve.Close();
			
			DblActVlvLS.SetB();
			
			DblActVlvLS.SetA();
			

			DblActVlvRS.SetA();
			
			lowGear = false;
			neutralGear = true;
		}

		if (highGear){
			Util.consoleLog();
			DblActVlvLS.SetA();
			

			DblActVlvRS.SetA();
			
			highGear = false;
			neutralGear = true;
		}

		if (neutralGear){
			Util.consoleLog("Already in neutral gear!!!");
			return;
		}


		}


	}



