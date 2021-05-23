import java.util.*;

public class BlackJack {
    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);

        BackEnd backEnd = new BackEnd();

        boolean isValid = false;
        int playerId = 1;
        int numberOfPlayers = -1;

        while(isValid != true) {

            try {
                System.out.print("How many players? (1-6) ");
                numberOfPlayers = s.nextInt();
                while(numberOfPlayers > 6 || numberOfPlayers <= 0){
                    System.out.print("Must be between 1 and 6 players. Please try again: ");
                    numberOfPlayers = s.nextInt();
                }
                isValid = true;
            }
            catch (Exception e) {
                System.out.println("That is not a number!! >:( Please try again");
            }
            finally {
                s.nextLine();
            }

        }

        for (; playerId <= numberOfPlayers; playerId++) {

            System.out.print("Enter the name for player #" + playerId + " ");
            String name = s.next();
            backEnd.addPlayer(name, playerId);

        }

        boolean keepPlaying = true;
        int gameNumber = 1;

        whileloop:
        while (keepPlaying) {
            System.out.println("\n********** START GAME #" + gameNumber + " **********\n");

            forloop0:
            for (int i = 0; i < numberOfPlayers; i++) {

                int twoCards = 1;

                while(twoCards <= 2) {
                    System.out.println(backEnd.generatePlayersCard(i));
                    twoCards++;
                }

                if(backEnd.getPlayersHand(i) == 21) {
                    System.out.println(backEnd.getPlayerName(i) + "'s got a BlackJack!!\n");
                }

                System.out.println(backEnd.getPlayerName(i) + "'s hand " + backEnd.getPlayersHand(i));
                System.out.println(" ");

            }

            System.out.println(backEnd.generateDealersCard());
            System.out.println("Dealer's hand " + backEnd.getDealersHand());
            System.out.println(" ");

            forloop1:
            for (int i = 0; i < numberOfPlayers; i++) {
                if(backEnd.getPlayersHand(i) == 21) { continue forloop1;}
                int option;
                do {
                    System.out.println("-------------------------");
                    System.out.println("1. Get another card\n2. Hold hands\n3. Get statistics\n4. Exit\n");
                    System.out.println(backEnd.getPlayerName(i) + "'s hand " + backEnd.getPlayersHand(i));
                    System.out.println("Dealer's hand " + backEnd.getDealersHand() + "\n");
                    System.out.print(backEnd.getPlayerName(i) + " please select an option: ");
                    option = s.nextInt();

                    switch (option) {
                        case 1:
                            System.out.println("\n" + backEnd.generatePlayersCard(i));
                            System.out.println(backEnd.getPlayerName(i) + "'s hand " + backEnd.getPlayersHand(i) + "\n");
                            if (backEnd.getPlayersHand(i) == 21) {
                                System.out.println(backEnd.getPlayerName(i) + " got a BlackJack!!\n");
                                continue forloop1;
                            } else if (backEnd.getPlayersHand(i) > 21) {
                                System.out.println(backEnd.getPlayerName(i) + " got busted :(\n");
                                continue forloop1;
                            } else {
                                continue;
                            }
                        case 2:
                            System.out.println("\n" + backEnd.getPlayerName(i) + "'s hand " + backEnd.getPlayersHand(i));
                            System.out.println("Dealer's hand " + backEnd.getDealersHand() + "\n");
                            continue forloop1;
                        case 3:
                            System.out.println("\nTotal # of games played: " + (gameNumber - 1));
                            System.out.println("Number of tie games: " + backEnd.getPlayersTies(i));
                            System.out.println(backEnd.getPlayerName(i) + " has won " + backEnd.getPlayersWins(i) + " times");
                            System.out.println("Win ratio: " + ((backEnd.getPlayersWins(i) / (double)(gameNumber - 1)) * 100) + "%\n");
                            break;
                        case 4:
                            System.out.println(backEnd.getPlayerName(i) + " has stopped the game. Chao");
                            keepPlaying = false;
                            continue whileloop;
                        default:
                            System.out.println("Invalid input\n");
                    }
                } while (option != 4);
            }

            while (backEnd.getDealersHand() < 17) {
                System.out.println("\n--------------------");
                System.out.println(backEnd.generateDealersCard());
                System.out.println("Dealer's hand " + backEnd.getDealersHand());
                System.out.println("--------------------\n");
            }

            if (backEnd.getDealersHand() > 21) {
                System.out.println("Dealer got busted\n");
                for (int i = 0; i < numberOfPlayers; i++) {
                    if (backEnd.getPlayersHand(i) > 21) {
                        backEnd.setPlayersTies(i);
                        System.out.println(backEnd.getPlayerName(i) + " has tie with dealer\n");
                    } else {
                        backEnd.setPlayerWins(i);
                        System.out.println(backEnd.getPlayerName(i) + " has won\n");
                    }
                }
            }
            else{
                for (int i = 0; i < numberOfPlayers; i++) {
                    if (backEnd.getPlayersHand(i) == backEnd.getDealersHand()) {
                        backEnd.setPlayersTies(i);
                        System.out.println(backEnd.getPlayerName(i) + " has tie with dealer\n");
                    }
                    else if (backEnd.getPlayersHand(i) > backEnd.getDealersHand()) {
                        if (backEnd.getPlayersHand(i) <= 21) {
                            backEnd.setPlayerWins(i);
                            System.out.println(backEnd.getPlayerName(i) + " has won\n");
                        } else { System.out.println(backEnd.getPlayerName(i) + " has lost\n"); }
                    }
                    else {
                        System.out.println(backEnd.getPlayerName(i) + " has lost\n");
                    }
                }
            }
            backEnd.resetValues();
            gameNumber++;
        }
    }
}

