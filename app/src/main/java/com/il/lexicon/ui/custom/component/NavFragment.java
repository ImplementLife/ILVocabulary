package com.il.lexicon.ui.custom.component;

import android.os.Bundle;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

public class NavFragment extends BaseFragment {
    protected NavController navController;
    private String name;
    private boolean hideHead = true;

    public NavFragment() {}
    public NavFragment(int layout) {
        super(layout);
    }
    public NavFragment(int layout, boolean hideHead) {
        super(layout);
        this.hideHead = hideHead;
    }
    public NavFragment(int layout, String name) {
        super(layout);
        this.name = name;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        navController = NavHostFragment.findNavController(this);
    }

    public void navigate(int resId) {
        navController.navigate(resId);
    }

    public void navigate(int resId, Bundle args) {
        navController.navigate(resId, args);
    }

    public boolean navigateUp() {
        return navController.navigateUp();
    }
}
