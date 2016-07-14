package com.example.xueyuanzhang.growthlog.ui.activity;
;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.xueyuanzhang.growthlog.R;
import com.example.xueyuanzhang.growthlog.api.GrowthLogApi;
import com.example.xueyuanzhang.growthlog.model.IntResponse;
import com.example.xueyuanzhang.growthlog.model.ListEntry;
import com.example.xueyuanzhang.growthlog.model.MyDiary;
import com.example.xueyuanzhang.growthlog.model.OtherDiary;
import com.example.xueyuanzhang.growthlog.model.QZone;
import com.example.xueyuanzhang.growthlog.ui.adapter.ZoneListAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
    private List<ListEntry> zoneList;
    private int userID;
    private String userName;
    private String userHeader;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zone);
        getDataFromSPR();
        initView();
    }

    private void initView(){
        ButterKnife.bind(this);
        initToolbar();
        initRecyclerView();
        addFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDiary myDiary = new MyDiary();
                myDiary.name = userName;
                myDiary.header = userHeader;
                Calendar c = Calendar.getInstance();
                myDiary.time=c.getTime().toString();
                zoneList.add(myDiary);
                adapter.notifyDataSetChanged();
            }
        });
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
        initData();
        adapter = new ZoneListAdapter(zoneList,this);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager lm = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(lm);
        recyclerView.setAdapter(adapter);
    }





    private void getDataFromSPR(){
        SharedPreferences pref = getSharedPreferences("Account", MODE_PRIVATE);
        userID = pref.getInt("USER_ID",0);
        userName = pref.getString("USER_NICK_NAME","null");
        userHeader = pref.getString("USER_HEADER","null");

    }

    private void initData(){
        MyDiary myDiary1 = new MyDiary();
        myDiary1.time = "2016-7-14";
        myDiary1.name = userName;
        myDiary1.header = userHeader;
        OtherDiary other1 = new OtherDiary();
        other1.time = "2016-7-13";
        other1.name = "Hawker";
        MyDiary myDiary2 = new MyDiary();
        myDiary2.time = "2016-7-12";
        myDiary2.name = userName;
        myDiary2.header = userHeader;
        OtherDiary other2 = new OtherDiary();
        other2.time = "2016-7-11";
        other2.name = "Joker";
        zoneList.add(myDiary1);
        zoneList.add(other1);
        zoneList.add(myDiary2);
        zoneList.add(other2);
    }
}
