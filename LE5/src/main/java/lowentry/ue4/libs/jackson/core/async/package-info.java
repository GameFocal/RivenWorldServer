/**
 * Package that contains abstractions needed to support optional
 * non-blocking decoding (parsing) functionality.
 * Although parsers are constructed normally via
 * {@link lowentry.ue4.libs.jackson.core.JsonFactory}
 * (and are, in fact, sub-types of {@link lowentry.ue4.libs.jackson.core.JsonParser}),
 * the way input is provided differs.
 *
 * @since 2.9
 */

package lowentry.ue4.libs.jackson.core.async;
