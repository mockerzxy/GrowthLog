package com.example.xueyuanzhang.growthlog.ui.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.example.xueyuanzhang.growthlog.R;
import com.example.xueyuanzhang.growthlog.ui.adapter.ViewPagerAdapter;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by xueyuanzhang on 16/7/6.
 */
public class ActivityImageFlipper extends AppCompatActivity {
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;
    private List<String> pic_list = new ArrayList<>();
    private List<View> pic_view = new ArrayList<>();
    private int position;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_flipper);
        pic_list = getIntent().getStringArrayListExtra("PIC_LIST");
        position = getIntent().getIntExtra("POSITION",0);
        initView();
        ProgressDialog progressDialog =ProgressDialog.show(this,"","",true);
        addImageView();
        initViewPager();
        progressDialog.dismiss();
    }

    private void initView() {
        ButterKnife.bind(this);
    }

    private void addImageView() {
        for (int i = 0; i < pic_list.size(); i++) {
            View view = LayoutInflater.from(this).inflate(R.layout.view_image_match_parent, null);
            ImageView imageView = (ImageView) view.findViewById(R.id.image_view);
            File file = new File(pic_list.get(i));
            System.out.println(pic_list.get(i));
            Picasso.with(this).load(file).into(imageView);
            pic_view.add(view);
        }
    }

    private void initViewPager() {
        viewPagerAdapter = new ViewPagerAdapter(pic_view);
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setCurrentItem(position);

    }
}
