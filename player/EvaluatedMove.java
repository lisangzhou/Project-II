package player;

/**
 * A public class for holding all the fields in a move. This class is a
 * container for data, not an ADT.
 * 
 * move field contains a Move object that has been choosen value field is the
 * assigned value from (Alpha Beta Pruning) miniMax method; greated value means
 * more preferred
 * 
 * The range of value field is (NEGATIVE_INFINITY,POSITIVE_INFINITY)
 * 
 * DO NOT CHANGE THIS FILE.
 */
public class EvaluatedMove {

	protected Move move;
	protected double value;

	/**
	 * return move
	 **/
	public Move getMove() {
		return move;
	}

	/**
	 * return value
	 **/
	public double getValue() {
		return value;
	}
	
	protected void setMove(Move m) {
		move = m;
	}
	
	protected void setValue(double v) {
		value = v;
	}

}
