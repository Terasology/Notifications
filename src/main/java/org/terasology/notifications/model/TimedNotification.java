// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0

package org.terasology.notifications.model;

public class TimedNotification {
    public static final int FADE_IN_TIME = 100;
    public static final int FADE_OUT_TIME = 5000;

    private Notification content;
    private long added;
    private long expires;

    public TimedNotification(Notification content, long added, long expires) {
        this.content = content;
        this.added = added;
        this.expires = expires;
    }

    public Notification getContent() {
        return content;
    }

    public void setContent(Notification content) {
        this.content = content;
    }

    public long getAdded() {
        return added;
    }

    public void setAdded(long added) {
        this.added = added;
    }

    public long getExpires() {
        return expires;
    }

    public void setExpires(long expires) {
        this.expires = expires;
    }
}
