// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0

package org.terasology.notifications;

import org.terasology.engine.Time;
import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.entitySystem.event.ReceiveEvent;
import org.terasology.entitySystem.systems.BaseComponentSystem;
import org.terasology.entitySystem.systems.RegisterMode;
import org.terasology.entitySystem.systems.RegisterSystem;
import org.terasology.entitySystem.systems.UpdateSubscriberSystem;
import org.terasology.logic.players.LocalPlayer;
import org.terasology.network.ClientComponent;
import org.terasology.notifications.events.AddNotificationEvent;
import org.terasology.notifications.events.RemoveNotificationEvent;
import org.terasology.notifications.model.NotificationComponent;
import org.terasology.notifications.model.TimedNotification;
import org.terasology.notifications.ui.NotificationAreaOverlay;
import org.terasology.registry.In;
import org.terasology.rendering.nui.NUIManager;

import java.util.List;
import java.util.stream.Collectors;

@RegisterSystem(RegisterMode.CLIENT)
public class NotificationClientSystem extends BaseComponentSystem implements UpdateSubscriberSystem {

    private static final int CHECK_INTERVAL = 200;

    @In
    private NUIManager nuiManager;

    @In
    private Time time;

    @In
    LocalPlayer localPlayer;

    private long lastCheck;

    private NotificationAreaOverlay overlay;

    @Override
    public void initialise() {
        overlay = nuiManager.addOverlay(NotificationAreaOverlay.ASSET_URI, NotificationAreaOverlay.class);
    }

    @Override
    public void shutdown() {
        nuiManager.closeScreen(overlay);
    }

    @Override
    public void update(float delta) {
        long current = time.getGameTimeInMs();
        if (current > lastCheck + CHECK_INTERVAL) {
            EntityRef client = localPlayer.getClientEntity();
            client.updateComponent(NotificationComponent.class, component -> {
                final List<TimedNotification> expired = component.notifications.stream()
                        .filter(notification -> notification.getExpires() > 0 && notification.getExpires() < current)
                        .collect(Collectors.toList());
                component.notifications.removeAll(expired);

                if (component.notifications.isEmpty()) {
                    return null;
                } else {
                    return component;
                }
            });
            lastCheck = current;
        }
    }

    @ReceiveEvent(components = ClientComponent.class)
    public void onNotificationAdded(AddNotificationEvent event, EntityRef entity) {
        if (!entity.hasComponent(NotificationComponent.class)) {
            entity.addComponent(new NotificationComponent());
        }
        entity.updateComponent(NotificationComponent.class, component -> {
            if (component.notifications.stream().noneMatch(n -> n.getContent().getId().equals(event.notification.getId()))) {
                //TODO: ensure end time is after start time
                TimedNotification timedNotification = new TimedNotification(event.notification,
                        time.getGameTimeInMs(), event.expires);
                component.notifications.add(timedNotification);
            }
            return component;
        });
    }

    @ReceiveEvent(components = ClientComponent.class)
    public void onNotificationRemoved(RemoveNotificationEvent event, EntityRef entity) {
        entity.updateComponent(NotificationComponent.class, component -> {
            component.notifications.removeIf(n -> n.getContent().getId().equals(event.id));

            if (component.notifications.isEmpty()) {
                return null;
            } else {
                return component;
            }
        });
    }
}
