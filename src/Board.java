class Board {
	private char[][] grid;     // row is y column is x
	private int gamePieces = 5;

	Board() {
		this.grid = createGrid();
	}

	protected void addHit(Point point) {
		this.grid[point.yValue()][point.xValue()] = 'X';
	}

	protected void addMiss(Point point) {
		this.grid[point.yValue()][point.xValue()] = 'M';
	}

	protected char detectShot(Point point) {
		return this.grid[point.yValue()][point.xValue()];
	}

	protected void updateGamePieces() {
		this.gamePieces -= 1;
	}

	protected int remainingGamePieces() {
		return this.gamePieces;
	}

	protected static char[][] createGrid() {
		char[][] grid = new char[10][10];
		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid[i].length; j++) {
				grid[i][j] = '_';
			}
		}
		return grid;
	}

	protected void displayBoard() {
		System.out.println("\n   0 1 2 3 4 5 6 7 8 9");
		for (int i = 0; i < this.grid.length; i++) {
			System.out.print(" " + i);
			for (int j = 0; j < this.grid[i].length; j++) {
				System.out.print(" " + this.grid[i][j]);
			}
			System.out.println();
		}
		System.out.println();
	}
	
	protected char[][] getGrid() {
		return this.grid;
	}

	protected void addShip(Ship ship) {
		switch (ship.shipOrientation()) {
		case 'V':
			if (ship.getDirection() == Ship.ShipDirection.UP) {
				for (int i = ship.yCoord(); i > ship.yCoord() - ship.shipLength(); i--) {
					this.grid[i][ship.xCoord()] = ship.shipType();
					//System.out.println(String.format("x: %d y: %d direction: up", ship.xCoord(), i));
				}
			} else if (ship.getDirection() == Ship.ShipDirection.DOWN) {
				for (int i = ship.yCoord(); i < ship.yCoord() + ship.shipLength(); i++) {
					this.grid[i][ship.xCoord()] = ship.shipType();
					//System.out.println(String.format("x: %d y: %d direction: down", ship.xCoord(), i));
				}
			}

			break;
		case 'H':
			if (ship.getDirection() == Ship.ShipDirection.LEFT) {
				for (int i = ship.xCoord(); i > ship.xCoord() - ship.shipLength(); i--) {
					this.grid[ship.yCoord()][i] = ship.shipType();
					//System.out.println(String.format("x: %d y: %d direction: left", i, ship.yCoord()));
				}
			} else if (ship.getDirection() == Ship.ShipDirection.RIGHT) {
				for (int i = ship.xCoord(); i < ship.xCoord() + ship.shipLength(); i++) {
					this.grid[ship.yCoord()][i] = ship.shipType();
					//System.out.println(String.format("x: %d y %s direction: right", i, ship.yCoord()));
				}
			}

			break;
		}

	}

	protected boolean doesPieceFit(Ship ship) {
		Ship.ShipDirection dir = Ship.ShipDirection.DEFAULT;
		boolean answer = false;

		switch (ship.shipOrientation()) {
		case 'V':
			// check if piece will fit on board, then see if space is already occupied
			if ((ship.yCoord() + ship.shipLength() - 1) <= 9) {
				// check from top to bottom
				for (int i = ship.yCoord(); i < ship.yCoord() + ship.shipLength(); i++) {
					//System.out.println(String.format("x: %d y %s direction: down", ship.xCoord(), i));
					if (this.grid[i][ship.xCoord()] != '_') { // check if space available using (x, y) coords
						answer = false;
						break;
					} else {
						answer = true;
					}
				}
				dir = Ship.ShipDirection.DOWN;
			}

			// if fails top to bottom check then check bottom to top
			if (!answer && (ship.yCoord() - ship.shipLength() - 1) >= 0) {
				// check to see if fits from bottom to top
				for (int i = ship.yCoord(); i > ship.yCoord() - ship.shipLength(); i--) {
					//System.out.println(String.format("x: %d y: %d direction: up", ship.xCoord(), i));
					if (this.grid[i][ship.xCoord()] != '_') {
						answer = false;
						break;
					} else {
						answer = true;
					}
				}
				dir = Ship.ShipDirection.UP;
			}
			break;
		case 'H':
			if ((ship.xCoord() + ship.shipLength() - 1) <= 9) {
				for (int i = ship.xCoord(); i < ship.xCoord() + ship.shipLength(); i++) {
					//System.out.println(String.format("x: %d y: %d direction: right", i, ship.yCoord()));
					if (this.grid[ship.yCoord()][i] != '_') {
						answer = false;
						break;
					} else {
						answer = true;
					}
				}
				dir = Ship.ShipDirection.RIGHT;
			}

			if (!answer && (ship.xCoord() - ship.shipLength() - 1) >= 0) {
				// check to see if fits from left to right
				for (int i = ship.xCoord(); i > ship.xCoord() - ship.shipLength(); i--) {
					//System.out.println(String.format("x: %d y: %d direction: left", i, ship.yCoord()));
					if (this.grid[ship.yCoord()][i] != '_') {
						answer = false;
						break;
					} else {
						answer = true;
					}
				}
				dir = Ship.ShipDirection.LEFT;
			}
			break;
		}
		
		if (answer) {
			ship.setDirection(dir);
		}

		return answer;
	}
}
