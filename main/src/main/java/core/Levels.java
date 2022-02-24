package core;

import java.io.*;

public class Levels {
    private final Field field;

    public Levels(Field field){
        this.field = field;
    }

    private String getPathname(int level){
        switch(level){
            case 1: return "./main/src/main/resources/level1.txt";
            case 2: return "./main/src/main/resources/level2.txt";
            case 3: return "./main/src/main/resources/level3.txt";
            case 4: return "./main/src/main/resources/level4.txt";
            case 5: return "./main/src/main/resources/level5.txt";
            case 6: return "./main/src/main/resources/level6.txt";
            case 7: return "./main/src/main/resources/level7.txt";
            case 8: return "./main/src/main/resources/level8.txt";
            case 9: return "./main/src/main/resources/level9.txt";
            case 10: return "./main/src/main/resources/level10.txt";
            default: return null;
        }
    }

    public void level(int level){
        try{
            File level1 = new File(getPathname(level));
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
        catch (FileNotFoundException e){
            System.out.println("File not found");
        }
        catch (IOException e){
            System.out.println("Error");
        }
    }

}
