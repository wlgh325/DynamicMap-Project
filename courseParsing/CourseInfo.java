package application;

public class CourseInfo {
	static final int COURSE_NUM = 909;	//���� ����
	private String[] courseName;	//���� �̸�
	private int[] grade;	//�г�
	private String[] location;	//���ǽ� ��ġ
	private String[] courseDate;	//���� ��¥, �ð�
	private float[] personNum;	//�����ο�
	
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
