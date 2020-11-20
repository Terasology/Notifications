// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0

package org.terasology.notifications.ui;

import org.terasology.assets.ResourceUrn;
import org.terasology.notifications.Notification;
import org.terasology.nui.widgets.UIList;
import org.terasology.rendering.nui.CoreScreenLayer;

import java.util.LinkedList;
import java.util.List;

public class NotificationAreaOverlay extends CoreScreenLayer {

    public static final ResourceUrn ASSET_URI = new ResourceUrn("Notifications:NotificationAreaOverlay");

    private List<Notification> notifications = new LinkedList<>();
    private UIList<Notification> notificationArea;

    @Override
    public void initialise() {
        notificationArea = find("notificationArea", UIList.class);
        notificationArea.setItemRenderer(new NotificationRenderer());
        notificationArea.setList(notifications);
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

    public void add(Notification notification) {
        if (notifications.stream().noneMatch(n -> n.getId().equals(notification.getId()))) {
            notifications.add(notification);
        }
    }

    public void remove(String id) {
        notifications.removeIf(notification -> notification.getId().equals(id));
    }
}
