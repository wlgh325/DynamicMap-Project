import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PImage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class DrawingPath extends PApplet {
	public static void main(String[] args) {
		
	}
	public DrawingPath(String path, String magicFloor) {
		this.path=path;
		this.magicFloor=magicFloor;
	}
	
	PImage floor;
	private String path;
	private String magicFloor;

	Divider divider=new Divider();
	Finder finder=new Finder();
	ImageLoader loader=new ImageLoader();
	MagicFloor text=new MagicFloor();

	int px=-1, py=-1;
	int outIdx=0, inIdx=0;
	int speed=1;

	String[][] pathDivided=divider.DividePath(path);

	public void settings() {
		size(500,800);
		
	}
	public void setup() {
		floor=loadImage(loader.LoadImgPath(finder.FindFloor(pathDivided[outIdx][0])));
		image(floor, 0, 0, width, height);

	}
	public void mousePressed() {
	  inIdx=0; //start node on the next floor
	  if (outIdx==pathDivided.length-1) outIdx=-1;		  floor=loadImage(loader.LoadImgPath(finder.FindFloor(pathDivided[++outIdx][0])));
	  image(floor, 0, 0, width, height);
	}

	public void draw() {
		if (inIdx>=(pathDivided[outIdx].length-1)) inIdx=0;
		if (inIdx==0)  stroke(random(100,255), 50, 50);
		text.HighlightMF(magicFloor);
		strokeWeight(3);
		line(finder.FindLocX(pathDivided[outIdx][inIdx]), finder.FindLocY(pathDivided[outIdx][inIdx])
				, finder.FindLocX(pathDivided[outIdx][inIdx+1]), finder.FindLocY(pathDivided[outIdx][inIdx+1]));
		inIdx++;
		delay(1000/speed);
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
	
	class Divider{
		   Finder finder=new Finder();
		  

		  public String[][] DividePath(String rsPath) { //경로를 층별로 각 배열에 나누는 메소드
		    String[][] dstPath=null;
		    
		    int prevFloor=0, idx=0;
		    int[] nodeNum;
		    int floorNum=0; // 경로 안의 층의 개수
		    String[] temp=rsPath.split(">"); //원래 경로를 ">" 토큰으로 나눈다
		    
		    for(int i=0;i<temp.length;i++) {
		      if(prevFloor!=finder.FindFloor(temp[i])) {
		        floorNum++;
		        prevFloor=finder.FindFloor(temp[i]);
		      }
		    }
		    
		    nodeNum=new int[floorNum]; int seq=0;
		    prevFloor=finder.FindFloor(temp[0]);
		    for(int i=0;i<temp.length;i++) {
		      if(prevFloor!=finder.FindFloor(temp[i])) {
		        prevFloor=finder.FindFloor(temp[i]);
		        seq++;
		      }
		      nodeNum[seq]++;
		    }
		    
		    dstPath=new String[floorNum][];
		    for(int i=0;i<nodeNum.length;i++) {
		      dstPath[i]=new String[nodeNum[i]];
		    }
		    
		    prevFloor=0;
		    for(int i=0;i<dstPath.length;i++) {
		      for(int j=0;j<dstPath[i].length;j++) {
		        dstPath[i][j]=temp[idx++];
		      }
		    }
		    
		    return dstPath;
		  }

	}
	
	class Finder{
		  
		  
		  public int FindFloor(String loc) { //노드를 받아 몇층인지 찾는 메소드
		    char temp;
		    
		    if(!loc.contains("N")&&!loc.contains("C")&&
		          !loc.contains("S")&&!loc.contains("E")) { //강의실이나 출입구인 경우
		        if(loc.contains("-")) temp=(char) (loc.charAt(1)+16); //지하층이면 B102 같이  1번째 인덱스에서 그 층을 알 수 있다.          
		        else if(loc.matches("서라벌 4층")) return -5;
		        else if(loc.matches("법학관 1층")) return -4;
		        else if(loc.matches("제2공학관")) return -3;
		        else if(loc.matches("후문")||loc.matches("기숙사")) return 1; //출입구 찾기
		        else temp=loc.charAt(0); //지상층이면 0번째 인덱스에서 그 층을 알 수 있다.
		        switch(temp) {
		        case'A':return -1;  case'B':return -2;  case'C':return -3;
		        case'D':return -4;  case'E':return -5;  case'F':return -6;
		        case'1':return 1;  case'2':return 2;  case'3':return 3;
		        case'4':return 4;  case'5':return 5;  case'6':return 6;
		        case'7':return 7;  case'8':return 8;  case'9':return 9;
		        }
		      }
		    else {
		      if(loc.contains("-")) temp=(char) (loc.charAt(1)+16); //지하층이면 B102 같이  1번째 인덱스에서 그 층을 알 수 있다.          
		      else temp=loc.charAt(0); //지상층이면 0번째 인덱스에서 그 층을 알 수 있다.
		      switch(temp) {
		      case'A':return -1;  case'B':return -2;  case'C':return -3;
		      case'D':return -4;  case'E':return -5;  case'F':return -6;
		      case'1':return 1;  case'2':return 2;  case'3':return 3;
		      case'4':return 4;  case'5':return 5;  case'6':return 6;
		      case'7':return 7;  case'8':return 8;  case'9':return 9;
		      }
		    }
		    return 0; //어떤 경우에도 속하지 않으면 0 반환 -> 예외
		  }
		    
		   public int FindLocX(String loc)
		   {
		      try {
		            File file = new File("C:\\Users\\senno\\Desktop\\DrawingPath\\NodeLocation.txt"); // graph를 그리기 위해 정리한 txt파일을 연다.
		            FileReader fileReader = new FileReader(file);// 파일을 읽기위해서 filereader를 선언한다.
		            BufferedReader bufReader = new BufferedReader(fileReader); // 한줄씩 읽어오기 위해서 버퍼를 생성한다.
		            String line=""; // 한줄씩 읽어온 것을 저장하기 위한 변수
		            while ((line = bufReader.readLine()) != null) { // 한줄씩 읽어오면서 line에 저장하고, line을 통해 graph를 생성한다.
		               String[] temp=line.split("/");
		               if(temp[0].matches(loc)) return Integer.parseInt(temp[1]);
		            }
		            bufReader.close(); // 버퍼를 없앤다.
		            fileReader.close(); // 리더를 없앤다
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
		            File file = new File("C:\\Users\\senno\\Desktop\\DrawingPath\\NodeLocation.txt"); // graph를 그리기 위해 정리한 txt파일을 연다.
		            FileReader fileReader = new FileReader(file);// 파일을 읽기위해서 filereader를 선언한다.
		            BufferedReader bufReader = new BufferedReader(fileReader); // 한줄씩 읽어오기 위해서 버퍼를 생성한다.
		            String line=""; // 한줄씩 읽어온 것을 저장하기 위한 변수
		            while ((line = bufReader.readLine()) != null) { // 한줄씩 읽어오면서 line에 저장하고, line을 통해 graph를 생성한다.
		               String[] temp=line.split("/");
		               if(temp[0].matches(loc)) return Integer.parseInt(temp[2]);
		            }
		            bufReader.close(); // 버퍼를 없앤다.
		            fileReader.close(); // 리더를 없앤다
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
		  
		  private final String imagePath_B6="C:\\Users\\senno\\Desktop\\Study\\Programming\\DataSturctureDesign\\floorImage\\B6thFloor.jpg";
		  private final String imagePath_B5="C:\\Users\\senno\\Desktop\\Study\\Programming\\DataSturctureDesign\\floorImage\\B5thFloor.jpg";
		  private final String imagePath_B4="C:\\Users\\senno\\Desktop\\Study\\Programming\\DataSturctureDesign\\floorImage\\B4thFloor.jpg";
		  private final String imagePath_B3="C:\\Users\\senno\\Desktop\\Study\\Programming\\DataSturctureDesign\\floorImage\\B3rdFloor.jpg";
		  private final String imagePath_B2="C:\\Users\\senno\\Desktop\\Study\\Programming\\DataSturctureDesign\\floorImage\\B2ndFloor.jpg";
		  private final String imagePath_B1="C:\\Users\\senno\\Desktop\\Study\\Programming\\DataSturctureDesign\\floorImage\\B1stFloor.jpg";
		  private final String imagePath_L1="C:\\Users\\senno\\Desktop\\Study\\Programming\\DataSturctureDesign\\floorImage\\1stFloor.jpg";
		  private final String imagePath_L2="C:\\Users\\senno\\Desktop\\Study\\Programming\\DataSturctureDesign\\floorImage\\2ndFloor.jpg";
		  private final String imagePath_L3="C:\\Users\\senno\\Desktop\\Study\\Programming\\DataSturctureDesign\\floorImage\\3rdFloor.jpg";
		  private final String imagePath_L4="C:\\Users\\senno\\Desktop\\Study\\Programming\\DataSturctureDesign\\floorImage\\4thFloor.jpg";
		  private final String imagePath_L5="C:\\Users\\senno\\Desktop\\Study\\Programming\\DataSturctureDesign\\floorImage\\5thFloor.jpg";
		  private final String imagePath_L6="C:\\Users\\senno\\Desktop\\Study\\Programming\\DataSturctureDesign\\floorImage\\6thFloor.jpg";
		  private final String imagePath_L7="C:\\Users\\senno\\Desktop\\Study\\Programming\\DataSturctureDesign\\floorImage\\7thFloor.jpg";
		  private final String imagePath_L8="C:\\Users\\senno\\Desktop\\Study\\Programming\\DataSturctureDesign\\floorImage\\8thFloor.jpg";
		  private final String imagePath_L9="C:\\Users\\senno\\Desktop\\Study\\Programming\\DataSturctureDesign\\floorImage\\9thFloor.jpg";
		  
		  public String LoadImgPath(int floor) { // 노드를 받아 이미지로드가 필요한 층의 이미지 디렉토리 경로를 반환한다
		    
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
	
	class MagicFloor {
		  PFont font;
		  final int MAX_FLOOR=15;

		  public void HighlightMF(String floors) {
		    int arrIdx=0, floor = -6;
		    int i=0;
		    font=createFont("C:\\Users\\senno\\Desktop\\DrawingPath\\data\\DS-DIGIT.TTF", 33);
		    textFont(font);
		    if(floors.matches(" ")) {
		    	text("B6 B5 B4 B3 B2 B1 1 2 3 4 5 6 7 8 9",25,775); 
		    }
		    else {
		    String[] MagicFloors=floors.split(",");
		    

		    while (arrIdx<MagicFloors.length && floor<=9) {
		      if (MagicFloors[arrIdx].matches(Integer.toString(floor))) {
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
		      } else {
		        fill(0);
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

		      floor++; 
		      i++;
		    }
		  }
		  }
		}
	
}
