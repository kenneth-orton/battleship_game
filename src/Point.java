import java.util.*;

class Point {
    private static final int MIN = 0;
    private static final int MAX = 9;
    private int x;
    private int y;

    Point(int x, int y){
        this.x = x;
        this.y = y;
    }

    protected int xValue(){
        return x;
    }

    protected int yValue(){
        return y;
    }

    protected void setXVal(int xVal){
        x = x;
    }

    protected void setYVal(int yVal){
        y = y;
    }

    public String toString(){
        return "(" + x + ", " + y + ")";
    }

	protected ArrayList<Point> nearbyNeighbors(){
        ArrayList<Point> pointsNearby = new ArrayList<Point>();
        // north (0), east (1), south (2), west (3)
        if(new Point(x, y - 1).validPoint()){
            pointsNearby.add(new Point(x, y - 1));
        }
        if(new Point(x + 1, y).validPoint()){
            pointsNearby.add(new Point(x + 1, y));
        }
        if(new Point(x, y + 1).validPoint()){
            pointsNearby.add(new Point(x, y + 1));
        }
        if(new Point(x - 1, y).validPoint()){
            pointsNearby.add(new Point(x - 1, y));
        }
        return pointsNearby;
    }

    protected boolean validPoint(){
        return(x >= MIN && x <= MAX && y >= MIN && y <= MAX);
    }

}
