// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0

package org.terasology.notifications;

public class Notification {
    private final String id;

    private String title;
    private String subtitle;
    private String icon;

    public Notification(String id, String title) {
        this(id, title, null);
    }

    public Notification(String id, String title, String icon) {
        this.id = id;
        this.title = title;
        this.icon = icon;
    }

    public Notification(String id, String title, String subtitle, String icon) {
        this.id = id;
        this.title = title;
        this.subtitle = subtitle;
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getId() {
        return id;
    }
}
