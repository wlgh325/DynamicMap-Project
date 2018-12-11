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

		public class Node {
			// �� node�� ����������, ��������������, ������� �������ִ� ������
			private char distinction; // �������̸� n, ���������͸� e, ����̸� s
			private int number; // ���° ������,����������,�������. 7n4 �� number�� 4�� ����.
			// �������� ������,���,���������� ������ ����, ��ũ�� ����Ʈ ���·� ����
			private ArrayList<Float> distanceNode = new ArrayList<>(); // ����� node�̸��� �Ÿ�
			private ArrayList<Node> nearNode = new ArrayList<>(); // ���� node�� ����
			// �������� ���ǽ� ������ ����,�迭�� ���·� ����.
			private String[] room; // �������� ������ room ����
			private float[] distance; // �������� ������ room���� �Ÿ�
			// ���������� �̵��� �̾��ֱ����� ������
			private int[][] floorConnect;
			// �����÷�����,����� ���� ����
			// ���� ����
			int upfloor; // 1�����̸� 1, 2�����̸� 2
			float upFloorDistance;
			private Node upfloorNode;

			// �Ʒ��� ����
			int downfloor; // 1�����̸� 1, 2�����̸� 2
			float downFloorDistance;
			private Node downfloorNode;

			// ������
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
				Node temp = new Node(nearNode[i].charAt(0),
						Integer.parseInt(nearNode[i].substring(1, nearNode[i].length()))); // temp���
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
						thisNode.distance[i] = (float) 0;
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
							Node temp = getNode(nearNode[i].charAt(0), Integer.parseInt(nearNode[i].substring(1))); // ����
																													// node��
																													// temp��
																													// ����
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
					if (existNode(nearNode[i].charAt(0),
							Integer.parseInt(nearNode[i].substring(1, nearNode[i].length())))) // graph�� ���������
					// �����Ѵٸ�,
					// �����ϵ����Ѵ�.
					{
						if (isAlreadyConnected(thisNode, nearNode[i].charAt(0),
								Integer.parseInt(nearNode[i].substring(1))) == false)// �̹� �̾����ִ� ���� �ѱ��, �������ִٸ� �̾��ش�.
						{
							// this -> ���� node�� ����
							Node temp = getNode(nearNode[i].charAt(0), Integer.parseInt(nearNode[i].substring(1))); // ����
																													// node��
																													// temp��
																													// ����
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
			} else if (dis == 'C') { // �������������� ��
				if (C[num] == true) { // �̹� node�� �ִ� ���
					return true;
				}
				return false;
			}
			System.out.println(dis + "" + num + "�� �ùٸ� node �������� Ȯ�����ּ���");
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
			String line = ""; // ���پ� �о�� ���� �����ϱ� ���� ����
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

		for (int i = 0; i < temp.length; i++) {
			System.out.print(temp[i] + ",");
		}

		// nearNode ���� ����
		if (temp[1].contains(",")) { // nearNode�� �������� ���
			String[] tempNode = temp[1].split(","); // ���� nearNode[0] -> N7 or S1 or E1 �̷������� ������ �Ǿ��ִ�.
			for (int i = 0; i < tempNode.length; i++)
				nearNode[i] = tempNode[i];
			for (int i = tempNode.length; i < NEAR_NODE_NUM; i++)
				nearNode[i] = null;
		} else // nearNode�� �ϳ� �ۿ� ������� ó��
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
		} else {
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
	// ���� ���ǽ��� �Ÿ��� �����ϱ� ���� ������ ����, line�� �д´�.
	public void readRoomDistanceFile(String roomDistanceFilename) {
		try {
			File file = new File(roomDistanceFilename); // graph�� �׸��� ���� ������ txt������ ����.
			FileReader fileReader = new FileReader(file);// ������ �б����ؼ� filereader�� �����Ѵ�.
			BufferedReader bufReader = new BufferedReader(fileReader); // ���پ� �о���� ���ؼ� ���۸� �����Ѵ�.
			String line = ""; // ���پ� �о�� ���� �����ϱ� ���� ����
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

	// ���� line�� ������ �Ÿ��� �����ش�.
	public void addRoomDistance(String line) {
		String[] temp = line.split("/"); // ex) 4N1/410/2.4 �̷� ���� /�� ������.
		String[] floorAndNumber = temp[0].split("N"); // �Ǿ� 4N1 ����� ���� N�� �������� ������.
		int floor = transformfloorType(floorAndNumber[0]); // String �� B6~9���� Integer������ ���ڷ� ��ȯ
		int num = Integer.parseInt(floorAndNumber[1]);
		// �о�� line�� ������ǳ������ ã�´�.
		Floor tempfloor = findFloor(floor); // floor�� ã�� tempfloor�� �����Ų��.
		// line���� �о�� ��带 ã�´�.
		Graph.Floor.Node tempNode = tempfloor.getNode('N', num);
		// node�� �ش� ���ǽ��������� ����� �ε��� �ִ��� Ȯ���Ѵ�.
		for (int i = 0; tempNode.room[i] != null; i++) {
			if (tempNode.room[i].equals(temp[1])) {
				tempNode.distance[i] = Float.parseFloat(temp[2]);
				break;
			}
		}
	}

	public void readNodeDistanceFile(String nodeDistanceFilename) {
		try {
			File file = new File(nodeDistanceFilename); // node�鰣�� �Ÿ��� ������ txt������ ����.
			FileReader fileReader = new FileReader(file);// ������ �б����ؼ� filereader�� �����Ѵ�.
			BufferedReader bufReader = new BufferedReader(fileReader); // ���پ� �о���� ���ؼ� ���۸� �����Ѵ�.
			String line = ""; // ���پ� �о�� ���� �����ϱ� ���� ����
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
		String[] floorAndNumber = temp[0].split("N"); // �Ǿ� 4N1 ����� ���� N�� �������� ������.
		int floor1 = transformfloorType(floorAndNumber[0]); // String �� B6~9���� Integer������ ���ڷ� ��ȯ
		int num1 = Integer.parseInt(floorAndNumber[1]);

		char disc = ' ';
		int num2 = 0;

		float distance = Float.parseFloat(temp[2]);

		// �о�� line�� ������ǳ������ ã�´�.
		Floor tempfloor = findFloor(floor1); // floor�� ã�� tempfloor�� �����Ų��.
		// line���� �о�� ��带 ã�´�.
		Graph.Floor.Node tempNode = tempfloor.getNode('N', num1);
		// �ش� ��� node�� � ����ϰ� �̾��� �ִ��� Ȯ���ϰ� ������ num2�� disc�� �����س��´�.
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
		// tempNode �� ���� ���ִ� ������ Ȯ���ϸ鼭 num2�� disc�� ���� ��带 �߰��ϸ�, �Ÿ��� �����Ų��.
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
			File file = new File(floorDistanceFilename); // node�鰣�� �Ÿ��� ������ txt������ ����.
			FileReader fileReader = new FileReader(file);// ������ �б����ؼ� filereader�� �����Ѵ�.
			BufferedReader bufReader = new BufferedReader(fileReader); // ���پ� �о���� ���ؼ� ���۸� �����Ѵ�.
			String line = ""; // ���پ� �о�� ���� �����ϱ� ���� ����
			while ((line = bufReader.readLine()) != null) { // ���پ� �о���鼭 line�� �����ϰ�, line�� ���� graph�� �����Ѵ�.
				System.out.println(line);
				addFloorDistance(line);
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

	public void addFloorDistance(String line) {
		String[] temp = line.split("/"); // ex) 4N1/4N2/2.4F �̷� ���� /�� ������.
		// temp[0]�� ����ִ� ���
		int floor1 = 0; // String �� B6~9���� Integer������ ���ڷ� ��ȯ
		char disc1 = ' ';
		int num1 = 0;
		// temp[1] �� ����ִ� ���
		int floor2 = 0;
		char disc2 = ' ';
		int num2 = 0;

		float distance = Float.parseFloat(temp[2]);
		// temp[0]�� ����� ������ num1 disc1�� �����Ѵ�.
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
		// temp[0]�� ������ǳ������ ã�´�.
		Floor tempfloor1 = findFloor(floor1); // floor�� ã�� tempfloor�� �����Ų��.
		// temp[0]�� ������ � ��带 ã�´�.
		Graph.Floor.Node tempNode1 = tempfloor1.getNode(disc1, num1);

		// �ش� node�� � ����ϰ� �̾��� �ִ��� Ȯ���ϰ� ������ num2�� disc2�� �����س��´�.
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
		// temp[1]�� ������ǳ������ ã�´�.
		Floor tempfloor2 = findFloor(floor2); // floor�� ã�� tempfloor�� �����Ų��.
		// temp[1]�� ������ � ��带 ã�´�.
		Graph.Floor.Node tempNode2 = tempfloor2.getNode(disc2, num2);
		// tempNode1 �� tempNode2�� ���� �������Ѵ�.
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
				System.out.println("�����Դϴ�.");
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

	public String sortPath(String startPoint, String endPoint) { // argument�� start,endpoint�� ���ǽ����� 710,B610

		int startFloor;// startPoint�� ��
		int endFloor; // endPoint�� ��
		int index = 0;
		boolean check = false;
		Stack<Graph.Floor.Node> temp = new Stack<Graph.Floor.Node>();
		// startPoint�� floor�� ã�´�.
		Stack<String> route = new Stack<String>();
		Stack<Float> dist = new Stack<Float>();
		Stack<Integer> stackFloor = new Stack<Integer>();
		// endPoint ���� ���������� ������ ���� stack
		Stack<Graph.Floor.Node> goalTemp = new Stack<Graph.Floor.Node>();
		Stack<String> goalRoute = new Stack<String>();
		Stack<Float> goalDist = new Stack<Float>();

		if (startPoint.contains("B")) {
			startFloor = transformfloorType(startPoint.substring(0, 2));
		} else {
			startFloor = transformfloorType(startPoint.substring(0, 1));
		}
		// startPoint�� ���� ã�´�.
		Floor startF = findFloor(startFloor);
		// startPoint�� ����Ǿ��̴� ��带 ã�´�. �������ϼ� �����Ƿ� �迭���·� �޴´�.
		Graph.Floor.Node[] startPointNode = findRoom(startPoint, startF);

		// endPoint�� floor�� ã�´�.
		if (endPoint.contains("B")) {
			endFloor = transformfloorType(endPoint.substring(0, 2));
		} else {
			endFloor = transformfloorType(endPoint.substring(0, 1));
		}
		// endPoint�� ���� ã�´�.
		Floor endF = findFloor(endFloor);
		// endPoint�� ����Ǿ��̴� ��带 ã�´�. �������ϼ� �����Ƿ� �迭���·� �޴´�.
		Graph.Floor.Node[] endPointNode = findRoom(endPoint, endF);
		// path�� ����� ���ۺκ�

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
				while (!temp.isEmpty()) { // �������� ������ ������������ �����Ѵ�.
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
					// path�� ����� �۾�.
					if (startFloor < endFloor) { // �ö󰡴� ���
						if (endFloor - startFloor > 2) {// 2���̻� ���̳����� ���������͸� �̿��Ѵ�.
							for (int k = 1; k < MAX_E; k++) { // ����������
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
													// ù ������ ������ ���������� ���� �̾������
													tempRoute += dijkstra(startTemp, endTemp, startFloor, dist);
													// end���� �ö󰡱�
													tempDist += dist.pop() + ((end_e - start_e) * (float) 0.1);
													Floor t_f = findFloor(endFloor);
													startFloor = endFloor;
													startTemp = t_f.getNode('E', k);
													// end������ ���ǽǱ��� ã��
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
								for (int k = 1; k < MAX_S; k++) { // ���
									if (startF.S[k]) {
										if (k == 3 && (startFloor == 1 || startFloor == 2)) {
											// ���� 4������ ���� 1�� ���� 1�� ���� 2���� ��� �ö󰥋� �����÷����͸� �̿��ϰ��Ѵ�.
										} else {
											Graph.Floor.Node endTemp = startF.getNode('S', k);
											if (endTemp.upfloor > 0 && endFloor >= startFloor + endTemp.upfloor) {
												// ���� �ö󰡰�, startTemp�� ���� �� temp�� Ǫ��
												if (endTemp.number == startTemp.number
														&& endTemp.distinction == startTemp.distinction) { // s1
													tempRoute += "" + (startFloor) + ""
															+ endTemp.upfloorNode.distinction + ""
															+ endTemp.upfloorNode.number + "/"; // ->s1�ö󰥶�
													tempDist += endTemp.upFloorDistance;
													startTemp = endTemp.upfloorNode;
													startFloor += endTemp.upfloor;
												} else { // s1->s3 ,s1->s4 �� ����
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
							for (int k = 1; k < MAX_C; k++) { // �����÷�����
								if (startF.C[k]) {
									Graph.Floor.Node endTemp = startF.getNode('C', k);
									if (endTemp.upfloor > 0 && endFloor >= startFloor + endTemp.upfloor) {
										// ���� �ö󰡰�, startTemp�� ���� �� temp�� Ǫ��
										if (endTemp.number == startTemp.number
												&& endTemp.distinction == startTemp.distinction) { // s1
											tempRoute += "" + (startFloor) + "" + endTemp.upfloorNode.distinction + ""
													+ endTemp.upfloorNode.number + "/"; // ->s1�ö󰥶�
											startTemp = endTemp.upfloorNode;
											startFloor += endTemp.upfloor;
										} else { // s1->s3 ,s1->s4 �� ����
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
					} else if (startFloor > endFloor) { // �������°��
						if (!(endFloor == -6 || endFloor == -5)) {
							for (int k = 1; k < MAX_S; k++) { // ���
								if (startF.S[k]) {
									Graph.Floor.Node endTemp = startF.getNode('S', k);
									if (endTemp.downfloor > 0 && startFloor >= endTemp.downfloor + endFloor) {
										// ���� �ö󰡰�, startTemp�� ���� �� temp�� Ǫ��
										if (endTemp.number == startTemp.number
												&& endTemp.distinction == startTemp.distinction) { // s1
											tempRoute += "" + (startFloor) + "" + endTemp.downfloorNode.distinction + ""
													+ endTemp.downfloorNode.number + "/"; // ->s1�ö󰥶�
											tempDist += endTemp.downFloorDistance;
											startTemp = endTemp.downfloorNode;
											startFloor -= endTemp.downfloor;
										} else { // s1->s3 ,s1->s4 �� ����
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
						}else { //���������͸� �̿��ؾ��ϴ� ���
							for (int k = 1; k < MAX_E; k++) { // ����������
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
													// ù ������ ������ ���������� ���� �̾������
													tempRoute += dijkstra(startTemp, endTemp, startFloor, dist);
													// end���� ��������													tempDist += dist.pop() + ((start_e - end_e) * (float) 0.1);
													Floor t_f = findFloor(endFloor);
													startFloor = endFloor;
													startTemp = t_f.getNode('E', k);
													// end������ ���ǽǱ��� ã��
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
			System.out.println(i + "�� route : " + b);
			System.out.println(i + "�� dist : " + a + "");
			if (min > a) {
				min = a;
				minr = b;
			}
			i++;
		}
		minr = startName(startPoint) + "/" + minr + endPoint + "";
		System.out.println(startName(startPoint) + "~" + endPoint + "�ִܰŸ� : " + min);
		System.out.println(startName(startPoint) + "~" + endPoint + "�ִܷ�Ʈ : " + minr);
		return minr;
	}

	public String startName(String startPoint) {
		String tempPoint = "";
		switch (startPoint) {
		case "B599":
			tempPoint = "�����";
			break;
		case "B399":
			tempPoint = "��2���а�";
			break;
		case "199":
			tempPoint = "�Ĺ�";
			break;
		case "198":
			tempPoint = "�����";
			break;
		default:
			tempPoint = startPoint;
		}
		return tempPoint;
	}

	// �������� �̵�
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
			// �ʱ�ȭ����
			startIndex = temp.number;
			for (int i = 0; i < MAX_N + 1; i++) {
				route[i] = null;
				dist[i] = 10000;
				check[i] = false;
				endCheck[i] = false;
			}
			// ����
			dist[startIndex] = 0;
			check[startIndex] = true;
			route[startIndex] = floor + "" + temp.distinction + "" + startIndex + "/";
			for (int k = 0; k < check.length; k++) {
				for (int j = 0; j < temp.nearNode.size(); j++) {
					if (temp.nearNode.get(j).distinction == 'N') { // �ϴ� ����������, ���, �����÷����� ���°͵��� �����ϰ� ����Ѵ�.
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
			// ���ͽ�Ʈ�� �˰����� ������, �������� end�� distinction�� n�� �ƴҋ��̾��ִ� code�̴�.
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
					if (endCheck[m]) { // end�� �̾��� �ִ� ���� Ȯ���Ѵ�.
						if (mindist > dist[m]) { // �ּҰ��� �ٲ�°��. dist�� �ٲ��ְ�, route�� �ٲ��ش�.
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
		// ���� �������ش�.
		if (start.distinction != 'N') {
			minRoute = floor + "" + start.distinction + "" + start.number + "/" + minRoute;
		} else {
			minRoute = minRoute;
		}
		distance.push(mindist);
		return minRoute;
	}

	// ���� �ּ� �Ÿ��� ������ ������ ��ȯ
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

	public void putElevate(int[][] elevateInfo) { // row�� ���������� 1ȣ��,2ȣ�� ~10ȣ��, cal : ���° ������ �̾��� �ִ���
		// input�� split���� ����� �����Ѵ�.
		for (int i = 0; i < elevateInfo.length; i++) {
			for (int j = 0; j < elevateInfo[i].length; j++) {
				Graph.Floor.Node temp = null;
				Floor f = findFloor(elevateInfo[i][j]);
				System.out.println(i + ":" + j + "");
				if (i >= 0 && i <= 3) { // a����
					if (f.E[1]) {
						temp = f.getNode('E', 1);
						temp.floorConnect[i] = new int[elevateInfo[i].length];
					}
				} else if (i >= 4 && i <= 7) { // b����
					if (f.E[2]) {
						temp = f.getNode('E', 2);

						temp.floorConnect[i - 4] = new int[elevateInfo[i].length];
					}
				} else { // c����
					if (f.E[3]) {
						temp = f.getNode('E', 3);
						temp.floorConnect[i - 8] = new int[elevateInfo[i].length];
					}
				}
				for (int k = 0; k < elevateInfo[i].length; k++) {
					if (i >= 0 && i <= 3) { // a����
						if (f.E[1]) {
							temp.floorConnect[i][k] = elevateInfo[i][k];
						}
					} else if (i >= 4 && i <= 7) { // b����
						if (f.E[2]) {
							temp.floorConnect[i - 4][k] = elevateInfo[i][k];
						}
					} else { // c����
						if (f.E[3]) {
							temp.floorConnect[i - 8][k] = elevateInfo[i][k];
						}
					}

				}

			}
		}
	}

	public Graph.Floor.Node[] findRoom(String room, Floor roomOfFloor) {
		// dfs�� �Ҷ��� node�� ��ȴ��� Ȯ�����ִ� ����
		boolean[] N_DFS = new boolean[MAX_N]; // �� ���Ұ� true��� �̹� ������ ���̰�, false�� ���� Ž������ ������
		boolean[] S_DFS = new boolean[MAX_S]; // �� ���Ұ� true��� �̹� ������ ���̰�, false�� ���� Ž������ ������
		boolean[] E_DFS = new boolean[MAX_E]; // �� ���Ұ� true��� �̹� ������ ���̰�, false�� ���� Ž������ ������
		boolean[] C_DFS = new boolean[MAX_C]; // �� ���Ұ� true��� �̹� ������ ���̰�, false�� ���� Ž������ ������
		Graph.Floor.Node root = roomOfFloor.nodeHead;
		Graph.Floor.Node[] returnNode = new Graph.Floor.Node[3];
		int index = 0;
		Stack<Graph.Floor.Node> stack = new Stack<Graph.Floor.Node>();
		N_DFS[root.number] = true;
		if (root.room != null) {
			for (int j = 0; j < root.room.length; j++) {
				if (root.room[j].equals(room)) { // argument�� ���� room�̶� ���� ���� �ֳ� Ȯ���Ѵ�.
					// �ִٸ� node�迭�� �����Ѵ�.
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
						if (n.room[j].equals(room)) { // argument�� ���� room�̶� ���� ���� �ֳ� Ȯ���Ѵ�.
							// �ִٸ� node�迭�� �����Ѵ�.
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