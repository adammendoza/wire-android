/**
 * Wire
 * Copyright (C) 2016 Wire Swiss GmbH
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.waz.zclient.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.view.Surface;
import android.view.WindowManager;

public enum SquareOrientation {
    NONE(0, 90, ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED),
    PORTRAIT_STRAIGHT(0, 90, ActivityInfo.SCREEN_ORIENTATION_PORTRAIT),
    PORTRAIT_UPSIDE_DOWN(180, 270, ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT),
    LANDSCAPE_LEFT(270, 0, ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE),
    LANDSCAPE_RIGHT(90, 180, ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);

    public final int displayOrientation;
    public final int cameraDisplayOrientation;
    public final int activityOrientation;

    SquareOrientation(int displayOrientation, int cameraDisplayOrientation, int activityOrientation) {
        this.displayOrientation = displayOrientation;
        this.cameraDisplayOrientation = cameraDisplayOrientation;
        this.activityOrientation = activityOrientation;
    }

    public static SquareOrientation getOrientation(Activity activity) {
        switch (activity.getWindowManager().getDefaultDisplay().getRotation()) {
            case Surface.ROTATION_0:
                return getOrientation(0, activity);
            case Surface.ROTATION_90:
                return getOrientation(270, activity);
            case Surface.ROTATION_180:
                return getOrientation(180, activity);
            case Surface.ROTATION_270:
                return getOrientation(90, activity);
        }
        return PORTRAIT_STRAIGHT;
    }

    public static SquareOrientation getOrientation(int orientation, Context context) {
        boolean landscapeOrientation;
        WindowManager windowManager =  (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Configuration config = context.getResources().getConfiguration();
        int defaultRotation = windowManager.getDefaultDisplay().getRotation();

        if (((defaultRotation == Surface.ROTATION_0 || defaultRotation == Surface.ROTATION_180) && config.orientation == Configuration.ORIENTATION_LANDSCAPE) ||
                ((defaultRotation == Surface.ROTATION_90 || defaultRotation == Surface.ROTATION_270) && config.orientation == Configuration.ORIENTATION_PORTRAIT)) {
            landscapeOrientation = true;
        } else {
            landscapeOrientation = false;
        }

        // if default is landscape then adjust orientation value to match regular
        // devices, this only happens on tablets with cameras on the long side
        if (landscapeOrientation && orientation < 90) {
            orientation = 360 - (90 - orientation);
        } else if (landscapeOrientation) {
            orientation -= 90;
        }

        if (orientation > 45 && orientation <= 135) {
            return LANDSCAPE_RIGHT;
        }

        if (orientation > 135 && orientation <= 225) {
            return PORTRAIT_UPSIDE_DOWN;
        }

        if (orientation > 225 && orientation <= 315) {
            return LANDSCAPE_LEFT;
        }

        return PORTRAIT_STRAIGHT;
    }

}
