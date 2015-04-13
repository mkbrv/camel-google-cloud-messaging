package org.apache.camel.component.google.gcm;

import java.util.Map;

import org.apache.camel.CamelContext;
import org.apache.camel.Endpoint;

import org.apache.camel.impl.UriEndpointComponent;

/**
 * Represents the component that manages {@link GCMEndpoint}.
 */
public class GCMComponent extends UriEndpointComponent {
    
    public GCMComponent() {
        super(GCMEndpoint.class);
    }

    public GCMComponent(CamelContext context) {
        super(context, GCMEndpoint.class);
    }

    protected Endpoint createEndpoint(String uri, String remaining, Map<String, Object> parameters) throws Exception {
        Endpoint endpoint = new GCMEndpoint(uri, this);
        setProperties(endpoint, parameters);
        return endpoint;
    }
}
