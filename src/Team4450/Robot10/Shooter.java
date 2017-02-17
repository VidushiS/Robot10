package Team4450.Robot10;

import com.ctre.CANTalon;
import Team4450.Lib.*;
import Team4450.Lib.JoyStick.*;
import Team4450.Lib.LaunchPad.*;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.CounterBase.EncodingType;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Relay;

public class Shooter {
	private final Robot				robot;
	private final Teleop 			teleop;
	private final CANTalon			shooterMotor1 =  new CANTalon(1);
	private final CANTalon			shooterMotor2 = new CANTalon(2);
	private final CANTalon			intakeMotor = new CANTalon(3);

	public Encoder					encoder = new Encoder(4, 4, true, EncodingType.k4X); 
	//CHANGE dio PORTS AND REMEMBER!!!!!!!!!!!!!!!!
		
	public double					SHOOTER_LOW_POWER = 1;
	public double					SHOOTER_HIGH_POWER = 1;
	public double					SHOOTER_LOW_RPM = 1; 
	public double					SHOOTER_HIGH_RPM;
	
	private Thread 					autoPickupThread, autoSpitballThread, shootThread;
	
	
	Shooter(Robot robot,Teleop teleop){
	
		Util.consoleLog();
		
		
		
	}
	}
	
}
