package Model;

import org.junit.*;

import static org.junit.Assert.*;

/**
 * It contains different Junit tests for the methods defined in
 * {@link Model.Country}
 * 
 * @see Country.java
 * @author birjotsingh17
 */

public class CountryTest {
	static GamePlay gamePlay;

	/**
	 * This method runs before every test method and creates an object for GamePlay
	 * and also loads a map
	 * 
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
		gamePlay = GamePlay.getInstance();
		gamePlay.editMap("map.map");
	}

	/**
	 * This method runs after each and every test method and clears the instance
	 * changed by the test method
	 * 
	 * @throws Exception
	 */
	@After
	public void tearDown() throws Exception {
		gamePlay.getGraphObj().getAdjList().clear();
		gamePlay = null;
		Database.getInstance().getContinentList().clear();
		Database.getInstance().getPlayerList().clear();
	}
	
	

	/**
	 * This is a jUnit test for {@link Model.Country#removeCountry(String, Graph)}
	 */
	@Test
	public void removeCountry() {
		boolean removedCounryExists = false;
		Country.removeCountry("India", gamePlay.getGraphObj());
		Country indiaObject = Country.getCountryByName("India", gamePlay.getGraphObj());

		if (indiaObject != null)
			removedCounryExists = true;
		assertFalse(removedCounryExists);
	}

	/**
	 * This is a jUnit test for {@link Model.Country#removeCountry(String, Graph)}
	 */
	@Test
	public void removeCountry2() {
		Country india = Country.getCountryByName("nonExistentCountry", gamePlay.getGraphObj());
		boolean removedCounryExists = false;
		if (india != null)
			removedCounryExists = true;
		assertEquals(removedCounryExists, false);
	}

	/**
	 * This is a jUnit test for {@link Model.Country#removeNeighbour(String, String, Graph))}
	 */
	@Test
	public void removeNeighbour1() {
		boolean neighbourStillExists = true;
		Country.removeNeighbour("EgyPT", "Southern-Europe", gamePlay.getGraphObj());
		neighbourStillExists = Country.getCountryByName("Egypt", gamePlay.getGraphObj()).neighbours.contains(20);
		assertFalse(neighbourStillExists);
	}

	/**
	 * This is a jUnit test for {@link Model.Country#removeNeighbour(String, String, Graph))}
	 */
	@Test
	public void removeNeighbour2() {
		assertFalse(Country.removeNeighbour("EgyPT", "Alaska", gamePlay.getGraphObj()));
	}

	/**
	 * This is a jUnit test for {@link Model.Country#checkExistenceOfCountry(String, Graph))}
	 */
	@Test
	public void checkExistenceOfCountry() {
		assertFalse(Country.checkExistenceOfCountry("Bir", gamePlay.getGraphObj()));
	}

	/**
	 * This is a jUnit test for {@link Model.Country#checkExistenceOfCountry(String, Graph))}
	 */
	@Test
	public void checkExistenceOfCountry2() {
		assertTrue(Country.checkExistenceOfCountry("chINa", gamePlay.getGraphObj()));
	}

}