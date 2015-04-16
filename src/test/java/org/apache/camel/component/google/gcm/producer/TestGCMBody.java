package org.apache.camel.component.google.gcm.producer;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.google.gcm.AbstractGCMTestSupport;
import org.apache.camel.component.mock.MockEndpoint;
import org.junit.Test;

/**
 * Created by miki on 16.04.2015.
 */
public class TestGCMBody extends AbstractGCMTestSupport {
    @Test
    public void testGoogleCloudMessaging() throws Exception {
        MockEndpoint mock = getMockEndpoint("mock:result");
        mock.expectedMinimumMessageCount(3);
        sendBody("direct:foo", "Hello World");


        assertMockEndpointsSatisfied();
    }

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            public void configure() {
                from("direct:foo")
                        .to("google-cloud-messaging://test")
                        .to("mock:result");
            }
        };
    }

}
