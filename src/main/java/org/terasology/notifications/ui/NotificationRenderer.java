// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0

package org.terasology.notifications.ui;

import org.joml.Rectanglei;
import org.joml.Vector2i;
import org.terasology.engine.Time;
import org.terasology.notifications.model.Notification;
import org.terasology.notifications.model.TimedNotification;
import org.terasology.nui.Canvas;
import org.terasology.nui.UITextureRegion;
import org.terasology.nui.itemRendering.AbstractItemRenderer;
import org.terasology.nui.util.RectUtility;
import org.terasology.utilities.Assets;

public class NotificationRenderer extends AbstractItemRenderer<TimedNotification> {
    private final int margin;

    private final Time time;

    public NotificationRenderer(Time time) {
        this(8, time);
    }

    public NotificationRenderer(int margin, Time time) {
        this.margin = margin;
        this.time = time;
    }

    @Override
    public void draw(TimedNotification notification, Canvas canvas) {
        canvas.setAlpha(computeAlpha(notification));
        // Drawing the icon
        UITextureRegion texture = Assets.getTextureRegion(notification.getContent().getIcon()).orElse(null);
        if (texture != null) {
            canvas.drawTexture(texture, RectUtility.createFromMinAndSize(margin, margin, 64, 64));
        }

        // Drawing the text, adjusting for icon width
        int horizontalOffset = (texture != null) ? 2 * margin + 64 : margin;
        drawText(canvas, horizontalOffset, notification.getContent());
        canvas.setAlpha(1f);
    }

    private void drawText(Canvas canvas, int horizontalOffset, Notification notification) {
        // Drawing the text, adjusting for icon width
        String title = notification.getTitle();
        String subtitle = notification.getSubtitle();

        int textHeight = (canvas.getRegion().lengthY() - 3 * margin) / 2;
        Rectanglei titleRegion = RectUtility.createFromMinAndSize(horizontalOffset, margin,
                canvas.getRegion().lengthX() - horizontalOffset, textHeight);

        Rectanglei subtitleRegion = RectUtility.createFromMinAndSize(horizontalOffset, 2 * margin + textHeight,
                canvas.getRegion().lengthX() - horizontalOffset, textHeight);

        canvas.drawText(title, titleRegion);
        canvas.drawText(subtitle, subtitleRegion);
    }

    private float computeAlpha(TimedNotification notification) {
        long currentTime = time.getGameTimeInMs();

        long timeSinceAdded = currentTime - notification.getAdded();
        if (timeSinceAdded < notification.getFadeInTime()) {
            return timeSinceAdded / (float) notification.getFadeInTime();
        }

        long timeUntilExpires = notification.getExpires() - currentTime;
        if (timeUntilExpires < notification.getFadeOutTime()) {
            return timeUntilExpires / (float) notification.getFadeOutTime();
        }

        return 1f;
    }

    @Override
    public Vector2i getPreferredSize(TimedNotification notification, Canvas canvas) {
        return new Vector2i(300, 80);
    }

}
