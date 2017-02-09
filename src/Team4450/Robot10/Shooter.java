package Team4450.Robot10;

import com.ctre.CANTalon;

import Team4450.Lib.*;
import Team4450.Lib.JoyStick.*;
import Team4450.Lib.LaunchPad.*;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.CounterBase.EncodingType;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Relay;

public class Shooter {
	private final Robot				robot;
	private final Teleop 			teleop;
	private final CANTalon			shooterMotor1 =  new CANTalon(1);
	private final CANTalon			shooterMotor2 = new CANTalon(2);
	//private final PIDController     shooterPIDController;
	public Encoder					encoder = new Encoder(4, 4, true, EncodingType.k4X); 
	//CHANGE dio PORTS AND REMEMBER!!!!!!!!!!!!!!!!
	
	private final FestoDA			shooterControl = new FestoDA(6);
	//Change name and port for festo
	
	public double					SHOOTER_LOW_POWER, SHOOTER_HIGH_POWER;
	public double					SHOOTER_LOW_RPM, SHOOTER_HIGH_RPM;
	
	private Thread 					autoPickupThread, autoSpitballThread, shootThread;
	
	Shooter(Robot robot,Teleop teleop){
	
		Util.consoleLog();
		this.robot = robot;
		this.teleop = teleop;
	
		encoder.setDistancePerPulse(.000976);
	
		//encoder.setPIDSourceType(PIDSourceType.kRate);
	
		if (robot.isClone) shooterSpeedSource.setInverted(true);
	
		//shooterPIDController = new PIDController(0.0, 0.0, shooterSpeedSource, shooterMotorControl);
	
		if(robot.isComp)
		{
	
		Object pickupMotor = new CANTalon(7); 
	
		robot.InitializeCANTalon((CANTalon), pickupMotor);
	
		SHOOTER_LOW_POWER = 1;
		SHOOTER_HIGH_POWER = 1;
		SHOOTER_LOW_RPM = 1;
		SHOOTER_HIGH_RPM = 1;
	
		/*PVALUE =
		IVALUE = 
		DVALUE =*/ 
		}
		else{
		SHOOTER_LOW_POWER = 1;
		SHOOTER_HIGH_POWER = 1;
		SHOOTER_LOW_RPM = 1;
		SHOOTER_HIGH_RPM = 1;	
		}
	}
	}
	
}
