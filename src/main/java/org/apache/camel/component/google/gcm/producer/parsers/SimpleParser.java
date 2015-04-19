package org.apache.camel.component.google.gcm.producer.parsers;

import org.apache.camel.component.google.gcm.model.GCMResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static org.apache.camel.component.google.gcm.oldclient.GoogleConstants.TOKEN_CANONICAL_REG_ID;
import static org.apache.camel.component.google.gcm.oldclient.GoogleConstants.TOKEN_ERROR;
import static org.apache.camel.component.google.gcm.oldclient.GoogleConstants.TOKEN_MESSAGE_ID;

/**
 * Parsing an HTTP Post request;
 * Created by miki on 16.04.2015.
 */
public class SimpleParser extends AbstractParser {

    private static final Logger LOG = LoggerFactory.getLogger(SimpleParser.class);

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


}
