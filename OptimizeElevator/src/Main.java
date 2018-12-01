import java.io.IOException;

public class Main {

	public static void main(String[] args) throws IOException {
		ReadExcelFile readExcelFile = new ReadExcelFile();
		readExcelFile.readExcel();
		
		Optimize optimize = new Optimize(readExcelFile.getCourseInfo());
		optimize.print();
	}

}