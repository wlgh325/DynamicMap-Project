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
      // �ش� oject�� data
      private int floor; // ���� 1���� -1 ������ 1���� 1
      // graph�� ����� ���� head,tail
      private Node nodeHead; // �ش����� �Ӹ�
      private Node nodeTail; // �ش����� ����
      // ���� �̵��� ���� ���������Ϳ� ��� ��带 ����
      private Node[] nodeElevate; // ������������ ���� node��.
      private Node[] nodeStair;// ����� ���� node��

      private int nodeSize = 0; // node�� ����� ������

      private Floor Next; // �׷��� ����� ���� ����
      private Floor[] floorNext; // ��� ����� ������ �����
      // �ش� ���� node�� ���ϰ� �̾��ֱ����� �迭
      private boolean[] N = new boolean[MAX_N]; // �� ���Ұ� true��� �̹� �����Ǿ� �ִ� node��� �Ҹ��̴�. ������ false
      private boolean[] S = new boolean[MAX_S]; // �� ���Ұ� true��� �̹� �����Ǿ� �ִ� node��� �Ҹ��̴�. ������ false
      private boolean[] E = new boolean[MAX_E]; // �� ���Ұ� true��� �̹� �����Ǿ� �ִ� node��� �Ҹ��̴�. ������ false
      private boolean[] C = new boolean[MAX_C];
      // ������

      public Floor(int floor) {
         this.floor = floor;
         for (int i = 0; i < MAX_N; i++) { // ó�� �� ���� �����Ǹ� ��� ���� �����Ƿ�, ��� false�� �ʱ�ȭ
            N[i] = false;
         }
         for (int i = 0; i < MAX_S; i++) { // ó�� �� ���� �����Ǹ� ��� ���� �����Ƿ�, ��� false�� �ʱ�ȭ
            S[i] = false;
         }
         for (int i = 0; i < MAX_E; i++) { // ó�� �� ���� �����Ǹ� ��� ���� �����Ƿ�, ��� false�� �ʱ�ȭ
            E[i] = false;
         }
         for (int i = 0; i < MAX_C; i++) { // ó�� �� ���� �����Ǹ� ��� ���� �����Ƿ�, ��� false�� �ʱ�ȭ
             C[i] = false;
          }
      }

      private class Node {
         // �� node�� ����������, ��������������, ������� �������ִ� ������
         private char distinction; // �������̸� n, ���������͸� e, ����̸� s
         private int number; // ���° ������,����������,�������. 7n4 �� number�� 4�� ����.
         // �������� ������,���,���������� ������ ����, ��ũ�� ����Ʈ ���·� ����
         private ArrayList<Float> distanceNode = new ArrayList<>(); // ����� node�̸��� �Ÿ�
         private ArrayList<Node> nearNode = new ArrayList<>(); // ���� node�� ����
         // �������� ���ǽ� ������ ����,�迭�� ���·� ����.
         private String[] room; // �������� ������ room ����
         private float[] distance; // �������� ������ room���� �Ÿ�

         // ������
         public Node(char dis, int num) {
            distinction = dis;
            number = num;
         }
      }

      // ó�� ������ node�� ���� ���� �Լ�
      public void addFirstNode(char dis, int num, String[] nearNode, String[] nearRoom) {// ���ο� node�� �����Ѵ�.
         // ù node�� �����Ѵ�.
         Node newNode = new Node(dis, num);
         changeExistArray(dis, num);
         // node�� �����Ǽ� nodeSize�� 1 ������Ų��.
         nodeSize++;
         // ���� ������ ���θ��� newNode��� �����Ѵ�.
         nodeHead = newNode;
         nodeTail = nodeHead;
         // ����� ���ǽ��� �迭�� �����Ѵ�.
         if (nearRoom.length > 0) {
            newNode.room = Arrays.copyOf(nearRoom, nearRoom.length); // ���Ͽ��� �о�� �������ǽ��� �迭�� �����Ѵ�.
            newNode.distance = new float[nearRoom.length]; // distance�� �迭�� ���ڸ� ���Ͽ��� �Ͼ�� room�� ������ �ʱ�ȭ�Ѵ�.
            for (int i = 0; i < newNode.distance.length; i++) { // ���Ƿ� �Ÿ��� �ʱ�ȭ�Ѵ�. ���߿� �ٲ���Ѵ�.%%%
               newNode.distance[i] = 0;
            }
         }
         // ������ node�� �����ϰ� ����Ʈ���·� �̾��ش�.
         for (int i = 0; nearNode[i] != null; i++) { // ���Ͽ��� �о�� ����node���� ��Ǿ�� �����Ѵ�.
            // ���� node temp�� �����Ѵ�.
            Node temp = new Node(nearNode[i].charAt(0), Integer.parseInt(nearNode[i].substring(1))); // temp���
                                                                                 // newNode��
                                                                                 // ������带
                                                                                 // �����Ѵ�.
            changeExistArray(nearNode[i].charAt(0), Integer.parseInt(nearNode[i].substring(1)));
            // new node ->����node �����Ѵ�.
            newNode.distanceNode.add((float) 0); // newNode�� temp�� ���� ������ �־��ش�.
            newNode.nearNode.add(temp);// ���� ������ arraylist�� �߰��Ѵ�.
            // ���� node ���� newnode�� �־��ش�.
            // String temp2 = newNode.distinction + newNode.number +"";
            temp.distanceNode.add((float) 0); // temp�� newNode�� ���� ������ �־��ش�.
            temp.nearNode.add(newNode); // ��������� �̾���� �ϹǷ� �ٽ� temp���� newNode�� �̾��ְ��Ѵ�.

            nodeSize++;
         }
      }

      public void addFirstNode(char dis, int num, String[] nearNode) {// ���ο� node�� �����Ѵ�.
         // ù node�� �����Ѵ�.
         Node newNode = new Node(dis, num);
         changeExistArray(dis, num);
         
         // node�� �����Ǽ� nodeSize�� 1 ������Ų��.
         nodeSize++;
         
         // ���� ������ ���θ��� newNode��� �����Ѵ�.
         nodeHead = newNode;
         nodeTail = nodeHead;
         
         // ������ node�� �����ϰ� ����Ʈ���·� �̾��ش�.
         for (int i = 0; nearNode[i] != null; i++) { // ���Ͽ��� �о�� ����node���� dictionary�� �����Ѵ�.
            // ���� node temp�� �����Ѵ�.
            Node temp = new Node(nearNode[i].charAt(0), Integer.parseInt(nearNode[i].substring(1, nearNode[i].length()))); // temp���
                                                                                 // newNode��
                                                                                 // ������带
                                                                                 // �����Ѵ�.
            changeExistArray(nearNode[i].charAt(0), Integer.parseInt(nearNode[i].substring(1)));
            // new node ->����node �����Ѵ�.
            newNode.distanceNode.add((float) 0); // newNode�� temp�� ���� ������ �־��ش�.
            newNode.nearNode.add(temp);// ���� ������ arraylist�� �߰��Ѵ�.
            // ���� node ���� newnode�� �־��ش�.
            // String temp2 = newNode.distinction + newNode.number +"";
            temp.distanceNode.add((float) 0); // temp�� newNode�� ���� ������ �־��ش�.
            temp.nearNode.add(newNode); // ��������� �̾���� �ϹǷ� �ٽ� temp���� newNode�� �̾��ְ��Ѵ�.

            nodeSize++;
         }
      }

      public void addNode(char dis, int num, String[] nearNode, String[] nearRoom) {
         if (existNode(dis, num)) { // �ش� node�� �����ϸ�, ���� ����������, �� ���� �̵��Ѵ�. �׸��� �����Ѵ�.
            Node thisNode = getNode(dis, num);
            // tail�� thisNode��� �����Ѵ�.
            nodeTail = thisNode;
            // ����� ���ǽ� �迭�� �����Ѵ�.
            if (nearRoom.length > 0) {
               thisNode.room = Arrays.copyOf(nearRoom, nearRoom.length);
               thisNode.distance = new float[nearRoom.length];
               for (int i = 0; i < thisNode.distance.length; i++) { // ���Ƿ� �Ÿ��� �ʱ�ȭ�Ѵ�. ���߿� �ٲ���Ѵ�.%%%
                  thisNode.distance[i] = (float)0;
               }
            }
            // ������ node�� �����ϰ� ����Ʈ���·� �̾��ش�.
            for (int i = 0; nearNode[i] != null; i++) { // ���Ͽ��� �о�� ����node���� ��Ǿ�� �����Ѵ�.
               if (existNode(nearNode[i].charAt(0), Integer.parseInt(nearNode[i].substring(1)))) // graph�� ���������
                                                                              // �����Ѵٸ�,
                                                                              // �����ϵ����Ѵ�.
               {
                  if (isAlreadyConnected(thisNode, nearNode[i].charAt(0),
                        Integer.parseInt(nearNode[i].substring(1))) == false)// �̹� �̾����ִ� ���� �ѱ��, �������ִٸ� �̾��ش�.
                  {
                     // this -> ���� node�� ����
                     Node temp = getNode(nearNode[i].charAt(0),Integer.parseInt(nearNode[i].substring(1))); // ���� node�� temp�� ����
                     thisNode.distanceNode.add((float) 0); // thisNode�� temp�� ���� �Ÿ������� �־��ش�.
                     thisNode.nearNode.add(temp);// ���� node���� arraylist�� �߰��Ѵ�.
                     // ���� node���� this�� ����
                     temp.distanceNode.add((float) 0);
                     temp.nearNode.add(thisNode);
                  }
               } else // graph�� ��������� ���ٸ� node�� �����ϰ�, �����Ѵ�.
               {
                  // ���� node temp�� �����Ѵ�.
                  Node temp = new Node(nearNode[i].charAt(0), Integer.parseInt(nearNode[i].substring(1))); // temp���thisNode��
                                                                                       // ������带
                                                                                       // �����Ѵ�.
                  changeExistArray(nearNode[i].charAt(0), Integer.parseInt(nearNode[i].substring(1)));
                  // this -> ���� node�� ����
                  thisNode.distanceNode.add((float) 0); // thisNode�� temp�� ���� �Ÿ������� �־��ش�.
                  thisNode.nearNode.add(temp);// ���� node���� arraylist�� �߰��Ѵ�.
                  // ���� node���� this�� ����
                  temp.distanceNode.add((float) 0);
                  temp.nearNode.add(thisNode);
                  // node�� �ϳ� �߰������Ƿ� nodeSize�� 1������Ų��.
                  nodeSize++;

               }
            }
         }
      }

      public void addNode(char dis, int num, String[] nearNode) {
         if (existNode(dis, num)) { // �ش� node�� �����ϸ�, ���� ����������, �� ���� �̵��Ѵ�. �׸��� �����Ѵ�.
            Node thisNode = getNode(dis, num);
            // tail�� thisNode��� �����Ѵ�.
            nodeTail = thisNode;
            // ������ node�� �����ϰ� ����Ʈ���·� �̾��ش�.
            for (int i = 0; nearNode[i] != null; i++) { // ���Ͽ��� �о�� ����node���� ��Ǿ�� �����Ѵ�.
               if (existNode(nearNode[i].charAt(0), Integer.parseInt(nearNode[i].substring(1, nearNode[i].length())))) // graph�� ���������
                                                                              // �����Ѵٸ�,
                                                                              // �����ϵ����Ѵ�.
               {
                  if (isAlreadyConnected(thisNode, nearNode[i].charAt(0),
                        Integer.parseInt(nearNode[i].substring(1))) == false)// �̹� �̾����ִ� ���� �ѱ��, �������ִٸ� �̾��ش�.
                  {
                     // this -> ���� node�� ����
                     Node temp = getNode(nearNode[i].charAt(0),Integer.parseInt(nearNode[i].substring(1))); // ���� node�� temp�� ����
                     thisNode.distanceNode.add((float) 0); // thisNode�� temp�� ���� �Ÿ������� �־��ش�.
                     thisNode.nearNode.add(temp);// ���� node���� arraylist�� �߰��Ѵ�.
                     // ���� node���� this�� ����
                     temp.distanceNode.add((float) 0);
                     temp.nearNode.add(thisNode);
                  }
               } else // graph�� ��������� ���ٸ� node�� �����ϰ�, �����Ѵ�.
               {
                  // ���� node temp�� �����Ѵ�.
                  Node temp = new Node(nearNode[i].charAt(0), Integer.parseInt(nearNode[i].substring(1))); // temp���thisNode��
                                                                                       // ������带
                                                                                       // �����Ѵ�.
                  changeExistArray(nearNode[i].charAt(0), Integer.parseInt(nearNode[i].substring(1)));
                  // this -> ���� node�� ����
                  thisNode.distanceNode.add((float) 0); // thisNode�� temp�� ���� �Ÿ������� �־��ش�.
                  thisNode.nearNode.add(temp);// ���� node���� arraylist�� �߰��Ѵ�.
                  // ���� node���� this�� ����
                  temp.distanceNode.add((float) 0);
                  temp.nearNode.add(thisNode);
                  // node�� �ϳ� �߰������Ƿ� nodeSize�� 1������Ų��.
                  nodeSize++;

               }
            }
         }
      }

      public Node getNode(char dis, int num) { // �ش� dis,num���� graph�� ���� ��尡 �ִٸ�, dfs������� ã�Ƽ� �ش� ��带 ������ش�.
         // dfs�� �Ҷ��� node�� ��ȴ��� Ȯ�����ִ� ����
         boolean[] N_DFS = new boolean[MAX_N]; // �� ���Ұ� true��� �̹� ������ ���̰�, false�� ���� Ž������ ������
         boolean[] S_DFS = new boolean[MAX_S]; // �� ���Ұ� true��� �̹� ������ ���̰�, false�� ���� Ž������ ������
         boolean[] E_DFS = new boolean[MAX_E]; // �� ���Ұ� true��� �̹� ������ ���̰�, false�� ���� Ž������ ������
         boolean[] C_DFS = new boolean[MAX_C]; // �� ���Ұ� true��� �̹� ������ ���̰�, false�� ���� Ž������ ������
         Node root = nodeHead;
         Stack<Node> stack = new Stack<Node>();
         N_DFS[root.number] = true;
         stack.push(root);
         while (!stack.isEmpty()) {
            Node r = stack.pop();
            for (int i = 0; i < r.nearNode.size(); i++) {
               Node n = r.nearNode.get(i);
               if (n.distinction == dis && n.number == num) {
                  return n; // dis�� num�� ��ġ�ϴ� node�� return���ش�.
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
         System.out.println("���� :�ش� graph���� node�� ã�� �� �����ϴ�.");
         return null;
      }

      public boolean isAlreadyConnected(Node temp, char dis, int num) { // �̹� �ش� ����ϰ� �̾����ִ� �����, treu, �ƴϸ� false��
                                                         // ��ȯ�Ѵ�.
         for (int i = 0; i < temp.nearNode.size(); i++) {
            if (temp.nearNode.get(i).distinction == dis && temp.nearNode.get(i).number == num) {
               return true;
            }
         }
         return false;
      }

      public boolean existNode(char dis, int num) {// �ش� ��尡 �ִٸ� �ٽ� ������ �ʿ䰡 ����. ������ true ������ false
         if (dis == 'N') { // ������ ���� ��
            if (N[num] == true) { // �̹� node�� �ִ� ���
               return true;
            }
            return false;
         } else if (dis == 'S') { // ������� ��
            if (S[num] == true) { // �̹� node�� �ִ� ���
               return true;
            }
            return false;
         } else if (dis == 'E') { // �������������� ��
            if (E[num] == true) { // �̹� node�� �ִ� ���
               return true;
            }
            return false;
         }else if (dis == 'C') { // �������������� ��
             if (C[num] == true) { // �̹� node�� �ִ� ���
                 return true;
              }
              return false;
           }
         System.out.println( dis +""+  num + "�� �ùٸ� node �������� Ȯ�����ּ���");
         return false; // ����

      }

      public void changeExistArray(char dis, int num) {// �ش� ��尡 �����̵Ǹ� false�� �ٲ��ش�.
         if (dis == 'N') { // ������ ���� ��
            if (N[num] == false) { // ���ٸ� �ִ°ɷ� �ٲ��ش�.
               N[num] = true;
               return;
            }
         } else if (dis == 'S') { // ������� ��
            if (S[num] == false) { // ���ٸ� �ִ°ɷ� �ٲ��ش�.
               S[num] = true;
               return;
            }
         } else if (dis == 'E') { // �������������� ��
            if (E[num] == false) { // ���ٸ� �ִ°ɷ� �ٲ��ش�.
               E[num] = true;
               return;
            }
         } else if (dis == 'C') { // �������������� ��
             if (C[num] == false) { // ���ٸ� �ִ°ɷ� �ٲ��ش�.
                 C[num] = true;
                 return;
              }
           }
         System.out.println(dis + num + "�� �ùٸ� node �������� Ȯ�����ּ���");
         return; // ����

      }
   }

   public int readFile(String filename) {
      try {
         File file = new File(filename); // graph�� �׸��� ���� ������ txt������ ����.
         FileReader fileReader = new FileReader(file);// ������ �б����ؼ� filereader�� �����Ѵ�.
         BufferedReader bufReader = new BufferedReader(fileReader); // ���پ� �о���� ���ؼ� ���۸� �����Ѵ�.
         String line=""; // ���پ� �о�� ���� �����ϱ� ���� ����
         while ((line = bufReader.readLine()) != null) { // ���پ� �о���鼭 line�� �����ϰ�, line�� ���� graph�� �����Ѵ�.
            System.out.println(line);
        	makeGraph(line);
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
      return -1;
   }

   private void makeGraph(String line) {
      // TODO Auto-generated method stub
	  final int NEAR_NODE_NUM = 10;
      String[] temp = line.split("/");
      String[] floorAndNumber = temp[0].split("N"); // [0]-> floor�� ����, [1] -> ������ ����
      String[] nearNode = new String[NEAR_NODE_NUM];
      
      
      for(int i=0; i<temp.length; i++) {
    	  System.out.print(temp[i] + ",");
      }
      
      //nearNode ���� ����
      if(temp[1].contains(",")) {	//nearNode�� �������� ���
    	  String[] tempNode = temp[1].split(","); // ���� nearNode[0] -> N7 or S1 or E1 �̷������� ������ �Ǿ��ִ�.
    	  for(int i=0; i<tempNode.length; i++)
    		  nearNode[i] = tempNode[i];
    	  for(int i=tempNode.length; i<NEAR_NODE_NUM; i++)
    		  nearNode[i] = null;
      }
      else	//nearNode�� �ϳ� �ۿ� ������� ó��
    	  nearNode[0] = temp[1];
      
      String[] nearRoom = null;
      int floor = transformfloorType(floorAndNumber[0]);
      int number = Integer.parseInt(floorAndNumber[1]);
      
      if (temp.length > 2) {
         if (temp[2].contains(",")) {
            nearRoom = temp[2].split(","); // �� �迭�� 727,726 �̷� ���ǽ� ���ȣ�� ������ �ȴ�.
         } else {
            nearRoom = new String[1];
            nearRoom[0] = temp[2];
         }
         if (floorHead == null) {
            Floor f = addFirstFloor(floor); // ó���� ���� �����ϰ�, �� ���� f�� �����Ѵ�.
           f.addFirstNode('N', number, nearNode, nearRoom); // �� ���� ó������ node�� �߰����ش�.

         } else {
            Floor f = addFloor(floor); // f�� ���� �����ϰ�, node���� �߰������ش�.
            if (f.nodeHead == null) // f�� ���� ù node�� ������
            {
               f.addFirstNode('N', number, nearNode, nearRoom);
            } else { // ù��尡 �ִٸ�
               f.addNode('N', number, nearNode, nearRoom);
            }
         }
      }
      else
      {
         if (floorHead == null) {
            Floor f = addFirstFloor(floor); // ó���� ���� �����ϰ�, �� ���� f�� �����Ѵ�.
            f.addFirstNode('N', number, nearNode); // �� ���� ó������ node�� �߰����ش�.

         } else {
            Floor f = addFloor(floor); // f�� ���� �����ϰ�, node���� �߰������ش�.
            if (f.nodeHead == null) // f�� ���� ù node�� ������
            {
               f.addFirstNode('N', number, nearNode);
            } else { // ù��尡 �ִٸ�
               f.addNode('N', number, nearNode);
            }
         }
      }
   }

   public int transformfloorType(String floor) {
      int trans = 0; // String tpye�� ������ int������ �ٲ��ش�.
      if (floor.contains("B")) {
         floor = floor.substring(1);
         trans = Integer.parseInt(floor) * (-1);
      } else {
         trans = Integer.parseInt(floor);
      }
      return trans;
   }

   public Floor addFirstFloor(int floor) {
      // ���ο� ���� �����Ѵ�.
      Floor newFloor = new Floor(floor);
      // �����ִ� ù��带 �� ��尡 ����Ű���Ѵ�.
      newFloor.Next = floorHead;
      // ��带 �� ��忡 �־���.
      floorHead = newFloor;
      // size�� 1���� ��Ų��.
      size++;
      if (floorHead.Next == null) {
         floorTail = floorHead;
      }
      return floorHead;
   }

   public Floor addLastFloor(int floor) {
      // ���� �ϳ� �����Ѵ�.
      Floor newFloor = new Floor(floor);
      // ����� 0 �̶�� �ƹ��͵� ���� �ű⶧���� ù ���̴�.
      if (size == 0) {
         addFirstFloor(floor);
      } else {
         // ������ ���� ���� ���� �� ���� ����Ű���Ѵ�.
         floorTail.Next = newFloor;
         floorTail = newFloor;
         // size �� 1������Ų��.
         size++;
      }
      return newFloor;
   }

   public Floor addFloor(int floor) {
      // size�� 0 �̸� ù��°�� ���� �ű⶧���� addFirst�� ���ش�.
      if (size == 0) {
         return addFirstFloor(floor);
      } else { // size�� 0�� �ƴ϶�� ���� ���ؼ� ���� floor�� �� ���� ���̸� �����Ŷ� ��,������ ��, ������ �� ���� ���°ŹǷ� ����
               // ���̿� �־��ش�.
         Floor temp = floorHead; // temp�� head�� �־� head���� ���Ѵ�.
         if (temp.floor > floor) // ���� head �κк��� ���� floor�� ���ٸ�, ���� �տ� �߰����ش�.
         {
            return addFirstFloor(floor);
         } else if (temp.floor == floor) { // ���ٸ� ��
            return temp;
         } else // �ٸ��ٸ� ������ �ִ� ���� ��
         {
            while (temp.Next != null) {
               if (temp.Next.floor < floor) { // ���ε��� floor�� ũ�ٸ� ���� �Ͱ� ��
                  temp = temp.Next; //
               } else if (temp.Next.floor == floor) { // ���ٸ� �����ϱ� ��
                  return temp.Next;
               } else { // ���� ���� floor�� ���� ���̶�� ���̿� �ִ°��� �����Ƿ� ���� ���̿� �־��ش�.
                  Floor newFloor = new Floor(floor);
                  newFloor.Next = temp.Next;
                  temp.Next = newFloor;
                  size++;
                  return newFloor;
               }

            }
            return addLastFloor(floor); // ���� while���� �������ͼ� �Ϸ� �Դٶ�� ���� ���������� �������� ���� ���������� ū ���̶�� �̾߱�Ƿ� �������� �߰����ش�.
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
   //���� ���ǽ��� �Ÿ��� �����ϱ� ���� ������ ����, line�� �д´�.
   public void readRoomDistanceFile(String roomDistanceFilename)
   {
	   try {
	         File file = new File(roomDistanceFilename); // graph�� �׸��� ���� ������ txt������ ����.
	         FileReader fileReader = new FileReader(file);// ������ �б����ؼ� filereader�� �����Ѵ�.
	         BufferedReader bufReader = new BufferedReader(fileReader); // ���پ� �о���� ���ؼ� ���۸� �����Ѵ�.
	         String line=""; // ���پ� �о�� ���� �����ϱ� ���� ����
	         while ((line = bufReader.readLine()) != null) { // ���پ� �о���鼭 line�� �����ϰ�, line�� ���� graph�� �����Ѵ�.
	            System.out.println(line);
	        	addRoomDistance(line);
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
   }
   //���� line�� ������ �Ÿ��� �����ش�.
   public void addRoomDistance(String line)
   {
	   String[] temp = line.split("/"); // ex) 4N1/410/2.4 �̷� ���� /�� ������.
	   String[] floorAndNumber = temp[0].split("N"); //�Ǿ�  4N1 ����� ���� N�� �������� ������.
	   int floor = transformfloorType(floorAndNumber[0]); //String �� B6~9���� Integer������ ���ڷ� ��ȯ
	   int num = Integer.parseInt(floorAndNumber[1]);
	   // �о�� line�� ������ǳ������ ã�´�.
	   Floor tempfloor = findFloor(floor); //floor�� ã�� tempfloor�� �����Ų��.
	   // line���� �о�� ��带 ã�´�.
	   Graph.Floor.Node tempNode = tempfloor.getNode('N', num);
	   //node�� �ش� ���ǽ���������  ����� �ε��� �ִ��� Ȯ���Ѵ�.
	   for(int i =0;tempNode.room[i]!=null;i++){
		   if(tempNode.room[i].equals(temp[1])) {
			   tempNode.distance[i] = Float.parseFloat(temp[2]);
			   break;
		   }
	   } 
   }
   public void readNodeDistanceFile(String nodeDistanceFilename){
	   try {
	         File file = new File(nodeDistanceFilename); // node�鰣�� �Ÿ��� ������ txt������ ����.
	         FileReader fileReader = new FileReader(file);// ������ �б����ؼ� filereader�� �����Ѵ�.
	         BufferedReader bufReader = new BufferedReader(fileReader); // ���پ� �о���� ���ؼ� ���۸� �����Ѵ�.
	         String line=""; // ���پ� �о�� ���� �����ϱ� ���� ����
	         while ((line = bufReader.readLine()) != null) { // ���پ� �о���鼭 line�� �����ϰ�, line�� ���� graph�� �����Ѵ�.
	            System.out.println(line);
	        	addNodeDistance(line);
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
   }
   public void addNodeDistance(String line) {
	// TODO Auto-generated method stub
	   String[] temp = line.split("/"); // ex) 4N1/4N2/2.4F �̷� ���� /�� ������.
	   String[] floorAndNumber = temp[0].split("N"); //�Ǿ�  4N1 ����� ���� N�� �������� ������.
	   int floor1 = transformfloorType(floorAndNumber[0]); //String �� B6~9���� Integer������ ���ڷ� ��ȯ
	   int num1 = Integer.parseInt(floorAndNumber[1]);
	  
	   char disc = ' ';
	   int num2 = 0;
	   
	   float distance = Float.parseFloat(temp[2]);
	   
	   // �о�� line�� ������ǳ������ ã�´�.
	   Floor tempfloor = findFloor(floor1); //floor�� ã�� tempfloor�� �����Ų��.
	   // line���� �о�� ��带 ã�´�.
	   Graph.Floor.Node tempNode = tempfloor.getNode('N', num1);
	   //�ش� ��� node�� � ����ϰ� �̾��� �ִ��� Ȯ���ϰ� ������ num2�� disc�� �����س��´�.
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
	   // tempNode �� ���� ���ִ� ������ Ȯ���ϸ鼭 num2�� disc�� ���� ��带 �߰��ϸ�, �Ÿ��� �����Ų��.
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