import java.util.*;

class GamePlay {
	private static final int GRID_SIZE = 10;
	private static Stack<Point> pointStack = new Stack<Point>();
	private static ArrayList<Point> pointsNearby;
	private static Patrol cpuPatrol = new Patrol();
	private static Destroyer cpuDestroyer = new Destroyer();
	private static Submarine cpuSubmarine = new Submarine();
	private static Battleship cpuBattleship = new Battleship();
	private static Carrier cpuCarrier = new Carrier();
	private static Patrol userPatrol = new Patrol();
	private static Destroyer userDestroyer = new Destroyer();
	private static Submarine userSubmarine = new Submarine();
	private static Battleship userBattleship = new Battleship();
	private static Carrier userCarrier = new Carrier();

	public static void main(String[] args) {
		System.out.println("\nWelcome to Battleship!");
		Scanner scanner = new Scanner(System.in);
		int answer = 0;
		while (answer != 3) {
			System.out.print("\nSelect an option:\n" + "\t1. Explain Rules\n" + "\t2. Start Game\n" + "\t3. Quit\n"
					+ "Enter (1/2/3): ");
			
			try {
				answer = scanner.nextInt();
				
				switch (answer) {
				case 1:
					displayRules();
					break;
				case 2:
					startGame(scanner);
					break;
				case 3:
					break;
				default:
					System.out.println("Not a valid choice, please try again.");
					break;
				}
			} catch (InputMismatchException e) {
				System.out.println("\nYou did not enter a number. Try again.");
				scanner.next();
			}
		}
		scanner.close();
	}

	protected static void displayRules() {
		System.out.println("\nEach player has a 10x10 gameboard and 5 Battleship pieces\n"
				+ "The 5 Battleship pieces can be placed on the grid anywhere they fit\n"
				+ "Each Battleship has a given length as follows:\n" + "\n\tCarrier: 5\n" + "\tBattleship: 4\n"
				+ "\tSubmarine: 3\n" + "\tDestroyer: 3\n" + "\tPatrol Boat: 2\n\n"
				+ "The game pieces can be placed horizontally or vertically on the board\n"
				+ "Each player takes turns firing a round at the coordinates of another user's\n"
				+ "board, not knowing where the opponents Battleship pieces are placed.\n"
				+ "The goal of the game is to destroy all of the enemy's ships before they destroy yours\n");
	}

	protected static boolean isYesOrNo(char input) {
		String yesOrNo = Character.toString(input);
		return yesOrNo.matches("^[yYnN]+");
	}

	protected static void startGame(Scanner scanner) {
		Board computerBoard = new Board();
		ArrayList<Ship> cpuFleet = fleetContainer(cpuPatrol, cpuDestroyer, cpuSubmarine, cpuBattleship, cpuCarrier);
		buildBoardRandomly(computerBoard, cpuFleet);

		Board opponentHub = new Board();
		Board userBoard = new Board();
		ArrayList<Ship> userFleet = fleetContainer(userPatrol, userDestroyer, userSubmarine, userBattleship,
				userCarrier);

		char input = '0';

		do {
			System.out.print("Do you want to build your board manually(Y/N)?: ");
			input = scanner.next().toUpperCase().charAt(0);
		} while (!isYesOrNo(input));

		switch (input) {
		case 'Y':
			buildBoardManually(userBoard, userFleet, scanner);
			break;
		case 'N':
			buildBoardRandomly(userBoard, userFleet);
			break;
		}

		Board userHub = userBoard;

		while (gameStatus(userBoard, computerBoard)) {
			boolean status = true;
			displayGameBoards(userBoard, opponentHub, cpuFleet, userFleet);
			do { // user fires
				status = outgoingFire(computerBoard, opponentHub, scanner);
			} while (status);

			do { // computer fires
				status = incomingFire(userBoard, userHub, scanner);
			} while (status);
		}

		displayGameBoards(userHub, opponentHub, cpuFleet, userFleet);

		if (userBoard.remainingGamePieces() > 0) {
			System.out.println("Congratulations! You destroyed all the computer's ships!");
		} else {
			System.out.println("Too Bad...the computer destroyed you!\n");
		}
	}

