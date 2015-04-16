package org.apache.camel.component.google.gcm;

import java.util.Map;

import org.apache.camel.CamelContext;
import org.apache.camel.Endpoint;
import org.apache.camel.component.google.gcm.configuration.GCMConfiguration;
import org.apache.camel.impl.UriEndpointComponent;

/**
 * Represents the component that manages {@link GCMEndpoint}.
 */
public class GCMComponent extends UriEndpointComponent {

    private String apiKey;

    public GCMComponent() {
        super(GCMEndpoint.class);
    }

    public GCMComponent(CamelContext context) {
        super(context, GCMEndpoint.class);
    }

    protected Endpoint createEndpoint(String uri, String remaining, Map<String, Object> parameters) throws Exception {

        GCMConfiguration configuration = new GCMConfiguration();
        //set options from component;
        configuration.setApiKey(apiKey);

        //and then override from parameters
        setProperties(configuration, parameters);

        return new GCMEndpoint(uri, this, configuration);
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }
}
