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
	
	public boolean				robotRightSide;
	public boolean				robotLeftSide;
	public boolean				robotCenter;
	
	private Vision(Robot robot){
		Util.consoleLog();
		
		this.robot = robot;
	}
	public static Vision getInstance(Robot robot){
		
		if (vision == null) vision = new Vision(robot);
		return vision;
	}
	public void PegOffsetCalculator(){
		int						centerX1 = 0, centerX2 = 0, pegX;
		Util.consoleLog();
		pegPipeLine.process(robot.cameraThread.getCurrentImage());
		
		if(pegPipeLine.filterContoursOutput().size() > 1){
			rect1 = Imgproc.boundingRect(pegPipeLine.filterContoursOutput().get(0));
			rect2 = Imgproc.boundingRect(pegPipeLine.filterContoursOutput().get(1));
		}
		if (rect1 != null){
			
			Util.consoleLog("x1=%d y1=%d c=%d h=%d w=%d cnt=%d", rect1.x, rect1.y, centerX1, rect1.height,
			         rect1.width, pegPipeLine.filterContoursOutput().size());
		}
		if (rect2 != null){
			Util.consoleLog("x1=%d y1=%d c=%d h=%d w=%d cnt=%d", rect2.x, rect2.y, centerX1, rect2.height,
			         rect2.width, pegPipeLine.filterContoursOutput().size());
		}
		if (rect2 != null & rect1 != null){
			
			centerX1 = rect1.width/2 + rect1.x;
			centerX2 = rect2.width/2 + rect2.x;
			
			if (centerX1 < centerX2){
				pegX = (centerX2 - centerX1)/2 + centerX1;
			}
			else pegX = (centerX1 - centerX2)/2 + centerX2;
			
			pegOffset = imageCenter - pegX;
			
		}
	}
	//Once the peg offset algorithm is completed, use if or else statements 
	//to store values into peg offset
	public void PegOffset(){
	if (pegOffset < 0){
		
		robotLeftSide = true;
		robotRightSide = false;
		robotCenter = false;
		
		Util.consoleLog("The robot is left of the gear.");
		return;
		}
		
		else if (pegOffset > 0){
		
		robotRightSide = true;
		robotLeftSide = false;
		robotCenter = false;
		
		Util.consoleLog("The robot is right of the gear.");
		return;}
		
		else if (pegOffset == 0){
		robotCenter = true;
		robotRightSide = false;
		robotLeftSide = false;
		
		Util.consoleLog("The robot is in front of the gear.");
		return;}
		}
		
		public boolean robotLeftSide(){
			return robotLeftSide;
		}
		public boolean robotRightSide(){
			return robotRightSide;
		}
		public boolean robotCenter(){
			return robotCenter;
		}
		
}
