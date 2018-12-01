import java.util.ArrayList;

public class Optimize {
	final int OPEN_CLOSE_DOOR_TIME = 17;	//단위: 초(s)
	final float[] BETWEEN_FLOOR_TIME = {6, 8, 8.75f, 9.5f, 10.25f, 11.0f, 11.75f, 12.5f};
	final int WORST_CASE_TOTAL_TIME = 940;
	final int[] diff_floor = {6,5,3,2,1,2,3,4,5,6,7,8};
	
	private CourseInfo courseinfo;
	private float x = 700.0f;
	private float y = 0.2f; 
	private ArrayList<Integer> magicfloor;	//x,y 값에 따라 결정된 magic floor
	
	Optimize(CourseInfo courseinfo){
		this.courseinfo = courseinfo;
		
		magicfloor = new ArrayList<Integer>();
	}
	
	
	public void CalMagicFloor() {
		int pre_floor=0;
		for(int day=0; day<courseinfo.ROW_NUM; day++) {
			for(int row=0; row<courseinfo.COLUMN_NUM; row++) {
				float floating_population = courseinfo.getFloatingPopulation(day, row);
				//비율이 x명 이상으로 많은 시간대
				if(floating_population >= x) {
					for(int column=0; column < courseinfo.COLUMN_NUM; column++) {
						//시간대 중에서 비율이 y% 이상으로 많은 층 골라서 magic time 시행
						if(floating_population * y <= courseinfo.getPeopleNum(day, row, column)) {
		
						}
							 
					}
				}
			}
		}
			
					
					
	}
	
	public void print() {
		for(int k=0; k<courseinfo.DAY_NUM; k++) {
			System.out.println(k);
			for(int i=0; i<courseinfo.ROW_NUM; i++) {
				System.out.print("Row: " + i);
				for(int j=0; j<courseinfo.COLUMN_NUM; j++) {
					System.out.print("\t"+courseinfo.getPeopleNum(k, i, j) + " ");
				}
				System.out.println();
			}
			System.out.print("\n\n");
		}
	}
}
