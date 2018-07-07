import Decks.Card;
import Decks.Deck;
import java.io.DataInputStream;
import java.io.PrintStream;
import java.io.IOException;
import java.net.Socket;
import java.net.ServerSocket;
import java.util.ArrayList;

public class MultiThreadChatServer {

    private static ServerSocket serverSocket = null;
    private static Socket clientSocket = null;
    private static final int maxClientsCount = 10;
    private static final clientThread[] threads = new clientThread[maxClientsCount];
    private static Deck deck;

    public static void main(String args[]) {


        int portNumber = 2222;
        if (args.length == 1) {
            portNumber = Integer.valueOf(args[0]).intValue();
        }

        try {
            serverSocket = new ServerSocket(portNumber);
        } catch (IOException e) {
            System.out.println("Something went wrong setting up everything at:" + portNumber);
        }

        deck = new Deck("src/Decks/deck.txt");
        while (true) {
            try {
                clientSocket = serverSocket.accept();
                int i = 0;
                for (i = 0; i < maxClientsCount; i++) {
                    if (threads[i] == null) {
                        (threads[i] = new clientThread(clientSocket, threads, deck)).start();
                        break;
                    }
                }
                if (i == maxClientsCount) {
                    PrintStream os = new PrintStream(clientSocket.getOutputStream());
                    os.println("Server too busy. Try later.");
                    os.close();
                    clientSocket.close();
                }
                for(clientThread t: threads){
                    if(t!=null && t.clientName != null) {
                        t.os.println("eyy");
                    }
                }
            } catch (IOException e) {
                System.out.println(e);
            }
        }
    }
}

class clientThread extends Thread {

    public String clientName = null;
    private DataInputStream is = null;
    public PrintStream os = null;
    private Socket clientSocket = null;
    private final clientThread[] threads;
    private int maxClientsCount;
    private Deck deck;
    private ArrayList<Card> hand;

    public clientThread(Socket clientSocket, clientThread[] threads, Deck c) {
        this.deck = c;
        this.clientSocket = clientSocket;
        this.threads = threads;
        maxClientsCount = threads.length;
        this.hand = deck.getCards(11);
    }

    public void run() {
        int maxClientsCount = this.maxClientsCount;
        clientThread[] threads = this.threads;

        try {
            is = new DataInputStream(clientSocket.getInputStream());
            os = new PrintStream(clientSocket.getOutputStream());
            String name;
            while (true) {
                os.println("Enter your name.");
                name = is.readLine().trim();
                if (name.indexOf('@') == -1) {
                    break;
                } else {
                    os.println("The name should not contain '@' character.");
                }
            }


            synchronized (this) {
                for (int i = 0; i < maxClientsCount; i++) {
                    if (threads[i] != null && threads[i] == this) {
                        //send to me
                        threads[i].os.println(hand);
                        clientName = "@" + name;
                        break;
                    }
                }
                for (int i = 0; i < maxClientsCount; i++) {
                    if (threads[i] != null && threads[i] != this) {
                        //send to everyone but me
                    }
                }
            }
            while (true) {
                String line = is.readLine();
                if (line.startsWith("/quit")) {
                    break;
                }
                int ind = Integer.parseInt(line);
                hand.remove(ind);
                hand.add(deck.getCard());

                    /* The message is public.*/
                    synchronized (this) {
                        for (int i = 0; i < maxClientsCount; i++) {
                            if (threads[i] != null && threads[i].clientName != null) {
//                                threads[i].os.println("<" + name + "> " + line);
                            }
                        }
                    }
                //send to client
                synchronized (this) {
                    for (int i = 0; i < maxClientsCount; i++) {
                        if (threads[i] != null && threads[i].clientName != null && threads[i] == this) {
                                threads[i].os.println(hand);
                        }
                    }
                }

            }
            synchronized (this) {
                for (int i = 0; i < maxClientsCount; i++) {
                    if (threads[i] != null && threads[i] != this && threads[i].clientName != null)  threads[i].os.println( name + " left.");
                }
            }
            os.println("You left.");

            synchronized (this) {
                for (int i = 0; i < maxClientsCount; i++) {
                    if (threads[i] == this) {
                        threads[i] = null;
                    }
                }
            }
            is.close();
            os.close();
            clientSocket.close();
        } catch (IOException e) {
        }
    }
}
