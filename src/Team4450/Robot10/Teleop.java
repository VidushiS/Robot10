
package Team4450.Robot10;

import java.lang.Math;

import org.opencv.core.Rect;
import org.opencv.imgproc.Imgproc;

import Team4450.Lib.*;
import Team4450.Lib.JoyStick.*;
import Team4450.Lib.LaunchPad.*;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.CounterBase.EncodingType;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

class Teleop
{
	private final Robot 		robot;
	//private final Teleop		teleop;
	private final GearBox		gearbox;
	private final GearLifter	gearlifter;
	private final BallPickupMotor ballpickupmotor;
	private final Shooter		shooter;
	public  JoyStick			rightStick, leftStick, utilityStick;
	public  LaunchPad			launchPad;
	private final ValveDA		shifterValve = new ValveDA(2);
	private final ValveDA		ptoValve = new ValveDA(0);
	//private boolean				ptoMode = false;
	private boolean				autoTarget = false;

	// Wheel encoder is plugged into dio port 1 - orange=+5v blue=signal, dio port 2 black=gnd yellow=signal. 
	//private Encoder				encoder = new Encoder(1, 2, true, EncodingType.k4X);

	// Encoder ribbon cable to dio ports: ribbon wire 2 = orange, 5 = yellow, 7 = blue, 10 = black

	// Constructor.
	
	Teleop(Robot robot)
	{
		Util.consoleLog();

		this.robot = robot;
		gearbox = new GearBox(robot, this);
		shooter = new Shooter(robot);
		ballpickupmotor = new BallPickupMotor(robot);
		gearlifter = new GearLifter(robot,this);
	}

	// Free all objects that need it.
	
	void dispose()
	{
		Util.consoleLog();
		
		if (leftStick != null) leftStick.dispose();
		if (rightStick != null) rightStick.dispose();
		if (utilityStick != null) utilityStick.dispose();
		if (launchPad != null) launchPad.dispose();
		if (shifterValve != null) shifterValve.dispose();
		if (ptoValve != null) ptoValve.dispose();
		if (gearbox != null) gearbox.Dispose();
		if (shooter != null) shooter.dispose();
		if (gearlifter != null) gearlifter.Dispose();
		if (ballpickupmotor != null) ballpickupmotor.Dispose();
		//if (encoder != null) encoder.free();
	}

	void OperatorControl()
	{
		double	rightY, leftY, utilX;
        
        // Motor safety turned off during initialization.
        robot.robotDrive.setSafetyEnabled(false);

		Util.consoleLog();
		
		LCD.printLine(1, "Mode: OperatorControl");
		LCD.printLine(2, "All=%s, Start=%d, FMS=%b", robot.alliance.name(), robot.location, robot.ds.isFMSAttached());
		
		// Initial setting of air valves.

		gearbox.lowGear();
		gearbox.ClosePTO();
		
		// Configure LaunchPad and Joystick event handlers.
		
		launchPad = new LaunchPad(robot.launchPad, LaunchPadControlIDs.BUTTON_BLUE, this);
		
		LaunchPadControl lpControl = launchPad.AddControl(LaunchPadControlIDs.ROCKER_LEFT_BACK);
		lpControl.controlType = LaunchPadControlTypes.SWITCH;

		launchPad.AddControl(LaunchPadControlIDs.BUTTON_YELLOW);
		launchPad.AddControl(LaunchPadControlIDs.BUTTON_RED_RIGHT);
		launchPad.AddControl(LaunchPadControlIDs.BUTTON_RED);
		launchPad.AddControl(LaunchPadControlIDs.BUTTON_BLUE_RIGHT);
		launchPad.AddControl(LaunchPadControlIDs.BUTTON_BLUE);
        launchPad.addLaunchPadEventListener(new LaunchPadListener());
        launchPad.Start();

		leftStick = new JoyStick(robot.leftStick, "LeftStick", JoyStickButtonIDs.TRIGGER, this);
		leftStick.addJoyStickEventListener(new LeftStickListener());
        leftStick.Start();
        
		rightStick = new JoyStick(robot.rightStick, "RightStick", JoyStickButtonIDs.TOP_LEFT, this);
        rightStick.addJoyStickEventListener(new RightStickListener());
        rightStick.Start();
        
		utilityStick = new JoyStick(robot.utilityStick, "UtilityStick", JoyStickButtonIDs.TRIGGER, this);
			utilityStick.AddButton(JoyStickButtonIDs.TOP_LEFT);
			utilityStick.AddButton(JoyStickButtonIDs.TOP_RIGHT);
			utilityStick.AddButton(JoyStickButtonIDs.TOP_MIDDLE);
			utilityStick.AddButton(JoyStickButtonIDs.TOP_BACK);
		utilityStick.addJoyStickEventListener(new UtilityStickListener());
        utilityStick.Start();
        
        // Tighten up dead zone for smoother turrent movement.
        utilityStick.deadZone = .05;

		// Set CAN Talon brake mode by rocker switch setting.
        // We do this here so that the Utility stick thread has time to read the initial state
        // of the rocker switch.
        if (robot.isComp) robot.SetCANTalonBrakeMode(lpControl.latchedState);
        
        // Set gyro to heading 0.
        //robot.gyro.reset();
        robot.navx.resetYaw();
        // Motor safety turned on.
        robot.robotDrive.setSafetyEnabled(true);
        
		// Driving loop runs until teleop is over.

		while (robot.isEnabled() && robot.isOperatorControl())
		{
			// Get joystick deflection and feed to robot drive object
			// using calls to our JoyStick class.

			if (gearbox.PTOstate())
			{
				rightY = utilityStick.GetY();

				leftY = rightY;
			} 
			else
			{
				rightY = stickLogCorrection(rightStick.GetY());	// fwd/back right
    			leftY = stickLogCorrection(leftStick.GetY());	// fwd/back left
			}
			
			utilX = utilityStick.GetX();
			
			LCD.printLine(4, "leftY=%.4f  rightY=%.4f utilX=%.4f", leftY, rightY, utilX);
			//LCD.printLine(5, "gyroAngle=%d, gyroRate=%d", (int) robot.gyro.getAngle(), (int) robot.gyro.getRate());
			LCD.printLine(6, "yaw=%.0f, total=%.0f, rate=%.3f", robot.navx.getYaw(), robot.navx.getTotalYaw(), robot.navx.getYawRate());
			// Set wheel motors.
			// Do not feed JS input to robotDrive if we are controlling the motors in automatic functions.

			if (!autoTarget) robot.robotDrive.tankDrive(leftY, rightY);

			// End of driving loop.
			
			Timer.delay(.020);	// wait 20ms for update from driver station.
		}
		
		// End of teleop mode.
		
		Util.consoleLog("end");
	}

