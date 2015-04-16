package org.apache.camel.component.google.gcm.producer.builders;

import org.apache.camel.component.google.gcm.model.GCMBody;
import org.apache.camel.component.google.gcm.producer.constants.Util;
import org.json.simple.JSONValue;

import java.util.HashMap;
import java.util.Set;
import java.util.Map;

import static org.apache.camel.component.google.gcm.oldclient.GoogleConstants.*;

/**
 * Created by miki on 15.04.2015.
 */
public class JsonBuilder extends AbstractBuilder {

    private Set<String> registrationIds;
    private Map<Object, Object> jsonRequest;


    public JsonBuilder(GCMBody message, Set<String> registrationIds) {
        super(message);
        this.registrationIds = registrationIds;
        jsonRequest = new HashMap<>();
    }

    @Override
    public String build() {
        if (Util.nonNull(registrationIds).isEmpty()) {
            throw new IllegalArgumentException("registrationIds cannot be empty");
        }
        jsonRequest.put(JSON_REGISTRATION_IDS, registrationIds);


        setJsonField(PARAM_TIME_TO_LIVE, this.message.getTimeToLive());
        setJsonField(PARAM_COLLAPSE_KEY, this.message.getCollapseKey());
        setJsonField(PARAM_RESTRICTED_PACKAGE_NAME, this.message.getRestrictedPackageName());
        setJsonField(PARAM_DELAY_WHILE_IDLE, this.message.isDelayWhileIdle());
        setJsonField(PARAM_DRY_RUN, this.message.isDryRun());

        addPayload();
        return JSONValue.toJSONString(jsonRequest);
    }

    protected JsonBuilder addPayload() {
        Map<String, String> payload = this.message.getData();
        if (!payload.isEmpty()) {
            jsonRequest.put(JSON_PAYLOAD, payload);
        }
        return this;
    }

    /**
     * Sets a JSON field, but only if the value is not {@literal null}.
     */
    private void setJsonField(String field,
                              Object value) {
        if (value != null) {
            jsonRequest.put(field, value);
        }
    }
}
