package NacXo.ChattingApp.Adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import NacXo.ChattingApp.Fragments.ChatFragment;
import NacXo.ChattingApp.Fragments.ContactFragment;
import NacXo.ChattingApp.Fragments.GroupFragment;

public class TabsAccessorsAdapter extends FragmentPagerAdapter {
    public TabsAccessorsAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position)
        {
            case 0:
                ChatFragment chatFragment=new ChatFragment();
                return chatFragment;
            case 1:
                GroupFragment groupFragment=new GroupFragment();
                return groupFragment;
            case 2:
                ContactFragment contactFragment=new ContactFragment();
                return contactFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position)
        {
            case 0:
                return "Chat";
            case 1:
                return "Group";
            case 2:
                return "Contact";
            default:
                return null;
        }
    }
}
