import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import processing.core.PApplet;


public class Frame extends JFrame implements MouseListener{

	final int DEFAULT_TIME = 2;
	
	private CourseInfo courseinfo;

	private String[] searchName;
	private String[] searchLocation;
	private String[] searchTime;
	private String[] searchCode;
	private String[] searchClassNum;
	private String[] searchContents;
	private String[] searchTotalTime;

	/* 오른쪽 Panel */
	private JPanel p1 = new JPanel();


	/* 왼 쪽 Panel */
	private JPanel p2 = new JPanel(new FlowLayout()); // 왼쪽 맨 위 부분
	private JPanel p3 = new JPanel(new FlowLayout(FlowLayout.LEFT, 70,5)); // 왼쪽 Panel My Courses 부분
	private JPanel p4 = new JPanel(new BorderLayout()); // My Courses밑 부분 칸
	private JPanel p5 = new JPanel(new BorderLayout()); // p3와 p4를 합친왼쪽 왼 쪽 밑 부분
	private JPanel p6 = new JPanel(new BorderLayout()); // p5와 p2(왼쪽 아래와 위)을 합친

	private JPanel p7 = new JPanel(new BorderLayout());
	private JPanel p8 = new JPanel(new GridLayout(1,5));
	private JPanel p9 = new JPanel(new GridLayout(1,5));
	private JTextField searchtext;
	private JComboBox<String> course_nameBox;

	/* Button */
	private JButton add_button;
	private JButton list_button;
	private JButton load_button;
	private JButton save_button;
	private JButton search_button;

	/* Label */
	private JLabel name_label;
	private JLabel location_label;
	private JLabel time_label;
	private final int COURSE_TIME = 21;

	/* Table */
	private String[] colname = {"", "Mon", "Tue", "Wed", "Thu", "Fri" }; // table column의 이름
	private DefaultTableModel add_model;	//table에 표시할 값
	private JScrollPane scroll;
	private JTable table;

	/* column 정보를 담는 object */
	private Object[] time = {"0","1A", "1B","2A","2B", "3A", "3B", "4A", "4B", "5A", "5B", "6A","6B","7A","7B","8A","8B","9A", "9B", "10A", "10B", "11", "12" };
	private Object[] mon_column;
	private Object[] tue_column;
	private Object[] wed_column;
	private Object[] thr_column;
	private Object[] fri_column;


	private ArrayList<Integer>[][][] runningElevatorInfo;
	private float x = 930.0f;
	private float y = 0.209f;
	private float z = 113.0f;
	private ReadCoursenum readCoursenum;
	private Optimize optimizer;
	private Table[][] tableInfo;

	private String before_coursePlace;
	private String after_coursePlace;
	
	// referenced by DrawingPath
	static boolean magicCheck;
	static String magicFloor;
	static String path;
	
