package com.example.xueyuanzhang.growthlog.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.xueyuanzhang.growthlog.R;
import com.example.xueyuanzhang.growthlog.model.QZone;
import com.example.xueyuanzhang.growthlog.model.Zone;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by xueyuanzhang on 16/7/11.
 */
public class ZoneListAdapter extends RecyclerView.Adapter{
    private List<QZone> list;
    private Context context;
    public ZoneListAdapter(List<QZone> list, Context context) {
        this.list = list;
        this.context = context;
    }

    public class ZoneHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.name)
        TextView nameTV;
        @BindView(R.id.description)
        TextView descTV;

        public ZoneHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_zone_item,parent,false);
        return new ZoneHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
         ZoneHolder viewHolder = (ZoneHolder)holder;

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
