package consoleUI;

import core.Field;
import core.GameState;
import core.TileState;
import core.TypeOfLevel;
import entity.Comment;
import entity.Rating;
import entity.Score;
import service.CommentServiceJDBC;
import service.RatingServiceJDBC;
import service.ScoreService;
import service.ScoreServiceJDBC;

import java.util.Date;
import java.util.Scanner;

public class ConsoleUI {
    private Field field;
    private String player;
    private final Scanner scanner = new Scanner(System.in);
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_BLUE = "\u001B[34m";
    private static final String RULES = "The Lights Off game has the following rules:\n" +
                                        "\n" +
                                        "● the purpose of the game is to turn off all the lights on the board\n" +
                                        "● printing coordinates of a circle switches its lightning state\n" +
                                        "● printing coordinates of a circle also switches the state of its North, South, East and West neighbors\n";
    private static final String CHOICE = "If you want to play random level enter R.\n" +
                                        "If you want to play prepared levels with increasing difficulty enter P.\n" +
                                        "If you want to stop enter X.";
    private int level = 0;
    private int score = 0;
    private final ScoreService scoreService = new ScoreServiceJDBC();

    public void start(){
        printRules();
        while(true){
            if(processInputBeginning()) break;
        }
        play();
    }

    private void printRules(){
        System.out.println(RULES);
        System.out.print("Enter your name: ");
        player = scanner.nextLine();
        System.out.println(CHOICE);
    }

    private boolean processInputBeginning(){
        String type = scanner.nextLine().toUpperCase();
        switch (type) {
            case "R":
                this.field = new Field(5, 5);
                this.field.setTypeOfLevel(TypeOfLevel.RANDOM);
                return true;
            case "P":
                level = 1;
                this.field = new Field(5, 5, level);
                this.field.setTypeOfLevel(TypeOfLevel.PREPARED);
                return true;
            case "X":
                System.exit(0);
            default:
                System.out.println("Please enter R or P!");
                break;
        }
        return false;
    }

    private void printField(){
        if(field.getTypeOfLevel() == TypeOfLevel.PREPARED) System.out.println("Level: " + level);
        for(int i = 1; i <= field.getRowCount(); i++){
            System.out.print(i + "  ");
        }
        System.out.println();
        for(int i = 0; i < field.getRowCount(); i++){
            for(int j = 0; j < field.getColumnCount(); j++){
                if(field.getTile(i, j).getTileState() == TileState.OFF) System.out.print(ANSI_BLUE + "●  " + ANSI_RESET);
                else System.out.print(ANSI_YELLOW + "●  " + ANSI_RESET);
            }
            System.out.println(i+1);
        }
    }

    private void play() {
        while(field.getGameState() != GameState.SOLVED) {
            printField();
            while(true) {
                System.out.println("Please enter row and column like this: y x\n" + "If you want to stop enter X");
                String line = scanner.nextLine().toUpperCase();
                if (line.equals("X")){
                    score += field.getScore();
                    end();
                }
                else if (line.length() == 3) {
                    int row = line.charAt(0) - '1';
                    int column = line.charAt(2) - '1';
                    if (!field.turnOff(row, column)) System.out.println("Wrong row or column!");
                    if (field.isSolved()) levelPassed();
                    break;
                }
            }
        }
    }

    private void levelPassed() {
        score += field.getScore();
        if (field.getTypeOfLevel() == TypeOfLevel.RANDOM || level == 10) end();
        else {
            printField();
            level++;
            this.field = new Field(5, 5, level);
            this.field.setTypeOfLevel(TypeOfLevel.PREPARED);
        }
    }

    private void end(){
        printField();
        if(field.getGameState() == GameState.SOLVED) System.out.println("Congratulations! You won!");
        System.out.println("Do you want to continue playing? Y/N");
        while (true) {
            if (processInputEnding()) break;
        }
        play();
    }

    private boolean processInputEnding(){
        String line = scanner.nextLine().toUpperCase();
        if (line.equals("Y")) {
            System.out.println(CHOICE);
            while (true) {
                if (processInputBeginning()) break;
            }
            return true;
        } else if (line.equals("N")) {
            service();
            System.out.println("Your score is " + scoreService.getScore("lights off", player));
            System.exit(0);
        } else {
            System.out.println("Please enter Y or N!");
        }
        return false;
    }

    private void service(){
        scoreService.addScore(new Score("lights off", player, score, new Date()));
        addComment();
        addRating();
        showTopScores();
    }

    private void showTopScores() {
        System.out.println("Do you want to see top scores? Y/N");
        while(true) {
            String line = scanner.nextLine().toUpperCase();
            if (line.equals("Y")) {
                var scores = new ScoreServiceJDBC().getTopScores("lights off");
                System.out.println("Top scores");
                System.out.println("---------------------------------------------------------------");
                for (int i = 0; i < scores.size(); i++) {
                    var score = scores.get(i);
                    System.out.printf("%d. %s %d\n", i + 1, score.getPlayer(), score.getPoints());
                }
                System.out.println("---------------------------------------------------------------");
                break;
            } else if (line.equals("N")) break;
            else System.out.println("Please enter Y or N!");
        }
    }

    private void addComment(){
        System.out.println("Do you want to add comment? Y/N");
        while(true){
            String line = scanner.nextLine().toUpperCase();
            if(line.equals("Y")) {
                System.out.println("Enter your comment");
                String comment = scanner.nextLine();
                new CommentServiceJDBC().addComment(new Comment("lights off", player, comment, new Date()));
                break;
            }
            else if(line.equals("N")) break;
            else System.out.println("Please enter Y or N!");
        }
    }

    private void addRating(){
        System.out.println("Do you want to add rating? Y/N");
        while(true){
            String line = scanner.nextLine().toUpperCase();
            if(line.equals("Y")) {
                System.out.println("Enter your rating from 0 to 5");
                int rating = scanner.nextInt();
                while(rating < 0 || rating > 5){
                    System.out.println("Invalid rating! Enter your rating from 0 to 5");
                    rating = scanner.nextInt();
                }
                new RatingServiceJDBC().setRating(new Rating("lights off", player, rating, new Date()));
                break;
            }
            else if(line.equals("N")) break;
            else System.out.println("Please enter Y or N!");
        }
    }

}
