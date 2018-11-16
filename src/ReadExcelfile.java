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
		this.path = "C:\\Users\\jiho\\eclipse-workspace\\Course\\src\\CourseList.xls";
	}
	
	public void readExcel(String str) throws IOException{
		FileInputStream fis=new FileInputStream(path);
		HSSFWorkbook workbook=new HSSFWorkbook(fis);

		int rowindex=0;
		int columnindex=0;
		int num =0;
		int check;	//������� ������ Ȯ��(False:0, True:1)
		
		for(int i=0; i<workbook.getNumberOfSheets(); i++) {
			HSSFSheet sheet = workbook.getSheetAt(i);	//sheet ����
			
			//���� ��
			int rows=sheet.getPhysicalNumberOfRows();
			for(rowindex=1;rowindex<rows;rowindex++){
			    //���� �д´�
			    HSSFRow row=sheet.getRow(rowindex);
			    check=0;
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
			            if(value.contains(str) || check==1) {
			            	check=1;
				            switch (columnindex % 5) {
				            case 0:
				            	courseinfo.setCourse_name(value, num);
				            	break;
				            case 1:
				            	courseinfo.setLocation(value, num);
				            	break;
				            case 2:
				            	//���ǰ� �� ���� �������� ���� ������
				            	if(!value.contains("/")) {
				            		courseinfo.setday(value.substring(0,1), num);
				            		//��7,8,9 �����ϋ�
				            		if(! value.substring(1,2).equals("(") ) {
					            		String temp = value.substring(1, value.length());	//���ϰ� �ð� �и�
							            String[] temp2 = temp.split(",");	//�ð� �и�
							           	courseinfo.setStart_time(temp2[0] + "A", num);	//���۽ð�
					            	}
					            	else {	//��(16:00~ ���� �϶�)
					            		String temp = value.substring(2, 7);	//���ϰ� �ð� �и�
						            	String[] temp2 = temp.split(":");	//�ð� �и�
						            	String time = changeTime(temp2);
						            	courseinfo.setStart_time(time, num);	//���۽ð�
					            	}
				            	}
				            	else {	//���ǰ� �� ���� �������� ������
				            		courseinfo.setday(value.substring(0,1), num);
				            		String[] temp2 = value.split("/");	//�� ���� ����
				            		
				            		//��1,2 / ��1,2
				            		if(! value.substring(1,2).equals("(")) {
				            			
				            		}
				            		else {	//ȭ(15:00~17:00) / ��(15:00~16:00)
				            			
				            		}
				            	}	//���� ������ �������� ��������?? �Ƴ�...
				            	break;
				            case 3:
				            	courseinfo.setCode(value, num);
				            	break;
				            case 4:
				            	courseinfo.setClassnum(value, num);
				            	num++;
				            }	//end of switch
				        
			            }// end of if
			            else
			            	break;
			        }	//end of for loop
			        
			        }	//end of if
		        
			}	//end of for loop
		}
				
	}
	
	public CourseInfo getCourseInfo() {
		return this.courseinfo;
	}
	
	//�ð��� ���÷� ��ȯ
	public String changeTime(String[] temp) {
		String time="";
		
		switch(temp[0]) {
		case "08":
			time="0";
			time+=changeTime2(temp[1]);
			break;
		case "09":
			time="1";
			time+=changeTime2(temp[1]);
			break;
		case "10":
			time="2";
			time+=changeTime2(temp[1]);
			break;
		case "11":
			time="3";
			time+=changeTime2(temp[1]);
			break;
		case "12":
			time="4";
			time+=changeTime2(temp[1]);
			break;
		case "13":
			time="5";
			time+=changeTime2(temp[1]);
			break;
		case "14":
			time="6";
			time+=changeTime2(temp[1]);
			break;
		case "15":
			time="7";
			time+=changeTime2(temp[1]);
			break;
		case "16":
			time="8";
			time+=changeTime2(temp[1]);
			break;
		case "17":
			time="9";
			time+=changeTime2(temp[1]);
			break;
		case "18":
			time="10";
			time+=changeTime2(temp[1]);
			break;
		case "19":
			time="11";
			time+=changeTime2(temp[1]);
			break;
		case "20":
			time="12";
			time+=changeTime2(temp[1]);
			break;
		case "21":
			time="13";
			time+=changeTime2(temp[1]);
		}
		return time;
	}
	
	//0~29���� A 30~59���� B�� ��ȯ�Ѵ�
	public String changeTime2(String temp) {
		String time="";
		int min = Integer.parseInt(temp);
		
		if(min >=0 && min <30)
			time = "A";
		else
			time = "B";
		
		return time;
	}
}