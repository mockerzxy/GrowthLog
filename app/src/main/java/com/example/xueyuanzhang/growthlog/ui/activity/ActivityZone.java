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
import com.example.xueyuanzhang.growthlog.model.QZone;
import com.example.xueyuanzhang.growthlog.ui.adapter.ZoneListAdapter;

import java.util.ArrayList;
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
    private List<QZone> zoneList;
    private int userID;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zone);
        initView();
        getDataFromSPR();
        getData();
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
        Call<List<QZone>> call = GrowthLogApi.getInstance().getZone(userID);
        call.enqueue(new Callback<List<QZone>>() {
            @Override
            public void onResponse(Call<List<QZone>> call, Response<List<QZone>> response) {
                if (response.body() == null) {
                    Toast.makeText(getApplicationContext(), "null", Toast.LENGTH_SHORT).show();
                } else {
                    zoneList = response.body();
                }
            }

            @Override
            public void onFailure(Call<List<QZone>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "failure connect", Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });

    }

    public void addZone(View view){
        LinearLayout inputName = (LinearLayout)getLayoutInflater().inflate(R.layout.activity_addzone,null);
        final EditText editGroupName = (EditText)inputName.findViewById(R.id.edit_zoneName);
        new AlertDialog.Builder(this)
                .setIcon(R.drawable.ic_insert_emoticon_black_24dp)
                .setTitle("请输入您的群名")
                .setView(inputName)
                .setPositiveButton("建立", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String groupName = editGroupName.getText().toString();
                        if(groupName.isEmpty()){
                            Toast.makeText(getApplicationContext(), "组名不可为空！", Toast.LENGTH_SHORT).show();
                        }else{
                            final ProgressDialog progressDialog = ProgressDialog.show(ActivityZone.this, "", "创建中", true);
                            Log.d("ACC",groupName);
                            Log.d("ACC",userID+"");
                            Call<IntResponse> call = GrowthLogApi.getInstance().addZone(groupName,userID);
                            call.enqueue(new Callback<IntResponse>() {
                                @Override
                                public void onResponse(Call<IntResponse> call, Response<IntResponse> response) {
                                    progressDialog.dismiss();
                                    if (response.body() == null) {
                                        Toast.makeText(getApplicationContext(), "null", Toast.LENGTH_SHORT).show();
                                    } else {
                                        IntResponse resp = response.body();
                                        Log.i("ACC", resp.getResult() + "");
                                        switch (resp.getResult()) {
                                            case 1:
                                                Toast.makeText(getApplicationContext(), "创建成功！", Toast.LENGTH_SHORT).show();
                                                ActivityZone.super.recreate();
                                                break;
                                            case 0:
                                                Toast.makeText(getApplicationContext(), "创建失败！", Toast.LENGTH_SHORT).show();
                                                break;
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<IntResponse> call, Throwable t) {
                                    progressDialog.dismiss();
                                    Toast.makeText(getApplicationContext(), "failure connect", Toast.LENGTH_SHORT).show();
                                    t.printStackTrace();
                                }
                            });
                        }
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create()
                .show();
    }

    private void getDataFromSPR(){
        SharedPreferences pref = getSharedPreferences("Account", MODE_PRIVATE);
        userID = pref.getInt("USER_ID",0);
    }
}
