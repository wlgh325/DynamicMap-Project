public class CourseInfo {
	static final int COURSE_NUM = 5504;	//강의 개수
	private String[] course_name;
	private String[] day;
	private int[] course_start_time;
	private String[] location;
	
	/* Constructor */
	CourseInfo() {
		this.course_name = new String[COURSE_NUM];
		this.day = new String[COURSE_NUM];
		this.course_start_time = new int[COURSE_NUM];
		this.location = new String[COURSE_NUM];
	}

	/*   set method   */
	public void setCourse_name(String course_name, int n) {
		this.course_name[n] = course_name;
	}
	public void setday(String day, int n) {
		this.day[n] = day;
	}
	public void setStart_time(int start_time, int n) {
		this.course_start_time[n] = start_time;
	}
	public void setLocation(String location, int n) {
		this.location[n] = location;
	}
	
	/*   get method   */
	public String getCourse_name(int n) {
		return course_name[n];
	}
	public String getday(int n) {
		return day[n];
	}
	public int getStart_time(int n) {
		return course_start_time[n];
	}
	public String getLocation(int n) {
		return location[n];
	}
}
