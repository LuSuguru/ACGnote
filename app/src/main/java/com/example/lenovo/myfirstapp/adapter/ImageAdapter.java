package com.example.lenovo.myfirstapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.lenovo.myfirstapp.R;
import com.example.lenovo.myfirstapp.util.ImageLoader;

import java.util.List;

/**
 * Created by lenovo on 2016/9/5.
 */
public class ImageAdapter extends BaseAdapter {

    private String mDirPath;
    private List<String> mImgPaths;
    private LayoutInflater mInflater;

    public ImageAdapter(Context context, List<String> mDatas, String dirPath) {
        this.mImgPaths = mDatas;
        this.mDirPath = dirPath;
        mInflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return mImgPaths.size();
    }

    @Override
    public Object getItem(int i) {
        return mImgPaths.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        final ViewHolder viewHolder  ;
        if(view == null) {
            view = mInflater.inflate(R.layout.item_gridview, viewGroup, false);
            viewHolder = new ViewHolder();
            viewHolder.mImg = (ImageView) view.findViewById(R.id.id_item_image);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder)view.getTag();
        }
        //重置状态
        viewHolder.mImg.setImageResource(R.drawable.pictures_no);
        viewHolder.mImg.setColorFilter(null);

        ImageLoader.getmInstance(3, ImageLoader.Type.LIFO).loadImage(mDirPath + "/" +mImgPaths.get(i), viewHolder.mImg);
        final String filePath = mDirPath + "/" + mImgPaths.get(i);

        return view;
    }

    private class ViewHolder {
        ImageView mImg;
    }
}