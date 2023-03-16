package lowentry.ue4.classes.http;


import lowentry.ue4.library.LowEntry;
import lowentry.ue4.libs.jackson.databind.JsonNode;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Locale;


public class HttpRequest
{
	protected final boolean                secure;
	protected       String                 method;
	protected       String                 path;
	protected       byte[]                 content;
	protected final HashMap<String,String> headers          = new HashMap<>();
	protected final HashMap<String,String> getVariables     = new HashMap<>();
	protected final HashMap<String,String> postVariables    = new HashMap<>();
	protected final HashMap<String,String> requestVariables = new HashMap<>();
	
	
	public HttpRequest(boolean secure)
	{
		this.secure = secure;
		this.method = "";
		this.path = "";
		this.content = new byte[0];
	}
	
	
	/**
	 * Sets the method.
	 */
	protected void internal_setMethod(String method)
	{
		this.method = method.trim().toUpperCase(Locale.ENGLISH);
	}
	
	/**
	 * Sets the path, automatically decodes it and retrieves any GET variables from it.
	 */
	protected void internal_setPath(String path)
	{
		String[] parts = path.split("\\?", 2);
		if(parts.length == 2)
		{
			path = parts[0];
			String getvars = parts[1];
			for(String getvar : getvars.split("&"))
			{
				String[] getvarparts = getvar.split("=", 2);
				internal_setGet(getvarparts[0], ((getvarparts.length == 2) ? getvarparts[1] : ""));
			}
		}
		try
		{
			this.path = URLDecoder.decode(path, StandardCharsets.UTF_8);
		}
		catch(Exception e)
		{
		}
	}
	
	/**
	 * Sets the content.
	 */
	protected void internal_setContent(byte[] content)
	{
		this.content = content;
		
		if(isMethod("POST") && isContentType("application/x-www-form-urlencoded"))
		{
			String contentstring = getContentAsString();
			if((contentstring == null) || (contentstring.length() <= 0))
			{
				return;
			}
			for(String postvar : contentstring.split("&"))
			{
				String[] postvarparts = postvar.split("=", 2);
				internal_setPost(postvarparts[0], ((postvarparts.length == 2) ? postvarparts[1] : ""));
			}
		}
	}
	
	/**
	 * Sets a header.
	 */
	protected void internal_setHeader(String header, String value)
	{
		this.headers.put(header.trim().toLowerCase(Locale.ENGLISH), value.trim());
	}
	
	/**
	 * Sets a GET variable.
	 */
	protected void internal_setGet(String name, String value)
	{
		try
		{
			this.getVariables.put(URLDecoder.decode(name, StandardCharsets.UTF_8), URLDecoder.decode(value, StandardCharsets.UTF_8));
		}
		catch(Exception e)
		{
		}
	}
	
	/**
	 * Sets a POST variable.
	 */
	protected void internal_setPost(String name, String value)
	{
		try
		{
			this.postVariables.put(URLDecoder.decode(name, StandardCharsets.UTF_8), URLDecoder.decode(value, StandardCharsets.UTF_8));
		}
		catch(Exception e)
		{
		}
	}
	
	/**
	 * Sets the REQUEST variables to GET and POST.
	 */
	protected void internal_setRequest()
	{
		requestVariables.putAll(postVariables);
		requestVariables.putAll(getVariables);
	}
	
	
	/**
	 * Returns true if the connection uses SSL, returns false otherwise.
	 */
	public boolean isSecure()
	{
		return secure;
	}
	
	/**
	 * Returns the method of the request, like GET, POST, etc.<br>
	 * <br>
	 * The method will always be trimmed and is always in upper case, like "GET" or "POST",<br>
	 * never ever will it return "Get", "get" or " GET ".
	 */
	public String getMethod()
	{
		return method;
	}
	
	/**
	 * Returns true if the method of the request is equals to the given method (case insensitive).
	 */
	public boolean isMethod(String method)
	{
		if(method == null)
		{
			return false;
		}
		return method.trim().toUpperCase(Locale.ENGLISH).equals(getMethod());
	}
	
	/**
	 * Returns the path of the request, like /test/index.html.<br>
	 * <br>
	 * Paths are case sensitive and will never be trimmed.
	 */
	public String getPath()
	{
		return path;
	}
	
