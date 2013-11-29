package pegasys;

public class Week 
{
	private Season m_season;
	private int m_week;

	public Week(Season season, int number)
	{
		m_season = season;
		m_week = number;
	}
	
	public static final String createKey(Season season, int number)
	{
		return String.valueOf(season.getYear()) + ":" + season.getType().toString() + ":" + String.valueOf(number);
	}
	
	public String getKey()
	{
		return createKey(m_season, m_week);
	}
	
	public int getWeekNumber()
	{
		return m_week;
	}

}
