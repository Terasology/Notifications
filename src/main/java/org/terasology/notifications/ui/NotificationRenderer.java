// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0

package org.terasology.notifications.ui;

import org.joml.Rectanglei;
import org.joml.Vector2i;
import org.terasology.notifications.model.Notification;
import org.terasology.nui.Canvas;
import org.terasology.nui.Color;
import org.terasology.nui.FontColor;
import org.terasology.nui.UITextureRegion;
import org.terasology.nui.itemRendering.AbstractItemRenderer;
import org.terasology.nui.util.RectUtility;
import org.terasology.utilities.Assets;

public class NotificationRenderer extends AbstractItemRenderer<Notification> {

    private final int margin;

    protected NotificationRenderer() {
        this(8);
    }

    protected NotificationRenderer(int margin) {
        this.margin = margin;
    }

    @Override
    public void draw(Notification notification, Canvas canvas) {
        // Drawing the icon
        UITextureRegion texture = Assets.getTextureRegion(notification.getIcon()).orElse(null);

        if (texture != null) {
            canvas.drawTexture(texture, RectUtility.createFromMinAndSize(margin, margin, 64, 64));
        }

        // Drawing the text, adjusting for icon width
        String title = notification.getTitle();
        String subtitle = notification.getSubtitle();

        int iconWidth;
        if (texture != null) {
            iconWidth = margin + 64 + margin;
        } else {
            iconWidth = margin;
        }

        int textHeight = (canvas.getRegion().lengthY() - 3 * margin) / 2;
        Rectanglei titleRegion = RectUtility.createFromMinAndSize(iconWidth, margin,
                canvas.getRegion().lengthX() - iconWidth, textHeight);

        Rectanglei subtitleRegion = RectUtility.createFromMinAndSize(iconWidth, 2 * margin + textHeight,
                canvas.getRegion().lengthX() - iconWidth, textHeight);

        canvas.drawText(FontColor.getColored(title, Color.magenta), titleRegion);
        canvas.drawText(subtitle, subtitleRegion);
    }

    @Override
    public Vector2i getPreferredSize(Notification notification, Canvas canvas) {
        return new Vector2i(300, 80);
    }

}
