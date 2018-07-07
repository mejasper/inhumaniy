package Decks;

import java.util.ArrayList;

public class decktest {
    public static void main(String[] args){
        Deck d = new Deck("src/Decks/deck.txt");

        ArrayList<Card> swag = d.getCards(4);
        System.out.println(swag);
        swag = d.getCards(4);
        System.out.println(swag);
        swag = d.getCards(4);
        System.out.println(swag);
        swag = d.getCards(4);
        System.out.println(swag);
        swag = d.getCards(4);
        System.out.println(swag);
        swag = d.getCards(4);
        System.out.println(swag);


    }
}
