import java.io.FileInputStream;
import java.io.IOException;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class ReadExcelFile {
	private CourseInfo courseinfo;
	private String path;

	// Constructor
	ReadExcelFile(){
		this.courseinfo = new CourseInfo();
		this.path = "C:\\Users\\Guest1\\eclipse-workspace\\OptimizeElevator\\Coursenum.xls";
	}

	public void readExcel() throws IOException{
		FileInputStream fis=new FileInputStream(path);
		HSSFWorkbook workbook=new HSSFWorkbook(fis);

		int rowindex=0;
		int columnindex=0;
		int num =0;

		for(int i=0; i<workbook.getNumberOfSheets(); i++) {
			HSSFSheet sheet = workbook.getSheetAt(i);	//sheet 생성

			//행의 수
			int rows=sheet.getPhysicalNumberOfRows();
			for(rowindex=1; rowindex<rows; rowindex++){
			    //행을 읽는다
			    HSSFRow row=sheet.getRow(rowindex);
			    if(row !=null){
			        //셀의 수
			        int cells=row.getPhysicalNumberOfCells();
			        for(columnindex=1; columnindex<cells; columnindex++){
			            //셀값을 읽는다
			            HSSFCell cell= sheet.getRow(rowindex).getCell((short)columnindex);
			            String value="";
			            //셀이 빈값일경우를 위한 널체크
			            if(cell==null){
			                continue;
			            }else{
			                //타입별로 내용 읽기
			                switch (cell.getCellType()){
			                case HSSFCell.CELL_TYPE_FORMULA:
			                    value=cell.getCellFormula();
			                    break;
			                case HSSFCell.CELL_TYPE_NUMERIC:
			                    value=cell.getNumericCellValue()+"";
			                    break;
			                case HSSFCell.CELL_TYPE_STRING:
			                    value=cell.getStringCellValue()+"";
			                    break;
			                case HSSFCell.CELL_TYPE_BLANK:
			                    value=cell.getBooleanCellValue()+"";
			                    break;
			                case HSSFCell.CELL_TYPE_ERROR:
			                    value=cell.getErrorCellValue()+"";
			                    break;
			                }
			            }	//end of else
			            if(columnindex == 8)
			            	courseinfo.setFloating_population(i, rowindex-1, Float.parseFloat(value));
			            else {
			            	if(value.equals("false"))
			            		courseinfo.setPeopleNum(i, rowindex-1, columnindex-1, 0.0f);
			            	else
			            		courseinfo.setPeopleNum(i, rowindex-1, columnindex-1, Float.parseFloat(value));
			            }

			        }	//end of for loop(column)
			    }	//end of if
			}	//end of for loop(row)
		}	// end of for loop(workbook)
	}

	public CourseInfo getCourseInfo() {
		return this.courseinfo;
	}

}
