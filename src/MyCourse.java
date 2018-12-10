
public class MyCourse {
	final int MAX_COURSE = 10;
	private String[] name;
	private String[] place;
	private String[] day;
	private int[] start_time;
	private int[] end_time;
	
	
	
	// Constructor
	MyCourse(){
		this.name = new String[MAX_COURSE];
		this.place = new String[MAX_COURSE];
		this.start_time = new int[MAX_COURSE];
		this.end_time = new int[MAX_COURSE];
		this.day = new String[MAX_COURSE];
	}
	
	
	/*  set method*/
	void setName(String name, int index) {
		this.name[index] = name;
	}
	
	void setPlace(String place, int index) {
		this.place[index] = place;
	}
	
	void setDay(String day, int index) {
		this.day[index] = day;
	}
	
	void setStartTime(int time, int index) {
		this.start_time[index] = time;
	}
	
	void setEndTime(int time, int index) {
		this.end_time[index] = time;
	}
	

	/*  get method*/
	
	String getName(int index) {
		return this.name[index];
	}
	
	String getPlace(int index) {
		return this.place[index];
	}
	
	String getDay(int index) {
		return this.day[index];
	}
	
	int getStartTime(int index) {
		return this.start_time[index];
	}
	
	int getEndTime(int index) {
		return this.end_time[index];
	}
}
