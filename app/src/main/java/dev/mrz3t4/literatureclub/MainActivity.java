package dev.mrz3t4.literatureclub;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;

import java.io.File;
import java.util.Objects;

import dev.mrz3t4.literatureclub.UI.CollectionsFragment;
import dev.mrz3t4.literatureclub.UI.ExploreFragment;
import dev.mrz3t4.literatureclub.UI.RecentsFragment;
import dev.mrz3t4.literatureclub.UI.SettingsFragment;

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

    Menu myMenu;

    private boolean connected;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        connected = checkConnection();

        if (connected) {
            setContentView(R.layout.activity_main);

            if (savedInstanceState == null) {

               /* fm.beginTransaction().add(R.id.fragment_container, settingsFragment, "4").hide(settingsFragment).commit();
                fm.beginTransaction().add(R.id.fragment_container, exploreFragment, "3").hide(exploreFragment).commit();
                fm.beginTransaction().add(R.id.fragment_container, collectionsFragment, "2").hide(collectionsFragment).commit();
                fm.beginTransaction().add(R.id.fragment_container, recentsFragment, "1").commit();
*/
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
        myMenu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_filter:
                Toast.makeText(this, "update clicked", Toast.LENGTH_SHORT).show();
                break;
            case R.id.menu_force_update:

                Intent intent = new Intent("FORCE_RELOAD");
                intent.putExtra("RELOAD", true);
                sendBroadcast(intent);

                break;

            default:
                return super.onOptionsItemSelected(item);

        }
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
                        //fm.beginTransaction().hide(active).setCustomAnimations(R.anim.slide_up, R.anim.alpha).show(recentsFragment).commit();
                        //active = recentsFragment;

                        toolbarTitle.setText(recents);
                        toolbarTitle.startAnimation(animation);

                        myMenu.findItem(R.id.menu_donate).setVisible(false);
                        myMenu.findItem(R.id.menu_filter).setVisible(false);
                        myMenu.findItem(R.id.menu_force_update).setVisible(false);

                        break;
                    case R.id.menu_collections:
                        changeFragment(collectionsFragment, CollectionsFragment.class.getSimpleName());
                        //fm.beginTransaction().hide(active).setCustomAnimations(R.anim.slide_up, R.anim.alpha).show(collectionsFragment).commit();
                        //active = collectionsFragment;

                        toolbarTitle.setText(collections);
                        toolbarTitle.startAnimation(animation);

                        myMenu.findItem(R.id.menu_filter).setVisible(false);
                        myMenu.findItem(R.id.menu_force_update).setVisible(false);
                        myMenu.findItem(R.id.menu_donate).setVisible(false);

                        break;
                    case R.id.menu_explore:
                        changeFragment(exploreFragment, ExploreFragment.class.getSimpleName());
                        //fm.beginTransaction().hide(active).setCustomAnimations(R.anim.slide_up, R.anim.alpha).show(exploreFragment).commit();
                        //active = exploreFragment;
                        toolbarTitle.setText(explore);
                        toolbarTitle.startAnimation(animation);

                        myMenu.findItem(R.id.menu_filter).setVisible(true);
                        myMenu.findItem(R.id.menu_force_update).setVisible(true);
                        myMenu.findItem(R.id.menu_donate).setVisible(false);

                        break;
                    case R.id.menu_settings:
                        changeFragment(settingsFragment, SettingsFragment.class.getSimpleName());
                       // fm.beginTransaction().hide(active).setCustomAnimations(R.anim.slide_up, R.anim.alpha).show(settingsFragment).commit();
                      //  active = settingsFragment;
                        toolbarTitle.setText(settings);
                        toolbarTitle.startAnimation(animation);

                        myMenu.findItem(R.id.menu_filter).setVisible(false);
                        myMenu.findItem(R.id.menu_force_update).setVisible(false);
                        myMenu.findItem(R.id.menu_donate).setVisible(true);

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
                    //.setCustomAnimations(R.anim.slide_up, R.anim.alpha)
                    .add(R.id.fragment_container, fragmentTemp, tagFragmentName);
        } else {
            fragmentTransaction
                    .setCustomAnimations(R.anim.slide_up, R.anim.slide_down)
                    .show(fragmentTemp);
        }

        fragmentTransaction.setPrimaryNavigationFragment(fragmentTemp);
        fragmentTransaction.setReorderingAllowed(true);
        fragmentTransaction.commitNowAllowingStateLoss();

    }
}