// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0

package org.terasology.notifications.events;

import org.terasology.entitySystem.event.AbstractConsumableEvent;
import org.terasology.notifications.model.Notification;
import org.terasology.notifications.model.TimedNotification;

/**
 * A request to remove a {@link Notification} from the notification overlay area.
 * <p>
 * This event should be targeted at a client entity (an entity with {@link org.terasology.network.ClientComponent}). If
 * there is a notification in the overlay with the given id it will be removed. Otherwise, this event will be ignored.
 * <p>
 * If not specified otherwise the notification is removed after the default fade-out time.
 */
public class ExpireNotificationEvent extends AbstractConsumableEvent {

    /**
     * Constant to denote that the notification should be expired and removed immediately.
     */
    public static final long IMMEDIATELY = 0;

    /**
     * The id of the {@link Notification} to be removed.
     */
    public final String id;

    /**
     * The duration after which the notification is expired.
     */
    public long expiresIn;

    /**
     * Request to expire a specific notification from the overlay after the default fade-out time.
     *
     * @param id the id of the notification to expire
     */
    public ExpireNotificationEvent(String id) {
        this(id, TimedNotification.DEFAULT_FADE_OUT_TIME);
    }

    /**
     * Request to expire a specific notification from the overlay after the given time.
     * <p>
     * Use {@link ExpireNotificationEvent#IMMEDIATELY} to directly expire a notification and remove it from the
     * overlay.
     *
     * @param id the id of the notification to expire
     * @param expiresIn the duration in ms after which the notification is expired
     */
    public ExpireNotificationEvent(String id, long expiresIn) {
        this.id = id;
        this.expiresIn = expiresIn;
    }
}
