// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0

package org.terasology.notifications;

import org.terasology.engine.core.Time;
import org.terasology.engine.entitySystem.entity.EntityRef;
import org.terasology.engine.entitySystem.systems.BaseComponentSystem;
import org.terasology.engine.entitySystem.systems.RegisterMode;
import org.terasology.engine.entitySystem.systems.RegisterSystem;
import org.terasology.engine.entitySystem.systems.UpdateSubscriberSystem;
import org.terasology.engine.logic.players.LocalPlayer;
import org.terasology.engine.network.ClientComponent;
import org.terasology.engine.registry.In;
import org.terasology.engine.rendering.nui.NUIManager;
import org.terasology.gestalt.entitysystem.event.ReceiveEvent;
import org.terasology.notifications.events.ExpireNotificationEvent;
import org.terasology.notifications.events.ShowNotificationEvent;
import org.terasology.notifications.model.Constants;
import org.terasology.notifications.model.Notification;
import org.terasology.notifications.model.NotificationComponent;
import org.terasology.notifications.model.TimedNotification;
import org.terasology.notifications.ui.NotificationAreaOverlay;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Client-side system to add {@link org.terasology.notifications.model.Notification Notifications} to the players client
 * entity.
 */
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

    /**
     * Periodically checks the clients {@link NotificationComponent} for expired notifications and removes them from the
     * overlay.
     * <p>
     * If the list of shown notifications becomes empty the component is removed.
     *
     * @param delta The time (in seconds) since the last engine update.
     */
    @Override
    public void update(float delta) {
        long current = time.getGameTimeInMs();
        if (current > lastCheck + CHECK_INTERVAL) {
            EntityRef client = localPlayer.getClientEntity();
            client.updateComponent(NotificationComponent.class, component -> {
                final List<TimedNotification> expired = component.notifications.stream()
                        .filter(notification -> notification.hasExpired(current))
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

    /**
     * Compute the timing information for the given notification based on the current time and duration.
     * <p>
     * Checks for whether the duration is negative, i.e., denotes that notification should never expire. The timed
     * notification will then have {@link TimedNotification#getExpiresAt()} set to {@link Constants#NEVER}.
     *
     * @param notification the notification to wrap with timing information
     * @param currentTime the current in-game time in milliseconds (ms)
     * @param duration the duration in milliseconds (ms)
     * @return the notification wrapped with timing information
     */
    private TimedNotification withTimingInformation(Notification notification, long currentTime, long duration) {
        if (duration < 0) {
            return new TimedNotification(notification, currentTime, Constants.NEVER);
        } else {
            return new TimedNotification(notification, currentTime, currentTime + duration);
        }
    }

    /**
     * Add or update the {@link NotificationComponent} for the client entity so that it contains the notification.
     * <p>
     * This will only add a new notification message if no other notification with the same id is present.
     */
    @ReceiveEvent(components = ClientComponent.class)
    public void onNotificationAdded(ShowNotificationEvent event, EntityRef entity) {
        entity.upsertComponent(NotificationComponent.class, maybeComponent -> {
            NotificationComponent component = maybeComponent.orElse(new NotificationComponent());
            if (component.notifications.stream().noneMatch(n -> n.getContent().getId().equals(event.notification.getId()))) {
                component.notifications.add(
                        withTimingInformation(event.notification, time.getGameTimeInMs(), event.duration));
            }
            return component;
        });
    }

    /**
     * Update the expiration time for a notification when receiving a {@link ExpireNotificationEvent}.
     * <p>
     * This will only update the expiration time of the timed notifications but will not modify the list of shown
     * messages.
     */
    @ReceiveEvent(components = ClientComponent.class)
    public void onNotificationRemoved(ExpireNotificationEvent event, EntityRef entity) {
        entity.updateComponent(NotificationComponent.class, component -> {
            component.notifications.stream()
                    .filter(n -> n.getContent().getId().equals(event.id))
                    .forEach(timedNotification -> timedNotification.setExpiresAt(time.getGameTimeInMs() + event.expiresIn));
            return component;
        });
    }
}
