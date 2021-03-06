package dev.mrz3t4.literatureclub;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Objects;

import dev.mrz3t4.literatureclub.UI.CollectionsFragment;
import dev.mrz3t4.literatureclub.UI.ExploreFragment;
import dev.mrz3t4.literatureclub.UI.RecentsFragment;
import dev.mrz3t4.literatureclub.UI.SettingsFragment;

import static dev.mrz3t4.literatureclub.Utils.Constants.broadcast;
import static dev.mrz3t4.literatureclub.Utils.Constants.calendar;
import static dev.mrz3t4.literatureclub.Utils.Constants.collections;
import static dev.mrz3t4.literatureclub.Utils.Constants.explore;
import static dev.mrz3t4.literatureclub.Utils.Constants.recents;
import static dev.mrz3t4.literatureclub.Utils.Constants.settings;

public class MainActivity extends AppCompatActivity {

    private final RecentsFragment recentsFragment = new RecentsFragment();
    private final CollectionsFragment collectionsFragment = new CollectionsFragment();
    private final ExploreFragment exploreFragment = new ExploreFragment();
    private final SettingsFragment settingsFragment= new SettingsFragment();

    private BottomNavigationView navigationView;
    private AppBarLayout appBarLayout;
    private ShapeableImageView toolbarImageView;

    private Animation animation;

    private Toolbar toolbar;
    private FloatingActionButton fab;
    private MaterialTextView toolbarTitle;

    private boolean connected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        connected = checkConnection();

        if (connected) {
            setContentView(R.layout.activity_main);

            if (savedInstanceState == null) {
                changeFragment(recentsFragment, RecentsFragment.class.getSimpleName());
            }
            setupBottomNavigationBar();
            setupAppBarLayout();
            setupFAB();
        } else {
            Intent intent = new Intent(MainActivity.this, Downloads.class);
            startActivity(intent);
            setContentView(R.layout.section_downloads);
        }

    }

    private boolean checkConnection(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;
        }
        else {
            connected = false;
        }
        return connected;

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        return true;
    }

    private void setupAppBarLayout() {
        appBarLayout = findViewById(R.id.app_bar_layout);
        toolbar= findViewById(R.id.toolbar);
        toolbarImageView = findViewById(R.id.toolbar_imageview);
        toolbarTitle= findViewById(R.id.toolbar_title);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("");

        toolbarImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigationView.setSelectedItemId(R.id.menu_settings);
            }
        });

    }

    private void setupFAB(){
        fab = findViewById(R.id.fab_search);

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (verticalOffset == 0) {
                    fab.show();
                } else {
                    fab.hide();
                }
            }
        });

    }

    private void setupBottomNavigationBar(){

        animation = AnimationUtils.loadAnimation(this, R.anim.slide_left);

        navigationView = findViewById(R.id.bottom_navigation_View);
        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){

                    case R.id.menu_recents:
                        changeFragment(recentsFragment, RecentsFragment.class.getSimpleName());
                        toolbarTitle.setText(recents);
                        toolbarTitle.startAnimation(animation);
                        break;
                    case R.id.menu_collections:
                        changeFragment(collectionsFragment, CollectionsFragment.class.getSimpleName());
                        toolbarTitle.setText(collections);
                        toolbarTitle.startAnimation(animation);
                        break;
                    case R.id.menu_explore:
                        changeFragment(exploreFragment, ExploreFragment.class.getSimpleName());
                        toolbarTitle.setText(explore);
                        toolbarTitle.startAnimation(animation);
                        break;
                    case R.id.menu_settings:
                        changeFragment(settingsFragment, SettingsFragment.class.getSimpleName());
                        toolbarTitle.setText(settings);
                        toolbarTitle.startAnimation(animation);
                        break;

                }

                return true;
            }
        });

        navigationView.setOnNavigationItemReselectedListener(new BottomNavigationView.OnNavigationItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull MenuItem item) {

            }
        });
    }
    private void changeFragment(Fragment fragment, String tagFragmentName) {

        FragmentManager mFragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();

        Fragment currentFragment = mFragmentManager.getPrimaryNavigationFragment();
        if (currentFragment != null) {
            fragmentTransaction.hide(currentFragment);
        }

        Fragment fragmentTemp = mFragmentManager.findFragmentByTag(tagFragmentName);
        if (fragmentTemp == null) {
            fragmentTemp = fragment;
            fragmentTransaction
                    .setCustomAnimations(R.anim.slide_up, R.anim.alpha)
                    .add(R.id.fragment_container, fragmentTemp, tagFragmentName);
        } else {
            fragmentTransaction.setCustomAnimations(R.anim.slide_up, R.anim.alpha).show(fragmentTemp);
        }

        fragmentTransaction.setPrimaryNavigationFragment(fragmentTemp);
        fragmentTransaction.setReorderingAllowed(true);
        fragmentTransaction.commitNowAllowingStateLoss();
    }
}