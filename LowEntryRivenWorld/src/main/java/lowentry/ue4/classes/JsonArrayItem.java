package lowentry.ue4.classes;


import lowentry.ue4.libs.jackson.databind.JsonNode;


public class JsonArrayItem
{
	public final int      index;
	public final JsonNode value;
	
	
	public JsonArrayItem(int index, JsonNode value)
	{
		this.index = index;
		this.value = value;
	}
}
