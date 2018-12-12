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
		this.path = "C:\\Users\\Guest1\\eclipse-workspace\\Course_swing\\data\\CourseList.xls";
	}
	
	public void readExcel(String str) throws IOException{
		FileInputStream fis=new FileInputStream(path);
		HSSFWorkbook workbook=new HSSFWorkbook(fis);

		int rowindex=0;
		int columnindex=0;
		int num =0;
		int check;	//과목명이 같은지 확인(False:0, True:1)
		
		for(int i=0; i<workbook.getNumberOfSheets(); i++) {
			HSSFSheet sheet = workbook.getSheetAt(i);	//sheet 생성
			
			//행의 수
			int rows=sheet.getPhysicalNumberOfRows();
			for(rowindex=1;rowindex<rows;rowindex++){
			    //행을 읽는다
			    HSSFRow row=sheet.getRow(rowindex);
			    check=0;
			    if(row !=null){
			        //셀의 수
			        int cells=row.getPhysicalNumberOfCells();
			        for(columnindex=0;columnindex<=cells;columnindex++){
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

			            //Integer.parseInt(value)
			            //set courseInfo
			            
			            //value와 str이 같을때 정보를 저장하고 보여준다
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
				            	//강의가 두 날에 나누어져 있지 않을때
				            	if(!value.contains("/")) {
				            		String day = value.substring(0,1);	//요일분리
				            		//목7,8,9 형식일
				            		if(! value.substring(1,2).equals("(") ) {
					            		String temp = value.substring(1, value.length());	//요일과 시간 분리
							            String[] temp2 = temp.split(",");	//시간 분리
							            
							            if( !temp2[0].equals("0"))
							            	courseinfo.setCoursetime(day + temp2[0] + "A", num);	//강의시간
							            else	//0교시는 A와 B로 시간을 분리하지 않음
							            	courseinfo.setCoursetime(day + temp2[0], num);
							           	courseinfo.setTotalTime(Integer.toString(temp2.length), num);	//총 강의시간
					            	}
					            	else {	//목(16:00~ 형식 일때)
					            		String start_temp = value.substring(2, 7);	//요일과 시간 분리
						            	String[] start_time = start_temp.split(":");	//시간 분리
						            	String time = changeTime(start_time);
						            	courseinfo.setCoursetime(day + time, num);	//시작시간
						            	
						            	//총 강의시간 구하기
						            	String end_temp = value.substring(8,13);
						            	String[] end_time = end_temp.split(":");
						            	int total_time = Integer.parseInt(end_time[0]) - Integer.parseInt(start_time[0]);
						            	courseinfo.setTotalTime(Integer.toString(total_time), num);
					            	}
				            	}
				            	else {	//강의가 두 날에 나누어져 있을때
				            		value = value.replaceAll(" ", "");	//공백 없애기
				            		String[] temp2 = value.split("/");	//두 날로 나눔
				            		String course_time = "";
				            		String day1 = temp2[0].substring(0, 1);	//첫 강의날
				            		String day2 = temp2[1].substring(0, 1);	//두번째 강의날
				            		
				            		course_time += day1;
				            		//목1,2 / 금1,2
				            		if(! value.substring(1,2).equals("(")) {
				            			String day1_time = temp2[0].substring(1, temp2[0].length());	// temp2[0]="목1,2"
				            			String day2_time = temp2[1].substring(1, temp2[1].length());
				             			
				            			String[] temp_time1 = day1_time.split(",");
				            			String[] temp_time2 = day2_time.split(",");
				            			
				            			course_time = day1 + temp_time1[0] + "A" + "/" + day2 + temp_time2[0] + "A";	//목1A / 금1A
				            			
				            			//강의 요일
				            			courseinfo.setCoursetime(course_time, num);	//시작시간
				            			
				            			//총 강의시간 set
				            			courseinfo.setTotalTime(Integer.toString(temp_time1.length) + "/" + Integer.toString(temp_time2.length), num);
				            		}
				            		else {	//화(15:00~17:00) / 목(15:00~16:00)
				            			String day1_time = temp2[0].substring(2, 7);
				            			String[] day1_temp = day1_time.split(":");
				            			day1_time = changeTime(day1_temp);
				            			
				            			String day2_time = temp2[1].substring(2, 7);
				            			String[] day2_temp = day2_time.split(":");
				            			day2_time = changeTime(day2_temp);
				            			course_time = day1 + day1_time + "/" + day2 + day2_time;	//목1A / 금1A
				            			courseinfo.setCoursetime(course_time, num);	//시작시간
				            			
				            			
				            			//총 강의시간 구하기
				            			String end_temp = temp2[0].substring(8,13);
						            	String[] end_time = end_temp.split(":");
				            			int total_time1 = Integer.parseInt(end_time[0]) - Integer.parseInt(day1_temp[0]);
				            			
				            			String end_temp2 = temp2[1].substring(8, 13);
				            			String[] end_time2 = end_temp2.split(":");
				            			int total_time2 = Integer.parseInt(end_time2[0]) - Integer.parseInt(day2_temp[0]);
				            			
				            			courseinfo.setTotalTime(Integer.toString(total_time1) + "/" + Integer.toString(total_time2), num);
				            		}
				            	}	//강의 세날에 나누어져 있을는?? 아놔...
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
	
	//시간을 교시로 변환
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
	
	//0~29분은 A 30~59분은 B로 변환한다
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