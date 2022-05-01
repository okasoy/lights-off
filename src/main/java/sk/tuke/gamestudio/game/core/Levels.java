package sk.tuke.gamestudio.game.core;

import java.io.*;
import java.util.Scanner;

public class Levels {
    private final Field field;

    public Levels(Field field){
        this.field = field;
    }

    private String getPathname(int level){
        switch(level){
            case 1: return "./src/main/resources/levels/level1.txt";
            case 2: return "./src/main/resources/levels/level2.txt";
            case 3: return "./src/main/resources/levels/level3.txt";
            case 4: return "./src/main/resources/levels/level4.txt";
            case 5: return "./src/main/resources/levels/level5.txt";
            case 6: return "./src/main/resources/levels/level6.txt";
            case 7: return "./src/main/resources/levels/level7.txt";
            case 8: return "./src/main/resources/levels/level8.txt";
            case 9: return "./src/main/resources/levels/level9.txt";
            case 10: return "./src/main/resources/levels/level10.txt";
            default: return null;
        }
    }

    public void level(int level){
        try{
            File level1 = new File(getPathname(level));
            checkSize(level1);
            BufferedReader bufferedReader = new BufferedReader(new FileReader(level1));
            char symbol;
            for(int j = 0; j < this.field.getRowCount(); j++){
                for(int k = 0; k < this.field.getColumnCount(); k++){
                    symbol = (char) bufferedReader.read();
                    if(symbol == '1'){
                        this.field.getTile(j,k).setTileState(TileState.ON);
                        this.field.setLightsCount(this.field.getLightsCount() + 1);
                    }
                }
                bufferedReader.readLine();
            }
        }
        catch (IllegalArgumentException | IOException e){
            e.printStackTrace();
        }
    }

    private void checkSize(File file){
        int size = 0;
        try (Scanner scanner = new Scanner(file)){
            while(scanner.hasNextLine()){
                String line = scanner.nextLine();
                size += line.length();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(size != this.field.getRowCount()*this.field.getColumnCount()) throw new IllegalArgumentException("Wrong size of field");
    }

}
