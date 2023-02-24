package lowentry.ue4.classes.utility;


import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


/**
 * Allows you to have a HashMap of Longs that you can easily clear of old values very efficiently.<br>
 * <br>
 * It does this by actually having two HashMaps, one marked old and one marked new.<br>
 * When you call flip, it clears the values from the old one, and then it flips the markings (making the old/cleared map the new one, and the other the old one).<br>
 * This means that after running flip() twice, both HashMaps will be cleared.<br>
 * <br>
 * The values it returns are the values from both the HashMaps added together.<br>
 * <br>
 * This class is useful for when you need to keep track of certain numeric data that expires by time, like the number of requests within a certain amount time for example.<br>
 */
public class FlippableCounter<K> extends AbstractMap<K,Long>
{
	protected HashMap<K,Long> current  = new HashMap<>();
	protected HashMap<K,Long> previous = new HashMap<>();
	
	
	/**
	 * Flips the sets, returns the expired map.
	 */
	public Map<K,Long> flip()
	{
		HashMap<K,Long> expired = previous;
		previous = current;
		current = new HashMap<>();
		return expired;
	}
	
	
	/**
	 * Increases the value by the given amount. Returns the new value.
	 */
	public Long increase(K key, Long value)
	{
		Long a = current.get(key);
		Long b = previous.get(key);
		current.put(key, ((a == null) ? 0 : a) + value);
		return ((a == null) ? 0 : a) + ((b == null) ? 0 : b) + value;
	}
	
	
	@Override
	public Long get(Object key)
	{
		Long a = current.get(key);
		Long b = previous.get(key);
		return ((a == null) ? 0 : a) + ((b == null) ? 0 : b);
	}
	
	@Override
	public Long put(K key, Long value)
	{
		Long b = previous.remove(key);
		Long a = current.put(key, value);
		return ((a == null) ? 0 : a) + ((b == null) ? 0 : b);
	}
	
	@Override
	public Long remove(Object key)
	{
		Long a = current.remove(key);
		Long b = previous.remove(key);
		return ((a == null) ? 0 : a) + ((b == null) ? 0 : b);
	}
	
	@Override
	public boolean containsKey(Object key)
	{
		return (current.containsKey(key) || previous.containsKey(key));
	}
	
	@Override
	public boolean containsValue(Object value)
	{
		return (current.containsValue(value) || previous.containsValue(value));
	}
	
	@Override
	public void clear()
	{
		current.clear();
		previous.clear();
	}
	
	
	@Override
	public boolean isEmpty()
	{
		return (current.isEmpty() && previous.isEmpty());
	}
	
	
	@Override
	@SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
	public boolean equals(Object o)
	{
		return (o == this);
	}
	
	@Override
	public int hashCode()
	{
		return System.identityHashCode(this);
	}
	
	@Override
	public String toString()
	{
		return "[" + current.toString() + ", " + previous.toString() + "]";
	}
	
	
	@Override
	public int size()
	{
		throw new UnsupportedOperationException();
	}
	
	@Override
	public Set<Entry<K,Long>> entrySet()
	{
		throw new UnsupportedOperationException();
	}
}
