package com.example.lenovo.myfirstapp.view;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.lenovo.myfirstapp.R;
import com.example.lenovo.myfirstapp.model.FolderBean;
import com.example.lenovo.myfirstapp.util.ImageLoader;

import java.util.List;

public class ListImageDirPopupWindow extends PopupWindow {
    private int mWidth;
    private int mHeight;
    private View mConvertView;
    private ListView mListView;

    private List<FolderBean> mDatas;

    public onDirSelectListener mOnDirSelectedListener;

    public interface onDirSelectListener {
        void onSelected(FolderBean folderBean);
    }

    public void setOnDirSelectedListener(onDirSelectListener mOnDirSelectedListener) {
        this.mOnDirSelectedListener = mOnDirSelectedListener;
    }

    public ListImageDirPopupWindow(Context context, List<FolderBean> datas) {
        calWidthAndHeight(context);
        mConvertView = LayoutInflater.from(context).inflate(R.layout.popup_main, null);
        mDatas = datas;

        setContentView(mConvertView);
        setWidth(mWidth);
        setHeight(mHeight);

        setFocusable(true);
        setTouchable(true);
        setOutsideTouchable(true);
        setBackgroundDrawable(new BitmapDrawable());

        setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    dismiss();
                    return true;
                }
                return false;
            }
        });

        intViews(context);
        intEvent();
    }

    private void intEvent() {
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(mOnDirSelectedListener != null) {
                    mOnDirSelectedListener.onSelected(mDatas.get(i));
                }
            }
        });
    }

    private void intViews(Context context) {
        mListView = (ListView) mConvertView.findViewById(R.id.id_list_dir);
        ListDiaAdapter ld = new ListDiaAdapter(context, mDatas);
        mListView.setAdapter(ld);
    }

    /**
     * 计算PopupWindow的宽度和高度
     * @param context
     */
    private void calWidthAndHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);

        mWidth = outMetrics.widthPixels;
        mHeight = (int) (outMetrics.heightPixels * 0.7f);
    }

    private class ListDiaAdapter extends ArrayAdapter<FolderBean> {
        private LayoutInflater mInflater;

        public ListDiaAdapter(Context context, List<FolderBean> objects) {
            super(context, 0, objects);
            mInflater = LayoutInflater.from(context);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            if(convertView == null) {
                viewHolder = new ViewHolder();
                convertView = mInflater.inflate(R.layout.item_popup_main, parent, false);
                viewHolder.mImg = (ImageView) convertView.findViewById(R.id.id_id_dir_item_image);
                viewHolder.mDirName = (TextView) convertView.findViewById(R.id.id_dir_item_name);
                viewHolder.mDirCount = (TextView) convertView.findViewById(R.id.id_dir_item_count);

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            FolderBean bean = getItem(position);
            //重置
            viewHolder.mImg.setImageResource(R.drawable.pictures_no);

            ImageLoader.getmInstance(3, ImageLoader.Type.LIFO).loadImage(bean.getFirstImgPath(), viewHolder.mImg);
            viewHolder.mDirName.setText(bean.getName());
            viewHolder.mDirCount.setText(bean.getCount() + "");

            return convertView;
        }
    }

    private class ViewHolder {
        ImageView mImg;
        TextView mDirName;
        TextView mDirCount;
    }
}

