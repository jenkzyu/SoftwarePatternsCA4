package com.example.onlineclothingstore;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.onlineclothingstore.Constants.Constants;
import com.example.onlineclothingstore.EventBus.CategoryClick;
import com.example.onlineclothingstore.EventBus.ToastEvent;
import com.example.onlineclothingstore.databinding.ActivityAdminMainBinding;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class AdminHomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityAdminMainBinding binding;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private NavController navController;
    private int menuClick= -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAdminMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarAdminMain.toolbar);

        drawer = binding.drawerLayout;
        navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_purchase, R.id.nav_customers, R.id.nav_stockList)
                .setOpenableLayout(drawer)
                .build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_admin_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.bringToFront();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_admin_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        binding.drawerLayout.closeDrawers();
        switch (item.getItemId()) {
            case R.id.nav_home:
                if (item.getItemId() != menuClick)
                    navController.navigate(R.id.nav_home);
                break;
            case R.id.nav_customers:
                if (item.getItemId() != menuClick)
                    navController.navigate(R.id.nav_customers);
                break;
            case R.id.nav_purchase:
                if (item.getItemId() != menuClick)
                    navController.navigate(R.id.nav_purchase);
                break;
            case R.id.nav_logout:
                logout();
                break;
        }
        menuClick = item.getItemId();
        return true;
    }
    /**
     * Command Pattern using Greenrobot's EventBus
     * An Event is a command-style object that’s triggered by user input, server data or pretty much anything else in the app.
     * **/
    //Toast Event updates
    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onToastEvent(ToastEvent event) {
        if (event.getAction() == Constants.ACTION.CREATE)
        {
            Toast.makeText(this, "Created successfully!", Toast.LENGTH_SHORT).show();
        } else if (event.getAction() == Constants.ACTION.UPDATE)
        {
            Toast.makeText(this, "Updated successfully!", Toast.LENGTH_SHORT).show();
        } else
        {
            Toast.makeText(this, "Deleted successfully!", Toast.LENGTH_SHORT).show();
        }
        //EventBus.getDefault().postSticky(new ChangeMenuClick(event.isFromFoodList()));

    }

    //EventBus
    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().removeAllStickyEvents(); // fix eventbus always called after onActivityResult
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    //recieving event from CategoryAdapter
    /**
     * **/
    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onCategorySelected(CategoryClick categoryClick) {
        if (categoryClick.isSuccess()) {
            if (menuClick != R.id.nav_stockList) {
                navController.navigate(R.id.nav_stockList);
                menuClick = R.id.nav_stockList;
            }
        }
    }

    private void logout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Logout").setMessage("Are you sure?")
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(AdminHomeActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();

            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }


}