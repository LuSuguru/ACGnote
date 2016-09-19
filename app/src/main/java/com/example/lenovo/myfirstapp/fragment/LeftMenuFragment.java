package com.example.lenovo.myfirstapp.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.lenovo.myfirstapp.R;
import com.example.lenovo.myfirstapp.adapter.LeftMenuAdapter;
import com.example.lenovo.myfirstapp.model.MenuItem;


/**
 * Created by lenovo on 2016/8/21.
 */
public class LeftMenuFragment extends ListFragment {

    private static final int SIZE_MENU_ITEM = 3;
    private MenuItem[] mItems = new MenuItem[SIZE_MENU_ITEM];
    private LeftMenuAdapter mAdapter;
    private LayoutInflater mInflater;
    private OnMenuItemSelectedListener mMenuItemSelectedListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mInflater = LayoutInflater.from(getActivity());
        mItems[0] = new MenuItem(getResources().getStringArray(R.array.array_left_menu)[0], false, R.drawable.toy_unchoose, R.drawable.toy_choose, "pvc");
        mItems[1] = new MenuItem(getResources().getStringArray(R.array.array_left_menu)[1], false, R.drawable.book_unchoose, R.drawable.book_choose, "book");
        mItems[2] = new MenuItem(getResources().getStringArray(R.array.array_left_menu)[2], false, R.drawable.keychain_unchoose, R.drawable.keychain_choose, "key");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setListAdapter(mAdapter = new LeftMenuAdapter(getActivity(),mItems));
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        if(mMenuItemSelectedListener != null) {
            mMenuItemSelectedListener.menuItemSelected(((MenuItem)getListAdapter().getItem(position)).text, ((MenuItem)getListAdapter().getItem(position)).tag);
        }
        mAdapter.setmSelected(position);
    }

    public interface OnMenuItemSelectedListener {
        void menuItemSelected(String title, String tag);
    }

    public void setOnMenuItemSelectedListener(OnMenuItemSelectedListener menuItemSelectedListener) {
        this.mMenuItemSelectedListener = menuItemSelectedListener;
    }
}
