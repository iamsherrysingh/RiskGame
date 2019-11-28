package Model;

import java.util.ArrayList;

public class CheaterPlayer implements IPlayer {

	private String name;
	private Integer number, numberOfArmies, numberOfFreeArmies;
	private ArrayList<Integer> myCountries = new ArrayList<Integer>();
	private Integer exchangeCardsTimes;
	public ArrayList<Card> playerCards;
	public boolean countryConquered;
	public boolean defenderRemoved;
	static Integer lastDiceSelected = null;
	
	public CheaterPlayer() {
		playerCards = new ArrayList<Card>();
	}

	public CheaterPlayer(Integer number, String name, Integer numberOfArmies) {
		this.number = number;
		this.name = name;
		this.numberOfArmies = numberOfArmies;
		playerCards = new ArrayList<Card>();
		exchangeCardsTimes = 0;
		countryConquered = false;
		defenderRemoved = false;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public PlayerStrategy getPlayerStrategy() {
		return PlayerStrategy.cheater; // Done
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public Integer getNumber() {
		return number;
	}

	@Override
	public void setNumber(Integer number) {
		this.number = number;
	}

	@Override
	public Integer getNumberOfArmies() {
		return numberOfArmies;
	}

	@Override
	public void setNumberOfArmies(Integer numberOfArmies) {
		this.numberOfArmies = numberOfArmies;
	}

	@Override
	public Integer getNumberOfFreeArmies() {
		return numberOfFreeArmies;
	}

	@Override
	public void setMyCountries(Integer number) {
		myCountries.add(number);
	}

	@Override
	public void setNumberOfFreeArmies(Integer numberOfFreeArmies) {
		this.numberOfFreeArmies = numberOfFreeArmies;
	}

	@Override
	public ArrayList<Integer> getMyCountries() {
		return myCountries;
	}

	public void setMyCountries(ArrayList<Integer> myCountries) {
		this.myCountries = myCountries;
	}

	@Override
	public Integer getExchangeCardsTimes() {
		return exchangeCardsTimes;
	}

	@Override
	public void setExchangeCardsTimes(Integer exchangeCardsTimes) {
		this.exchangeCardsTimes = exchangeCardsTimes;
	}

	@Override
	public ArrayList<Card> getPlayerCards() {
		return playerCards;
	}

	@Override
	public void setPlayerCards(Card card) {
		playerCards.add(card);
	}

	@Override
	public boolean getCountryConquered() {
		return false;
	}

	public void setPlayerCards(ArrayList<Card> playerCards) {
		this.playerCards = playerCards;
	}

	public boolean isCountryConquered() {
		return countryConquered;
	}

	@Override
	public void setCountryConquered(boolean countryConquered) {
		this.countryConquered = countryConquered;
	}

	@Override
	public boolean getDefenderRemoved() {
		return false;
	}

	public boolean isDefenderRemoved() {
		return defenderRemoved;
	}

	@Override
	public void setDefenderRemoved(boolean defenderRemoved) {
		this.defenderRemoved = defenderRemoved;
	}

	@Override
	public boolean reinforcement(String countryName, Integer numberOfArmies, Graph graphObj,
			CurrentPlayer currentPlayerObj) {

		for (Country country : graphObj.getAdjList()) {

			if (country.getOwner().equalsIgnoreCase(currentPlayerObj.currentPlayer.getName())) {
				country.setNumberOfArmies(country.getNumberOfArmies() * 2);
			}

		}

		GamePlay.getInstance().setCurrentOperation("Reinforcement done by cheater player");
		return true;
	}

	@Override
	public boolean attackAllout(String fromCountry, String toCountry, Graph graphObj, CurrentPlayer currentPlayerObj) {

		ArrayList<Country> countriesOwnedbyPlayer = new ArrayList<Country>();
		for (Country country : graphObj.getAdjList()) {
			if (country.getOwner().equalsIgnoreCase(currentPlayerObj.currentPlayer.getName())) {
				countriesOwnedbyPlayer.add(country);
			}
		}

		for (int i = 0; i < countriesOwnedbyPlayer.size(); i++) {

			ArrayList<Integer> neighboursOfCountry = countriesOwnedbyPlayer.get(i).neighbours;

			for (int j = 0; j < neighboursOfCountry.size(); j++) {
				Country.getCountryByNumber(neighboursOfCountry.get(j), graphObj.getInstance())
						.setOwner(currentPlayerObj.currentPlayer.getName());
			}
		}

		GamePlay.getInstance().setCurrentOperation("Performing all-out attack by cheater player");
		return true;
	}

	@Override
	public boolean fortify(String fromCname, String toCountryName, Integer numberOfArmies, Graph gameGraph) {

		boolean neighbourWithDifferentOwner = false;

		for (Country country : gameGraph.getAdjList()) {

			if (country.getOwner().equalsIgnoreCase(CurrentPlayer.getCurrentPlayerObj().getCurrentPlayer().getName())) {

				for (int i = 0; i < country.neighbours.size(); i++) {

					if (!Country.getCountryByNumber(country.neighbours.get(i), gameGraph).getOwner()
							.equalsIgnoreCase(CurrentPlayer.getCurrentPlayerObj().getCurrentPlayer().getName())) {

						neighbourWithDifferentOwner = true;
						break;

					}

				}

				if (neighbourWithDifferentOwner = true) {
					country.setNumberOfArmies(country.getNumberOfArmies() * 2);
					neighbourWithDifferentOwner = false;
				}

			}

		}

		System.out.println("Fortification done in cheater player");
		return true;
	}

	@Override
	/**
	 * This method returns the total number of countries owned by the players.
	 * 
	 * @param playerName The name of the player
	 * @param gameGraph  This is an object of the class Graph
	 * @return An integer value that is equal to the total number of countries owned
	 *         by the player
	 */
	public Integer getNumberOfCountriesOwned(String playerName, Graph gameGraph) {
		Integer numberOfCountriesOwned = 0;

		if (Database.getPlayerByName(playerName) == null)
			return -1;
		for (Country country : gameGraph.getAdjList()) {
			if (country.owner.equalsIgnoreCase(playerName)) {
				numberOfCountriesOwned += 1;
			}
		}
		return numberOfCountriesOwned;
	}

	@Override
	/**
	 * This method returns the total number of armies owned by the players.
	 * 
	 * @param gameGraph It is an object of the class Graph
	 * @returnAn integer value that is equal to the total number of armies owned by
	 *           the player
	 */
	public Integer getTotalArmiesOwnedByPlayer(Graph gameGraph) {
		Integer numberOfArmies = 0;

		if (Database.getPlayerByName(this.name) == null)
			return -1;
		for (Country country : gameGraph.getAdjList()) {
			if (country.owner.equalsIgnoreCase(this.name)) {
				numberOfArmies += country.numberOfArmies;
			}
		}
		return numberOfArmies;
	}

	@Override
	public boolean normalAttack(String fromCountry, String toCountry, Integer numDice, Graph graphObj,
			CurrentPlayer currentPlayerObj) {
		return false;
	}

	public static Integer getLastDiceSelected() {
		return lastDiceSelected;
	}

	public static void setLastDiceSelected(Integer lastDiceSelected) {
		CheaterPlayer.lastDiceSelected = lastDiceSelected;
	}
}
