package at.fhtw.sampleapp.service.model;
import at.fhtw.sampleapp.model.Cards;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CardModelTest {

    //test if Regular Spells switch Element to Normal by calling the Constructor
    @Test
    void testCardModelSwitchRegularToNormalByConstructor() {
        Cards testCard = new Cards("3fa85f64-5717-4562-b3fc-2c963f66afa6", "RegularSpell", 50);
        assertEquals("RegularSpell", testCard.getName());
        assertEquals("Normal", testCard.getElement());
        assertEquals("Spell", testCard.getType());
    }
    //test if Regular Spells switch Element to Normal by calling setName
    @Test
    void testCardModelSwitchRegularToNormalBySetName() {
        Cards testCard = new Cards();
        testCard.setName("RegularSpell");
        assertEquals("RegularSpell", testCard.getName());
        assertEquals("Normal", testCard.getElement());
        assertEquals("Spell", testCard.getType());
    }

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

    //test if setName() sets Type and Element
    @Test
    void testCardModelSetTypeSetElementbyName() {
        Cards testCard = new Cards();
        testCard.setName("FireMage");
        assertEquals("FireMage", testCard.getName());
        assertEquals("Fire", testCard.getElement());
        assertEquals("Mage", testCard.getType());
    }

    //test if setName() sets Type and Element with normal type
    @Test
    void testCardModelSetTypeSetElementbyNameWithoutElement() {
        Cards testCard = new Cards();
        testCard.setName("Dragon");
        assertEquals("Dragon", testCard.getName());
        assertEquals("Normal", testCard.getElement());
        assertEquals("Dragon", testCard.getType());
    }

};
