// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0

package org.terasology.notifications.events;

import org.terasology.entitySystem.event.AbstractConsumableEvent;
import org.terasology.notifications.model.Notification;

public class AddNotificationEvent extends AbstractConsumableEvent {

    public static final long EXPIRES_NEVER = -1;

    public Notification notification;
    public long expires;

    public AddNotificationEvent(Notification notification) {
        this(notification, EXPIRES_NEVER);
    }

    public AddNotificationEvent(Notification notification, long expires) {
        this.notification = notification;
        this.expires = expires;
    }
}
