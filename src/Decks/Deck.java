package Decks;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

public class Deck {
    private ArrayList<Card> deck;
    private ArrayList<Card> used;

    public Deck(String path){
        deck = new ArrayList<>();
        try {
            Scanner s = new Scanner(new BufferedReader(new FileReader(path)));
            while(s.hasNextLine()){
                deck.add(new Card(s.nextLine()));
            }
            used = new ArrayList<>();

        }catch(Exception e){
            System.out.println("File not found");
        }

    }
    public ArrayList<Card> getCards(int need){
        ArrayList<Card> cards = new ArrayList<>();
        for(int i = 0; i<need ; i++){
            if(this.deck.size() == 0){
                this.shuffle();
            }
            Card crnt = deck.get(new Random().nextInt(deck.size()));
            deck.remove(crnt);
            cards.add(crnt);
            used.add(crnt);

        }
        return cards;
    }

    public Card getCard(){
        Card crnt;

        if(this.deck.size() == 0){
            this.shuffle();
        }
        crnt = deck.get(new Random().nextInt(deck.size()));
        deck.remove(crnt);
        used.add(crnt);
        return crnt;
    }




    private void shuffle(){
        deck = used;
        used = new ArrayList<>();
        Collections.shuffle(deck);
    }

    public String toString(){
        return this.deck.toString();
    }


}
