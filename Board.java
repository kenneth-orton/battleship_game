class Board {
    private char[][] grid;
    private int gamePieces = 5;

    Board() {
        grid = createGrid();
    }

    protected void addHit(int x, int y){
        this.grid[y][x] = 'X';
    }

    protected void addMiss(int x, int y){
        this.grid[y][x] = 'M';
    }

    protected char detectShot(int x, int y){
        return this.grid[y][x];
    }

    protected void updateGamePieces(){
        this.gamePieces -= 1;
    }

    protected int remainingGamePieces(){
        return this.gamePieces;
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
        System.out.println();
    }

    protected void addShip(Ship ship){
        switch(ship.direction()){
            case 'V':
                for(int i = ship.yCoord(); i < ship.yCoord() + ship.shipLength(); i ++){
                    this.grid[i][ship.xCoord()] = ship.shipType();
                }
                break;
            case 'H':
                for(int i = ship.xCoord(); i < ship.xCoord() + ship.shipLength(); i ++){
                    this.grid[ship.yCoord()][i] = ship.shipType();
                }
                break;
        }

    }

    protected boolean doesPieceFit(Ship ship){
        boolean answer = true;
        switch(ship.direction()){
            case 'V': 
                // check if piece will fit on board, then see if space is already occupied
                if(ship.yCoord() + ship.shipLength() - 1 <= 9){
                    for(int i = ship.yCoord(); i < ship.yCoord() + ship.shipLength(); i ++){
                        if(this.grid[i][ship.xCoord()] != '_'){
                            return false;
                        }
                    }
                }else{
                    return false;
                }
                break;
            case 'H':
                if(ship.xCoord() + ship.shipLength() - 1 <= 9){
                    for(int i = ship.xCoord(); i < ship.xCoord() + ship.shipLength(); i ++){
                        if(this.grid[ship.yCoord()][i] != '_'){
                            return false;
                        }
                    }
                }else{
                    return false;
                }
                break;
        }
        return answer;
    }
}