class BackEnd {

    private Deck deck = new Deck();
    private Dealer dealer = new Dealer();
    private Player[] playerArray = new Player[6];


    public String addPlayer(String name, int playerId) {

        for (int i = 0; i < playerArray.length; i++) {
            if (playerArray[i] == null) {
                playerArray[i] = new Player(name, playerId);
                return "Player Edwin has been added as player # " + playerId;
            }
        } return "No one has been added";
    }

    public String generatePlayersCard(int arrayIndex) {

        deck.generateCardValueAndFace();
        if(playerArray[arrayIndex].getFirstAce() == false) {
            if(deck.getCardValue() == 1) {
                if(playerArray[arrayIndex].getHand() < 11) {
                    playerArray[arrayIndex].setFirstAce('t');
                    playerArray[arrayIndex].setHand(11);
                }
                else playerArray[arrayIndex].setHand(deck.getCardValue());
            } else playerArray[arrayIndex].setHand(deck.getCardValue());
        }
        else{
            if((playerArray[arrayIndex].getHand() + deck.getCardValue()) > 21) {
                playerArray[arrayIndex].setHand(-10 + deck.getCardValue());
                playerArray[arrayIndex].setFirstAce('f');
            } else {
                playerArray[arrayIndex].setHand(deck.getCardValue());
            }
        }

        return playerArray[arrayIndex].getName() + " got " + deck.getCardFace();

    }

    public String generateDealersCard() {

        deck.generateCardValueAndFace();
        if(dealer.getFirstAce() == false) {
            if(deck.getCardValue() == 1) {
                if(dealer.getHand() < 11) {
                    dealer.setFirstAce('t');
                    dealer.setHand(11);
                }
                else dealer.setHand(deck.getCardValue());
            } else dealer.setHand(deck.getCardValue());
        }
        else{
            if((dealer.getHand() + deck.getCardValue()) > 21) {
                dealer.setHand(-10 + deck.getCardValue());
                dealer.setFirstAce('f');
            } else {
                dealer.setHand(deck.getCardValue());
            }
        }

        return "Dealer got " + deck.getCardFace();

    }

    public int getDealersHand() {

        return dealer.getHand();

    }

    public void resetValues() {
        dealer.resetHand();
        for (int i = 0; i < playerArray.length; i++) {
            if(playerArray[i] != null) {
                playerArray[i].resetHand();
            }
        }
    }

    //<editor-fold desc="Player Get Methods">

    public String getPlayerName(int arrayIndex) {

        return playerArray[arrayIndex].getName();

    }

    public int getPlayersHand(int arrayIndex) {

        return playerArray[arrayIndex].getHand();

    }

    public int getPlayersWins(int arrayIndex) {

        return playerArray[arrayIndex].getPlayerWins();

    }

    public int getPlayersTies(int arrayIndex) {

        return playerArray[arrayIndex].getPlayerTie();

    }

    //</editor-fold>

    //<editor-fold desc="Player Set Methods">

    public void setPlayerWins(int arrayIndex) {

        playerArray[arrayIndex].setPlayerWins();

    }

    public void setPlayersTies(int arrayIndex) {

        playerArray[arrayIndex].setPlayerTie();

    }

    //</editor-fold>

}

class Deck {

    public Deck() {}

    private String cardFace;
    private int cardValue;

    public void generateCardValueAndFace() {

        cardValue = (int)(Math.random()*(13)+1);
        if(cardValue == 1) {cardFace = "an Ace";}
        else if(cardValue == 11) {cardFace = "a Jack"; cardValue = 10;}
        else if(cardValue == 12) {cardFace = "a Queen"; cardValue = 10;}
        else if(cardValue == 13) {cardFace = "a King"; cardValue = 10;}
        else cardFace = "a " + Integer.toString(cardValue);

    }

    public int getCardValue() {

        return cardValue;

    }

    public String getCardFace() {

        return cardFace;

    }

}

class Player {

    private String name;
    private int playerId;
    private int hand;
    private int playerWins;
    private int playerTie;
    private boolean firstAce;

    public Player(String name, int playerId) {

        this.name = name;
        this.playerId = playerId;

    }

    public int getPlayerId() {

        return playerId;

    }

    public String getName() {

        return name;

    }

    public boolean getFirstAce() {

        return firstAce;

    }

    public void setFirstAce(char t) {

        if(t == 't') firstAce = true;
        else firstAce = false;

    }

    //<editor-fold desc="Get/Set Hand">
    public int getHand() {

        return hand;

    }

    public void setHand(int card) {

        hand += card;

    }
    //</editor-fold>

    //<editor-fold desc="Get/Set Wins">
    public int getPlayerWins() {

        return playerWins;

    }

    public void setPlayerWins() {

        playerWins++;

    }
    //</editor-fold>

    //<editor-fold desc="Get/Set Tie">
    public int getPlayerTie() {

        return playerTie;

    }

    public void setPlayerTie() {

        playerTie++;

    }
    //</editor-fold>

    public void resetHand() {

        hand = 0;
        firstAce = false;

    }
}

class Dealer {

    private int hand;
    private boolean firstAce;

    public void setHand(int card) {

        hand += card;

    }

    public int getHand() {

        return hand;

    }

    public boolean getFirstAce() {

        return firstAce;

    }

    public void setFirstAce(char t) {

        if(t == 't') firstAce = true;
        else firstAce = false;

    }

    public void resetHand() {

        hand = 0;
        firstAce = false;

    }

}







