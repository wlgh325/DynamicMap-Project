import java.util.ArrayList;

public class Optimize {

	/*
	* elevator running time info constant
	*/
	final int OPEN_CLOSE_DOOR_TIME = 17;	//단위: 초(s)
	final float[] BETWEEN_FLOOR_TIME = {0, 6, 8, 8.75f, 9.5f, 10.25f, 11.0f, 11.75f, 12.5f, 13.25f, 14.0f, 14.75f, 15.5f, 16.25f, 17.0f, 17.75f};
	final int WORST_CASE_TOTAL_PEOPLE_NUM = 940;
	final int[] floor = {3,4,5,6,7,8,9};	//excel의 column name(floor), -6 => B6층, 6 => 지상 6층
	final int ELEVATOR_NUM = 10;
	final float DOWNSIDE_ELEVATOR_1_NORMAL_TIME = 355.0f;
	final float DOWNSIDE_ELEVATOR_2_NORMAL_TIME = 385.0f;
	final float UPSIDE_ELEVATOR_NORMAL_TIME = 200.0f;
	final float ODD_ELEVATOR_NORMAL_TIME = 620.0f;
	final float EVEN_ELEVATOR_NORMAL_TIME = 350.0f;
	final float B_AREA_ELEVATOR_1_NORMAL_TIME = 160.0f;
	final float B_AREA_ELEVATOR_2_NORMAL_TIME = 165.0f;
	final float C_AREA_ELEVATOR_NORMAL_TIME = 290.0f;
	final float MAGIC_TIME_SECONDS = 1200.0f;


	/*
	* normal elevator running info constant
	* not magic time running info
	*/

	// A area elevator running info constant
	final int[] DOWNSIDE_ELEVATOR_A1_RUNNING_INFO = {-6,-5,-4,-3,1,4,6,8};
	final int[] DOWNSIDE_ELEVATOR_A2_RUNNING_INFO = {-6,-5,-4,-3,1,3,5,7,9};
	final int[] UPSIDE_ELEVATOR_A3_RUNNING_INFO = {1,2,4,6,8};
	final int[] UPSIDE_ELEVATOR_A4_RUNNING_INFO = {1,3,5,7,9};

	// B area elevator running info constant
	final int[] UPSIDE_ELEVATOR_B1_RUNNING_INFO = {1,5,8,11};
	final int[] ALL_STOP_ELEVATOR_B2_RUNNING_INFO = {-3,-2,-1,1,2,3,4,5,6,7,8,9,10,11,12};
	final int[] UPSIDE_ELEVATOR_B3_RUNNING_INFO = {1,4,7,10};
	final int[] MULTIPLES_OF_3_ELEVATOR_B4_RUNNING_INFO = {-3,-2,-1,1,3,6,9,12};



	// C area elevator running info constant
	final int[] ODD_ELEVATOR_C1_RUNNING_INFO = {-3,-2,-1,1,3,5,7,9};
	final int[] EVEN_ELEVATOR_C2_RUNNING_INFO = {-3,-2,-1,1,4,6,8};


	// elevator each max boarding num
	final int A_AREA_ELEVATOR_PEOPLE_NUM = 15;
	final int B_AREA_ELEVATOR_PEOPLE_NUM = 20;
	final int C_AREA_ELEVATOR_PEOPLE_NUM = 20;

	private CourseInfo courseinfo;
	private float x;
	private float y;
	private float z;
	private ArrayList<Integer>[][] magicfloor;	//x,y 값에 따라 결정된 magic floor(요알별, 시간대별)
	private int avgPeoplenum;	//모든 인원수의 평균
	private ArrayList<Integer>[][][] runningElevator;

	/*
	* each running time calculate
	* 1,2,3,4 : A area
	* 5,6,7,8 : B area
	* 9, 10 : C area
	* 1,2: underground
	* 5,6: odd floor
	* 7,8 even floor
	*/
	private float[][][] Elevator;	//요일별, 시간별 각 엘리베이터별 운행시간

