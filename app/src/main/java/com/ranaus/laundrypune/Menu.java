package com.ranaus.laundrypune;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Menu extends AppCompatActivity {

    private BottomNavigationView homeNav,NotifNav,UserNav;
    private HomeFragment homeFragment;
    private NotificationFragment notificationFragment;
    private UserFragment userFragment;
    private OderFragment oderFragment;
    private FrameLayout homeFrame,NotifFragment,UserFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        homeNav = (BottomNavigationView) findViewById(R.id.menu_Main);
        homeFrame = (FrameLayout) findViewById(R.id.home_fragment_MenuAct);

        UserFragment = findViewById(R.id.user_fragment);

        homeFragment = new HomeFragment();
        notificationFragment = new NotificationFragment();
        userFragment = new UserFragment();
        oderFragment = new OderFragment();

        setFragment(homeFragment);

        homeNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId())
                {
                    case R.id.notification:
                        setFragment(notificationFragment);
                        return true;

                    case R.id.home:
                        setFragment(homeFragment);
                        return true;

                    case R.id.order:
                        setFragment(oderFragment);
                        return  true;

                        default:
                            return false;
                }
            }
        });
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.home_fragment_MenuAct,fragment);
        fragmentTransaction.commit();
    }
}
