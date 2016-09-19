package com.example.lenovo.myfirstapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;

import com.example.lenovo.myfirstapp.R;
import com.example.lenovo.myfirstapp.fragment.BookContentFragment;
import com.example.lenovo.myfirstapp.fragment.ContentFragment;
import com.example.lenovo.myfirstapp.fragment.LeftMenuFragment;
import com.example.lenovo.myfirstapp.fragment.VisibleFragment;
import com.example.lenovo.myfirstapp.inter.CallBacks;
import com.example.lenovo.myfirstapp.model.Book;
import com.example.lenovo.myfirstapp.model.BookLab;
import com.example.lenovo.myfirstapp.model.Pvc;
import com.example.lenovo.myfirstapp.model.PvcLab;
import com.example.lenovo.myfirstapp.model.UseDate;

import java.util.List;

public class MainActivity extends AppCompatActivity implements CallBacks {

    private ActionBarDrawerToggle mActionBarDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private Toolbar mToolbar;
    private LeftMenuFragment mLeftMenuFragment;
    private VisibleFragment mCurrentFragment;
    private ImageButton mAddButton;
    private  UseDate mUseDate;

    private String mTitle;

    private static final String KEY_TITLE = "key_title";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initToolBar();
        initView();
        restoreTitle(savedInstanceState);

        FragmentManager fm = getSupportFragmentManager();
        mCurrentFragment = (VisibleFragment) fm.findFragmentByTag(mTitle);

        if (mCurrentFragment == null) {
            mCurrentFragment = VisibleFragment.newInstance(mTitle);
            fm.beginTransaction().add(R.id.id_content, mCurrentFragment, mTitle).commit();
        }

        mLeftMenuFragment = (LeftMenuFragment) fm.findFragmentById(R.id.id_left_menu);

        if (mLeftMenuFragment == null) {
            mLeftMenuFragment = new LeftMenuFragment();
            fm.beginTransaction().add(R.id.id_left_menu, mLeftMenuFragment).commit();
        }

        List<Fragment> fragments = fm.getFragments();
        if (fragments != null) {
            for (Fragment fragment : fragments) {
                if (fragment == mCurrentFragment || fragment == mLeftMenuFragment) continue;
                fm.beginTransaction().hide(fragment).commit();
            }
        }

        mLeftMenuFragment.setOnMenuItemSelectedListener(new LeftMenuFragment.OnMenuItemSelectedListener() {
            @Override
            public void menuItemSelected(String title, String tag) {
                FragmentManager fm = getSupportFragmentManager();
                VisibleFragment fragment = (VisibleFragment) fm.findFragmentByTag(title);

                if (fragment == mCurrentFragment) {
                    mDrawerLayout.closeDrawer(Gravity.LEFT);
                    return;
                }

                FragmentTransaction ft = fm.beginTransaction();
                ft.hide(mCurrentFragment);
                if (fragment == null) {
                    fragment = VisibleFragment.newInstance(title);
                    ft.add(R.id.id_content, fragment, title);
                } else {
                    ft.show(fragment);
                }

                ft.commit();

                mCurrentFragment = fragment;
                mTitle = title;
                mToolbar.setTitle(title);
                mDrawerLayout.closeDrawer(Gravity.LEFT);
            }
        });
    }

    private void restoreTitle(Bundle savedInstanceState) {
        if (savedInstanceState != null)
            mTitle = savedInstanceState.getString(KEY_TITLE);

        if (TextUtils.isEmpty(mTitle)) {
            mTitle = getResources().getStringArray(
                    R.array.array_left_menu)[0];
        }

        mToolbar.setTitle(mTitle);
    }


    private void initView() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.id_drawerlayout);
        mActionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.open, R.string.close);
        mActionBarDrawerToggle.syncState();
        mDrawerLayout.addDrawerListener(mActionBarDrawerToggle);
        mAddButton = (ImageButton) findViewById(R.id.add_button);
        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UseDate date = new UseDate();
                date.setYear(mUseDate.getYear());
                date.setMonth(mUseDate.getMonth());
                switch (mTitle) {
                    case "预定手办":
                        Pvc pvc = new Pvc();
                        PvcLab.get(MainActivity.this).addPvc(pvc);
                        pvc.setmDate(date);
                        Intent i = new Intent(MainActivity.this, ContentActivity.class);
                        i.putExtra(ContentFragment.EXTRA_CONTENT_ID, pvc.getmId());
                        startActivity(i);
                        break;
                    
                    case "预定轻小说" :
                        Book book = new Book();
                        BookLab.get(MainActivity.this).addBook(book);
                        book.setmDate(date);
                        Intent i1 = new Intent(MainActivity.this, BookContentActivity.class);
                        i1.putExtra(BookContentFragment.EXTRA_BOOK_CONTENT_ID, book.getmId());
                        startActivity(i1);
                        break;
                }
            }
        });

    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_TITLE, mTitle);
    }

    private void initToolBar() {
        mToolbar = (Toolbar) findViewById(R.id.id_toolbat);
        mToolbar.setTitle(getResources().getStringArray(R.array.array_left_menu)[0]);
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.menu_tubiao);
    }

    @Override
    public void onPvcDateselected(UseDate useDate) {
        mUseDate = useDate;
    }
}



