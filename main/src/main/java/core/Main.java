package core;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("Do you want to play random level or static levels with increasing difficulty? Answer R for random level, answer S for static levels");
        Scanner sc = new Scanner(System.in);
        String type = sc.nextLine();
        if(type.equals("R") || type.equals("r")){
            Field field = new Field(5, 5, type);
            gameplay(field);
        }
        else if(type.equals("S") || type.equals("s")){
            int level = 1;
            while(level != 10){
                System.out.println("Level: " + level);
                Field field = new Field(5, 5, type, level);
                gameplay(field);
                level++;
                System.out.println();
            }
        }
    }

    private static void gameplay(Field field) {
        while(field.getGameState() != GameState.SOLVED) {
            Scanner scanner = new Scanner(System.in);
            Scanner scanner1 = new Scanner(System.in);
            int row = scanner.nextInt();
            int column = scanner1.nextInt();
            field.turnOff(row, column);
        }
    }
}
