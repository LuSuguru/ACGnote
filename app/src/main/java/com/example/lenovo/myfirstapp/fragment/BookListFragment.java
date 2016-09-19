package com.example.lenovo.myfirstapp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.lenovo.myfirstapp.R;
import com.example.lenovo.myfirstapp.activity.BookContentActivity;
import com.example.lenovo.myfirstapp.adapter.BookAdapter;
import com.example.lenovo.myfirstapp.inter.CallBacks;
import com.example.lenovo.myfirstapp.model.Book;
import com.example.lenovo.myfirstapp.model.BookLab;
import com.example.lenovo.myfirstapp.model.UseDate;
import com.example.lenovo.myfirstapp.service.AcgService;
import com.example.lenovo.myfirstapp.util.BookLabCursorLoader;
import com.example.lenovo.myfirstapp.util.Helper;
import com.example.lenovo.myfirstapp.view.DateChooseView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;



public class BookListFragment extends VisibleFragment implements LoaderManager.LoaderCallbacks<BookLab> {
    private Calendar mCalendar;
    private GridView mGridview;
    private DateChooseView mDateChooseView;

    private int initYear;
    private int initMonth;
    private int initDay;

    private float mAllMoey;

    private boolean isInit = true;

    private Helper mHelper;
    private Date mDate;
    private UseDate mUseDate;
    private ArrayList<Book> mBooks;
    private BookAdapter mAdapter;
    private CallBacks callBacks;
    private AcgService mService;


    private static final int REQUEST_BOOK_LIST_DATE = 432;
    public static final String DIALOGLIST_BOOK_DATE = "listbookdate";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        callBacks = (CallBacks) getActivity();

        mCalendar = Calendar.getInstance();
        mDate = new Date();
        mCalendar.setTime(mDate);

        // 初始化日期
        initYear = mCalendar.get(Calendar.YEAR);
        initMonth = mCalendar.get(Calendar.MONTH) + 1;
        initDay = mCalendar.get(Calendar.DAY_OF_MONTH);

        //将日期存储到时间存储器中
        mUseDate = new UseDate();
        mUseDate.setYear(initYear);
        mUseDate.setMonth(initMonth);

        callBacks.onPvcDateselected(mUseDate);

        LoaderManager lm = getLoaderManager();
        lm.initLoader(0, null, this);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getActivity().getMenuInflater().inflate(R.menu.menu_main, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int i = info.position;
        Book book = (Book) mAdapter.getItem(i);

        switch (item.getItemId()) {
            case R.id.menu_item_delete_book :
                BookLab.get(getActivity()).deleteBook(book);
                mAdapter.getFilter().filter(null);
                return true;
        }
        return super.onContextItemSelected(item);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View tv = inflater.inflate(R.layout.item_book_list, container, false);
        mGridview = (GridView) tv.findViewById(R.id.book_gridview);

        mGridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Book c = (Book) mAdapter.getItem(i);
                Intent intent = new Intent(getActivity(), BookContentActivity.class);
                intent.putExtra(BookContentFragment.EXTRA_BOOK_CONTENT_ID, c.getmId());
                startActivity(intent);
            }
        });

        //设置列表视图的选择模式
        registerForContextMenu(mGridview);

        mDateChooseView = (DateChooseView) tv.findViewById(R.id.id_bookdatechoose);
        mDateChooseView.setText(initYear, initMonth);
        mDateChooseView.setDateChangeListener(new DateChooseView.OnDateChangeListener() {
            @Override
            public void onDateChange() {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                DatePickerFragment dialog = DatePickerFragment.newInstance(mUseDate);
                dialog.setTargetFragment(BookListFragment.this, REQUEST_BOOK_LIST_DATE);
                dialog.show(fm, DIALOGLIST_BOOK_DATE);
            }
        });
        return tv;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isInit == true) {           // 第一次加载不用筛选
            isInit = false;
            return;
        } else {
            mAdapter.updateDate(mUseDate);
            mAdapter.getFilter().filter(null);
        }
    }

    /**
     *  在List不可见时计算总价格，并传递给service
     */
    @Override
    public void onPause() {
        super.onPause();

        mHelper = new Helper();
        mAllMoey = mHelper.getAllMoney(mBooks, mUseDate, Helper.BOOK);
        Intent i = new Intent();
        i.putExtra(AcgService.EXTRA_BOOKSERVICE_ID, mAllMoey);
        mService.setBookIntent(i);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != DatePickerFragment.BOOK_LIST_REQUEST) return;

        if (requestCode == REQUEST_BOOK_LIST_DATE) {
            UseDate date = (UseDate) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            callBacks.onPvcDateselected(date);
            int updateYear = date.getYear();
            int updateMonth = date.getMonth();
            mDateChooseView.setText(updateYear, updateMonth);
            mAdapter.updateDate(date);
            mAdapter.getFilter().filter(null);
        }
    }

    @Override
    public Loader<BookLab> onCreateLoader(int id, Bundle args) {
        return new BookLabCursorLoader(getActivity());
    }

    @Override
    public void onLoadFinished(Loader<BookLab> loader, BookLab data) {
        mBooks = data.getBooks();
        mAdapter = new BookAdapter(getActivity(), mBooks, mUseDate, mDateChooseView.getMallMoney());
        mGridview.setAdapter(mAdapter);

        mHelper = new Helper();
        mAllMoey = mHelper.getAllMoney(mBooks, mUseDate, Helper.BOOK);
        Intent i = new Intent();
        i.putExtra(AcgService.EXTRA_BOOKSERVICE_ID, mAllMoey);
        mService = new AcgService();
        AcgService.setServiceAlarm(getActivity(), true);
        mService.setBookIntent(i);
    }

    @Override
    public void onLoaderReset(Loader<BookLab> loader) {
    }

}
