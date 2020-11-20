// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0

package org.terasology.notifications.ui;

import org.terasology.assets.ResourceUrn;
import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.logic.players.LocalPlayer;
import org.terasology.notifications.model.Notification;
import org.terasology.notifications.model.NotificationComponent;
import org.terasology.nui.databinding.ReadOnlyBinding;
import org.terasology.nui.widgets.UIList;
import org.terasology.registry.In;
import org.terasology.rendering.nui.CoreScreenLayer;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class NotificationAreaOverlay extends CoreScreenLayer {

    public static final ResourceUrn ASSET_URI = new ResourceUrn("Notifications:NotificationAreaOverlay");

    @In
    LocalPlayer localPlayer;

    private List<Notification> notifications = new LinkedList<>();
    private UIList<Notification> notificationArea;

    @Override
    public void initialise() {
        notificationArea = find("notificationArea", UIList.class);
        notificationArea.setItemRenderer(new NotificationRenderer());

        notificationArea.bindList(new ReadOnlyBinding<List<Notification>>() {
            @Override
            public List<Notification> get() {
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
