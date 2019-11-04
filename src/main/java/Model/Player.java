package Model;

import javax.xml.crypto.Data;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ListIterator;
import java.util.Random;
import java.util.Scanner;

/**
 * This file holds data members and all the methods which are related to player.
 * This has singleton implementation.
 */
public class Player {
    String name;
    Integer number, numberOfArmies;
    ArrayList<Integer> myCountries = new ArrayList<Integer>();
    Integer exchangeCardsTimes;
    ArrayList<Card> playerCards;
    boolean countryConquered;
    boolean defenderRemoved;
	static Integer lastDiceSelected = null;

    public String getName() {
        return name;
    }

    private Player(Integer number, String name, Integer numberOfArmies) {
        this.number = number;
        this.name = name;
        this.numberOfArmies = numberOfArmies;
        playerCards = new ArrayList<Card>();
        exchangeCardsTimes = 0;
        countryConquered = false;
        defenderRemoved = false;
    }

    public Integer getNumber() {
        return number;
    }


    public void setName(String name) {
        this.name = name;
    }

    public Integer getNumberOfArmies() {
        return numberOfArmies;
    }

    public void setNumberOfArmies(Integer numberOfArmies) {
        this.numberOfArmies = numberOfArmies;
    }

    public ArrayList<Integer> getMyCountries() {
        return myCountries;
    }

    public void setMyCountries(Integer number) {
    	myCountries.add(number); 
    }

    /**
     * This adds a new player in Database.playerlist
     * @param playerName
     * @param noOfArmies
	 * @return true or false
     */
    public static boolean addPlayer(String playerName, Integer noOfArmies){
        if(Player.getPlayerByName(playerName)!=null){
            System.out.println("=======> This player exists <========");
            return false;
        }
        else if(playerName.trim().length() ==0){
            System.out.println("=======> Please enter a name for the player <========");
            return false;
        }
        Integer id= Database.getInstance().getPlayerList().size() + 1;

        Player player= new Player(id, playerName,noOfArmies);
        Database.playerList.add(player);
        return true;
    }

    /**
     * This removes a player from Database.playerlist
     * @param playerName
	 * @return true or false
     */
    public static boolean removePlayer(String playerName){
        Player player= Player.getPlayerByName(playerName);
        if(player==null){
            return false;
        }
        Database.playerList.remove(player);

        Integer playerNumber=1;
        for(Player player1: Database.getInstance().getPlayerList() ){
            player1.number=playerNumber;
            playerNumber++;
        }

        return true;
    }

    /**
     * This returns the instance of the player where a player is saved in Database.playerlist using player's name
     * @param playerName
     * @return instance of the player
     */
    public static Player getPlayerByName(String playerName){
        for(Player player: Database.playerList){
            if(player.getName().equalsIgnoreCase(playerName)){
                return player;
            }
        }
        return null;
    }

    /**
     * This returns the instance of the player where a player is saved in Database.playerlist using player's number
     * @param playerNumber
     * @return instance of the player
     */
    public static Player getPlayerByNumber(Integer playerNumber){
        for(Player player: Database.getInstance().getPlayerList()){
            if(player.getNumber() == playerNumber){
                return player;
            }
        }
        return null;
    }

    
    /**
     *  check if the number of remaining armies that can be placed is equal to zero for every player
     * 
	 * @return true or false
     */
    public static boolean allPlayersRemainingArmiesExhausted(){
        for(Player player: Database.getInstance().getPlayerList()){
            if(player.getNumberOfArmies()>0){
                return false;
            }
        }
        return true;
    }

    /**
     * This prints all the details for each and every player in Database.playerlist 
     */
    public static void printAllPlayers(){
        for(Player player: Database.getInstance().getPlayerList()){
            System.out.println(player.getNumber()+ " " + player.getName() +" "+ player.getNumberOfArmies() );
        }
        System.out.println();
    }

    
    /**
     * this provides the list of countries owned by a particular player
     * @param playerName
     * @param gameGraph
     * @return list of countries owned by a player
     */
    public static ArrayList<Country> getOwnedCountryList(String playerName, Graph gameGraph){
        ArrayList<Country> countryList= new ArrayList<Country>();
        for(Country country: gameGraph.getAdjList()){
            if(country.getOwner().equalsIgnoreCase(playerName)){
                countryList.add(country);
            }
        }
        return countryList;
    }
    