	protected static int currentDirection(Point startPoint, Point endPoint) {
		int direction = 0;
		if (startPoint.xValue() == endPoint.xValue() && startPoint.yValue() > endPoint.yValue()) {
			direction = 0; // North
		} else if (startPoint.xValue() < endPoint.xValue() && startPoint.yValue() == endPoint.yValue()) {
			direction = 1; // East
		} else if (startPoint.xValue() == endPoint.xValue() && startPoint.yValue() < endPoint.yValue()) {
			direction = 2; // South
		} else if (startPoint.xValue() > endPoint.xValue() && startPoint.yValue() == endPoint.yValue()) {
			direction = 3; // West
		}
		return direction;
	}

	protected static int reverseDirection(int direction) {
		int reverse = 0;
		if (direction == 0) {
			reverse = 2;
		} else if (direction == 1) {
			reverse = 3;
		} else if (direction == 2) {
			reverse = 0;
		} else if (direction == 3) {
			reverse = 1;
		}
		return reverse;
	}

	protected static boolean incomingFire(Board userBoard, Board userHub, Scanner scanner) {
		boolean success = false;
		Random rand = new Random();
		Point nextPoint = new Point();

		// either the first time firing or just sunk a ship
		if (pointStack.empty()) {
			nextPoint.setXVal(rand.nextInt(10));
			nextPoint.setYVal(rand.nextInt(10));
		} else if (pointStack.size() == 1) {
			Point point = pointStack.peek();
			pointsNearby = point.nearbyNeighbors();
			int random = rand.nextInt(pointsNearby.size());

			if (!pointsNearby.get(random).validPoint()) {
				return true;
			}

			nextPoint.setXVal(pointsNearby.get(random).xValue());
			nextPoint.setYVal(pointsNearby.get(random).yValue());

			if (userBoard.detectShot(point) == userBoard.detectShot(nextPoint)) {
				pointStack.push(nextPoint);
				return true;
			}
		} else {
			Point endPoint = pointStack.pop();
			// if missed target but hit more than once and ship not sunk yet, reverse
			// direction
			if (userHub.detectShot(endPoint) == 'M') {
				Point startPoint = pointStack.pop();
				int direction = reverseDirection(currentDirection(startPoint, endPoint));

				while (!pointStack.empty()) {
					startPoint = pointStack.pop();
				}

				pointStack.push(startPoint); // end with only one item on stack
				pointsNearby = startPoint.nearbyNeighbors();
				nextPoint.setXVal(pointsNearby.get(direction).xValue());
				nextPoint.setYVal(pointsNearby.get(direction).yValue());
			} else if (userHub.detectShot(endPoint) == 'X') {
				// if hit target but ship not sunk yet, maintain current direction
				Point startPoint = pointStack.peek();
				pointsNearby = endPoint.nearbyNeighbors();
				int direction = currentDirection(startPoint, endPoint);
				pointStack.push(endPoint);

				if (!pointsNearby.get(direction).validPoint()) { // if reached end of grid
					direction = reverseDirection(direction);

					while (!pointStack.empty()) {
						startPoint = pointStack.pop();
					}

					pointStack.push(startPoint); // keep at least one item on stack
					pointsNearby = startPoint.nearbyNeighbors();
				}
				nextPoint.setXVal(pointsNearby.get(direction).xValue());
				nextPoint.setYVal(pointsNearby.get(direction).yValue());
			}
		}

		switch (userHub.detectShot(nextPoint)) {
		case 'M': // already shot here
		case 'X': // already shot here
			if (pointStack.size() > 1) {
				pointStack.push(nextPoint);
			}
			success = true;
			break;
		case '_':
			System.out.println("\nThe opponent fired but missed!");
			userHub.addMiss(nextPoint);

			if (pointStack.size() > 1) {
				pointStack.push(nextPoint);
			}
			break;
		case 'P':
			userHub.addHit(nextPoint);
			pointStack.push(nextPoint);
			System.out.println("\nThe opponent has hit your Patrol Boat");
			userPatrol.updateHits();

			if (userPatrol.isSunk()) {
				System.out.println("\nThe opponent sunk your Patrol Boat!");
				userHub.updateGamePieces();
				pointStack = new Stack<Point>();
			}
			break;
		case 'D':
			userHub.addHit(nextPoint);
			pointStack.push(nextPoint);
			System.out.println("\nThe opponent has hit your Destroyer");
			userDestroyer.updateHits();

			if (userDestroyer.isSunk()) {
				System.out.println("\nThe opponent sunk your Destroyer!");
				userHub.updateGamePieces();
				pointStack = new Stack<Point>();
			}
			break;
		case 'S':
			userHub.addHit(nextPoint);
			pointStack.push(nextPoint);
			System.out.println("\nThe opponent has hit your Submarine");
			userSubmarine.updateHits();

			if (userSubmarine.isSunk()) {
				System.out.println("\nThe opponent sunk your Submarine!");
				userHub.updateGamePieces();
				pointStack = new Stack<Point>();
			}
			break;
		case 'B':
			userHub.addHit(nextPoint);
			pointStack.push(nextPoint);
			System.out.println("\nThe opponent has hit your Battleship");
			userBattleship.updateHits();

			if (userBattleship.isSunk()) {
				System.out.println("\nThe opponent sunk your Battleship!");
				userHub.updateGamePieces();
				pointStack = new Stack<Point>();
			}
			break;
		case 'C':
			userHub.addHit(nextPoint);
			pointStack.push(nextPoint);
			System.out.println("\nThe opponent has hit your Carrier");
			userCarrier.updateHits();

			if (userCarrier.isSunk()) {
				System.out.println("\nThe opponent sunk your Carrier!");
				userHub.updateGamePieces();
				pointStack = new Stack<Point>();
			}
			break;
		}
		return success;
	}

