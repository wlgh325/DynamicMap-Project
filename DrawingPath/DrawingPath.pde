PImage floor;
String path="-311>-3N6>-3N5>-3E2>7E3>7N15>7N9>7N8>7N7>7N6>705";
String magicFloor="-5,-3,9";

Divider divider=new Divider();
Finder finder=new Finder();
ImageLoader loader=new ImageLoader();
MagicFloor text=new MagicFloor();

int px=-1, py=-1;
int outIdx=0, inIdx=0;
int speed=1;

String[][] pathDivided=divider.DividePath(path);


void setup() {
  size(500, 800);
  floor=loadImage(loader.LoadImgPath(finder.FindFloor(pathDivided[outIdx][0])));
  image(floor, 0, 0, width, height);
}

void mousePressed() {
  inIdx=0; //start node on the next floor
  if (outIdx==pathDivided.length-1) outIdx=-1;
  floor=loadImage(loader.LoadImgPath(finder.FindFloor(pathDivided[++outIdx][0])));
  image(floor, 0, 0, width, height);
}


void draw() {
  if (inIdx>=(pathDivided[outIdx].length-1)) inIdx=0;
  if (inIdx==0)  stroke(random(255), 50, 50);
  text.HighlightMF(magicFloor);
  strokeWeight(3);
  line(finder.FindLocX(pathDivided[outIdx][inIdx]), finder.FindLocY(pathDivided[outIdx][inIdx])
    , finder.FindLocX(pathDivided[outIdx][inIdx+1]), finder.FindLocY(pathDivided[outIdx][inIdx+1]));
  inIdx++;
  delay(1000/speed);
}

void keyPressed() {
  if (key==CODED) {
    if (keyCode==RIGHT) {
      speed++;
    }
    if (keyCode==LEFT) {
      speed--;
      if (speed==0) speed=1;
    }
  }
}
