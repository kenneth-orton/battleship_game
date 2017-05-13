class Board {
    private char[][] grid;

    Board() {
        grid = createGrid();
    }

    protected void addShip(Ship ship){
        switch(ship.direction()){
            case 'V': 
                
                break;
            case 'H':
                break;
        }
        //this.grid[y][x] = 'O';
    }

    protected void addHits(int x, int y){
    }

    protected void addMisses(int x, int y){
    }

    protected void updateGrid(Ship ship, char orientation, int shipLength){
        switch(orientation){
            case 'V':
                for(int i = ship.yCoord(); i < ship.yCoord() + shipLength; i ++){
                    this.grid[i][ship.xCoord()] = 'O';
                }
                break;
            case 'H':
                for(int i = ship.xCoord(); i < ship.xCoord() + shipLength; i ++){
                    this.grid[ship.yCoord()][i] = 'O';
                }
                break;
        }

    }

    protected boolean isOccupied(Ship ship, char orientation, int shipLength){
        boolean answer = false;
        switch(orientation){
            case 'V':
                for(int i = ship.yCoord(); i < ship.yCoord() + shipLength; i ++){
                    if(this.grid[i][ship.xCoord()] == 'O'){
                        answer = true;  
                    }
                }
                break;
            case 'H':
                for(int i = ship.xCoord(); i < ship.xCoord() + shipLength; i ++){
                    if(this.grid[ship.yCoord()][i] == 'O'){
                        answer = true;
                    }
                }
                break;
        }
        return answer;
    }

    protected static char[][] createGrid(){
        char[][] grid = new char[10][10];
        for(int i = 0; i < grid.length; i ++){
            for(int j = 0; j < grid[i].length; j++){
                grid[i][j] = '_';
            }
        }
        return grid;
    }

    protected void displayBoard(){
        System.out.println("\n   0 1 2 3 4 5 6 7 8 9");
        for(int i = 0; i < this.grid.length; i++){
            System.out.print(" " + i);
            for(int j = 0; j < this.grid[i].length; j++){
                System.out.print(" " + this.grid[i][j]);
            }
            System.out.println();
        }
    }

    protected boolean doesPieceFit(Ship ship){
        boolean answer = false;
        int shipLength = ship.shipLength();
        switch(ship.direction()){
            case 'V': 
                if(ship.yCoord() + shipLength - 1 <= 9 && !isOccupied(ship, 'V', shipLength)){
                    updateGrid(ship, 'V', shipLength);
                    answer = true;
                }
                break;
            case 'H':
                if(ship.xCoord() + shipLength - 1 <= 9 && !isOccupied(ship, 'H', shipLength)){
                    updateGrid(ship, 'H', shipLength);
                    answer = true;
                }
                break;
        }
        return answer;
    }

}
