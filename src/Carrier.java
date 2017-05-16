class Carrier extends Ship{
    Carrier(){
        super(new Point(0, 0), 'C', 'H');
    }

    Carrier(Point point, char type, char orientation){
		super(point, 'C', orientation);        
	}
	
	protected int shipLength(){
		return 5;	
	}
	
	protected String shipName(){
		return "Carrier";	
	}
}

