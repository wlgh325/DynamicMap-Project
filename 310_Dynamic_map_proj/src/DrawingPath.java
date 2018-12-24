import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PImage;
import processing.core.PVector;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;


public class DrawingPath extends PApplet {

	PImage floor;
	String path = Frame.path;
	String magicFloor = Frame.magicFloor;
	

	Divider divider=new Divider();
	Finder finder=new Finder();
	ImageLoader loader=new ImageLoader();
	RunningFloor text=new RunningFloor();
	Player player=new Player();
	ExProcessor processor = new ExProcessor();
	
	String newPath=processor.embodyPath(path);
	int px=-1, py=-1;
	int outIdx=0, inIdx=0;
	int speed=1;
	float t=0.0f;

	String[][] pathDivided=divider.DividePath(newPath);

	public void setup() {
	  floor=loadImage(loader.LoadImgPath(finder.FindFloor(pathDivided[outIdx][0]))); 
	  image(floor, 0, 0, width, height);
	  													//ù��°�� �̹����ε带 ù ����������
//	  for(int i=0;i<pathDivided.length;i++) { //debugging
//		  for(int j=0;j<pathDivided[i].length;j++) {
//			  System.out.print(pathDivided[i][j]+" ");
//		  }
//		  System.out.println();
//	  }
	  
	}

	public void settings() {
		  size(500, 800); //ȭ�� ũ��� 500 x 800
	}
	public void mousePressed() {
	  nextFloor(); //���콺 Ŭ�� �̺�Ʈ �� ������ �̵� ���� ǥ��
	}


	public void draw() {
	  if (inIdx>=(pathDivided[outIdx].length-1)){	//���������� ���������� �����ϸ�
	    inIdx=0;    //ó�� ��ġ�� ������
	    floor=loadImage(loader.LoadImgPath(finder.FindFloor(pathDivided[outIdx][0])));
	    image(floor,0,0,width,height); //�ٽ� �� �� �̹����� �ε��Ѵ�.
	    stroke(random(200,255), random(50), random(50)); //���� ���� �ݺ��� ������ �ٲ��.
	  }
	  if (inIdx==0)  
	  text.HighlightRF(magicFloor);
	  if (pathDivided[outIdx].length>1) { //���� ǥ���� ��尡 1�� ���ϸ� �����Ѵ�.
	    player.move(finder.FindLocX(pathDivided[outIdx][inIdx]), finder.FindLocY(pathDivided[outIdx][inIdx])
	      , finder.FindLocX(pathDivided[outIdx][inIdx+1]), finder.FindLocY(pathDivided[outIdx][inIdx+1]), t);
	  }
	  
	  // Player�� Ȯ��Ǵ� ������ ���̱� ���� ���� set
	  t+=0.015;
	  if(t>=1.0){
	    t=0.0f;
	    inIdx++;  
	  }
	}

	public void keyPressed() {
	  if (key==CODED) {
	    if (keyCode==RIGHT) {
	      speed++;
	    }
	    if (keyCode==LEFT) {
	      speed--;
	      if (speed==0) speed=1;
	    }
	  }
	}

	void nextFloor(){
		t=0.0f;
	  inIdx=0; //start node on the next floor
	  if (outIdx==pathDivided.length-1) outIdx=-1;
	  outIdx++;
	  if (pathDivided[outIdx].length>1) {
	    floor=loadImage(loader.LoadImgPath(finder.FindFloor(pathDivided[outIdx][0])));
	    image(floor, 0, 0, width, height);
	  } else {
	    while (pathDivided[outIdx].length==1) outIdx++;
	    floor=loadImage(loader.LoadImgPath(finder.FindFloor(pathDivided[outIdx][0])));
	    image(floor, 0, 0, width, height);
	  }
	}
	public class Divider{
		   Finder finder=new Finder();
		  

