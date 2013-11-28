package pegasys;

import java.util.concurrent.ConcurrentHashMap;

public abstract class Index 
{
	public enum Name
	{
		TeamsByID,
		PlayersByID,
		PlayersByTeam;
	}
	
	protected static final ConcurrentHashMap<Index.Name, ConcurrentHashMap<?,?>> _indexes = 
			new ConcurrentHashMap<Index.Name, ConcurrentHashMap<?,?>>();

	static
	{
		_indexes.put(Name.TeamsByID, new ConcurrentHashMap<String, Team>());
	}
	
	public static ConcurrentHashMap<?,?> get(Index.Name index)
	{
		return _indexes.get(index);
	}
}
