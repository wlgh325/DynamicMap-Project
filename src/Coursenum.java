
public class Coursenum {
	final int ROW_NUM = 21;	// row: 0���� ~11����
	final int COLUMN_NUM = 9;	//column: 3~ 9��
	final int DAY_NUM = 5;

	/*
	 * people_num
	 * �� ���� �ð��뺰 �����ο� ����
	 * row: 0����~11����
	 * column: B6 ~ 9��
	 */

	/*
	 * floating_population: �� ���� �����ο��� ��
	 */

	private float[][][] people_num;
	private float[][] floating_population;

	Coursenum(){
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
