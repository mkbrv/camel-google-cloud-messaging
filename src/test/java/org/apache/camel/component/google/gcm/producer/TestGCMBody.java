package org.apache.camel.component.google.gcm.producer;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.google.gcm.AbstractGCMTestSupport;
import org.apache.camel.component.google.gcm.model.GCMBody;
import org.apache.camel.component.mock.MockEndpoint;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

/**
 * Created by miki on 16.04.2015.
 */
public class TestGCMBody extends AbstractGCMTestSupport {

    private String testRegID;

    @Before
    public void setUP() throws Exception {
        testRegID = testProperties.getProperty("test.regid");
    }

    @Test
    public void testGoogleCloudMessaging() throws Exception {

        GCMBody message = new GCMBody.Builder()
                .addData("test", "test")
                .collapseKey("test")
                .timeToLive(3)
                .delayWhileIdle(true)
                .dryRun(false)
                .build();
        sendBody("direct:foo", message, new HashMap<String, Object>() {
            {
                put("to", testRegID);
            }
        });
    }

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            public void configure() {
                from("direct:foo")
                        .to("google-cloud-messaging://bar?apiKey=AIzaSyC74HutekXLZ7wNpCH6RTWyg3Mg__BW7sI")
                        .to("mock:result");
            }
        };
    }

}
