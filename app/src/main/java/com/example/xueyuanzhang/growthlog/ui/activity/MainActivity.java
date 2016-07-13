package com.example.xueyuanzhang.growthlog.ui.activity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.daimajia.swipe.SwipeLayout;
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

    private SearchView searchView;


    private final static int EDIT_NEW_DIARY = 1;

    private LocalDataBaseHelper dbHelper;
    private Drawer drawer;
    private AccountHeader headerView;
    private List<Record> recordList = new ArrayList<>();
    private List<ListEntry> parentList = new ArrayList<>();
    private RecordListAdapter adapter;
    private int userId;
    private String email;
    private String nickName;
    private String header;


    private Paint p = new Paint();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences pref = getSharedPreferences("Account", MODE_PRIVATE);
        int id = pref.getInt("USER_ID", 0);
        if (id == 0) {
            Intent intent = new Intent(MainActivity.this, ActivityLogin.class);
            startActivity(intent);
        } else {
            setContentView(R.layout.activity_main);
            initView();
            dbHelper = new LocalDataBaseHelper(this, "GrowthLogDB.db3", 2);
            queryAllData(dbHelper.getReadableDatabase());

            floatingActionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, ActivityEdit.class);
                    startActivityForResult(intent, EDIT_NEW_DIARY, ActivityOptions.makeSceneTransitionAnimation(MainActivity.this).toBundle());
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


    }

    private void initView() {
        ButterKnife.bind(this);
        toolbar.inflateMenu(R.menu.menu_toobar);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.zone) {
                    Intent intent = new Intent(MainActivity.this, ActivityZone.class);
                    startActivity(intent);
                }
                if (itemId == R.id.search) {
                    searchView = (SearchView) MenuItemCompat.getActionView(item);
                    searchView.setVisibility(View.VISIBLE);
                    return true;
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
        PrimaryDrawerItem item2 = new PrimaryDrawerItem().withName("修改密码").withIcon(R.drawable.ic_build_black_24dp).withTag("modifyPW").withTextColor(Color.BLACK).withSelectable(false);
        PrimaryDrawerItem item3 = new PrimaryDrawerItem().withName("退出登录").withIcon(R.drawable.ic_clear_black_24dp).withTag("quit").withTextColor(Color.BLACK).withSelectable(false);
        drawer = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withAccountHeader(headerView)
                .addDrawerItems(
                        item1,
                        item2,
                        item3
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        switch (position) {
                            case 1:
                                Intent intent = new Intent(MainActivity.this, ActivityProfile.class);
                                startActivity(intent);
                                break;
                            case 2:
                                Intent intent1 = new Intent(MainActivity.this, ActivityModifyPw.class);
                                startActivity(intent1);
                                break;

                            case 3:
                                SharedPreferences pref = getSharedPreferences("Account", MODE_PRIVATE);
                                pref.edit().clear().commit();
                                Intent intent2 = new Intent(MainActivity.this,ActivityLogin.class);
                                intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent2);
                                break;
                        }
                        return true;
                    }
                })
                .withSelectedItem(-1)
                .build();
    }

    private void initRecyclerView() {
        adapter = new RecordListAdapter(parentList, this);
        adapter.setSwipeListener(new SwipeLayout.SwipeListener() {
            @Override
            public void onStartOpen(SwipeLayout layout) {

            }

            @Override
            public void onOpen(SwipeLayout layout) {

            }

            @Override
            public void onStartClose(SwipeLayout layout) {

            }

            @Override
            public void onClose(SwipeLayout layout) {

            }

            @Override
            public void onUpdate(SwipeLayout layout, int leftOffset, int topOffset) {

            }

            @Override
            public void onHandRelease(SwipeLayout layout, float xvel, float yvel) {

            }
        });
        adapter.setOnDeleteListener(new RecordListAdapter.OnDeleteListener() {
            @Override
            public void onDelete(int id) {
                deleteItemData(dbHelper.getReadableDatabase(), id);
            }
        });
        adapter.setOnShowDetailListener(new RecordListAdapter.OnShowDetailListener() {
            @Override
            public void onDetail(int id) {
                Intent intent = new Intent(MainActivity.this, ActivityDetail.class);
                intent.putExtra("diary_id", id);
                startActivity(intent);
            }
        });
        adapter.setOnClickImageListener(new RecordListAdapter.OnClickImageListener() {
            @Override
            public void onClick(int position, List<String> picList) {
                Intent intent = new Intent(MainActivity.this, ActivityImageFlipper.class);
                ArrayList<String> pic_list = (ArrayList<String>) picList;
                intent.putStringArrayListExtra("PIC_LIST", pic_list);
                intent.putExtra("POSITION", position);
                startActivity(intent);
            }
        });
        adapter.setOnShareListener(new RecordListAdapter.OnShareListener() {
            @Override
            public void onShare(int id) {
                Intent intent = new Intent(MainActivity.this, ActivityShare.class);
                intent.putExtra("record_id", id);
                startActivity(intent);
            }
        });
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager lm = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(lm);
        recyclerView.setAdapter(adapter);

//        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0,
//                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
//            public int flag;
//
//            @Override
//            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
//                return false;
//            }
//
//            @Override
//            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
//                deleteItemData(dbHelper.getReadableDatabase(), viewHolder.getAdapterPosition());
//
//            }
//
//            @Override
//            public boolean isItemViewSwipeEnabled() {
//
//                return super.isItemViewSwipeEnabled();
//            }
//
//
//
//
//            @Override
//            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
////                Bitmap deleteIcon = BitmapFactory.decodeResource(MainActivity.this.getResources(), R.drawable.ic_delete_black_24dp);
//                Bitmap icon;
//                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
//                    View itemView = viewHolder.itemView;
//                    float height = (float) itemView.getBottom() - (float) itemView.getTop();
//                    float width = height / 3;
//                    if (dX < 0) {
//                        p.setColor(Color.parseColor("#D32F2F"));
//                        RectF background = new RectF((float) itemView.getRight()+dX, (float) itemView.getTop(),(float) itemView.getRight(), (float) itemView.getBottom());
//                        c.drawRect(background,p);
//                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_delete_black_24dp);
//                        RectF icon_dest = new RectF((float) itemView.getRight() - 2*width ,(float) itemView.getTop() + width,(float) itemView.getRight() - width,(float)itemView.getBottom() - width);
//                        if(icon!=null) {
//                            c.drawBitmap(icon, null, icon_dest, p);
//                        }
//                    }
//                }
//                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
//            }
//
//        };
//        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
//        itemTouchHelper.attachToRecyclerView(recyclerView);

    }

    private void initAccountHeader() {
        getDataFromSharePR();
        headerView = new AccountHeaderBuilder()
                .withActivity(this)
                .withTranslucentStatusBar(true)
                .withHeaderBackground(R.drawable.growth)
                .addProfiles(new ProfileDrawerItem().withName(nickName).withEmail(email).withIcon(header))
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

            } else if (!TimeUtil.formatTimeInRough(recordList.get(recordList.size() - 1).getTime()).equals(TimeUtil.formatTimeInRough(record.getTime()))) {
                TimeEntity timeEntity = new TimeEntity();
                timeEntity.setTime(cursor.getString(3));
                parentList.add(timeEntity);
            }
            parentList.add(record);
            recordList.add(record);

        }
        cursor.close();

    }

    private void deleteAllData(SQLiteDatabase db) {
        db.execSQL("delete from GrowthLog");
    }

    private void deleteItemData(SQLiteDatabase db, int id) {
        int position = 0;
        db.execSQL("delete from GrowthLog where _id = " + id);
        for (int i = 0; i < parentList.size(); i++) {
            if (parentList.get(i) instanceof Record) {
                Record record = (Record) parentList.get(i);
                if (record.getId() == id) {
                    position = i;
                }

            }
        }
        parentList.remove(position);
        adapter.notifyItemRemoved(position);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == EDIT_NEW_DIARY) {
                queryAllData(dbHelper.getReadableDatabase());
                adapter.notifyDataSetChanged();
            }
        }
    }

    private void getDataFromSharePR() {
        SharedPreferences sharedPreferences = getSharedPreferences("Account", MODE_PRIVATE);
        userId = sharedPreferences.getInt("USER_ID", 0);
        email = sharedPreferences.getString("USER_EMAIL", "null");
        nickName = sharedPreferences.getString("USER_NICK_NAME", "null");
        header = sharedPreferences.getString("USER_HEADER", "null");
    }

    private void initSearchView() {

    }


}
