PImage floor;
String path="701>7N10>7N11>7N7>7N8>7N4>7E1>9E1>9N4>921";
Divider divider=new Divider();
Finder finder=new Finder();
ImageLoader loader=new ImageLoader();

int px, py;
int outIdx=0, inIdx=0;
int speed=1;

String[][] pathDivided=divider.DividePath(path);


void setup() {
  size(500, 800);
  //floor=loadImage(loader.LoadImgPath(finder.FindFloor(pathDivided[0][0])));
  //image(floor, 0, 0, width, height);
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
  //fill(random(255),50,50);
  //noStroke();
  if(inIdx==0)  stroke(random(255), 50, 50);
  strokeWeight(3);
  line(finder.FindLocX(pathDivided[outIdx][inIdx]), finder.FindLocY(pathDivided[outIdx][inIdx])
    , finder.FindLocX(pathDivided[outIdx][inIdx+1]), finder.FindLocY(pathDivided[outIdx][inIdx+1]));
  inIdx++;
  delay(1000/speed);
}

void keyPressed(){
  if(key==CODED){
    if(keyCode==RIGHT){
      speed++;
    }
    if(keyCode==LEFT){
      speed--;
      if(speed==0) speed=1;
    }
  }
}
