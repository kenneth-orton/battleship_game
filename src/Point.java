import java.util.LinkedList;

class Point {
    private static final int MIN_X = 0;
    private static final int MAX_X = 9;
    private static final int MIN_Y = 0;
    private static final int MAX_Y = 9;
    private static int x;
    private static int y;
    private static LinkedList<Point> pointList = new LinkedList<Point>();

    Point(){
        this.x = -1;
        this.y = -1;
    }

    Point(int x, int y){
        this.x = x;
        this.y = y;
    }

    protected int xValue(){
        return this.x;
    }

    protected int yValue(){
        return this.y;
    }

    protected void setXVal(int x){
        this.x = x;
    }

    protected void setYVal(int y){
        this.y = y;
    }

    protected Point nearbyNeighbor(){
        int x = this.x;
        int y = this.y;
        Point newPoint = new Point();
        do{
            // north, east, south, west
            if(validPoint(new Point(x, y - 1))){
                newPoint = new Point(x, y - 1);
            }else if(validPoint(new Point(x + 1, y))){
                newPoint = new Point(x + 1, y);
            }else if(validPoint(new Point(x, y + 1))){
                newPoint = new Point(x, y + 1);
            }else if(validPoint(new Point(x - 1, y))){
                newPoint = new Point(x - 1, y);
            }
        }while(visitedPoint(newPoint));
        pointList.add(newPoint);
        return newPoint;
    }

    protected boolean validPoint(Point point){
        int x = point.xValue();
        int y = point.yValue();
        return(x >= MIN_X && x <= MAX_X && y >= MIN_Y && y <= MAX_Y);
    }

    protected boolean visitedPoint(Point point){
        boolean result = false;
        for(Point item : pointList){
            if(item.equals(point)){
                result = true;
            }
        }
        return(result);
    }

    protected boolean isDefault(){
        return(this.x == -1 && this.y == -1);
    }
}