    public static boolean reinforcement(String countryName, Integer numberOfArmies, Graph graphObj, CurrentPlayer currentPlayerObj) {
    	
    	//check: if target country is not exist, return false
    	Country targetCountry= Country.getCountryByName(countryName, graphObj);
    	
    	if(targetCountry == null)
    		return false;
    			
    	//check: if country does not belong to the currentPlayer, return false
    	if(targetCountry.getOwner() != null){
    		if(targetCountry.getOwner().equalsIgnoreCase(currentPlayerObj.getCurrentPlayer().getName()) == false){			
    			System.out.println("The country is not belong to the current player");
    			return false;
    		}
    	}
    	
    	//check: if numberOfArmy is more than allocated army, return false
    	if(numberOfArmies > currentPlayerObj.getNumReinforceArmies()) {
    		System.out.println("The current player can reinforce just " + currentPlayerObj.getNumReinforceArmies() + "armies");
    		return false;
    	}
    			
    	//Reinforce armies in the target country
    	targetCountry.setNumberOfArmies(targetCountry.getNumberOfArmies() + numberOfArmies);
    			
    	//increase the number of armies belong to the player
    	currentPlayerObj.increaseCurrentPlayerArmies(numberOfArmies);
    	currentPlayerObj.decreaseReinforceentArmies(numberOfArmies);
    			
    	return true;
    } 
    
	public static Integer defenderCommandInput(Integer numberOfArmiesDefenderCanSelect, Scanner sc) {
		String defenderCommand = "";
		Integer defenderDice = null;
		defenderCommand = sc.nextLine();
		String[] defenderDiceSplit = defenderCommand.split(" ");
		if (!(defenderDiceSplit.length == 2 && defenderDiceSplit[0].trim().equals("defend")
				&& ((Integer.parseInt(defenderDiceSplit[1])) < (numberOfArmiesDefenderCanSelect + 1))
				&& ((Integer.parseInt(defenderDiceSplit[1])) > 0))) {

			System.out.println("Please write a valid command");

		} else {
			defenderDice = Integer.parseInt(defenderDiceSplit[1].trim());
		}
		defenderCommand = "";

		if (defenderDice != null) {
			return defenderDice;
		} else {
			return defenderCommandInput(numberOfArmiesDefenderCanSelect, sc);
		}
	}

	public Integer defenderArmiesSelectionForAllout() {
		return 0;
	}

