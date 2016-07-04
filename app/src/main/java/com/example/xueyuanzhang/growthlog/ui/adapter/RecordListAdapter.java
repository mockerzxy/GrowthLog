package com.example.xueyuanzhang.growthlog.ui.adapter;

import android.content.Context;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.xueyuanzhang.growthlog.R;
import com.example.xueyuanzhang.growthlog.model.ListEntry;
import com.example.xueyuanzhang.growthlog.model.Record;
import com.example.xueyuanzhang.growthlog.model.TimeEntity;
import com.example.xueyuanzhang.growthlog.ui.util.TimeUtil;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by xueyuanzhang on 16/7/3.
 */
public class RecordListAdapter extends RecyclerView.Adapter {
    private final static int TIME_STAMP = 0;
    private final static int NORMAL_VIEW = 1;
    private List<ListEntry> list = new ArrayList<>();
    private Context context;

    public RecordListAdapter(List<ListEntry> list, Context context) {
        this.context = context;
        this.list = list;
    }

    public class LogViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.text_content)
        TextView textView;
        @BindView(R.id.imageStub_type1)
        ViewStub viewStubType1;
        @BindView(R.id.imageStub_type2)
        ViewStub viewStubType2;
        @BindView(R.id.imageStub_type3)
        ViewStub viewStubType3;
        View contentView;

        public LogViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            contentView = itemView;
        }
    }

    public class TimeViewHolder extends RecyclerView.ViewHolder{
    @BindView(R.id.timeStamp)
    TextView timeView;
        public TimeViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (list.get(position) instanceof TimeEntity) {
            return TIME_STAMP;
        }
        if (list.get(position) instanceof Record) {
            return NORMAL_VIEW;
        }
        return super.getItemViewType(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == TIME_STAMP) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_timestamp, parent, false);
            return new TimeViewHolder(view);
        } else if (viewType == NORMAL_VIEW) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_mydialog, parent, false);
            return new LogViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof TimeViewHolder){
            TimeViewHolder timeViewHolder = (TimeViewHolder)holder;
            TimeEntity timeEntity = (TimeEntity)list.get(position);
            timeViewHolder.timeView.setText(TimeUtil.formatTime(timeEntity.getTime()));
        }
        else if(holder instanceof LogViewHolder) {
            LogViewHolder viewHolder = (LogViewHolder) holder;
            Record record = (Record)list.get(position);
            if (record.getText().equals("")) {
                viewHolder.textView.setVisibility(View.GONE);
            } else {
                viewHolder.textView.setVisibility(View.VISIBLE);
                viewHolder.textView.setText(record.getText());
            }
            if (!record.getPicList().get(0).equals("")) {
                if (record.getPicList().size() == 1) {
                    Log.i("SIGNAL","**called**");
                    hideViewHolder(viewHolder, 2);
                    hideViewHolder(viewHolder, 3);
                    showType1Holder(viewHolder, record);
                } else if (record.getPicList().size() > 1 && record.getPicList().size() < 5) {
                    hideViewHolder(viewHolder, 1);
                    hideViewHolder(viewHolder, 3);
                    showType2Holder(viewHolder, record);
                } else {
                    hideViewHolder(viewHolder, 1);
                    hideViewHolder(viewHolder, 2);
                    showType3Holder(viewHolder, record);
                }

            } else {
                hideViewHolder(viewHolder, 1);
                hideViewHolder(viewHolder, 2);
                hideViewHolder(viewHolder, 3);
            }
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private void hideViewHolder(LogViewHolder viewHolder, int type) {
        switch (type) {
            case 1:
                if (viewHolder.contentView.findViewById(R.id.stubInflateId1) != null) {
                    viewHolder.contentView.findViewById(R.id.stubInflateId1).setVisibility(View.GONE);
                }
                break;
            case 2:
                if (viewHolder.contentView.findViewById(R.id.stubInflateId2) != null) {
                    viewHolder.contentView.findViewById(R.id.stubInflateId2).setVisibility(View.GONE);
                }
                break;
            case 3:
                if (viewHolder.contentView.findViewById(R.id.stubInflateId3) != null) {
                    viewHolder.contentView.findViewById(R.id.stubInflateId3).setVisibility(View.GONE);
                }
                break;
        }

    }

    private void showType1Holder(LogViewHolder viewHolder, Record record) {
        View view;
        if (viewHolder.contentView.findViewById(R.id.stubInflateId1) != null) {
            view = viewHolder.contentView.findViewById(R.id.stubInflateId1);
            view.setVisibility(View.VISIBLE);
        } else {
            view = viewHolder.viewStubType1.inflate();
        }
        ImageView imageView = (ImageView) view.findViewById(R.id.image_view);
        File file = new File(record.getPicList().get(0));
        Picasso.with(context).load(file).into(imageView);
    }

    private void showType2Holder(LogViewHolder viewHolder, Record record) {
        View view;
        if (viewHolder.contentView.findViewById(R.id.stubInflateId2) != null) {
            view = viewHolder.contentView.findViewById(R.id.stubInflateId2);
            view.setVisibility(View.VISIBLE);
        } else {
            view = viewHolder.viewStubType2.inflate();
        }
        GridLayout gridLayout = (GridLayout) view.findViewById(R.id.gridLayout);
        gridLayout.removeAllViewsInLayout();
        for (int i = 0; i < record.getPicList().size(); i++) {
            ImageView imageView = (ImageView) LayoutInflater.from(context).inflate(R.layout.view_imageitem, gridLayout, false);
            File file = new File(record.getPicList().get(i));
            Picasso.with(context).load(file).resize(410, 410).centerCrop().into(imageView);
            gridLayout.addView(imageView);
        }
    }

    private void showType3Holder(LogViewHolder viewHolder, Record record) {
        View view;
        if (viewHolder.contentView.findViewById(R.id.stubInflateId3) != null) {
            view = viewHolder.contentView.findViewById(R.id.stubInflateId3);
            view.setVisibility(View.VISIBLE);
        } else {
            view = viewHolder.viewStubType3.inflate();
        }
        GridLayout gridLayout = (GridLayout) view.findViewById(R.id.gridLayout);
        gridLayout.removeAllViewsInLayout();
        for (int i = 0; i < record.getPicList().size(); i++) {
            ImageView imageView = (ImageView) LayoutInflater.from(context).inflate(R.layout.view_imageitem, gridLayout, false);
            File file = new File(record.getPicList().get(i));
            Picasso.with(context).load(file).resize(280, 280).centerCrop().into(imageView);
            gridLayout.addView(imageView);
        }
    }
}
