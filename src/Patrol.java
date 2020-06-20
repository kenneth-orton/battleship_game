class Patrol extends Ship {
	Patrol() {
		super(new Point(0, 0), 'P', 'H');
	}

	Patrol(Point point, char type, char orientation) {
		super(point, 'P', orientation);
	}

	protected int shipLength() {
		return 2;
	}

	protected String shipName() {
		return "Patrol";
	}
}
