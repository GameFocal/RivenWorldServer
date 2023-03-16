package lowentry.ue4.classes;


import lowentry.ue4.libs.jackson.databind.JsonNode;


public class JsonObjectItem
{
	public final String   key;
	public final JsonNode value;
	
	
	public JsonObjectItem(String key, JsonNode value)
	{
		this.key = key;
		this.value = value;
	}
}
