package com.example.xueyuanzhang.growthlog.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.xueyuanzhang.growthlog.R;
import com.example.xueyuanzhang.growthlog.model.ListEntry;
import com.example.xueyuanzhang.growthlog.model.MyDiary;
import com.example.xueyuanzhang.growthlog.model.OtherDiary;
import com.example.xueyuanzhang.growthlog.model.QZone;
import com.example.xueyuanzhang.growthlog.model.Zone;
import com.example.xueyuanzhang.growthlog.ui.util.TimeUtil;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by xueyuanzhang on 16/7/11.
 */
public class ZoneListAdapter extends RecyclerView.Adapter {
    private final static int PRIVATE_DIARY = 1;
    private final static int PUBLIC_DIARY = 2;
    private List<ListEntry> list;
    private Context context;

    public ZoneListAdapter(List<ListEntry> list, Context context) {
        this.list = list;
        this.context = context;
    }

    public class ZoneHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.name)
        TextView nameTV;
        @BindView(R.id.header)
        CircleImageView header;
        @BindView(R.id.time)
        TextView timeTV;
        @BindView(R.id.praiseButton)
        ImageButton praiseButton;
        @BindView(R.id.num)
        TextView numTV;

        public ZoneHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public class OtherHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.name)
        TextView nameTV;
        @BindView(R.id.header)
        CircleImageView headerV;
        @BindView(R.id.time)
        TextView timeTV;
        @BindView(R.id.praiseButton)
        ImageButton praiseButton;
        @BindView(R.id.num)
        TextView numTV;

        public OtherHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == PRIVATE_DIARY) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_zone_item, parent, false);
            return new ZoneHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_other_diary,parent,false);
            return new OtherHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof ZoneHolder) {
            final ZoneHolder viewHolder = (ZoneHolder) holder;
            MyDiary myDiary = (MyDiary)list.get(position);
            File file = new File(myDiary.header);
            Picasso.with(context).load(file).resize(48,48).into(viewHolder.header);
            viewHolder.nameTV.setText(myDiary.name);
            if(myDiary.time.length()!=9) {
                viewHolder.timeTV.setText(TimeUtil.formatTimeInRough(myDiary.time));
            }else {
                viewHolder.timeTV.setText(myDiary.time);
            }
            viewHolder.praiseButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int num = Integer.valueOf(viewHolder.numTV.getText().toString());
                    num++;
                    viewHolder.numTV.setText(num+"");
                }
            });
        }else {
            final OtherHolder viewHolder = (OtherHolder) holder;
            OtherDiary otherDiary = (OtherDiary) list.get(position);
            viewHolder.nameTV.setText(otherDiary.name);
            viewHolder.timeTV.setText(otherDiary.time);
            viewHolder.praiseButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int num = Integer.valueOf(viewHolder.numTV.getText().toString());
                    num++;
                    viewHolder.numTV.setText(num+"");
                }
            });

        }
        }


    @Override
    public int getItemViewType(int position) {
        if (list.get(position) instanceof MyDiary) {
            return PRIVATE_DIARY;
        } else {
            return PUBLIC_DIARY;
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
