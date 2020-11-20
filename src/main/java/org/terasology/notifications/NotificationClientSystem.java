// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0

package org.terasology.notifications;

import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.entitySystem.event.ReceiveEvent;
import org.terasology.entitySystem.systems.BaseComponentSystem;
import org.terasology.entitySystem.systems.RegisterMode;
import org.terasology.entitySystem.systems.RegisterSystem;
import org.terasology.notifications.ui.NotificationAreaOverlay;
import org.terasology.registry.In;
import org.terasology.rendering.nui.NUIManager;

@RegisterSystem(RegisterMode.CLIENT)
public class NotificationClientSystem extends BaseComponentSystem {

    @In
    private NUIManager nuiManager;

    private NotificationAreaOverlay overlay;

    @Override
    public void initialise() {
        overlay = nuiManager.addOverlay(NotificationAreaOverlay.ASSET_URI, NotificationAreaOverlay.class);
    }

    @Override
    public void shutdown() {
        nuiManager.closeScreen(overlay);
    }

    @ReceiveEvent
    public void onNotificationAdded(AddNotificationEvent event, EntityRef entity) {
        overlay.add(event.notification);
    }

    @ReceiveEvent
    public void onNotificationRemoved(RemoveNotificationEvent event, EntityRef entity) {
        overlay.remove(event.id);
    }


}
