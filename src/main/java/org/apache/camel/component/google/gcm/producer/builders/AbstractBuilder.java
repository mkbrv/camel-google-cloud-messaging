package org.apache.camel.component.google.gcm.producer.builders;

import org.apache.camel.component.google.gcm.model.GCMBody;

/**
 * Created by miki on 15.04.2015.
 */
public abstract class AbstractBuilder {

    protected GCMBody message;

    public AbstractBuilder(GCMBody message) {
        this.message = message;
    }

    public abstract String build();
}
