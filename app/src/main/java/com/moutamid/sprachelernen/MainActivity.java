package com.moutamid.sprachelernen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.core.view.MenuItemCompat;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.fxn.stash.Stash;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.navigation.NavigationView;
import com.moutamid.sprachelernen.activities.LanguageSelectionActivity;
import com.moutamid.sprachelernen.activities.ProfileEditActivity;
import com.moutamid.sprachelernen.activities.SettingsActivity;
import com.moutamid.sprachelernen.databinding.ActivityMainBinding;
import com.moutamid.sprachelernen.fragments.HomeFragment;
import com.moutamid.sprachelernen.models.UserModel;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        Constants.checkApp(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,
                binding.drawLayout, binding.toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        binding.navView.setNavigationItemSelectedListener(this);
        binding.drawLayout.addDrawerListener(toggle);
        toggle.syncState();

        binding.navView.setItemIconTintList(null);
        MenuItem logoutItem = binding.navView.getMenu().findItem(R.id.nav_logout);
        SpannableString spannableString = new SpannableString("Logout");
        spannableString.setSpan(new ForegroundColorSpan(ContextCompat.getColor(this, R.color.red)), 0, spannableString.length(), 0);
        logoutItem.setTitle(spannableString);


        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();

    }

    private void updateNavHead(NavigationView navView) {
        View Header = navView.getHeaderView(0);
        TextView headerName = Header.findViewById(R.id.tv_nav_Name);
        TextView headerEmail = Header.findViewById(R.id.tv_nav_yourEmail);

        Constants.databaseReference().child(Constants.USER).child(Constants.auth().getCurrentUser().getUid())
                .get().addOnSuccessListener(dataSnapshot -> {
                   if (dataSnapshot.exists()){
                       UserModel userModel = dataSnapshot.getValue(UserModel.class);
                       Stash.put(Constants.STASH_USER, userModel);
                       headerName.setText(userModel.getName());
                       headerEmail.setText(userModel.getEmail());
                   }
                });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.profile_menu, menu);

        MenuItem menuProfile = menu.findItem(R.id.profile_toolbar);
        View view = MenuItemCompat.getActionView(menuProfile);

        CircleImageView profileImage = view.findViewById(R.id.toolbar_profile_image);

        profileImage.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, ProfileEditActivity.class));
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        if (binding.drawLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.nav_logout) {
            new MaterialAlertDialogBuilder(this)
                    .setCancelable(true)
                    .setTitle("Logout")
                    .setMessage("Are you sure you want to logout?")
                    .setPositiveButton("Yes", ((dialog, which) -> {
                        dialog.dismiss();
                        Constants.showDialog();
                        new Handler().postDelayed(() -> {
                            Constants.auth().signOut();
                            startActivity(new Intent(MainActivity.this, SplashScreenActivity.class));
                            finish();
                        }, 2000);
                    }))
                    .setNegativeButton("No", ((dialog, which) -> {
                        dialog.dismiss();
                    }))
                    .show();
        } else if (item.getItemId() == R.id.nav_language) {
            startActivity(new Intent(MainActivity.this, LanguageSelectionActivity.class).putExtra(Constants.SHOW_TOOLBAR, true));
            finish();
        } else if (item.getItemId() == R.id.nav_setting) {
            startActivity(new Intent(MainActivity.this, SettingsActivity.class));
            finish();
        } else if (item.getItemId() == R.id.nav_privacy) {

        }

        binding.drawLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Constants.initDialog(this);
        updateNavHead(binding.navView);
    }
}