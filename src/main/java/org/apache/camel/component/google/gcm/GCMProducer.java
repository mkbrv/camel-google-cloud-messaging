package org.apache.camel.component.google.gcm;

import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The GoogleCloudMessaging producer.
 */
public class GCMProducer extends DefaultProducer {
    private static final Logger LOG = LoggerFactory.getLogger(GCMProducer.class);
    private GCMEndpoint endpoint;

    public GCMProducer(GCMEndpoint endpoint) {
        super(endpoint);
        this.endpoint = endpoint;
    }

    public void process(Exchange exchange) throws Exception {
        System.out.println(exchange.getIn().getBody());

    }

}
