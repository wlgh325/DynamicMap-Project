import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

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


public class Frame extends JFrame{
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
														// 왼쪽 총 Panel
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
	private Object[] time = {"0","1A", "1B","2A","2B", "3A", "3B", "4A", "4B", "5A", "5B", "6A","6B","7A","7B","8A","8B","9A", "9B", "10", "11" };
	private Object[] mon_column;
	private Object[] tue_column;
	private Object[] wed_column;
	private Object[] thr_column;
	private Object[] fri_column;
	
	
	/* constructor */
	public Frame(){
    	
		/* 
		 * initialize 
		 * */
		
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
				System.out.println("search click");
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
				//course_nameBox.removeAllItems();
				
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
            	int selectedIndex = 0;
            	
            	try {
            		selectedIndex = course_nameBox.getSelectedIndex();	//combobox의 index            		
            		selectedItem = searchTime[selectedIndex].split("/");	//comboBox에서 선택한 강의 시간
            		
            		
            		for(int i=0; i<selectedItem.length; i++) {
	            		switch(selectedItem[i].charAt(0)) {
	            			case '월':
	            				setCourseSchedule(time, selectedItem, selectedIndex, 1,i);
	            				break;
	            			case '화':
	            				setCourseSchedule(time, selectedItem, selectedIndex, 2,i);
	            				break;
	                    	case '수':
	                    		setCourseSchedule(time, selectedItem, selectedIndex, 3,i);
	                    		break;
	            			case '목':
	            				setCourseSchedule(time, selectedItem, selectedIndex, 4,i);
	            				break;
	            			case '금':
	            				setCourseSchedule(time, selectedItem, selectedIndex, 5,i);
	            			}
            		}
            	}catch(Exception ie){
            		JOptionPane.showMessageDialog(null, "강의를 선택해주세요");
            		ie.printStackTrace();
            	}
            	
            }
        });
		
		
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
	
	public void setCourseSchedule(Object[] time, String[] selectedItem, int selectedIndex, int columnIndex, int i) {
		int course_time_index;
		//몇시의 강의인지 찾기
		for(course_time_index=0; course_time_index < time.length; course_time_index++) {
			if(time[course_time_index].equals(selectedItem[0].substring(1,3)) ) {
				break;
			}
		}
		
		String[] tempTotalTime = searchTotalTime[selectedIndex].split("/");
		
		for(int j=course_time_index; j< course_time_index + Integer.parseInt(tempTotalTime[i]); j++) {
			//column[j] =searchName[selectedIndex];
			if(add_model.getValueAt(j, columnIndex) == null || add_model.getValueAt(j, columnIndex).toString().length() == 0)
				add_model.setValueAt(searchName[selectedIndex], j, columnIndex);
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
}

