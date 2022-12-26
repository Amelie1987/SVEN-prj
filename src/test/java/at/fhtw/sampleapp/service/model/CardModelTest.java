package at.fhtw.sampleapp.service.model;
import at.fhtw.sampleapp.model.Cards;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CardModelTest {

    //test if constructor sets Element and Type from name right
    @Test
    void testCardModelSetTypeSetElement() {
        Cards testCard = new Cards("3fa85f64-5717-4562-b3fc-2c963f66afa6", "WaterGoblin", 55);
        assertEquals("WaterGoblin", testCard.getName());
        assertEquals("Water", testCard.getElement());
        assertEquals("Goblin", testCard.getType());
    }

    //test if constructor sets Element and Type from name right if there is no element
    @Test
    void testCardModelSetTypeSetElementWithoutElement() {
        Cards testCard = new Cards("3fa85f64-5717-4562-b3fc-2c963f66afa6", "Dragon", 50);
        assertEquals("Dragon", testCard.getName());
        assertEquals("Normal", testCard.getElement());
        assertEquals("Dragon", testCard.getType());
    }
};
