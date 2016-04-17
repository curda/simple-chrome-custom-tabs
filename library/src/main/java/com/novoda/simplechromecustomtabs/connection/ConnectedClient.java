package com.novoda.simplechromecustomtabs.connection;

import android.support.annotation.NonNull;
import android.support.customtabs.CustomTabsClient;

import com.novoda.notils.exception.DeveloperError;

class ConnectedClient {

    private static final int NO_FLAGS = 0;

    private CustomTabsClient customTabsClient;

    public ConnectedClient(@NonNull CustomTabsClient customTabsClient) {
        this.customTabsClient = customTabsClient;
    }

    public Session newSession() {
        if (!stillConnected()) {
            throw new DeveloperError("Cannot start session on a disconnected client. Use stillConnected() to check connection");
        }

        return SimpleChromeCustomTabsSession.newSessionFor(customTabsClient);
    }

    public boolean stillConnected() {
        return customTabsClient != null;
    }

    public void warmup() {
        if (!stillConnected()) {
            throw new DeveloperError("Cannot warm up a disconnected client. Use stillConnected() to check connection");
        }

        customTabsClient.warmup(NO_FLAGS);
    }

    public void disconnect() {
        customTabsClient = null;
    }

}
