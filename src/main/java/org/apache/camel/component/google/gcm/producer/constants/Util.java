package org.apache.camel.component.google.gcm.producer.constants;

/**
 * Created by miki on 16.04.2015.
 */
public abstract class Util {
    /**
     * @param argument to be checked if null;
     * @param <T>      type can be whatever;
     * @return argument if not null; If null exceptions is thrown;
     */
    public static <T> T nonNull(T argument) {
        if (argument == null) {
            throw new IllegalArgumentException("argument cannot be null");
        }
        return argument;
    }
}
