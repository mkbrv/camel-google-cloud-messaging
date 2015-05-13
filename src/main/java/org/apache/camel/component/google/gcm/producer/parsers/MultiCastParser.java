package org.apache.camel.component.google.gcm.producer.parsers;

import org.apache.camel.component.google.gcm.model.GCMResponse;
import org.apache.camel.component.google.gcm.model.MultiCastResponse;
import org.apache.camel.component.google.gcm.producer.exceptions.CustomParserException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.apache.camel.component.google.gcm.configuration.GoogleConstants.JSON_CANONICAL_IDS;
import static org.apache.camel.component.google.gcm.configuration.GoogleConstants.JSON_ERROR;
import static org.apache.camel.component.google.gcm.configuration.GoogleConstants.JSON_FAILURE;
import static org.apache.camel.component.google.gcm.configuration.GoogleConstants.JSON_MESSAGE_ID;
import static org.apache.camel.component.google.gcm.configuration.GoogleConstants.JSON_MULTICAST_ID;
import static org.apache.camel.component.google.gcm.configuration.GoogleConstants.JSON_RESULTS;
import static org.apache.camel.component.google.gcm.configuration.GoogleConstants.JSON_SUCCESS;
import static org.apache.camel.component.google.gcm.configuration.GoogleConstants.TOKEN_CANONICAL_REG_ID;

/**
 * Created by miki on 19.04.2015.
 */
public class MultiCastParser extends AbstractParser {

    public MultiCastResponse parseResponseString(String responseBody) throws IOException {
        JSONParser parser = new JSONParser();
        JSONObject jsonResponse;
        try {
            jsonResponse = (JSONObject) parser.parse(responseBody);
            int success = getNumber(jsonResponse, JSON_SUCCESS).intValue();
            int failure = getNumber(jsonResponse, JSON_FAILURE).intValue();
            int canonicalIds = getNumber(jsonResponse, JSON_CANONICAL_IDS).intValue();
            long multicastId = getNumber(jsonResponse, JSON_MULTICAST_ID).longValue();
            MultiCastResponse.Builder builder = new MultiCastResponse.Builder(success,
                    failure, canonicalIds, multicastId);
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> results =
                    (List<Map<String, Object>>) jsonResponse.get(JSON_RESULTS);
            if (results != null) {
                for (Map<String, Object> jsonResult : results) {
                    String messageId = (String) jsonResult.get(JSON_MESSAGE_ID);
                    String canonicalRegId =
                            (String) jsonResult.get(TOKEN_CANONICAL_REG_ID);
                    String error = (String) jsonResult.get(JSON_ERROR);
                    GCMResponse result = new GCMResponse.Builder()
                            .messageId(messageId)
                            .canonicalRegistrationId(canonicalRegId)
                            .errorCode(error)
                            .build();
                    builder.addResult(result);
                }
            }
            return builder.build();
        } catch (ParseException | CustomParserException e) {
            throw newIoException(responseBody, e);
        }
    }
}
