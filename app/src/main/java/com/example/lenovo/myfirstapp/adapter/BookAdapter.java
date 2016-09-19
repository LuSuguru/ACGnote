package com.example.lenovo.myfirstapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lenovo.myfirstapp.R;
import com.example.lenovo.myfirstapp.model.Book;
import com.example.lenovo.myfirstapp.model.UseDate;
import com.example.lenovo.myfirstapp.util.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 2016/9/14.
 */
public class BookAdapter extends BaseAdapter implements Filterable {
    private LayoutInflater mInflater;
    private ArrayList<Book> mInitBooks,mBooks;
    private TextView AllMoneyText;

    private MyFilter mFilter;
    private UseDate mDate;

    private float allFinishMoney ;

    /**
     * 初始化的日期
     */
    private int mYear;
    private int mMonth;

    public BookAdapter(Context context, List<Book> object, UseDate date, TextView text) {
        mInflater = LayoutInflater.from(context);

        mInitBooks = (ArrayList<Book>) object;
        mBooks = new ArrayList<Book>();

        AllMoneyText = text;

        updateDate(date);

        /**
         * 根据初始化日期筛选相应的项
         */
        for(Book book : mInitBooks) {
            int year = getYear(book);
            int month = getMonth(book);

            if(year == mYear && month == mMonth) {
                mBooks.add(book);

                if(book.ismIspay() == false) {
                    allFinishMoney = allFinishMoney + book.getmFinalMoney();
                }
            }
        }
        setAllMoney(AllMoneyText);
    }

    @Override
    public int getCount() {
        return mBooks.size();
    }

    @Override
    public Object getItem(int i) {
        return mBooks.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Book book = (Book) getItem(i);
       BookViewHolder viewHolder;

        if(view == null) {
            view = mInflater.inflate(R.layout.item_book_gridview, viewGroup, false);

            viewHolder = new BookViewHolder();
            viewHolder.image = (ImageView) view.findViewById(R.id.id_book_image);
            viewHolder.bookName = (TextView) view.findViewById(R.id.id_book_name);
            view.setTag(viewHolder);
        } else {
            viewHolder = (BookViewHolder) view.getTag();
        }
        viewHolder.bookName.setText(book.getmName());

        if(book.getmImagePath() != null) {
            ImageLoader.getmInstance(1, ImageLoader.Type.LIFO).loadImage(book.getmImagePath(), viewHolder.image);
        } else {
            viewHolder.image.setImageBitmap(null);
        }

        return view;
    }

    class BookViewHolder {
        ImageView image;
        TextView bookName;

    }

    public void updateDate(UseDate date) {
        mDate = date;
        mYear = mDate.getYear();
        mMonth = mDate.getMonth();
    }

    private int getMonth(Book book) {
        return book.getmDate().getMonth();
    }

    private int getYear(Book book) {
        return book.getmDate().getYear();
    }

    private void setAllMoney(TextView mAllMoney) {
        mAllMoney.setText("本月需补款" + allFinishMoney + "元");
    }

    @Override
    public Filter getFilter() {
        if(mFilter == null) {
            mFilter = new MyFilter();
        }
        return mFilter;
    }

    class MyFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            FilterResults filterResults = new FilterResults();
            List<Book> list = new ArrayList<Book>();
            allFinishMoney = 0;

            /**
             * 根据选择的日期筛选项
             */
            for(Book book : mInitBooks) {
                int year = getYear(book);
                int month = getMonth(book);
                if(year == mYear && month == mMonth ) {
                    list.add(book);
                    if(book.ismIspay() == false) {
                        allFinishMoney = allFinishMoney + book.getmFinalMoney();
                    }
                }
            }
            filterResults.values = list;
            filterResults.count = list.size();
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            mBooks = (ArrayList<Book>) filterResults.values;
            notifyDataSetChanged();
            setAllMoney(AllMoneyText);
        }
    }
}

