// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0

package org.terasology.notifications.ui;

import org.joml.Vector2i;
import org.terasology.engine.core.Time;
import org.terasology.engine.utilities.Assets;
import org.terasology.joml.geom.Rectanglei;
import org.terasology.notifications.model.Notification;
import org.terasology.notifications.model.TimedNotification;
import org.terasology.nui.Canvas;
import org.terasology.nui.UITextureRegion;
import org.terasology.nui.itemRendering.AbstractItemRenderer;
import org.terasology.nui.util.RectUtility;

import java.util.Optional;

/**
 * A customized renderer for {@link Notification}, based on
 * {@link org.terasology.nui.itemRendering.StringTextIconRenderer
 * StringTextIconRenderer}.
 * <p>
 * This will render a notification message with a fixed layout and size.
 * <p>
 * TODO: allow to adjust how notifications are rendered my modules.
 */
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
        UITextureRegion texture =
                Optional.ofNullable(notification.getContent().getIconUri())
                        .flatMap(iconUri -> Assets.getTextureRegion(notification.getContent().getIconUri()))
                        .orElse(null);
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

    /**
     * Compute the alpha value for the given {@link TimedNotification} based on the current time to fade it in or out.
     *
     * @param notification the notification with timing/expiration information
     * @return the alpha value in range [0...1]
     */
    private float computeAlpha(TimedNotification notification) {
        long currentTime = time.getGameTimeInMs();

        long timeSinceAdded = currentTime - notification.getAddedAt();
        if (timeSinceAdded < notification.getFadeInTime()) {
            return timeSinceAdded / (float) notification.getFadeInTime();
        }

        if (notification.expires()) {
            long timeUntilExpires = notification.getExpiresAt() - currentTime;
            if (timeUntilExpires < 0) {
                return 0f;
            }
            if (timeUntilExpires < notification.getFadeOutTime()) {
                return timeUntilExpires / (float) notification.getFadeOutTime();
            }
        }
        return 1f;
    }

    @Override
    public Vector2i getPreferredSize(TimedNotification notification, Canvas canvas) {
        return new Vector2i(300, 80);
    }

}
