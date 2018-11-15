import java.io.FileInputStream;
import java.io.IOException;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class ReadExcelfile {
	private CourseInfo courseinfo;
	private String path;
	
	// Constructor
	ReadExcelfile(){
		this.courseinfo = new CourseInfo();
		this.path = "C:\\Users\\Guest1\\eclipse-workspace\\src\\application\\CourseList.xls";
	}
	
	public void readExcel(String str) throws IOException{
		FileInputStream fis=new FileInputStream(path);
		HSSFWorkbook workbook=new HSSFWorkbook(fis);
		
		int rowindex=0;
		int columnindex=0;
		int num =0;
		
		for(int i=0; i<workbook.getNumberOfSheets(); i++) {
			HSSFSheet sheet = workbook.getSheetAt(i);	//sheet ����
			
			//���� ��
			int rows=sheet.getPhysicalNumberOfRows();
			for(rowindex=1;rowindex<rows;rowindex++){
			    //���� �д´�
			    HSSFRow row=sheet.getRow(rowindex);
			    if(row !=null){
			        //���� ��
			        int cells=row.getPhysicalNumberOfCells();
			        for(columnindex=0;columnindex<=cells;columnindex++){
			            //������ �д´�
			            HSSFCell cell= sheet.getRow(rowindex).getCell((short)columnindex);
			            String value="";
			            //���� ���ϰ�츦 ���� ��üũ
			            if(cell==null){
			                continue;
			            }else{
			                //Ÿ�Ժ��� ���� �б�
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

			            //Integer.parseInt(value)
			            //set courseInfo
			            
			            //value�� str�� ������ ������ �����ϰ� �����ش�
			            if(value.equals(str)) {
				            switch (columnindex % 7) {
				            case 4:
				            	courseinfo.setCourse_name(value, num);
				            	break;
				            case 5:
				            	courseinfo.setLocation(value, num);
				            	break;
				            case 6:
				            	System.out.println(value.substring(0));
				            	courseinfo.setday(value.substring(0), num);
				            	
				            	String temp = value.substring(1, value.length()-1);
				            	String[] temp2 = value.split(",");
				            	courseinfo.setStart_time(Integer.parseInt(temp2[0]), num);
				            	break;
				            }	//end of switch
			            }
			        }	//end of for loop
			        
			        }	//end of if
		        num++;
			}	//end of for loop
		}
				
		print_Course(courseinfo);
	}
	
	//print
	public void print_Course(CourseInfo courseinfo) {	
		for(int i=0; i<courseinfo.COURSE_NUM; i++) {
		   	System.out.println("���Ǹ� : "+ courseinfo.getCourse_name(i));
		   	System.out.println("��� : "+ courseinfo.getLocation(i));
		   	System.out.println("���� : "+ courseinfo.getday(i));
		   	System.out.println("���۽ð� : "+ courseinfo.getStart_time(i));
		   	System.out.println();
		}
	}
	
	public CourseInfo getCourseInfo() {
		return this.courseinfo;
	}
}
