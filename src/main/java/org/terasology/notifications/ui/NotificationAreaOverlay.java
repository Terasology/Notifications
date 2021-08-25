// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0

package org.terasology.notifications.ui;

import org.terasology.engine.core.Time;
import org.terasology.engine.entitySystem.entity.EntityRef;
import org.terasology.engine.logic.players.LocalPlayer;
import org.terasology.engine.registry.In;
import org.terasology.engine.rendering.nui.CoreScreenLayer;
import org.terasology.gestalt.assets.ResourceUrn;
import org.terasology.notifications.model.NotificationComponent;
import org.terasology.notifications.model.TimedNotification;
import org.terasology.nui.databinding.ReadOnlyBinding;
import org.terasology.nui.widgets.UIList;

import java.util.ArrayList;
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
        notificationArea.setInteractive(false);
        notificationArea.setCanBeFocus(false);
        notificationArea.setItemRenderer(new NotificationRenderer(time));

        // This binding will do a lookup of the currently active notifications stored in the NotificationComponent of
        // the client entity associated with the local player. That way the list is automatically updated every time the
        // binding is recomputed.
        notificationArea.bindList(new ReadOnlyBinding<List<TimedNotification>>() {
            @Override
            public List<TimedNotification> get() {
                EntityRef client = localPlayer.getClientEntity();
                List<TimedNotification> timedNotifications = Optional.ofNullable(client.getComponent(NotificationComponent.class))
                        .map(component -> component.notifications)
                        .orElse(Collections.emptyList());
                return timedNotifications;
            }
        });
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
