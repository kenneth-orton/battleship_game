class Ship {
    private int x;
    private int y;
    private int hits;
    private char type;
    private char orientation;

    Ship(int x, int y, char type, char orientation){
        this.x = x;
        this.y = y;
        this.type = type;
        this.orientation = orientation;
        this.hits = 0;
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

    protected char shipType(){
        return this.type;
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

    protected String shipName(){
        String name = "";
        switch(this.type){
            case 'P':
                name += "Patrol";
                break;
            case 'D':
                name += "Destroyer";
                break;
            case 'S':
                name += "Submarine";
                break;
            case 'B':
                name += "Battleship";
                break;
            case 'C':
                name += "Carrier";
                break;
        }
        return name;
    }

    protected void updateHits(){
        this.hits += 1;
    }

    protected boolean isSunk(){
        return(this.hits == shipLength());
    }
}

