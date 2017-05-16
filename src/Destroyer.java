class Destroyer extends Ship{
    Destroyer(){
        super(new Point(0, 0), 'D', 'H');
    }

    Destroyer(Point point, char type, char orientation){
		super(point, 'D', orientation);        
	}
	
	protected int shipLength(){
		return 3;	
	}
	
	protected String shipName(){
		return "Destroyer";	
	}
}

