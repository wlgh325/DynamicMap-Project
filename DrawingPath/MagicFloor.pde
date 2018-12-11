public class MagicFloor {
  PFont font;
  final int MAX_FLOOR=15;

  public void HighlightMF(String floors) {
    String[] MagicFloors=floors.split(",");
    int arrIdx=0, floor = -6;
    int i=0;
    font=createFont("DS-DIGIT.TTF", 33);
    textFont(font);

    while (arrIdx<MagicFloors.length && floor<=9) {
      if (MagicFloors[arrIdx].matches(Integer.toString(floor))) {
        fill(255, 0, 0); 
        arrIdx++;
        if (floor<0) {
          text("B"+Integer.toString(floor).charAt(1), 25+40*i, 775);
        } else if (floor==0) {
          floor++; 
          i++;
          continue;
        } else {
          text(floor, 235+25*(i-6), 775);
        }
      } else {
        fill(0);
        if (floor<0) {
          text("B"+Integer.toString(floor).charAt(1), 25+40*i, 775);
        } else if (floor==0) {

          floor++; 
          i++;
          continue;
        } else {
          text(floor, 235+25*(i-6), 775);
        }
      }

      floor++; 
      i++;
    }
  }
}
