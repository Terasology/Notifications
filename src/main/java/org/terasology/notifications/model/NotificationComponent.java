// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0

package org.terasology.notifications.model;

import org.terasology.gestalt.entitysystem.component.Component;
import org.terasology.notifications.events.ExpireNotificationEvent;
import org.terasology.notifications.events.ShowNotificationEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * [INTERNAL] Not for public access! Managed by {@link org.terasology.notifications.NotificationClientSystem}
 * <p>
 * Please use {@link ShowNotificationEvent} and {@link ExpireNotificationEvent} to modify the displayed notifications.
 */
public class NotificationComponent implements Component<NotificationComponent> {
    /**
     * The ordered list of notifications to show in the notification overlay.
     */
    public List<TimedNotification> notifications;

    public NotificationComponent() {
        notifications = new ArrayList<>();
    }

    @Override
    public void copy(NotificationComponent other) {
        notifications.clear();
        notifications.addAll(other.notifications);
    }
}
