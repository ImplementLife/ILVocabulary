package com.il.vcb.ui.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.il.vcb.R;
import com.il.vcb.data.jpa.provide.AppDatabase;
import com.il.vcb.databinding.ActivityMainBinding;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    public static MainActivity getInstance() {
        return instance;
    }
    @SuppressLint("StaticFieldLeak")
    private static MainActivity instance;

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //hide top bar with app name
        Objects.requireNonNull(getSupportActionBar()).hide();
        instance = this;

        AppDatabase.getInstance(this);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration =
            new AppBarConfiguration.Builder(
                R.id.nav_settings,
                R.id.nav_add_word,
                R.id.nav_learn
            )
            .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
    }

}