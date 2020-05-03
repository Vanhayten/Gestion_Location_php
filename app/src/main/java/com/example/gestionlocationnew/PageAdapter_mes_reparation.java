package com.example.gestionlocationnew;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class PageAdapter_mes_reparation extends FragmentPagerAdapter {

    private int numoftabs;
    public PageAdapter_mes_reparation(@NonNull FragmentManager fm,int numoftabs) {
        super(fm);
        this.numoftabs = numoftabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new nouvel_reparation();
            case 1:
                return new consulter_reparation();
            default:
                return null;
        }
    }
    @Override
    public int getCount() {
        return numoftabs;
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }
}
