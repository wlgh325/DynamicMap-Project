import java.io.IOException;
import java.util.ArrayList;

public class Main {

	public static void main(String[] args) throws IOException {
		final int ROW_NUM = 21;
		final int ELEVATOR_NUM = 10;
		final int DAY_NUM = 5;
		
		ReadExcelFile readExcelFile = new ReadExcelFile();
		readExcelFile.readExcel();
		float x = 930.0f;
		float y = 0.209f;
		float z = 113.0f;

		ArrayList<Integer>[][][] runningElevatorInfo;
		
		Optimize optimizer = new Optimize(readExcelFile.getCourseInfo());
		//optimizer.Fileprint();
		optimizer.setXYZ(x,y,z);
		optimizer.OptimizeElevator();
		runningElevatorInfo = optimizer.getRunningElevatorInfo();
		
		for(int i=0; i< DAY_NUM; i++) {
			System.out.println("Day : " + i);
			for(int j=0; j< ROW_NUM; j++) {
				System.out.println("row: " + j);
				for(int k =0; k< ELEVATOR_NUM; k++) {
					System.out.print("ELEVATOR num : " + k + " ");
					for(int index = 0; index < runningElevatorInfo[i][j][k].size(); index++)
						System.out.print(runningElevatorInfo[i][j][k].get(index) + " ");
					System.out.println();
				}
			}
			System.out.println();
		}
		
		System.out.println();
		System.out.println();
		optimizer.MagicFloorPrint();
		
		/* 
		int max = 0;
		float[] xyz = new float[3];
		int avgPeoplenum;
		max = optimizer.OptimizeElevator();
		
		for(float temp_x=600.0f; temp_x<9500f; temp_x+=1.0f) {
			for(float temp_y=0.2f; temp_y<0.3f; temp_y+=0.001f) {
				for(float temp_z=100.0f; temp_z<200.0f; temp_z+=1.0f){
					optimizer = new Optimize(readExcelFile.getCourseInfo());
					optimizer.setXYZ(temp_x, temp_y, temp_z);
					avgPeoplenum = optimizer.OptimizeElevator();
					
					if(max < avgPeoplenum){
						max = avgPeoplenum;
						xyz[0] = temp_x;
						xyz[1] = temp_y;
						xyz[2] = temp_z;
						optimizer.MagicFloorPrint();
						System.out.println("x: " + xyz[0] + ", y: " + xyz[1]+ ", z: " +  xyz[2] + ", "+ max + "명");
					}
				}	
			}
		}
		*/
		System.out.println("finish!!!");
			
		
	}

}
