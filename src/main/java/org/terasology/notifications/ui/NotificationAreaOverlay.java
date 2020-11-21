// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0

package org.terasology.notifications.ui;

import org.terasology.assets.ResourceUrn;
import org.terasology.engine.Time;
import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.logic.players.LocalPlayer;
import org.terasology.notifications.model.NotificationComponent;
import org.terasology.notifications.model.TimedNotification;
import org.terasology.nui.databinding.ReadOnlyBinding;
import org.terasology.nui.widgets.UIList;
import org.terasology.registry.In;
import org.terasology.rendering.nui.CoreScreenLayer;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class NotificationAreaOverlay extends CoreScreenLayer {

    public static final ResourceUrn ASSET_URI = new ResourceUrn("Notifications:NotificationAreaOverlay");

    @In
    LocalPlayer localPlayer;
    @In
    Time time;

    private UIList<TimedNotification> notificationArea;

    @Override
    public void initialise() {
        notificationArea = find("notificationArea", UIList.class);
        notificationArea.setItemRenderer(new NotificationRenderer(time));

        notificationArea.bindList(new ReadOnlyBinding<List<TimedNotification>>() {
            @Override
            public List<TimedNotification> get() {
                EntityRef client = localPlayer.getClientEntity();
                return Optional.ofNullable(client.getComponent(NotificationComponent.class))
                        .map(NotificationComponent::getNotifications)
                        .orElse(Collections.emptyList());
            }
        });
    }

    @Override
    public void onClosed() {
        super.onClosed();
        notificationArea.setItemRenderer(null);
    }

    @Override
    public boolean isModal() {
        return false;
    }

    @Override
    public boolean isReleasingMouse() {
        return false;
    }

    @Override
    public boolean isEscapeToCloseAllowed() {
        return false;
    }
}
