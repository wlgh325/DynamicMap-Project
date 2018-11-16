import java.io.IOException;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
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
	private String[] searchLocation;
	private String[] searchTime;
	private String[] searchCode;
	private String[] searchClassNum;
	private String[] searchContents;
	
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
	private CourseInfo courseinfo;
	
	/* Button */
	private Button add_button;
	private Button list_button;
	private Button load_button;
	private Button save_button;
	private Button searchButton;
	
	
	/* Label */
	private Label name_label;
	private Label location_label;
	private Label time_label;
	
	@Override
	public void start(Stage stage) throws Exception{
		
		/* 
		 * initialize 
		 * */
		
		add_button = new Button("Add");
		list_button = new Button("List");
		load_button = new Button("Load");
		save_button = new Button("save");
		searchButton = new Button("Search");
		
		searchtext = new TextField();
		course_nameBox= new ComboBox<String>();
		course_nameBox.setMinWidth(400);
		name_label = new Label("과목명");
		location_label = new Label("강의실");
		time_label = new Label("강의시간");
		
		
		/* set label size */
		name_label.setMinWidth(60);
		name_label.setMinHeight(20);
		location_label.setMinWidth(100);
		location_label.setMinHeight(20);
		time_label.setMinWidth(50);
		time_label.setMinHeight(20);
		
		
		/* right button panel */
		p1.add(add_button, 0,0,1,1);
		p1.add(list_button,0,1,1,1);
		p1.add(load_button,0,2,1,1);
		p1.add(save_button,0,3,1,1);
		
		
		/* top label panel*/
		
		p2.setHgap(15);
        p2.setVgap(15);
        p2.setPadding(new Insets(5, 5, 5, 5));
        
		p2.getChildren().add(name_label);
		p2.getChildren().add(location_label);
		p2.getChildren().add(time_label);
		
		p6.getChildren().add(course_nameBox);
		p6.setAlignment(Pos.CENTER);
		
		p7.setVgap(5);
		p7.setAlignment(Pos.TOP_CENTER);
		p7.setMinSize(400, 100);
		p7.getChildren().add(p2);
		p7.getChildren().add(p6);
		
		p3.setAlignment(Pos.CENTER);
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
				//새로 목록 출력하기
				course_nameBox.getItems().clear();
				
				//comboBox 값 넣기
				
				for(int i=0; searchContents[i] != null; i++) {
					course_nameBox.getItems().add(searchContents[i]);
				}
            }
        });

		
		//시간표 table에 추가하기
		add_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event){
				
            }
        });

		
		Scene scene = new Scene(p4, 600, 300);
		stage.setScene(scene);
		stage.setTitle("MakeCourseSchedule");
		stage.show();

		
	}
	
	public void setSearchInform() {
		searchName = courseinfo.getCourse_name();
		searchLocation = courseinfo.getLocation();
		searchTime = courseinfo.getCoursetime();
		searchCode = courseinfo.getClassnum();
		//searchClassNum = courseinfo.getClassnum();
		
		int length = searchName.length;
		searchContents = new String[length];
		
		//비어있는 배열(null) 전까지만 값 넣기
		for(int i=0; !(searchName[i] == null || searchName[i].length() == 0); i++) {
			
			searchContents[i] = searchName[i] + "  " + searchLocation[i] + "  " + searchTime[i];
		}
	}
	
}
