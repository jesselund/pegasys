package pegasys;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Game 
{
	private String m_id;
	private Team m_HomeTeam;
	private Team m_AwayTeam;
	private String m_FirstName;
	private Team m_team;

	public static void main(String[] args) throws ParseException 
	{
		//DateTime d = ISODateTimeFormat.dateParser().parseDateTime("2013-11-22T01:25:00+00:00");
		SimpleDateFormat df = new SimpleDateFormat("yyyy-mm-dd'T'HH:mm:ssX");
		Date date = df.parse("2013-11-22T01:25:00+00:00");
		//System.out.println(new Date(d.getMillis()));
		System.out.println(date);
		System.out.println(date.getTime());
	}
}
