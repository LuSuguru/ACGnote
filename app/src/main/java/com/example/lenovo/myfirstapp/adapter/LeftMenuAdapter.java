package com.example.lenovo.myfirstapp.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lenovo.myfirstapp.R;
import com.example.lenovo.myfirstapp.model.MenuItem;


/**
 * Created by lenovo on 2016/8/21.
 */
public class LeftMenuAdapter extends ArrayAdapter<MenuItem> {

    private LayoutInflater mInflater;
    private int mSelected;

    public LeftMenuAdapter(Context context, MenuItem[] object) {
        super(context, 0 ,object);
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            convertView = mInflater.inflate(R.layout.item_left_menu, parent,false);
        }
        ImageView iv = (ImageView) convertView.findViewById(R.id.id_item_icon);
        TextView tv = (TextView) convertView.findViewById(R.id.id_item_title);

        iv.setImageResource(getItem(position).icon);
        tv.setText(getItem(position).text);
        convertView.setBackgroundColor(Color.TRANSPARENT);

        if(position == mSelected) {
            iv.setImageResource(getItem(position).iconSelected);
            convertView.setBackgroundColor(getContext().getResources().getColor(R.color.state_menu_item_selected));
        }

        return convertView;
    }

    public void setmSelected(int position) {
        mSelected =position;
        notifyDataSetChanged();
    }
}
