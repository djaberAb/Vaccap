package com.example.vaccap.ui.appointments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.vaccap.databinding.ActivityAppointmentsBinding;
import com.example.vaccap.ui.DrawerBaseActivity;
import com.google.android.material.tabs.TabLayout;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.ArrayList;
import java.util.List;

public class AppointmentsActivity extends DrawerBaseActivity {
    ActivityAppointmentsBinding activityAppointmentsBinding;
    TabLayout tabLayout;
    ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityAppointmentsBinding = ActivityAppointmentsBinding.inflate(getLayoutInflater());
        setContentView(activityAppointmentsBinding.getRoot());
        allocateActivityTitles("Appointments");

        // Get references to the TabLayout and ViewPager
        tabLayout = activityAppointmentsBinding.tabLayout;
        viewPager = activityAppointmentsBinding.viewPager;

        // Create a PagerAdapter that returns a Fragment for each tab
        PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        pagerAdapter.addFragment(new PendingFragment(), "Pending");
        pagerAdapter.addFragment(new AcceptedFragment(), "Accepted");
        pagerAdapter.addFragment(new DeclinedFragment(), "Declined");

        // Set the adapter on the ViewPager
        viewPager.setAdapter(pagerAdapter);

        // Set up the TabLayout with the ViewPager
        tabLayout.setupWithViewPager(viewPager);
    }

    private static class PagerAdapter extends FragmentPagerAdapter {

        private final List<Fragment> fragmentList = new ArrayList<>();
        private final List<String> fragmentTitleList = new ArrayList<>();

        public PagerAdapter(@NonNull FragmentManager fragmentManager, int behavior) {
            super(fragmentManager, behavior);
        }

        public void addFragment(Fragment fragment, String title) {
            fragmentList.add(fragment);
            fragmentTitleList.add(title);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentTitleList.get(position);
        }
    }
}