		  public String[][] DividePath(String rsPath) { //��θ� ������ �� �迭�� ������ �޼ҵ�
		    String[][] dstPath=null;
		    
		    int prevFloor=0;
		    int numTemp=0; //���� ���� �ӽ� ����
		    int[] nodeNum;
		    int floorNum=0; // ��� ���� ���� ����
		    String[] temp=rsPath.split("/"); //���� ��θ� "/" ��ū���� ������
		    
		    for(int i=0;i<temp.length;i++) {
		      if(prevFloor!=finder.FindFloor(temp[i])) {
		        floorNum++; // �� ���� ���� �ٸ� ��带 �߰��ϸ� ���� ������ ������Ų��.
		        prevFloor=finder.FindFloor(temp[i]); //������ �����Ѵ�.
		      }
		    }
		    
		    nodeNum=new int[floorNum]; int seq=0; //������ ��� ������ ���ϱ� ���� �迭�� �����Ѵ�.
		    prevFloor=finder.FindFloor(temp[0]);
		    for(int i=0;i<temp.length;i++) {
		      if(prevFloor!=finder.FindFloor(temp[i])) { //���� temp�� ����Ű�� ����� ���� ���� ���� �ٸ���
		    	  
		    	if(nodeNum[seq]==3) { //���� ���� ��� ������ 3���̰�
		    		for(int j=0;j<seq;j++) {
		    			numTemp+=nodeNum[j];
		    		} //path String�� �������� ��Ÿ���� ���� ����
		    		
		    		if(temp[numTemp].contains("-")&&
		    					(temp[numTemp].contains("C")||temp[numTemp].contains("S")))
		    		{//���� �����̰� ���̰� 3�� �迭�� �����Ǵ� ���� ���� ���̸� 2�� �Ѵ�.
		    				nodeNum[seq]=2; numTemp=0;
		    		}
		    	}
		        prevFloor=finder.FindFloor(temp[i]); //�� ���� ���� �ٸ��� �������� �������� �����Ѵ�.
		        seq++; //�� ���� ���� �ٸ��� �������� ���� �ε����� �Ѿ��
		      }
		      nodeNum[seq]++; //�� ���� ��� ���� �ϳ��� ������Ų��.
		    }
		    
		    dstPath=new String[floorNum][]; //������ ����� ������ŭ ���� �迭 ���̸� �����Ѵ�.
		    for(int i=0;i<nodeNum.length;i++) {
		      dstPath[i]=new String[nodeNum[i]];
		    }
		    
		    prevFloor=0; numTemp=0; //���� ���� ���� �ӽú��� ������ ���� �ʱ�ȭ
		    for(int i=0;i<dstPath.length;i++) {
		      for(int j=0;j<dstPath[i].length;j++) {
		    	  if(temp[numTemp].contains("-")&&
		    			  (temp[numTemp].contains("S")||temp[numTemp].contains("C"))&&
		    			  		dstPath[i].length==2) { //����̳� �����÷����Ϳ��� �����ؼ� ����̳� �����÷����ͷ� ���� ���
		    		  dstPath[i][j]=temp[numTemp];//��ȯ�� ���� �迭�� ���� �ְ�
		    		  numTemp+=2; //�߰� ��带 �����Ѵ�.
		    		  dstPath[i][j+1]=temp[numTemp++];
		    		  break;
		    	  }
		    					  
		    	  else {
		    		  dstPath[i][j]=temp[numTemp++]; //��ȯ�� ���� �迭�� ���� �ִ´�.
		    	  }
		      }
		    	  
		    }
		    
		    return dstPath;
		  }

		}
	
