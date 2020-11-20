// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0

package org.terasology.notifications.events;

import org.terasology.entitySystem.event.AbstractConsumableEvent;
import org.terasology.notifications.model.Notification;

public class RemoveNotificationEvent extends AbstractConsumableEvent {
    public final String id;

    public RemoveNotificationEvent(String id) {
        this.id = id;
    }

    public RemoveNotificationEvent(Notification notification) {
        this.id = notification.getId();
    }
}
