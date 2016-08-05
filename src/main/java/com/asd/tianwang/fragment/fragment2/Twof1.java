package com.asd.tianwang.fragment.fragment2;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.asd.tianwang.R;
import com.asd.tianwang.dao.YhisDao;
import com.asd.tianwang.dao.table.Tbyhis;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ASD on 2016/8/4.
 */
public class Twof1 extends Fragment{
    private ListView lv;
    private LvAdapter lvAdapter;
    private List<Tbyhis> listData=new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.twof1, null, false);
        init(view);
        dataset();
        return view;
    }
    private void init(View view){
        lv=(ListView) view.findViewById(R.id.lvdata);
    }
    private void dataset(){
        YhisDao yhisDao=new YhisDao(getActivity());
        listData=yhisDao.find("2016-8-2");
        lvAdapter=new LvAdapter(listData,getActivity());
        lvAdapter.notifyDataSetChanged();
        lv.setAdapter(lvAdapter);
    }
}
