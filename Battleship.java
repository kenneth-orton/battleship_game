import java.util.Scanner;
import java.util.Random;

class Battleship {

    public static void main(String[] args){
        System.out.println("Welcome to Battleship!");
        Scanner response = new Scanner(System.in);
        int answer = 0;
        while(answer != 3){
            System.out.print("Select an option:\n" +
                               "\t1. Explain Rules\n" +
                               "\t2. Start Game\n" + 
                               "\t3. Quit\n" +
                               "Enter (1/2/3): ");
            answer = response.nextInt();
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

        //use two boards, one representing your board; the other representing opponents board;
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
                           "board, not knowing where the other users Battleship pieces are placed\n" + 
                           "The goal of the game is to destroy all of the enemies ships before they destroy yours\n");
    }

    protected static void startGame(){
        char[] shipTypes = {'P', 'D', 'S', 'B', 'C'};
        createComputerBoard(shipTypes);
        createUserBoard(shipTypes);
    }
    
    protected static void createUserBoard(char[] shipTypes){
        Board userBoard = new Board();
        for(char type : shipTypes){
            switch(type){
                case 'P':
                    userBoardCases("Patrol Boat", 'P', userBoard);
                    break;
                case 'D':
                    userBoardCases("Destroyer", 'D', userBoard);
                    break;
                case 'S':
                    userBoardCases("Submarine", 'S', userBoard);
                    break;
                case 'B':
                    userBoardCases("Battleship", 'B', userBoard);
                    break;
                case 'C':
                    userBoardCases("Carrier", 'C', userBoard);
                    break;
            }
        }
    }

    protected static void userBoardCases(String typeName, char type, Board userBoard){
        Scanner input = new Scanner(System.in);
        int xCoord = 0;
        int yCoord = 0;
        char orientation = 'a';
        while(true){
            System.out.print("Enter the x coordinate for the " + typeName + ": ");
            xCoord = input.nextInt();
            System.out.print("Enter the y coordinate for the " + typeName + ": ");
            yCoord = input.nextInt();
            System.out.print("Enter the orientation for the " + typeName +"(Vertical[V]/Horizontal[H]): ");
            orientation = input.next().toUpperCase().charAt(0);
            Ship ship = new Ship(xCoord, yCoord, type, orientation);
            if(userBoard.doesPieceFit(ship)){
                userBoard.addShip(ship); 
                break;
            }
        }
    }

    protected static void createComputerBoard(char[] shipTypes){
        Board computerBoard = new Board();
        for(char type : shipTypes){
            while(true){
                Random rand = new Random();
                int randX = rand.nextInt(10);
                int randY = rand.nextInt(10);
                char orientation = rand.nextBoolean() ? 'V' : 'H';
                Ship ship = new Ship(randX, randY, type, orientation);
                if(computerBoard.doesPieceFit(ship)){
                    computerBoard.addShip(ship); 
                    break;
                }
            }
        }
    }
}
