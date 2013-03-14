public class Coordinate{
  private int x;
  private int y;
  
  /**
  * Coordinate() is the two-parameter constructor for a Coordinate object
  * @param x is the x coordinate of the Coordinate object
  * @param y is the y coordinate of the Coordinate object
  **/
  public Coordinate(int x, int y){
    this.x = x;
    this.y = y;
  }

  /**
  * getX() is a getter for the x instance variable. It returns the x coordinate of a Coordinate object
  **/
  public int getX(){
    return this.x;
  }

  /**
  * getY() is a getter for the y instance variable. It returns the y coordinate of a Coordinate object
  **/
  public int getY(){
    return this.y;
  }
}
