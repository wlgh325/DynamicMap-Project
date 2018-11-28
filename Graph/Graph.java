import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;

public class Graph {

   private final static int MAX_N = 17;
   private final static int MAX_S = 5;
   private final static int MAX_E = 4;
   private final static int MAX_C = 3;
   private Floor floorHead; //
   private Floor floorTail;
   private int size = 0;

   private class Floor {
      // 해당 oject의 data
      private int floor; // 지하 1층은 -1 지상은 1층은 1
      // graph를 만들기 위한 head,tail
      private Node nodeHead; // 해당층의 머리
      private Node nodeTail; // 해당층의 꼬리
      // 층간 이동을 위한 엘레베이터와 계단 노드를 지정
      private Node[] nodeElevate; // 엘레베이터의 시작 node들.
      private Node[] nodeStair;// 계단의 시작 node들

      private int nodeSize = 0; // node의 싸이즈가 얼마인지

      private Floor Next; // 그래프 만들기 위한 변수
      private Floor[] floorNext; // 어느 층들과 연결이 됬는지
      // 해당 층의 node를 편하게 이어주기위한 배열
      private boolean[] N = new boolean[MAX_N]; // 각 원소가 true라면 이미 생성되어 있는 node라는 소리이다. 없으면 false
      private boolean[] S = new boolean[MAX_S]; // 각 원소가 true라면 이미 생성되어 있는 node라는 소리이다. 없으면 false
      private boolean[] E = new boolean[MAX_E]; // 각 원소가 true라면 이미 생성되어 있는 node라는 소리이다. 없으면 false
      private boolean[] C = new boolean[MAX_C];
      // 생성자

      public Floor(int floor) {
         this.floor = floor;
         for (int i = 0; i < MAX_N; i++) { // 처음 이 층이 생성되면 모든 노드는 없으므로, 모두 false로 초기화
            N[i] = false;
         }
         for (int i = 0; i < MAX_S; i++) { // 처음 이 층이 생성되면 모든 노드는 없으므로, 모두 false로 초기화
            S[i] = false;
         }
         for (int i = 0; i < MAX_E; i++) { // 처음 이 층이 생성되면 모든 노드는 없으므로, 모두 false로 초기화
            E[i] = false;
         }
         for (int i = 0; i < MAX_C; i++) { // 처음 이 층이 생성되면 모든 노드는 없으므로, 모두 false로 초기화
             C[i] = false;
          }
      }

      private class Node {
         // 이 node가 교차점인지, 엘레베이터인지, 계단인지 구분해주는 변수들
         private char distinction; // 교차점이면 n, 엘레베이터면 e, 계단이면 s
         private int number; // 몇번째 교차점,엘레베이터,계단인지. 7n4 면 number에 4가 들어간다.
         // 교차점과 교차점,계단,엘레베이터 사이의 관계, 링크드 리스트 형태로 저장
         private ArrayList<Float> distanceNode = new ArrayList<>(); // 연결된 node이름과 거리
         private ArrayList<Node> nearNode = new ArrayList<>(); // 인접 node를 연결
         // 교차점과 강의실 사이의 관계,배열의 형태로 저장.
         private String[] room; // 교차점과 인접한 room 정보
         private float[] distance; // 교차점과 인접한 room과의 거리

         // 생성자
         public Node(char dis, int num) {
            distinction = dis;
            number = num;
         }
      }

