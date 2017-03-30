package Team4450.Robot10;

import Team4450.Lib.*;
import edu.wpi.first.wpilibj.Counter;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.smartdashboard.*;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.CounterBase.EncodingType;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;


public class Shooter {
	private Robot			robot;
	private Shooter			shooter;
	public Talon		 	powerMotor = new Talon(1);
	private Talon			shooterMotor = new Talon(3);
	private Talon			regulatorMotor= new Talon(2);
	public Counter					shooterEncoder = new Counter(0);
	public ShooterSpeedSource 		shooterSpeedSource = new ShooterSpeedSource(shooterEncoder); //fix later in by making a class
	
	public double			pValue,iValue,dValue;
	public double			SHOOTER_LOW_RPM, SHOOTER_HIGH_RPM;
	public double			SHOOTER_LOW_POWER, SHOOTER_HIGH_POWER;
	
	public PIDController		shooterPidController;
	
	public boolean			shooterOn = false;
	public boolean			feederOn = false;
	public boolean			shooterCheck = false;
	
	public Shooter(Robot robot){
		Util.consoleLog();
		this.robot = robot;
		
		shooterEncoder.reset();
		shooterEncoder.setDistancePerPulse(.000976);
		shooterEncoder.setPIDSourceType(PIDSourceType.kRate);
		shooterPidController = new PIDController(0.0, 0.0, 0.0, shooterSpeedSource, powerMotor);
		
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
		if(shooterPidController != null){
			if (shooterPidController.isEnabled()) shooterPidController.disable();
			shooterPidController.free();
		}
		if(shooterMotor != null) shooterMotor.free();
		if (shooterEncoder != null) shooterEncoder.free();
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
			powerMotor.set(SHOOTER_LOW_POWER);
			holdShooterRPM(SmartDashboard.getNumber("LowSetting", SHOOTER_LOW_RPM));
		}
		else if(power == SHOOTER_HIGH_POWER){
			powerMotor.set(SHOOTER_HIGH_POWER);
			holdShooterRPM(SmartDashboard.getNumber("HighSetting", SHOOTER_LOW_RPM));
			}
	}
	public void shooterStop(){
		Util.consoleLog();
		
		powerMotor.set(0);
		
		shooterOn = false;
		
		SmartDashboard.putBoolean("Shooter Motor", shooterOn);
	}
	public boolean shooterCheck(){
		
		if(shooterOn = true){
			Util.consoleLog("The Shooter Motor is on");
			return true;
		}
		else if(shooterOn = false){
			Util.consoleLog("The Shooter Motor is off");
			return false;
		}
		return shooterCheck;
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
		double pValue = SmartDashboard.getNumber("pValue", this.pValue);
		double iValue = SmartDashboard.getNumber("iValue", this.iValue);
		double dValue = SmartDashboard.getNumber("dValue", this.dValue);
		
		Util.consoleLog("pValue = %.4f iValue = %.4f dValue = %.4f ",  rpm, pValue ,iValue, dValue);
		
		shooterPidController.setPID(pValue, iValue, dValue, 0);
		shooterPidController.setSetpoint(rpm/60);
		shooterPidController.setPercentTolerance(5);
		shooterPidController.setToleranceBuffer(4096);
		shooterPidController.setContinuous();
		shooterSpeedSource.reset();
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
		public void setPIDSourceType(PIDSourceType pidSource) {
			if(encoder != null)encoder.setPIDSourceType(pidSource);
			if(counter != null)counter.setPIDSourceType(pidSource);
		}
		@Override
		public PIDSourceType getPIDSourceType(){
			
			if (encoder != null)return encoder.getPIDSourceType();
			if (counter != null)return counter.getPIDSourceType();
			
			return null;
		}
		
		public void setInverted(boolean inverted){
			if (inverted){
				inversion = -1;
			}
			else inversion = 1;
			
		}
		public int get()
		{
			if (encoder != null) return encoder.get() * inversion;
			if (counter != null) return counter.get()* inversion;
			
			return 0;
		}
		public double getRate()
		{
			if(encoder != null)return encoder.getRate() * inversion;
			if(counter != null)return counter.getRate() * inversion;
			return 0;
		}
		@Override
		public double pidGet() {
			if (encoder != null){
				if(encoder.getPIDSourceType() == PIDSourceType.kRate){
					return getRate();
				}
				else return get();
			}
			if (counter != null){
			if(counter.getPIDSourceType() == PIDSourceType.kRate){
				return getRate();
			}
			else return get();
			}
			return 0;
		}
		public void reset()
		{
			rpmAccumulator = rpmSampleCount = 0;
			
			if(encoder != null) encoder.reset();
			if(counter != null) counter.reset();
		}
	}
	}