	// Map joystick y value of 0.0-1.0 to the motor working power range of approx 0.5-1.0
	
	private double stickCorrection(double joystickValue)
	{
		if (joystickValue != 0)
		{
			if (joystickValue > 0)
				joystickValue = joystickValue / 1.5 + .4;
			else
				joystickValue = joystickValue / 1.5 - .4;
		}
		
		return joystickValue;
	}
	
	// Custom base logrithim.
	// Returns logrithim base of the value.
	
	private double baseLog(double base, double value)
	{
		return Math.log(value) / Math.log(base);
	}

	// Map joystick y value of 0.0 to 1.0 to the motor working power range of approx 0.5 to 1.0 using
	// logrithmic curve.
	
	private double stickLogCorrection(double joystickValue)
	{
		double base = Math.pow(2, 1/3) + Math.pow(2, 1/3);
		
		if (joystickValue > 0)
			joystickValue = baseLog(base, joystickValue + 1);
		else if (joystickValue < 0)
			joystickValue = -baseLog(base, -joystickValue + 1);
			
		return joystickValue;
	}
	
	// Transmission control functions.
	
	//--------------------------------------
	/*void shifterLow()
	{
		Util.consoleLog();
		
		shifterValve.SetA();

		SmartDashboard.putBoolean("Low", true);
		SmartDashboard.putBoolean("High", false);
	}

	void shifterHigh()
	{
		Util.consoleLog();
		
		shifterValve.SetB();

		SmartDashboard.putBoolean("Low", false);
		SmartDashboard.putBoolean("High", true);
	}
	
	//--------------------------------------
	void ptoDisable()
	{
		Util.consoleLog();
		
		ptoMode = false;
		
		ptoValve.SetA();

		SmartDashboard.putBoolean("PTO", false);
	}
	
	void ptoEnable()
	{
		Util.consoleLog();
		
		ptoValve.SetB();

		ptoMode = true;
		
		SmartDashboard.putBoolean("PTO", true);
	}*/
	
	// Handle LaunchPad control events.
	
