package pegasys.util;

import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

public abstract class MapSorter 
{
	public static Map sortByValue(Map unsortedMap)
	{
		Map sortedMap = new TreeMap(new ValueComparator(unsortedMap));
		sortedMap.putAll(unsortedMap);
		return sortedMap;
	}
	
	public static Map sortByKey(Map unsortedMap)
	{
		Map sortedMap = new TreeMap();
		sortedMap.putAll(unsortedMap);
		return sortedMap;
	}
	
	private static class ValueComparator implements Comparator
	{
		protected Map m_map;
		
		public ValueComparator(Map map)
		{
			this.m_map = map;
		}
		
		public int compare(Object keyA, Object keyB)
		{
			Comparable valueA = (Comparable) m_map.get(keyA);
			Comparable valueB = (Comparable) m_map.get(keyB);
		    return valueA.compareTo(valueB);
		}
		
	}
}
