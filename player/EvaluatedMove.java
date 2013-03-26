package player;

/**
 *  A public class for holding all the fields in a move.  This class is a
 *  container for data, not an ADT.
 *
 *  move field contains a Move object that has been choosen
 *  value field is the assigned value from (Alpha Beta Pruning) miniMax method; greated value means more preferred
 *
 *  The range of value field is (NEGATIVE_INFINITY,POSITIVE_INFINITY) 
 *
 *  DO NOT CHANGE THIS FILE.
 */
public class EvaluatedMove{

    // fields
    //(NEGATIVE_INFINITY,POSITIVE_INFINITY) range of the value
    protected Move move;
    protected double value;
    
    /**
     * constructor
     **/
    public EvaluatedMove()
    { 
    	move = null;
    }
    
    public EvaluatedMove(double value)
    {
    	this.value = value;
    }
    public EvaluatedMove(Move move,double value){
        this.move=move;
        this.value=value;
    }
    
    /**
     * return move
     **/
    public Move moveGetter(){
        return move;
    }
    
    
    /**
     * return value
     **/
    public double valueGetter(){
        return value;
    }

}
