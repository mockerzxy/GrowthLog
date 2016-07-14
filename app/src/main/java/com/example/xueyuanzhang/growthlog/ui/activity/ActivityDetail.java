package com.example.xueyuanzhang.growthlog.ui.activity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewStub;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xueyuanzhang.growthlog.R;
import com.example.xueyuanzhang.growthlog.model.Record;
import com.example.xueyuanzhang.growthlog.util.Export;
import com.example.xueyuanzhang.growthlog.util.LocalDataBaseHelper;
import com.example.xueyuanzhang.growthlog.util.PicPathUtil;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by xueyuanzhang on 16/7/5.
 */
public class ActivityDetail extends AppCompatActivity {
    @BindView(R.id.text_content)
    TextView textView;
    @BindView(R.id.container_of_detail_page)
    LinearLayout container;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.imageStub_type1)
    ViewStub viewStubType1;
    @BindView(R.id.imageStub_type2)
    ViewStub viewStubType2;
    @BindView(R.id.imageStub_type3)
    ViewStub viewStubType3;
    @BindView(R.id.editButton)
    FloatingActionButton fab;

    private LocalDataBaseHelper dbHelper;
    private Record record = new Record();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        dbHelper = new LocalDataBaseHelper(this, "GrowthLogDB.db3", 4);
        int id = getIntent().getIntExtra("diary_id", -1);
        Log.e("ERROR_ID", id + "");
        if (id != -1) {
            QueryData(dbHelper.getReadableDatabase(), id);
        } else {
            Toast.makeText(getApplicationContext(), "该日记不存在!", Toast.LENGTH_SHORT).show();
        }
        initView();

    }

    private void initView() {
        ButterKnife.bind(this);
        initToolbar();
        textView.setText(record.getText());

        if (!record.getPicList().get(0).equals("")) {
            if (record.getPicList().size() == 1) {
                showType1Holder();
            } else if (record.getPicList().size() > 1 && record.getPicList().size() < 5) {
                showType2Holder();
            } else {
                showType3Holder();
            }

        }
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityDetail.this,ActivityEdit.class);
                ArrayList<String> pic_list = (ArrayList<String>) record.getPicList();
                intent.putStringArrayListExtra("PIC_LIST",pic_list);
                intent.putExtra("TEXT",record.getText());
                intent.putExtra("ID",record.getId());
                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(ActivityDetail.this).toBundle());
            }
        });

    }

    private void initToolbar() {
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        toolbar.setTitle("日志详情");
        toolbar.setTitleTextColor(getResources().getColor(R.color.lightBlack));
        toolbar.inflateMenu(R.menu.menu_detail);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Export.createImage(textView.getText().toString());
                return false;
            }
        });
    }

    private void QueryData(SQLiteDatabase db, int id) {
        Cursor cursor = db.rawQuery("select * from GrowthLog where _id = " + id, null);
        while (cursor.moveToNext()) {
            record.setId(cursor.getInt(0));
            record.setText(cursor.getString(1));
            record.setPicList(PicPathUtil.split(cursor.getString(2)));
            record.setTime(cursor.getString(3));
        }
        cursor.close();
    }

    private void showType1Holder() {
        View view = viewStubType1.inflate();
        ImageView imageView = (ImageView) view.findViewById(R.id.image_view);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                   jumpToImageFlipper(0);
            }
        });
        File file = new File(record.getPicList().get(0));
        Picasso.with(this).load(file).into(imageView);
    }

    private void showType2Holder() {
        View view = viewStubType2.inflate();
        GridLayout gridLayout = (GridLayout) view.findViewById(R.id.gridLayout);
        for (int i = 0; i < record.getPicList().size(); i++) {
            final int position =i;
            ImageView imageView = (ImageView) LayoutInflater.from(this).inflate(R.layout.view_imageitem, gridLayout, false);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    jumpToImageFlipper(position);
                }
            });
            File file = new File(record.getPicList().get(i));
            Picasso.with(this).load(file).resize(440, 440).centerCrop().into(imageView);
            gridLayout.addView(imageView);
        }

    }

    private void showType3Holder() {
        View view = viewStubType3.inflate();
        GridLayout gridLayout = (GridLayout) view.findViewById(R.id.gridLayout);
        for (int i = 0; i < record.getPicList().size(); i++) {
            final int position = i;
            ImageView imageView = (ImageView) LayoutInflater.from(this).inflate(R.layout.view_imageitem, gridLayout, false);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    jumpToImageFlipper(position);
                }
            });
            File file = new File(record.getPicList().get(i));
            Picasso.with(this).load(file).resize(290, 290).centerCrop().into(imageView);
            gridLayout.addView(imageView);
        }
    }

    private void jumpToImageFlipper(int position){
        Intent intent = new Intent(ActivityDetail.this,ActivityImageFlipper.class);
        ArrayList<String> pic_list = (ArrayList<String>) record.getPicList();
        intent.putStringArrayListExtra("PIC_LIST",pic_list);
        intent.putExtra("POSITION",position);
        startActivity(intent);
    }

}
