package at.fhtw.sampleapp.service.repository;

import at.fhtw.sampleapp.dal.UnitOfWork;
import at.fhtw.sampleapp.dal.repository.CardRepository;
import at.fhtw.sampleapp.model.Cards;
import at.fhtw.sampleapp.model.Users;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CardRepositoryTest {

    // test getRandomCard and set stats for card
    @Test
    void testGetRandomCard() throws Exception {
        UnitOfWork unitOfWork = new UnitOfWork();
        Users user = new Users();
        Cards card = new Cards();
        user.setDeck_id(327);       // must be changed, after deletes in db
        CardRepository cardRepository = new CardRepository(unitOfWork);
        cardRepository.getRandomCard(user, card);
        System.out.println("Card found: " + card.getCard_id());
        cardRepository.setCardStats(card);
        System.out.println("Cards Name: " + card.getName() + " Type: " + card.getType() +
                " Element: " + card.getElement() + " Damage: " + card.getDamage());
    }
}
