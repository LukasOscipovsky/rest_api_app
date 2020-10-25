package virt.server.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

/**
 * Deserialize string and validate content to match IPv4 standard
 *
 * @author Lukas Oscipovsky
 */
public class AddressDeserializer extends JsonDeserializer<String> {

    private static final String PATTERN = "^((0|1\\d?\\d?|2[0-4]?\\d?|25[0-5]?|[3-9]\\d?)\\.){3}(0|1\\d?\\d?|2[0-4]?\\d?|25[0-5]?|[3-9]\\d?)$";

    @Override
    public String deserialize(final JsonParser jsonParser, final DeserializationContext deserializationContext) throws IOException {

        final String ipAddress = jsonParser.getValueAsString();

        if (!ipAddress.matches(PATTERN)) throw new IllegalArgumentException("ip address provided is not valid");

        return ipAddress;
    }
}
