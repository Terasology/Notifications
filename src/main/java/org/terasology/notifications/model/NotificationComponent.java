// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0

package org.terasology.notifications.model;

import org.terasology.entitySystem.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Not for public access! Managed by {@link org.terasology.notifications.NotificationClientSystem}
 * <p>
 * Please use {@link org.terasology.notifications.events.AddNotificationEvent} and {@link
 * org.terasology.notifications.events.RemoveNotificationEvent} to modify the displayed notifications.
 */
public class NotificationComponent implements Component {
    public List<TimedNotification> notifications;

    public NotificationComponent() {
        notifications = new ArrayList<>();
    }

    public List<TimedNotification> getNotifications() {
        return notifications;
    }
}
