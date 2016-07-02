package com.example.xueyuanzhang.growthlog.ui.activity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.example.xueyuanzhang.growthlog.R;
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

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.fab)
    FloatingActionButton floatingActionButton;

    private Drawer drawer;
    private AccountHeader headerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,ActivityEdit.class);
                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(MainActivity.this).toBundle());
            }
        });
    }

    private void initView(){
        ButterKnife.bind(this);
        toolbar.inflateMenu(R.menu.menu_toobar);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int itemId = item.getItemId();
                if(itemId==R.id.share){

                }
                return false;
            }
        });
//        setSupportActionBar(toolbar);
        initAccountHeader();
        initDrawer();
    }

    private void initDrawer(){
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

    private void initAccountHeader(){
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


}
