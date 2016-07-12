package com.example.xueyuanzhang.growthlog.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.example.xueyuanzhang.growthlog.R;
import com.example.xueyuanzhang.growthlog.model.Zone;
import com.example.xueyuanzhang.growthlog.ui.adapter.ZoneListAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by xueyuanzhang on 16/7/7.
 */
public class ActivityZone extends AppCompatActivity{
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.addZoneButton)
    FloatingActionButton addFab;

    private ZoneListAdapter adapter;
    private List<Zone> zoneList;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zone);
        initView();
    }

    private void initView(){
        ButterKnife.bind(this);
        initToolbar();
        initRecyclerView();
        getData();
    }
    private void initToolbar(){
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setTitle("空间");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        toolbar.setTitleTextColor(getResources().getColor(R.color.lightBlack));
    }

    private void initRecyclerView(){
        zoneList = new ArrayList<>();
        adapter = new ZoneListAdapter(zoneList,this);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager lm = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(lm);
        recyclerView.setAdapter(adapter);
    }

    private void getData(){
        //get data from the server;
    }
}
