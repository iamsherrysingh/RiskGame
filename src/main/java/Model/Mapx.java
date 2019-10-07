package Model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import Database.Database;

public class Mapx {
    private String continents, countries, borders;
    Database database = Database.getInstance();

    private void loadMap(String mapFile) throws FileNotFoundException {
        // Read Continents
        try (BufferedReader br = new BufferedReader(new FileReader(mapFile))) {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();
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
                Continent continent = new Continent(database.getInstance().getContinentList().size() + 1, split[0], Integer.parseInt(split[1]), split[2]);
                database.getContinentList().add(continent);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
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
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Read Borders
        try (BufferedReader br = new BufferedReader(new FileReader(mapFile))) {
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
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Create the main gameGraph
    public Graph createGameGraph(String mapFile) {
        try {
            loadMap(mapFile);
        } catch (FileNotFoundException f) {
            System.out.println(f.getMessage());
        }
        Graph gameGraph = Graph.getInstance();
        Scanner countryScanner = new Scanner(this.countries);
        countryScanner.nextLine(); // Ignoring first line of this.countries
        while (countryScanner.hasNext()) {
            String lineCountry = countryScanner.nextLine();
            lineCountry = lineCountry.trim();
            String countryLineSubstrings[] = lineCountry.split(" ");

            ArrayList<Integer> neighbours = new ArrayList<Integer>();
            Scanner borderScanner = new Scanner(this.borders);
            borderScanner.nextLine(); // Ignoring first line of this.borders
            while (borderScanner.hasNext()) {
                String lineBorder = borderScanner.nextLine();
                lineBorder = lineBorder.trim();
                String borderLineSubstrings[] = lineBorder.split(" ");
                if (Integer.parseInt(borderLineSubstrings[0]) == Integer.parseInt(countryLineSubstrings[0])) {
                    for (int i = 1; i < borderLineSubstrings.length; i++) {
                        neighbours.add(Integer.parseInt(borderLineSubstrings[i]));
                    }
                    break;
                }
            }
            Country country = new Country(Integer.parseInt(countryLineSubstrings[0]), countryLineSubstrings[1],
                    Integer.parseInt(countryLineSubstrings[2]), null, 0, Integer.parseInt(countryLineSubstrings[3]),
                    Integer.parseInt(countryLineSubstrings[4]), neighbours);
            gameGraph.getAdjList().add(country);
        }
        return gameGraph;
    }

    // Birjot
    public static File createFile(String mapName) throws IOException {
        Scanner sc1 = new Scanner(System.in);
        File file = new File("src/main/resources/" + mapName);
        if (file.createNewFile()) {
            System.out.println("map saved");
        } else {
            System.out.println("File with same name already exists!!");
            System.out.println("press 1 to overwrite");
            System.out.println("press any other number to cancel");
            Integer in = 0;

            try {
                in = sc1.nextInt();
            } catch (Exception e) {
                System.out.println("Number Expected " + e.getMessage());
            }
            if (in == 1) {
                if (file.delete()) {
                    // delete file to make new one with same name
                } else {
                    System.out.println("something went wrong");
                }
                createFile(mapName);
            } else {
                System.out.println("cancelled");
            }
        }
        return file;
    }

    public void saveMap(Graph gameGraph) throws IOException {
        ArrayList<Country> ct = gameGraph.adjList;
        Iterator itr = ct.iterator();
        Scanner scCreate = new Scanner(System.in);
        System.out.println("Enter new map name");
        String mapName = scCreate.nextLine();
        mapName = mapName + ".map";
        // Create the file
        File f = createFile(mapName);
        FileWriter writer = new FileWriter(f);
        writer.write("[files]" + System.getProperty("line.separator"));
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
        writer.write("[countries] " + System.getProperty("line.separator"));
        Integer countitr = 0;
        while (itr.hasNext()) {
            Country country = (Country) itr.next();
            countitr++;
            String CountryName = country.name;
            Integer ContiNumber = country.inContinent;
            Integer coordinateOne = country.coOrdinate1;
            Integer coordinateTwo = country.getCoOrdinate2;
            writer.write(countitr + " " + CountryName + " " + ContiNumber + " " + coordinateOne + " " + coordinateTwo
                    + System.getProperty("line.separator"));
        }
        writer.write(System.getProperty("line.separator"));
        itr = ct.iterator();
        writer.write("[borders] " + System.getProperty("line.separator"));

        Integer countIterator = 0;
        while (itr.hasNext()) {
            countIterator++;
            Country country = (Country) itr.next();
            ArrayList<Integer> NeighbourList = new ArrayList<Integer>();
            NeighbourList = country.neighbours;
            String borderString = "";
            for (int i = 0; i < NeighbourList.size(); i++) {
                borderString = borderString + " " + NeighbourList.get(i);
            }
            writer.write(countIterator + borderString + System.getProperty("line.separator"));
        }
        writer.close();
    }

    public boolean validateMap(Graph gameGraph) {
        Integer startPosition = 1;
        int count = 0;
        boolean visited[] = new boolean[gameGraph.getAdjList().size() + 1];
        Stack<Integer> stack = new Stack<Integer>();
        stack.push(startPosition);

        while (stack.empty() == false) {
            Integer topElement = stack.peek();
            stack.pop();
            if (visited[topElement] == false) {
                count++;
                visited[topElement] = true;
            }

            Iterator<Integer> itr = gameGraph.getAdjList().get(topElement - 1).getNeighbours().iterator();
            while (itr.hasNext()) {
                Integer next = itr.next();
                if (visited[next] == false) {
                    stack.push(next);
                }
            }
        }
        System.out.println(count);
        System.out.println(gameGraph.getAdjList().size());
        if (count == gameGraph.getAdjList().size()) {// if count==no. of countries return true;
            return true;
        }
        return false;
    }

    public void addCountry(String newCountry, String inContinent, Graph gameGraph) {
        Integer continentNumber = -1;
        for (Continent continent : database.getContinentList()) {
            if (continent.getName().equalsIgnoreCase(inContinent)) {
                continentNumber = continent.getNumber();
            }
        }
        if (continentNumber == -1) {
            System.out.println("Continent: " + inContinent + " not found!");
            System.out.println("Perhaps add one first");
            return;
        }
        Country country = new Country(gameGraph.getAdjList().size() + 1, newCountry, continentNumber, null, null, 0, 0, new ArrayList<Integer>());
        gameGraph.getAdjList().add(country);
    }

    public void addNeighbour(String countryWithNewNeighbour, String neighbour, Graph gameGraph) {
        Integer numberOfCountryWithNewNeighbour = -1;
        for (Country country : gameGraph.getAdjList()) {
            if (country.getName().equalsIgnoreCase(countryWithNewNeighbour)) {
                numberOfCountryWithNewNeighbour = country.getNumber();
            }
        }
        if (numberOfCountryWithNewNeighbour == -1) {
            System.out.println("Country: " + countryWithNewNeighbour + " not in the map!");
            System.out.println("Perhaps add one first.");
            return;
        }
        Integer neighbourNumber = -1;
        for (Country country : gameGraph.getAdjList()) {
            if (country.getName().equalsIgnoreCase(neighbour)) {
                neighbourNumber = country.getNumber();
            }
        }
        if (neighbourNumber == -1) {
            System.out.println("Neighbour Country: " + neighbour + " not in the map!");
            return;
        }
        gameGraph.getAdjList().get(numberOfCountryWithNewNeighbour - 1).getNeighbours().add(neighbourNumber); //Added new neighbour for this country
        gameGraph.getAdjList().get(neighbourNumber - 1).getNeighbours().add(numberOfCountryWithNewNeighbour); //Added this country as neighbour to it's neighbour
    }

    public void removeNeighbour(String countryThatLosesNeighbour, String neighbour, Graph gameGraph) {
        Integer countryNumber = -1;
        for (Country country : gameGraph.getAdjList()) {
            if (country.getName().equalsIgnoreCase(countryThatLosesNeighbour)) {
                countryNumber = country.getNumber();
            }
        }
        if (countryNumber == -1) {
            System.out.println("Country: " + countryThatLosesNeighbour + " not in the map!");
            return;
        }
        Integer neighbourNumber = -1;
        for (Country country : gameGraph.getAdjList()) {
            if (country.getName().equalsIgnoreCase(neighbour)) {
                neighbourNumber = country.getNumber();
            }
        }
        if (neighbourNumber == -1) {
            System.out.println("Neighbour Country: " + neighbour + " not in the map!");
            return;
        }
        boolean neighbourPresentInList = false;
        for (Integer singleNeighbour : gameGraph.getAdjList().get(countryNumber - 1).getNeighbours()) {
            if (gameGraph.getAdjList().get(singleNeighbour - 1).getName().equalsIgnoreCase(neighbour)) {
                neighbourPresentInList = true;
            }
        }
        if (neighbourPresentInList == false) {
            System.out.println("These countries are not neighbours");
            return;
        }
        gameGraph.getAdjList().get(countryNumber - 1).getNeighbours().remove(neighbourNumber);
        gameGraph.getAdjList().get(neighbourNumber - 1).getNeighbours().remove(countryNumber);
    }

    public void removeCountry(String NameOfCountryToRemove, Graph gameGraph) {
        Country countryToRemove = null;
        for (Country country : gameGraph.getAdjList()) {
            if (country.getName().equalsIgnoreCase(NameOfCountryToRemove)) {
                countryToRemove = country;
            }
        }
        if (countryToRemove == null) {
            System.out.println("Country: " + NameOfCountryToRemove + " not in the map!");
            return;
        }
        for (Integer neighbour : countryToRemove.getNeighbours()) {
            ArrayList<Integer> neighbourListOfNeighbour = gameGraph.getAdjList().get(neighbour - 1).getNeighbours();
            neighbourListOfNeighbour.remove(countryToRemove.getNumber());
        }
        gameGraph.getAdjList().remove(countryToRemove);

        //Now fixing the serial number mess after a country is removed
        //serial number for every country after the removedd country is decremented
        for (int i = countryToRemove.getNumber() - 1; i < gameGraph.getAdjList().size(); i++) {
            Integer oldSerialNumber = gameGraph.getAdjList().get(i).getNumber();
            Integer newSerialNumber = oldSerialNumber - 1;
            gameGraph.getAdjList().get(i).setNumber(newSerialNumber);
        }

        //neighbour list updated for every affected country
        for (Country country : gameGraph.getAdjList()) {
            ArrayList<Integer> newNeighbourList = new ArrayList<Integer>();
            for (Integer singleNeighbour : country.getNeighbours()) {
                if (singleNeighbour > countryToRemove.getNumber()) {
                    newNeighbourList.add(singleNeighbour - 1);
                } else {
                    newNeighbourList.add(singleNeighbour);
                }
            }
            country.setNeighbours(newNeighbourList);
        }

    }

    public void addContinent(String continentName, Integer controlValue) {
        Continent newContinent = new Continent(Database.getInstance().getContinentList().size() + 1, continentName, controlValue, "");
        Database.getInstance().getContinentList().add(newContinent);
    }

    public void removeContinent(String continentToRemove, Graph gameGraph) {
        Integer serialNumberOfContinentToRemove = -1;
        Continent continent=null;
        for (Continent singleContinent : Database.getInstance().getContinentList()) {
            if (singleContinent.getName().equalsIgnoreCase(continentToRemove)) {
                serialNumberOfContinentToRemove = singleContinent.getNumber();
                continent= singleContinent;
            }
        }
        if (serialNumberOfContinentToRemove == -1) {
            System.out.println("Continent: " + continentToRemove + " not found in the map!");
            return;
        }
        ArrayList<String> countriesInContinentToRemove = new ArrayList<String>();
        for (Country country : gameGraph.getAdjList()) {
            if (country.getInContinent() == serialNumberOfContinentToRemove) {
                countriesInContinentToRemove.add(country.getName());
            }
        }

        //Start deleting all the countries in this continent
        for (String country : countriesInContinentToRemove) {
            this.removeCountry(country, gameGraph);
        }
        Database.getInstance().getContinentList().remove(continent);
    }

    public void addArmiesToCountry(String countryName, Integer numberOfArmies, Graph gameGraph) {
        Country countryToReinforce = null;
        for (Country country : gameGraph.getAdjList()) {
            if (country.getName().equalsIgnoreCase(countryName)) {
                countryToReinforce = country;
            }
        }
        if (countryToReinforce == null) {
            System.out.println("Country: " + countryName + " not in the map!");
            return;
        }
        countryToReinforce.setNumberOfArmies(countryToReinforce.getNumberOfArmies() + numberOfArmies);
    }

    public void removeArmiesFromCountry(String countryName, Integer numberOfArmies, Graph gameGraph) {
        Country countryToWeaken = null;
        for (Country country : gameGraph.getAdjList()) {
            if (country.getName().equalsIgnoreCase(countryName)) {
                countryToWeaken = country;
            }
        }
        if (countryToWeaken == null) {
            System.out.println("Country: " + countryName + " not in the map!");
            return;
        }
        if ((countryToWeaken.getNumberOfArmies() - numberOfArmies) <= 0) {
            countryToWeaken.setNumberOfArmies(countryToWeaken.getNumberOfArmies() - numberOfArmies);
        } else {
            System.out.println("Cannot have less than 1 army in a country!");
        }
    }

}