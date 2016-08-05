package com.asd.tianwang.fragment.fragment2;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.asd.tianwang.R;
import com.asd.tianwang.dao.table.Tbyhis;

import java.util.List;

/**
 * Created by ASD on 2016/8/4.
 */
public class LvAdapter extends BaseAdapter {
    private List<Tbyhis> data;
    private Context context;
    private LayoutInflater layoutInflater;

    public LvAdapter(List<Tbyhis> data, Context context) {
        this.data = data;
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public boolean isEnabled(int position) {
        return super.isEnabled(position);
    }

    public int getCount() {
        return data.size();
    }

    public Object getItem(int position) {
        return data.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Tbyhis tbyhis = data.get(position);
        ViewHolder viewHolder = null;
        if (position % 2 == 0) {
            convertView = layoutInflater.inflate(R.layout.lvitem, null);
        } else {
            convertView = layoutInflater.inflate(R.layout.lvitem2, null);
        }
        viewHolder = new ViewHolder();
        viewHolder.txid = (TextView) convertView.findViewById(R.id.tw_id);
        viewHolder.txcon = (TextView) convertView.findViewById(R.id.tw_con);
        viewHolder.txpre = (TextView) convertView.findViewById(R.id.tw_pre);
        viewHolder.txlev = (TextView) convertView.findViewById(R.id.tw_level);
        viewHolder.txtime = (TextView) convertView.findViewById(R.id.tw_time);
        viewHolder.txdate = (TextView) convertView.findViewById(R.id.tw_date);
        viewHolder.txid.setText(position + "");
        viewHolder.txcon.setText(tbyhis.con + "");
        viewHolder.txpre.setText(tbyhis.pre + "");
        viewHolder.txlev.setText(tbyhis.level + "");
        viewHolder.txtime.setText(tbyhis.mtime);
        viewHolder.txdate.setText(tbyhis.mdate);
        if (tbyhis.con > 13) {
            viewHolder.txcon.setTextColor(Color.parseColor("#EB523B"));
        }
        if (tbyhis.pre > 15) {
            viewHolder.txpre.setTextColor(Color.parseColor("#EB523B"));
        }
        return convertView;
    }

    static class ViewHolder {
        public TextView txid, txcon, txpre, txlev, txtime, txdate;

    }
}