	public static boolean attackAllout(String fromCountry, String toCountry, Graph graphObj, CurrentPlayer currentPlayerObj) {

		Country attackerCountry = Country.getCountryByName(fromCountry, graphObj);
		Country defenderCountry = Country.getCountryByName(toCountry, graphObj);

		if (attackerCountry.getOwner().equalsIgnoreCase(currentPlayerObj.getCurrentPlayer().name)) {

			if (defenderCountry.getOwner().equalsIgnoreCase(currentPlayerObj.getCurrentPlayer().name)) {
				
				System.out.println("You can only attack the countries that are owned by some other player");
				return false;	
			} 
			else {
				
				if (attackerCountry.neighbours.contains(defenderCountry.getNumber())) {

					Integer AttackerArmiesSelected = null;
					Integer DefenderArmiesSelected = null;

					if (attackerCountry.getNumberOfArmies() > 3) {
						// armies selected 3
						AttackerArmiesSelected = 3;
					} else if (attackerCountry.getNumberOfArmies() == 3) {
						// armies selected 2
						AttackerArmiesSelected = 2;
					} else if (attackerCountry.getNumberOfArmies() == 2) {
						// armies selected 1
						AttackerArmiesSelected = 1;
					} else {
						System.out.println("Cannot attack anymore " + fromCountry + ", has only 1 army left!");
					}

					if (defenderCountry.getNumberOfArmies() >= 2) {
						// armies selected 2
						DefenderArmiesSelected = 2;
					} else if (defenderCountry.getNumberOfArmies() == 1) {
						// armies selected 1
						DefenderArmiesSelected = 1;
					} else {
						System.out.println("No more armies to defend the country");
					}

					System.out.println("A" + Country.getCountryByName("Quebec", Graph.getInstance()).getNumberOfArmies());
					System.out.println(Country.getCountryByName("Greenland", Graph.getInstance()).getNumberOfArmies());
					battle(attackerCountry, defenderCountry, AttackerArmiesSelected, DefenderArmiesSelected);
					AttackerArmiesSelected = null;
					DefenderArmiesSelected = null;
					System.out.println("A" + Country.getCountryByName("Quebec", Graph.getInstance()).getNumberOfArmies());
					System.out.println(Country.getCountryByName("Greenland", Graph.getInstance()).getNumberOfArmies());

					if (defenderCountry.getNumberOfArmies() == 0) {
						
						System.out.println("Attacker won the country " + defenderCountry.name);
						defenderCountry.setOwner(attackerCountry.getOwner());
						System.out.println("Please enter a command to move armies to " + defenderCountry.name);
						System.out.println("Please select a number greater than or equal to " + lastDiceSelected + " and less than " + attackerCountry.getNumberOfArmies());

						Integer attackMove = null;
						Scanner scanner = new Scanner(System.in);

						attackMove = attackMoveCommand(lastDiceSelected, scanner, attackerCountry.getNumberOfArmies());

						if (attackMove != null) {
							attackMove(attackerCountry, defenderCountry, attackMove);
						} else {
							System.out.println("something went wrong!!");
						}
						System.out.println("allout is finished here.");
					//	scanner.close();

					} else if (attackerCountry.getNumberOfArmies() == 1) {

						System.out.println("no attack anymore possible!!!");
					} else {
						attackAllout(fromCountry, toCountry, graphObj, currentPlayerObj);
					}

				} else {
					System.out.println("Attacker country and the defender country should be adjacent");
				}
			}
		} 
		else {
			System.out.println("Please select the country owned by you(" + currentPlayerObj.getCurrentPlayer().name + ") as attackerCountry");
			return false;
		}

		return true;

	}

	public static Integer attackMoveCommand(Integer numberOfArmiesDuringLastAttack, Scanner scanner,
		
		Integer numberOfArmiesThatAttackerHave) {
		Integer attackMoveNumber = null;
		String Command = scanner.nextLine();

		String[] AttackMoveSplit = Command.split(" ");
		if (!(AttackMoveSplit.length == 2 && AttackMoveSplit[0].trim().equals("attackmove")
				&& ((Integer.parseInt(AttackMoveSplit[1])) >= numberOfArmiesDuringLastAttack))) {

			System.out.println("Please write a valid command");

		} else {
			attackMoveNumber = Integer.parseInt(AttackMoveSplit[1].trim());
		}

		if (attackMoveNumber != null) {

			if (attackMoveNumber < numberOfArmiesDuringLastAttack || attackMoveNumber>=numberOfArmiesThatAttackerHave) {
				Integer range =numberOfArmiesThatAttackerHave-1;
				if(numberOfArmiesDuringLastAttack==range) {
					System.out.println("you can only move " + range + " armies/army" + ", please retry:" );
					return attackMoveCommand(numberOfArmiesDuringLastAttack, scanner, numberOfArmiesThatAttackerHave);}
				else {
					System.out.println("you have to select number of armies between the number " + numberOfArmiesDuringLastAttack + "and " + range +", please retry:");
					return attackMoveCommand(numberOfArmiesDuringLastAttack, scanner, numberOfArmiesThatAttackerHave);
				}	
			}
			else {
				return attackMoveNumber;
			}
		} 
		else {
			return attackMoveCommand(numberOfArmiesDuringLastAttack, scanner, numberOfArmiesThatAttackerHave);
		}
	}

