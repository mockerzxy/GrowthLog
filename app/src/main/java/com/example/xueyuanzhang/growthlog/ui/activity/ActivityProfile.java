package com.example.xueyuanzhang.growthlog.ui.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xueyuanzhang.growthlog.R;
import com.example.xueyuanzhang.growthlog.api.GrowthLogApi;
import com.example.xueyuanzhang.growthlog.model.IntResponse;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by xueyuanzhang on 16/7/10.
 */
public class ActivityProfile extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.header_bar)
    RelativeLayout headerBar;
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
    @BindView(R.id.header_profile)
    ImageView headerBT;
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
    @BindView(R.id.save_modify)
    Button saveButton;

    private String userName;
    private String nickName;
    private String email;
    private String birth;
    private String sex;
    private String password;
    private String header;

    private final static int SELECT_HEADER = 1;

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
        File file = new File(header);
        Picasso.with(this).load(file).into(headerBT);
        userNameTV.setText(userName);
        nickNameTV.setText(nickName);
        emailTV.setText(email);
        birthTV.setText(birth);
        sexTV.setText(sex);
        initLayoutClick();
        initButtonClick();
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
        password = sharedPreferences.getString("USER_PW", "null");
        header = sharedPreferences.getString("USER_HEADER", "null");

    }

    private void initLayoutClick() {
        headerBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/s/*");
                intent.setAction(intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, SELECT_HEADER);

            }
        });
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

    private void initButtonClick() {
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog progressDialog  = ProgressDialog.show(ActivityProfile.this,"","正在修改中，请稍后",false);
                Call<IntResponse> call = GrowthLogApi.getInstance().updateUser(userName, password, nickName, email, sex, birth);
                call.enqueue(new Callback<IntResponse>() {
                    @Override
                    public void onResponse(Call<IntResponse> call, Response<IntResponse> response) {
                        if (response.body() == null) {
                            Toast.makeText(ActivityProfile.this, "null", Toast.LENGTH_SHORT).show();
                        } else {
                            progressDialog.dismiss();
                            IntResponse intResponse = response.body();
                            switch (intResponse.getResult()){
                                case 0:
                                    Toast.makeText(getApplicationContext(), "修改失败", Toast.LENGTH_SHORT).show();
                                    break;
                                case 1:
                                    Toast.makeText(getApplicationContext(), "修改成功", Toast.LENGTH_SHORT).show();
                                    break;
                                default:
                                    Toast.makeText(getApplicationContext(), "系统错误", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<IntResponse> call, Throwable t) {
                        Toast.makeText(ActivityProfile.this, "连接异常，请检查网络", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void initInputDialog(final String flags) {
        final EditText editText = new EditText(ActivityProfile.this);
        if (flags.equals("修改昵称")) {
            editText.setHint(nickName);
        } else {
            editText.setHint(email);
        }
        final AlertDialog.Builder builder = new AlertDialog.Builder(ActivityProfile.this);
        builder.setTitle(flags)
                .setView(editText)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (flags.equals("修改昵称")) {
                            nickNameTV.setText(editText.getText());
                        }
                        if (flags.equals("修改邮箱")) {
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
        Calendar c = new GregorianCalendar();
        if (!birth.isEmpty()) {
            Date oldDate = new Date(birth);
            Log.i("CLD", oldDate.toString());
            c.setTime(oldDate);
        }
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, monthOfYear, dayOfMonth);
                Date date = calendar.getTime();
                String birth = new SimpleDateFormat("yyyy/MM/dd").format(date);
                birthTV.setText(birth);


            }
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_HEADER) {
                doPhoto(data);
            }
        }
    }

    private void doPhoto(Intent data) {
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
                    header = cursor.getString(columnIndex);
                    File file = new File(header);
                    Log.i("PATH", header);
                    Picasso.with(this).load(file).resize(48, 48).into(headerBT);
                    if (Integer.parseInt(Build.VERSION.SDK) < 14) {
                        cursor.close();
                    }
                }
            }

        }
    }
}