	private float[][] totalPeopleNum;	//요일별, 시간별 1-10회기 운행시간의 총합 * 운행정원

	Optimize(CourseInfo courseinfo){
		this.courseinfo = courseinfo;
		this.Elevator = new float[courseinfo.DAY_NUM][courseinfo.ROW_NUM][this.ELEVATOR_NUM];
		this.totalPeopleNum = new float[courseinfo.DAY_NUM][courseinfo.ROW_NUM];

		this.runningElevator = new ArrayList[courseinfo.DAY_NUM][courseinfo.ROW_NUM][this.ELEVATOR_NUM];
		for(int i=0; i<courseinfo.DAY_NUM; i++)
			for(int j=0; j<courseinfo.ROW_NUM; j++)
				for(int k=0; k<ELEVATOR_NUM; k++)
					this.runningElevator[i][j][k] = new ArrayList<Integer>();

		// initialize Elevator
		this.magicfloor = new ArrayList[courseinfo.DAY_NUM][courseinfo.ROW_NUM];
		for(int i=0; i<courseinfo.DAY_NUM; i++) {
			for(int j=0; j<courseinfo.ROW_NUM; j++){
				this.magicfloor[i][j] = new ArrayList<Integer>();
				this.totalPeopleNum[i][j] = 0;
			}

		}
	}


