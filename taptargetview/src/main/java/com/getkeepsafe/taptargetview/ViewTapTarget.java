/**
 * Copyright 2016 Keepsafe Software, Inc.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.getkeepsafe.taptargetview;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ViewTapTarget extends TapTarget {
    private final View view;

    public ViewTapTarget(@NonNull View view, CharSequence title, @Nullable CharSequence description) {
        super(title, description);

        if (view == null)
            throw new NullPointerException("View cannot be null");

        this.view = view;
    }

    public ViewTapTarget(@NonNull View parentView, @IdRes int viewId, CharSequence title, @Nullable CharSequence description) {
        super(title, description);

        if (parentView == null)
            throw new NullPointerException("Parent view cannot be null");

        View view = parentView.findViewById(viewId);
        if (view == null)
            throw new IllegalArgumentException("View not found");

        this.view = view;
    }

    public ViewTapTarget(@NonNull Activity activity, @IdRes int viewId, CharSequence title, @Nullable CharSequence description) {
        super(title, description);

        if (activity == null)
            throw new NullPointerException("Activity cannot be null");

        View view = activity.findViewById(viewId);
        if (view == null)
            throw new IllegalArgumentException("View not found");

        this.view = view;
    }

    public ViewTapTarget(@NonNull View parentView, @IdRes int[] viewIds, CharSequence title, @Nullable CharSequence description) {
        super(title, description);

        if (parentView == null)
            throw new NullPointerException("Parent view cannot be null");

        View view = null;
        if (viewIds != null) {
            for (int viewId : viewIds) {
                if (viewId == View.NO_ID)
                    continue;

                try {
                    view = parentView.findViewById(viewId);
                } catch (Exception e) {
                    view = null;
                }

                if (view != null)
                    break;
            }
        }
        if (view == null)
            throw new IllegalArgumentException("View not found");

        this.view = view;
    }

    public ViewTapTarget(@NonNull Activity activity, @IdRes int[] viewIds, CharSequence title, @Nullable CharSequence description) {
        super(title, description);

        if (activity == null)
            throw new NullPointerException("Activity cannot be null");

        View view = null;
        if (viewIds != null) {
            for (int viewId : viewIds) {
                if (viewId == View.NO_ID)
                    continue;

                try {
                    view = activity.findViewById(viewId);
                } catch (Exception e) {
                    view = null;
                }

                if (view != null)
                    break;
            }
        }
        if (view == null)
            throw new IllegalArgumentException("View not found");

        this.view = view;
    }

    @Override
    public void onReady(final Runnable runnable) {
        ViewUtil.onLaidOut(view, new Runnable() {
            @Override
            public void run() {
                // Cache bounds
                final int[] location = new int[2];
                view.getLocationOnScreen(location);
                bounds = new Rect(location[0],
                        location[1],
                        location[0] + view.getWidth(),
                        location[1] + view.getHeight());

                if (icon == null && view.getWidth() > 0 && view.getHeight() > 0) {
                    final Bitmap viewBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
                    final Canvas canvas = new Canvas(viewBitmap);
                    view.draw(canvas);
                    icon = new BitmapDrawable(view.getContext().getResources(), viewBitmap);
                    icon.setBounds(0, 0, icon.getIntrinsicWidth(), icon.getIntrinsicHeight());
                }

                runnable.run();
            }
        });
    }
}