	public static boolean attackMove(Country attackerCountry, Country defenderCountry, Integer numberOfArmiesToMove) {

		if (defenderCountry.getNumberOfArmies() == 0) {

			if (attackerCountry.getNumberOfArmies() > numberOfArmiesToMove) {
				defenderCountry.setNumberOfArmies(numberOfArmiesToMove);
				attackerCountry.setNumberOfArmies(attackerCountry.getNumberOfArmies() - numberOfArmiesToMove);
			} 
			else {
				System.out.println("you selected a greater number than you are allowed to move from attacker country");
				return false;
			}

		} 
		else {
			System.out.println("something went wrong!!");
			return false;
		}
		return true;
	}

	public static boolean attackCountry(String fromCountry, String toCountry, Integer numDice, Graph graphObj, CurrentPlayer currentPlayerObj) {

		Country attackerCountry = Country.getCountryByName(fromCountry, graphObj);
		Country defenderCountry = Country.getCountryByName(toCountry, graphObj);

		// Owner of attackerCountry should be same as current player
		if (attackerCountry.getOwner().equalsIgnoreCase(currentPlayerObj.getCurrentPlayer().name)) { 
			
			// Owner of defender Country cannot be currentPlayer
			if (defenderCountry.getOwner().equalsIgnoreCase(currentPlayerObj.getCurrentPlayer().name)) { 
				
				System.out.println("You can only attack the countries that are owned by some other player");
				return false;
			} 
			else {
				
				// checks adjacency of the countries 
				if (attackerCountry.neighbours.contains(defenderCountry.getNumber())) { 
					
					// can attack keeping minimum 1 army in his own country
					if (attackerCountry.getNumberOfArmies() > 1) {  
						
						// armies in attacker country should be more than armies selected for attack
						if (attackerCountry.getNumberOfArmies() > numDice) { 

							// max armies for attack can be 3
							if (numDice < 4) { 
								Integer defenderDice = 0;

								Scanner sc = new Scanner(System.in);
								if (defenderCountry.getNumberOfArmies() >= 2) {
									System.out.println(defenderCountry.getOwner() + ", you can select maximum of 2 armies to defend your country");
									defenderDice = defenderCommandInput(2, sc);

								} 
								else {
									System.out.println(defenderCountry.getOwner() + ", you only have 1 army with which you can defend your country");
									defenderDice = defenderCommandInput(1, sc);
								}

						//		sc.close();
								
								// defender can select max 2 armies
								if (defenderDice > 2) { 
									System.out.println("defender entered a number greater than 2");
									return false;
								}
								else {

									System.out.println("A" + Country.getCountryByName("Quebec", Graph.getInstance()).getNumberOfArmies());
									System.out.println(Country.getCountryByName("Greenland", Graph.getInstance()).getNumberOfArmies());
									battle(attackerCountry, defenderCountry, numDice, defenderDice);

									if (defenderCountry.getNumberOfArmies() == 0) {
										
										System.out.println("Attacker won the country " + defenderCountry.name);
										defenderCountry.setOwner(attackerCountry.getOwner());
										System.out.println("Please enter a command to move armies to " + defenderCountry.name);
										System.out.println("Please select a number greater than or equal to " + lastDiceSelected + " and less than " + attackerCountry.getNumberOfArmies());

										Integer attackMove = null;
										Scanner scanner = new Scanner(System.in);
										attackMove = attackMoveCommand(lastDiceSelected, scanner, attackerCountry.getNumberOfArmies());

										if (attackMove != null) {
											attackMove(attackerCountry, defenderCountry, attackMove);
										} 
										else {
											System.out.println("something went wrong!!");
										}
										
										System.out.println("allout is finished here.");
										scanner.close();
										System.out.println("attackCountry command finished");
									}
									
									System.out.println("A" + Country.getCountryByName("Quebec", Graph.getInstance()).getNumberOfArmies());
									System.out.println(Country.getCountryByName("Greenland", Graph.getInstance()).getNumberOfArmies());
								}
							} 
							else {
								System.out.println("You can attack with atmost 3 armies");
							}
						}
						else if (attackerCountry.getNumberOfArmies() == numDice) {			
							System.out.println("You cannot attack with all your armies");
							return false;			
						} 
						else {				
							System.out.println("You selected more armies than you have in " + fromCountry);
							return false;		
						}
					} 
					else {
						System.out.println("You dont have enough number of armies to attack from " + fromCountry);
						return false;
					}
				} 
				else {
					System.out.println("Attacker country and the defender country should be adjacent");
				}
			}
		}
		else {
			System.out.println("Please select the country owned by you(" + currentPlayerObj.getCurrentPlayer().name + ") as attackerCountry");
			return false;
		}
		return true;
	}

