package lowentry.ue4.libs.jackson.core;

import java.io.IOException;

/**
 * Interface that defines objects that can read and write
 * {@link TreeNode} instances using Streaming API.
 * 
 * @since 2.3
 */
@SuppressWarnings("all")
public abstract class TreeCodec
{
    public abstract <T extends TreeNode> T readTree(JsonParser p) throws IOException, JsonProcessingException;
    public abstract void writeTree(JsonGenerator g, TreeNode tree) throws IOException, JsonProcessingException;

    /**
     * @since 2.10
     */
    public TreeNode missingNode() {
        return null;
    }

    /**
     * @since 2.10
     */
    public TreeNode nullNode() {
        return null;
    }

    public abstract TreeNode createArrayNode();
    public abstract TreeNode createObjectNode();
    public abstract JsonParser treeAsTokens(TreeNode node);
}