	class Finder{
		  
		  
		  public int FindFloor(String loc) { //��带 �޾� �������� ã�� �޼ҵ�
		    char temp;
		    
		    if(!loc.contains("N")&&!loc.contains("C")&&
		          !loc.contains("S")&&!loc.contains("E")) { //���ǽ��̳� ���Ա��� ���
		        if(loc.contains("-")) temp=(char) (loc.charAt(1)+16); //�������̸� B102 ����  1��° �ε������� �� ���� �� �� �ִ�.          
		        else if(loc.matches("�����")) return -5;
		        else if(loc.matches("���а�")) return -4;
		        else if(loc.matches("��2���а�")) return -3;
		        else if(loc.matches("�Ĺ�")||loc.matches("�����")) return 1; //���Ա� ã��
		        else temp=loc.charAt(0); //�������̸� 0��° �ε������� �� ���� �� �� �ִ�.
		        switch(temp) {
		        case'A':return -1;  case'B':return -2;  case'C':return -3;
		        case'D':return -4;  case'E':return -5;  case'F':return -6;
		        case'1':return 1;  case'2':return 2;  case'3':return 3;
		        case'4':return 4;  case'5':return 5;  case'6':return 6;
		        case'7':return 7;  case'8':return 8;  case'9':return 9;
		        }
		      }
		    else {
		      if(loc.contains("-")) temp=(char) (loc.charAt(1)+16); //�������̸� B102 ����  1��° �ε������� �� ���� �� �� �ִ�.          
		      else temp=loc.charAt(0); //�������̸� 0��° �ε������� �� ���� �� �� �ִ�.
		      switch(temp) {
		      case'A':return -1;  case'B':return -2;  case'C':return -3;
		      case'D':return -4;  case'E':return -5;  case'F':return -6;
		      case'1':return 1;  case'2':return 2;  case'3':return 3;
		      case'4':return 4;  case'5':return 5;  case'6':return 6;
		      case'7':return 7;  case'8':return 8;  case'9':return 9;
		      }
		    }
		    return 0; //� ��쿡�� ������ ������ 0 ��ȯ -> ����
		  }
		    
		   public int FindLocX(String loc)
		   {
		      try {
		            File file = new File("C:\\Users\\senno\\Desktop\\310_Dynamic_map_proj\\data\\NodeLocation.txt"); // graph�� �׸��� ���� ������ txt������ ����.
		            FileReader fileReader = new FileReader(file);// ������ �б����ؼ� filereader�� �����Ѵ�.
		            BufferedReader bufReader = new BufferedReader(fileReader); // ���پ� �о���� ���ؼ� ���۸� �����Ѵ�.
		            String line=""; // ���پ� �о�� ���� �����ϱ� ���� ����
		            while ((line = bufReader.readLine()) != null) { 
		               String[] temp=line.split("/");
		               if(temp[0].matches(loc)) return Integer.parseInt(temp[1]);
		            }
		            bufReader.close(); // ���۸� ���ش�.
		            fileReader.close(); // ������ ���ش�
		         } catch (FileNotFoundException e) {
		            // TODO Auto-generated catch block
		            e.printStackTrace();
		         } catch (IOException e) {
		            // TODO Auto-generated catch block
		            e.printStackTrace();
		         }
		         return -1; //can`t find location
		   }
		  
		  
		  public int FindLocY(String loc){
		    try {
		            File file = new File("C:\\Users\\senno\\Desktop\\310_Dynamic_map_proj\\data\\NodeLocation.txt"); // graph�� �׸��� ���� ������ txt������ ����.
		            FileReader fileReader = new FileReader(file);// ������ �б����ؼ� filereader�� �����Ѵ�.
		            BufferedReader bufReader = new BufferedReader(fileReader); // ���پ� �о���� ���ؼ� ���۸� �����Ѵ�.
		            String line=""; // ���پ� �о�� ���� �����ϱ� ���� ����
		            while ((line = bufReader.readLine()) != null) { // ���پ� �о���鼭 line�� �����ϰ�, line�� ���� graph�� �����Ѵ�.
		               String[] temp=line.split("/");
		               if(temp[0].matches(loc)) return Integer.parseInt(temp[2]);
		            }
		            bufReader.close(); // ���۸� ���ش�.
		            fileReader.close(); // ������ ���ش�
		         } catch (FileNotFoundException e) {
		            // TODO Auto-generated catch block
		            e.printStackTrace();
		         } catch (IOException e) {
		            // TODO Auto-generated catch block
		            e.printStackTrace();
		         }
		         return -1; //can`t find location
		  }

		  
		}
	
