package tests;

import core.Field;
import core.TileState;
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
        String random = "R";
        String level = "S";
        this.fieldRandom = new Field(this.rowCount, this.columnCount, random);
        this.fieldLevels = new Field(this.rowCount, this.columnCount, level, 1);
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
}
