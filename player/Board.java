package player;
import list.*;


public class Board {
  public static final int EMPTY = 2;
  public static final int BLACK = 0;
  public static final int WHITE = 1;
  public static final int MAXPIECES = 10;
  private int[][] board;
  private int blackpieces, whitepieces;
  public static final int HEIGHT = 8;
  public static final int WIDTH = 8;
  
  public Board(){
    board = new int[HEIGHT][WIDTH];
    blackpieces = 0;
    whitepieces = 0;
    for (int i = 0; i < HEIGHT; i++)
      for (int j = 0; j < WIDTH; j++)
      {
        board[i][j] = EMPTY;
      }
  }
  
  public int getPiece(int x, int y){
    return board[y][x];
  }
  
  private void setPiece(int x, int y, int player)
  {
    board[y][x] = player;
  }
  
  public boolean isValidMove(Move m, int player){
    
    if (m == null){
      return false;
    }
    if (m.moveKind == Move.QUIT){
      if (isNetworkComplete(player)) {
        return true;
      }
      else{
        return false;
      }
    }
    if (player != BLACK && player != WHITE){
      return false;
    }
    if (m.x1 < 0 || m.x1 > WIDTH-1 || m.y1 < 0 || m.y1 > HEIGHT-1 || m.x2 < 0 || m.x2 > WIDTH-1 || m.y2 < 0 || m.y2 > HEIGHT-1 ){
      return false;
    }
    if ((m.x1 == 0 || m.x1 == WIDTH-1) && (m.y1 == 0 || m.y1 == HEIGHT-1)){
      return false;
    }
    if (getPiece(m.x1,m.y1) != EMPTY){
      return false;
    }
    if (player == WHITE && (m.y1 == HEIGHT-1 || m.y1 == 0)){
      return false;   
    } else if(player == BLACK && (m.x1 == 0 || m.x1 == WIDTH-1)){
      return false;
    }
    if (m.moveKind == Move.ADD){
      if (player == BLACK){
        if (blackpieces > 10){
          return false;
        }
      }
      else if(player == WHITE) {
        if (whitepieces > 10){
          return false;
        }
      }
    } else if (m.moveKind == Move.STEP){
      if ((getPiece(m.x2,m.y2) != player) || (player == BLACK && blackpieces < 10) || (player == WHITE && whitepieces < 10)){
        return false;
      }
      if (m.x2 == m.x1 && m.y2 == m.y1){
        return false;
      }
      if(player == BLACK && (m.x2 == 0 || m.x2 == WIDTH-1)){
        return false;
      } else if(player == WHITE && (m.y2 == HEIGHT-1 || m.y2 == 0)){
        return false;
      }
    }
    makeMove(m, player);  
    DList n = neighbors(m.x1,m.y1, player);
    if (n.length() > 1){
      undoMove(m,player);
      return false;
    } else if (n.length() < 1){
       undoMove(m,player);
      return true;
    } else {
      DList n1 = new DList();
      try {
        n1 = neighbors(( (Coordinate) n.front().item()).getX(), ((Coordinate) n.front().item()).getY(), player);
      } catch (InvalidNodeException e) {}
      if (n1.length() > 1){
          undoMove(m,player);
        return false;
      }
    }
    undoMove(m,player);
    
    return true;
  }
  
   private DList neighbors (int i, int j, int player){
     DList neighbors = new DList();
     for (int a = -1; a <= 1; a++)
       for (int b = -1; b <=1; b++){
         if ((a != 0 || b != 0) && (j+ a >= 0) && (j + a <= HEIGHT-1) && (i + b >= 0) && (i + b <= WIDTH-1))
         {
           if (getPiece(i+b, j+a) == player)
           {
             neighbors.insertFront(new Coordinate(i+b,j+a));
           }
         }
       }
     return neighbors;
    }
  public boolean makeMove (Move m, int player){
    if (m.moveKind == Move.ADD){
      setPiece(m.x1,m.y1,player);
      if (player == BLACK){
        blackpieces++;
      } else{
        whitepieces++;
      }
      return true;
    } else if (m.moveKind == Move.STEP){
      setPiece(m.x2,m.y2, EMPTY);
      setPiece(m.x1,m.y1,player);
      return true;
    } else{
      return false;
    }
  }
  
