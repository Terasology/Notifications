// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0

package org.terasology.notifications;

import org.terasology.entitySystem.event.AbstractConsumableEvent;

public class AddNotificationEvent extends AbstractConsumableEvent {
    Notification notification;

    public AddNotificationEvent(Notification notification) {
        this.notification = notification;
    }
}
