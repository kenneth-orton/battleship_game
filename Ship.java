class Ship {
    private int x;
    private int y;
    private int hits = 0;
    private char type;
    private char orientation;

    Ship(int x, int y, char type, char orientation){
        this.x = x;
        this.y = y;
        this.type = type;
        this.orientation = orientation;
    }

    protected int xCoord(){
        return this.x;
    }

    protected int yCoord(){
        return this.y;
    }

    protected char direction(){
        return this.orientation;
    }

    protected int shipLength(){
        int length = 0;
        switch(this.type){
            case 'P':
                length = 2;
                break;
            case 'D':
                length = 3;
                break;
            case 'S':
                length = 3;
                break;
            case 'B':
                length = 4;
                break;
            case 'C':
                length = 5;
                break;
        }
        return length;
    }

    protected void detectHit(int x, int y) {
        if(this.orientation == 'V'){
            if(x == xCoord() && (y - yCoord()) < shipLength() && (y - yCoord()) >= 0){
                System.out.println("Hit!");
                this.hits += 1;
            }else{
                System.out.println("Miss!");
            }
        }else{
            if(y == yCoord() && (x - xCoord()) < shipLength() && (x - xCoord()) >= 0){
                System.out.println("Hit!");
                this.hits += 1;
            }else{
                System.out.println("Miss!");
            }
        }
    }

    protected boolean isSunk(){
        return(this.hits == shipLength());
    }

    protected void printResults(){
        System.out.println("(" + xCoord() + ", " + yCoord() + ") "  + shipLength() + " " + this.hits);
    }
}

