public class CourseInfo {
	
	static final int COURSE_NUM = 100;	//
	private String[] course_name;
	private String[] course_time;
	private String[] location;
	private String[] code;
	private String[] class_num;
	private String[] total_time;
	
	/* Constructor */
	CourseInfo() {
		this.course_name = new String[COURSE_NUM];
		this.course_time = new String[COURSE_NUM];
		this.location = new String[COURSE_NUM];
		this.code = new String[COURSE_NUM];
		this.class_num = new String[COURSE_NUM];
		this.total_time = new String[COURSE_NUM];
	}

	/*   set method   */
	public void setCourse_name(String course_name, int n) {
		this.course_name[n] = course_name;
	}
	
	public void setCoursetime(String start_time, int n) {
		this.course_time[n] = start_time;
	}
	
	public void setLocation(String location, int n) {
		this.location[n] = location;
	}
	
	public void setCode(String code, int n) {
		this.code[n] = code;
	}
	
	public void setClassnum(String class_num, int n) {
		this.class_num[n] = class_num;
	}
	
	public void setTotalTime(String total_time, int n) {
		this.total_time[n] = total_time;
	}
	
	/*   get method   */
	public String[] getCourse_name() {
		return course_name;
	}
	
	public String[] getCoursetime() {
		return course_time;
	}
	public String[] getLocation() {
		return location;
	}
	
	public String[] getCode() {
		return code;
	}
	
	public String[] getClassnum(){
		return class_num;
	}
	public String[] getTotalTime() {
		return total_time;
	}
}