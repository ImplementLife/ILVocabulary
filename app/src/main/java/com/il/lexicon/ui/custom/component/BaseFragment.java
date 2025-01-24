package com.il.lexicon.ui.custom.component;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public abstract class BaseFragment extends Fragment {
    private int layout;
    protected View root;

    public BaseFragment() {}
    public BaseFragment(int layout) {
        this.layout = layout;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = createView(layout, inflater, container);
        init();
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle arguments = getArguments();
        if (arguments == null) arguments = new Bundle();
        argumentProcessing(arguments);
    }


    protected void runAsync(Runnable runnable) {
        CompletableFuture.runAsync(() -> {
            try {
                runnable.run();
            } catch (Throwable t) {
                Log.e("runAsync", "", t);
            }
        });
    }

    public View createView(int layout, LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(layout, container, false);
        root = view;
        return view;
    }

    public void updateView(Runnable runnable) {
        getView().post(runnable);
    }

    protected <T extends View> T findViewById(int id) {
        return getView().findViewById(id);
    }

    protected void init() {}
    protected void argumentProcessing(Bundle arguments) {}

    /**
     * @return root view
     * @see #createView(int, LayoutInflater, ViewGroup)
     */
    @Override
    public View getView() {
        View view = super.getView();
        if (view == null) view = root;
        return view;
    }

    public boolean post(Runnable action) {
        boolean result = root.post(action);
        if (!result) Log.e("post", "task doesn't performed complete");
        return result;
    }

    public boolean postDelayed(Runnable action, long delayMillis) {
        boolean result = root.postDelayed(action, delayMillis);
        if (!result) Log.e("postDelayed", "task doesn't performed complete");
        return result;
    }

    protected boolean isPermissionGranted(String permission) {
        return checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
    }

    protected int checkSelfPermission(String permission) {
        return ContextCompat.checkSelfPermission(Objects.requireNonNull(getContext()), permission);
    }

    protected void requestPermissions(String permission) {
        ActivityCompat.requestPermissions(Objects.requireNonNull(getActivity()), new String[]{permission}, 1);
    }

    protected boolean checkAndRequestPermission(String permission) {
        if (!isPermissionGranted(permission)) {
            requestPermissions(permission);
        }
        return isPermissionGranted(permission);
    }
}
