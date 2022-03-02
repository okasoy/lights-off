package consoleUI;

import core.Field;
import core.GameState;
import core.TileState;

import java.util.Scanner;

public class ConsoleUI {
    private Field field;

    public void start(){
        System.out.println("If you want to play random level enter R. If you want to play static levels with increasing difficulty enter S. If you want to stop enter X.");
        while(true){
            Scanner sc = new Scanner(System.in);
            String type = sc.nextLine().toUpperCase();
            if(type.equals("R")){
                this.field = new Field(5, 5, type);
                gameplay();
                break;
            }
            else if(type.equals("S")){
                int level = 1;
                while (level != 10) {
                    System.out.println("Level: " + level);
                    this.field = new Field(5, 5, type, level);
                    gameplay();
                    level++;
                    System.out.println();
                }
                break;
            }

            else if(type.equals("X")) System.exit(0);
            else System.out.println("Please enter R or S!");
        }
    }

    private void printField(){
        for(int i = 0; i < field.getRowCount(); i++){
            for(int j = 0; j < field.getColumnCount(); j++){
                if(field.getTile(i, j).getTileState() == TileState.OFF) System.out.print("0  ");
                else System.out.print("1  ");
            }
            System.out.println();
        }
    }

    public void gameplay() {
        while(field.getGameState() != GameState.SOLVED) {
            printField();
            Scanner scanner = new Scanner(System.in);
            Scanner scanner1 = new Scanner(System.in);
            String exit = scanner.nextLine().toUpperCase();
            if(exit.equals("X")) System.exit(0);
            int row = scanner.nextInt();
            int column = scanner1.nextInt();
            field.turnOff(row, column);
            if(field.isSolved()) printField();
        }
    }
}
