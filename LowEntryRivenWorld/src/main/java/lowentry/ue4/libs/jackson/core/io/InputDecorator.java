package lowentry.ue4.libs.jackson.core.io;

import java.io.*;

/**
 * Handler class that can be used to decorate input sources.
 * Typical use is to use a filter abstraction (filtered stream,
 * reader) around original input source, and apply additional
 * processing during read operations.
 */
@SuppressWarnings("all")
public abstract class InputDecorator
    implements Serializable // since 2.1
{
    private static final long serialVersionUID = 1L;
    
    /**
     * Method called by {@link lowentry.ue4.libs.jackson.core.JsonFactory} instance when
     * creating parser given an {@link InputStream}, when this decorator
     * has been registered.
     * 
     * @param ctxt IO context in use (provides access to declared encoding).
     *   NOTE: at this point context may not have all information initialized;
     *   specifically auto-detected encoding is only available once parsing starts,
     *   which may occur only after this method is called.
     * @param in Original input source
     * 
     * @return InputStream to use; either 'in' as is, or decorator
     *   version that typically delogates to 'in'
     */
    public abstract InputStream decorate(IOContext ctxt, InputStream in)
        throws IOException;

    /**
     * Method called by {@link lowentry.ue4.libs.jackson.core.JsonFactory} instance when
     * creating parser on given "raw" byte source.
     * Method can either construct a {@link InputStream} for reading; or return
     * null to indicate that no wrapping should occur.
     * 
     * @param ctxt IO context in use (provides access to declared encoding)
     *   NOTE: at this point context may not have all information initialized;
     *   specifically auto-detected encoding is only available once parsing starts,
     *   which may occur only after this method is called.
     * @param src Input buffer that contains contents to parse
     * @param offset Offset of the first available byte in the input buffer
     * @param length Number of bytes available in the input buffer
     * 
     * @return Either {@link InputStream} to use as input source; or null to indicate
     *   that contents are to be processed as-is by caller
     */
    public abstract InputStream decorate(IOContext ctxt, byte[] src, int offset, int length)
        throws IOException;

    /**
     * Method called by {@link lowentry.ue4.libs.jackson.core.JsonFactory} instance when
     * creating parser given an {@link DataInput}, when this decorator
     * has been registered.
     *<p>
     * Default implementation simply throws {@link UnsupportedOperationException}
     * 
     * @param ctxt IO context in use (provides access to declared encoding).
     *   NOTE: at this point context may not have all information initialized;
     *   specifically auto-detected encoding is only available once parsing starts,
     *   which may occur only after this method is called.
     * @param input Original input source
     * 
     * @return InputStream to use; either 'input' as is, or decorator
     *   version that typically delogates to 'input'
     * 
     * @since 2.8
     */
    public DataInput decorate(IOContext ctxt, DataInput input)
        throws IOException {
        throw new UnsupportedOperationException();
    }

    /**
     * Method called by {@link lowentry.ue4.libs.jackson.core.JsonFactory} instance when
     * creating parser given an {@link Reader}, when this decorator
     * has been registered.
     * 
     * @param ctxt IO context in use (provides access to declared encoding)
     *   NOTE: at this point context may not have all information initialized;
     *   specifically auto-detected encoding is only available once parsing starts,
     *   which may occur only after this method is called.
     * @param r Original reader
     * 
     * @return Reader to use; either passed in argument, or something that
     *   calls it (for example, a {@link FilterReader})
     */
    public abstract Reader decorate(IOContext ctxt, Reader r) throws IOException;
}