	class ImageLoader{
		  
		  private final String imagePath_B6="C:\\Users\\senno\\Desktop\\310_Dynamic_map_proj\\data\\floorImage\\B6thFloor.jpg";
		  private final String imagePath_B5="C:\\Users\\senno\\Desktop\\310_Dynamic_map_proj\\data\\floorImage\\B5thFloor.jpg";
		  private final String imagePath_B4="C:\\Users\\senno\\Desktop\\310_Dynamic_map_proj\\data\\floorImage\\B4thFloor.jpg";
		  private final String imagePath_B3="C:\\Users\\senno\\Desktop\\310_Dynamic_map_proj\\data\\floorImage\\B3rdFloor.jpg";
		  private final String imagePath_B2="C:\\Users\\senno\\Desktop\\310_Dynamic_map_proj\\data\\floorImage\\B2ndFloor.jpg";
		  private final String imagePath_B1="C:\\Users\\senno\\Desktop\\310_Dynamic_map_proj\\data\\floorImage\\B1stFloor.jpg";
		  private final String imagePath_L1="C:\\Users\\senno\\Desktop\\310_Dynamic_map_proj\\data\\floorImage\\1stFloor.jpg";
		  private final String imagePath_L2="C:\\Users\\senno\\Desktop\\310_Dynamic_map_proj\\data\\floorImage\\2ndFloor.jpg";
		  private final String imagePath_L3="C:\\Users\\senno\\Desktop\\310_Dynamic_map_proj\\data\\floorImage\\3rdFloor.jpg";
		  private final String imagePath_L4="C:\\Users\\senno\\Desktop\\310_Dynamic_map_proj\\data\\floorImage\\4thFloor.jpg";
		  private final String imagePath_L5="C:\\Users\\senno\\Desktop\\310_Dynamic_map_proj\\data\\floorImage\\5thFloor.jpg";
		  private final String imagePath_L6="C:\\Users\\senno\\Desktop\\310_Dynamic_map_proj\\data\\floorImage\\6thFloor.jpg";
		  private final String imagePath_L7="C:\\Users\\senno\\Desktop\\310_Dynamic_map_proj\\data\\floorImage\\7thFloor.jpg";
		  private final String imagePath_L8="C:\\Users\\senno\\Desktop\\310_Dynamic_map_proj\\data\\floorImage\\8thFloor.jpg";
		  private final String imagePath_L9="C:\\Users\\senno\\Desktop\\310_Dynamic_map_proj\\data\\floorImage\\9thFloor.jpg";
		  
		  public String LoadImgPath(int floor) { // ��带 �޾� �̹����ε尡 �ʿ��� ���� �̹��� ���丮 ��θ� ��ȯ�Ѵ�
		    
		    switch(floor) {
		    case -1:return imagePath_B1;  case -2:return imagePath_B2;  case -3:return imagePath_B3;
		    case -4:return imagePath_B4;  case -5:return imagePath_B5;  case -6:return imagePath_B6;
		    case 1:return imagePath_L1;    case 2:return imagePath_L2;    case 3:return imagePath_L3;
		    case 4:return imagePath_L4;    case 5:return imagePath_L5;    case 6:return imagePath_L6;
		    case 7:return imagePath_L7;  case 8:return imagePath_L8;  case 9:return imagePath_L9;
		    }
		    return null; 
		  }

		}
	
	//Printing RunningFloor
	class RunningFloor {
		  PFont font;

