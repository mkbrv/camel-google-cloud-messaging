package org.apache.camel.component.google.gcm.producer.parsers;

import org.apache.camel.component.google.gcm.producer.exceptions.InvalidRequestException;
import org.apache.camel.component.google.gcm.model.GCMResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

import static org.apache.camel.component.google.gcm.oldclient.GoogleConstants.TOKEN_CANONICAL_REG_ID;
import static org.apache.camel.component.google.gcm.oldclient.GoogleConstants.TOKEN_ERROR;
import static org.apache.camel.component.google.gcm.oldclient.GoogleConstants.TOKEN_MESSAGE_ID;

/**
 * Parsing an HTTP Post request;
 * Created by miki on 16.04.2015.
 */
public class SimplePostResponseParser {

    private static final Logger LOG = LoggerFactory.getLogger(SimplePostResponseParser.class);

    public GCMResponse parseResponseString(String responseBody) throws IOException {
        String[] lines = responseBody.split("\n");
        if (lines.length == 0 || lines[0].equals("")) {
            throw new IOException("Received empty response from GCM service.");
        }
        String firstLine = lines[0];
        String[] responseParts = split(firstLine);
        String token = responseParts[0];
        String value = responseParts[1];
        switch (token) {
            case TOKEN_MESSAGE_ID:
                GCMResponse.Builder builder = new GCMResponse.Builder().messageId(value);
                // check for canonical registration id
                if (lines.length > 1) {
                    String secondLine = lines[1];
                    responseParts = split(secondLine);
                    token = responseParts[0];
                    value = responseParts[1];
                    if (token.equals(TOKEN_CANONICAL_REG_ID)) {
                        builder.canonicalRegistrationId(value);
                    } else {
                        LOG.warn("Invalid response from GCM: {}", responseBody);
                    }
                }
                GCMResponse result = builder.build();
                LOG.debug("GCMResponse created succesfully {},", result);
                return result;
            case TOKEN_ERROR:
                return new GCMResponse.Builder().errorCode(value).build();
            default:
                throw new IOException("Invalid response from GCM: " + responseBody);
        }
    }


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

    private static String getAndClose(InputStream stream) throws IOException {
        try {
            return getString(stream);
        } finally {
            if (stream != null) {
                close(stream);
            }
        }
    }

    private String[] split(String line) throws IOException {
        String[] split = line.split("=", 2);
        if (split.length != 2) {
            throw new IOException("Received invalid response line from GCM: " + line);
        }
        return split;
    }

    private static void close(Closeable closeable) {
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
    protected static String getString(InputStream stream) throws IOException {
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
}
