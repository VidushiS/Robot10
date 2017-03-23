package Team4450.Robot10;

import Team4450.Lib.*;
import Team4450.Robot9.Shooter.ShooterSpeedSource;
import edu.wpi.first.wpilibj.Counter;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.smartdashboard.*;
//import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.CounterBase.EncodingType;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;


public class Shooter {
	private Robot			robot;
	
	public Talon		 	powerMotor = new Talon(1);
	private Talon			shooterMotor = new Talon(3);
	private Talon			regulatorMotor= new Talon(2);
	private final PIDController		pidController;
	public Counter					shooterEncoder = new Counter(0);
	public ShooterSpeedSource 		shooterSpeedSource = new ShooterSpeedSource(shooterEncoder); //fix later in by making a class
	
	public double			pValue,iValue,dValue;
	public double			SHOOTER_LOW_RPM, SHOOTER_HIGH_RPM;
	public double			SHOOTER_LOW_POWER, SHOOTER_HIGH_POWER;
	
	private final PIDController		shooterPidController;
	
	public boolean			shooterOn = false;
	public boolean			feederOn = false;
	
	public Shooter(Robot robot){
		Util.consoleLog();
		this.robot = robot;
		
		shooterEncoder.reset();
		shooterEncoder.setDistancePerPulse(.000976);
		shooterEncoder.setPIDSourceType(PIDSourceType.kRate);
		pidController = new PIDController(0.0, 0.0, 0.0, shooterSpeedSource, powerMotor);
		
		shooterStop();
		
		if (robot.isComp){
			pValue = .0025;
			iValue = .0025;
			dValue = .005; //3
			
			SHOOTER_LOW_RPM = 2500;
			SHOOTER_HIGH_RPM = 3100;
			
			SHOOTER_LOW_POWER = .45;
			SHOOTER_HIGH_POWER =.70;
		}
		else if (robot.isClone){
			pValue = .002;
			iValue = .002;
			dValue = .003; 
			
			SHOOTER_LOW_RPM = 2500;
			SHOOTER_HIGH_RPM = 3100;
			
			SHOOTER_LOW_POWER = .45;
			SHOOTER_HIGH_POWER = .70;
		}
	}
	public void dispose(){
		Util.consoleLog();
		
		if(powerMotor != null) powerMotor.free();
		//if(shooterEncoder != null)shooterEncoder.free();
		if(pidController != null){
			pidController.disable();
			pidController.free();
		}
		if(shooterMotor != null) shooterMotor.free();
		if(regulatorMotor != null) regulatorMotor.free();
	}
	/*public void shooterStart(){
		Util.consoleLog();
		
		powerMotor.set(.8);
		
		shooterOn = true;
		
		SmartDashboard.putBoolean("Shooter Motor", shooterOn);
	}*/
	public void shooterStart(double power){
		if(power == SHOOTER_LOW_POWER){
			holdShooterRPM(SmartDashboard.getNumber("LowSetting", SHOOTER_LOW_RPM));
		}
		else if(power == SHOOTER_LOW_POWER){
			holdShooterRPM(SmartDashboard.getNumber("HighSetting", SHOOTER_LOW_RPM));
			}
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
		shooterMotor.set(-.25); // comp = .3
		regulatorMotor.set(-.8); // comp =-.5
		
		feederOn = true;
	}
	public void feedReverse(){
		Util.consoleLog();
		shooterMotor.set(.2);
		
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
	private void holdShooterRPM(double rpm){
		double pValue = SmartDashboard.getNumber("pValue", pValue);
		double iValue = SmartDashboard.getNumber("iValue", iValue);
		double dValue = SmartDashboard.getNumber("dValue", dValue);
		
		Util.consoleLog("rpm = %.4f  pValue = %.4f iValue = %.4f dValue = %.4f " rpm, pValue, iValue, dValue);
		
		shooterPidController.setPID(pValue, iValue, dValue 0);
		shooterPidController.setSetpoint(rpm/60);
		shooterPidController.setPercentTolerance(5);
		shooterPidController.setToleranceBuffer(4096);
		shooterPidController.setContinuous;
		ShooterSpeedSource.reset();
		shooterPidController.enable();
	}
	public class ShooterSpeedSource implements PIDSource{
		
		private Encoder				encoder;
		private Counter				counter;
		private int					inversion = 1;
		private double				rpmAccumulator, rpmSampleCount;
		
		public ShooterSpeedSource(Encoder encoder){
			this.encoder = encoder;
		}
		public ShooterSpeedSource(Counter counter){
			this.counter = counter;
		}
		
		@Override
		
	}
	}

