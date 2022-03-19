package tests;

import core.Field;
import core.TileState;
import core.TypeOfLevel;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class FieldTest {
    private final Field fieldRandom;
    private final Field fieldLevels;
    private final int rowCount;
    private final int columnCount;

    public FieldTest() {
        this.rowCount = 5;
        this.columnCount = 5;
        this.fieldRandom = new Field(this.rowCount, this.columnCount);
        this.fieldRandom.setTypeOfLevel(TypeOfLevel.RANDOM);
        this.fieldLevels = new Field(this.rowCount, this.columnCount, 1);
        this.fieldLevels.setTypeOfLevel(TypeOfLevel.PREPARED);
    }

    @Test
    public void checkLightsCountInRandomLevel() {
        int lightsCounter = 0;
        for(int row = 0; row < this.rowCount; row++){
            for(int column = 0; column < this.columnCount; column++){
                if(this.fieldRandom.getTile(row, column).getTileState() == TileState.ON) lightsCounter++;
            }
        }
        assertEquals(this.fieldRandom.getLightsCount(), lightsCounter, "Field was initialized incorrectly - " +
                "a different amount of mines was counted in the field than amount given in the constructor.");
    }

    @Test
    public void checkLightsCountInStaticLevel() {
        int lightsCounter = 0;
        for(int row = 0; row < this.rowCount; row++){
            for(int column = 0; column < this.columnCount; column++){
                if(this.fieldLevels.getTile(row, column).getTileState() == TileState.ON) lightsCounter++;
            }
        }
        assertEquals(this.fieldLevels.getLightsCount(), lightsCounter, "Field was initialized incorrectly - " +
                "a different amount of mines was counted in the field than amount given in the constructor.");
    }

    @Test
    public void checkInvalidRowAndColumnInTurnOff() {
        Field fieldTest = this.fieldRandom;
        this.fieldRandom.turnOff(-1, 35);
        for(int row = 0; row < this.rowCount; row++){
            for(int column = 0; column < this.columnCount; column++){
                assertEquals(this.fieldRandom.getTile(row, column).getTileState(), fieldTest.getTile(row, column).getTileState());
            }
        }
    }

    @Test
    public void checkValidRowAndColumnInTurnOff() {
        TileState tileState1 = this.fieldRandom.getTile(1, 3).getTileState();
        TileState tileState2 = this.fieldRandom.getTile(0, 3).getTileState();
        TileState tileState3 = this.fieldRandom.getTile(2, 3).getTileState();
        TileState tileState4 = this.fieldRandom.getTile(1, 2).getTileState();
        TileState tileState5 = this.fieldRandom.getTile(1, 4).getTileState();
        this.fieldRandom.turnOff(1, 3);
        assertNotEquals(tileState1, this.fieldRandom.getTile(1, 3).getTileState());
        assertNotEquals(tileState2, this.fieldRandom.getTile(0, 3).getTileState());
        assertNotEquals(tileState3, this.fieldRandom.getTile(2, 3).getTileState());
        assertNotEquals(tileState4, this.fieldRandom.getTile(1, 2).getTileState());
        assertNotEquals(tileState5, this.fieldRandom.getTile(1, 4).getTileState());
    }
}
