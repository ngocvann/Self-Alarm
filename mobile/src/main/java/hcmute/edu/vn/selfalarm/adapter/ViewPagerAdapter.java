package hcmute.edu.vn.selfalarm.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import hcmute.edu.vn.selfalarm.fragments.CallLogFragment;
import hcmute.edu.vn.selfalarm.fragments.SMSListFragment;

public class ViewPagerAdapter extends FragmentStateAdapter {
    public ViewPagerAdapter(FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new SMSListFragment();
            case 1:
                return new CallLogFragment();
            default:
                return new SMSListFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}