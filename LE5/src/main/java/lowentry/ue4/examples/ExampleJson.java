package lowentry.ue4.examples;


import lowentry.ue4.classes.JsonArrayItem;
import lowentry.ue4.classes.JsonObjectItem;
import lowentry.ue4.library.LowEntry;
import lowentry.ue4.libs.jackson.databind.JsonNode;

import java.util.HashMap;


public class ExampleJson
{
	public static void main(final String[] args) throws Throwable
	{
		String jsonString;
		
		
		{// example data to JSON >>
			System.out.println("##### example data to JSON #####");
			
			HashMap<String,Object> root = new HashMap<>();
			root.put("version", "1.0.0");
			root.put("version_array", new int[]{1, 0, 0});
			root.put("action", "LOGIN");
			
			HashMap<String,Object> data = new HashMap<>();
			data.put("username", "myusername");
			data.put("password", "mytopsecretpassword");
			
			root.put("data", data);
			
			jsonString = LowEntry.toJsonString(root, true);
			System.out.println(jsonString);
		}// example data to JSON <<
		
		
		System.out.print("\n\n");
		
		
		{// example parse and data retrieval >>
			System.out.println("##### example parse and data retrieval #####");
			
			JsonNode root = LowEntry.parseJsonString(jsonString);
			if(root == null)
			{
				System.out.println("parsing failed");
			}
			else
			{
				JsonNode actionNode = root.get("action");
				if(actionNode != null)
				{
					String action = actionNode.textValue();
					System.out.println("action: " + action);
				}
				
				JsonNode versionNode = root.get("version");
				if(versionNode != null)
				{
					String version = versionNode.textValue();
					System.out.println("version: " + version);
				}
				
				JsonNode versionArrayNode = root.get("version_array");
				if(versionArrayNode != null)
				{
					JsonNode versionArrayNode0 = versionArrayNode.get(0);
					if(versionArrayNode0 != null)
					{
						int versionArray0 = versionArrayNode0.asInt();
						System.out.println("version array 0: " + versionArray0);
					}
					
					JsonNode versionArrayNode1 = versionArrayNode.get(1);
					if(versionArrayNode1 != null)
					{
						int versionArray1 = versionArrayNode1.asInt();
						System.out.println("version array 1: " + versionArray1);
					}
					
					JsonNode versionArrayNode2 = versionArrayNode.get(2);
					if(versionArrayNode2 != null)
					{
						int versionArray2 = versionArrayNode2.asInt();
						System.out.println("version array 2: " + versionArray2);
					}
				}
				
				JsonNode dataNode = root.get("data");
				if(dataNode != null)
				{
					JsonNode dataUsernameNode = dataNode.get("username");
					if(dataUsernameNode != null)
					{
						String dataUsername = dataUsernameNode.textValue();
						System.out.println("data username: " + dataUsername);
					}
					
					JsonNode dataPasswordNode = dataNode.get("password");
					if(dataPasswordNode != null)
					{
						String dataPassword = dataPasswordNode.textValue();
						System.out.println("data password: " + dataPassword);
					}
				}
			}
		}// example parse and data retrieval <<
		
		
		System.out.print("\n\n");
		
		
		{// example parse and iterate >>
			System.out.println("##### example parse and iterate #####");
			
			JsonNode root = LowEntry.parseJsonString(jsonString);
			if(root == null)
			{
				System.out.println("parsing failed");
			}
			else if(root.isArray())
			{
				for(JsonArrayItem node : LowEntry.getArrayNodes(root))
				{
					if(node.value.isArray())
					{
						for(JsonArrayItem subnode : LowEntry.getArrayNodes(node.value))
						{
							System.out.println("[" + node.index + "][" + subnode.index + "] : " + subnode.value);
						}
					}
					else if(node.value.isObject())
					{
						for(JsonObjectItem subnode : LowEntry.getObjectNodes(node.value))
						{
							System.out.println("[" + node.index + "].\"" + subnode.key + "\" : " + subnode.value);
						}
					}
					else
					{
						System.out.println("[" + node.index + "] : " + node.value);
					}
				}
			}
			else if(root.isObject())
			{
				for(JsonObjectItem node : LowEntry.getObjectNodes(root))
				{
					if(node.value.isArray())
					{
						for(JsonArrayItem subnode : LowEntry.getArrayNodes(node.value))
						{
							System.out.println("\"" + node.key + "\"[" + subnode.index + "] : " + subnode.value);
						}
					}
					else if(node.value.isObject())
					{
						for(JsonObjectItem subnode : LowEntry.getObjectNodes(node.value))
						{
							System.out.println("\"" + node.key + "\".\"" + subnode.key + "\" : " + subnode.value);
						}
					}
					else
					{
						System.out.println("\"" + node.key + "\" : " + node.value);
					}
				}
			}
			else
			{
				System.out.println(root);
			}
		}// example parse and iterate <<
	}
}
