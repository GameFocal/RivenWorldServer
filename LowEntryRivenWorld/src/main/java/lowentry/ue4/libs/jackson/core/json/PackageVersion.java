package lowentry.ue4.libs.jackson.core.json;


import lowentry.ue4.libs.jackson.core.Version;
import lowentry.ue4.libs.jackson.core.Versioned;
import lowentry.ue4.libs.jackson.core.util.VersionUtil;


/**
 * Automatically generated from PackageVersion.java.in during
 * packageVersion-generate execution of maven-replacer-plugin in
 * pom.xml.
 */
@SuppressWarnings("all")
public final class PackageVersion implements Versioned
{
	public final static Version VERSION = VersionUtil.parseVersion("2.10.2", "lowentry.ue4.libs.jackson.core", "jackson-dataformat-yaml");
	
	@Override
	public Version version()
	{
		return VERSION;
	}
}
