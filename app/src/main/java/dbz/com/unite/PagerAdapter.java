package dbz.com.unite;

import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
/**
 * Created by kcarj on 28-12-2016.
 */

public class PagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;
    public PagerAdapter(FragmentManager fm,int NumOfTabs) {
        super(fm);
        this.mNumOfTabs=NumOfTabs;
    }
    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                global tab1 = new global();
                return tab1;
            case 1:
                contacts tab2 = new contacts();
                return tab2;
            case 2:
                search tab3 = new search();
                return tab3;
            case 3:
                group_create tab4=new group_create();
                return tab4;
            default:
                return null;
        }
    }


    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
