public class ImageLoader{
  
  private final String imagePath_B6="C:\\Users\\senno\\Desktop\\Study\\Programming\\DataSturctureDesign\\floorImage\\B6thFloor.jpg";
  private final String imagePath_B5="C:\\Users\\senno\\Desktop\\Study\\Programming\\DataSturctureDesign\\floorImage\\B5thFloor.jpg";
  private final String imagePath_B4="C:\\Users\\senno\\Desktop\\Study\\Programming\\DataSturctureDesign\\floorImage\\B4thFloor.jpg";
  private final String imagePath_B3="C:\\Users\\senno\\Desktop\\Study\\Programming\\DataSturctureDesign\\floorImage\\B3thFloor.jpg";
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
