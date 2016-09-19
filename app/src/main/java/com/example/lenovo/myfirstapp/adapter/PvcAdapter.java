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
import com.example.lenovo.myfirstapp.model.Pvc;
import com.example.lenovo.myfirstapp.model.UseDate;
import com.example.lenovo.myfirstapp.util.ImageLoader;

import java.util.ArrayList;
import java.util.List;


public class PvcAdapter extends BaseAdapter implements Filterable{
    private LayoutInflater mInflater;
    private ArrayList<Pvc> mInitPvcs,mPvcs;
    private TextView AllMoneyText;

    private MyFilter mFilter;
    private UseDate mDate;

    private float allFinishMoney ;

    /**
     * 初始化的日期
     */
    private int mYear;
    private int mMonth;

    public PvcAdapter(Context context, List<Pvc> objects, UseDate date, TextView text) {
        mInflater = LayoutInflater.from(context);
        mInitPvcs = (ArrayList<Pvc>) objects;
        mPvcs = new ArrayList<Pvc>();
        AllMoneyText = text;

        mYear = date.getYear();
        mMonth = date.getMonth();

        allFinishMoney = 0;

        updateDate(date);

        /**
         * 根据初始化日期筛选相应的项
         */
      for(Pvc c : mInitPvcs) {
           int year = getYear(c);
           int month = getMonth(c);

           if(year == mYear && month == mMonth) {
               mPvcs.add(c);

               if(c.ismIspay() == false) {
               allFinishMoney = allFinishMoney + c.getmFinalMoney();
                }
            }
      }
        setAllMoney(AllMoneyText);
    }

    private int getMonth(Pvc c) {
        return c.getmDate().getMonth();
    }

    private int getYear(Pvc c) {
        return c.getmDate().getYear();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Pvc c = (Pvc) getItem(position);
        ViewHolder mViewHolder ;
        if(convertView == null) {
            convertView = mInflater.inflate(R.layout.item_list_content, parent, false);

            mViewHolder = new ViewHolder();
            mViewHolder.nameText = (TextView) convertView.findViewById(R.id.list_name);
            mViewHolder.isPay = (TextView) convertView.findViewById(R.id.item_ispay);
            mViewHolder.priceText = (TextView) convertView.findViewById(R.id.list_price);
            mViewHolder.image = (ImageView) convertView.findViewById(R.id.list_imageview);
            convertView.setTag(mViewHolder);
        }else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }
            mViewHolder.nameText.setText(c.getmName());
            mViewHolder.priceText.setText(c.getmFinalMoney() + "");

        if(c.getmImagePath() != null) {
            ImageLoader.getmInstance(1, ImageLoader.Type.LIFO).loadImage(c.getmImagePath(), mViewHolder.image);
        } else {
            mViewHolder.image.setImageBitmap(null);
        }

            if(c.ismIspay()) {
                mViewHolder.isPay.setText("已完成补款");
            }else {
                mViewHolder.isPay.setText("未完成补款");
            }

        return convertView;
    }

    @Override
    public int getCount() {
        return mPvcs.size();
    }

    @Override
    public Object getItem(int i) {
        return mPvcs.get(i);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void updateDate(UseDate date) {
        mDate = date;
        mYear = mDate.getYear();
        mMonth = mDate.getMonth();
    }

    @Override
    public Filter getFilter() {
        if(mFilter == null) {
            mFilter = new MyFilter();
        }
        return mFilter;
    }

    class ViewHolder {
        TextView nameText;
        TextView isPay;
        TextView priceText;
        ImageView image;
    }

    class MyFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            FilterResults filterResults = new FilterResults();
            List<Pvc> list = new ArrayList<Pvc>();
            allFinishMoney = 0;
            /**
             * 根据选择的日期筛选项
             */
            for(Pvc c : mInitPvcs) {
                int year = getYear(c);
                int month = getMonth(c);
                if(year == mYear && month == mMonth ) {
                    list.add(c);
                    if(c.ismIspay() == false) {
                        allFinishMoney = allFinishMoney + c.getmFinalMoney();
                    }
                }
            }
            filterResults.values = list;
            filterResults.count = list.size();
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            mPvcs = (ArrayList<Pvc>) filterResults.values;
            notifyDataSetChanged();
            setAllMoney(AllMoneyText);
        }
    }

    private void setAllMoney(TextView mAllMoney) {
        mAllMoney.setText("本月需补款" + allFinishMoney + "元");
    }
}
