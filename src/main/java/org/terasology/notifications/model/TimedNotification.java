// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0

package org.terasology.notifications.model;

public class TimedNotification {
    public static final int DEFAULT_FADE_IN_TIME = 100;
    public static final int DEFAULT_FADE_OUT_TIME = 5000;

    private Notification content;
    private long added;
    private long expires;

    private long fadeInTime;
    private long fadeOutTime;

    public TimedNotification(Notification content, long added, long expires) {
        this.content = content;
        this.added = added;
        this.expires = expires;

        this.fadeInTime = DEFAULT_FADE_IN_TIME;
        this.fadeOutTime = DEFAULT_FADE_OUT_TIME;
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

    public long getFadeInTime() {
        return fadeInTime;
    }

    public void setFadeInTime(long fadeInTime) {
        this.fadeInTime = fadeInTime;
    }

    public long getFadeOutTime() {
        return fadeOutTime;
    }

    public void setFadeOutTime(long fadeOutTime) {
        this.fadeOutTime = fadeOutTime;
    }
}