	protected static boolean outgoingFire(Board computerBoard, Board opponentHub, Scanner scanner) {
		int x, y = 0;

		do {
			System.out.print("\nEnter x coordinate to fire shot: ");
			x = scanner.nextInt();
		} while (x < 0 || x > 9);

		do {
			System.out.print("Enter y coordinate to fire shot: ");
			y = scanner.nextInt();
		} while (y < 0 || y > 9);

		Point firePoint = new Point(x, y);

		switch (computerBoard.detectShot(firePoint)) {
		case 'M':
			System.out.println("\nYou've already fired in that area! Try again.");
			return true;
		case 'X':
			System.out.println("\nYou've already fired in that area! Try again.");
			return true;
		case '_':
			System.out.println("\nYou missed the target!");
			opponentHub.addMiss(firePoint);
			computerBoard.addMiss(firePoint);
			break;
		case 'P':
			System.out.println("\nYou hit the opponents Patrol Boat!");
			opponentHub.addHit(firePoint);
			computerBoard.addHit(firePoint);
			cpuPatrol.updateHits();
			if (cpuPatrol.isSunk()) {
				System.out.println("\nYou sunk the opponents Patrol Boat!");
				computerBoard.updateGamePieces();
			}
			break;
		case 'D':
			System.out.println("\nYou hit the opponents Destroyer!");
			opponentHub.addHit(firePoint);
			computerBoard.addHit(firePoint);
			cpuDestroyer.updateHits();
			if (cpuDestroyer.isSunk()) {
				System.out.println("\nYou sunk the opponents Destroyer!");
				computerBoard.updateGamePieces();
			}
			break;
		case 'S':
			System.out.println("\nYou hit the opponents Submarine!");
			opponentHub.addHit(firePoint);
			computerBoard.addHit(firePoint);
			cpuSubmarine.updateHits();
			if (cpuSubmarine.isSunk()) {
				System.out.println("\nYou sunk the opponents Submarine!");
				computerBoard.updateGamePieces();
			}
			break;
		case 'B':
			System.out.println("\nYou hit the opponents Battleship!");
			opponentHub.addHit(firePoint);
			computerBoard.addHit(firePoint);
			cpuBattleship.updateHits();
			if (cpuBattleship.isSunk()) {
				System.out.println("\nYou sunk the opponents Battleship!");
				computerBoard.updateGamePieces();
			}
			break;
		case 'C':
			System.out.println("\nYou hit the opponents Carrier!");
			opponentHub.addHit(firePoint);
			computerBoard.addHit(firePoint);
			cpuCarrier.updateHits();
			if (cpuCarrier.isSunk()) {
				System.out.println("\nYou sunk the opponents Carrier!");
				computerBoard.updateGamePieces();
			}
			break;
		}
		return false;
	}

