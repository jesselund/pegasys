package pegasys;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ConcurrentHashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;

public class _Server 
{
	public static final String SPORTSDATA_KEY = "ez6vx43qsas4tzwywp5q7wx4";
	
	protected static ObjectContainer DB = null;
	
	public static void main(String[] args) 
	{
		// add shutdown logic
		Runtime.getRuntime().addShutdownHook(new Thread() {
		      public void run() {
		    	  try 
		    	  {
		    		  // shutdown code goes here
		    	  }
		    	  catch(Throwable t)
		    	  {
		    		  t.printStackTrace();
		    	  }
		      }
		 }
		);

		// primary server logic
		try
		{
			DB = Db4oEmbedded.openFile(Db4oEmbedded.newConfiguration(), "/Users/jesse/Desktop/database.db40");
			//initializeTeams();
			//initializePlayers();
			//DB.store(Index.get(Index.Name.TeamsByID).values().toArray(new Team[0]));
			//DB.store(Index.get(Index.Name.PlayersByID).values().toArray(new Player[0]));
		}
		catch(Throwable t)
		{
			t.printStackTrace();
		}
		finally
		{
			DB.close();
		}
	}

	@SuppressWarnings("unchecked")
	protected static void initializePlayers() 
		throws ParserConfigurationException, SAXException, MalformedURLException, IOException, InterruptedException
	{
		String url = "http://api.sportsdatallc.org/nfl-t1/teams/@@TEAM@@/roster.xml?api_key=" + SPORTSDATA_KEY;
		
		// create the index hasmaps for the teams
		ConcurrentHashMap<String, Player> chm_players_by_id = new ConcurrentHashMap<String, Player>();
		ConcurrentHashMap<Team, Player> chm_players_by_team = new ConcurrentHashMap<Team, Player>();
		
		for(Team team : ((ConcurrentHashMap<String, Team>) Index.get(Index.Name.TeamsByID)).values())
		{
			// seed the URL
			URL u = new URL(url.replaceAll("@@TEAM@@", team.getID()));
			
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
		    			Player p = new Player(id, last, first, full, team);
		    			chm_players_by_id.put(id, p);
		    			chm_players_by_team.put(team, p);
		    		}
		    			
		    	}
		    }
		}
		
	    // add the index to the master collection
	    if(!chm_players_by_id.isEmpty())
	    	Index._indexes.put(Index.Name.PlayersByID, chm_players_by_id);
	    if(!chm_players_by_team.isEmpty())
	    	Index._indexes.put(Index.Name.PlayersByTeam, chm_players_by_team);
	}

	protected static void initializeTeams() 
			throws ParserConfigurationException, SAXException, MalformedURLException, IOException
	{
		URL url = new URL("http://api.sportsdatallc.org/nfl-t1/teams/hierarchy.xml?api_key=" + SPORTSDATA_KEY);
		
		// create the index hasmaps for the teams
		ConcurrentHashMap<String, Team> chm_teams_by_id = new ConcurrentHashMap<String, Team>();
		
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
	    			chm_teams_by_id.put(id, t);
	    		}
	    			
	    	}
	    }
	    
	    // add the index to the master collection
	    if(!chm_teams_by_id.isEmpty())
	    	Index._indexes.put(Index.Name.TeamsByID, chm_teams_by_id);
	}
}
