package Team4450.Robot10;

import Team4450.Lib.*;

public class GearBox {
	private final Robot			robot;
	private final Teleop		teleop;
	
	
	private final ValveDA		RightSideRobot = new ValveDA(2);
	private final ValveDA		LeftSideRobot = new ValveDA(3);// It is both a single and double action, consider calling ValveSA as well
	private final ValveSA		LeftSideRobot2 = new ValveSA(4);

	public void highGear(String highGear){


		switch(highGear){

		case "lowGear": 
			LeftSideRobot.SetB();
			LeftSideRobot2.Close();

			RightSideRobot.SetB();

			break;

		case "highGear":
			Util.consoleLog("Already in High Gear");

			break;

		case "neutralGear":
			Util.consoleLog();
			LeftSideRobot2.Close();

			break;
		}
	}
	public void lowGear(String lowGear){


		switch(lowGear){

		case "lowGear": 
			Util.consoleLog("Already in Low Gear!!!");

			break;

		case "highGear":
			Util.consoleLog();

			LeftSideRobot2.Open();
			LeftSideRobot.SetA();

			RightSideRobot.SetA();

			break;

		case "neutralGear":
			LeftSideRobot2.Open();

			break;
		}

	}
	public void neutralGear(String neutralGear){

		switch(neutralGear){

		case "lowGear": 
			Util.consoleLog();
			LeftSideRobot2.Close();
			LeftSideRobot.SetB();
			LeftSideRobot.SetA();

			RightSideRobot.SetA();

			break;

		case "highGear":
			Util.consoleLog();
			LeftSideRobot.SetA();

			RightSideRobot.SetA();
			break;

		case "neutralGear":
			Util.consoleLog("Already in neutral gear!!!");

			break;


		}


	}
}


