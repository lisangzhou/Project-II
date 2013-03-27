/* MachinePlayer.java */
package player;

import list.*;

import java.util.Random;

/**
 *  An implementation of an automatic Network player.  Keeps track of moves
 *  made by both players.  Can select a move for itself.
 */
public class MachinePlayer extends Player {
    
    public static final int DEEP=4;
    protected int color;
    protected int depth;
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
    public Move chooseMove(){
    	if (board.getAddsRemaining(color) > 0)
            depth = 4;
        else
            depth = 2;
        Move temp = miniMax(color, depth, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY).moveGetter();
        // miniMax function doesn't exist right now
        // find the best move
        moving(temp, color);
        // after finding the best move, do it and make a change
        
        // return this move
        return temp;
        
        
    }
    
    // If the Move m is legal, records the move as a move by the opponent
    // (updates the internal game board) and returns true.  If the move is
    // illegal, returns false without modifying the internal state of "this"
    // player.  This method allows your opponents to inform you of their moves.
    public boolean opponentMove(Move m) {
        return moving(m, colorOpponent());
    }
    
    // If the Move m is legal, records the move as a move by "this" player
    // (updates the internal game board) and returns true.  If the move is
    // illegal, returns false without modifying the internal state of "this"
    // player.  This method is used to help set up "Network problems" for your
    // player to solve.
    public boolean forceMove(Move m) {
        return moving(m,color);
    }
    
    
    // private function used by chooseMove, opponentMove, and forceMove
    private boolean moving(Move m, int color){
    	if (board.isValidMove(m, color))
    	{
    		board.makeMove(m, color);
    		return true;
    	}
    	else
    		return false;
    }
    
    // getting the color of the opponent
    private int colorOpponent(){
        if(this.color==Board.BLACK){
            return Board.WHITE;
        }else{
            return Board.BLACK;
        }
    }
    
    // miniMax function to be used later
    private EvaluatedMove miniMax(int color, int depth, double alpha, double beta){
    	
    	
    	EvaluatedMove myMove = new EvaluatedMove(); //My best move
    	EvaluatedMove reply; //Opponent's best reply
    	int oppositeColor;
    	if (color == Board.WHITE)
    		oppositeColor = Board.BLACK;
    	else
    		oppositeColor = Board.WHITE;
    	if (evaluateMove(board) == Double.NEGATIVE_INFINITY || evaluateMove(board) == Double.POSITIVE_INFINITY || depth == 0)
    		return new EvaluatedMove(evaluateMove(board));
        
    	
    	if (color == this.color)
    	{
    		myMove.value = alpha;
    	}
    	else
    	{
    		myMove.value = beta;
    	}
    	
    	DList allMoves = board.generateAllPossibleMoves(color);
    	try
    	{
            DListNode n = (DListNode) allMoves.front();
            while (n.isValidNode())
            {
                Move m = (Move) n.item();
                board.makeMove(m, color);
                reply = miniMax(oppositeColor, depth-1, alpha, beta);
                board.undoMove(m, color);
                
                if ((color == this.color) && (reply.value >= myMove.value))
                {
                    myMove.move = m;
                    myMove.value = reply.value;
                    alpha = reply.value;
                }
                else if ((color == colorOpponent()) && (reply.value <= myMove.value))
                {
                    myMove.move = m;
                    myMove.value = reply.value;
                    beta = reply.value;
                }
                if (alpha >= beta)
                {
                    return myMove;
                }
                n = (DListNode) n.next();
            }
    	}
    	catch(InvalidNodeException e)
    	{
		}
    	return myMove;
    }
    
    
    
