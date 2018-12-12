
public class Table {
	
	
	private boolean check;	//강의가 있는지 check
	private String place;	//강의가 있으면 그때 어느 강의실인지에 대한 정보
	
	
	//constructor
	Table(){
		this.check = false;	//default
		this.place = "";
	}
		
	
	/*  set method*/
	void setCheck(boolean check) {
		this.check = check;
	}
	
	void setPlace(String place) {
		this.place = place;
	}
	
	/*  get method*/
	
	boolean getTableCheck() {
		return this.check;
	}
	
	String getTablePlace() {
		return this.place;
	}	
}
