// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0

package org.terasology.notifications.events;

import org.terasology.engine.entitySystem.event.AbstractConsumableEvent;
import org.terasology.notifications.model.Constants;
import org.terasology.notifications.model.Notification;

/**
 * A request to show a {@link Notification} to the player in the notification overlay area.
 * <p>
 * This event should be targeted at a client entity (an entity with {@link org.terasology.engine.network.ClientComponent}). If
 * not specified otherwise notifications will be shown indefinitely. This event allows to set the expiration time of the
 * notification in in-game time (ms).
 * <p>
 * It is recommended to show a notification for at least 10 seconds to give the player the chance to notice it. See
 * {@link Constants#RECOMMENDED_DURATION}.
 * <p>
 * The event may be consumed to prevent the message from showing.
 *
 * @see org.terasology.notifications.NotificationClientSystem NotificationClientSystem
 */
public class ShowNotificationEvent extends AbstractConsumableEvent {

    /**
     * The notification to show to the player.
     */
    public Notification notification;

    /**
     * How long this notification should be visible on screen in milliseconds (ms).
     */
    public long duration;

    /**
     * Request to show the {@code notification} indefinitely (until it is explicitly removed).
     *
     * @param notification the notification to show
     */
    public ShowNotificationEvent(Notification notification) {
        this(notification, Constants.NEVER);
    }

    /**
     * @param notification the notification to show
     * @param duration the game time in ms when the notification expires
     */
    public ShowNotificationEvent(Notification notification, long duration) {
        this.notification = notification;
        this.duration = duration;
    }
}
