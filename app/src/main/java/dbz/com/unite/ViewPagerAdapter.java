package dbz.com.unite;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if (position ==0) {
            return new global();
        }
        else if (position == 1) {
            return new contacts();
        }
        else if (position==2)return new search();
        else if(position==3)return new your_groups();
        else if(position==4)return new search_groups();
        else if(position==5)return new group_create();

      else return new global();
    }

    @Override
    public int getCount() {
        return 6;
    }
}
