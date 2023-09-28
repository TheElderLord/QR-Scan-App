package com.example.finalcut.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class ViewPageAdapter extends FragmentPagerAdapter {
   private final List<Fragment> fragmentList = new ArrayList<>();
   private final List<String> title = new ArrayList<>();


   public ViewPageAdapter(FragmentManager fragmentManager){
       super(fragmentManager);
   }
   public void addTitle(String str){
       title.add(str);
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
   public void addFragment(Fragment fragment){
       fragmentList.add(fragment);
   }
   @Override
   public CharSequence getPageTitle(int position){
       return title.get(position);
   }
}
