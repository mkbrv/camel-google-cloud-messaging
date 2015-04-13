package org.apache.camel.component.google.gcm;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.junit.Test;

public class GCMComponentTest extends AbstractGCMTestSupport {

    @Test
    public void testGoogleCloudMessaging() throws Exception {
        MockEndpoint mock = getMockEndpoint("mock:result");
        mock.expectedMinimumMessageCount(1);
        sendBody("direct:foo", "Hello World");


        assertMockEndpointsSatisfied();
    }

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            public void configure() {
                from("direct:foo")
                        .to("google-cloud-messaging://bar")
                        .to("mock:result");
            }
        };
    }
}