  public boolean undoMove (Move m, int player){
    if (m.moveKind == Move.ADD){
      setPiece(m.x1,m.y1,EMPTY);
      if (player == BLACK){
        blackpieces--;
      }
      else{
        whitepieces--;
      }
      return true;
    } else if (m.moveKind == Move.STEP){
        setPiece(m.x1,m.y1,EMPTY);
        setPiece(m.x2,m.y2,player);
        return true;
    } else{
      return false;
    }
  }
  
  public int getAddsRemaining(int player){
    if (player == BLACK){
      return 10-blackpieces;
    } else{
      return 10-whitepieces;
    }
  }
  
  public DList generateAllPossibleMoves(int player){
    DList allMoves = new DList();
    if (getAddsRemaining(player) > 0){
      for (int i = 0; i < WIDTH; i++){
        for (int j = 0; j < HEIGHT; j++){
          Move m = new Move(i,j);
          if (isValidMove(m, player)){
            allMoves.insertBack(m);
          }
        }
      }
    } else{
      DList startPositions = new DList();
      for (int i = 0; i < WIDTH; i++){
        for (int j = 0; j < HEIGHT; j++){
          if (getPiece(i,j) == player){
            startPositions.insertBack(new Coordinate(i, j));
          }
        }
      }
      for (int k = 0; k < WIDTH; k++){
        for (int l = 0; l < HEIGHT; l++){
          try {
            Move m = new Move (k,l,((Coordinate) startPositions.front().item()).getX(), ((Coordinate) startPositions.front().item()).getY());
            if (isValidMove(m, player)){
              allMoves.insertBack(m);
            }
          } catch (InvalidNodeException e) {}
        }
      }
    }
    return allMoves;
  }
  
  public DList getAllConnections(int player, Coordinate c){
    DList connections = new DList();
    for (int i = 0; i < 8; i++) {
      Coordinate neighbor = incrementDirection(i, c);
      while (neighbor.getX() < WIDTH && neighbor.getX() >= 0 && neighbor.getY() >= 0 && neighbor.getY() < HEIGHT){
        if (getPiece(neighbor.getX(), neighbor.getY()) == player){
          connections.insertBack(neighbor);
        }
        if (getPiece(neighbor.getX(), neighbor.getY()) != EMPTY){
          break;
        } else {
          neighbor = incrementDirection(i, neighbor); 
        }
      }
    }
    return connections;
  }
  
  private Coordinate incrementDirection (int i, Coordinate c){
    int neighborX = Integer.MAX_VALUE, neighborY = Integer.MAX_VALUE;
    switch (i){
      case 0:
        neighborX = c.getX()+1;
        neighborY = c.getY()+1;
        break;
      case 1:
        neighborX = c.getX()-1;
        neighborY = c.getY()-1;
        break;
      case 2:
        neighborX = c.getX()+1;
        neighborY = c.getY()-1;
        break;
      case 3:
        neighborX = c.getX()-1;
        neighborY = c.getY()+1;
        break;
      case 4:
        neighborX = c.getX();
        neighborY = c.getY()+1;
        break;
      case 5:
        neighborX = c.getX();
        neighborY = c.getY()-1;
        break;
      case 6:
        neighborX = c.getX()+1;
        neighborY = c.getY();
        break;
      case 7:
        neighborX = c.getX()-1;
        neighborY = c.getY();
        break;
    }
    return new Coordinate(neighborX, neighborY);
  }

