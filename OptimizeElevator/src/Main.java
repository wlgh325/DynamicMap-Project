import java.io.IOException;

public class Main {

	public static void main(String[] args) throws IOException {
		ReadExcelFile readExcelFile = new ReadExcelFile();
		readExcelFile.readExcel();
		float x = 700.0f;
		float y = 0.2f;
		float z = 200.0f;
		int max = 0;
		float[] xyz = new float[3];
		int avgPeoplenum;
		
		Optimize optimizer = new Optimize(readExcelFile.getCourseInfo());
		optimizer.setXYZ(x,y,z);
		max = optimizer.OptimizeElevator();

		for(float temp_x=650.0f; temp_x<800f; temp_x+=10.0f)
			for(float temp_y=0.0f; temp_y<0.3f; temp_y+=0.01f)
				for(float temp_z=0.0f; temp_z<400.0f; temp_z+=5.0f){
					optimizer = new Optimize(readExcelFile.getCourseInfo());
					optimizer.setXYZ(temp_x, temp_y, temp_z);
					avgPeoplenum = optimizer.OptimizeElevator();
					
					if(max < avgPeoplenum){
						max = avgPeoplenum;
						xyz[0] = temp_x;
						xyz[1] = temp_y;
						xyz[2] = temp_z;
						System.out.println("x: " + xyz[0] + ", y: " + xyz[1]+ ", z: " +  xyz[2] + ", "+ max + "명");
					}

				}
		
	}

}