      // 처음 그층에 node가 들어갈때 쓰는 함수
      public void addFirstNode(char dis, int num, String[] nearNode, String[] nearRoom) {// 새로운 node를 생성한다.
         // 첫 node를 생성한다.
         Node newNode = new Node(dis, num);
         changeExistArray(dis, num);
         // node가 생성되서 nodeSize를 1 증가시킨다.
         nodeSize++;
         // 헤드와 꼬리를 새로만든 newNode라고 지정한다.
         nodeHead = newNode;
         nodeTail = nodeHead;
         // 가까운 강의실을 배열에 저장한다.
         if (nearRoom.length > 0) {
            newNode.room = Arrays.copyOf(nearRoom, nearRoom.length); // 파일에서 읽어온 인접강의실을 배열에 저장한다.
            newNode.distance = new float[nearRoom.length]; // distance의 배열의 숫자를 파일에서 일어온 room의 갯수로 초기화한다.
            for (int i = 0; i < newNode.distance.length; i++) { // 임의로 거리를 초기화한다. 나중에 바꿔야한다.%%%
               newNode.distance[i] = 0;
            }
         }
         // 인접한 node를 생성하고 리스트형태로 이어준다.
         for (int i = 0; nearNode[i] != null; i++) { // 파일에서 읽어온 인접node들을 딕션어리에 저장한다.
            // 인접 node temp를 생성한다.
            Node temp = new Node(nearNode[i].charAt(0), Integer.parseInt(nearNode[i].substring(1))); // temp라는
                                                                                 // newNode의
                                                                                 // 인접노드를
                                                                                 // 생성한다.
            changeExistArray(nearNode[i].charAt(0), Integer.parseInt(nearNode[i].substring(1)));
            // new node ->인접node 연결한다.
            newNode.distanceNode.add((float) 0); // newNode에 temp에 대한 정보를 넣어준다.
            newNode.nearNode.add(temp);// 인접 노드들을 arraylist에 추가한다.
            // 인접 node 에서 newnode로 넣어준다.
            // String temp2 = newNode.distinction + newNode.number +"";
            temp.distanceNode.add((float) 0); // temp에 newNode에 대한 정보를 넣어준다.
            temp.nearNode.add(newNode); // 양방향으로 이어줘야 하므로 다시 temp에서 newNode로 이어주게한다.

            nodeSize++;
         }
      }

      public void addFirstNode(char dis, int num, String[] nearNode) {// 새로운 node를 생성한다.
         // 첫 node를 생성한다.
         Node newNode = new Node(dis, num);
         changeExistArray(dis, num);
         
         // node가 생성되서 nodeSize를 1 증가시킨다.
         nodeSize++;
         
         // 헤드와 꼬리를 새로만든 newNode라고 지정한다.
         nodeHead = newNode;
         nodeTail = nodeHead;
         
         // 인접한 node를 생성하고 리스트형태로 이어준다.
         for (int i = 0; nearNode[i] != null; i++) { // 파일에서 읽어온 인접node들을 dictionary에 저장한다.
            // 인접 node temp를 생성한다.
            Node temp = new Node(nearNode[i].charAt(0), Integer.parseInt(nearNode[i].substring(1, nearNode[i].length()))); // temp라는
                                                                                 // newNode의
                                                                                 // 인접노드를
                                                                                 // 생성한다.
            changeExistArray(nearNode[i].charAt(0), Integer.parseInt(nearNode[i].substring(1)));
            // new node ->인접node 연결한다.
            newNode.distanceNode.add((float) 0); // newNode에 temp에 대한 정보를 넣어준다.
            newNode.nearNode.add(temp);// 인접 노드들을 arraylist에 추가한다.
            // 인접 node 에서 newnode로 넣어준다.
            // String temp2 = newNode.distinction + newNode.number +"";
            temp.distanceNode.add((float) 0); // temp에 newNode에 대한 정보를 넣어준다.
            temp.nearNode.add(newNode); // 양방향으로 이어줘야 하므로 다시 temp에서 newNode로 이어주게한다.

            nodeSize++;
         }
      }