		  public void HighlightRF(String floors) {
		    int arrIdx=0, floor = -6;
		    int i=0;
		    
		    font=createFont("C:\\Users\\senno\\Desktop\\310_Dynamic_map_proj\\data\\DS-DIGIT.TTF", 33);
		    textFont(font);
		    
		    if(Frame.magicCheck) { //����Ÿ�� ���θ� �޾ƿ� ����Ÿ���� ��� �ؽ�Ʈ�� ���������� �����Ѵ�.
		    	fill(255,0,0);
		    	text("MAGIC TIME",100,65);
		    }
		    else {
		    	fill(0);
		    	text("MAGIC TIME",100,65);
		    }
		    
		    if(floors.matches("")) {
		    	text("B6 B5 B4 B3 B2 B1 1 2 3 4 5 6 7 8 9",25,775); 
		    }
		    else {
		    String[] RunningFloors=floors.split(",");
		    
		    while (arrIdx<RunningFloors.length && floor<=9) {
		      if (RunningFloors[arrIdx].matches(Integer.toString(floor))) {
		        fill(255, 0, 0); 
		        arrIdx++;
		        
		        if (floor<0) {
		          text("B"+Integer.toString(floor).charAt(1), 25+40*i, 775);
		        } else if (floor==0) {
		          floor++; 
		          i++;
		          continue;
		        } else {
		          text(floor, 235+25*(i-6), 775);
		        }
		      } 
		      else {
		        fill(0);
		        if (floor<0) {
		          text("B"+Integer.toString(floor).charAt(1), 25+40*i, 775);
		        } else if (floor==0) {

		          floor++; 
		          i++;
		          continue;
		        } 
		        else {
		          text(floor, 235+25*(i-6), 775);
		        }
		      }

		      floor++; 
		      i++;
		    }
		  }
		  }
		}
	
	class Player {
		  //VECTOR
		  void move(int startX, int startY, int finishX, int finishY,  float t) {
		    PVector startV=new PVector(startX, startY);
		    PVector finishV=new PVector(finishX, finishY);
		    PVector moveV=new PVector(finishV.x-startV.x, finishV.y-startV.y);

		    strokeWeight(3);
		    point(ceil(startV.x+t*moveV.x), ceil(startV.y+t*moveV.y));
		  }
		  
		}
	
	class ExProcessor{
		  private ArrayList<String> exception=new ArrayList<String>();
		  
		   
		   public String embodyPath(String path){
		      addAllException();
		      String[] divided=path.split("/");
		      String newPath=divided[0];
		      
		      for(int i=0;i<divided.length-1;i++){
		        for(int j=0;j<exception.size();j++){
		          String[] temp=exception.get(j).split("/");
		          
		          if(divided[i].matches(temp[0]) && divided[i+1].matches(temp[1])){

		        	  if(temp.length==4) {//���� �κ��� �α����� ���
		        		  newPath+=("/"+temp[2]+"/"+temp[3]); 
		        	  }
		        	  else {//���� �κ��� �ѱ����� ���; temp.length==3
		        		  newPath+=("/"+temp[2]);		              
		        	  }
		          }
		        }
		        newPath+=("/"+divided[i+1]);
		      }
		       
		      System.out.println("newPath="+path);
		      return newPath;
		   }
		  
