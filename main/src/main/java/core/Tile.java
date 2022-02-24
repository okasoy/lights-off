package core;

public class Tile {
    private TileState tileState;

    public Tile(){
        tileState = TileState.OFF;
    }

    public TileState getTileState(){
        return this.tileState;
    }

    public void setTileState(TileState tileState) {
        this.tileState = tileState;
    }

    public void changeTileState(){
        if(this.tileState == TileState.OFF) this.tileState = TileState.ON;
        else this.tileState = TileState.OFF;
    }
}
