package com.example.xueyuanzhang.growthlog.ui.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.xueyuanzhang.growthlog.R;
import com.example.xueyuanzhang.growthlog.model.Record;
import com.example.xueyuanzhang.growthlog.util.LocalDataBaseHelper;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
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
    private LocalDataBaseHelper dbHelper;
    private Record record;
    private int ifModify;
    private String capturePath;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        initView();
        dbHelper = new LocalDataBaseHelper(this, "GrowthLogDB.db3", 2);
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
                String path = Environment.getExternalStorageDirectory().toString()+"/photoForGrowthLog";
                File path1 = new File(path);
                if(!path1.exists()){
                    path1.mkdirs();
                }
                File file = new File(path1,System.currentTimeMillis()+".jpg");
                capturePath = file.getPath();
                Log.i("CAP",capturePath);
                Uri uri  = Uri.fromFile(file);
                intent.putExtra(MediaStore.EXTRA_OUTPUT,uri);
                intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, TAKE_PIC);
            }
        });

    }

    private void initView() {
        ButterKnife.bind(this);
        initToolbar();
        ifModify = getIntent().getIntExtra("ID",0);
        if(ifModify!=0){
            initModifyView();
        }
    }
    private void initModifyView(){
        record = new Record();
        record.setId(getIntent().getIntExtra("ID",0));
        if(getIntent().getStringExtra("TEXT")!=null){
            record.setText(getIntent().getStringExtra("TEXT"));
        }
        if(getIntent().getStringArrayListExtra("PIC_LIST")!=null){
            record.setPicList(getIntent().getStringArrayListExtra("PIC_LIST"));
        }
        editText.setText(record.getText());
        picPath.addAll(record.getPicList());
        addImageToHolder(false);

    }

    private void addImageToHolder(boolean ifAddOne){
        for(int i=0;i<picPath.size();i++) {
            ImageView imageView = (ImageView) LayoutInflater.from(this).inflate(R.layout.view_imageitem, imageViewHolder, false);
            File file;
            if(ifAddOne){
                file  = new File(picPath.get(picPath.size()-1));
            }
            else {
                file = new File(picPath.get(i));
            }
            Picasso.with(this).load(file).resize(300, 300).centerCrop().into(imageView);
            imageViewHolder.addView(imageView);
            imageViewList.add(imageView);
            imageView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    for (int i = 0; i < imageViewList.size(); i++) {
                        ImageView view = imageViewList.get(i);
                        view.setTag(i);
                    }
                    Log.i("picNUM", v.getTag() + "");
                    v.setVisibility(View.GONE);
                    imageViewHolder.removeView(v);
                    imageViewList.remove((int) v.getTag());
                    picPath.remove((int) v.getTag());

                    return true;
                }
            });
            if(ifAddOne){
                break;
            }
        }
    }

    private void initToolbar() {
        toolbar.setNavigationIcon(R.drawable.ic_clear_black_24dp);
        toolbar.setTitle("记录");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        toolbar.inflateMenu(R.menu.menu_edit);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.send) {
                    Log.i("merge", mergePicPath());
                    ProgressDialog dialog = ProgressDialog.show(ActivityEdit.this, "", "正在记录，请稍后", true);
                    if(ifModify==0) {
                        insertData(dbHelper.getReadableDatabase(), editText.getText().toString(), mergePicPath(), getTime());
                    }else{
                        modifyData(dbHelper.getReadableDatabase(),record.getId(),editText.getText().toString(),mergePicPath());
                    }
                    dialog.dismiss();
                    Toast.makeText(getApplicationContext(), "记录成功", Toast.LENGTH_SHORT).show();
                    ActivityEdit.this.setResult(RESULT_OK);
                    ActivityEdit.this.finish();
                }
                return true;
            }
        });
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
                    addImageToHolder(true);
//                    final ImageView imageView = (ImageView) LayoutInflater.from(this).inflate(R.layout.view_imageitem, imageViewHolder, false);
//                    File file = new File(picPath.get(picPath.size() - 1));
//                    Picasso.with(this).load(file).resize(300, 300).centerCrop().into(imageView);
//                    imageViewHolder.addView(imageView);
//                    imageView.setOnLongClickListener(new View.OnLongClickListener() {
//                        @Override
//                        public boolean onLongClick(View v) {
//                            for (int i = 0; i < imageViewList.size(); i++) {
//                                ImageView view = imageViewList.get(i);
//                                view.setTag(i);
//                            }
//                            Log.i("picNUM", v.getTag() + "");
//                            v.setVisibility(View.GONE);
//                            imageViewHolder.removeView(v);
//                            imageViewList.remove((int) v.getTag());
//                            picPath.remove((int) v.getTag());
//
//                            return true;
//                        }
//                    });
//                    imageViewList.add(imageView);
                }

            }
        } else if (requestCode == TAKE_PIC) {

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void insertData(SQLiteDatabase db, String text, String imagePath, String timeStamp) {
        db.execSQL("insert into GrowthLog values(null , ? , ? , ?)", new String[]{text, imagePath,timeStamp});
    }

    private void modifyData(SQLiteDatabase db,int id,String text,String imagePath){
        db.execSQL("update GrowthLog set text = '"+text+"', image = '"+imagePath+"' where _id = "+id);
    }

    private String mergePicPath() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < picPath.size(); i++) {
            String url = picPath.get(i);
            stringBuilder.append(url);
            if (i != picPath.size() - 1) {
                stringBuilder.append(";");
            }
        }
        return stringBuilder.toString();
    }

    private String getTime() {
        Calendar calendar = new GregorianCalendar();
        String timeStamp = calendar.getTime().toString();
        return timeStamp;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(capturePath!=null){
            String path = capturePath;
            outState.putString("capturePath",path);
//            ArrayList<String> picPathList= (ArrayList<String>)picPath;
//            outState.putStringArrayList("picPathList",picPathList);

        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if(savedInstanceState.getString("capturePath")!=null){
            picPath.add(savedInstanceState.getString("capturePath"));
            Log.i("imCount",imageViewList.size()+"");
            addImageToHolder(true);
        }
    }
}