	public static int getRandomNumber(Integer maxDice) {
		Random randomGenerator;
		randomGenerator = new Random();
		return randomGenerator.nextInt(maxDice) + 1;
	}

	public static boolean battle(Country attackerCountry, Country defenderCountry, Integer attackerArmies, Integer defenderArmies) {

		lastDiceSelected = attackerArmies;
		Integer index = 0;
		Integer defenderArmiesKilled = 0;
		Integer attackerArmiesKilled = 0;
		ArrayList<Integer> defenderDices = new ArrayList<Integer>();
		ArrayList<Integer> attackerDices = new ArrayList<Integer>();

		for (int i = 0; i < attackerArmies; i++) {
			index = getRandomNumber(6);
			attackerDices.add(index);
		}
		for (int i = 0; i < defenderArmies; i++) {
			index = getRandomNumber(6);
			defenderDices.add(index);
		}
		Collections.sort(attackerDices);
		Collections.sort(defenderDices);

		if (defenderArmies > attackerArmies) {
			for (int i = 0; i < attackerArmies; i++) {
				if (attackerDices.get(i) > defenderDices.get(i)) {
					defenderArmiesKilled++;
					System.out.println("--- Attacker wins the battle ---");
				} 
				else {
					attackerArmiesKilled++;
					System.out.println("--- Defender wins the battle ---");
				}
			}
		} 
		else {

			for (int i = 0; i < defenderArmies; i++) {

				if (attackerDices.get(i) > defenderDices.get(i)) {
					defenderArmiesKilled++;
					System.out.println("--- Attacker wins the battle ---");
				} 
				else {
					attackerArmiesKilled++;
					System.out.println("--- Defender wins the battle ---");
				}
			}
		}

		System.out.println("AC" + attackerArmiesKilled);
		System.out.println("DC" + defenderArmiesKilled);
		attackerCountry.setNumberOfArmies(attackerCountry.getNumberOfArmies() - attackerArmiesKilled);
		defenderCountry.setNumberOfArmies(defenderCountry.getNumberOfArmies() - defenderArmiesKilled);

		return true;
	}

    
    /**
	 * This allows a player to move any number of armies from his owned country to
	 * any of his owned neighbor country, leaving behind at least 1 army unit.
	 *
	 * @param fromCname
	 * @param toCountryName
	 * @param numberOfArmies
	 * @param gameGraph
	 * @return true or false
	 */
	public static boolean fortify(String fromCname, String toCountryName, Integer numberOfArmies, Graph gameGraph) {
		Country toCountry = Country.getCountryByName(toCountryName, gameGraph);
		Country fromcountry = Country.getCountryByName(fromCname, gameGraph);

		if (fromcountry == null || toCountry == null) {
			System.out.println("One or both countries do not exist");
			return false;
		} else if (!(toCountry.getOwner().equalsIgnoreCase(fromcountry.getOwner()))) {
			System.out.println("A player has to own both the countries");
			return false;
		} else if (!(toCountry.getNeighbours().contains(fromcountry.getNumber()))) {
			System.out.println("Both countries should be adjacent");
			return false;
		} else if (!(fromcountry.getNumberOfArmies() - numberOfArmies > 0)) {
			System.out.println("You must leave at least 1 army unit behind");
			return false;
		}

		ArrayList<Integer> toCountryNeighbours = toCountry.getNeighbours();

		fromcountry.setNumberOfArmies(fromcountry.getNumberOfArmies() - numberOfArmies);
		toCountry.setNumberOfArmies(toCountry.getNumberOfArmies() + numberOfArmies);

		Country.updatePlayerListAndDeclareWinner(gameGraph);

		return true;
	}
}

