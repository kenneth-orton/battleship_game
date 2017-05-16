class Battleship extends Ship{
    Battleship(){
        super(new Point(0, 0), 'B', 'H');
    }

    Battleship(Point point, char type, char orientation){
		super(point, 'B', orientation);        
	}
	
	protected int shipLength(){
		return 4;	
	}
	
	protected String shipName(){
		return "Battleship";	
	}
}

