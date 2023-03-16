package lowentry.ue4.classes.http;


import lowentry.ue4.library.LowEntry;
import lowentry.ue4.libs.jackson.databind.JsonNode;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map.Entry;


public class HttpResponse
{
	protected final HttpClient  client;
	protected final HttpRequest request;
	
	protected volatile int                    responseCode = 200;
	protected final    HashMap<String,String> headers      = new HashMap<>();
	protected final    ByteArrayOutputStream  content      = new ByteArrayOutputStream();
	
	
	public HttpResponse(HttpClient client, HttpRequest request)
	{
		this.client = client;
		this.request = request;
		
		setContentIsHtml();
	}
	
	
	/**
	 * Returns the set response code.
	 */
	public int getReponseCode()
	{
		return responseCode;
	}
	
	/**
	 * Sets the response code.
	 */
	public void setResponseCode(int responseCode)
	{
		this.responseCode = responseCode;
	}
	
	
	/**
	 * Returns the value of the given header, returns null if the header wasn't sent.<br>
	 * <br>
	 * Headers are case insensitive.
	 */
	public String getHeader(String header)
	{
		synchronized(headers)
		{
			return headers.get(header.trim().toLowerCase(Locale.ENGLISH));
		}
	}
	
	/**
	 * Sets the value of the given header, set the value to null to remove the header.<br>
	 * <br>
	 * Headers are case insensitive.
	 */
	public void setHeader(String header, String value)
	{
		synchronized(headers)
		{
			if(value == null)
			{
				headers.remove(header.trim().toLowerCase(Locale.ENGLISH));
			}
			else
			{
				headers.put(header.trim().toLowerCase(Locale.ENGLISH), value.trim());
			}
		}
	}
	
	
	/**
	 * Returns the value of the Content-Type header, returns null if the header wasn't sent.
	 */
	public String getContentType()
	{
		return getHeader("content-type");
	}
	
	/**
	 * Returns the value of the Content-Type header, returns null if the header wasn't sent.
	 */
	public void setContentType(String type)
	{
		setHeader("content-type", type);
	}
	
	
	/**
	 * Sets the Content-Type header to html.
	 */
	public void setContentIsHtml()
	{
		setContentType("text/html; charset=utf-8");
	}
	
	/**
	 * Sets the Content-Type header to plain text.
	 */
	public void setContentIsPlainText()
	{
		setContentType("text/plain; charset=utf-8");
	}
	
	/**
	 * Sets the Content-Type header to JSON.
	 */
	public void setContentIsJson()
	{
		setContentType("application/json; charset=utf-8");
	}
	
	
	/**
	 * Adds the given content.
	 */
	public void addContent(byte[] bytes)
	{
		if((bytes == null) || (bytes.length <= 0))
		{
			return;
		}
		try
		{
			content.write(bytes);
		}
		catch(Exception e)
		{
		}
	}
	
	/**
	 * Adds the given content (UTF-8).
	 */
	public void addContent(String text)
	{
		if((text == null) || (text.length() <= 0))
		{
			return;
		}
		try
		{
			content.write(LowEntry.stringToBytesUtf8(text));
		}
		catch(Exception e)
		{
		}
	}
	
	/**
	 * Sets the content as JSON.
	 */
	public void setContentAsJson(Object object)
	{
		setContentIsJson();
		clearContent();
		addContent(LowEntry.toJsonString(object));
	}
	
	/**
	 * Clears the added content.
	 */
	public void clearContent()
	{
		content.reset();
	}
	
	
	/**
	 * Returns the added content.
	 */
	public byte[] getContentAsBytes()
	{
		return content.toByteArray();
	}
	
	/**
	 * Returns the added content as a String (UTF-8), returns an empty String if it fails.
	 */
	public String getContentAsString()
	{
		return LowEntry.bytesToStringUtf8(content.toByteArray());
	}
	
	/**
	 * Returns the added content as JSON, returns null if it fails.
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
	 * Converts the data in this class into an actual response.
	 */
	public byte[] toBytes()
	{
		StringBuilder buffer = new StringBuilder();
		
		buffer.append("HTTP/1.1 ");
		buffer.append(responseCode);
		buffer.append("\r\n");
		
		synchronized(headers)
		{
			for(Entry<String,String> header : headers.entrySet())
			{
				buffer.append(header.getKey());
				buffer.append(":");
				buffer.append(header.getValue());
				buffer.append("\r\n");
			}
		}
		
		byte[] contentBytes = content.toByteArray();
		
		buffer.append("content-length:");
		buffer.append(contentBytes.length);
		buffer.append("\r\n");
		
		buffer.append("date:");
		buffer.append(HttpServer.internal_getServerTime());
		buffer.append("\r\n");
		
		buffer.append("connection:close\r\n");
		
		buffer.append("\r\n");
		
		byte[] header = LowEntry.stringToBytesLatin1(buffer.toString());
		if(request.isMethod("HEAD"))
		{
			return header;
		}
		return LowEntry.mergeBytes(header, contentBytes);
	}
	
	/**
	 * Causes the response to be sent.<br>
	 * <br>
	 * You have to call this function when done with the response, otherwise the client will never 'load' the webpage.<br>
	 * <br>
	 * If you want the client to never load the webpage (against a DDOS'ing client for example), just never call this function.
	 */
	public void done()
	{
		if(client.isConnected())
		{
			client.send(toBytes());
			//client.disconnect(); // should be called by the client now, caused by connection:close
		}
	}
}
