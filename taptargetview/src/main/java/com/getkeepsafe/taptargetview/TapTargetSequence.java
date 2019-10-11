package com.getkeepsafe.taptargetview;

import android.app.Activity;
import android.app.Dialog;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class TapTargetSequence extends TargetSequence {
    private final @Nullable TapTargetViewProvider provider;
    private final @Nullable Activity activity;
    private final @Nullable Dialog dialog;

    public TapTargetSequence(TapTargetViewProvider provider) {
        if (provider == null)
            throw new IllegalArgumentException("No provider attached");

        this.provider = provider;
        this.activity = null;
        this.dialog = null;
    }

    public TapTargetSequence(Activity activity) {
        if (activity == null)
            throw new IllegalArgumentException("No activity attached");

        this.provider = null;
        this.activity = activity;
        this.dialog = null;
    }

    public TapTargetSequence(Dialog dialog) {
        if (dialog == null)
            throw new IllegalArgumentException("No dialog attached");

        this.provider = null;
        this.activity = null;
        this.dialog = dialog;
    }

    @NonNull
    @Override
    protected TapTargetView getNextView(@NonNull TapTarget tapTarget, @NonNull TapTargetView.Listener listener) {
        if (provider != null) {
            TapTargetView tapTargetView = provider.provide();
            if (tapTargetView == null)
                throw new NullPointerException("No view returned");
            return tapTargetView;
        } else if (activity != null) {
            return TapTargetView.showFor(activity, tapTarget, listener);
        } else if (dialog != null) {
            return TapTargetView.showFor(dialog, tapTarget, listener);
        } else {
            throw new IllegalStateException();
        }
    }

    public interface TapTargetViewProvider {
        @NonNull TapTargetView provide();
    }
}
