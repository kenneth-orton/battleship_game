import java.util.*;

class Point {
    private static final int MIN = 0;
    private static final int MAX = 9;
    private int x;
    private int y;

    Point(){
        this.x = 0;
        this.y = 0;
    }

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
        this.x = xVal;
    }

    protected void setYVal(int yVal){
        this.y = yVal;
    }

    public String toString(){
        return "(" + x + ", " + y + ")";
    }

	protected ArrayList<Point> nearbyNeighbors(){
        ArrayList<Point> pointsNearby = new ArrayList<Point>();
        // north (0), east (1), south (2), west (3)
        pointsNearby.add(0, new Point(x, y - 1));
        pointsNearby.add(1, new Point(x + 1, y));
        pointsNearby.add(2, new Point(x, y + 1));
        pointsNearby.add(3, new Point(x - 1, y));
        return pointsNearby;
    }

    protected boolean validPoint(){
        return(x >= MIN && x <= MAX && y >= MIN && y <= MAX);
    }
}
