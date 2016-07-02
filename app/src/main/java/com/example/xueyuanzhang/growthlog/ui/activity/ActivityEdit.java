package com.example.xueyuanzhang.growthlog.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.xueyuanzhang.growthlog.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by xueyuanzhang on 16/7/1.
 */
public class ActivityEdit extends AppCompatActivity {
    public static final int SELECT_PIC = 1;
    public static final int TAKE_PIC = 2;
    private List<String> picPath = new ArrayList<>();

    @BindView(R.id.editText)
    EditText editText;
    @BindView(R.id.centerLayout)
    RelativeLayout relativeLayout;
    @BindView(R.id.selectPicButton)
    ImageButton selectPicButton;
    @BindView(R.id.takePicButton)
    ImageButton takePicButton;
    @BindView(R.id.imageViewHolder)
    GridLayout imageViewHolder;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private List<ImageView> imageViewList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        initView();
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.setFocusable(true);
                editText.requestFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);

            }
        });
        selectPicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/s/*");
                intent.setAction(intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, SELECT_PIC);
            }
        });
        takePicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, TAKE_PIC);
            }
        });

    }

    private void initView() {
        ButterKnife.bind(this);
        initToolbar();
    }
    private void initToolbar(){
        toolbar.setNavigationIcon(R.drawable.ic_clear_black_24dp);
        toolbar.setTitle("Record");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        toolbar.inflateMenu(R.menu.menu_edit);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            doPhoto(requestCode, data);
        }
    }

    private void doPhoto(int requestCode, Intent data) {
        if (requestCode == SELECT_PIC) {
            if (data == null) {
                Toast.makeText(getApplicationContext(), "选择文件图片出错", Toast.LENGTH_SHORT).show();
            } else {
                Uri uri = data.getData();
                if (uri == null) {
                    Toast.makeText(getApplicationContext(), "选择文件图片出错", Toast.LENGTH_SHORT).show();
                } else {
                    String[] po = {MediaStore.Images.Media.DATA};
                    Cursor cursor = this.managedQuery(uri, po, null, null, null);
                    if (cursor != null) {
                        int columnIndex = cursor.getColumnIndexOrThrow(po[0]);
                        cursor.moveToFirst();
                        picPath.add(cursor.getString(columnIndex));
                        if (Integer.parseInt(Build.VERSION.SDK) < 14) {
                            cursor.close();
                        }
                    }
                    Log.i("imageTag", picPath.get(picPath.size() - 1));
                    final ImageView imageView = (ImageView) LayoutInflater.from(this).inflate(R.layout.view_imageitem, imageViewHolder, false);
                    File file = new File(picPath.get(picPath.size() - 1));
                    Picasso.with(this).load(file).resize(300, 300).centerCrop().into(imageView);
                    imageViewHolder.addView(imageView);
                    imageView.setTag(picPath.size() - 1);
                    imageView.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            for (int i=0;i<imageViewList.size();i++) {
                                ImageView view = imageViewList.get(i);
                                view.setTag(i);
                            }
                            Log.i("picNUM", v.getTag() + "");
                            v.setVisibility(View.GONE);
                            imageViewHolder.removeView(v);
                            imageViewList.remove((int)v.getTag());
                            picPath.remove((int) v.getTag());

                            return true;
                        }
                    });
                    imageViewList.add(imageView);
                }

            }
        } else if (requestCode == TAKE_PIC) {

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
