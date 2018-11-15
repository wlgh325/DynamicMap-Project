import java.io.IOException;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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

public class Main extends Application {
	
	/* 오른쪽 Panel */
	static GridPane p1 = new GridPane();
	/* 왼 쪽 Panel */
	static GridPane p2 = new GridPane(); // 왼쪽 맨 위 부분

	static FlowPane p3 = new FlowPane();
	
	static BorderPane p4 = new BorderPane();
	
	static BorderPane p5 = new BorderPane();
	
	String[] date = { " ", "Sun", "Mon", "Tue", "Wed", "Thr", "Fri", "Sat" };
	static TextField searchtext;

	static ComboBox<String> course_nameBox;
	static ComboBox<String> course_dayBox;
	static ComboBox<String> course_locationBox;
	static ComboBox<Integer> course_timeBox;
	static CourseInfo courseinfo;
	
	@Override
	public void start(Stage stage) throws Exception{
		Button add_button = new Button("Add");
		Button list_button = new Button("List");
		Button load_button = new Button("Load");
		Button save_button = new Button("save");
		Button searchButton = new Button("Search");
		
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
		p1.add(list_button,1,0,1,1);
		p1.add(load_button,2,0,1,1);
		p1.add(save_button,3,0,1,1);
		
		p2.setAlignment(Pos.CENTER);
		p2.add(new Label("과목명"), 0,0,3,1);
		p2.add(new Label("요일"), 1,0,1,1);
		p2.add(new Label("강의실"), 2,0,3,1);
		p2.add(new Label("시간"), 3,0,1,1);
		p2.add(course_nameBox, 0,1,3,1);
		p2.add(course_dayBox, 1, 1,1,1);
		p2.add(course_locationBox, 2, 1,3,1);
		p2.add(course_timeBox, 3, 1,1,1);
		
		p3.getChildren().add(searchtext);
		p3.getChildren().add(searchButton);
		
		p4.setTop(p4);
		p4.setCenter(p2);
		p4.setBottom(p5);
		p4.setLeft(p1);
		
		searchButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event){
            	//새로 목록 출력하기        	
				String str = searchtext.getText();
				System.out.print("click search button: ");
				System.out.println(str);
				showCourseInfo(str);
				
				
				//검색된 강의정보 엑셀에서 읽어오기
				ReadExcelfile read_excel = new ReadExcelfile();
				try {
					read_excel.readExcel(str);
					courseinfo = read_excel.getCourseInfo();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        });

		load_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	//밑에 판넬부분에 추가
            }
        });
		
		/* 왼쪽 밑 Panel 만들기 */
		Scene scene = new Scene(p4, 800, 600);
		stage.setScene(scene);
		stage.setTitle("MakeCourseSchedule");
		stage.show();

		
	}
	
	public void showCourseInfo(String str) {
		
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