      public void addNode(char dis, int num, String[] nearNode, String[] nearRoom) {
         if (existNode(dis, num)) { // 해당 node가 존재하면, 새로 만들지말고, 그 노드로 이동한다. 그리고 생성한다.
            Node thisNode = getNode(dis, num);
            // tail을 thisNode라고 지정한다.
            nodeTail = thisNode;
            // 가까운 강의실 배열을 저장한다.
            if (nearRoom.length > 0) {
               thisNode.room = Arrays.copyOf(nearRoom, nearRoom.length);
               thisNode.distance = new float[nearRoom.length];
               for (int i = 0; i < thisNode.distance.length; i++) { // 임의로 거리를 초기화한다. 나중에 바꿔야한다.%%%
                  thisNode.distance[i] = (float)0;
               }
            }
            // 인접한 node를 생성하고 리스트형태로 이어준다.
            for (int i = 0; nearNode[i] != null; i++) { // 파일에서 읽어온 인접node들을 딕션어리에 저장한다.
               if (existNode(nearNode[i].charAt(0), Integer.parseInt(nearNode[i].substring(1)))) // graph에 인접행렬이
                                                                              // 존재한다면,
                                                                              // 연결하도록한다.
               {
                  if (isAlreadyConnected(thisNode, nearNode[i].charAt(0),
                        Integer.parseInt(nearNode[i].substring(1))) == false)// 이미 이어져있는 경우는 넘기고, 안이져있다면 이어준다.
                  {
                     // this -> 인접 node로 연결
                     Node temp = getNode(nearNode[i].charAt(0),Integer.parseInt(nearNode[i].substring(1))); // 인접 node를 temp라 지정
                     thisNode.distanceNode.add((float) 0); // thisNode에 temp에 대한 거리정보를 넣어준다.
                     thisNode.nearNode.add(temp);// 인접 node들을 arraylist에 추가한다.
                     // 인접 node에서 this로 연결
                     temp.distanceNode.add((float) 0);
                     temp.nearNode.add(thisNode);
                  }
               } else // graph에 인접행렬이 없다면 node를 생성하고, 연결한다.
               {
                  // 인접 node temp를 생성한다.
                  Node temp = new Node(nearNode[i].charAt(0), Integer.parseInt(nearNode[i].substring(1))); // temp라는thisNode의
                                                                                       // 인접노드를
                                                                                       // 생성한다.
                  changeExistArray(nearNode[i].charAt(0), Integer.parseInt(nearNode[i].substring(1)));
                  // this -> 인접 node로 연결
                  thisNode.distanceNode.add((float) 0); // thisNode에 temp에 대한 거리정보를 넣어준다.
                  thisNode.nearNode.add(temp);// 인접 node들을 arraylist에 추가한다.
                  // 인접 node에서 this로 연결
                  temp.distanceNode.add((float) 0);
                  temp.nearNode.add(thisNode);
                  // node가 하나 추가됬으므로 nodeSize를 1증가시킨다.
                  nodeSize++;

               }
            }
         }
      }

      public void addNode(char dis, int num, String[] nearNode) {
         if (existNode(dis, num)) { // 해당 node가 존재하면, 새로 만들지말고, 그 노드로 이동한다. 그리고 생성한다.
            Node thisNode = getNode(dis, num);
            // tail을 thisNode라고 지정한다.
            nodeTail = thisNode;
            // 인접한 node를 생성하고 리스트형태로 이어준다.
            for (int i = 0; nearNode[i] != null; i++) { // 파일에서 읽어온 인접node들을 딕션어리에 저장한다.
               if (existNode(nearNode[i].charAt(0), Integer.parseInt(nearNode[i].substring(1, nearNode[i].length())))) // graph에 인접행렬이
                                                                              // 존재한다면,
                                                                              // 연결하도록한다.
               {
                  if (isAlreadyConnected(thisNode, nearNode[i].charAt(0),
                        Integer.parseInt(nearNode[i].substring(1))) == false)// 이미 이어져있는 경우는 넘기고, 안이져있다면 이어준다.
                  {
                     // this -> 인접 node로 연결
                     Node temp = getNode(nearNode[i].charAt(0),Integer.parseInt(nearNode[i].substring(1))); // 인접 node를 temp라 지정
                     thisNode.distanceNode.add((float) 0); // thisNode에 temp에 대한 거리정보를 넣어준다.
                     thisNode.nearNode.add(temp);// 인접 node들을 arraylist에 추가한다.
                     // 인접 node에서 this로 연결
                     temp.distanceNode.add((float) 0);
                     temp.nearNode.add(thisNode);
                  }
               } else // graph에 인접행렬이 없다면 node를 생성하고, 연결한다.
               {
                  // 인접 node temp를 생성한다.
                  Node temp = new Node(nearNode[i].charAt(0), Integer.parseInt(nearNode[i].substring(1))); // temp라는thisNode의
                                                                                       // 인접노드를
                                                                                       // 생성한다.
                  changeExistArray(nearNode[i].charAt(0), Integer.parseInt(nearNode[i].substring(1)));
                  // this -> 인접 node로 연결
                  thisNode.distanceNode.add((float) 0); // thisNode에 temp에 대한 거리정보를 넣어준다.
                  thisNode.nearNode.add(temp);// 인접 node들을 arraylist에 추가한다.
                  // 인접 node에서 this로 연결
                  temp.distanceNode.add((float) 0);
                  temp.nearNode.add(thisNode);
                  // node가 하나 추가됬으므로 nodeSize를 1증가시킨다.
                  nodeSize++;

               }
            }
         }
      }

