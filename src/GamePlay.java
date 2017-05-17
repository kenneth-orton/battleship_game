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

        
    public static void main(String[] args){
        System.out.println("\nWelcome to Battleship!");
        Scanner scanner = new Scanner(System.in);
        int answer = 0;
        while(answer != 3){
            System.out.print("Select an option:\n" +
                               "\t1. Explain Rules\n" +
                               "\t2. Start Game\n" + 
                               "\t3. Quit\n" +
                               "Enter (1/2/3): ");
            answer = scanner.nextInt();
            switch(answer){
                case 1:
                    displayRules();
                    break;
                case 2:
                    startGame();
                    break;
                case 3: 
                    break;
                default:
                    System.out.println("Not a valid choice, please try again.");
                    break;
            }
        }
    }

    protected static void displayRules(){
        System.out.println("\nEach player has a 10x10 gameboard and 5 Battleship pieces\n" +
                           "The 5 Battleship pieces can be placed on the grid anywhere they fit\n" +
                           "Each Battleship has a given length as follows:\n" + 
                           "\n\tCarrier: 5\n" +
                           "\tBattleship: 4\n" + 
                           "\tSubmarine: 3\n" + 
                           "\tDestroyer: 3\n" + 
                           "\tPatrol Boat: 2\n\n" + 
                           "The game pieces can be placed horizontally or vertically on the board\n" + 
                           "Each player takes turns firing a round at the coordinates of another user's\n" + 
                           "board, not knowing where the opponents Battleship pieces are placed.\n" + 
                           "The goal of the game is to destroy all of the enemie's ships before they destroy yours\n");
    }

    protected static void startGame(){
        Board computerBoard = new Board();
        ArrayList<Ship> cpuFleet = fleetContainer(cpuPatrol, cpuDestroyer, cpuSubmarine, cpuBattleship, cpuCarrier);
        buildBoardRandomly(computerBoard, cpuFleet);

        Board opponentHub = new Board();
        Board userBoard = new Board();
        ArrayList<Ship> userFleet = fleetContainer(userPatrol, userDestroyer, userSubmarine, userBattleship, userCarrier);
        
        Scanner scanner = new Scanner(System.in);
        System.out.print("Do you want to build your board manually(Y/N)?: ");
        char input = scanner.next("[a-zA-Z]").toUpperCase().charAt(0);

        switch(input){
            case 'Y':
                buildBoardManually(userBoard, userFleet);
                break;
            case 'N':
                buildBoardRandomly(userBoard, userFleet);
                break;
        }
        
        while(gameStatus(userBoard, computerBoard)){
            boolean status = true;
            displayGameBoards(userBoard, opponentHub);
            do{ //user fires
                status = outgoingFire(computerBoard, opponentHub);
            }while(status);

            do{ //cpu fires
                status = incomingFire(userBoard);
            }while(status);
        }
        displayGameBoards(userBoard, opponentHub);
        if(userBoard.remainingGamePieces() > 0){
            System.out.println("Congratulations! You destroyed all the computer's ships!");
        }else{
            System.out.println("Too Bad...the computer destroyed you!\n");
        }
    }
    
    protected static int currentDirection(Point startPoint, Point endPoint){
        int direction = 0;
        if(startPoint.xValue() == endPoint.xValue() && startPoint.yValue() > endPoint.yValue()){
            direction = 0; // North
        }else if(startPoint.xValue() < endPoint.xValue() && startPoint.yValue() == endPoint.yValue()){
            direction = 1; // East
        }else if(startPoint.xValue() == endPoint.xValue() && startPoint.yValue() < endPoint.yValue()){
            direction = 2; // South
        }else if(startPoint.xValue() > endPoint.xValue() && startPoint.yValue() == endPoint.yValue()){
            direction = 3; // West
        }
        return direction;
    }

    protected static int reverseDirection(int direction){
        int reverse = 0;
        if(direction == 0){
            reverse = 2;
        }else if(direction == 1){
            reverse = 3;
        }else if(direction == 2){
            reverse = 0;
        }else if(direction == 3){
            reverse = 1;
        }
        return reverse;
    }

    protected static boolean incomingFire(Board board){
        Random rand = new Random();
        Point nextPoint = new Point();
        //either the first time firing or just sunk a ship
        if(pointStack.empty()){
            nextPoint.setXVal(rand.nextInt(10));
            nextPoint.setYVal(rand.nextInt(10));
        }else if(pointStack.size() == 1){
            Point point = pointStack.peek();
            pointsNearby = point.nearbyNeighbors();
            int random = rand.nextInt(pointsNearby.size());
            if(!pointsNearby.get(random).validPoint()){
                return true;
            }
            nextPoint.setXVal(pointsNearby.get(random).xValue());
            nextPoint.setYVal(pointsNearby.get(random).yValue());
        }else{
            Point endPoint = pointStack.pop();
            // if missed target but hit more than once and ship not sunk yet, reverse direction
            if(board.detectShot(endPoint) == 'M'){
                Point startPoint = pointStack.pop();
                int direction = reverseDirection(currentDirection(startPoint, endPoint));
                while(!pointStack.empty()){
                    startPoint = pointStack.pop();
                }
                pointStack.push(startPoint);
                pointsNearby = startPoint.nearbyNeighbors();
                nextPoint.setXVal(pointsNearby.get(direction).xValue());
                nextPoint.setYVal(pointsNearby.get(direction).yValue());
            }else if(board.detectShot(endPoint) == 'X'){
                // if hit target but ship not sunk yet, maintain current direction
                Point startPoint = pointStack.peek();
                pointsNearby = endPoint.nearbyNeighbors();
                int direction = currentDirection(startPoint, endPoint);
                pointStack.push(endPoint);
                if(!pointsNearby.get(direction).validPoint()){ // if reached end of grid
                    direction = reverseDirection(direction);
                    while(!pointStack.empty()){
                        startPoint = pointStack.pop();
                    }
                    pointStack.push(startPoint); // keep at least one item on stack
                    pointsNearby = startPoint.nearbyNeighbors();
                }
                nextPoint.setXVal(pointsNearby.get(direction).xValue());
                nextPoint.setYVal(pointsNearby.get(direction).yValue());
            }
        }
        switch(board.detectShot(nextPoint)){
            case 'M':
                if(pointStack.size() > 1){
                    pointStack.push(nextPoint);
                }
                return true;
            case 'X':
                if(pointStack.size() > 1){
                    pointStack.push(nextPoint);
                }
                return true;
            case '_':
                System.out.println("\nThe opponent fired but missed!");
                board.addMiss(nextPoint);
                if(pointStack.size() > 1){
                    pointStack.push(nextPoint);
                }
                break;
            case 'P':
                board.addHit(nextPoint);
                pointStack.push(nextPoint);
                System.out.println("\nThe opponent has hit your Patrol Boat");
                userPatrol.updateHits();
                if(userPatrol.isSunk()){
                    System.out.println("\nThe opponent sunk your Patrol Boat!");
                    board.updateGamePieces();
                    pointStack = new Stack<Point>();
                }
                break;
            case 'D':
                board.addHit(nextPoint);
                pointStack.push(nextPoint);
                System.out.println("\nThe opponent has hit your Destroyer");
                userDestroyer.updateHits();
                if(userDestroyer.isSunk()){
                    System.out.println("\nThe opponent sunk your Destroyer!");
                    board.updateGamePieces();
                    pointStack = new Stack<Point>();
                }
                break;
            case 'S':
                board.addHit(nextPoint);
                pointStack.push(nextPoint);
                System.out.println("\nThe opponent has hit your Submarine");
                userSubmarine.updateHits();
                if(userSubmarine.isSunk()){
                    System.out.println("\nThe opponent sunk your Submarine!");
                    board.updateGamePieces();
                    pointStack = new Stack<Point>();
                }
                break;
            case 'B':
                board.addHit(nextPoint);
                pointStack.push(nextPoint);
                System.out.println("\nThe opponent has hit your Battleship");
                userBattleship.updateHits();
                if(userBattleship.isSunk()){
                    System.out.println("\nThe opponent sunk your Battleship!");
                    board.updateGamePieces();
                    pointStack = new Stack<Point>();
                }
                break;
            case 'C':
                board.addHit(nextPoint);
                pointStack.push(nextPoint);
                System.out.println("\nThe opponent has hit your Carrier");
                userCarrier.updateHits();
                if(userCarrier.isSunk()){
                    System.out.println("\nThe opponent sunk your Carrier!");
                    board.updateGamePieces();
                    pointStack = new Stack<Point>();
                }
                break;
        }
        return false;
    }

    protected static boolean outgoingFire(Board computerBoard, Board opponentHub){
        Scanner scanner = new Scanner(System.in);
        System.out.print("\nEnter x coordinate to fire shot: ");
        int x = scanner.nextInt();
        System.out.print("Enter y coordinate to fire shot: ");
        int y = scanner.nextInt();
        Point firePoint = new Point(x, y);

        switch(computerBoard.detectShot(firePoint)){
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
                if(cpuPatrol.isSunk()){
                    System.out.println("\nYou sunk the opponents Patrol Boat!");
                    computerBoard.updateGamePieces();
                }
                break;
            case 'D':
                System.out.println("\nYou hit the opponents Destroyer!");
                opponentHub.addHit(firePoint);
                computerBoard.addHit(firePoint);
                cpuDestroyer.updateHits();
                if(cpuDestroyer.isSunk()){
                    System.out.println("\nYou sunk the opponents Destroyer!");
                    computerBoard.updateGamePieces();
                }
                break;
            case 'S':
                System.out.println("\nYou hit the opponents Submarine!");
                opponentHub.addHit(firePoint);
                computerBoard.addHit(firePoint);
                cpuSubmarine.updateHits();
                if(cpuSubmarine.isSunk()){
                    System.out.println("\nYou sunk the opponents Submarine!");
                    computerBoard.updateGamePieces();
                }
                break;
            case 'B':
                System.out.println("\nYou hit the opponents Battleship!");
                opponentHub.addHit(firePoint);
                computerBoard.addHit(firePoint);
                cpuBattleship.updateHits();
                if(cpuBattleship.isSunk()){
                    System.out.println("\nYou sunk the opponents Battleship!");
                    computerBoard.updateGamePieces();
                }
                break;
            case 'C':
                System.out.println("\nYou hit the opponents Carrier!");
                opponentHub.addHit(firePoint);
                computerBoard.addHit(firePoint);
                cpuCarrier.updateHits();
                if(cpuCarrier.isSunk()){
                    System.out.println("\nYou sunk the opponents Carrier!");
                    computerBoard.updateGamePieces();
                }
                break;
        }
        return false;
    }

    protected static boolean gameStatus(Board userBoard, Board computerBoard){
        return(userBoard.remainingGamePieces() != 0 && computerBoard.remainingGamePieces() != 0);
    }

    protected static void displayGameBoards(Board userBoard, Board opponentBoard){
        System.out.println("\nOpponent's Board");
        opponentBoard.displayBoard();
        System.out.println("\nYour Board");
        userBoard.displayBoard();
    }

    protected static void buildBoardManually(Board board, ArrayList<Ship> listOfShips){
        Scanner scanner = new Scanner(System.in);
        for(Ship ship : listOfShips){
            board.displayBoard();
            while(true){
                System.out.print("\nEnter the x coordinate for the " + ship.shipName() + ": ");
                int xCoord = scanner.nextInt();
                System.out.print("Enter the y coordinate for the " + ship.shipName() + ": ");
                int yCoord = scanner.nextInt();
                System.out.print("Enter the orientation for the " + ship.shipName() +"(Vertical[V]/Horizontal[H]): ");
                char orientation = scanner.next("[a-zA-Z]").toUpperCase().charAt(0);
                ship.setPoint(new Point(xCoord, yCoord));
                ship.setOrientation(orientation);
                if(board.doesPieceFit(ship)){
                    board.addShip(ship); 
                    break;
                }
            }
        }
    }

    protected static void buildBoardRandomly(Board board, ArrayList<Ship> listOfShips){
        Random randomBool = new Random();
        for(Ship ship : listOfShips){
            while(true){
                char orientation = randomBool.nextBoolean() ? 'V' : 'H';
                ship.setPoint(randomPoint());
                ship.setOrientation(orientation);
                if(board.doesPieceFit(ship)){
                    board.addShip(ship); 
                    break;
                }
            }
        }
    }

    protected static ArrayList<Ship> fleetContainer(Ship Patrol, Ship Destroyer, Ship Submarine, Ship Battleship, Ship Carrier){
        ArrayList<Ship> listOfShips = new ArrayList<Ship>();
        listOfShips.add(Patrol);
        listOfShips.add(Destroyer);
        listOfShips.add(Submarine);
        listOfShips.add(Battleship);
        listOfShips.add(Carrier);
        return listOfShips;
    }

    protected static Point randomPoint(){
        Random randomInt = new Random();
        int randomX = randomInt.nextInt(GRID_SIZE);
        int randomY = randomInt.nextInt(GRID_SIZE);
        return new Point(randomX, randomY);
    }
}
