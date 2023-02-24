package lowentry.ue4.classes.internal;


import lowentry.ue4.library.LowEntry;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;


public class FilePath
{
	private static String generate(Class<?> c)
	{
		try
		{
			String path = new File(c.getProtectionDomain().getCodeSource().getLocation().toURI().getPath()).getAbsolutePath();
			if(path.length() <= 0)
			{
				return "";
			}
			path = path.replaceAll("\\\\", "/");
			
			int endIndex = path.lastIndexOf('/');
			if(endIndex >= 0)
			{
				path = path.substring(0, endIndex);
			}
			
			return path + "/";
		}
		catch(URISyntaxException e)
		{
			return "";
		}
	}
	
	
	public static String get(Class<?> c)
	{
		return generate(c);
	}
	
	public static String get(Class<?> c, String file)
	{
		if(LowEntry.IS_WINDOWS)
		{
			if((file.length() >= 2) && (file.charAt(1) == ':'))
			{
				return file;
			}
		}
		else
		{
			if(file.startsWith("/"))
			{
				return file;
			}
		}
		return get(c) + file;
	}
	
	
	public static Path getAsPath(Class<?> c)
	{
		return Paths.get(get(c));
	}
	
	public static Path getAsPath(Class<?> c, String file)
	{
		return Paths.get(get(c, file));
	}
	
	
	public static File getAsFile(Class<?> c)
	{
		return Paths.get(get(c)).toFile();
	}
	
	public static File getAsFile(Class<?> c, String file)
	{
		return Paths.get(get(c, file)).toFile();
	}
	
	
	public static URI getAsUri(Class<?> c)
	{
		return getAsPath(c).toUri();
	}
	
	public static URI getAsUri(Class<?> c, String file)
	{
		return getAsPath(c, file).toUri();
	}
	
	
	public static URL getAsUrl(Class<?> c) throws MalformedURLException
	{
		return getAsUri(c).toURL();
	}
	
	public static URL getAsUrl(Class<?> c, String file) throws MalformedURLException
	{
		return getAsUri(c, file).toURL();
	}
}
