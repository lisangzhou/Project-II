/* MachinePlayer.java */
package player;

import list.*;

/**
 * An implementation of an automatic Network player. Keeps track of moves made
 * by both players. Can select a move for itself.
 **/
public class MachinePlayer extends Player {

	public static final int DEEP = 4;
	protected int color;
	protected int depth;
	protected Board board;

	/**Creates a machine player with the given color. Color is either 0 (black)
	 * or 1 (white). (White has the first move.)
	 **/
	public MachinePlayer(int color) {
		this(color, DEEP);
	}

	/** Creates a machine player with the given color and search depth. Color is
	 * either 0 (black) or 1 (white). (White has the first move.)
	 **/
	public MachinePlayer(int color, int searchDepth) {
		this.color = color;
		this.depth = searchDepth;
		board = new Board();
	}

	/** Returns a new move by "this" player. Internally records the move (updates
	 *  the internal game board) as a move by "this" player.
	 **/
	public Move chooseMove() {
		Move playerMove = miniMax(color, depth, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY).getMove();
		moving(playerMove, color);
		return playerMove;
	}

	/** If the Move m is legal, records the move as a move by the opponent
	 * (updates the internal game board) and returns true. If the move is
	 * illegal, returns false without modifying the internal state of "this"
	 * player. This method allows your opponents to inform you of their moves.
	 **/public boolean opponentMove(Move m) {
		return moving(m, colorOpponent());
	}

	/** If the Move m is legal, records the move as a move by "this" player
	 *  (updates the internal game board) and returns true. If the move is
	 *  illegal, returns false without modifying the internal state of "this"
	 *  player. This method is used to help set up "Network problems" for your
	 *  player to solve.
	**/ public boolean forceMove(Move m) {
		return moving(m, color);
	}

	/** Private function used by chooseMove, opponentMove, and forceMove
	 *  Makes a move on the board after verifying that the move is valid
	 *  otherwise, return false
	 *  @param m: the move planned
	 *  @param color: the color of the player
	 *  @return true if the move is successfully made. false if not (i.e. the move was invalid).
	 **/private boolean moving(Move m, int color) {
		if (board.isValidMove(m, color)) {
			board.makeMove(m, color);
			return true;
		} else {
			return false;
		}
	}

	/** Private function for convenience. Returns the opponent of the player. 
	 *  ie: return BLACK if "this" player is WHITE
	 **/
	private int colorOpponent() {
		if (this.color == Board.BLACK) {
			return Board.WHITE;
		} else {
			return Board.BLACK;
		}
	}

	/** miniMax is a private function that finds the best move on the game tree. Uses Alpha Beta Pruning. 
	 *  @param color: color being searched 
	 *  @param depth: the depth intended for searching
	 *  @param alpha: score that the computer knows with certainty it can achieve
	 *  @param beta:  the opponent can achieve a score of beta or lower.
	 *  @return an EvaluatedMove object that contains the move and value of the best possible move.
	 **/
	private EvaluatedMove miniMax(int color, int depth, double alpha, double beta) {

		EvaluatedMove myMove = new EvaluatedMove(); 
		EvaluatedMove reply; 
		int oppositeColor;
		if (color == Board.WHITE) {
			oppositeColor = Board.BLACK;
		} else {
			oppositeColor = Board.WHITE;
		}
		
		if (board.isNetworkComplete(this.color) || board.isNetworkComplete(colorOpponent()) || depth == 0) {
			EvaluatedMove eval = new EvaluatedMove();
			eval.setValue(evaluateMove(board, depth));
			return eval;
		}

		if (color == this.color) {
			myMove.setValue(alpha);
		} else {
			myMove.setValue(beta);
		}

		DList allMoves = board.generateAllPossibleMoves(color);
		try {
			DListNode n = (DListNode) allMoves.front();
			for (int i = 0; i < allMoves.length(); i++) {
				Move move = (Move) n.item();
				board.makeMove(move, color);
				reply = miniMax(oppositeColor, depth - 1, alpha, beta);
				board.undoMove(move, color);
				
				if ((color == this.color) && (reply.getValue() > myMove.getValue())) {
					myMove.setMove(move);
					myMove.setValue(reply.getValue());
					alpha = reply.getValue();
				} else if ((color == colorOpponent()) && (reply.getValue() < myMove.getValue())) {
					myMove.setMove(move);
					myMove.setValue(reply.getValue());
					beta = reply.getValue();
				}
				
				if (alpha >= beta) {
					return myMove;
				}
				n = (DListNode) n.next();
			}
		} catch (InvalidNodeException e) {}
		return myMove;
	}

	/**
	 * evaluateMove(Board b, int depth), evaluates a board, assigning a score to it. It assigns a maximum
	 * positive score to a win by the MachinePlayer, a minimum negative score to
	 * win by the opponent, and an intermediate score to a board where neither player has completed a network. 
	 * The evaluation function also assigns a higher score to a win in a shorter number of moves (for example a win in 1 move, versus a win in 3 moves).
	 * To evaluate boards where no win exists, the evaluation function adds the number of computer connections for each chip and the number of computer pieces 
	 * in goal squares. From this value, it subtracts the number of opponent connections for each chip and the number of opponent pieces in goal squares.
	 * 
	 * @param b: the Board object being evaluated
	 * @param depth: depth at which the Board b is reached
	 * @return a double that is the evaluated score of the board b. 
	 **/
	private double evaluateMove(Board b, int depth) {

		int playerScore = 0;
		int opponentScore = 0;

		if (b.isNetworkComplete(colorOpponent())) {
			return -1000 - depth;
		} else if (b.isNetworkComplete(color)) {
			return 1000 + depth;
		}
		
		for (int i = 0; i < Board.WIDTH; i++) {
			for (int j = 0; j < Board.HEIGHT; j++) {
				if (board.getPiece(i, j) == color) {
					playerScore += board.getAllConnections(color, new Coordinate(i, j)).length();
				} else if (board.getPiece(i, j) == colorOpponent()) {
					opponentScore += board.getAllConnections(colorOpponent(),new Coordinate(i, j)).length();
				}
				if (board.getPiece(i, j) == color && board.isInGoal(new Coordinate(i, j))) {
					playerScore += 1;
				} else if (board.getPiece(i, j) == colorOpponent() && board.isInGoal(new Coordinate(i, j))) {
					opponentScore += 1;
				}
			}
		}
		return playerScore - opponentScore;
	}
}
