package org.apache.camel.component.google.gcm;

import org.apache.camel.Processor;
import org.apache.camel.impl.DefaultConsumer;
/**
 * The GoogleCloudMessaging consumer.
 */
public class GCMConsumer extends DefaultConsumer {

    public GCMConsumer(GCMEndpoint endpoint, Processor processor) throws Exception {
        super(endpoint, processor);
        throw new RuntimeException("Invalid. There is nothing to consume from GCM");
    }
}
