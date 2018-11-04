package application;

import java.io.FileInputStream;
import java.io.IOException;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class Main {
	private static float lat[];
	private static float lng[];
	private final static int NUM = 24;

	public static void main(String[] args) throws IOException {
		CourseInfo courseinfo = new CourseInfo();
		
		FileInputStream fis=new FileInputStream("C:\\Users\\Guest1\\eclipse-workspace\\DSProject\\src\\application\\CourseList.xls");
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

			            //set courseInfo
			            switch (columnindex % 5) {
			            case 0:
			            	courseinfo.setCourseName(value, num);
			            	break;
			            case 1:
			            	courseinfo.setGrade(Integer.parseInt(value), num);
			            	break;
			            case 2:
			            	String[] temp = value.split(" ");
			            	if(temp.length <= 3)
			            		courseinfo.setLocation(temp[1], num);
			            	else
			            		courseinfo.setLocation(temp[1] + " / " + temp[4], num);	//���ǽ��� 2���� �ִ� ���� ����ó��
			            	break;
			            case 3:
			            	courseinfo.setCourseDate(value, num);
			            	break;
			            case 4:
			            	if (!value.equals("false"))
			            		courseinfo.setPersonNum(Float.parseFloat(value), num);	//�����ο� ������ ���� ���� ����ó��
			            	else
			            		courseinfo.setPersonNum(0, num);
			            }	//end of switch
			        }	//end of for loop
			        
			        }	//end of if
		        num++;
			}	//end of for loop
		}
				
		//print
		for(int i=0; i<courseinfo.COURSE_NUM; i++) {
        	System.out.println("���Ǹ� : "+ courseinfo.getCourseName(i));
        	System.out.println("�г�: "+ courseinfo.getGrade(i));
        	System.out.println("��� : "+ courseinfo.getLocation(i));
        	System.out.println("���ǽð� : "+ courseinfo.getCourseDate(i));
        	System.out.println("�����ο� : "+ courseinfo.getPersonNum(i));
        	System.out.println();
        }
		
		
		//WriteExcel wExcel = new WriteExcel();
		
	}
}

		
   