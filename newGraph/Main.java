import java.io.IOException;
import java.util.ArrayList;

public class Main {
	public static void main(String[] args) {
		Graph graph = new Graph();
		ReadCoursenum readCoursenum = new ReadCoursenum();
		int rm =0;
		try {
			readCoursenum.readExcel();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ArrayList<Integer>[][][] Elevator; //요일 ,시간 엘레베이터 호기
		String[] elevator_one = new String[10]; //10호기까지 있으므로
		int[][] elevator_two = new int[10][];
		Optimize optimizer = new Optimize(readCoursenum.getCoursenum());
		optimizer.setXYZ(930,21,300);
		optimizer.OptimizeElevator();
		Elevator = optimizer.getRunningElevatorInfo();
		
		String graphPath = "C:\\Users\\YungJae\\git\\DynamicMap-Project\\Graph\\310Graph.txt";
		String nodeDistancePath = "C:\\Users\\YungJae\\git\\DynamicMap-Project\\Graph\\NodeDistance.txt";
		String roomDistancePath = "C:\\Users\\YungJae\\git\\DynamicMap-Project\\Graph\\roomDistance.txt";
		String floorDistancePath = "C:\\Users\\YungJae\\git\\DynamicMap-Project\\Graph\\FloorDistance2.txt";
		String startPoint = "316";
		String endPoint = "B599";
		

		for(int i= 0; i< Elevator[1][9].length; i++) {
			for(int k =0; k<Elevator[1][9][i].size();k++ )
			{
				if(Elevator[1][9][i].get(k)>=10) {
					rm++;
				}
			}
			elevator_two[i] = new int[Elevator[1][9][i].size()-rm];
			for(int j = 0; j<Elevator[1][9][i].size();j++)
			{
				if(Elevator[1][9][i].get(j)<10)
				{
					elevator_two[i][j] = Elevator[1][9][i].get(j);
				}
			}
			rm =0;
		}
		graph.readFile(graphPath);
		graph.readNodeDistanceFile(nodeDistancePath);
		graph.readRoomDistanceFile(roomDistancePath);
		graph.readFloorDistance(floorDistancePath);
		graph.putElevate(elevator_two);
		graph.sortPath(startPoint, endPoint);
		graph.sortPath(endPoint, startPoint);
		
		
//		try {
//			Frame frame = new Frame();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}

}
