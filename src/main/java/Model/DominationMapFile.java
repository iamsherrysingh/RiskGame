package Model;



import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/** 
 * This Class read and write data from/to file in the domination format.
 */
public class DominationMapFile {

	    protected String continents;
		protected String countries;
		protected String borders;
		Database database = Database.getInstance();


		/**
		 * This reads the maps file and stores the country, continent and border details
		 * in their variables This is used by loadMap(). The variables generated
		 * by this method are used throughout the game.
		 *
		 * @param mapFile It is the name of the map file that is to be executed
		 * @throws FileNotFoundException Throws an exception if the file is not found
		 * @return true(If the method is executed completely)
		 */
		public boolean readMapIntoVariables(String mapFile) throws FileNotFoundException {
			  
			// Read Continents
			try (BufferedReader br = new BufferedReader(new FileReader(mapFile))) {
				StringBuilder sb = new StringBuilder();
				String line = br.readLine().trim();
				int continentsEncountered = 0;
				while (line != null) {
					if (line.equals("[countries]"))
						break;
					if (continentsEncountered == 1) {
						sb.append(line);
						sb.append(System.lineSeparator());
					}

					if (line.equals("[continents]")) {
						continentsEncountered = 1;
						sb.append(line);
						sb.append(System.lineSeparator());
					}
					line = br.readLine();
				}
				continents = sb.toString();
				continents = continents.trim();
				String continentLine[] = continents.split("\n");
				for (int i = 1; i < continentLine.length; i++) {
					continentLine[i] = continentLine[i].trim();
					String split[] = continentLine[i].split(" ");
					Continent continent = new Continent(Database.getInstance().getContinentList().size() + 1, split[0],
							Integer.parseInt(split[1]), split[2]);
					database.getContinentList().add(continent);
				}
			} catch (FileNotFoundException e) {

			} catch (IOException e) {

			} catch (Exception e) {

			}

			// Read countries
			try (BufferedReader br = new BufferedReader(new FileReader(mapFile))) {
				StringBuilder sb = new StringBuilder();
				String line = br.readLine();
				int countriesEncountered = 0;
				while (line != null) {
					if (line.equals("[borders]"))
						break;
					if (countriesEncountered == 1) {
						sb.append(line);
						sb.append(System.lineSeparator());
					}
					if (line.equals("[countries]")) {
						countriesEncountered = 1;
						sb.append(line);
						sb.append(System.lineSeparator());
					}
					line = br.readLine();
				}
				countries = sb.toString();
				countries = countries.trim();
			} catch (FileNotFoundException e) {

			} catch (IOException e) {

			} catch (Exception e) {

			}

			// Read Borders
			try {
				BufferedReader br = new BufferedReader(new FileReader(mapFile));
				StringBuilder sb = new StringBuilder();
				String line = br.readLine();
				int bordersEncountered = 0;
				while (line != null) {
					if (bordersEncountered == 1) {
						sb.append(line);
						sb.append(System.lineSeparator());
					}
					if (line.equals("[borders]")) {
						bordersEncountered = 1;
						sb.append(line);
						sb.append(System.lineSeparator());
					}
					line = br.readLine();
				}
				borders = sb.toString();
				borders = borders.trim();
			} catch (FileNotFoundException e) {

			} catch (IOException e) {

			} catch (Exception e) {

			}

			return true;
		}

		
		/**
		 * This method operates on the gameGraph variable and converts it to map file.
		 *
		 * @param gameGraph It is the object of the class Graph
		 * @param mp name of map
		 * @throws IOException If the Input or Output file is invalid
		 * @return true(If the method executes and the map is saved) or false(If no map
		 *         name is entered or is invalid)
		 */
		public boolean writeMapFile(Graph gameGraph, String mp, File f) throws IOException {

					FileWriter writer = new FileWriter(f);
					
					writer.write("name " + mp + System.getProperty("line.separator"));
					writer.write(System.getProperty("line.separator"));
					writer.write("[files]" + System.getProperty("line.separator"));
					writer.write("pic sample.jpg" + System.getProperty("line.separator"));
					writer.write("map sample.gif" + System.getProperty("line.separator"));
					writer.write("crd sample.cards" + System.getProperty("line.separator"));
					writer.write("prv world.jpg" + System.getProperty("line.separator"));
					writer.write(System.getProperty("line.separator"));
					writer.write("[continents]" + System.getProperty("line.separator"));
					
					
					for (int i = 0; i < database.getContinentList().size(); i++) {
						Continent continent = database.getContinentList().get(i);
						writer.write(continent.getName() + " " + continent.getControlValue() + " " + continent.getColor());
						if (i < database.getContinentList().size() - 1) {
							writer.write(System.getProperty("line.separator"));
						}
					}
					
					
					writer.write(System.getProperty("line.separator"));
					writer.write(System.getProperty("line.separator"));
					writer.write("[countries]" + System.getProperty("line.separator"));
					
					
					int cnt = 0;
					for (Country country : gameGraph.getAdjList()) {
						cnt++;
						String  CountryName = country.name;
						Integer ContiNumber = country.inContinent;
						Integer coordinateOne = country.coOrdinate1;
						Integer coordinateTwo = country.getCoOrdinate2;
						writer.write(cnt + " " + CountryName + " " + ContiNumber + " " + coordinateOne + " "
								+ coordinateTwo + System.getProperty("line.separator"));
					}
					
					
					writer.write(System.getProperty("line.separator"));
					writer.write("[borders]" + System.getProperty("line.separator"));


					cnt = 0;
					for (Country country : gameGraph.getAdjList()) {
						cnt++;
						ArrayList<Integer> NeighbourList = new ArrayList<Integer>();
						NeighbourList = country.neighbours;
						String borderString = "";
						for (int i = 0; i < NeighbourList.size(); i++) {
							borderString = borderString + " " + NeighbourList.get(i);
						}
						writer.write(cnt + borderString + System.getProperty("line.separator"));
					}
					
					
					writer.close();
					
					
					
					return true;

		}
}
