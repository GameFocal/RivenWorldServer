/**
 * Classes used for exposing logical structure of POJOs as Jackson
 * sees it, and exposed via
 * {@link lowentry.ue4.libs.jackson.databind.ObjectMapper#acceptJsonFormatVisitor(Class, JsonFormatVisitorWrapper)}
 * and
 * {@link lowentry.ue4.libs.jackson.databind.ObjectMapper#acceptJsonFormatVisitor(lowentry.ue4.libs.jackson.databind.JavaType, JsonFormatVisitorWrapper)}
 * methods.
 *<p>
 * The main entrypoint for code, then, is {@link lowentry.ue4.libs.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper} and other
 * types are recursively needed during traversal.
 */
package lowentry.ue4.libs.jackson.databind.jsonFormatVisitors;
