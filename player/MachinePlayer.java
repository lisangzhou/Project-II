/* MachinePlayer.java */
package player;

import list.*;
import java.util.Random;

/**
 *  An implementation of an automatic Network player.  Keeps track of moves
 *  made by both players.  Can select a move for itself.
 */
public class MachinePlayer extends Player {
    
    public static final int DEEP=10;
    protected int color;
    protected int depth=DEEP;
    protected Board board;
    
    // Creates a machine player with the given color.  Color is either 0 (black)
    // or 1 (white).  (White has the first move.)
    public MachinePlayer(int color) {
        this.color=color;
        this.depth=DEEP;
        board=new Board();
    }
    
    // Creates a machine player with the given color and search depth.  Color is
    // either 0 (black) or 1 (white).  (White has the first move.)
    public MachinePlayer(int color, int searchDepth) {
        this.color=color;
        this.depth=searchDepth;
        board=new Board();
    }
    
    // Returns a new move by "this" player.  Internally records the move (updates
    // the internal game board) as a move by "this" player.
    public Move chooseMove() {
        
        return new Move();
    }
    
    // If the Move m is legal, records the move as a move by the opponent
    // (updates the internal game board) and returns true.  If the move is
    // illegal, returns false without modifying the internal state of "this"
    // player.  This method allows your opponents to inform you of their moves.
    public boolean opponentMove(Move m) {
        return false;
    }
    
    // If the Move m is legal, records the move as a move by "this" player
    // (updates the internal game board) and returns true.  If the move is
    // illegal, returns false without modifying the internal state of "this"
    // player.  This method is used to help set up "Network problems" for your
    // player to solve.
    public boolean forceMove(Move m) {
        return false;
    }
    
    /**
     * Evaluates moves, assigning a score to each one. It assigns a maximum
     * positive score to a win by the MachinePlayer, a minimum negative score to
     * win by the opponent, and an intermediate score to a board where neither
     * player has completed a network.
     * @param b is the Board object being evaluated
     * @param player is the player whose turn it is
    **/
    public double evaluateMove(Board b, int player){
      if(b.isNetworkComplete(changePlayer(player))){
        return Double.NEGATIVE_INFINITY;
      } else if(b.isNetworkComplete(player)){
        return Double.POSITIVE_INFINITY;
      }
    
    } 
    
    public int changePlayer(int player){
      return (3 - player) % 3;
    }
}
