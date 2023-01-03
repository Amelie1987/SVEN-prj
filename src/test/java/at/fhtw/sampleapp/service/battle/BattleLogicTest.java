package at.fhtw.sampleapp.service.battle;

import at.fhtw.sampleapp.model.Cards;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BattleLogicTest {

    //test if card dmg by element multiplier is correct and special effects like goblins cant attack dragons
    @Test
    void testBattleLogicSetMultiplierGoblinVsDragon() {
        BattleLogic battleLogic = new BattleLogic();
        Cards cardOne = new Cards("3fa85f64-5717-4562-b3fc-2c963f66afa6", "WaterGoblin", 50);
        Cards cardTwo = new Cards("b2237eca-0271-43bd-87f6-b22f70d42ca4", "Dragon", 40);

        assertEquals(0, battleLogic.getMultiplier(cardOne, cardTwo));
        assertEquals("Normal", cardTwo.getElement());
        assertEquals(1, battleLogic.getMultiplier(cardTwo, cardOne));
    }

    @Test
    void testBattleLogicSetMultiplierFireElfVsDragon() {
        BattleLogic battleLogic = new BattleLogic();
        Cards cardOne = new Cards("3fa85f64-5717-4562-b3fc-2c963f66afa6", "FireElf", 50);
        Cards cardTwo = new Cards("b2237eca-0271-43bd-87f6-b22f70d42ca4", "Dragon", 40);

        assertEquals(1, battleLogic.getMultiplier(cardOne, cardTwo));
        assertEquals("Normal", cardTwo.getElement());
        assertEquals(0, battleLogic.getMultiplier(cardTwo, cardOne));
    }

    @Test
    void testBattleLogicSetMultiplierSpellVsMonster() {
        BattleLogic battleLogic = new BattleLogic();
        Cards cardOne = new Cards("3fa85f64-5717-4562-b3fc-2c963f66afa6", "FireSpell", 20);
        Cards cardTwo = new Cards("b2237eca-0271-43bd-87f6-b22f70d42ca4", "Knight", 20);

        assertEquals(2, battleLogic.getMultiplier(cardOne, cardTwo));
        assertEquals("Normal", cardTwo.getElement());
        assertEquals(1, battleLogic.getMultiplier(cardTwo, cardOne));
    }

    @Test
    void testBattleLogicSetMultiplierKnightVsWater() {
        BattleLogic battleLogic = new BattleLogic();
        Cards cardOne = new Cards("3fa85f64-5717-4562-b3fc-2c963f66afa6", "WaterSpell", 20);
        Cards cardTwo = new Cards("b2237eca-0271-43bd-87f6-b22f70d42ca4", "Knight", 20);

        assertEquals(50, battleLogic.getMultiplier(cardOne, cardTwo));
        assertEquals("Normal", cardTwo.getElement());
        assertEquals(1, battleLogic.getMultiplier(cardTwo, cardOne));
    }

    @Test
    void testBattleLogicSetMultiplierSpellVsSpell() {
        BattleLogic battleLogic = new BattleLogic();
        Cards cardOne = new Cards("3fa85f64-5717-4562-b3fc-2c963f66afa6", "RegularSpell", 20);
        Cards cardTwo = new Cards("b2237eca-0271-43bd-87f6-b22f70d42ca4", "FireSpell", 20);

        assertEquals(0.5, battleLogic.getMultiplier(cardOne, cardTwo));
        assertEquals("Normal", cardOne.getElement());
        assertEquals(2, battleLogic.getMultiplier(cardTwo, cardOne));
    }
}
