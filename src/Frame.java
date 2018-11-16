import java.io.IOException;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class Frame extends Application{
	private String[] searchName;
	private String[] searchDay;
	private String[] searchLocation;
	private String[] searchTime;
	private String[] searchCode;
	private String[] searchClassNum;
	
	private GridPane p1 = new GridPane();
	/* 왼 쪽 Panel */
	private FlowPane p2 = new FlowPane(); // 왼쪽 맨 위 부분

	private FlowPane p3 = new FlowPane();
	
	private BorderPane p4 = new BorderPane();
	
	private BorderPane p5 = new BorderPane();
	
	private FlowPane p6 = new FlowPane();
	
	private FlowPane p7 = new FlowPane();
	
	private TextField searchtext;

	private ComboBox<String> course_nameBox;
	private ComboBox<String> course_dayBox;
	private ComboBox<String> course_locationBox;
	private ComboBox<String> course_timeBox;
	private CourseInfo courseinfo;
	
	/* Button */
	private Button add_button;
	private Button list_button;
	private Button load_button;
	private Button save_button;
	private Button searchButton;
	
	@Override
	public void start(Stage stage) throws Exception{
		
		add_button = new Button("Add");
		list_button = new Button("List");
		load_button = new Button("Load");
		save_button = new Button("save");
		searchButton = new Button("Search");
		
		searchtext = new TextField();
		course_nameBox= new ComboBox<String>();
		course_dayBox= new ComboBox<String>();
		course_locationBox = new ComboBox<String>();
		course_timeBox = new ComboBox<String>();
		
		/*
		AddListenerClass listner1 = new AddListenerClass();
		ListListenerClass listner2 = new ListListenerClass();
		LoadListenerClass listner3 = new LoadListenerClass();
		SaveListenerClass listner4 = new SaveListenerClass();
		b1.addActionListener(listner1);
		b2.addActionListener(listner2);
		b3.addActionListener(listner3);
		b4.addActionListener(listner4);
*/
		
		p1.add(add_button, 0,0,1,1);
		p1.add(list_button,0,1,1,1);
		p1.add(load_button,0,2,1,1);
		p1.add(save_button,0,3,1,1);
		
		//p2.setAlignment(Pos.CENTER);
		p2.setHgap(15);
        p2.setVgap(15);
        p2.setPadding(new Insets(5, 5, 5, 5));
		p2.getChildren().add(new Label("과목명"));
		p2.getChildren().add(new Label("요일"));
		p2.getChildren().add(new Label("강의실"));
		p2.getChildren().add(new Label("시간"));
		
		p6.getChildren().add(course_nameBox);
		//p6.getChildren().add(course_dayBox);
		//p6.getChildren().add(course_locationBox);
		//p6.getChildren().add(course_timeBox);
		
		p7.setVgap(5);
		p7.getChildren().add(p2);
		p7.getChildren().add(p6);
		
		
		p3.getChildren().add(searchtext);
		p3.getChildren().add(searchButton);
		
		p4.setTop(p3);
		p4.setCenter(p7);
		//p4.setBottom(p5);
		p4.setRight(p1);
		
		searchButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event){
				String str = searchtext.getText();	//검색 강의명
				System.out.print("click search button: ");
				
				
				//검색된 강의정보 엑셀에서 읽어오기
				ReadExcelfile read_excel = new ReadExcelfile();
				try {
					read_excel.readExcel(str);
					courseinfo = read_excel.getCourseInfo();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				//검색된 정보들 가져오기
				setSearchInform();
				        		
				//검색목록 출력
				setComboBox(course_nameBox, searchName);
				//setComboBox(course_dayBox, searchDay);
				//setComboBox(course_locationBox, searchLocation);
				//setComboBox(course_timeBox, searchTime);
            }
        });

		load_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	//밑에 판넬부분에 추가
            }
        });
		
		Scene scene = new Scene(p4, 600, 300);
		stage.setScene(scene);
		stage.setTitle("MakeCourseSchedule");
		stage.show();

		
	}
	
	public void setSearchInform() {
		searchName = courseinfo.getCourse_name();
		//searchDay = courseinfo.getday();
		//searchLocation = courseinfo.getLocation();
		//searchTime = courseinfo.getStart_time();
		//searchCode = courseinfo.getClassnum();
		//searchClassNum = courseinfo.getClassnum();

	}
	
	public void setComboBox(ComboBox<String> comboBox, String[] searchContents) {
		//새로 목록 출력하기
		comboBox.getItems().clear();
		
		//comboBoxd 값 넣기
		for(int i=0; i<searchContents.length; i++)
			comboBox.getItems().add(searchContents[i]);
	}
}
