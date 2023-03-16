/**
 * 
 */
package lowentry.ue4.libs.jackson.databind.jsonFormatVisitors;

import lowentry.ue4.libs.jackson.databind.SerializerProvider;

/**
 * @author jphelan
 */
@SuppressWarnings("all")
public interface JsonFormatVisitorWithSerializerProvider {
    public SerializerProvider getProvider();
    public abstract void setProvider(SerializerProvider provider);
}
