// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0

package org.terasology.notifications;

import org.terasology.engine.Time;
import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.entitySystem.systems.BaseComponentSystem;
import org.terasology.entitySystem.systems.RegisterSystem;
import org.terasology.logic.console.commandSystem.annotations.Command;
import org.terasology.logic.console.commandSystem.annotations.CommandParam;
import org.terasology.logic.console.commandSystem.annotations.Sender;
import org.terasology.notifications.events.AddNotificationEvent;
import org.terasology.notifications.events.RemoveNotificationEvent;
import org.terasology.notifications.model.Notification;
import org.terasology.registry.In;

import java.util.UUID;

@RegisterSystem
public class NotificationCommands extends BaseComponentSystem {

    @In
    Time time;

    @Command
    public String addNotification(
            @Sender EntityRef sender,
            @CommandParam(value = "title") String title,
            @CommandParam(value = "icon") String icon) {
        String id = UUID.randomUUID().toString();
        sender.send(new AddNotificationEvent(new Notification(id, title, icon)));
        return id;
    }

    @Command
    public String addNotification(
            @Sender EntityRef sender,
            @CommandParam(value = "title") String title,
            @CommandParam(value = "subtitle") String subtitle,
            @CommandParam(value = "icon") String icon) {
        String id = UUID.randomUUID().toString();
        sender.send(new AddNotificationEvent(new Notification(id, title, subtitle, icon)));
        return id;
    }

    @Command
    public String addNotification(
            @Sender EntityRef sender,
            @CommandParam(value = "title") String title,
            @CommandParam(value = "subtitle") String subtitle,
            @CommandParam(value = "icon") String icon,
            @CommandParam(value = "duration") long duration) {
        String id = UUID.randomUUID().toString();
        sender.send(new AddNotificationEvent(new Notification(id, title, subtitle, icon), time.getGameTimeInMs() + duration));
        return id;
    }

    @Command
    public void removeNotification(
            @Sender EntityRef sender,
            @CommandParam(value = "id") String id) {
        sender.send(new RemoveNotificationEvent(id));
    }

}
