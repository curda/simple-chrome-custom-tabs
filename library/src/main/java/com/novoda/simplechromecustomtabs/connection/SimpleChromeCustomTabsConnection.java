package com.novoda.simplechromecustomtabs.connection;

import android.app.Activity;
import android.net.Uri;
import android.support.annotation.NonNull;

public class SimpleChromeCustomTabsConnection implements Connection, ServiceConnectionCallback {

    private final Binder binder;

    private ConnectedClient client;
    private Session session = Session.NULL_SESSION;
    private Uri futureUrl;

    SimpleChromeCustomTabsConnection(Binder binder) {
        this.binder = binder;
    }

    public static Connection newInstance() {
        Binder binder = Binder.newInstance();
        return new SimpleChromeCustomTabsConnection(binder);
    }

    @Override
    public void connectTo(@NonNull Activity activity) {
        binder.bindCustomTabsServiceTo(activity.getApplicationContext(), this);
    }

    @Override
    public void onServiceConnected(ConnectedClient client) {
        this.client = client;

        if (hasConnectedClient()) {
            this.client.warmup();
            session = client.newSession();
            session.mayLaunch(futureUrl);
        }
    }

    private boolean hasConnectedClient() {
        return client != null && client.stillConnected();
    }

    @Override
    public boolean isConnected() {
        return hasConnectedClient();
    }

    @Override
    public void mayLaunch(Uri url) {
        futureUrl = url;
        session.mayLaunch(futureUrl);
    }

    @Override
    public Session getSession() {
        return session;
    }

    @Override
    public void disconnectFrom(@NonNull Activity activity) {
        binder.unbindCustomTabsService(activity.getApplicationContext());
    }

    @Override
    public boolean isDisconnected() {
        return !isConnected();
    }

    @Override
    public void onServiceDisconnected() {
        if (hasConnectedClient()) {
            client.disconnect();
        }
    }

}
