package com.il.lexicon.ui.fragment;

import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import com.il.lexicon.R;
import com.il.lexicon.ui.custom.component.BaseFragment;

public class AddWordFragment extends BaseFragment {
    public AddWordFragment() {
        super(R.layout.fragment_add_word);
    }

    public NavController navController;

    @Override
    protected void init() {
//        NavHostFragment navHostFragment = (NavHostFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.cv_nav);
//        navController = navHostFragment.getNavController();
    }
}