package Decks;

public class Card {
    private String text;

    public Card(String s){
        this.text = s;
    }

    public String getText(){
        return this.text;
    }

    public void setText(String s){
        this.text = s;
    }

    public String toString(){
        return this.text;
    }
}
