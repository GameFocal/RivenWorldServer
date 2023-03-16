/**
 * Base classes used by concrete Parser and Generator implementations;
 * contain functionality that is not specific to JSON or input
 * abstraction (byte vs char).
 * Most formats extend these types, although it is also possible to
 * directly extend {@link lowentry.ue4.libs.jackson.core.JsonParser} or
 * {@link lowentry.ue4.libs.jackson.core.JsonGenerator}.
 */
package lowentry.ue4.libs.jackson.core.base;