    /**
     * Evaluates moves, assigning a score to each one. It assigns a maximum
     * positive score to a win by the MachinePlayer, a minimum negative score to
     * win by the opponent, and an intermediate score to a board where neither
     * player has completed a network.
     * @param b is the Board object being evaluated
     * @param player is the player whose turn it is
     **/
    public double evaluateMove(Board b){

    	int playerScore = 0;
    	int opponentScore = 0; 
        
    	if(b.isNetworkComplete(colorOpponent())) // change this later; going out for dinner
    	{
    		return Double.NEGATIVE_INFINITY;
    	}
    	else if(b.isNetworkComplete(color))
    	{
    		return Double.POSITIVE_INFINITY;
    	}
    	for (int i = 0; i < Board.WIDTH; i++)
    	{
    		for (int j = 0; j < Board.HEIGHT; j++)
    		{
    			if (board.getPiece(i, j) == color)
    			{
    				playerScore += board.getAllConnections(color, new Coordinate(i,j)).length();
    			}
    			else if (board.getPiece(i,j) == colorOpponent())
    			{
    				opponentScore += board.getAllConnections(colorOpponent(), new Coordinate(i,j)).length();
    			}
    			if (board.getPiece(i,j) == color && board.isInGoal(new Coordinate(i,j)))
    			{
    				playerScore += 1;
    			}
    			else if  (board.getPiece(i,j) == colorOpponent() && board.isInGoal(new Coordinate(i,j)))
    			{
    				opponentScore += 1;
    			}
    		}
    	}
    	return playerScore - opponentScore;
      
    } 

}


/* Toby's Eval Function: 
    /**
    * evaluateBoard predicts the outcome of the game for the given board, b,
    * based on the team's devised strategies and returns a floating point number
    * between -1 and 1 to represent the outcome.  Scores closer to 1 mean "this"
    * player is more likely to win and scores closer to -1 mean the opponent is
    * more likely to win.
    *
    * @param b the Board object to be evaluated.
    * @return a floating point number between -1 to 1 of the possible outcome.
    
    private float evaluateBoard(Board b, int playerColor){
        double playerConnections = 0;
        double oppConnections = 0;
        float score = 0;
        float goalScore = .05f;
        int[] pieceInGoal = {0,0,0,0};
        
        if(b.isNetworkComplete(colorOpponent())){
    		return -1;
    	}else if(b.isNetworkComplete(color)){
    		return 1;
    	}
        
        for(int k = 1; k < Board.WIDTH-1; k++){
              if(b.getSquare(k,0) == Board.BLACK){
                  pieceInGoal[0]++;
              }
              if(b.getSquare(k,Board.HEIGHT-1) == Board.BLACK){
                  pieceInGoal[1]++;
              }
              if(b.getSquare(0,k) == Board.WHITE){
                  pieceInGoal[2]++;
              }
              if(b.getSquare(Board.WIDTH-1,k) == Board.WHITE){
                  pieceInGoal[3]++;
             }
        }
        for(int m = 2; m < 4; m++){
            if(pieceInGoal[m] == 1){
                if(playerColor == Board.WHITE){
                    score += goalScore;
                }else{
                    score -= goalScore;
                }
            }
            if(goalPieces[m-2] == 1) {
                if(playerColor == Board.BLACK){
                    score += goalScore;
                }else{
                    score -= goalScore;
                }
            }
        }
        for(int j = 0; j < Board.WIDTH; j++){
            for(int i = 0; i < Board.HEIGHT; i++){
                if(b.getPiece(i,j) == playerColor){
                    playerConnections += (b.getAllConnections(playerColor, new Coordinate(i,j))).length();
                }
                else if(b.getPiece(i,j) == oppositeColor(playerColor)){
                    oppConnections += (b.getAllConnections(oppositeColor(playerColor), new Coordinate(i,j))).length();
                }
            }
        }
        score += (playerConnections-oppConnections)*goalScore/2.0;
        return score;
    }


     /**
    * colorOpponent is a helper for getting the color of the other player. 
    * It returns the color of the opponent
    
    private int oppositeColor(int color){
        if(color==Board.WHITE){
            return Board.BLACK;
        }else{
            return Board.WHITE;
        }
    }


	


*/
