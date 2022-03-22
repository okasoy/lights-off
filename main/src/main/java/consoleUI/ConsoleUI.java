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
    private String yesOrNo;
    private int level = 0;
    private int score = 0;
    private final ScoreService scoreService = new ScoreServiceJDBC();

    public void start(){
        printRules();
        while(processInputBeginning()) System.out.println("Please enter R or P!");
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
                return false;
            case "P":
                level = 1;
                this.field = new Field(5, 5, level);
                this.field.setTypeOfLevel(TypeOfLevel.PREPARED);
                return false;
            case "X":
                System.exit(0);
            default:
                return true;
        }
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
            System.out.println("Congratulations! You solved the level!");
            System.out.println();
            level++;
            this.field = new Field(5, 5, level);
            this.field.setTypeOfLevel(TypeOfLevel.PREPARED);
        }
    }

    private boolean processInputYesOrNo(){
        yesOrNo = scanner.nextLine().toUpperCase();
        return !yesOrNo.equals("Y") && !yesOrNo.equals("N");
    }

    private void end(){
        printField();
        if(field.getGameState() == GameState.SOLVED && level != 10) System.out.println("Congratulations! You solved the level!");
        if(field.getGameState() == GameState.SOLVED && level == 10) System.out.println("Congratulations! You solved all levels!");
        System.out.println("Do you want to continue playing? Y/N");
        while(processInputYesOrNo()) System.out.println("Please enter Y or N!");
        if (yesOrNo.equals("Y")) {
            System.out.println(CHOICE);
            while(processInputBeginning()) System.out.println("Please enter R or P!");
            play();
        }
        else if (yesOrNo.equals("N")) {
            service();
            System.out.println("Your score is " + scoreService.getScore("lights off", player));
            System.exit(0);
        }
    }

    private void service(){
        scoreService.addScore(new Score("lights off", player, score, new Date()));
        addComment();
        addRating();
        showTopScores();
    }

    private void showTopScores() {
        System.out.println("Do you want to see top scores? Y/N");
        while(processInputYesOrNo()) System.out.println("Please enter Y or N!");
        if (yesOrNo.equals("Y")) {
            var scores = new ScoreServiceJDBC().getTopScores("lights off");
            System.out.println("Top scores");
            System.out.println("---------------------------------------------------------------");
            for (int i = 0; i < scores.size(); i++) {
                var score = scores.get(i);
                System.out.printf("%d. %s %d\n", i + 1, score.getPlayer(), score.getPoints());
            }
            System.out.println("---------------------------------------------------------------");
        }
    }

    private void addComment(){
        System.out.println("Do you want to add comment? Y/N");
        while(processInputYesOrNo()) System.out.println("Please enter Y or N!");
        if (yesOrNo.equals("Y")) {
            System.out.println("Enter your comment");
            String comment = scanner.nextLine();
            new CommentServiceJDBC().addComment(new Comment("lights off", player, comment, new Date()));
        }
    }

    private void addRating(){
        System.out.println("Do you want to add rating? Y/N");
        while(processInputYesOrNo()) System.out.println("Please enter Y or N!");
        if (yesOrNo.equals("Y")) {
            System.out.println("Enter your rating from 0 to 5");
            int rating = scanner.nextInt();
            while(rating < 0 || rating > 5){
                System.out.println("Invalid rating! Enter your rating from 0 to 5");
                rating = scanner.nextInt();
            }
            new RatingServiceJDBC().setRating(new Rating("lights off", player, rating, new Date()));
        }
    }

}
