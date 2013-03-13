public class EvaluatedMove{

    // fields
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