      public Node getNode(char dis, int num) { // 해당 dis,num으로 graph에 같은 노드가 있다면, dfs방법으로 찾아서 해당 노드를 출력해준다.
         // dfs를 할때에 node가 들렸는지 확인해주는 변수
         boolean[] N_DFS = new boolean[MAX_N]; // 각 원소가 true라면 이미 지나온 것이고, false은 아직 탐색하지 않은것
         boolean[] S_DFS = new boolean[MAX_S]; // 각 원소가 true라면 이미 지나온 것이고, false은 아직 탐색하지 않은것
         boolean[] E_DFS = new boolean[MAX_E]; // 각 원소가 true라면 이미 지나온 것이고, false은 아직 탐색하지 않은것
         boolean[] C_DFS = new boolean[MAX_C]; // 각 원소가 true라면 이미 지나온 것이고, false은 아직 탐색하지 않은것
         Node root = nodeHead;
         Stack<Node> stack = new Stack<Node>();
         N_DFS[root.number] = true;
         stack.push(root);
         while (!stack.isEmpty()) {
            Node r = stack.pop();
            for (int i = 0; i < r.nearNode.size(); i++) {
               Node n = r.nearNode.get(i);
               if (n.distinction == dis && n.number == num) {
                  return n; // dis와 num와 일치하는 node를 return해준다.
               }
               if (n.distinction == 'N') {
                  if (N_DFS[n.number] == false) {
                     N_DFS[n.number] = true;
                     stack.push(n);
                  }
               } else if (n.distinction == 'S') {
                  if (S_DFS[n.number] == false) {
                     S_DFS[n.number] = true;
                     stack.push(n);
                  }
               } else if (n.distinction == 'E') {
                  if (E_DFS[n.number] == false) {
                     E_DFS[n.number] = true;
                     stack.push(n);
                  }
               } else if (n.distinction == 'C') {
                   if (C_DFS[n.number] == false) {
                       C_DFS[n.number] = true;
                       stack.push(n);
                    }
                 }
            }
         }
         System.out.println("오류 :해당 graph에서 node를 찾을 수 없습니다.");
         return null;
      }

      public boolean isAlreadyConnected(Node temp, char dis, int num) { // 이미 해당 노드하고 이어져있는 경우라면, treu, 아니면 false를
                                                         // 반환한다.
         for (int i = 0; i < temp.nearNode.size(); i++) {
            if (temp.nearNode.get(i).distinction == dis && temp.nearNode.get(i).number == num) {
               return true;
            }
         }
         return false;
      }

      public boolean existNode(char dis, int num) {// 해당 노드가 있다면 다시 생성할 필요가 없다. 있으면 true 없으면 false
         if (dis == 'N') { // 교차점 인지 비교
            if (N[num] == true) { // 이미 node가 있는 경우
               return true;
            }
            return false;
         } else if (dis == 'S') { // 계단인지 비교
            if (S[num] == true) { // 이미 node가 있는 경우
               return true;
            }
            return false;
         } else if (dis == 'E') { // 엘레베이터인지 비교
            if (E[num] == true) { // 이미 node가 있는 경우
               return true;
            }
            return false;
         }else if (dis == 'C') { // 엘레베이터인지 비교
             if (C[num] == true) { // 이미 node가 있는 경우
                 return true;
              }
              return false;
           }
         System.out.println( dis +""+  num + "가 올바른 node 정보인지 확인해주세요");
         return false; // 오류

      }

      public void changeExistArray(char dis, int num) {// 해당 노드가 생성이되면 false로 바꿔준다.
         if (dis == 'N') { // 교차점 인지 비교
            if (N[num] == false) { // 없다면 있는걸로 바꿔준다.
               N[num] = true;
               return;
            }
         } else if (dis == 'S') { // 계단인지 비교
            if (S[num] == false) { // 없다면 있는걸로 바꿔준다.
               S[num] = true;
               return;
            }
         } else if (dis == 'E') { // 엘레베이터인지 비교
            if (E[num] == false) { // 없다면 있는걸로 바꿔준다.
               E[num] = true;
               return;
            }
         } else if (dis == 'C') { // 엘레베이터인지 비교
             if (C[num] == false) { // 없다면 있는걸로 바꿔준다.
                 C[num] = true;
                 return;
              }
           }
         System.out.println(dis + num + "가 올바른 node 정보인지 확인해주세요");
         return; // 오류

      }
   }