	/**
	 * Returns the value of the Content-Type header, returns null if the header wasn't sent.
	 */
	public String getContentType()
	{
		return getHeader("content-type");
	}
	
	/**
	 * Returns true if the given String was present in the Content-Type header.<br>
	 * <br>
	 * This function is case-insensitive.
	 */
	public boolean isContentType(String type)
	{
		if(type == null)
		{
			return false;
		}
		String contentType = getContentType();
		if(contentType == null)
		{
			return false;
		}
		return contentType.toLowerCase(Locale.ENGLISH).contains(type.trim().toLowerCase(Locale.ENGLISH));
	}
	
	/**
	 * Returns the value of the Content-Length header, returns 0 if the header wasn't sent.
	 */
	public int getContentLength()
	{
		try
		{
			String contentLength = getHeader("content-length");
			if(contentLength == null)
			{
				return 0;
			}
			return Integer.parseInt(contentLength);
		}
		catch(Exception e)
		{
			return 0;
		}
	}
	
	
	/**
	 * Returns the content of the request as bytes.
	 */
	public byte[] getContentAsBytes()
	{
		return content;
	}
	
	/**
	 * Tries to return the content of the request as a String, returns an empty string if it fails.
	 */
	public String getContentAsString()
	{
		String charset = "ISO-8859-1";
		
		{// get charset if present >>
			String contentType = getContentType();
			if(contentType != null)
			{
				for(String contentTypeInfo : contentType.split(";"))
				{
					contentTypeInfo = contentTypeInfo.trim();
					if(contentTypeInfo.toLowerCase(Locale.ENGLISH).startsWith("charset="))
					{
						charset = contentTypeInfo.substring("charset=".length()).trim();
					}
				}
			}
		}// get charset if present <<
		
		try
		{
			return new String(content, charset);
		}
		catch(Exception e)
		{
			return "";
		}
	}
	
	/**
	 * Tries to convert the content of the request as a String and parse it as Json, returns null if it fails.
	 */
	public JsonNode getContentAsJson()
	{
		String contentAsString = getContentAsString();
		if((contentAsString == null) || (contentAsString.length() <= 0))
		{
			return null;
		}
		return LowEntry.parseJsonString(contentAsString);
	}
	
	
	/**
	 * Returns the value of the given header, returns null if the header wasn't sent.<br>
	 * <br>
	 * Headers are trimmed and are case insensitive.
	 */
	public String getHeader(String header)
	{
		return headers.get(header.trim().toLowerCase(Locale.ENGLISH));
	}
	
	
	/**
	 * Returns the value of the given GET variable, returns null if the variable wasn't sent.<br>
	 * <br>
	 * GET variable names are case sensitive.
	 */
	public String GET(String name)
	{
		return getVariables.get(name);
	}
	
	/**
	 * Returns the value of the given POST variable, returns null if the variable wasn't sent.<br>
	 * <br>
	 * POST variable names are case sensitive.
	 */
	public String POST(String name)
	{
		return postVariables.get(name);
	}
	
	/**
	 * Returns the value of the given REQUEST variable, returns null if the variable wasn't sent.<br>
	 * <br>
	 * The REQUEST variable will contain the GET value if it is set, otherwise it will contain the POST variable if set.<br>
	 * <br>
	 * REQUEST variable names are case sensitive.
	 */
	public String REQUEST(String name)
	{
		return requestVariables.get(name);
	}
	
	
	/**
	 * Returns the headers.
	 */
	public HashMap<String,String> getHeaders()
	{
		return headers;
	}
	
	/**
	 * Returns the sent GET variables.
	 */
	public HashMap<String,String> GET()
	{
		return getVariables;
	}
	
	/**
	 * Returns the sent POST variables.
	 */
	public HashMap<String,String> POST()
	{
		return postVariables;
	}
	
	/**
	 * Returns the sent REQUEST variables.<br>
	 * <br>
	 * The REQUEST variables will contain the GET and POST variables.<br>
	 * <br>
	 * For duplicate GET and POST variable names the value of the GET variable will be used.
	 */
	public HashMap<String,String> REQUEST()
	{
		return requestVariables;
	}
}
