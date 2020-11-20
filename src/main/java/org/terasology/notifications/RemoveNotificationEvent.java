// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0

package org.terasology.notifications;

import org.terasology.entitySystem.event.AbstractConsumableEvent;

public class RemoveNotificationEvent extends AbstractConsumableEvent {
    final String id;

    public RemoveNotificationEvent(String id) {
        this.id = id;
    }

    public RemoveNotificationEvent(Notification notification) {
        this.id = notification.getId();
    }
}
