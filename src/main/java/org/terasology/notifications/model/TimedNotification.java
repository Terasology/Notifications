// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0

package org.terasology.notifications.model;

/**
 * [INTERNAL] A wrapper to keep track of addition and expiration time of a {@link Notification}.
 *
 * @see org.terasology.notifications.NotificationClientSystem NotificationClientSystem
 * @see org.terasology.notifications.ui.NotificationRenderer NotificationRenderer
 */
public class TimedNotification {

    /**
     * The wrapped notification message.
     */
    private Notification content;

    /**
     * The time at which this notification was added in in-game time (ms).
     */
    private long addedAt;

    /**
     * The time at which this notification expires in in-game time (ms).
     */
    private long expiresAt;

    /**
     * The fade-in time in milliseconds (ms).
     */
    private long fadeInTime;

    /**
     * The fade-out time in milliseconds (ms).
     */
    private long fadeOutTime;

    /**
     * Create a timed notification
     *
     * @param content
     * @param addedAt
     * @param expiresAt
     */
    public TimedNotification(Notification content, long addedAt, long expiresAt) {
        this.content = content;
        this.addedAt = addedAt;
        this.expiresAt = expiresAt;

        this.fadeInTime = Constants.DEFAULT_FADE_IN_TIME;
        this.fadeOutTime = Constants.DEFAULT_FADE_OUT_TIME;
    }

    /**
     * Whether this notification has been expired at the given point in time.
     *
     * @param time the in-game time in milliseconds
     * @return true if the notification has been expired at that point in time, false otherwise
     */
    public boolean hasExpired(long time) {
        return expiresAt > 0 && expiresAt < time;
    }

    /**
     * The wrapped notification message.
     */
    public Notification getContent() {
        return content;
    }
    
    public void setContent(Notification content) {
        this.content = content;
    }

    /**
     * The time at which this notification was added in in-game time (ms).
     */
    public long getAddedAt() {
        return addedAt;
    }

    /**
     * The time at which this notification expires in in-game time (ms).
     * <p>
     * This will return {@link Constants#NEVER} for notifications that should be shown indefinitely.
     */
    public long getExpiresAt() {
        return expiresAt < 0 ? Constants.NEVER : expiresAt;
    }

    /**
     * Set the time at which this notification expires in in-game time (ms).
     * <p>
     * Use {@link Constants#NEVER} for notifications that should be shown indefinitely.
     */
    public void setExpiresAt(long expiresAt) {
        this.expiresAt = expiresAt;
    }

    /**
     * The fade-in time in milliseconds (ms).
     */
    public long getFadeInTime() {
        return fadeInTime;
    }

    /**
     * Set the fade-in time in milliseconds (ms).
     */
    public void setFadeInTime(long fadeInTime) {
        this.fadeInTime = fadeInTime;
    }

    /**
     * The fade-out time in milliseconds (ms).
     */
    public long getFadeOutTime() {
        return fadeOutTime;
    }

    /**
     * Set the fade-out time in milliseconds (ms).
     */
    public void setFadeOutTime(long fadeOutTime) {
        this.fadeOutTime = fadeOutTime;
    }
}