/**
 * This Class handle players turn and calculate reinforcement armies
 */
class CurrentPlayer{
	
	private static CurrentPlayer currentPlayerObj = null;
	private ListIterator<Player> currentPlayerItr;
	private Player currentPlayer;
	private Integer numReinforceArmies;
	CardPlay cardPlayObj;
	
	public static CurrentPlayer getCurrentPlayerObj() {
		return currentPlayerObj;
	}


	private CurrentPlayer() {
		currentPlayerItr = Database.playerList.listIterator();
		cardPlayObj = CardPlay.getInstance();
	}
	
	/**
	 * Current Player get Instance method with SingleTone design
	 * @return
	 */
	public static CurrentPlayer getInstance(){
        if(currentPlayerObj==null)
        	currentPlayerObj= new CurrentPlayer();
        return currentPlayerObj;
    }
	
	public void setNumReinforceArmies(Integer numArmies) {
		this.numReinforceArmies = numArmies;
	}
	
	public Integer getNumReinforceArmies() {
		return this.numReinforceArmies;
	}
	
	public Player getCurrentPlayer() {
		return this.currentPlayer;
	}
	
	/**
	 * Go to next player.
	 * @param currentState
	 * @param gameGraph
	 */
	public void goToNextPlayer(State currentState, Graph gameGraph) {
		
    	if(currentPlayerItr.hasNext()) {
    		
    		numReinforceArmies = 0;
    		currentPlayer = currentPlayerItr.next();
    		
        	System.out.println("Current player is " + currentPlayer.getName());
    		Continent.updateContinitsOwner(gameGraph);
    		calculateReinforceentArmies();
    	}
		else {
			goToFirstPlayer(currentState, gameGraph);
		}	
    }
		
	/**
	 * Reset players turn whenever it comes to the end of the players list.
	 * @param currentState
	 * @param gameGraph
	 */
	public void goToFirstPlayer(State currentState, Graph gameGraph) {
		currentPlayerItr = Database.playerList.listIterator();
		numReinforceArmies = 0;
		currentPlayer = currentPlayerItr.next();
		
		System.out.println("Current player is " + currentPlayer.getName());
		Continent.updateContinitsOwner(gameGraph);
		calculateReinforceentArmies();
	}
	
	/**
	 * Calculate Reinforcement Armies 
	 */
	public void calculateReinforceentArmies() {
    	
    	Integer numOfCountries = currentPlayer.myCountries.size();
    	Integer numArmies = (numOfCountries/3);
    	
    	for(Continent continentItr : Database.continentList) {
    		if(continentItr.getOwner().equals(currentPlayer.name))
    			numArmies += continentItr.getControlValue();
    	}
    	
    	//Each player has at least 3 armies for reinforcement
    	numReinforceArmies += (numArmies>3) ? numArmies : 3;
    }
	
	/**
	 * Decrease Reinforcement Armies.
	 * @param numOfArmies
	 */
	public void decreaseReinforceentArmies(Integer numOfArmies) {
		numReinforceArmies -= numOfArmies;
	}
	
	/**
	 * Update The number of players for current Player.
	 * @param numArmies
	 */
	public void increaseCurrentPlayerArmies(Integer numArmies) {
		currentPlayer.setNumberOfArmies(currentPlayer.getNumberOfArmies() + numArmies);
		currentPlayerItr.set(currentPlayer);
	}
}
