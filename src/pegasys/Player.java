package pegasys;

public class Player 
{
	private String m_id;
	private String m_FullName;
	private String m_LastName;
	private String m_FirstName;
	private Team m_team;
	
	public Player(String id, String last, String first, String full, Team team)
	{
		m_id = id;
		m_LastName = last;
		m_FirstName = first;
		m_FullName = full;
		m_team = team;
	}
	
	public String getID() {
		return m_id;
	}
	
	public String getLastName() {
		return m_LastName;
	}
	
	public String getFirstName() {
		return m_FirstName;
	}

	public String getFullName() {
		return m_FullName;
	}
	
	public Team getTeam() {
		return m_team;
	}
	
}
