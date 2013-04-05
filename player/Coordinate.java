package player;
public class Coordinate{
  private int x;
  private int y;
  
  /**
  * Coordinate(int x, int y) is the two-parameter constructor for a Coordinate object
  * @param a: the x coordinate of the Coordinate object
  * @param b: the y coordinate of the Coordinate object
  **/
  public Coordinate(int a, int b){
    x = a;
    y = b;
  }

  /**
  *  Coordinate() is the no parameter constructor for a Coordinate object
  *  Creates a new Coordinate object and sets both x and y to Integer.MAX_VALUE to represent
  *  a point that does not exist
  **/
  public Coordinate(){
    x = Integer.MAX_VALUE;
    y = Integer.MAX_VALUE;
  }
  /**
  *  getX() is a getter for the x instance variable. 
  *  return the x coordinate of a Coordinate object
  **/
  public int getX(){
    return this.x;
  }

  /**
  *  getY() is a getter for the y instance variable. 
  *  return the y coordinate of a Coordinate object
  **/
  public int getY(){
    return this.y;
  }

  /**
  *  equals() checks if two Coordinate objects are equal. This method returns true if both Coordinates
  *  have the same x values and the same y values
  *  @param other: the Coordinate being compared to
  **/
  public boolean equals(Coordinate other){
    return x == other.getX() && y == other.getY();
  }

}
