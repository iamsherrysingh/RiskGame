package Model;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class MapTest {

	static Mapx map;
	static Graph g;

	@Before
	public void before() throws Exception {
		map = new Mapx();
		g = map.createGameGraph("src/main/resources/map.map");
	}

	@After
	public void after() throws Exception {
		map = null;
		g = null;
		Database.getInstance().getContinentList().clear();
		Database.getInstance().getPlayerList().clear();
	}

	//addContinentTests
	@Test
	public void addContinent() {
		//empty continent name
		boolean check = Continent.addContinent(" ", 12);
		assertFalse(check);
		
	}
	
	@Test
	public void addContinent2() {
		//existing continent name
		boolean check = Continent.addContinent("Europe", 12);
		assertFalse(check);
		
	}
	
	@Test
	public void addContinent3() {
		//control value
		boolean check = Continent.addContinent("Bir", 0);
		assertFalse(check);
		
	}
	
	
	
}
