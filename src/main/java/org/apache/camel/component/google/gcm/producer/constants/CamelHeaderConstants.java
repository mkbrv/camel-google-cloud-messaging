package org.apache.camel.component.google.gcm.producer.constants;


/**
 * Constants used in camel message headers;
 * Created by miki on 16.04.2015.
 */
public abstract class CamelHeaderConstants {
    public final static String TO = "to";
    public final static String RETRIES = "retries";
    public final static String COLLAPSE_KEY = "collapseKey";
    public final static String DELAY_WHILE_IDLE = "delayWhileIdle";
    public final static String DRY_RUN = "dryRun";
    public final static String RESTRICTED_PACKAGE_NAME = "restrictedPackageName";
    public static final String TIME_TO_LIVE = "timeToLive";
    public static final String DATA = "data";
}
