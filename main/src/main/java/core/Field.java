package core;

import java.util.Random;

public class Field {
    private GameState gameState;
    private final int rowCount;
    private final int columnCount;
    private int lightsCount;
    private final Tile[][] tiles;
    private final String typeOfLevel;
    private int level;

    public Field(int rowCount, int columnCount, String typeOfLevel){
        this.rowCount = rowCount;
        this.columnCount = columnCount;
        this.lightsCount = 0;
        this.gameState = GameState.PLAYING;
        this.tiles = new Tile[rowCount][columnCount];
        this.typeOfLevel = typeOfLevel;
        generate();
    }

    public Field(int rowCount, int columnCount, String typeOfLevel, int level){
        this.rowCount = rowCount;
        this.columnCount = columnCount;
        this.lightsCount = 0;
        this.gameState = GameState.PLAYING;
        this.tiles = new Tile[rowCount][columnCount];
        this.typeOfLevel = typeOfLevel;
        this.level = level;
        generate();
    }

    private void generate(){
        for(int i = 0; i < this.rowCount; i++){
            for(int j = 0; j < this.columnCount; j++){
                this.tiles[i][j] = new Tile();
            }
        }
        if(this.typeOfLevel.equals("R")) generateRandomly();
        if(this.typeOfLevel.equals("S")) generateLevel(this.level);
    }

    private void generateRandomly(){
        Random random = new Random();
        this.lightsCount = random.nextInt(this.rowCount * this.columnCount - 1) + 1;
        int lightedCount = 0;
        while(lightedCount < this.lightsCount) {
            int row = random.nextInt(this.rowCount);
            int column = random.nextInt(this.columnCount);
            if (this.tiles[row][column].getTileState() == TileState.OFF) {
                this.tiles[row][column].setTileState(TileState.ON);
                lightedCount++;
            }
        }
    }

    private void generateLevel(int level){
        Levels levelToPlay = new Levels(this);
        if(level > 0 && level < 11) levelToPlay.level(level);
        else if(level > 10) System.out.println("Congratulations you won!");
        else System.out.println("Invalid level!");
    }

    public GameState getGameState(){
        return this.gameState;
    }

    public int getRowCount() {
        return this.rowCount;
    }

    public int getColumnCount() {
        return this.columnCount;
    }

    public int getLightsCount() {
        return this.lightsCount;
    }

    public void setLightsCount(int lightsCount) {
        this.lightsCount = lightsCount;
    }

    public int getLevel() {
        return this.level;
    }

    public Tile getTile(int row, int column) {
        return this.tiles[row][column];
    }

    public boolean isSolved(){
        int lightedTiles = 0;
        for(int i = 0; i < this.rowCount; i++){
            for(int j = 0; j < this.columnCount; j++){
                if(this.tiles[i][j].getTileState() == TileState.ON) lightedTiles++;
            }
        }
        return lightedTiles == 0;
    }

    public boolean turnOff(int row, int column){
        if(row >= this.rowCount || row < 0 || column >= this.columnCount || column < 0){
            return false;
        }
        this.tiles[row][column].changeTileState();
        if(row-1 >= 0) this.tiles[row-1][column].changeTileState();
        if(row+1 < this.rowCount) this.tiles[row+1][column].changeTileState();
        if(column-1 >= 0) this.tiles[row][column-1].changeTileState();
        if(column+1 < this.columnCount) this.tiles[row][column+1].changeTileState();
        if(isSolved()) this.gameState = GameState.SOLVED;
        return true;
    }
}
