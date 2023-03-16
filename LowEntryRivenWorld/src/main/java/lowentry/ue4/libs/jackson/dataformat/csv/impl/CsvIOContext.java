package lowentry.ue4.libs.jackson.dataformat.csv.impl;

import lowentry.ue4.libs.jackson.core.io.IOContext;
import lowentry.ue4.libs.jackson.core.util.BufferRecycler;

@SuppressWarnings("all")
public class CsvIOContext extends IOContext
{
    public CsvIOContext(BufferRecycler br, Object sourceRef, boolean managedResource) {
        super(br, sourceRef, managedResource);
    }

    public TextBuffer csvTextBuffer() {
        return new TextBuffer(_bufferRecycler);
    }
}
