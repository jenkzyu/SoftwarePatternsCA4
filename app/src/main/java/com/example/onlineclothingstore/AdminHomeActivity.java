package com.example.onlineclothingstore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.onlineclothingstore.Fragments.CustomerFragment;
import com.example.onlineclothingstore.Fragments.HomeFragment;
import com.example.onlineclothingstore.Fragments.PurchaseFragment;
import com.example.onlineclothingstore.Fragments.StockFragment;
import com.example.onlineclothingstore.databinding.ActivityAdminHomeBinding;
import com.google.android.material.navigation.NavigationBarView;

public class AdminHomeActivity extends AppCompatActivity {
    private ActivityAdminHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.adminFrameContainer, new HomeFragment()).commit();

        binding.adminBottomNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        fragment = new HomeFragment();
                        break;
                    case R.id.nav_customers:
                        fragment = new CustomerFragment();
                        break;
                    case R.id.nav_stock:
                        fragment = new StockFragment();
                        break;
                    case R.id.nav_purchase:
                        fragment = new PurchaseFragment();
                        break;
                }
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.adminFrameContainer, fragment).commit();
                return true;
            }
        });
    }
}