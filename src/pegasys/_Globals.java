package pegasys;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public abstract class _Globals 
{
	public static final String SPORTSDATA_KEY = "ez6vx43qsas4tzwywp5q7wx4";

	protected static void initializeTeams() 
			throws ParserConfigurationException, SAXException, MalformedURLException, IOException
		{
			URL url = new URL("http://api.sportsdatallc.org/nfl-t1/teams/hierarchy.xml?api_key=" + _Globals.SPORTSDATA_KEY);
			System.out.print("initializing teams...");
			
			//Get the DOM Builder Factory
		    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		    DocumentBuilder builder = factory.newDocumentBuilder();
		    Document document = builder.parse(url.openStream());

		    // constants for enumeration logic
		    String CONFERENCE = null;
		    String DIVISION = null;
		    
		    // enumerate teams sequentially
		    NodeList root = document.getElementsByTagName("*");
		    for(int i = 0; i < root.getLength(); i++)
		    {
		    	Node node = root.item(i);
		    	if(node instanceof Element)
		    	{
		    		Element e = (Element)node;
		    		
		    		// conference
		    		if(e.getTagName().equalsIgnoreCase("conference"))
		    			CONFERENCE = e.getAttribute("name");
		    		// division
		    		if(e.getTagName().equalsIgnoreCase("division"))
		    			DIVISION = e.getAttribute("name");
		    		// team
		    		if(e.getTagName().equalsIgnoreCase("team"))
		    		{
		    			String id = e.getAttribute("id");
		    			String name = e.getAttribute("name");
		    			String market = e.getAttribute("market");
		    			Team t = new Team(id, name, market, DIVISION, CONFERENCE);
		    			
		    			// add team to global index
		    			((ConcurrentHashMap<String, Team>)Index._indexes.get(Index.Name.TeamsByID)).put(id, t);
		    		}
		    			
		    	}
		    }
		    System.out.println("done");
		}

	protected static void initializePlayers() 
			throws ParserConfigurationException, SAXException, MalformedURLException, IOException, InterruptedException
		{
			String url = "http://api.sportsdatallc.org/nfl-t1/teams/@@TEAM@@/roster.xml?api_key=" + _Globals.SPORTSDATA_KEY;
			System.out.print("Initializing players...");
			
			for(Team team : ((ConcurrentHashMap<String, Team>) Index.get(Index.Name.TeamsByID)).values())
			{
				// seed the URL
				URL u = new URL(url.replaceAll("@@TEAM@@", team.getID()));
				System.out.print(team.getID() + "...");
				
				// mandatory API delay
				Thread.sleep(1100);
		
				//Get the DOM Builder Factory
			    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			    DocumentBuilder builder = factory.newDocumentBuilder();
			    Document document = builder.parse(u.openStream());
		
			    // enumerate teams sequentially
			    NodeList root = document.getElementsByTagName("*");
			    for(int i = 0; i < root.getLength(); i++)
			    {
			    	Node node = root.item(i);
			    	if(node instanceof Element)
			    	{
			    		Element e = (Element)node;
			    		
			    		// conference
			    		if(e.getTagName().equalsIgnoreCase("player"))
			    		{
			    			String id = e.getAttribute("id");
			    			String last = e.getAttribute("name_last");
			    			String first = e.getAttribute("name_first");
			    			String full = e.getAttribute("name_full");
			    			short jersey = Short.parseShort(e.getAttribute("jersey_number"));
			    			String position = e.getAttribute("position");
			    			Player p = new Player(id, last, first, full, team, jersey, position);
			    			
			    			// populate global indexes
			    			((ConcurrentHashMap<String, Player>)Index._indexes.get(Index.Name.PlayersByID)).put(id, p);
			    		}
			    			
			    	}
			    }
			}
			System.out.println("done");
		}	

	protected static Season initializeSeason(Season.Type type, int year) 
			throws ParserConfigurationException, SAXException, MalformedURLException, IOException, ParseException, InterruptedException
		{ 
			System.out.print("Initializing season " + type.toString() + "...");
			
			URL url = new URL("http://api.sportsdatallc.org/nfl-t1/" + String.valueOf(year) + "/" + 
									type.toString() + "/schedule.xml?api_key=" + _Globals.SPORTSDATA_KEY);
			
			// mandatory API delay
			Thread.sleep(1100);

			//Get the DOM Builder Factory
		    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		    DocumentBuilder builder = factory.newDocumentBuilder();
		    Document document = builder.parse(url.openStream());
		    
		    // constants for enumeration
		    int weeks_in_season = 0;
		    
		    // enumerate teams sequentially
		    NodeList root = document.getElementsByTagName("*");
		    for(int i = 0; i < root.getLength(); i++)
		    {
		    	Node node = root.item(i);
		    	if(node instanceof Element)
		    	{
		    		Element e = (Element)node;
		    		
		    		// week
		    		if(e.getTagName().equalsIgnoreCase("week"))
		    		{
		    			int week = Integer.parseInt(e.getAttribute("week"));
		    			weeks_in_season = (weeks_in_season < week ? week : weeks_in_season);
		    		}
		    	}
		    }
		    
		    System.out.println("done");
		    
		    Season season = new Season(type, year, weeks_in_season);
		    ((ConcurrentHashMap<String, Season>)Index._indexes.get(Index.Name.SeasonsByID)).put(season.getID(), season);
		    return season;
		}

	protected static Week initializeWeek(Season season, int number) 
			throws ParserConfigurationException, SAXException, MalformedURLException, IOException, ParseException, InterruptedException
		{ System.out.print("Initializing week " + Week.createKey(season, number) + "...");
			
		// constants for parsing and enumeration
			SimpleDateFormat df = new SimpleDateFormat("yyyy-mm-dd'T'HH:mm:ssX");

			// get or create (and add) the week to the global index
			Week week = (Week)Index._indexes.get(Index.Name.WeeksByKey).get(Week.createKey(season, number)); 
			if(week == null)
			{
				week = new Week(season, number);
				((ConcurrentHashMap<String, Week>)Index._indexes.get(Index.Name.WeeksByKey)).put(week.getKey(), week);
			}

			// process weekly schedule
			URL url = new URL("http://api.sportsdatallc.org/nfl-t1/" + 
						String.valueOf(season.getYear()) + "/" + 
						season.getType().toString() + "/" + 
						String.valueOf(week.getWeekNumber()) + "/schedule.xml?api_key=" + _Globals.SPORTSDATA_KEY);
			// mandatory API delay
			Thread.sleep(1100);

			//Get the DOM Builder Factory
		    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		    DocumentBuilder builder = factory.newDocumentBuilder();
		    Document document = builder.parse(url.openStream());

		    // enumerate games sequentially
		    NodeList root = document.getElementsByTagName("*");
		    for(int i = 0; i < root.getLength(); i++)
		    {
		    	Node node = root.item(i);
		    	if(node instanceof Element)
		    	{
		    		Element e = (Element)node;
		    		
		    		// game
		    		if(e.getTagName().equalsIgnoreCase("game"))
		    		{
		    			String id = e.getAttribute("id");
		    			String schedule = e.getAttribute("scheduled");
		    			String home = e.getAttribute("home");
		    			String away = e.getAttribute("away");
		    			
		    			Date date = df.parse(schedule);
		    			Team h = (Team)Index._indexes.get(Index.Name.TeamsByID).get(home);
		    			Team a = (Team)Index._indexes.get(Index.Name.TeamsByID).get(away);
		    			Game g = new Game(id, h, a, date.getTime(), season, week);
		    			
		    			// add the game to the global indexes
		    			((ConcurrentHashMap<String, Game>)Index._indexes.get(Index.Name.GamesByID)).put(id, g);
		    			((ConcurrentHashMap<Season, Game>)Index._indexes.get(Index.Name.GamesBySeason)).put(season, g);	    			
		    		}	    			
		    	}
		    }
		    
		    // add this week to the global indexes
		    ((ConcurrentHashMap<String, Week>)Index._indexes.get(Index.Name.WeeksByKey)).put(week.getKey(), week);
			((ConcurrentHashMap<Season, Week>)Index._indexes.get(Index.Name.WeeksBySeason)).put(season, week);	  
			
			System.out.println("done");
		    return week;
		}

}
