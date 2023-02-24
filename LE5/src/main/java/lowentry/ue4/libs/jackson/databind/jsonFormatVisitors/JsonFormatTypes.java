package lowentry.ue4.libs.jackson.databind.jsonFormatVisitors;

import java.util.*;

import lowentry.ue4.libs.jackson.annotation.JsonCreator;
import lowentry.ue4.libs.jackson.annotation.JsonValue;

@SuppressWarnings("all")
public enum JsonFormatTypes
{
	STRING,
	NUMBER,
	INTEGER,
	BOOLEAN,
	OBJECT,
	ARRAY,
	NULL,
	ANY;

	private static final Map<String,JsonFormatTypes> _byLCName = new HashMap<String,JsonFormatTypes>();
	static {
	    for (JsonFormatTypes t : values()) {
	        _byLCName.put(t.name().toLowerCase(), t);
	    }
	}
	
	@JsonValue
	public String value() {
		return name().toLowerCase();
	}
	
	@JsonCreator
	public static JsonFormatTypes forValue(String s) {
		return _byLCName.get(s);
	}
}