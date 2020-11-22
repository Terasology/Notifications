// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0

package org.terasology.notifications.model;

import com.google.common.base.Preconditions;

/**
 * A notification message to be displayed to the user.
 * <p>
 * It consist of three parts, a title, a subtitle, and an icon. Every part is optional, but it is highly recommended to
 * provide at least a title and an icon.
 * <p>
 * Notification messages are short snippets of text displayed to the user. They are by default displayed in boxes of
 * fixed size. Therefore, both title and subtitle should be kept short to fit into the available space.
 */
public class Notification {
    private final String id;

    private String title;
    private String subtitle;
    private String iconUri;

    /**
     * Create an empty notification.
     *
     * @param id the identifier for this notification
     */
    public Notification(String id) {
        this(id, "", "", null);
    }

    /**
     * Create a complete notification.
     *
     * @param id the identifier for this notification
     * @param title the title text
     * @param subtitle the subtitle text
     * @param iconUri the URI of the icon to display, may be {@code null}
     */
    public Notification(String id, String title, String subtitle, String iconUri) {
        Preconditions.checkNotNull(id);
        this.id = id;
        this.title = title;
        this.subtitle = subtitle;
        this.iconUri = iconUri;
    }

    /**
     * The title to display.
     *
     * @return the title string; not {@code null}
     */
    public String getTitle() {
        return title == null ? "" : title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * The subtitle to display.
     *
     * @return the subtitle; not {@code null}
     */
    public String getSubtitle() {
        return subtitle == null ? "" : subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    /**
     * The URI of the icon texture to show.
     *
     * @return the icon URI, may be {@code null} if the icon should be left blank
     */
    public String getIconUri() {
        return iconUri;
    }

    public void setIconUri(String iconUri) {
        this.iconUri = iconUri;
    }

    /**
     * The identifier for this notification.
     * <p>
     * Two notifications with the same id should be considered to reference the same logic notification, even though
     * their content may differ.
     *
     * @return the id of this notification
     */
    public String getId() {
        return id;
    }
}
