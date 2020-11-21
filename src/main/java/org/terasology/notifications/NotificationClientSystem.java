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
import org.terasology.notifications.model.Notification;
import org.terasology.notifications.model.NotificationComponent;
import org.terasology.notifications.ui.NotificationAreaOverlay;
import org.terasology.registry.In;
import org.terasology.rendering.nui.NUIManager;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
                final List<String> expired = component.notificationEndTimes.entrySet().stream()
                        .filter(entry -> entry.getValue() > 0)
                        .filter(entry -> entry.getValue() < current)
                        .map(Map.Entry::getKey)
                        .collect(Collectors.toList());

                component.notifications.removeIf(n -> expired.contains(n.getId()));

                if (component.notifications.isEmpty()) {
                    return null;
                } else {
                    expired.forEach(id -> component.notificationEndTimes.remove(id));
                    component.nextEndTime =
                            component.notificationEndTimes.values().stream()
                                    .filter(endTime -> endTime > 0)
                                    .sorted()
                                    .findFirst()
                                    .orElse(Long.MAX_VALUE);
                    return component;
                }
            });
            lastCheck = current;
        }
    }

    private static Function<NotificationComponent, NotificationComponent> addNotification(final Notification notification, long expires) {
        return component -> {
            if (component.notifications.stream().noneMatch(n -> n.getId().equals(notification.getId()))) {
                component.notifications.add(notification);

            }
            component.notificationEndTimes.put(notification.getId(), expires);
            if (expires > 0 && expires < component.nextEndTime) {
                component.nextEndTime = expires;
            }

            return component;
        };
    }

    @ReceiveEvent(components = ClientComponent.class)
    public void onNotificationAdded(AddNotificationEvent event, EntityRef entity) {
        if (!entity.hasComponent(NotificationComponent.class)) {
            entity.addComponent(new NotificationComponent());
        }
        entity.updateComponent(NotificationComponent.class, addNotification(event.notification, event.expires));
    }

    private static Function<NotificationComponent, NotificationComponent> removeId(String id) {
        return component -> {
            component.notifications.removeIf(n -> n.getId().equals(id));

            if (component.notifications.isEmpty()) {
                return null;
            } else {
                component.notificationEndTimes.remove(id);
                component.nextEndTime =
                        component.notificationEndTimes.values().stream()
                                .filter(endTime -> endTime > 0)
                                .sorted()
                                .findFirst()
                                .orElse(Long.MAX_VALUE);
                return component;
            }
        };
    }

    @ReceiveEvent(components = ClientComponent.class)
    public void onNotificationRemoved(RemoveNotificationEvent event, EntityRef entity) {
        entity.updateComponent(NotificationComponent.class, removeId(event.id));
    }
}
