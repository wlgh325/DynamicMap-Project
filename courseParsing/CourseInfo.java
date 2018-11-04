package application;

public class CourseInfo {
	static final int COURSE_NUM = 909;	//강의 개수
	private String[] courseName;	//과목 이름
	private int[] grade;	//학년
	private String[] location;	//강의실 위치
	private String[] courseDate;	//강의 날짜, 시간
	private float[] personNum;	//수강인원
	
	CourseInfo(){
		courseName = new String[COURSE_NUM];
		grade = new int[COURSE_NUM];
		location = new String[COURSE_NUM];
		courseDate = new String[COURSE_NUM];
		personNum = new float[COURSE_NUM];
	}
	
	/* 
	 * set method 
	 * */
	public void setCourseName(String courseName, int n) {
		this.courseName[n] = courseName;
	}
	
	public void setGrade(int grade, int n) {
		this.grade[n] = grade;
	}
	
	public void setLocation(String location, int n) {
		this.location[n] = location;
	}
	
	public void setCourseDate(String courseDate, int n) {
		this.courseDate[n] = courseDate;
	}
	
	public void setPersonNum(float personNum, int n) {
		this.personNum[n] = personNum;
	}
	
	
	/* 
	 * get method 
	 * */
	
	public String getCourseName(int n) {
		return courseName[n];
	}
	
	public int getGrade(int n) {
		return grade[n];
	}
	
	public String getLocation(int n) {
		return location[n];
	}
	
	public String getCourseDate(int n) {
		return courseDate[n];
	}
	
	public float getPersonNum(int n) {
		return personNum[n];
	}
}
