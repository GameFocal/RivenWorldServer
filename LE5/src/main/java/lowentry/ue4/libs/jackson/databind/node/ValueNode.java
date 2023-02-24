package lowentry.ue4.libs.jackson.databind.node;

import java.io.IOException;
import java.util.List;

import lowentry.ue4.libs.jackson.core.*;
import lowentry.ue4.libs.jackson.core.type.WritableTypeId;
import lowentry.ue4.libs.jackson.databind.JsonNode;
import lowentry.ue4.libs.jackson.databind.SerializerProvider;
import lowentry.ue4.libs.jackson.databind.jsontype.TypeSerializer;

/**
 * This intermediate base class is used for all leaf nodes, that is,
 * all non-container (array or object) nodes, except for the
 * "missing node".
 */
@SuppressWarnings("all")
public abstract class ValueNode
    extends BaseJsonNode
{
    private static final long serialVersionUID = 1L;

    protected ValueNode() { }

    @Override
    protected JsonNode _at(JsonPointer ptr) {
        // will only allow direct matches, but no traversal through
        // (base class checks for direct match)
        return MissingNode.getInstance();
    }

    /**
     * All current value nodes are immutable, so we can just return
     * them as is.
     */
    @SuppressWarnings("unchecked")
    @Override
    public <T extends JsonNode> T deepCopy() { return (T) this; }
    
    @Override public abstract JsonToken asToken();

    @Override
    public void serializeWithType(JsonGenerator g, SerializerProvider provider,
            TypeSerializer typeSer)
        throws IOException
    {
        WritableTypeId typeIdDef = typeSer.writeTypePrefix(g,
                typeSer.typeId(this, asToken()));
        serialize(g, provider);
        typeSer.writeTypeSuffix(g, typeIdDef);
    }

    /*
    /**********************************************************************
    /* Basic property access
    /**********************************************************************
     */

    @Override
    public boolean isEmpty() { return true; }
    
    /*
    /**********************************************************************
    /* Navigation methods
    /**********************************************************************
     */

    @Override
    public final JsonNode get(int index) { return null; }

    @Override
    public final JsonNode path(int index) { return MissingNode.getInstance(); }

    @Override
    public final boolean has(int index) { return false; }

    @Override
    public final boolean hasNonNull(int index) { return false; }

    @Override
    public final JsonNode get(String fieldName) { return null; }

    @Override
    public final JsonNode path(String fieldName) { return MissingNode.getInstance(); }

    @Override
    public final boolean has(String fieldName) { return false; }

    @Override
    public final boolean hasNonNull(String fieldName) { return false; }

    /*
     **********************************************************************
     * Find methods: all "leaf" nodes return the same for these
     **********************************************************************
     */

    @Override
    public final JsonNode findValue(String fieldName) {
        return null;
    }

    // note: co-variant return type
    @Override
    public final ObjectNode findParent(String fieldName) {
        return null;
    }

    @Override
    public final List<JsonNode> findValues(String fieldName, List<JsonNode> foundSoFar) {
        return foundSoFar;
    }

    @Override
    public final List<String> findValuesAsText(String fieldName, List<String> foundSoFar) {
        return foundSoFar;
    }

    @Override
    public final List<JsonNode> findParents(String fieldName, List<JsonNode> foundSoFar) {
        return foundSoFar;
    }
}
