package dev.mrz3t4.literatureclub.UI;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import java.util.ArrayList;

import dev.mrz3t4.literatureclub.ViewPager.BroadcastFragment;
import dev.mrz3t4.literatureclub.ViewPager.SeasonFragment;

public class AnimeViewPager {

    public void setupViewPager(ViewPager2 viewPager,Fragment fragment1, Fragment fragment2, FragmentManager fm, Lifecycle lifecycle){
        AnimeViewPagerAdapter adapter = new AnimeViewPagerAdapter(fm, lifecycle);

        adapter.addFragment(fragment1);
        adapter.addFragment(fragment2);

        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(3);

    }

    public class AnimeViewPagerAdapter extends FragmentStateAdapter {

        ArrayList<Fragment> fragments = new ArrayList<>();

        AnimeViewPagerAdapter(FragmentManager fm, Lifecycle lifecycle) {
            super(fm, lifecycle);
        }

        void addFragment(Fragment fragment) {
            fragments.add(fragment);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            return fragments.get(position);
        }

        @Override
        public int getItemCount() {
            return fragments.size();
        }
    }
}
