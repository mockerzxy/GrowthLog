package com.example.xueyuanzhang.growthlog.ui.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xueyuanzhang.growthlog.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by xueyuanzhang on 16/7/10.
 */
public class ActivityProfile extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.username_bar)
    LinearLayout userNameBar;
    @BindView(R.id.nickname_bar)
    LinearLayout nickNameBar;
    @BindView(R.id.email_bar)
    LinearLayout emailBar;
    @BindView(R.id.birth_bar)
    LinearLayout birthBar;
    @BindView(R.id.sex_bar)
    LinearLayout sexBar;
    @BindView(R.id.username_profile)
    TextView userNameTV;
    @BindView(R.id.nickname_profile)
    TextView nickNameTV;
    @BindView(R.id.email_profile)
    TextView emailTV;
    @BindView(R.id.birth_profile)
    TextView birthTV;
    @BindView(R.id.sex_profile)
    TextView sexTV;

    private String userName;
    private String nickName;
    private String email;
    private String birth;
    private String sex;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getDataFromSharePR();
        initView();
    }

    private void initView() {
        ButterKnife.bind(this);
        initToolbar();
        userNameTV.setText(userName);
        nickNameTV.setText(nickName);
        emailTV.setText(email);
        birthTV.setText(birth);
        sexTV.setText(sex);
        initLayoutClick();
    }

    private void initToolbar() {
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setTitle("个人信息");
        toolbar.setTitleTextColor(getResources().getColor(R.color.lightBlack));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void getDataFromSharePR() {
        SharedPreferences sharedPreferences = getSharedPreferences("Account", MODE_PRIVATE);
        userName = sharedPreferences.getString("USER_NAME", "null");
        nickName = sharedPreferences.getString("USER_NICK_NAME", "null");
        email = sharedPreferences.getString("USER_EMAIL", "null");
        birth = sharedPreferences.getString("USER_BIRTH", "null");
        sex = sharedPreferences.getString("USER_SEX", "null");
    }

    private void initLayoutClick() {
        nickNameBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initInputDialog("修改昵称");
            }
        });

        birthBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initDatePickerdialog();
            }
        });

        emailBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initInputDialog("修改邮箱");
            }
        });

        sexBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initSelectDialog();
            }
        });
    }

    private void initInputDialog(final String flags) {
        final EditText editText = new EditText(ActivityProfile.this);
        editText.setHint(nickName);
        final AlertDialog.Builder builder = new AlertDialog.Builder(ActivityProfile.this);
        builder.setTitle(flags)
                .setView(editText)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(flags.equals("修改昵称")){
                            nickNameTV.setText(editText.getText());
                        }
                        if(flags.equals("修改邮箱")){
                            emailTV.setText(editText.getText());
                        }
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setIcon(R.drawable.ic_mode_edit_24dp)
                .setCancelable(true)
                .show();

    }

    private void initSelectDialog() {

        new AlertDialog.Builder(ActivityProfile.this)
                .setTitle("选择性别")
                .setSingleChoiceItems(new String[]{"男", "女"}, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            sexTV.setText("男");
                            dialog.dismiss();
                        }
                        if (which == 1) {
                            sexTV.setText("女");
                            dialog.dismiss();
                        }
                    }
                })
                .setCancelable(true)
                .show();
    }

    private void initDatePickerdialog() {
        Date oldDate = new Date(birth);
        Log.i("OLDD",new SimpleDateFormat("yyyy-MM-dd").format(oldDate));

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Date date = new Date(year-1900,monthOfYear,dayOfMonth);
                String birth = new SimpleDateFormat("yyyy/MM/dd").format(date);
                birthTV.setText(birth);
                Log.i("TIMEQ",birth);


            }
        }, oldDate.getYear()+1900, oldDate.getMonth(), oldDate.getDay()+10);
        datePickerDialog.show();
    }
}
