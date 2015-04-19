package org.apache.camel.component.google.gcm.producer.builders;

import org.apache.camel.component.google.gcm.model.GCMBody;
import org.apache.camel.component.google.gcm.producer.constants.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

import static org.apache.camel.component.google.gcm.oldclient.GoogleConstants.*;

/**
 * Created by miki on 15.04.2015.
 */
public class HTTPostBuilder extends AbstractBuilder {

    private static final Logger LOG = LoggerFactory.getLogger(HTTPostBuilder.class);

    private StringBuilder body;

    public HTTPostBuilder(GCMBody message, String registrationId) {
        super(message);
        body = new StringBuilder(PARAM_REGISTRATION_ID).append('=').append(Util.nonNull(registrationId));
    }

    public String build() {
        this.addDelayWhileIdle()
                .addCollapseKey()
                .addDryRun()
                .addRestrictedPackageName()
                .addTimeToLive()
                .addPayLoad();
        return body.toString();
    }

    protected HTTPostBuilder addPayLoad() {
        for (Map.Entry<String, String> entry : this.message.getData().entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            if (key == null || value == null) {
                LOG.warn("Ignoring payload entry thas has null: {} ", entry);
            } else {
                key = PARAM_PAYLOAD_PREFIX + key;
                try {
                    addParameter(key, URLEncoder.encode(value, UTF8));
                } catch (UnsupportedEncodingException e) {
                    LOG.warn("UnsupportedEncodingException. Unable to encode payload : {} ", e);
                }
            }
        }

        return this;
    }


    protected HTTPostBuilder addTimeToLive() {
        Integer timeToLive = this.message.getTimeToLive();
        if (timeToLive != null) {
            addParameter(PARAM_TIME_TO_LIVE, Integer.toString(timeToLive));
        }
        return this;
    }

    protected HTTPostBuilder addRestrictedPackageName() {
        String restrictedPackageName = this.message.getRestrictedPackageName();
        if (restrictedPackageName != null) {
            addParameter(PARAM_RESTRICTED_PACKAGE_NAME, restrictedPackageName);
        }
        return this;
    }

    protected HTTPostBuilder addCollapseKey() {
        String collapseKey = this.message.getCollapseKey();
        if (collapseKey != null) {
            addParameter(PARAM_COLLAPSE_KEY, collapseKey);
        }
        return this;
    }

    protected HTTPostBuilder addDryRun() {
        if (this.message.isDryRun() != null) {
            addParameter(PARAM_DRY_RUN, this.message.isDryRun() ? "1" : "0");
        }
        return this;
    }

    protected HTTPostBuilder addDelayWhileIdle() {
        if (this.message.isDelayWhileIdle() != null) {
            addParameter(PARAM_DELAY_WHILE_IDLE, this.message.isDelayWhileIdle() ? "1" : "0");
        }
        return this;
    }

    /**
     * Adds a new parameter to the HTTP POST body.
     *
     * @param name  parameter's name.
     * @param value parameter's value.
     */
    protected void addParameter(String name,
                                String value) {
        Util.nonNull(this.body).append('&')
                .append(Util.nonNull(name)).append('=').append(Util.nonNull(value));
    }


}
