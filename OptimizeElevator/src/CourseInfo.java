
public class CourseInfo {
	final int ROW_NUM = 21;	// row: 0교시 ~11교시
	final int COLUMN_NUM = 19;	//column: B6 ~ 9층
	final int DAY_NUM = 5;
	
	/*
	 * people_num
	 * 각 층별 시간대별 유동인원 정보
	 * row: 0교시~11교시
	 * column: B6 ~ 9층
	 */
	
	/*
	 * floating_population: 각 층별 유동인원의 합
	 */
	
	private float[][][] people_num;
	private float[][] floating_population;
	
	CourseInfo(){
		this.people_num = new float[DAY_NUM][ROW_NUM][COLUMN_NUM];
		this.floating_population = new float[DAY_NUM][ROW_NUM];
	}
	
	/* set method */
	public void setPeopleNum(int day, int row, int column, float num) {
		people_num[day][row][column] = num;
	}
	
	public void setFloating_population(int day, int time, float value) {
		floating_population[day][time] = value; 
	}
	
	/* get method */
	public float getPeopleNum(int day, int row, int column){
		return this.people_num[day][row][column];
	}
	
	public float getFloatingPopulation(int day, int time) {
		return this.floating_population[day][time];
	}
}
