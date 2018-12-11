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

	class Floor {
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

		public class Node {
			// 이 node가 교차점인지, 엘레베이터인지, 계단인지 구분해주는 변수들
			private char distinction; // 교차점이면 n, 엘레베이터면 e, 계단이면 s
			private int number; // 몇번째 교차점,엘레베이터,계단인지. 7n4 면 number에 4가 들어간다.
			// 교차점과 교차점,계단,엘레베이터 사이의 관계, 링크드 리스트 형태로 저장
			private ArrayList<Float> distanceNode = new ArrayList<>(); // 연결된 node이름과 거리
			private ArrayList<Node> nearNode = new ArrayList<>(); // 인접 node를 연결
			// 교차점과 강의실 사이의 관계,배열의 형태로 저장.
			private String[] room; // 교차점과 인접한 room 정보
			private float[] distance; // 교차점과 인접한 room과의 거리
			// 엘레베이터 이동을 이어주기위한 변수들
			private int[][] floorConnect;
			// 에스컬레이터,계단을 위한 변수
			// 위층 정보
			int upfloor; // 1개층이면 1, 2개층이면 2
			float upFloorDistance;
			private Node upfloorNode;

			// 아랫층 정보
			int downfloor; // 1개층이면 1, 2개층이면 2
			float downFloorDistance;
			private Node downfloorNode;

			// 생성자
			public Node(char dis, int num) {
				distinction = dis;
				number = num;
				if (dis == 'E' && (num == 1 || num == 2)) {

					floorConnect = new int[4][];
				} else if (dis == 'E' && num == 3) {
					floorConnect = new int[2][];
				}

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
				Node temp = new Node(nearNode[i].charAt(0),
						Integer.parseInt(nearNode[i].substring(1, nearNode[i].length()))); // temp라는
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
						thisNode.distance[i] = (float) 0;
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
							Node temp = getNode(nearNode[i].charAt(0), Integer.parseInt(nearNode[i].substring(1))); // 인접
																													// node를
																													// temp라
																													// 지정
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
					if (existNode(nearNode[i].charAt(0),
							Integer.parseInt(nearNode[i].substring(1, nearNode[i].length())))) // graph에 인접행렬이
					// 존재한다면,
					// 연결하도록한다.
					{
						if (isAlreadyConnected(thisNode, nearNode[i].charAt(0),
								Integer.parseInt(nearNode[i].substring(1))) == false)// 이미 이어져있는 경우는 넘기고, 안이져있다면 이어준다.
						{
							// this -> 인접 node로 연결
							Node temp = getNode(nearNode[i].charAt(0), Integer.parseInt(nearNode[i].substring(1))); // 인접
																													// node를
																													// temp라
																													// 지정
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
			} else if (dis == 'C') { // 엘레베이터인지 비교
				if (C[num] == true) { // 이미 node가 있는 경우
					return true;
				}
				return false;
			}
			System.out.println(dis + "" + num + "가 올바른 node 정보인지 확인해주세요");
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
			String line = ""; // 한줄씩 읽어온 것을 저장하기 위한 변수
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

		for (int i = 0; i < temp.length; i++) {
			System.out.print(temp[i] + ",");
		}

		// nearNode 정보 저장
		if (temp[1].contains(",")) { // nearNode가 여러개인 경우
			String[] tempNode = temp[1].split(","); // 각각 nearNode[0] -> N7 or S1 or E1 이런식으로 저장이 되어있다.
			for (int i = 0; i < tempNode.length; i++)
				nearNode[i] = tempNode[i];
			for (int i = tempNode.length; i < NEAR_NODE_NUM; i++)
				nearNode[i] = null;
		} else // nearNode가 하나 밖에 없을경우 처리
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
		} else {
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
		while (temp != null) {
			if (temp.floor == floor) {
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
	// 노드와 강의실의 거리를 저장하기 위해 파일을 열고, line을 읽는다.
	public void readRoomDistanceFile(String roomDistanceFilename) {
		try {
			File file = new File(roomDistanceFilename); // graph를 그리기 위해 정리한 txt파일을 연다.
			FileReader fileReader = new FileReader(file);// 파일을 읽기위해서 filereader를 선언한다.
			BufferedReader bufReader = new BufferedReader(fileReader); // 한줄씩 읽어오기 위해서 버퍼를 생성한다.
			String line = ""; // 한줄씩 읽어온 것을 저장하기 위한 변수
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

	// 읽은 line을 가지고 거리를 더해준다.
	public void addRoomDistance(String line) {
		String[] temp = line.split("/"); // ex) 4N1/410/2.4 이런 것을 /로 나눈다.
		String[] floorAndNumber = temp[0].split("N"); // 맨앞 4N1 저장된 것을 N을 기준으로 나눈다.
		int floor = transformfloorType(floorAndNumber[0]); // String 인 B6~9층을 Integer형태의 숫자로 변환
		int num = Integer.parseInt(floorAndNumber[1]);
		// 읽어온 line이 어느층의노드인지 찾는다.
		Floor tempfloor = findFloor(floor); // floor를 찾고 tempfloor에 저장시킨다.
		// line에서 읽어온 노드를 찾는다.
		Graph.Floor.Node tempNode = tempfloor.getNode('N', num);
		// node에 해당 강의실의정보가 몇번쨰 인덱스 있는지 확인한다.
		for (int i = 0; tempNode.room[i] != null; i++) {
			if (tempNode.room[i].equals(temp[1])) {
				tempNode.distance[i] = Float.parseFloat(temp[2]);
				break;
			}
		}
	}

	public void readNodeDistanceFile(String nodeDistanceFilename) {
		try {
			File file = new File(nodeDistanceFilename); // node들간의 거리를 정리한 txt파일을 연다.
			FileReader fileReader = new FileReader(file);// 파일을 읽기위해서 filereader를 선언한다.
			BufferedReader bufReader = new BufferedReader(fileReader); // 한줄씩 읽어오기 위해서 버퍼를 생성한다.
			String line = ""; // 한줄씩 읽어온 것을 저장하기 위한 변수
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
		String[] floorAndNumber = temp[0].split("N"); // 맨앞 4N1 저장된 것을 N을 기준으로 나눈다.
		int floor1 = transformfloorType(floorAndNumber[0]); // String 인 B6~9층을 Integer형태의 숫자로 변환
		int num1 = Integer.parseInt(floorAndNumber[1]);

		char disc = ' ';
		int num2 = 0;

		float distance = Float.parseFloat(temp[2]);

		// 읽어온 line이 어느층의노드인지 찾는다.
		Floor tempfloor = findFloor(floor1); // floor를 찾고 tempfloor에 저장시킨다.
		// line에서 읽어온 노드를 찾는다.
		Graph.Floor.Node tempNode = tempfloor.getNode('N', num1);
		// 해당 노드 node에 어떤 노드하고 이어져 있는지 확인하고 정보를 num2와 disc에 저장해놓는다.
		if (temp[1].contains("N")) {
			String[] floorAndNumber2 = temp[1].split("N");
			num2 = Integer.parseInt(floorAndNumber2[1]);
			disc = 'N';
		} else if (temp[1].contains("E")) {
			String[] floorAndNumber2 = temp[1].split("E");
			num2 = Integer.parseInt(floorAndNumber2[1]);
			disc = 'E';
		} else if (temp[1].contains("S")) {
			String[] floorAndNumber2 = temp[1].split("S");
			num2 = Integer.parseInt(floorAndNumber2[1]);
			disc = 'S';
		} else if (temp[1].contains("C")) {
			String[] floorAndNumber2 = temp[1].split("C");
			num2 = Integer.parseInt(floorAndNumber2[1]);
			disc = 'C';
		}
		// tempNode 와 인접 해있는 노드들을 확인하면서 num2와 disc와 같은 노드를 발견하면, 거리를 저장시킨다.
		for (int i = 0; i < tempNode.nearNode.size(); i++) {
			if (tempNode.nearNode.get(i).distinction == disc && tempNode.nearNode.get(i).number == num2) {
				tempNode.distanceNode.set(i, distance);
				for (int j = 0; j < tempNode.nearNode.get(i).nearNode.size(); j++) {
					if (tempNode.nearNode.get(i).nearNode.get(j).distinction == tempNode.distinction
							&& tempNode.nearNode.get(i).nearNode.get(j).number == tempNode.number) {
						tempNode.nearNode.get(i).distanceNode.set(j, distance);
						return;
					}
				}
			}
		}

	}

	public void readFloorDistance(String floorDistanceFilename) {
		try {
			File file = new File(floorDistanceFilename); // node들간의 거리를 정리한 txt파일을 연다.
			FileReader fileReader = new FileReader(file);// 파일을 읽기위해서 filereader를 선언한다.
			BufferedReader bufReader = new BufferedReader(fileReader); // 한줄씩 읽어오기 위해서 버퍼를 생성한다.
			String line = ""; // 한줄씩 읽어온 것을 저장하기 위한 변수
			while ((line = bufReader.readLine()) != null) { // 한줄씩 읽어오면서 line에 저장하고, line을 통해 graph를 생성한다.
				System.out.println(line);
				addFloorDistance(line);
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

	public void addFloorDistance(String line) {
		String[] temp = line.split("/"); // ex) 4N1/4N2/2.4F 이런 것을 /로 나눈다.
		// temp[0]에 들어있는 노드
		int floor1 = 0; // String 인 B6~9층을 Integer형태의 숫자로 변환
		char disc1 = ' ';
		int num1 = 0;
		// temp[1] 에 들어있는 노드
		int floor2 = 0;
		char disc2 = ' ';
		int num2 = 0;

		float distance = Float.parseFloat(temp[2]);
		// temp[0]의 노드의 정보를 num1 disc1에 저장한다.
		if (temp[0].contains("S")) {
			String[] floorAndNumber1 = temp[0].split("S");
			floor1 = transformfloorType(floorAndNumber1[0]);
			num1 = Integer.parseInt(floorAndNumber1[1]);
			disc1 = 'S';
		} else if (temp[0].contains("C")) {
			String[] floorAndNumber1 = temp[0].split("C");
			floor1 = transformfloorType(floorAndNumber1[0]);
			num1 = Integer.parseInt(floorAndNumber1[1]);
			disc1 = 'C';
		}
		// temp[0]가 어느층의노드인지 찾는다.
		Floor tempfloor1 = findFloor(floor1); // floor를 찾고 tempfloor에 저장시킨다.
		// temp[0]가 그층의 어떤 노드를 찾는다.
		Graph.Floor.Node tempNode1 = tempfloor1.getNode(disc1, num1);

		// 해당 node에 어떤 노드하고 이어져 있는지 확인하고 정보를 num2와 disc2에 저장해놓는다.
		if (temp[1].contains("S")) {
			String[] floorAndNumber2 = temp[1].split("S");
			floor2 = transformfloorType(floorAndNumber2[0]);
			num2 = Integer.parseInt(floorAndNumber2[1]);
			disc2 = 'S';
		} else if (temp[1].contains("C")) {
			String[] floorAndNumber2 = temp[1].split("C");
			floor2 = transformfloorType(floorAndNumber2[0]);
			num2 = Integer.parseInt(floorAndNumber2[1]);
			disc2 = 'C';
		}
		// temp[1]가 어느층의노드인지 찾는다.
		Floor tempfloor2 = findFloor(floor2); // floor를 찾고 tempfloor에 저장시킨다.
		// temp[1]가 그층의 어떤 노드를 찾는다.
		Graph.Floor.Node tempNode2 = tempfloor2.getNode(disc2, num2);
		// tempNode1 와 tempNode2의 층간 연결을한다.
		if (disc1 == 'C') {
			if (floor1 - floor2 < 0) {
				tempNode1.upfloor = floor2 - floor1;
				tempNode1.upfloorNode = tempNode2;
				tempNode1.upFloorDistance = Float.parseFloat(temp[2]);
			} else if (floor1 - floor2 == 0) {
				tempNode1.nearNode.add(tempNode2);
				tempNode1.distanceNode.add(Float.parseFloat(temp[2]));
				tempNode2.nearNode.add(tempNode1);
				tempNode2.distanceNode.add(Float.parseFloat(temp[2]));
			} else {
				System.out.println("오류입니다.");
			}
		} else if (disc1 == 'S') {
			if (floor1 - floor2 < 0) {
				// up
				tempNode1.upfloor = floor2 - floor1;
				tempNode1.upfloorNode = tempNode2;
				tempNode1.upFloorDistance = Float.parseFloat(temp[2]);
				// down
				tempNode2.downfloor = floor2 - floor1;
				tempNode2.downfloorNode = tempNode1;
				tempNode2.downFloorDistance = Float.parseFloat(temp[2]);
			} else if (floor1 - floor2 == 0) {
				tempNode1.nearNode.add(tempNode2);
				tempNode1.distanceNode.add(Float.parseFloat(temp[2]));
				tempNode2.nearNode.add(tempNode1);
				tempNode2.distanceNode.add(Float.parseFloat(temp[2]));
			} else if (floor1 - floor2 > 0) {
				// up
				tempNode2.upfloor = floor1 - floor2;
				tempNode2.upfloorNode = tempNode1;
				tempNode2.upFloorDistance = Float.parseFloat(temp[2]);
				// down
				tempNode1.downfloor = floor1 - floor2;
				tempNode1.downfloorNode = tempNode2;
				tempNode1.downFloorDistance = Float.parseFloat(temp[2]);
			}
		}
	}

	public String sortPath(String startPoint, String endPoint) { // argument로 start,endpoint는 강의실정보 710,B610

		int startFloor;// startPoint의 층
		int endFloor; // endPoint의 층
		int index = 0;
		boolean check = false;
		Stack<Graph.Floor.Node> temp = new Stack<Graph.Floor.Node>();
		// startPoint의 floor를 찾는다.
		Stack<String> route = new Stack<String>();
		Stack<Float> dist = new Stack<Float>();
		Stack<Integer> stackFloor = new Stack<Integer>();
		// endPoint 층에 도착했을때 저장해 놓는 stack
		Stack<Graph.Floor.Node> goalTemp = new Stack<Graph.Floor.Node>();
		Stack<String> goalRoute = new Stack<String>();
		Stack<Float> goalDist = new Stack<Float>();

		if (startPoint.contains("B")) {
			startFloor = transformfloorType(startPoint.substring(0, 2));
		} else {
			startFloor = transformfloorType(startPoint.substring(0, 1));
		}
		// startPoint의 층을 찾는다.
		Floor startF = findFloor(startFloor);
		// startPoint가 저장되어이는 노드를 찾는다. 여러개일수 있으므로 배열형태로 받는다.
		Graph.Floor.Node[] startPointNode = findRoom(startPoint, startF);

		// endPoint의 floor를 찾는다.
		if (endPoint.contains("B")) {
			endFloor = transformfloorType(endPoint.substring(0, 2));
		} else {
			endFloor = transformfloorType(endPoint.substring(0, 1));
		}
		// endPoint의 층을 찾는다.
		Floor endF = findFloor(endFloor);
		// endPoint가 저장되어이는 노드를 찾는다. 여러개일수 있으므로 배열형태로 받는다.
		Graph.Floor.Node[] endPointNode = findRoom(endPoint, endF);
		// path를 만드는 시작부분

		for (int i = 0; startPointNode[i] != null; i++) {
			for (int j = 0; endPointNode[j] != null; j++) {
				temp.push(startPointNode[i]);
				route.push("");
				dist.push((float) 0);
				if (startPoint.contains("B")) {
					stackFloor.push(transformfloorType(startPoint.substring(0, 2)));
				} else {
					stackFloor.push(transformfloorType(startPoint.substring(0, 1)));
				}
				while (!temp.isEmpty()) { // 시작층과 끝층이 같아질때까지 실행한다.
					Graph.Floor.Node startTemp = temp.pop();
					String tempRoute = route.pop();
					float tempDist = dist.pop();
					startFloor = stackFloor.pop();
					startF = findFloor(startFloor);
					Graph.Floor.Node startTemp2 = startTemp;
					String tempRoute2 = tempRoute;
					float tempDist2 = tempDist;
					check = false;
					// startFloor2 = startFloor;
					// path를 만드는 작업.
					if (startFloor < endFloor) { // 올라가는 경우
						if (endFloor - startFloor > 2) {// 2층이상 차이날떄만 엘레베이터를 이용한다.
							for (int k = 1; k < MAX_E; k++) { // 엘레베이터
								if (startF.E[k]) {
									int start_e = 0;
									int end_e = 0;
									float e_dist;
									Graph.Floor.Node endTemp = startF.getNode('E', k);
									for (int l = 0; l < endTemp.floorConnect.length; l++) {
										if (endTemp.floorConnect[l] != null) {
											for (int m = 0; m < endTemp.floorConnect[l].length; m++) {
												if (endTemp.floorConnect[l][m] == startFloor) {
													start_e = m;
												}
												if (endTemp.floorConnect[l][m] == endFloor) {
													end_e = m;
													check = true;
													// 첫 시작의 층에서 엘레베이터 까지 이어버리기
													tempRoute += dijkstra(startTemp, endTemp, startFloor, dist);
													// end층에 올라가기
													tempDist += dist.pop() + ((end_e - start_e) * (float) 0.1);
													Floor t_f = findFloor(endFloor);
													startFloor = endFloor;
													startTemp = t_f.getNode('E', k);
													// end층에서 강의실까지 찾기
													int u = 0;
													int v = 0;
													for (; endPointNode[j].room[u] != null; u++) {
														if (endPointNode[j].room[u].equals(endPoint)) {
															break;
														}
													}
													for (; startPointNode[i].room[v] != null; v++) {
														if (startPointNode[i].room[v].equals(startPoint)) {
															break;
														}
													}
													tempRoute += dijkstra(startTemp, endPointNode[j], startFloor, dist);
													tempDist += dist.pop() + startPointNode[i].distance[v]
															+ endPointNode[j].distance[u];
													goalRoute.push(tempRoute);
													goalDist.push(tempDist);
												}
											}
										}
										start_e = 0;
										end_e = 0;
										startTemp = startTemp2;
										tempRoute = tempRoute2;
										tempDist = tempDist2;
										startFloor = startF.floor;
									}
								}
							}
						}
						if (!check) {
							if (startFloor > 0) {
								for (int k = 1; k < MAX_S; k++) { // 계단
									if (startF.S[k]) {
										if (k == 3 && (startFloor == 1 || startFloor == 2)) {
											// 지하 4층부터 지하 1층 지상 1층 지상 2층은 모두 올라갈떄 에스컬레이터를 이용하게한다.
										} else {
											Graph.Floor.Node endTemp = startF.getNode('S', k);
											if (endTemp.upfloor > 0 && endFloor >= startFloor + endTemp.upfloor) {
												// 층을 올라가고, startTemp에 저장 후 temp에 푸쉬
												if (endTemp.number == startTemp.number
														&& endTemp.distinction == startTemp.distinction) { // s1
													tempRoute += "" + (startFloor) + ""
															+ endTemp.upfloorNode.distinction + ""
															+ endTemp.upfloorNode.number + "/"; // ->s1올라갈때
													tempDist += endTemp.upFloorDistance;
													startTemp = endTemp.upfloorNode;
													startFloor += endTemp.upfloor;
												} else { // s1->s3 ,s1->s4 로 갈때
													tempRoute += dijkstra(startTemp, endTemp, startFloor, dist);
													tempDist += dist.pop() + endTemp.upFloorDistance;
													startTemp = endTemp.upfloorNode;
													startFloor += endTemp.upfloor;
												}

												if (startFloor != endFloor) {
													temp.push(startTemp);
													route.push(tempRoute);
													dist.push(tempDist);
													stackFloor.push(startFloor);
												} else if (startFloor == endFloor) {
													int u = 0;
													int v = 0;
													for (; endPointNode[j].room[u] != null; u++) {
														if (endPointNode[j].room[u].equals(endPoint)) {
															break;
														}
													}
													for (; startPointNode[i].room[v] != null; v++) {
														if (startPointNode[i].room[v].equals(startPoint)) {
															break;
														}
													}
													tempRoute += dijkstra(startTemp, endPointNode[j], startFloor, dist);
													tempDist += dist.pop() + startPointNode[i].distance[v]
															+ endPointNode[j].distance[u];
													goalRoute.push(tempRoute);
													goalDist.push(tempDist);

												}
												startTemp = startTemp2;
												tempRoute = tempRoute2;
												tempDist = tempDist2;
												startFloor = startF.floor;
											}
										}
									}
								}
							}
							for (int k = 1; k < MAX_C; k++) { // 에스컬레이터
								if (startF.C[k]) {
									Graph.Floor.Node endTemp = startF.getNode('C', k);
									if (endTemp.upfloor > 0 && endFloor >= startFloor + endTemp.upfloor) {
										// 층을 올라가고, startTemp에 저장 후 temp에 푸쉬
										if (endTemp.number == startTemp.number
												&& endTemp.distinction == startTemp.distinction) { // s1
											tempRoute += "" + (startFloor) + "" + endTemp.upfloorNode.distinction + ""
													+ endTemp.upfloorNode.number + "/"; // ->s1올라갈때
											startTemp = endTemp.upfloorNode;
											startFloor += endTemp.upfloor;
										} else { // s1->s3 ,s1->s4 로 갈때
											tempRoute += dijkstra(startTemp, endTemp, startFloor, dist);
											tempDist += dist.pop() + endTemp.upFloorDistance;
											startTemp = endTemp.upfloorNode;
											startFloor += endTemp.upfloor;
										}

										if (startFloor != endFloor) {
											temp.push(startTemp);
											route.push(tempRoute);
											dist.push(tempDist);
											stackFloor.push(startFloor);
										} else if (startFloor == endFloor) {
											int u = 0;
											int v = 0;
											for (; endPointNode[j].room[u] != null; u++) {
												if (endPointNode[j].room[u].equals(endPoint)) {
													break;
												}
											}
											for (; startPointNode[i].room[v] != null; v++) {
												if (startPointNode[i].room[v].equals(startPoint)) {
													break;
												}
											}
											tempRoute += dijkstra(startTemp, endPointNode[j], startFloor, dist);
											tempDist += dist.pop() + startPointNode[i].distance[v]
													+ endPointNode[j].distance[u];
											goalRoute.push(tempRoute);
											goalDist.push(tempDist);

										}
										startTemp = startTemp2;
										tempRoute = tempRoute2;
										tempDist = tempDist2;
										startFloor = startF.floor;
									}
								}
							}
						}
					} else if (startFloor > endFloor) { // 내려가는경우
						if (!(endFloor == -6 || endFloor == -5)) {
							for (int k = 1; k < MAX_S; k++) { // 계단
								if (startF.S[k]) {
									Graph.Floor.Node endTemp = startF.getNode('S', k);
									if (endTemp.downfloor > 0 && startFloor >= endTemp.downfloor + endFloor) {
										// 층을 올라가고, startTemp에 저장 후 temp에 푸쉬
										if (endTemp.number == startTemp.number
												&& endTemp.distinction == startTemp.distinction) { // s1
											tempRoute += "" + (startFloor) + "" + endTemp.downfloorNode.distinction + ""
													+ endTemp.downfloorNode.number + "/"; // ->s1올라갈때
											tempDist += endTemp.downFloorDistance;
											startTemp = endTemp.downfloorNode;
											startFloor -= endTemp.downfloor;
										} else { // s1->s3 ,s1->s4 로 갈때
											tempRoute += dijkstra(startTemp, endTemp, startFloor, dist);
											tempDist += dist.pop() + endTemp.downFloorDistance;
											startTemp = endTemp.downfloorNode;
											startFloor -= endTemp.downfloor;
										}

										if (startFloor != endFloor) {
											temp.push(startTemp);
											route.push(tempRoute);
											dist.push(tempDist);
											stackFloor.push(startFloor);
										} else if (startFloor == endFloor) {
											int u = 0;
											int v = 0;
											for (; endPointNode[j].room[u] != null; u++) {
												if (endPointNode[j].room[u].equals(endPoint)) {
													break;
												}
											}
											for (; startPointNode[i].room[v] != null; v++) {
												if (startPointNode[i].room[v].equals(startPoint)) {
													break;
												}
											}
											tempRoute += dijkstra(startTemp, endPointNode[j], startFloor, dist);
											tempDist += dist.pop() + startPointNode[i].distance[v]
													+ endPointNode[j].distance[u];
											goalRoute.push(tempRoute);
											goalDist.push(tempDist);

										}
										startTemp = startTemp2;
										tempRoute = tempRoute2;
										tempDist = tempDist2;
										startFloor = startF.floor;
									}
								}
							}
						}else { //엘레베이터를 이용해야하는 경우
							for (int k = 1; k < MAX_E; k++) { // 엘레베이터
								if (startF.E[k]) {
									int start_e = 0;
									int end_e = 0;
									float e_dist;
									Graph.Floor.Node endTemp = startF.getNode('E', k);
									for (int l = 0; l < endTemp.floorConnect.length; l++) {
										if (endTemp.floorConnect[l] != null) {
											for (int m = 0; m < endTemp.floorConnect[l].length; m++) {
												if (endTemp.floorConnect[l][m] == startFloor) {
													start_e = m;
												}
											}
											for (int m = 0; m < endTemp.floorConnect[l].length; m++) {
												if (endTemp.floorConnect[l][m] == endFloor) {
													end_e = m;
													check = true;
													// 첫 시작의 층에서 엘레베이터 까지 이어버리기
													tempRoute += dijkstra(startTemp, endTemp, startFloor, dist);
													// end층에 내려가기													tempDist += dist.pop() + ((start_e - end_e) * (float) 0.1);
													Floor t_f = findFloor(endFloor);
													startFloor = endFloor;
													startTemp = t_f.getNode('E', k);
													// end층에서 강의실까지 찾기
													int u = 0;
													int v = 0;
													for (; endPointNode[j].room[u] != null; u++) {
														if (endPointNode[j].room[u].equals(endPoint)) {
															break;
														}
													}
													for (; startPointNode[i].room[v] != null; v++) {
														if (startPointNode[i].room[v].equals(startPoint)) {
															break;
														}
													}
													tempRoute += dijkstra(startTemp, endPointNode[j], startFloor, dist);
													tempDist += dist.pop() + startPointNode[i].distance[v]
															+ endPointNode[j].distance[u];
													goalRoute.push(tempRoute);
													goalDist.push(tempDist);
												}
											}
										}
										start_e = 0;
										end_e = 0;
										startTemp = startTemp2;
										tempRoute = tempRoute2;
										tempDist = tempDist2;
										startFloor = startF.floor;
									}
								}
							}
						}
					}
				}
			}
		}
		int i = 0;
		float a;
		float min = 10000;
		String b = "";
		String minr = "";
		while (!goalRoute.isEmpty()) {
			b = goalRoute.pop();
			a = goalDist.pop();
			System.out.println(i + "번 route : " + b);
			System.out.println(i + "번 dist : " + a + "");
			if (min > a) {
				min = a;
				minr = b;
			}
			i++;
		}
		minr = startName(startPoint) + "/" + minr + endPoint + "";
		System.out.println(startName(startPoint) + "~" + endPoint + "최단거리 : " + min);
		System.out.println(startName(startPoint) + "~" + endPoint + "최단루트 : " + minr);
		return minr;
	}

	public String startName(String startPoint) {
		String tempPoint = "";
		switch (startPoint) {
		case "B599":
			tempPoint = "서라벌";
			break;
		case "B399":
			tempPoint = "제2공학관";
			break;
		case "199":
			tempPoint = "후문";
			break;
		case "198":
			tempPoint = "기숙사";
			break;
		default:
			tempPoint = startPoint;
		}
		return tempPoint;
	}

	// 층에서의 이동
	public String dijkstra(Graph.Floor.Node start, Graph.Floor.Node end, int floor, Stack<Float> distance) {
		String[] route = new String[MAX_N + 1];
		String minRoute = null;
		float[] dist = new float[MAX_N + 1];
		float mindist = 10000;
		boolean[] check = new boolean[MAX_N + 1];
		boolean[] endCheck = new boolean[MAX_N + 1];
		int startIndex;
		Stack<Graph.Floor.Node> nodeStack = new Stack<Graph.Floor.Node>();
		Stack<Float> tempDist = new Stack<Float>();

		Floor tempFloor = findFloor(floor);
		if (start.distinction == 'N') {
			nodeStack.push(start);
		} else {
			for (int i = 0; i < start.nearNode.size(); i++) {
				if (start.nearNode.get(i).distinction == 'N') {
					nodeStack.push(start.nearNode.get(i));
					tempDist.push(start.distanceNode.get(i));
				}
			}
		}
		while (!nodeStack.isEmpty()) {
			Graph.Floor.Node temp = nodeStack.pop();
			float tempD = 0;
			if (!tempDist.isEmpty()) {
				tempD = tempDist.pop();
			}
			// 초기화과정
			startIndex = temp.number;
			for (int i = 0; i < MAX_N + 1; i++) {
				route[i] = null;
				dist[i] = 10000;
				check[i] = false;
				endCheck[i] = false;
			}
			// 시작
			dist[startIndex] = 0;
			check[startIndex] = true;
			route[startIndex] = floor + "" + temp.distinction + "" + startIndex + "/";
			for (int k = 0; k < check.length; k++) {
				for (int j = 0; j < temp.nearNode.size(); j++) {
					if (temp.nearNode.get(j).distinction == 'N') { // 일단 엘레베이터, 계단, 에스컬레이터 가는것들은 무시하고 계산한다.
						if (dist[temp.nearNode.get(j).number] >= 10000) {
							dist[temp.nearNode.get(j).number] = dist[temp.number] + temp.distanceNode.get(j);
							route[temp.nearNode.get(j).number] = route[temp.number];
							route[temp.nearNode.get(j).number] += floor + "" + temp.nearNode.get(j).distinction + ""
									+ temp.nearNode.get(j).number + "/";
						} else if (dist[temp.nearNode.get(j).number] < 10000 && !check[temp.nearNode.get(j).number]) {
							if (dist[temp.nearNode.get(j).number] > dist[temp.number] + temp.distanceNode.get(j)) {
								dist[temp.nearNode.get(j).number] = dist[temp.number] + temp.distanceNode.get(j);
								route[temp.nearNode.get(j).number] = route[temp.number];
								route[temp.nearNode.get(j).number] += floor + "" + temp.nearNode.get(j).distinction + ""
										+ temp.nearNode.get(j).number + "/";
							}
						}
					}
				}
				int index = getSmallIndex(check, dist);
				check[index] = true;
				if (index == 0) {
					break;
				}
				temp = tempFloor.getNode('N', index);
			}
			// 다익스트라 알고리즘이 끝나고, 마지막에 end의 distinction이 n이 아닐떄이어주는 code이다.
			if (end.distinction != 'N') {
				for (int l = 0; l < end.nearNode.size(); l++) {
					if (end.nearNode.get(l).distinction == 'N') {
						dist[end.nearNode.get(l).number] += end.distanceNode.get(l) + tempD;
						route[end.nearNode.get(l).number] += floor + "" + end.distinction + "" + end.number + "/";
						endCheck[end.nearNode.get(l).number] = true;
					}
				}
				int shortPathIndex = 0;
				for (int m = 0; m < MAX_N + 1; m++) {
					if (endCheck[m]) { // end와 이어져 있는 곳만 확인한다.
						if (mindist > dist[m]) { // 최소값이 바뀌는경우. dist를 바꿔주고, route도 바꿔준다.
							mindist = dist[m];
							shortPathIndex = m;
							minRoute = route[shortPathIndex];
						}
					}
				}
			} else if (end.distinction == 'N') {
				if (mindist > dist[end.number]) {
					mindist = dist[end.number];
					minRoute = route[end.number];
				}
			}
		}
		// 값을 저장해준다.
		if (start.distinction != 'N') {
			minRoute = floor + "" + start.distinction + "" + start.number + "/" + minRoute;
		} else {
			minRoute = minRoute;
		}
		distance.push(mindist);
		return minRoute;
	}

	// 가장 최소 거리를 가지는 정점을 반환
	public int getSmallIndex(boolean[] check, float[] dist) {
		float min = 10000;
		int index = 0;
		for (int i = 1; i < MAX_N + 1; i++) {
			if (dist[i] < min && !check[i]) {
				min = dist[i];
				index = i;
			}
		}
		return index;
	}

	public void putElevate(int[][] elevateInfo) { // row는 엘레베이터 1호기,2호기 ~10호기, cal : 몇번째 엘베랑 이어져 있는지
		// input을 split으로 나누어서 저장한다.
		for (int i = 0; i < elevateInfo.length; i++) {
			for (int j = 0; j < elevateInfo[i].length; j++) {
				Graph.Floor.Node temp = null;
				Floor f = findFloor(elevateInfo[i][j]);
				System.out.println(i + ":" + j + "");
				if (i >= 0 && i <= 3) { // a구역
					if (f.E[1]) {
						temp = f.getNode('E', 1);
						temp.floorConnect[i] = new int[elevateInfo[i].length];
					}
				} else if (i >= 4 && i <= 7) { // b구역
					if (f.E[2]) {
						temp = f.getNode('E', 2);

						temp.floorConnect[i - 4] = new int[elevateInfo[i].length];
					}
				} else { // c구역
					if (f.E[3]) {
						temp = f.getNode('E', 3);
						temp.floorConnect[i - 8] = new int[elevateInfo[i].length];
					}
				}
				for (int k = 0; k < elevateInfo[i].length; k++) {
					if (i >= 0 && i <= 3) { // a구역
						if (f.E[1]) {
							temp.floorConnect[i][k] = elevateInfo[i][k];
						}
					} else if (i >= 4 && i <= 7) { // b구역
						if (f.E[2]) {
							temp.floorConnect[i - 4][k] = elevateInfo[i][k];
						}
					} else { // c구역
						if (f.E[3]) {
							temp.floorConnect[i - 8][k] = elevateInfo[i][k];
						}
					}

				}

			}
		}
	}

	public Graph.Floor.Node[] findRoom(String room, Floor roomOfFloor) {
		// dfs를 할때에 node가 들렸는지 확인해주는 변수
		boolean[] N_DFS = new boolean[MAX_N]; // 각 원소가 true라면 이미 지나온 것이고, false은 아직 탐색하지 않은것
		boolean[] S_DFS = new boolean[MAX_S]; // 각 원소가 true라면 이미 지나온 것이고, false은 아직 탐색하지 않은것
		boolean[] E_DFS = new boolean[MAX_E]; // 각 원소가 true라면 이미 지나온 것이고, false은 아직 탐색하지 않은것
		boolean[] C_DFS = new boolean[MAX_C]; // 각 원소가 true라면 이미 지나온 것이고, false은 아직 탐색하지 않은것
		Graph.Floor.Node root = roomOfFloor.nodeHead;
		Graph.Floor.Node[] returnNode = new Graph.Floor.Node[3];
		int index = 0;
		Stack<Graph.Floor.Node> stack = new Stack<Graph.Floor.Node>();
		N_DFS[root.number] = true;
		if (root.room != null) {
			for (int j = 0; j < root.room.length; j++) {
				if (root.room[j].equals(room)) { // argument로 들어온 room이랑 같은 것이 있나 확인한다.
					// 있다면 node배열에 저장한다.
					returnNode[index] = root;
					index++;
				}
			}
		}
		stack.push(root);
		while (!stack.isEmpty()) {
			Graph.Floor.Node r = stack.pop();
			for (int i = 0; i < r.nearNode.size(); i++) {
				Graph.Floor.Node n = r.nearNode.get(i);
				if (n.room != null && N_DFS[n.number] == false) {
					for (int j = 0; j < n.room.length; j++) {
						if (n.room[j].equals(room)) { // argument로 들어온 room이랑 같은 것이 있나 확인한다.
							// 있다면 node배열에 저장한다.
							returnNode[index] = n;
							index++;
						}
					}
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
		return returnNode;
	}
}