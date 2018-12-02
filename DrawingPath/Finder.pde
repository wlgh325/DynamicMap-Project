import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class Finder{
  
  
  public int FindFloor(String loc) { //노드를 받아 몇층인지 찾는 메소드
    char temp;
    
    if(!loc.contains("N")&&!loc.contains("C")&&
          !loc.contains("S")&&!loc.contains("E")) { //강의실이나 출입구인 경우
        if(loc.contains("B")) temp=(char) (loc.charAt(1)+16); //지하층이면 B102 같이  1번째 인덱스에서 그 층을 알 수 있다.          
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
      if(loc.contains("B")) temp=(char) (loc.charAt(1)+16); //지하층이면 B102 같이  1번째 인덱스에서 그 층을 알 수 있다.          
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