	public class LaunchPadListener implements LaunchPadEventListener 
	{
	    public void ButtonDown(LaunchPadEvent launchPadEvent) 
	    {
	    	LaunchPadControl	control = launchPadEvent.control;
	    	
			Util.consoleLog("%s, latchedState=%b", control.id.name(),  control.latchedState);
			
			switch(control.id)
			{
				case BUTTON_YELLOW: //set up auto gear pickup
					if(launchPadEvent.control.latchedState){
						gearlifter.AutoGearPickup();
					}
					else gearlifter.StopAutoGearPickup();
					break;
					
				case BUTTON_BLUE_RIGHT:
					if (launchPadEvent.control.latchedState){// Takes climber class place.
						gearlifter.GearUp();
					}
					else gearlifter.GearDown();
					
    				break;
    				
				case BUTTON_BLUE:
    				if (launchPadEvent.control.latchedState)
    				{
    					gearbox.EnablePTO();
    				
    				}
        			else gearbox.ClosePTO();
        				

    				break;
				case BUTTON_RED:
					if (launchPadEvent.control.latchedState)
					{
						gearlifter.GearOut();
					}
					else gearlifter.GearIn();
					
					break;
				
				case BUTTON_RED_RIGHT:
					break;
					
				default:
					break;
			}
	    }
	    
	    public void ButtonUp(LaunchPadEvent launchPadEvent) 
	    {
	    	//Util.consoleLog("%s, latchedState=%b", launchPadEvent.control.name(),  launchPadEvent.control.latchedState);
	    }

	    public void SwitchChange(LaunchPadEvent launchPadEvent) 
	    {
	    	LaunchPadControl	control = launchPadEvent.control;
	    	
	    	Util.consoleLog("%s", control.id.name());

	    	switch(control.id)
	    	{
				// Set CAN Talon brake mmode.
	    		case ROCKER_LEFT_BACK:
    				if (control.latchedState)
    					robot.SetCANTalonBrakeMode(false);	// coast
    				else
    	    			robot.SetCANTalonBrakeMode(true);	// brake
    				
    				break;
	    		case ROCKER_LEFT_FRONT:
	    			robot.cameraThread.ChangeCamera();
					//invertDrive = !invertDrive;
					break;
					
				default:
					break;
	    	}
	    }
	}

	// Handle Right JoyStick Button events.
	
	private class RightStickListener implements JoyStickEventListener 
	{
		
	    public void ButtonDown(JoyStickEvent joyStickEvent) 
	    {
	    	JoyStickButton	button = joyStickEvent.button;
	    	
			Util.consoleLog("%s, latchedState=%b", button.id.name(),  button.latchedState);
			
			switch(button.id)
			{
				case TOP_LEFT:
   					robot.cameraThread.ChangeCamera();
    				break;
				
				default:
					break;
			}
	    }

	    public void ButtonUp(JoyStickEvent joyStickEvent) 
	    {
	    	//Util.consoleLog("%s", joyStickEvent.button.name());
	    }
	}

	// Handle Left JoyStick Button events.
	
	private class LeftStickListener implements JoyStickEventListener 
	{
	    public void ButtonDown(JoyStickEvent joyStickEvent) 
	    {
	    	JoyStickButton	button = joyStickEvent.button;
	    	
			Util.consoleLog("%s, latchedState=%b", button.id.name(),  button.latchedState);
			
			switch(button.id)
			{
				case TRIGGER:
					if (button.latchedState)
	    				gearbox.highGear();
	    			else
	    				gearbox.lowGear();

					break;
					
				default:
					break;
			}
	    }

	    public void ButtonUp(JoyStickEvent joyStickEvent) 
	    {
	    	//Util.consoleLog("%s", joyStickEvent.button.name());
	    }
	}

	// Handle Utility JoyStick Button events.
	
	private class UtilityStickListener implements JoyStickEventListener 
	{
	    public void ButtonDown(JoyStickEvent joyStickEvent) 
	    {
	    	JoyStickButton	button = joyStickEvent.button;
	    	
			Util.consoleLog("%s, latchedState=%b", button.id.name(),  button.latchedState);
			
			switch(button.id)
			{
				// Trigger starts shoot sequence.
				case TRIGGER:
					if (button.latchedState){
						gearbox.highGear();
					}
					else gearbox.lowGear();
    				break;
				case TOP_RIGHT:
					if (button.latchedState){
						ballpickupmotor.shooterStart();
					}
					else ballpickupmotor.shooterStop();
					break;
				case TOP_LEFT:
					if (button.latchedState){
						shooter.shooterStart();
					}
					else shooter.shooterStop();
					break;
				case TOP_MIDDLE:
					if(button.latchedState){
					gearlifter.startGearIn();}
					else gearlifter.stopGear();
					break;
				case TOP_BACK:
					if(button.latchedState){
						gearlifter.startGearOut();
					}
					else gearlifter.stopGear();
					break;
				default:
					break;
			}
	    }

	    public void ButtonUp(JoyStickEvent joyStickEvent) 
	    {
	    	//Util.consoleLog("%s", joyStickEvent.button.id.name());
	    }
	}
}
