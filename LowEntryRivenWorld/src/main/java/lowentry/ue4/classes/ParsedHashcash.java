package lowentry.ue4.classes;


import java.util.Date;
import java.util.Objects;


public final class ParsedHashcash
{
	protected final boolean valid;
	
	protected final String resource;
	protected final Date   date;
	protected final int    bits;
	
	protected Integer hashCode = null;
	
	
	public ParsedHashcash(boolean valid, String resource, Date date, int bits)
	{
		this.valid = valid;
		this.resource = resource;
		this.date = date;
		this.bits = bits;
	}
	
	
	/**
	 * Returns true if this HashCash is valid, returns false if it is not valid.
	 */
	public boolean isValid()
	{
		return valid;
	}
	
	/**
	 * Returns the resource (basically the service ID) of this HashCash.
	 */
	public String getResource()
	{
		return resource;
	}
	
	/**
	 * Returns the creation date of this HashCash.
	 */
	public Date getDate()
	{
		return date;
	}
	
	/**
	 * Returns the bits (the strength, the value) of this HashCash.
	 */
	public int getBits()
	{
		return bits;
	}
	
	
	@Override
	public String toString()
	{
		if(!valid)
		{
			return getClass().getSimpleName() + "{invalid}";
		}
		return getClass().getSimpleName() + "{resource=\"" + resource + "\", date=\"" + date + "\", bits=" + bits + "}";
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if(!(obj instanceof ParsedHashcash))
		{
			return false;
		}
		ParsedHashcash o = (ParsedHashcash) obj;
		if((valid != o.valid) || (bits == o.bits))
		{
			return false;
		}
		if(resource == null)
		{
			if(o.resource != null)
			{
				return false;
			}
		}
		else if((o.resource == null) || !resource.equals(o.resource))
		{
			return false;
		}
		if(date == null)
		{
			return (o.date == null);
		}
		else
		{
			return ((o.date != null) && date.equals(o.date));
		}
	}
	
	@SuppressWarnings("NonFinalFieldReferencedInHashCode")
	@Override
	public int hashCode()
	{
		// thread safety doesn't matter if the generated value is always the same
		if(hashCode == null)
		{
			hashCode = Objects.hash(valid, resource, date, bits);
		}
		return hashCode;
	}
}
