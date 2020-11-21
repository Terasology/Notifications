// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0

package org.terasology.notifications;

import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.entitySystem.systems.BaseComponentSystem;
import org.terasology.entitySystem.systems.RegisterSystem;
import org.terasology.logic.console.commandSystem.annotations.Command;
import org.terasology.logic.console.commandSystem.annotations.CommandParam;
import org.terasology.logic.console.commandSystem.annotations.Sender;
import org.terasology.notifications.events.ExpireNotificationEvent;
import org.terasology.notifications.events.ShowNotificationEvent;
import org.terasology.notifications.model.Notification;

import java.util.Optional;
import java.util.UUID;

@RegisterSystem
public class NotificationCommands extends BaseComponentSystem {
    @Command
    public String showNotification(
            @Sender EntityRef sender,
            @CommandParam(value = "title") String title,
            @CommandParam(value = "subtitle", required = false) String subtitle,
            @CommandParam(value = "icon", required = false) String icon,
            @CommandParam(value = "duration", required = false) Long duration) {
        String id = UUID.randomUUID().toString();

        final Notification notification =
                new Notification(id, title, subtitle, icon);

        ShowNotificationEvent event = new ShowNotificationEvent(notification);
        if (duration != null) {
            event.duration = duration;
        }
        sender.send(event);
        return id;
    }

    @Command
    public void expireNotification(
            @Sender EntityRef sender,
            @CommandParam(value = "id") String id,
            @CommandParam(value = "expiresIn", required = false) Long expiresIn) {
        sender.send(new ExpireNotificationEvent(id,
                Optional.ofNullable(expiresIn).orElse(ExpireNotificationEvent.IMMEDIATELY)));
    }

}
