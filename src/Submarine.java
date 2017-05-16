class Submarine extends Ship{
    Submarine(){
        super(new Point(0, 0), 'S', 'H');
    }

    Submarine(Point point, char type, char orientation){
		super(point, 'S', orientation);        
	}
	
	protected int shipLength(){
		return 3;	
	}
	
	protected String shipName(){
		return "Submarine";	
	}
}

