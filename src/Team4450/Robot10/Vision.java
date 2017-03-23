package Team4450.Robot10;

import org.opencv.core.Rect;
import org.opencv.imgproc.Imgproc;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import Team4450.Lib.CameraFeed;
import Team4450.Lib.Util;

public class Vision {
	private static Vision				vision;
	private Robot				robot;
	private Teleop				teleop;
	private PegPipeline			pegPipeLine = new PegPipeline();
	private int					pegOffset = 0;
	private int					imageCenter = CameraFeed.imageWidth/2;
	public Rect					rect1 = null, rect2 = null;
	
	private Vision(Robot robot){
		Util.consoleLog();
		
		this.robot = robot;
	}
	public static Vision getInstance(Robot robot){
		
		if (vision == null) vision = new Vision(robot);
		return vision;
	}
}
