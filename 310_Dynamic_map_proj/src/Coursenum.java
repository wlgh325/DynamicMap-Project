
public class Coursenum {
	static final int ROW_NUM = 23;	// row: 0교시 ~ 11교시
	static final int FLOOR_NUM = 9;	//column: 1~ 9층
	static final int DAY_NUM = 5;

	/*
	 * people_num
	 * row: 0교시~11교시
	 * column: 1층 ~ 9층
	 */

	/*
	 * floating_population: 각 날짜별 시간대별 유동인구
	 */

	private float[][][] people_num;
	private float[][] floating_population;

	Coursenum(){
		this.people_num = new float[DAY_NUM][ROW_NUM][FLOOR_NUM];
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
