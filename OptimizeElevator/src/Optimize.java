import java.util.ArrayList;

public class Optimize {
	final int OPEN_CLOSE_DOOR_TIME = 17;	//단위: 초(s)
	final float[] BETWEEN_FLOOR_TIME = {0, 6, 8, 8.75f, 9.5f, 10.25f, 11.0f, 11.75f, 12.5f};
	final int WORST_CASE_TOTAL_TIME = 940;
	final int[] floor = {-6,-5,-3,-2,-1,3,3,4,4,5,5,6,6,7,7,8,8,9,9};	//excel의 column name(floor), -6 => B6층, 6 => 지상 6층
	final int ELEVATOR_NUM = 10;

	private CourseInfo courseinfo;
	private float x = 700.0f;
	private float y = 0.2f;
	private ArrayList<Integer>[][] magicfloor;	//x,y 값에 따라 결정된 magic floor(요알별, 시간대별)

	/*
	* each running time calculate
	* 1,2,3,4 : A area
	* 5,6,7,8 : B area
	* 9, 10 : C area
	* 1,2: underground
	* 5,6: odd floor
	* 7,8 even floor
	*/
	private float[][][] Elevator;

	Optimize(CourseInfo courseinfo){
		this.courseinfo = courseinfo;
		this.Elevator = new float[courseinfo.DAY_NUM][courseinfo.ROW_NUM][this.ELEVATOR_NUM];

		// initialize Elevator
		this.magicfloor = new ArrayList[courseinfo.DAY_NUM][courseinfo.ROW_NUM];
		for(int i=0; i<courseinfo.DAY_NUM; i++) {
			for(int j=0; j<courseinfo.ROW_NUM; j++)
				this.magicfloor[i][j] = new ArrayList<Integer>();
		}
	}

	//magic floor calculate
	public void CalMagicFloor() {
		int pre_floor=0;
		for(int day=0; day<courseinfo.DAY_NUM; day++) {
			for(int row=0; row<courseinfo.ROW_NUM; row++) {
				float floating_population = courseinfo.getFloatingPopulation(day, row);
				//������ x�� �̻����� ���� �ð���
				if(floating_population >= x) {
					for(int column=0; column < courseinfo.COLUMN_NUM; column++) {
						//�ð��� �߿��� ������ y% �̻����� ���� �� ������ magic time ����
						if(floating_population * y <= courseinfo.getPeopleNum(day, row, column)) {
							magicfloor[day][row].add(floor[column]);
						}
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
		downsideElevator_Aarea(0);	// 1 Elevator
		downsideElevator_Aarea(1);	// 2 Elevator
		upsideElevator_Aarea(2);	// 3 Elevator
		upsideElevator_Aarea(3);	// 4 Elevator

		// B area Elevator
		oddElevator_Barea(4);
		oddElevator_Barea(5);
		evenElevator_Barea(6);
		evenElevator_Barea(7);

		//C area Elevator
		Elevator_Carea(8);
		Elevator_Carea(9);
	}

	public void upsideElevator_Aarea(int number){
		int day, row, index;
		int floor;
		int pre_floor=0;
		int size;
		int flag = 0;

		for(day=0; day<courseinfo.DAY_NUM; day++){
			for(row =0; row<courseinfo.ROW_NUM; row++){
				size = this.magicfloor[day][row].size();
				for(index =0; index < size; index++){
					floor = this.magicfloor[day][row].get(index);	//magic floor

					//if underground floor
					if(floor < 0){
						floor = Math.abs(floor);
						this.Elevator[day][row][number] += this.OPEN_CLOSE_DOOR_TIME + this.BETWEEN_FLOOR_TIME[Math.abs(floor-pre_floor)];	//upside
						pre_floor = floor;
						flag = -1;
					}
					else{
						//only up floor
						if(flag == 0){
							this.Elevator[day][row][number] += this.OPEN_CLOSE_DOOR_TIME + this.BETWEEN_FLOOR_TIME[floor - pre_floor - 1];	//upside
							pre_floor = floor -1;
						}
						//up + down floor
						else{
							this.Elevator[day][row][number] += this.OPEN_CLOSE_DOOR_TIME + this.BETWEEN_FLOOR_TIME[floor + pre_floor - 1];	//upside
							pre_floor = floor -1;
						}
					}
				}
				if(flag == 0)
					this.Elevator[day][row][number] += this.OPEN_CLOSE_DOOR_TIME + this.BETWEEN_FLOOR_TIME[this.magicfloor[day][row].get(size-1) - 1];	//downside
				else
					this.Elevator[day][row][number] += this.OPEN_CLOSE_DOOR_TIME + this.BETWEEN_FLOOR_TIME[this.magicfloor[day][row].get(0)];	//downside
			}
		}
	}

	public void downsideElevator_Aarea(int number){
		int day, row, index;
		int floor;
		int pre_floor=0;
		int size;
		int flag = 0;

		for(day=0; day<courseinfo.DAY_NUM; day++){
			for(row =0; row<courseinfo.ROW_NUM; row++){
				size = this.magicfloor[day][row].size();
				for(index =0; index < size; index++){
					floor = this.magicfloor[day][row].get(index);	//magic floor
					this.Elevator[day][row][number] += this.OPEN_CLOSE_DOOR_TIME + this.BETWEEN_FLOOR_TIME[5];	//B5 floor
					this.Elevator[day][row][number] += this.OPEN_CLOSE_DOOR_TIME + this.BETWEEN_FLOOR_TIME[2];	//B5-> B3 floor
				}
			}
		}
	}

	public void oddElevator_Barea(int number){
		
	}

	public void evenElevator_Barea(int number){

	}

	public void Elevator_Carea(int number){

	}
}