  /**
  * isNetworkComplete(int player) determines whether player has formed a complete network
  * @param player is the player whose possible networks this method is checking
  **/
  public boolean isNetworkComplete(int player){
    boolean reachedGoal = false;
    int[][] exploration = new int[WIDTH][HEIGHT];
    // System.out.println("Exploration is initialized to");
    // printArray(exploration);
    Coordinate notACoordinate = new Coordinate();
    if(player == WHITE){
      int i = 0;
      while(i < HEIGHT && !reachedGoal){
        // call networkReachesGoal
        Coordinate coordinateToTest = new Coordinate(0,i);
        reachedGoal = reachedGoal || networkReachesGoal(WHITE,coordinateToTest,coordinateToTest,notACoordinate,exploration,5);
        i++;
      }

    } else if(player == BLACK){
      int i = 0;
      while(i < WIDTH && !reachedGoal){
        // call networkReachesGoal
        Coordinate coordinateToTest = new Coordinate(i,0);
        reachedGoal = reachedGoal || networkReachesGoal(BLACK,coordinateToTest,coordinateToTest,notACoordinate,exploration,5);
        i++;
      }
    }
    return reachedGoal;
  }

  private boolean networkReachesGoal(int player,Coordinate origin,Coordinate currCoord,Coordinate prevCoord,int[][] exploration,int depth){
    int explored = 1;
    int unexplored = 0;
    // System.out.println("The current search depth is " + depth);
    // System.out.println("The current coordinate being checked is " + currCoord);
    // if piece doesn't match player, return false
    // if the depth is below the minimum and the current piece is on the opposite end of the origin
    //  a valid network has been formed and return true
    // if the current node is an edge, and no valid network is formed, return false
    if(getPiece(currCoord.getX(),currCoord.getY()) != player){
      return false;
    } else if(inBothGoals(origin,currCoord) && depth <= 0){
      return true;
    } else if(isInGoal(currCoord) && !currCoord.equals(origin)){
      return false;
    }
    // mark the current node as explored
    exploration[currCoord.getY()][currCoord.getX()] = explored;
    // get all of the pieces linked to the current piece, recurse through them
    DList allConnections = getAllConnections(player,currCoord);
    boolean validNetworkFormed = false;
    while(allConnections.length() > 0 && !validNetworkFormed){
      try{
        DListNode connectedPiece = (DListNode) allConnections.back();
        Coordinate nextCoord = (Coordinate) connectedPiece.item();
        /* don't continue to explore nodes that are collinear with the past 2 points
          or nodes that have already been explored */
        if(!isOnSameLine(prevCoord,currCoord,nextCoord) && exploration[nextCoord.getY()][nextCoord.getX()] == unexplored){
          validNetworkFormed = validNetworkFormed || networkReachesGoal(player,origin,nextCoord,currCoord,exploration,depth-1);
        }
        // remove the DListNode
        connectedPiece.remove();
      } catch(InvalidNodeException e){}
    }
    // unmark current node as explored, so it can be reached by other paths
    exploration[currCoord.getY()][currCoord.getX()] = unexplored;
    return validNetworkFormed;
  }

  /**
  * isOnSameLine determines whether three (x,y) coordinates are on the same line by calculating
  *   the slope of the lines between them
  * It returns true if the coordinates are on the same line, and returns false if the coordinates
  *   are on different lines
  * @param first is a Coordinate object representing the first (x,y) coordinate
  * @param second is a Coordinate object representing the second (x,y) coordinate
  * @param third is a Coordinate object representing the third (x,y) coordinate
  **/
  private boolean isOnSameLine(Coordinate first, Coordinate second, Coordinate third){
    double firstRun = second.getY() - first.getY();
    double firstRise = second.getX() - first.getX();

    double secondRun = third.getY() - second.getY();
    double secondRise = third.getX() - second.getX();

    return (firstRise / firstRun) == (secondRise / secondRun);
  }

