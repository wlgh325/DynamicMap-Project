import processing.core.*;

public class PrintMap extends PApplet{
	PImage sevenFloor;
	String[] appletArgs;
	
	PrintMap(){
		sevenFloor = null;
		appletArgs = new String[] { "PrintMap" };
		//PApplet.main(new String[] { "--bgcolor=#F0F0F0", "PrintMap" });
		System.out.println("hello");
	}
	
	
	public void settings() {
		size(500,800);
	}
	
	public void setup() {
		String imagePath = "C:\\Users\\Guest1\\eclipse-workspace\\Course_swing\\sevenFloor.jpg";
		sevenFloor = loadImage(imagePath);
		image(sevenFloor,0,0,width,height);
	}
	
	public void draw() {
		if(mousePressed==true) println(mouseX+","+mouseY);
		delay(100);
		line(254,421,189,415);
	}
}
