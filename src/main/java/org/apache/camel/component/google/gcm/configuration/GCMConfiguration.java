package org.apache.camel.component.google.gcm.configuration;


import org.apache.camel.spi.Metadata;
import org.apache.camel.spi.UriParam;
import org.apache.camel.spi.UriParams;

/**
 * Created by miki on 10.04.2015.
 */
@UriParams
public final class GCMConfiguration {

    public static final String DEFAULT_COMPONENT_NAME = "google-cloud-messaging";

    @UriParam
    @Metadata(required = "true")
    private String apiKey;

    @UriParam
    private String collapseKey;

    @UriParam
    @Metadata(defaultValue = "3")
    private Integer timeToLive;

    @UriParam
    private Boolean delayWhileIdle;

    @UriParam
    private String restrictedPackageName;

    @UriParam
    private Boolean dryRun;

    @UriParam
    private int retries = 0;


    public String getCollapseKey() {
        return collapseKey;
    }

    public void setCollapseKey(String collapseKey) {
        this.collapseKey = collapseKey;
    }

    public Integer getTimeToLive() {
        return timeToLive;
    }

    public void setTimeToLive(Integer timeToLive) {
        this.timeToLive = timeToLive;
    }

    public Boolean isDelayWhileIdle() {
        return delayWhileIdle;
    }

    public void setDelayWhileIdle(Boolean delayWhileIdle) {
        this.delayWhileIdle = delayWhileIdle;
    }

    public String getRestrictedPackageName() {
        return restrictedPackageName;
    }

    public void setRestrictedPackageName(String restrictedPackageName) {
        this.restrictedPackageName = restrictedPackageName;
    }

    public Boolean isDryRun() {
        return dryRun;
    }

    public void setDryRun(Boolean dryRun) {
        this.dryRun = dryRun;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public int getRetries() {
        return retries;
    }

    public void setRetries(int retries) {
        this.retries = retries;
    }
}