  /**
  * inBothGoals determines whether two (x,y) coordinates are in opposite goals
  *   (i.e.) if the two coordinates are linked, then the two points form the endpoints of a network
  * @param origin is a Coordinate object representing the second (x,y) coordinate 
  * @param destination is a Coordinate object representing the second (x,y) coordinate
  **/
  public boolean inBothGoals(Coordinate origin, Coordinate destination){
    if(isInGoal(origin) && isInGoal(destination)){    
      if(origin.getX() == 0){
        return destination.getX() == WIDTH - 1;
      } else if(origin.getX() == WIDTH-1){
        return destination.getX() == 0;
      } else if(origin.getY() == 0){
        return destination.getY() == HEIGHT-1;
      } else if(origin.getY() == HEIGHT-1){
        return destination.getY() == 0;
      }
    }
    return false;
  }
  
  /**
  * isInGoal determines whether an (x,y) coordinate is in a goal.
  *   It returns true if the coordinate is on one of the edges of the gameboard.
  *   It returns false otherwise
  * @param coordinate is the Coordinate object which is being tested
  **/
  protected boolean isInGoal(Coordinate coordinate){
    int x = coordinate.getX();
    int y = coordinate.getY();
    return (x==0)||(x==WIDTH-1)||(y==0)||(y==HEIGHT-1);
  }

  public static void main(String args[]){
    Board testBoard = new Board();
    //testBoard.printBoard();

    /*

    testBoard.addPiece(BLACK,3,4);
    testBoard.printBoard();
    testBoard.addPiece(BLACK,1,0);
    testBoard.printBoard();
    testBoard.addPiece(BLACK,3,2);
    testBoard.addPiece(BLACK,5,4);
    testBoard.printBoard();
    testBoard.addPiece(BLACK,4,5);
    testBoard.printBoard();
    testBoard.addPiece(BLACK,4,7);
    testBoard.printBoard();
    //System.out.println(testBoard.isInGoal(new Coordinate(1,0)));
    //System.out.println(testBoard.inBothGoals(new Coordinate(1,0), new Coordinate(4,7)));
    // System.out.println(testBoard.getAllConnections(BLACK,new Coordinate(1,0)));
    System.out.println("Valid network formed: " + testBoard.isNetworkComplete(BLACK));


    testBoard.addPiece(WHITE,2,1);
    testBoard.printBoard();
    System.out.println("Valid network formed: " + testBoard.isNetworkComplete(BLACK));

    */
    /*
    //testBoard.addPiece(BLACK,2,0);
    testBoard.addPiece(BLACK,6,0);
    testBoard.addPiece(BLACK,4,2);
    testBoard.addPiece(BLACK,3,3);
    //testBoard.addPiece(BLACK,1,3);
    testBoard.addPiece(BLACK,2,5);
    testBoard.addPiece(BLACK,3,5);
    //testBoard.addPiece(BLACK,5,5);
    //testBoard.addPiece(BLACK,6,5);
    testBoard.addPiece(BLACK,2,7);
    //testBoard.addPiece(BLACK,5,7);
    //testBoard.printBoard();
    //System.out.println("Valid network formed " + testBoard.isNetworkComplete(BLACK));
   // System.out.println(testBoard.isValidMove(new Move(4,6), BLACK));
    testBoard.printBoard();
    System.out.println(testBoard.generateAllPossibleMoves(BLACK));
    */

    Move testMove = new Move(1,1);
    System.out.println(testMove.x1 + " " + testMove.y1);
    System.out.println(testMove.x2 + " " + testMove.y2);

  }

  private void addPiece(int player, int x, int y){
    Move addMove = new Move(x,y);
    makeMove(addMove,player);

  }


  public void printBoard(){
    System.out.println("New Board is " );
    for(int i = 0; i < HEIGHT; i++){
      for(int j = 0; j < WIDTH; j++){
        String character;
        System.out.print(getPiece(j,i) + " ");
      }
      System.out.println();
    }

  }
  
  public void printArray(int[][] array){
    for(int i = 0; i < HEIGHT; i++){
      for(int j = 0; j < WIDTH; j++){
        System.out.print(array[i][j] + " ");
      }
      System.out.println();
    }
  }
  
}
