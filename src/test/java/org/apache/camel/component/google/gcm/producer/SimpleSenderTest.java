package org.apache.camel.component.google.gcm.producer;

import org.apache.camel.component.google.gcm.AbstractGCMTestSupport;
import org.apache.camel.component.google.gcm.model.GCMBody;
import org.apache.camel.component.google.gcm.model.GCMResponse;
import org.apache.camel.component.google.gcm.producer.http.SimpleSender;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by miki on 14.04.2015.
 */
public class SimpleSenderTest extends AbstractGCMTestSupport {

    private String testRegID;

    @Before
    public void setUP() throws Exception {
        testRegID = testProperties.getProperty("test.regid");
    }

    @Test
    public void testSimpleNotification() throws Exception {
        GCMBody message = new GCMBody.Builder()
                .addData("test", "test")
                .collapseKey("test")
                .timeToLive(3)
                .delayWhileIdle(true)
                .dryRun(false)
                .build();

        SimpleSender simpleSender = new SimpleSender(null, testProperties.get("gcmApiKey").toString());

        GCMResponse response = simpleSender.send(message, testRegID);
        assertNotNull(response);
    }
}
