public class Divider{
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
