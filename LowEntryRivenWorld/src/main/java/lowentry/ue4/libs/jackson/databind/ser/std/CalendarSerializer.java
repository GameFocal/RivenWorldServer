package lowentry.ue4.libs.jackson.databind.ser.std;

import java.io.IOException;
import java.text.DateFormat;
import java.util.Calendar;

import lowentry.ue4.libs.jackson.core.*;
import lowentry.ue4.libs.jackson.databind.SerializerProvider;
import lowentry.ue4.libs.jackson.databind.annotation.JacksonStdImpl;

/**
 * Standard serializer for {@link java.util.Calendar}.
 * As with other time/date types, is configurable to produce timestamps
 * (standard Java 64-bit timestamp) or textual formats (usually ISO-8601).
 */
@JacksonStdImpl
@SuppressWarnings("all")
public class CalendarSerializer
    extends DateTimeSerializerBase<Calendar>
{
    public static final CalendarSerializer instance = new CalendarSerializer();

    public CalendarSerializer() { this(null, null); }

    public CalendarSerializer(Boolean useTimestamp, DateFormat customFormat) {
        super(Calendar.class, useTimestamp, customFormat);
    }

    @Override
    public CalendarSerializer withFormat(Boolean timestamp, DateFormat customFormat) {
        return new CalendarSerializer(timestamp, customFormat);
    }

    @Override
    protected long _timestamp(Calendar value) {
        return (value == null) ? 0L : value.getTimeInMillis();
    }

    @Override
    public void serialize(Calendar value, JsonGenerator g, SerializerProvider provider) throws IOException
    {
        if (_asTimestamp(provider)) {
            g.writeNumber(_timestamp(value));
            return;
        }
        _serializeAsString(value.getTime(), g, provider);
    }
}
