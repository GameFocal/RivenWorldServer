package lowentry.ue4.classes.utility;


import java.util.AbstractSet;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;


/**
 * Allows you to have a HashSet that you can easily clear of old values very efficiently.<br>
 * <br>
 * It does this by actually having two HashSets, one marked old and one marked new.<br>
 * When you call flip, it clears the values from the old one, and then it flips the markings (making the old/cleared map the new one, and the other the old one).<br>
 * This means that after running flip() twice, both HashSets will be cleared.<br>
 * <br>
 * This class is useful for when you need to keep track of certain data that expires by time, like used hashcashes for example.<br>
 */
public class FlippableSet<T> extends AbstractSet<T>
{
	protected HashSet<T> current  = new HashSet<>();
	protected HashSet<T> previous = new HashSet<>();
	
	
	/**
	 * Flips the sets, returns the expired set.
	 */
	public Collection<T> flip()
	{
		HashSet<T> expired = previous;
		previous = current;
		current = new HashSet<>();
		return expired;
	}
	
	
	@Override
	public boolean add(T value)
	{
		return current.add(value);
	}
	
	@Override
	public boolean remove(Object value)
	{
		boolean a = current.remove(value);
		boolean b = previous.remove(value);
		return (a || b);
	}
	
	@Override
	public boolean contains(Object value)
	{
		return (current.contains(value) || previous.contains(value));
	}
	
	@Override
	public void clear()
	{
		current.clear();
		previous.clear();
	}
	
	
	@Override
	public boolean retainAll(Collection<?> c)
	{
		boolean a = current.retainAll(c);
		boolean b = previous.retainAll(c);
		return (a || b);
	}
	
	@Override
	public boolean removeAll(Collection<?> c)
	{
		boolean a = current.removeAll(c);
		boolean b = previous.removeAll(c);
		return (a || b);
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
	public Iterator<T> iterator()
	{
		throw new UnsupportedOperationException();
	}
}