   public int readFile(String filename) {
      try {
         File file = new File(filename); // graph를 그리기 위해 정리한 txt파일을 연다.
         FileReader fileReader = new FileReader(file);// 파일을 읽기위해서 filereader를 선언한다.
         BufferedReader bufReader = new BufferedReader(fileReader); // 한줄씩 읽어오기 위해서 버퍼를 생성한다.
         String line=""; // 한줄씩 읽어온 것을 저장하기 위한 변수
         while ((line = bufReader.readLine()) != null) { // 한줄씩 읽어오면서 line에 저장하고, line을 통해 graph를 생성한다.
            System.out.println(line);
        	makeGraph(line);
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
      return -1;
   }

   private void makeGraph(String line) {
      // TODO Auto-generated method stub
	  final int NEAR_NODE_NUM = 10;
      String[] temp = line.split("/");
      String[] floorAndNumber = temp[0].split("N"); // [0]-> floor의 정보, [1] -> 교차점 정보
      String[] nearNode = new String[NEAR_NODE_NUM];
      
      
      for(int i=0; i<temp.length; i++) {
    	  System.out.print(temp[i] + ",");
      }
      
      //nearNode 정보 저장
      if(temp[1].contains(",")) {	//nearNode가 여러개인 경우
    	  String[] tempNode = temp[1].split(","); // 각각 nearNode[0] -> N7 or S1 or E1 이런식으로 저장이 되어있다.
    	  for(int i=0; i<tempNode.length; i++)
    		  nearNode[i] = tempNode[i];
    	  for(int i=tempNode.length; i<NEAR_NODE_NUM; i++)
    		  nearNode[i] = null;
      }
      else	//nearNode가 하나 밖에 없을경우 처리
    	  nearNode[0] = temp[1];
      
      String[] nearRoom = null;
      int floor = transformfloorType(floorAndNumber[0]);
      int number = Integer.parseInt(floorAndNumber[1]);
      
      if (temp.length > 2) {
         if (temp[2].contains(",")) {
            nearRoom = temp[2].split(","); // 각 배열에 727,726 이런 강의실 방번호가 저장이 된다.
         } else {
            nearRoom = new String[1];
            nearRoom[0] = temp[2];
         }
         if (floorHead == null) {
            Floor f = addFirstFloor(floor); // 처음에 층을 생성하고, 그 층을 f에 저장한다.
           f.addFirstNode('N', number, nearNode, nearRoom); // 그 층에 처음으로 node를 추가해준다.

         } else {
            Floor f = addFloor(floor); // f에 층을 저장하고, node들을 추가시켜준다.
            if (f.nodeHead == null) // f에 아직 첫 node가 없을떄
            {
               f.addFirstNode('N', number, nearNode, nearRoom);
            } else { // 첫노드가 있다면
               f.addNode('N', number, nearNode, nearRoom);
            }
         }
      }
      else
      {
         if (floorHead == null) {
            Floor f = addFirstFloor(floor); // 처음에 층을 생성하고, 그 층을 f에 저장한다.
            f.addFirstNode('N', number, nearNode); // 그 층에 처음으로 node를 추가해준다.

         } else {
            Floor f = addFloor(floor); // f에 층을 저장하고, node들을 추가시켜준다.
            if (f.nodeHead == null) // f에 아직 첫 node가 없을떄
            {
               f.addFirstNode('N', number, nearNode);
            } else { // 첫노드가 있다면
               f.addNode('N', number, nearNode);
            }
         }
      }
   }

   public int transformfloorType(String floor) {
      int trans = 0; // String tpye의 변수를 int형으로 바꿔준다.
      if (floor.contains("B")) {
         floor = floor.substring(1);
         trans = Integer.parseInt(floor) * (-1);
      } else {
         trans = Integer.parseInt(floor);
      }
      return trans;
   }

   public Floor addFirstFloor(int floor) {
      // 새로운 층을 생성한다.
      Floor newFloor = new Floor(floor);
      // 원래있던 첫노드를 이 노드가 가르키게한다.
      newFloor.Next = floorHead;
      // 헤드를 이 노드에 넣어논다.
      floorHead = newFloor;
      // size를 1증가 시킨다.
      size++;
      if (floorHead.Next == null) {
         floorTail = floorHead;
      }
      return floorHead;
   }

   public Floor addLastFloor(int floor) {
      // 층을 하나 생성한다.
      Floor newFloor = new Floor(floor);
      // 사이즈가 0 이라면 아무것도 없는 거기때문에 첫 층이다.
      if (size == 0) {
         addFirstFloor(floor);
      } else {
         // 마지막 층의 다음 층을 새 층을 가르키게한다.
         floorTail.Next = newFloor;
         floorTail = newFloor;
         // size 를 1증가시킨다.
         size++;
      }
      return newFloor;
   }

   public Floor addFloor(int floor) {
      // size가 0 이면 첫번째로 들어가는 거기때문에 addFirst를 해준다.
      if (size == 0) {
         return addFirstFloor(floor);
      } else { // size가 0이 아니라면 층을 비교해서 들어온 floor가 더 높은 층이면 다음거랑 비교,같으면 끝, 낮으면 그 층이 없는거므로 만들어서
               // 사이에 넣어준다.
         Floor temp = floorHead; // temp에 head를 넣어 head부터 비교한다.
         if (temp.floor > floor) // 만약 head 부분부터 들어온 floor가 낮다면, 가장 앞에 추가해준다.
         {
            return addFirstFloor(floor);
         } else if (temp.floor == floor) { // 같다면 끝
            return temp;
         } else // 다르다면 다음에 있는 값을 비교
         {
            while (temp.Next != null) {
               if (temp.Next.floor < floor) { // 새로들어온 floor가 크다면 다음 것과 비교
                  temp = temp.Next; //
               } else if (temp.Next.floor == floor) { // 같다면 있으니까 끝
                  return temp.Next;
               } else { // 새로 들어온 floor가 낮은 층이라면 사이에 있는것이 없으므로 만들어서 사이에 넣어준다.
                  Floor newFloor = new Floor(floor);
                  newFloor.Next = temp.Next;
                  temp.Next = newFloor;
                  size++;
                  return newFloor;
               }

            }
            return addLastFloor(floor); // 위에 while문을 빠져나와서 일로 왔다라는 것은 마지막까지 같은것이 없고 마지막보다 큰 층이라는 이야기므로 마지막에 추가해준다.
         }
      }
   }
   public Floor findFloor(int floor) {
	   Floor temp = floorHead;
	   while(temp != null) {
		   if(temp.floor == floor)
		   {
			   return temp;
		   }
		   temp = temp.Next;
	   }
	   return null;
	}
   //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
   //
   //
   ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
   //노드와 강의실의 거리를 저장하기 위해 파일을 열고, line을 읽는다.
   public void readRoomDistanceFile(String roomDistanceFilename)
   {
	   try {
	         File file = new File(roomDistanceFilename); // graph를 그리기 위해 정리한 txt파일을 연다.
	         FileReader fileReader = new FileReader(file);// 파일을 읽기위해서 filereader를 선언한다.
	         BufferedReader bufReader = new BufferedReader(fileReader); // 한줄씩 읽어오기 위해서 버퍼를 생성한다.
	         String line=""; // 한줄씩 읽어온 것을 저장하기 위한 변수
	         while ((line = bufReader.readLine()) != null) { // 한줄씩 읽어오면서 line에 저장하고, line을 통해 graph를 생성한다.
	            System.out.println(line);
	        	addRoomDistance(line);
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
   }
   //읽은 line을 가지고 거리를 더해준다.
   public void addRoomDistance(String line)
   {
	   String[] temp = line.split("/"); // ex) 4N1/410/2.4 이런 것을 /로 나눈다.
	   String[] floorAndNumber = temp[0].split("N"); //맨앞  4N1 저장된 것을 N을 기준으로 나눈다.
	   int floor = transformfloorType(floorAndNumber[0]); //String 인 B6~9층을 Integer형태의 숫자로 변환
	   int num = Integer.parseInt(floorAndNumber[1]);
	   // 읽어온 line이 어느층의노드인지 찾는다.
	   Floor tempfloor = findFloor(floor); //floor를 찾고 tempfloor에 저장시킨다.
	   // line에서 읽어온 노드를 찾는다.
	   Graph.Floor.Node tempNode = tempfloor.getNode('N', num);
	   //node에 해당 강의실의정보가  몇번쨰 인덱스 있는지 확인한다.
	   for(int i =0;tempNode.room[i]!=null;i++){
		   if(tempNode.room[i].equals(temp[1])) {
			   tempNode.distance[i] = Float.parseFloat(temp[2]);
			   break;
		   }
	   } 
   }
   public void readNodeDistanceFile(String nodeDistanceFilename){
	   try {
	         File file = new File(nodeDistanceFilename); // node들간의 거리를 정리한 txt파일을 연다.
	         FileReader fileReader = new FileReader(file);// 파일을 읽기위해서 filereader를 선언한다.
	         BufferedReader bufReader = new BufferedReader(fileReader); // 한줄씩 읽어오기 위해서 버퍼를 생성한다.
	         String line=""; // 한줄씩 읽어온 것을 저장하기 위한 변수
	         while ((line = bufReader.readLine()) != null) { // 한줄씩 읽어오면서 line에 저장하고, line을 통해 graph를 생성한다.
	            System.out.println(line);
	        	addNodeDistance(line);
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
   }
   public void addNodeDistance(String line) {
	// TODO Auto-generated method stub
	   String[] temp = line.split("/"); // ex) 4N1/4N2/2.4F 이런 것을 /로 나눈다.
	   String[] floorAndNumber = temp[0].split("N"); //맨앞  4N1 저장된 것을 N을 기준으로 나눈다.
	   int floor1 = transformfloorType(floorAndNumber[0]); //String 인 B6~9층을 Integer형태의 숫자로 변환
	   int num1 = Integer.parseInt(floorAndNumber[1]);
	  
	   char disc = ' ';
	   int num2 = 0;
	   
	   float distance = Float.parseFloat(temp[2]);
	   
	   // 읽어온 line이 어느층의노드인지 찾는다.
	   Floor tempfloor = findFloor(floor1); //floor를 찾고 tempfloor에 저장시킨다.
	   // line에서 읽어온 노드를 찾는다.
	   Graph.Floor.Node tempNode = tempfloor.getNode('N', num1);
	   //해당 노드 node에 어떤 노드하고 이어져 있는지 확인하고 정보를 num2와 disc에 저장해놓는다.
	   if(temp[1].contains("N")) {
		   String[] floorAndNumber2  = temp[1].split("N");
		   num2 = Integer.parseInt(floorAndNumber2[1]);
		   disc = 'N';
	   }else if(temp[1].contains("E")) {
		   String[] floorAndNumber2  = temp[1].split("E");
		   num2 = Integer.parseInt(floorAndNumber2[1]);
		   disc = 'E';
	   }else if(temp[1].contains("S")) {
		   String[] floorAndNumber2  = temp[1].split("S");
		   num2 = Integer.parseInt(floorAndNumber2[1]);
		   disc = 'S';
	   }else if(temp[1].contains("C")) {
		   String[] floorAndNumber2  = temp[1].split("C");
		   num2 = Integer.parseInt(floorAndNumber2[1]);
		   disc = 'C';
	   }
	   // tempNode 와 인접 해있는 노드들을 확인하면서 num2와 disc와 같은 노드를 발견하면, 거리를 저장시킨다.
	   for(int i = 0; i<tempNode.nearNode.size();i++) {
		   if(tempNode.nearNode.get(i).distinction == disc && tempNode.nearNode.get(i).number == num2) {
			   tempNode.distanceNode.set(i,distance);
			   for(int j=0;j< tempNode.nearNode.get(i).nearNode.size();j++) {
					   if(tempNode.nearNode.get(i).nearNode.get(j).distinction == tempNode.distinction && tempNode.nearNode.get(i).nearNode.get(j).number == tempNode.number) {
						   tempNode.nearNode.get(i).distanceNode.set(j,distance);
						   return;
					   }
			   }
		   }
	   }
	   
	}

}