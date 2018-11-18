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
	
	/* ������ Panel */
	private JPanel p1 = new JPanel();
	
	/* �� �� Panel */
	private JPanel p2 = new JPanel(new FlowLayout()); // ���� �� �� �κ�

	private JPanel p3 = new JPanel(new FlowLayout(FlowLayout.LEFT, 70,5)); // ���� Panel My Courses �κ�
	private JPanel p4 = new JPanel(new BorderLayout()); // My Courses�� �κ� ĭ

	private JPanel p5 = new JPanel(new BorderLayout()); // p3�� p4�� ��ģ���� �� �� �� �κ�

	private JPanel p6 = new JPanel(new BorderLayout()); // p5�� p2(���� �Ʒ��� ��)�� ��ģ
														// ���� �� Panel
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
	private String[] colname = {"", "Mon", "Tue", "Wed", "Thu", "Fri" }; // table column�� �̸�
	private DefaultTableModel add_model;	//table�� ǥ���� ��
	private JScrollPane scroll;
	private JTable table;
	
	/* column ������ ��� object */
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
		name_label = new JLabel("�����");
		location_label = new JLabel("���ǽ�");
		time_label = new JLabel("���ǽð�");

		mon_column = new Object[COURSE_TIME];
		tue_column = new Object[COURSE_TIME];
		wed_column = new Object[COURSE_TIME];
		thr_column = new Object[COURSE_TIME];
		fri_column = new Object[COURSE_TIME];
		add_model = new DefaultTableModel();	//table�� ǥ���� ��
		
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
		
		
		/* ���� �� panel */	
		p2.add(searchtext);
		p2.add(search_button);
	
		
		/* ���� �� Panel */
		p3.add(name_label);
		p3.add(location_label);
		p3.add(time_label);
		
	
		/* ���� �� ��ġ�� */
		p7.add(p2, BorderLayout.NORTH);
		p7.add(p3, BorderLayout.CENTER);
		
		
		/* �Ʒ� panel */
		p8.add(course_nameBox);
		table = new JTable(add_model);	//table ����
		
		table.setRowHeight(27);
		scroll = new JScrollPane(table);	//table�� ��ũ�� ����� �ϱ�
		scroll.setSize(576,700);
		p4.add(scroll);
		
		p5.add(p8, BorderLayout.NORTH);
		p5.add(p4, BorderLayout.CENTER);

		
		/* ��(p7)�� �Ʒ�(p5)�� ��ģ �� ���� Panel */
		p6.add(p7, BorderLayout.NORTH);
		p6.add(p5, BorderLayout.CENTER);
		
		
		search_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	String str = searchtext.getText();	//�˻� ���Ǹ�
				System.out.println("search click");
				//�˻��� �������� �������� �о����
				ReadExcelfile read_excel = new ReadExcelfile();
				try {
					read_excel.readExcel(str);
					courseinfo = read_excel.getCourseInfo();
				} catch (IOException ioException) {
					JOptionPane.showMessageDialog(null, "���Ǹ� �Է����ּ���");
					ioException.printStackTrace();
				}
				
				//�˻��� ������ ��������
				setSearchInform();
				        		
				//�˻���� ���
				
				//���� ��� ����ϱ�
				//course_nameBox.removeAllItems();
				
				//comboBox �� �ֱ�
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
            		selectedIndex = course_nameBox.getSelectedIndex();	//combobox�� index            		
            		selectedItem = searchTime[selectedIndex].split("/");	//comboBox���� ������ ���� �ð�
            		
            		
            		for(int i=0; i<selectedItem.length; i++) {
	            		switch(selectedItem[i].charAt(0)) {
	            			case '��':
	            				setCourseSchedule(time, selectedItem, selectedIndex, 1,i);
	            				break;
	            			case 'ȭ':
	            				setCourseSchedule(time, selectedItem, selectedIndex, 2,i);
	            				break;
	                    	case '��':
	                    		setCourseSchedule(time, selectedItem, selectedIndex, 3,i);
	                    		break;
	            			case '��':
	            				setCourseSchedule(time, selectedItem, selectedIndex, 4,i);
	            				break;
	            			case '��':
	            				setCourseSchedule(time, selectedItem, selectedIndex, 5,i);
	            			}
            		}
            	}catch(Exception ie){
            		JOptionPane.showMessageDialog(null, "���Ǹ� �������ּ���");
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
		
		//����ִ� �迭(null) �������� �� �ֱ�
		for(int i=0; !(searchName[i] == null || searchName[i].length() == 0); i++) {
			searchContents[i] = searchName[i] + "  " + searchLocation[i] + "  " + searchTime[i];
		}
	}
	
	public void setCourseSchedule(Object[] time, String[] selectedItem, int selectedIndex, int columnIndex, int i) {
		int course_time_index;
		//����� �������� ã��
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
				JOptionPane.showMessageDialog(null, "�̹� �ߺ��Ǵ� �ð��� ���ǰ� �ֽ��ϴ�");
				break;
			}	
		}
	}
	
	public void setModel() {
		
		/* table Model */
    	add_model.addColumn(colname[0], time);			//�ð� column
    	add_model.addColumn(colname[1], mon_column);	//��
    	add_model.addColumn(colname[2], tue_column);	//ȭ
    	add_model.addColumn(colname[3], wed_column);	//��
    	add_model.addColumn(colname[4], thr_column);	//��
    	add_model.addColumn(colname[5], fri_column);	//��
    	
	}
}