	protected static boolean gameStatus(Board userBoard, Board computerBoard) {
		return (userBoard.remainingGamePieces() != 0 && computerBoard.remainingGamePieces() != 0);
	}

	protected static void displayShipsRemaining(ArrayList<Ship> fleet) {
		String shipsRemaining = "";
		String shipsSunk = "";
		System.out.print("Ships remaining: ");

		for (Ship ship : fleet) {
			if (!ship.isSunk()) {
				shipsRemaining += ship.shipName() + " ";
			} else {
				shipsSunk += ship.shipName() + " ";
			}
		}
		System.out.print(shipsRemaining + "\n");
		System.out.print("Ships sunk: ");
		System.out.println(shipsSunk);
	}

	protected static void displayGameBoards(Board userBoard, Board opponentBoard, ArrayList<Ship> cpuFleet,
			ArrayList<Ship> userFleet) {
		System.out.println("\nOpponent's Board:");
		displayShipsRemaining(cpuFleet);
		opponentBoard.displayBoard();

		System.out.println("\nYour Board:");
		displayShipsRemaining(userFleet);
		userBoard.displayBoard();
	}

	protected static boolean isValidOrientation(char orientation) {
		String repStr = Character.toString(orientation);
		return repStr.matches("^[vVhH]+");
	}

	protected static void buildBoardManually(Board board, ArrayList<Ship> listOfShips, Scanner scanner) {
		String request = "";
		boolean gamePieceSet;

		for (Ship ship : listOfShips) {
			do {
				request = "";
				gamePieceSet = false;

				board.displayBoard();

				int xCoord, yCoord = -1;
				char orientation = '0';

				do {
					request = String.format(
							"Enter the x coordinate for the %s ship with length %d (value between 0 and 9): ",
							ship.shipName(), ship.shipLength());
					System.out.print(request);
					xCoord = scanner.nextInt();
				} while (xCoord < 0 || xCoord > 9);

				do {
					request = String.format(
							"Enter the y coordinate for the %s ship with length %d (value between 0 and 9): ",
							ship.shipName(), ship.shipLength());
					System.out.print(request);
					yCoord = scanner.nextInt();
				} while (yCoord < 0 || yCoord > 9);

				do {
					request = String.format("Enter the orientation for the %s ship(Vertical[V]/Horizontal[H]): ",
							ship.shipName());
					System.out.print(request);
					orientation = scanner.next().toUpperCase().charAt(0);
				} while (!isValidOrientation(orientation));

				ship.setPoint(new Point(xCoord, yCoord));
				ship.setOrientation(orientation);
				
				if (board.doesPieceFit(ship)) {
					gamePieceSet = true;
					board.addShip(ship);
					break;
				}

			} while (!gamePieceSet);
		}
	}

	protected static void buildBoardRandomly(Board board, ArrayList<Ship> listOfShips) {
		Random randomBool = new Random();
		boolean gamePieceSet;

		for (Ship ship : listOfShips) {
			do {
				gamePieceSet = false;
				char orientation = randomBool.nextBoolean() ? 'V' : 'H';
				ship.setPoint(randomPoint());
				ship.setOrientation(orientation);

				if (board.doesPieceFit(ship)) {
					gamePieceSet = true;
					board.addShip(ship);
					break;
				}
			} while (!gamePieceSet);
		}
	}

	protected static ArrayList<Ship> fleetContainer(Ship Patrol, Ship Destroyer, Ship Submarine, Ship Battleship,
			Ship Carrier) {
		ArrayList<Ship> listOfShips = new ArrayList<Ship>();
		listOfShips.add(Patrol);
		listOfShips.add(Destroyer);
		listOfShips.add(Submarine);
		listOfShips.add(Battleship);
		listOfShips.add(Carrier);
		return listOfShips;
	}

	protected static Point randomPoint() {
		Random randomInt = new Random();
		int randomX = randomInt.nextInt(GRID_SIZE);
		int randomY = randomInt.nextInt(GRID_SIZE);
		return new Point(randomX, randomY);
	}
}
