package player;

public class EvaluatedMove {

	protected Move move;
	protected double value;

	/**
	 * getMove() is a getter method for the move of the EvaluatedMove.
	 * @return move of the EvaluatedMove
	 */
	public Move getMove() {
		return move;
	}

	/**
	 * getValue() is a getter method for the value of the EvaluatedMove.
	 * @return value of the EvaluatedMove
	 */
	public double getValue() {
		return value;
	}
	
	/**
	 * setMove(Move m) is a setter method for the move of the EvaluatedMove.
	 * @param m is the move to set
	 */
	protected void setMove(Move m) {
		move = m;
	}
	
	/**
	 * setValue(double v) is a setter method for the value of the EvaluatedMove.
	 * @param v is the value to set
	 */
	protected void setValue(double v) {
		value = v;
	}

}
