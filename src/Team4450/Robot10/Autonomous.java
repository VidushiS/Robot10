
package Team4450.Robot10;

import Team4450.Lib.*;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.CounterBase.EncodingType;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Autonomous
{
	private final Robot	robot;
	private final int	program = (int) SmartDashboard.getNumber("AutoProgramSelect");
	private GearLifter  gearLifter;
	private GearBox     gearBox;
	private Vision      vision;
	
	// encoder is plugged into dio port 2 - orange=+5v blue=signal, dio port 3 black=gnd yellow=signal. 
	private Encoder		encoder = new Encoder(1, 2, true, EncodingType.k4X);

	Autonomous(Robot robot)
	{
		Util.consoleLog();
		
		this.robot = robot;
		gearBox = new GearBox(robot, null);
		gearLifter = new GearLifter(robot, null);
		vision = Vision.getInstance(robot);
	}

	public void dispose()
	{
		Util.consoleLog();
		
		if(encoder != null) encoder.free();
		if(gearBox != null) gearBox.Dispose();
		if(gearLifter != null) gearLifter.Dispose();
	}

	public void execute()
	{
		Util.consoleLog("Alliance=%s, Location=%d, Program=%d, FMS=%b", robot.alliance.name(), robot.location, program, robot.ds.isFMSAttached());
		LCD.printLine(2, "Alliance=%s, Location=%d, FMS=%b, Program=%d", robot.alliance.name(), robot.location, robot.ds.isFMSAttached(), program);

		robot.robotDrive.setSafetyEnabled(false);

		// Initialize encoder.
		encoder.reset();
        
		robot.navx.resetYaw();
        // Set gyro to heading 0.
     //   robot.gyro.reset();

        // Wait to start motors so gyro will be zero before first movement.
        Timer.delay(.50);

		switch (program)
		{
			case 0:		// No auto program.
				break;
			case 1:
				autoDrive(-.70, 9000, true);
				break;
			case 2:
				RobotOrientation();
				break;
			case 3:
				autoDrive(.70, 9000, true);
		}
		
		Util.consoleLog("end");
	}

	// Auto drive in set direction and power for specified encoder count. Stops
	// with our without brakes on CAN bus drive system. Uses gyro to go straight.
	private void RobotOrientation(int encoderCounts){
		
		if(vision.robotLeftSide()){
			autoDrive(-.50, 5600, true);
			autoRotate(-.60, 55);
		
		}
		else if(!vision.robotLeftSide() && vision.robotRightSide()){
			autoDrive(-.50, 5600, true);
			autoRotate(.60, 55);
		}
		else if(vision.robotCenter()){
			autoDrive(-.60, encoderCounts, true);
			gearLifter.GearOut();
			Timer.delay(.5);
			gearLifter.GearDown();
			Timer.delay(.5);
			gearLifter.stopGear();
		}
		
		
	}
	private void autoDrive(double power, int encoderCounts, boolean enableBrakes)
	{
		int		angle;
		double	gain = .03;
		
		Util.consoleLog("pwr=%f, count=%d, coast=%b", power, encoderCounts, enableBrakes);

		if (robot.isComp) robot.SetCANTalonBrakeMode(enableBrakes);
		
		encoder.reset();
		robot.navx.resetYaw();
		
		while (robot.isAutonomous() && Math.abs(encoder.get()) < encoderCounts) 
		{
			LCD.printLine(3, "encoder=%d", encoder.get());
		}
			//LCD.printLine(5, "gyroAngle=%d, gyroRate=%d", (int) robot.gyro.getAngle(), (int) robot.gyro.getRate());
			
			// Angle is negative if robot veering left, positive if veering right.
			
			//angle = (int) robot.gyro.getAngle();
			angle = (int) robot.navx.getYaw();
			
			if (power > 0) angle = -angle;
			
			robot.robotDrive.drive(power, -angle * gain);
			
			//Util.consoleLog("angle=%d", angle);
			
			//robot.robotDrive.drive(power, -angle * gain);
			
			Timer.delay(.020);
			
		robot.robotDrive.tankDrive(0, 0, true);	
}

		public void autoRotate(double power, int angle){
			Util.consoleLog("power = %.3f, angle = %", power, angle);
			
			robot.navx.resetYaw();
			
			robot.robotDrive.tankDrive(power, -power);
			
			while(robot.isEnabled() && Math.abs(int))robot.navx.getYaw()) < angle){Timer.delay(.020);}
			
			robot.robotDrive.tankDrive(0, 0);
			return;
		}
		}
	
