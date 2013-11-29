package pegasys;

public class Season 
{
	public enum Type
	{
		Pre ("PRE"),
		Post ("PST"),
		Regular ("REG");
		
		private String m_type = null;
		
		private Type(String type)
		{
			m_type = type;
		}
		
		public String toString()
		{
			return m_type;
		}
	}
	
	private Type m_type = null;
	private int m_weeks_in_season = 0;
	private int m_year = 0;
	
	protected Season(Type type, int year, int weeks) 
	{
		m_type = type;
		m_year = year;
		m_weeks_in_season = weeks;
	}
	
	public int getWeeksInSeason()
	{
		return m_weeks_in_season;
	}
	
	public Type getType()
	{
		return m_type;
	}
	
	public int getYear()
	{
		return m_year;
	}
	
	public String getID()
	{
		return String.valueOf(m_year) + ":" + m_type.toString();
	}
	 
	public String toString()
	{
		return getID();
	}


}
