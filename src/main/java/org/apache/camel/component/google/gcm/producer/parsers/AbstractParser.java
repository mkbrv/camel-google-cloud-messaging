package org.apache.camel.component.google.gcm.producer.parsers;

import org.apache.camel.component.google.gcm.producer.exceptions.CustomParserException;
import org.apache.camel.component.google.gcm.producer.exceptions.InvalidRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.Map;

/**
 * Created by miki on 19.04.2015.
 */
public abstract class AbstractParser {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractParser.class);

    public String getResponseFromConnection(HttpURLConnection conn) throws IOException {
        int status = conn.getResponseCode();
        String responseBody;
        if (status != 200) {
            try {
                responseBody = getAndClose(conn.getErrorStream());
                LOG.debug("Plain post error response: {}", responseBody);
            } catch (IOException e) {
                // ignore the exceptions since it will thrown an InvalidRequestException
                // anyways
                responseBody = "N/A";
                LOG.warn("Exception reading response: ", e);
            }
            throw new InvalidRequestException(status, responseBody);
        } else {
            try {
                responseBody = getAndClose(conn.getInputStream());
            } catch (IOException e) {
                LOG.warn("Exception reading response: ", e);
                // return null so it can retry
                return null;
            }
        }
        if (status / 100 == 5) {
            LOG.error("GCM service is unavailable (status: {})", status);
            return null;
        }
        return responseBody;
    }

    protected String getAndClose(InputStream stream) throws IOException {
        try {
            return getString(stream);
        } finally {
            if (stream != null) {
                close(stream);
            }
        }
    }

    protected String[] split(String line) throws IOException {
        String[] split = line.split("=", 2);
        if (split.length != 2) {
            throw new IOException("Received invalid response line from GCM: " + line);
        }
        return split;
    }

    protected void close(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                // ignore error
                LOG.warn("IOException closing stream", e);
            }
        }
    }

    /**
     * Convenience method to convert an InputStream to a String.
     * <p/>
     * If the stream ends in a newline character, it will be stripped.
     * <p/>
     * If the stream is {@literal null}, returns an empty string.
     */
    protected String getString(InputStream stream) throws IOException {
        if (stream == null) {
            return "";
        }
        BufferedReader reader =
                new BufferedReader(new InputStreamReader(stream));
        StringBuilder content = new StringBuilder();
        String newLine;
        do {
            newLine = reader.readLine();
            if (newLine != null) {
                content.append(newLine).append('\n');
            }
        } while (newLine != null);
        if (content.length() > 0) {
            // strip last newline
            content.setLength(content.length() - 1);
        }
        return content.toString();
    }

    protected Number getNumber(Map<?, ?> json, String field) {
        Object value = json.get(field);
        if (value == null) {
            throw new CustomParserException("Missing field: " + field);
        }
        if (!(value instanceof Number)) {
            throw new CustomParserException("Field " + field +
                    " does not contain a number: " + value);
        }
        return (Number) value;
    }

    protected IOException newIoException(String responseBody, Exception e) {
        String msg = "Error parsing JSON response (" + responseBody + ")";
        LOG.warn(msg, e);
        return new IOException(msg + ":" + e);
    }


}