	/* constructor */
	public Frame() throws IOException{
		/*
		 * initialize
		 * */

		readCoursenum = new ReadCoursenum();
		readCoursenum.readExcel();
		optimizer = new Optimize(readCoursenum.getCoursenum());
		optimizer.setXYZ(x,y,z);
		optimizer.OptimizeElevator();
		this.runningElevatorInfo = optimizer.getRunningElevatorInfo();

		/*
		for(int i=0; i<Coursenum.DAY_NUM; i++) {
			System.out.println("-----------------");
			System.out.println(i);
			System.out.println("-----------------");
			for(int j=0; j<Coursenum.ROW_NUM; j++) {
				System.out.println("-----------------");
				System.out.println("Time : " + j);
				for(int k=0; k<optimizer.ELEVATOR_NUM; k++) {
					System.out.println("elevator num : " + k);					
					System.out.println(this.runningElevatorInfo[i][j][k]);
					System.out.println();
				}
			}
		}
			*/
				
					
		
		this.tableInfo = new Table[Coursenum.ROW_NUM][Coursenum.DAY_NUM];
		for(int i=0; i<Coursenum.ROW_NUM; i++)
			for(int j=0; j<Coursenum.DAY_NUM; j++)
				this.tableInfo[i][j] = new Table();

		before_coursePlace = "";
		after_coursePlace = "";
		
		add_button = new JButton("Add");
		list_button = new JButton("List");
		load_button = new JButton("Load");
		save_button = new JButton("save");
		search_button = new JButton("Search");
		searchtext = new JTextField(15);
		course_nameBox= new JComboBox<String>();
		name_label = new JLabel("과목명");
		location_label = new JLabel("강의실");
		time_label = new JLabel("강의시간");


		mon_column = new Object[COURSE_TIME];
		tue_column = new Object[COURSE_TIME];
		wed_column = new Object[COURSE_TIME];
		thr_column = new Object[COURSE_TIME];
		fri_column = new Object[COURSE_TIME];
		add_model = new DefaultTableModel();	//table에 표시할 값

		setModel();
		table = new JTable(add_model);
		DefaultTableCellRenderer celAlignCenter = new DefaultTableCellRenderer();
		celAlignCenter.setHorizontalAlignment(JLabel.CENTER);


		/* right button panel */
		p1.setLayout(new GridLayout(4, 1));
		p1.add(add_button);
		p1.add(list_button);
		p1.add(load_button);
		p1.add(save_button);


		/* 왼쪽 위 panel */
		p2.add(searchtext);
		p2.add(search_button);


		/* 왼쪽 밑 Panel */
		p3.add(name_label);
		p3.add(location_label);
		p3.add(time_label);


		/* 왼쪽 위 합치기 */
		p7.add(p2, BorderLayout.NORTH);
		p7.add(p3, BorderLayout.CENTER);


		/* 아래 panel */
		p8.add(course_nameBox);
		table = new JTable(add_model);	//table 생성

		table.setRowHeight(27);
		scroll = new JScrollPane(table);	//table에 스크롤 생기게 하기
		scroll.setSize(576,700);
		p4.add(scroll);

		p5.add(p8, BorderLayout.NORTH);
		p5.add(p4, BorderLayout.CENTER);


		/* 위(p7)와 아래(p5)를 합친 총 왼쪽 Panel */
		p6.add(p7, BorderLayout.NORTH);
		p6.add(p5, BorderLayout.CENTER);


		search_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	String str = searchtext.getText();	//검색 강의명

				//검색된 강의정보 엑셀에서 읽어오기
				ReadExcelfile read_excel = new ReadExcelfile();
				try {
					read_excel.readExcel(str);
					courseinfo = read_excel.getCourseInfo();
				} catch (IOException ioException) {
					JOptionPane.showMessageDialog(null, "강의를 입력해주세요");
					ioException.printStackTrace();
				}

				//검색된 정보들 가져오기
				setSearchInform();


				//검색목록 출력
				//새로 목록 출력하기
				course_nameBox.removeAllItems();

				//comboBox 값 넣기
				for(int i=0; i<searchContents.length; i++) {
					course_nameBox.addItem(searchContents[i]);
				}
				searchtext.setText("");
            }
        });


		add_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	String selectedItem[];
            	String[] place;
            	int selectedIndex = 0;
					int j=0;

            	try {
            		selectedIndex = course_nameBox.getSelectedIndex();	//combobox의 index
            		selectedItem = searchTime[selectedIndex].split("/");	//comboBox에서 선택한 강의 시간
            		place = searchLocation[selectedIndex].split("/");
            		String temp_place = "";
            		// 강의실이 310관에 있을때
            		for(int i=0; i<selectedItem.length; i++, j++) {
            			if(i < place.length){
            				
            			String temp_string = place[i].trim();
						if (temp_string.substring(0,3).equals("310")){
							if(j < place.length) {
								String[] temp = temp_string.split(" ");	//parsing
								temp_place = temp[1];
							}			
						}
						else{
							
							switch(temp_string.substring(0,3)){
								case "102":
								case "103":
								case "104":
								case "105":
								case "106":
								case "203":
									temp_place = "B599호";
									break;
								case "207":
								case "208":
								case "209":
									temp_place = "B399호";
									break;
								case "301":
								case "302":
								case "303":
								case "304":
								case "305":
									temp_place = "199호";
									break;
								case "308":
								case "309":
									temp_place ="198호";
							}
						}
            			}
	       	        	switch(selectedItem[i].charAt(0)) {
	       	        		case '월':
	       	        			setCourseSchedule(time, selectedItem[i], selectedIndex, 1,i, temp_place);
	                 			break;
	                   		case '화':
	  	            				setCourseSchedule(time, selectedItem[i], selectedIndex, 2,i, temp_place);
	     	            			break;
	     	                 	case '수':
	 	            				setCourseSchedule(time, selectedItem[i], selectedIndex, 3,i, temp_place);
	        	                 	break;
	        	            	case '목':
	        	            		setCourseSchedule(time, selectedItem[i], selectedIndex, 4,i, temp_place);
	        	            		break;
	        	            	case '금':
	        	            		setCourseSchedule(time, selectedItem[i], selectedIndex, 5,i, temp_place);
        	            } // end of switch
						} // end of for loop
            	}catch(Exception ie){
            		JOptionPane.showMessageDialog(null, "강의를 선택해주세요");
            		ie.printStackTrace();
            	}
            }
        });

		table.addMouseListener(this);

		Container contentPane = this.getContentPane();
		contentPane.add(p6, BorderLayout.CENTER);
		contentPane.add(p1, BorderLayout.EAST);
		this.setTitle("MakeCourseSchedule");
		this.setSize(650, 700);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
	}

	public void setSearchInform() {
		searchName = courseinfo.getCourse_name();
		searchLocation = courseinfo.getLocation();
		searchTime = courseinfo.getCoursetime();
		searchCode = courseinfo.getClassnum();
		searchTotalTime = courseinfo.getTotalTime();

		int length = searchName.length;
		searchContents = new String[length];

		//비어있는 배열(null) 전까지만 값 넣기
		for(int i=0; !(searchName[i] == null || searchName[i].length() == 0); i++) {
			searchContents[i] = searchName[i] + "  " + searchLocation[i] + "  " + searchTime[i];
		}
	}



	public void setCourseSchedule(Object[] time, String selectedItem, int selectedIndex, int columnIndex, int i, String place) {
		int course_time_index;
		int index=0;


		//몇시의 강의인지 찾기
		for(course_time_index=0; course_time_index < time.length; course_time_index++) {
			if(time[course_time_index].equals(selectedItem.substring(1,selectedItem.length())) ) {
				break;
			}
		}

		this.tableInfo[course_time_index][columnIndex-1].setCheck(true);


		String[] tempTotalTime = searchTotalTime[selectedIndex].split("/");


		for(int j=course_time_index; j< course_time_index + (Integer.parseInt(tempTotalTime[i]) * 2); j++) {
			if(add_model.getValueAt(j, columnIndex) == null || add_model.getValueAt(j, columnIndex).toString().length() == 0) {
				add_model.setValueAt(searchName[selectedIndex], j, columnIndex);	//time table에 강의 추가하기
				this.tableInfo[j][columnIndex-1].setCheck(true);
				this.tableInfo[j][columnIndex-1].setPlace(place.substring(0, place.length() -1));
			}
			else {
				JOptionPane.showMessageDialog(null, "이미 중복되는 시간에 강의가 있습니다");
				break;
			}
		}
	}


	public void setModel() {
		/* table Model */
    	add_model.addColumn(colname[0], time);			//시간 column
    	add_model.addColumn(colname[1], mon_column);	//월
    	add_model.addColumn(colname[2], tue_column);	//화
    	add_model.addColumn(colname[3], wed_column);	//수
    	add_model.addColumn(colname[4], thr_column);	//목
    	add_model.addColumn(colname[5], fri_column);	//금

	}


	/* Jtable click listener */
	public void mouseClicked(MouseEvent me) {
		int row = table.getSelectedRow();
		int column = table.getSelectedColumn();
		int temp;
		magicCheck = false;
		
		ArrayList<Integer>[][] magicfloor = this.optimizer.getMagicFloorInfo();
			
		//System.out.println("row: " + row + "  column: " + column);
		if(table.getValueAt(row, column) != null) {
			temp = cal_start_end_place(row, column);
			
			//magic time check
			if( magicfloor[column-1][temp].size() != 0 ) {
				magicCheck = true;
			}
			System.out.println("magic check:: " + magicCheck + " column-1 : " + (column-1) + "  temp: " + temp);
			makeGraph(temp, column -1);	
			
			//call DrawingPath
			PApplet.main("DrawingPath");
		}
		else {
			JOptionPane.showMessageDialog(null, "비어있는 시간표입니다");
		}
		
		
	}

	@Override
	public void mousePressed(MouseEvent e) {

	}


	@Override
	public void mouseReleased(MouseEvent e) {

	}

	@Override
	public void mouseEntered(MouseEvent e) {

	}

	@Override
	public void mouseExited(MouseEvent e) {

	}

	public int cal_start_end_place(int row, int column) {
		int check =row;	//after check(누른거)
		int check2=row;	//before check(전 강의)

		// 이전 강의 위치 계산
		int temp = row;
		for(; check > 0; check --) {
			if(!this.tableInfo[check][column - 1].getTablePlace().equals( this.tableInfo[check-1][column - 1].getTablePlace() )) {
				break;
			}
		}
		
		if(check != 23)
			this.after_coursePlace = this.tableInfo[check][column-1].getTablePlace();
		else
			JOptionPane.showMessageDialog(null, "전 시간에 강의가 없습니다");
		
		
		if(check != 0) {
			for(; check2 >0; check2--) {
				if(!this.tableInfo[check][column - 1].getTablePlace().equals( this.tableInfo[check2-1][column - 1].getTablePlace() ))
					if(!this.tableInfo[check2-1][column - 1].getTablePlace().equals("")) {
						check2--;
						break;
					}	
			}
		}
		else {
			this.before_coursePlace = "199";
			return check;
		}
		
		if(check2 != 0) {
			this.before_coursePlace = this.tableInfo[check2][column - 1].getTablePlace();
		}
		else {
			this.before_coursePlace = "199";
		}
			
		
		//공강시간이 2시간 이상일경우
		if(check2 - check >= this.DEFAULT_TIME)
			this.before_coursePlace = "B399";
	
		return check;
	}

	public void makeGraph(int time, int day) {
	      Graph graph = new Graph();
	      ReadCoursenum readCoursenum = new ReadCoursenum();
	      int rm =0;
	      
	      try {
	         readCoursenum.readExcel();
	      } catch (IOException e) {
	         // TODO Auto-generated catch block
	         e.printStackTrace();
	      }
	      String[] elevator_one = new String[10]; //10호기까지 있으므로
	      int[][] elevator_two = new int[10][];
	      
	      String graphPath = "C:\\Users\\senno\\Desktop\\310_Dynamic_map_proj\\data\\310Graph.txt";
	      String nodeDistancePath = "C:\\Users\\senno\\Desktop\\310_Dynamic_map_proj\\data\\NodeDistance.txt";
	      String roomDistancePath = "C:\\Users\\senno\\Desktop\\310_Dynamic_map_proj\\data\\roomDistance.txt";
	      String floorDistancePath = "C:\\Users\\senno\\Desktop\\310_Dynamic_map_proj\\data\\FloorDistance.txt";
	      
	      //시작이나 끝지점 예를 들어서 서라벌에서 -> 316강의실가고싶다하시면 서라벌이라는게 B599라는 겁니다.
	      // 그런게 몇개있는데
	      //서라벌 = B599, 제2공학관 = B399 후문 199 기숙사 198이렇게 됩니다.
	      String startPoint = this.before_coursePlace;
	      String endPoint = this.after_coursePlace;
	      
	      
	      //엘베정보 받아오기
	      //엘베 다른 시간에 걸 받아올려면  지금 [1][9] 로 되어있는부분의 숫자를 바꿔 주시면됩니다. 요일, 시간 엘레베이터 호 순이라서 지금 1 = 화요일  9 4교시 a일껄요
	      for(int i= 0; i< this.runningElevatorInfo[day][time].length; i++) {
	         for(int k =0; k<this.runningElevatorInfo[day][time][i].size();k++ )
	         {
	            if(this.runningElevatorInfo[day][time][i].get(k)>=10) {
	               rm++;
	            }
	         }
	         elevator_two[i] = new int[this.runningElevatorInfo[day][time][i].size()-rm];
	         for(int j = 0; j<this.runningElevatorInfo[day][time][i].size();j++)
	         {
	            if(this.runningElevatorInfo[day][time][i].get(j)<10)
	            {
	               elevator_two[i][j] = this.runningElevatorInfo[day][time][i].get(j);
	            }
	         }
	         rm =0;
	      }
	      graph.readFile(graphPath);
	      graph.readNodeDistanceFile(nodeDistancePath);
	      graph.readRoomDistanceFile(roomDistancePath);
	      graph.readFloorDistance(floorDistancePath);
	      
	      //엘베 정보 넣기
	      graph.putElevate(elevator_two);
	      System.out.println(startPoint+endPoint);
	      path = graph.sortPath(startPoint, endPoint);
	      magicFloor=graph.magicFloorInfo(path);
	      //graph.sortPath(endPoint, startPoint);
	}
	
}