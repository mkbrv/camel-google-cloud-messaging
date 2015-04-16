package org.apache.camel.component.google.gcm.producer.http;

import org.apache.camel.component.google.gcm.GCMEndpoint;
import org.apache.camel.component.google.gcm.model.GCMBody;
import org.apache.camel.component.google.gcm.producer.GCMProducer;
import org.apache.camel.component.google.gcm.producer.builders.PlainTextBodyBuilder;
import org.apache.camel.component.google.gcm.producer.parsers.SimplePostResponseParser;
import org.apache.camel.component.google.gcm.model.GCMResponse;
import org.apache.camel.component.google.gcm.model.MultiCastResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Set;

import static org.apache.camel.component.google.gcm.oldclient.GoogleConstants.GCM_SEND_ENDPOINT;

/**
 * Created by miki on 15.04.2015.
 */
public class SimpleSender extends GCMProducer {

    private static final Logger LOG = LoggerFactory.getLogger(SimpleSender.class);

    public SimpleSender(GCMEndpoint endpoint, String apiKey) {
        super(endpoint, apiKey);
    }

    @Override
    public MultiCastResponse send(GCMBody message, Set<String> regIds) {
        return null;
    }

    /**
     * @param message to be pushed to GCM; (body of camel message ) ;
     * @param regId   device id where to send the message;
     * @return response from google, including errors;
     */
    @Override
    public GCMResponse send(GCMBody message, String regId) {
        String requestBody = new PlainTextBodyBuilder(message, regId).build();
        LOG.debug("Request body: {}", requestBody);
        try {
            HttpURLConnection conn = post(GCM_SEND_ENDPOINT, "application/x-www-form-urlencoded;charset=UTF-8", requestBody);
            SimplePostResponseParser parser = new SimplePostResponseParser();
            String response = parser.getResponseFromConnection(conn);
            return parser.parseResponseString(response);
        } catch (Exception e) {
            LOG.error("Exception posting to GCM", e);
            return null;
        }
    }


    /**
     * Makes an HTTP POST request to a given endpoint.
     * <p/>
     * <p/>
     * <strong>Note: </strong> the returned connected should not be disconnected,
     * otherwise it would kill persistent connections made using Keep-Alive.
     *
     * @param url         endpoint to post the request.
     * @param contentType type of request.
     * @param body        body of the request.
     * @return the underlying connection.
     * @throws IOException propagated from underlying methods.
     */
    protected HttpURLConnection post(String url, String contentType, String body)
            throws IOException {
        if (url == null || body == null) {
            throw new IllegalArgumentException("arguments cannot be null");
        }
        byte[] bytes = body.getBytes();
        HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
        conn.setDoOutput(true);
        conn.setUseCaches(false);
        conn.setFixedLengthStreamingMode(bytes.length);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", contentType);
        conn.setRequestProperty("Authorization", "key=" + "KEY");
        OutputStream out = conn.getOutputStream();
        try {
            out.write(bytes);
        } finally {
            close(out);
        }
        return conn;
    }
}
