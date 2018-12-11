
public class Main {

	public static void main(String[] args) {
		Graph graph = new Graph();
		String graphPath = "C:\\Users\\YungJae\\git\\DynamicMap-Project\\Graph\\310Graph.txt";
		String nodeDistancePath = "C:\\Users\\YungJae\\git\\DynamicMap-Project\\Graph\\NodeDistance.txt";
		String roomDistancePath = "C:\\Users\\YungJae\\git\\DynamicMap-Project\\Graph\\roomDistance.txt";
		String floorDistancePath = "C:\\Users\\YungJae\\git\\DynamicMap-Project\\Graph\\FloorDistance2.txt";
		String startPoint = "B310";
		String endPoint = "728";
		
		graph.readFile(graphPath);
		graph.readNodeDistanceFile(nodeDistancePath);
		graph.readRoomDistanceFile(roomDistancePath);
		graph.readFloorDistance(floorDistancePath);
		graph.sortPath(startPoint, endPoint);
	}

}
