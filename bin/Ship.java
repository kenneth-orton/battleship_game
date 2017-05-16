abstract class Ship {
    protected int x;
    protected int y;
    protected int hits = 0;
    protected char type;
    protected char orientation;

    Ship(Point point, char type, char orientation){
        this.x = point.xValue();
        this.y = point.yValue();
        this.type = type;
        this.orientation = orientation;
    }

    protected int xCoord(){
        return x;
    }

    protected int yCoord(){
        return y;
    }

    protected void setPoint(Point point){
        this.x = point.xValue();
        this.y = point.yValue();
    }

    protected void setOrientation(char newOrient){
        this.orientation = newOrient;
    }

    protected char shipOrientation(){
        return orientation;
    }

    protected char shipType(){
        return type;
    }

    protected void updateHits(){
        hits += 1;
    }

    protected boolean isSunk(){
        return(hits == shipLength());
    }

	protected abstract int shipLength();

	protected abstract String shipName();
}