	public void setXYZ(float x, float y, float z){
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public void OptimizeElevator(){
			CalMagicFloor();
			Elevator_RunningTime();
			roundRunningNum();
			Elevator_ruuningInfo();
	}

	//magic floor calculate
	public void CalMagicFloor() {

		for(int day=0; day<courseinfo.DAY_NUM; day++) {
			for(int row=0; row<courseinfo.ROW_NUM; row++) {
				float floating_population = courseinfo.getFloatingPopulation(day, row);
				//  total floating populaton is bigger than x
				if(floating_population >= x) {
					for(int column=0; column < 7; column++) {
						// peoplenum is bigger than y%
						if(floating_population * y <= courseinfo.getPeopleNum(day, row, column)) {
							magicfloor[day][row].add(floor[column]);
						}
					}
				}
				// total floating populaton is bigger than x
				// but floating polulation is bigger than y
				else{
					for(int column=0; column < 7; column++){
						if(courseinfo.getPeopleNum(day, row, column) >= z )
							magicfloor[day][row].add(floor[column]);
					}
				}
			}
		}
	}

	// excel file print
	public void Fileprint() {
		for(int day=0; day<courseinfo.DAY_NUM; day++) {
			System.out.println(day);
			for(int row=0; row<courseinfo.ROW_NUM; row++) {
				System.out.print("Row: " + row);
				for(int column=0; column<courseinfo.COLUMN_NUM; column++) {
					System.out.print("\t"+courseinfo.getPeopleNum(day, row, column) + " ");
				}
				System.out.println();
			}
			System.out.print("\n\n");
		}
	}


	// each day, time type : magic floor print
	public void MagicFloorPrint() {
		for(int day=0; day<courseinfo.DAY_NUM; day++) {
			System.out.println(day);
			for(int row=0; row<courseinfo.ROW_NUM; row++) {
				System.out.print("Row: " + row);
				for(int index=0; index<magicfloor[day][row].size(); index++) {
					System.out.print("\t"+this.magicfloor[day][row].get(index) + " ");
				}
				System.out.println();
			}
			System.out.print("\n\n");
		}
	}

	public void Elevator_RunningTime(){

		// A area Elevator
		downsideElevator_Aarea(0, DOWNSIDE_ELEVATOR_1_NORMAL_TIME);
		downsideElevator_Aarea(1, DOWNSIDE_ELEVATOR_2_NORMAL_TIME);
		upsideElevator_Aarea(2);	// 3 Elevator
		upsideElevator_Aarea(3);	// 4 Elevator

		// B area Elevator
		oddElevator_Barea(4);	//odd, upper floor
		oddElevator_Barea2(5, ODD_ELEVATOR_NORMAL_TIME);	//odd, underground floor	//all floor 620s
		evenElevator_Barea(6);	//even, uppper floor
		evenElevator_Barea2(7, EVEN_ELEVATOR_NORMAL_TIME);	//even, underground floor	//350s

		//C area Elevator
		Elevator_Carea(8);
		Elevator_Carea(9);

	}

	public void Elevator_ruuningInfo(){
		//running elevator info
		runningElevator_InfoNormal(0, DOWNSIDE_ELEVATOR_A1_RUNNING_INFO);
		runningElevator_InfoNormal(1,DOWNSIDE_ELEVATOR_A2_RUNNING_INFO);

		runningElevator_InfoMagicTime(2, UPSIDE_ELEVATOR_A3_RUNNING_INFO);
		runningElevator_InfoMagicTime(3, UPSIDE_ELEVATOR_A4_RUNNING_INFO);

		runningElevator_InfoNormal(4, UPSIDE_ELEVATOR_B1_RUNNING_INFO);
		runningElevator_InfoMagicTime(5, ALL_STOP_ELEVATOR_B2_RUNNING_INFO);
		runningElevator_InfoNormal(6, UPSIDE_ELEVATOR_B3_RUNNING_INFO);
		runningElevator_InfoMagicTime(7, MULTIPLES_OF_3_ELEVATOR_B4_RUNNING_INFO);

		runningElevator_InfoNormal(8, ODD_ELEVATOR_C1_RUNNING_INFO);
		runningElevator_InfoNormal(9,EVEN_ELEVATOR_C2_RUNNING_INFO);
	}

	public void upsideElevator_Aarea(int number){
		int day, row, index;
		int floor;
		int pre_floor=1;
		int size;

		/*
		* 1 -> magic floor -> 1
		*/
		for(day=0; day<courseinfo.DAY_NUM; day++){
			for(row =0; row<courseinfo.ROW_NUM; row++){
				size = this.magicfloor[day][row].size();
				for(index =0, pre_floor=1; index < size; index++){
					floor = this.magicfloor[day][row].get(index);	//magic floor
					this.Elevator[day][row][number] += this.OPEN_CLOSE_DOOR_TIME + this.BETWEEN_FLOOR_TIME[floor - pre_floor];	//upside
					pre_floor = floor;
				}
				if(size != 0)
					this.Elevator[day][row][number] += this.OPEN_CLOSE_DOOR_TIME + this.BETWEEN_FLOOR_TIME[this.magicfloor[day][row].get(size-1) - 1];	//downside
				else
					this.Elevator[day][row][number] = UPSIDE_ELEVATOR_NORMAL_TIME;
				pre_floor = 1;
			}
		}
	}

	public void downsideElevator_Aarea(int number, float time){
		int day, row, index;
		int size;

		for(day=0; day<courseinfo.DAY_NUM; day++){
			for(row =0; row<courseinfo.ROW_NUM; row++){
				this.Elevator[day][row][number] = time;
			}
		}
	}

	//only upside odd elevator
	public void oddElevator_Barea(int number){
		int day, row, index;
		int floor;
		int pre_floor=1;
		int size;
		ArrayList<Integer> odd_floor = new ArrayList<Integer>();

		//initialize odd_floor
		odd_floor.add(1);
		odd_floor.add(3);
		odd_floor.add(5);
		odd_floor.add(7);
		odd_floor.add(9);
		odd_floor.add(11);

		for(day=0; day<courseinfo.DAY_NUM; day++){
			for(row =0; row<courseinfo.ROW_NUM; row++){
				size = this.magicfloor[day][row].size();
				for(index =0; index < size; index++){
					floor = this.magicfloor[day][row].get(index);	//magic floor

					for(int temp=0; temp < odd_floor.size(); temp ++){
						if(odd_floor.get(temp) == floor){
							odd_floor.remove(temp);
							break;
						}	//except magic floor
					}
				}
				for(int temp = 0; temp < odd_floor.size(); temp++){
					this.Elevator[day][row][number] += this.OPEN_CLOSE_DOOR_TIME + this.BETWEEN_FLOOR_TIME[odd_floor.get(temp) - pre_floor];
					pre_floor = odd_floor.get(temp);
				}
				if(size != 0)
					this.Elevator[day][row][number] += this.OPEN_CLOSE_DOOR_TIME + this.BETWEEN_FLOOR_TIME[odd_floor.get(odd_floor.size() - 1) - 1];
				else
					this.Elevator[day][row][number] = B_AREA_ELEVATOR_1_NORMAL_TIME;
				pre_floor=1;
			}
		}
	}

	//down & upside odd elevator
	public void oddElevator_Barea2(int number, float time){
		int day, row, index;
		int size;

		for(day=0; day<courseinfo.DAY_NUM; day++){
			for(row =0; row<courseinfo.ROW_NUM; row++){
				this.Elevator[day][row][number] = time;
			}
		}
	}


	public void evenElevator_Barea(int number){
		int day, row, index;
		int floor;
		int pre_floor=0;
		int size;
		ArrayList<Integer> even_floor = new ArrayList<Integer>();

		even_floor.add(1);
		even_floor.add(4);
		even_floor.add(6);
		even_floor.add(8);
		even_floor.add(10);
		even_floor.add(12);

		for(day=0; day<courseinfo.DAY_NUM; day++){
			for(row =0; row<courseinfo.ROW_NUM; row++){
				size = this.magicfloor[day][row].size();
				for(index =0; index < size; index++){
					floor = this.magicfloor[day][row].get(index);	//magic floor

					for(int temp=0; temp < even_floor.size(); temp ++){
						if(even_floor.get(temp) == floor){
							even_floor.remove(temp);
							break;
						}	//except magic floor
					}
				}
				for(int temp = 0; temp < even_floor.size(); temp++){
					this.Elevator[day][row][number] += this.OPEN_CLOSE_DOOR_TIME + this.BETWEEN_FLOOR_TIME[even_floor.get(temp) - pre_floor];
					pre_floor = even_floor.get(temp);
				}
				if(size != 0)
					this.Elevator[day][row][number] += this.OPEN_CLOSE_DOOR_TIME + this.BETWEEN_FLOOR_TIME[even_floor.get(even_floor.size() - 1) - 1];
				else
					this.Elevator[day][row][number] = B_AREA_ELEVATOR_2_NORMAL_TIME;
				pre_floor = 1;
			}
		}
	}

	public void evenElevator_Barea2(int number, float time){
		int day, row, index;
		int size;

		for(day=0; day<courseinfo.DAY_NUM; day++){
			for(row =0; row<courseinfo.ROW_NUM; row++){
				this.Elevator[day][row][number]  = time;
			}
		}
	}

	/*
	*B3 -> 1 -> magic floor -> B3
	*/
	public void Elevator_Carea(int number){
		int day, row, index;
		int floor;
		int pre_floor=1;
		int size;

		for(day=0; day<courseinfo.DAY_NUM; day++){
			for(row =0; row<courseinfo.ROW_NUM; row++){
				size = this.magicfloor[day][row].size();
				this.Elevator[day][row][number] += this.OPEN_CLOSE_DOOR_TIME + this.BETWEEN_FLOOR_TIME[3];	//B3 -> 1 floor
				for(index =0; index < size; index++){
					floor = this.magicfloor[day][row].get(index);	//magic floor
					this.Elevator[day][row][number] += this.OPEN_CLOSE_DOOR_TIME + this.BETWEEN_FLOOR_TIME[floor - pre_floor];
					pre_floor = floor;
				}
				if(size != 0)
					this.Elevator[day][row][number] += this.OPEN_CLOSE_DOOR_TIME + this.BETWEEN_FLOOR_TIME[this.magicfloor[day][row].get(size-1) - 1];
				else
					this.Elevator[day][row][number] = C_AREA_ELEVATOR_NORMAL_TIME;
				pre_floor=1;
			}
		}
	}

	public void printRunningTime(){
		for(int day=0; day<courseinfo.DAY_NUM; day++) {
			System.out.println(day);
			for(int row=0; row<courseinfo.ROW_NUM; row++) {
				System.out.print("Row: " + row);
				for(int index=0; index< ELEVATOR_NUM; index++) {
					System.out.print("\t"+ this.Elevator[day][row][index] + " ");
				}
				System.out.println();
			}
			System.out.print("\n\n");
		}
	}

	public void roundRunningNum(){

		for(int day=0; day< courseinfo.DAY_NUM; day++)
			for(int row=0; row < courseinfo.ROW_NUM; row++){
				for(int index =0; index < 4; index++)
					this.Elevator[day][row][index] = (int) (Math.ceil(MAGIC_TIME_SECONDS / this.Elevator[day][row][index]) ) * A_AREA_ELEVATOR_PEOPLE_NUM;
				for(int index=4; index < 8; index++)
					this.Elevator[day][row][index] = (int) (Math.ceil(MAGIC_TIME_SECONDS / this.Elevator[day][row][index]) ) * B_AREA_ELEVATOR_PEOPLE_NUM;
				for(int index=8; index <ELEVATOR_NUM; index++)
					this.Elevator[day][row][index] = (int) (Math.ceil(MAGIC_TIME_SECONDS / this.Elevator[day][row][index]) ) * C_AREA_ELEVATOR_PEOPLE_NUM;

			}
		for(int day=0; day < courseinfo.DAY_NUM; day++)
			for(int row =0; row < courseinfo.ROW_NUM; row++)
				for(int index = 0; index < ELEVATOR_NUM; index++)
					this.totalPeopleNum[day][row] += this.Elevator[day][row][index];

		//printTotalPeopleNum();
		avgPeoplenum();
	}

	public void printTotalPeopleNum(){
		for(int day=0; day <courseinfo.DAY_NUM; day++){
			System.out.println(day);
			for(int row =0; row < courseinfo.ROW_NUM; row++)
				System.out.println("row : " + row + " " + this.totalPeopleNum[day][row]);
			System.out.println();
		}
	}

	public void avgPeoplenum(){
		float temp=0;

		for(int day=0; day< courseinfo.DAY_NUM; day++)
			for(int row=0; row < courseinfo.ROW_NUM; row++)
					temp += this.totalPeopleNum[day][row];

		this.avgPeoplenum = (int)Math.ceil( temp / (courseinfo.DAY_NUM * courseinfo.ROW_NUM) );
		//System.out.println(this.avgPeoplenum + "명");
	}


	public void runningElevator_InfoNormal(int elevator_num, int[] elevator_runninginfo){

		for(int day=0; day< courseinfo.DAY_NUM; day++){
			for(int row=0; row < courseinfo.ROW_NUM; row++){
				for(int index = 0; index < elevator_runninginfo.length; index++)
						this.runningElevator[day][row][elevator_num].add(elevator_runninginfo[index]);
			}
		}
	}


	public void runningElevator_InfoMagicTime(int elevator_num, int[] elevator_runninginfo){
		for(int day=0; day< courseinfo.DAY_NUM; day++){
			for(int row=0; row < courseinfo.ROW_NUM; row++){
				//magic floor existing
				if(!magicfloor[day][row].isEmpty()){
					for(int index = 0; index < magicfloor[day][row].size(); index++)
						this.runningElevator[day][row][elevator_num].add(magicfloor[day][row].get(index));
				}
				else{
					for(int index =0; index < elevator_runninginfo.length; index++)
						this.runningElevator[day][row][elevator_num].add(elevator_runninginfo[index]);
				}
			}
		}
	}

	public ArrayList<Integer>[][][] getRunningElevatorInfo(){
		return this.runningElevator;
	}

}
