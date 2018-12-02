import java.io.IOException;

public class Main {

	public static void main(String[] args) throws IOException {
		ReadExcelFile readExcelFile = new ReadExcelFile();
		readExcelFile.readExcel();

		Optimize optimizer = new Optimize(readExcelFile.getCourseInfo());
		//optimize.Fileprint();
		optimizer.CalMagicFloor();
		//optimizer.MagicFloorPrint();
		optimizer.Elevator_RunningTime();
		//optimizer.printRunningTime();
		
	}

}
