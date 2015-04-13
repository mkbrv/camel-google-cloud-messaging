package org.apache.camel.component.google.gcm.configuration;


import org.apache.camel.spi.UriParam;

/**
 * Created by miki on 10.04.2015.
 */
public final class GCMConfiguration {

    @UriParam
    private String gcmApiKey;

    @UriParam
    private String gcmUrl;

}
