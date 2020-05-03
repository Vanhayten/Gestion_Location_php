package com.example.gestionlocationnew;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

public class reparation extends AppCompatActivity {


    private TabLayout tablayout;
    private ViewPager viewPager;
    private TabItem tabItem,tabItem1;
    public PageAdapter_mes_reparation pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reparation);
        tablayout =(TabLayout)findViewById(R.id.tablayout2);
        tabItem = (TabItem)findViewById(R.id.ajouter_reparation);
        tabItem1 = (TabItem)findViewById(R.id.affiche_reparation);
        viewPager = (ViewPager)findViewById(R.id.viewpaper2);

        pagerAdapter = new PageAdapter_mes_reparation(getSupportFragmentManager(),tablayout.getTabCount());
        viewPager.setAdapter(pagerAdapter);



        tablayout.setOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                if(tab.getPosition() == 0) {
                    pagerAdapter.notifyDataSetChanged();
                }
                else if(tab.getPosition() == 1) {
                    pagerAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tablayout));

    }
}
