// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0

package org.terasology.notifications.model;

/**
 * Common constants used in the context of notifications.
 */
public final class Constants {
    /**
     * The default time it takes a notification to be fully visible on the screen in milliseconds (ms).
     */
    public static final int DEFAULT_FADE_IN_TIME = 100;

    /**
     * The default time over which a notification fades out from the screen in milliseconds (ms).
     */
    public static final int DEFAULT_FADE_OUT_TIME = 5000;

    /**
     * Constant to denote that the notification should be expired and removed immediately.
     */
    public static final long IMMEDIATE = 0;

    /**
     * Constant to indicate that the notification should be shown indefinitely.
     */
    public static final long NEVER = -1;

    /**
     * Constant denoting the recommended duration volatile notifications should be visible on screen.
     * <p>
     * Duration as milliseconds (of in-game time).
     */
    public static final long RECOMMENDED_DURATION = 10000;

    private Constants() {
    }
}
