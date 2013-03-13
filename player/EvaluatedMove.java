package player;
public class EvaluatedMove{

    // fields
    //(NEGATIVE_INFINITY,POSITIVE_INFINITY) range of the value
    protected Move move;
    protected double value;
    
    /**
     * constructor
     **/
    public EvaluatedMove(move,value){
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
    public Move valueGetter(){
        return value;
    }

}