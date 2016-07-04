package com.example.xueyuanzhang.growthlog.ui.activity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.example.xueyuanzhang.growthlog.R;
import com.example.xueyuanzhang.growthlog.model.ListEntry;
import com.example.xueyuanzhang.growthlog.model.Record;
import com.example.xueyuanzhang.growthlog.model.TimeEntity;
import com.example.xueyuanzhang.growthlog.ui.adapter.RecordListAdapter;
import com.example.xueyuanzhang.growthlog.ui.util.TimeUtil;
import com.example.xueyuanzhang.growthlog.util.LocalDataBaseHelper;
import com.example.xueyuanzhang.growthlog.util.PicPathUtil;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.holder.BadgeStyle;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.fab)
    FloatingActionButton floatingActionButton;
    @BindView(R.id.swipe_container)
    SwipeRefreshLayout swipeRefreshLayout;

    private LocalDataBaseHelper dbHelper;
    private Drawer drawer;
    private AccountHeader headerView;
    private List<Record> recordList = new ArrayList<>();
    private List<ListEntry> parentList = new ArrayList<>();
    private RecordListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        dbHelper = new LocalDataBaseHelper(this, "GrowthLogDB.db3", 2);
        queryAllData(dbHelper.getReadableDatabase());

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ActivityEdit.class);
                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(MainActivity.this).toBundle());
            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                queryAllData(dbHelper.getReadableDatabase());
                adapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

    }

    private void initView() {
        ButterKnife.bind(this);
        toolbar.inflateMenu(R.menu.menu_toobar);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.share) {
                    //分享功能
                }
                return false;
            }
        });
        initAccountHeader();
        initDrawer();
        initRecyclerView();
    }

    private void initDrawer() {
        PrimaryDrawerItem item1 = new PrimaryDrawerItem().withName("个人资料").withIcon(R.drawable.ic_person_black_24dp).withTag("profile").withTextColor(Color.BLACK).withSelectable(false);

        drawer = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withAccountHeader(headerView)
                .addDrawerItems(
                        item1,
                        new DividerDrawerItem()
                )
                .withSelectedItem(-1)
                .build();
    }

    private void initRecyclerView() {
        adapter = new RecordListAdapter(parentList, this);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager lm = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(lm);
        recyclerView.setAdapter(adapter);
    }

    private void initAccountHeader() {
        headerView = new AccountHeaderBuilder()
                .withActivity(this)
                .withTranslucentStatusBar(true)
                .withHeaderBackground(R.drawable.growth)
                .addProfiles(new ProfileDrawerItem().withName("Moris").withEmail("moris.zxy@gmail.com").withIcon(R.drawable.header))
                .withOnAccountHeaderSelectionViewClickListener(new AccountHeader.OnAccountHeaderSelectionViewClickListener() {
                    @Override
                    public boolean onClick(View view, IProfile profile) {
                        return false;
                    }
                })
                .withSelectionListEnabledForSingleProfile(false)
                .build();

    }

    private void queryAllData(SQLiteDatabase db) {
        Cursor cursor = db.rawQuery("select * from GrowthLog ORDER BY _id DESC", null);
        recordList.clear();
        parentList.clear();
        while (cursor.moveToNext()) {
            Record record = new Record();
            record.setId(cursor.getInt(0));
            record.setText(cursor.getString(1));
            record.setPicList(PicPathUtil.split(cursor.getString(2)));
            record.setTime(cursor.getString(3));
            if (parentList.size() == 0) {
                TimeEntity timeEntity = new TimeEntity();
                timeEntity.setTime(cursor.getString(3));
                parentList.add(timeEntity);

            } else if (!TimeUtil.formatTime(recordList.get(recordList.size() - 1).getTime()).equals(TimeUtil.formatTime(record.getTime()))) {
                TimeEntity timeEntity = new TimeEntity();
                timeEntity.setTime(cursor.getString(3));
                parentList.add(timeEntity);
            }
                parentList.add(record);
                recordList.add(record);

        }
        cursor.close();

    }

    private void deleteAllData(SQLiteDatabase db){
        db.execSQL("delete from GrowthLog");
    }


}
