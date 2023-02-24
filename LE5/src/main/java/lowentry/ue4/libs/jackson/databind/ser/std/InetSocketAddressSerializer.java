package lowentry.ue4.libs.jackson.databind.ser.std;

import java.io.IOException;
import java.net.*;

import lowentry.ue4.libs.jackson.core.JsonGenerator;
import lowentry.ue4.libs.jackson.core.JsonToken;
import lowentry.ue4.libs.jackson.core.type.WritableTypeId;
import lowentry.ue4.libs.jackson.databind.SerializerProvider;
import lowentry.ue4.libs.jackson.databind.jsontype.TypeSerializer;

/**
 * Simple serializer for {@link InetSocketAddress}.
 */
@SuppressWarnings("all")
public class InetSocketAddressSerializer
    extends StdScalarSerializer<InetSocketAddress>
{
    public InetSocketAddressSerializer() { super(InetSocketAddress.class); }

    @Override
    public void serialize(InetSocketAddress value, JsonGenerator jgen, SerializerProvider provider) throws IOException
    {
        InetAddress addr = value.getAddress();
        String str = addr == null ? value.getHostName() : addr.toString().trim();
        int ix = str.indexOf('/');
        if (ix >= 0) {
            if (ix == 0) { // missing host name; use address
                str = addr instanceof Inet6Address
                        ? "[" + str.substring(1) + "]" // bracket IPv6 addresses with
                        : str.substring(1);

            } else { // otherwise use name
                str = str.substring(0, ix);
            }
        }

        jgen.writeString(str + ":" + value.getPort());
    }

    @Override
    public void serializeWithType(InetSocketAddress value, JsonGenerator g,
            SerializerProvider provider, TypeSerializer typeSer) throws IOException
    {
        // Better ensure we don't use specific sub-classes...
        WritableTypeId typeIdDef = typeSer.writeTypePrefix(g,
                typeSer.typeId(value, InetSocketAddress.class, JsonToken.VALUE_STRING));
        serialize(value, g, provider);
        typeSer.writeTypeSuffix(g, typeIdDef);
    }
}