		  /*
		   ***�߰��� �ڳ�����
		   ***�����  nodeLocation.txt ���Ͽ�
		   ***ex>�������1/�������2/�ڳ������
		   */
		  private void addAllException(){
			//B6F
			exception.add("-6N1/-6E1/-6X1/-6X2");
			exception.add("-6E1/-6N1/-6X2/-6X1");
			//B5F
			exception.add("-5N1/-5N2/-5X1");
			exception.add("-5N2/-5N1/-5X1");
			
			exception.add("-5N2/-5E1/-5X2/-5X3");
			exception.add("-5E1/-5N2/-5X3/-5X2");
			  
		    //1F
		    exception.add("1N4/1N6/1X1");
		    exception.add("1N6/1N4/1X1");
		    
		    exception.add("1N3/1N6/1X2");
		    exception.add("1N6/1N3/1X2");
		    
		    exception.add("1N7/1E3/1X3");
		    exception.add("1E3/1N7/1X3");
		    
		    exception.add( "1N7/1S2/1X3");
		    exception.add( "1S2/1N7/1X3");
		    
		    exception.add( "1N7/1C1/1X3");
		    exception.add( "1C1/1N7/1X3");
		    
		    //3F
		    exception.add( "3N1/3N4/3X1");
		    exception.add( "3N4/3N1/3X1");
		    
		    exception.add( "311/3N4/3X1");
		    exception.add( "3N4/311/3X1");
		    
		    exception.add( "312/3N4/3X1");
		    exception.add( "3N4/312/3X1");
		    
		    exception.add( "3N1/3N2/3X2");
		    exception.add( "3N2/3N1/3X2");
		    
		    exception.add( "3N6/3N8/3X3");
		    exception.add( "3N8/3N6/3X3");
		    
		    exception.add( "3S4/3N8/3X3");
		    exception.add( "3N8/3S4/3X3");
		    
		    exception.add( "3S1/3N2/3X4");
		    exception.add( "3N2/3S1/3X4");
		    
		    exception.add( "3N2/3N3/3X4");
		    exception.add( "3N3/3N2/3X4");
		    
		    //4F
		    exception.add( "4N1/4N4/4X1");
		    exception.add( "4N4/4N1/4X1");
		    
		    exception.add( "414/4N4/4X1");
		    exception.add( "4N4/414/4X1");
		    
		    exception.add( "4N2/4N6/4X2");
		    exception.add( "4N6/4N2/4X2");
		    
		    exception.add( "4N2/4N3/4X3");
		    exception.add( "4N3/4N2/4X3");
		    
		    //5F
		    exception.add( "5N1/5N5/5X1");
		    exception.add( "5N5/5N1/5X1");
		    
		    exception.add( "513/5N5/5X1");
		    exception.add( "5N5/513/5X1");
		    
		    exception.add( "514/5N5/5X1");
		    exception.add( "5N5/514/5X1");
		    
		    exception.add( "5N2/5N3/5X2");
		    exception.add( "5N3/5N2/5X2");
		    
		    exception.add( "5N3/5N6/5X3");
		    exception.add( "5N6/5N3/5X3");
		    
		    exception.add( "5N2/5N4/5X4");
		    exception.add( "5N4/5N2/5X4");
		    
		    //6F
		    exception.add( "6N1/6N6/6X1");
		    exception.add( "6N6/6N1/6X1");
		    
		    exception.add( "612/6N6/6X1");
		    exception.add( "6N6/612/6X1");
		    
		    exception.add( "613/6N6/6X1");
		    exception.add( "6N6/613/6X1");
		    
		    exception.add( "6N4/6N7/6X2");
		    exception.add( "6N7/6N4/6X2");
		    
		    exception.add( "6N2/6N5/6X3");
		    exception.add( "6N5/6N2/6X3");
		    
		    //7F
		    exception.add( "7N1/7N5/7X1");
		    exception.add( "7N5/7N1/7X1");
		    
		    exception.add( "722/7N5/7X1");
		    exception.add( "7N5/722/7X1");
		    
		    exception.add( "723/7N5/7X1");
		    exception.add( "7N5/723/7X1");
		    
		    exception.add( "7N3/7N4/7X2");
		    exception.add( "7N4/7N3/7X2");
		    
		    //8F
		    exception.add( "8N1/8N5/8X1");
		    exception.add( "8N5/8N1/8X1");
		    
		    exception.add( "819/8N5/8X1");
		    exception.add( "8N5/819/8X1");
		    
		    exception.add("8N3/8N4/8X2");
		    exception.add("8N4/8N3/8X2");
		    
		    //9F
		    exception.add("9N1/9N4/9X1");
		    exception.add("9N4/9N1/9X1");
		    
		    exception.add("921/9N4/9X1");
		    exception.add("9N4/921/9X1");
		    
		    exception.add("922/9N4/9X1");
		    exception.add("9N4/922/9X1");
		    
		    exception.add("9S1/9N5/9X2");
		    exception.add("9N5/9S1/9X2");
		    
		    exception.add( "9N10/9N12/9X3");
		    exception.add( "9N12/9N10/9X3");
		  }
		  
		  
		}
	}
