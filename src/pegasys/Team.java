package pegasys;

public class Team 
{
	private String m_id;
	private String m_name;
	private String m_market;
	private String m_division;
	private String m_conference;
	
	public Team(String id, String name, String market, String division, String conference)
	{
		m_id = id;
		m_name = name;
		m_market = market;
		m_division = division;
		m_conference = conference;
	}
	
	public String getID()
	{
		return m_id;
	}
	
	public String getName()
	{
		return m_name;
	}
	
	public String getMarket()
	{
		return m_market;
	}
	
	public String getLongName()
	{
		return m_market + " " + m_name;
	}
	
	public String getDivision()
	{
		return m_division;
	}
	
	public String getConference()
	{
		return m_conference;
	}
}
