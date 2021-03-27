package com.sys.ldk.dg;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.sys.ldk.MainActivity;
import com.sys.ldk.R;
import com.sys.ldk.easyfloat.EasyFloat;
import com.sys.ldk.sdcard.SaveLog;

import java.io.File;

public class ActicityConfig extends AppCompatActivity {
    private EditText edit_read;
    private EditText edit_video;

    @SuppressLint("StaticFieldLeak")
    private EditText edit_log;
    private EditText edit_read_times;
    private EditText edit_video_times;
    private EditText edit_duan_video;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dg_config);
        initView();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish(); // back button
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onResume() {
        super.onResume();
        if (LdkConfig.getRead_time_Mill() >= 1000 * 60) {
            int n = LdkConfig.getRead_time_second();
            edit_read.setText((int) n / 60 + "");
        } else {
            int m = LdkConfig.getRead_time_second();
            edit_read.setText((int) m + "秒");
        }
        if (LdkConfig.getVideo_time_Mill() >= 1000 * 60) {
            int n = LdkConfig.getVideo_time_second();
            edit_video.setText((int) n / 60 + "");
        } else {
            int m = LdkConfig.getVideo_time_second();
            edit_video.setText((int) m + "秒");
        }

        edit_read_times.setText(LdkConfig.getReading_times() + "");
        edit_video_times.setText(LdkConfig.getVideoing_times() + "");

        edit_duan_video.setText(LdkConfig.getDuan_video_time_second() + "");
        submit();
    }

    private void initView() {
        edit_read_times = (EditText) findViewById(R.id.edit_read_times);
        edit_video_times = (EditText) findViewById(R.id.edit_video_times);
        edit_duan_video = (EditText) findViewById(R.id.edit_duan_video_time);

        edit_read = (EditText) findViewById(R.id.edit_read);

        edit_video = (EditText) findViewById(R.id.edit_video);

        edit_log = (EditText) findViewById(R.id.edit_log);


        edit_log.setFocusable(false);
        edit_log.setFocusableInTouchMode(false);

        @SuppressLint("UseSwitchCompatOrMaterialCode") Switch sw_xin_wen = (Switch) findViewById(R.id.sw_xin_wen);

        sw_xin_wen.setChecked(LdkConfig.isXin_wen_lian_bo());
        sw_xin_wen.setOnCheckedChangeListener((buttonView, isChecked) -> {
            LdkConfig.setXin_wen_lian_bo(isChecked);
            if (isChecked) {
                Toast.makeText(ActicityConfig.this, "打开新闻视频", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(ActicityConfig.this, "关闭新闻视频", Toast.LENGTH_SHORT).show();
            }
        });

        @SuppressLint("UseSwitchCompatOrMaterialCode") Switch sw_save_log = findViewById(R.id.sw_save_log);
        sw_save_log.setChecked(LdkConfig.isSave());
        sw_save_log.setOnCheckedChangeListener((buttonView, isChecked) -> {
            LdkConfig.setSave(isChecked);
            if (isChecked) {
                Toast.makeText(ActicityConfig.this, "保存日志", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(ActicityConfig.this, "取消保存", Toast.LENGTH_SHORT).show();
            }
        });

        @SuppressLint("UseSwitchCompatOrMaterialCode") Switch sw_is_duan_video = findViewById(R.id.sw_is_duan_video);
        sw_is_duan_video.setChecked(LdkConfig.isDuan_video());
        sw_is_duan_video.setOnCheckedChangeListener((buttonView, isChecked) -> {
            LdkConfig.setDuan_video(isChecked);
            if (isChecked) {
                Toast.makeText(ActicityConfig.this, "打开短视频", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(ActicityConfig.this, "关闭短视频", Toast.LENGTH_SHORT).show();
            }
        });

        @SuppressLint("UseSwitchCompatOrMaterialCode") Switch sw_is_dati = findViewById(R.id.sw_dati);
        sw_is_dati.setChecked(LdkConfig.isDati());
        sw_is_dati.setOnCheckedChangeListener(((buttonView, isChecked) -> {
            LdkConfig.setDati(isChecked);
            if (isChecked) {
                Toast.makeText(ActicityConfig.this, "打开答题", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(ActicityConfig.this, "关闭答题", Toast.LENGTH_SHORT).show();
            }
        }));

        Button btn_log = findViewById(R.id.btn_log);
        btn_log.setOnClickListener(v -> openlog());

        Button btn_read_times = findViewById(R.id.btn_save);
        btn_read_times.setOnClickListener(v -> {
            save_all();
        });

        @SuppressLint("UseSwitchCompatOrMaterialCode") Switch xufuchuang = findViewById(R.id.sw_float);
        xufuchuang.setChecked(EasyFloat.appFloatIsShow("1"));
        xufuchuang.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                EasyFloat.showAppFloat("1");
                Toast.makeText(this, "打开悬浮窗", Toast.LENGTH_SHORT).show();
            } else {
                EasyFloat.hideAppFloat("1");
                Toast.makeText(this, "关闭悬浮窗", Toast.LENGTH_SHORT).show();
            }
        });

        @SuppressLint("UseSwitchCompatOrMaterialCode") Switch read = (Switch) findViewById(R.id.sw_read);
        read.setChecked(LdkConfig.isRead());
        read.setOnCheckedChangeListener(((buttonView, isChecked) -> {
            if (isChecked) {
                LdkConfig.setRead(true);
                Toast.makeText(this, "阅读文章", Toast.LENGTH_SHORT).show();
            } else {
                LdkConfig.setRead(false);
                Toast.makeText(this, "取消阅读文章", Toast.LENGTH_SHORT).show();
            }
        }));

        @SuppressLint("UseSwitchCompatOrMaterialCode") Switch video = findViewById(R.id.sw_video);
        video.setChecked(LdkConfig.isVideo());
        video.setOnCheckedChangeListener(((buttonView, isChecked) -> {
            if(isChecked){
                LdkConfig.setVideo(true);
                Toast.makeText(this, "打开第一视频", Toast.LENGTH_SHORT).show();
            }else {
                LdkConfig.setVideo(false);
                Toast.makeText(this, "关闭第一视频", Toast.LENGTH_SHORT).show();
            }
        }));

        @SuppressLint("UseSwitchCompatOrMaterialCode") Switch sichuan = findViewById(R.id.sw_sichuan);
        sichuan.setChecked(LdkConfig.isSichuan());
        sichuan.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked){
                LdkConfig.setSichuan(true);
                Toast.makeText(this, "打开四川频道", Toast.LENGTH_SHORT).show();
            }else {
                LdkConfig.setSichuan(false);
                Toast.makeText(this, "关闭四川频道", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void save_all() {
        save_read_time();
        save_video_time();
        String s = edit_read_times.getText().toString();
        int i = Integer.parseInt(s);
        LdkConfig.setReading_times(i);

        String m = edit_video_times.getText().toString();
        int i1 = Integer.parseInt(m);
        LdkConfig.setVideoing_times(i1);

        String s2 = edit_duan_video.getText().toString();
        int i2 = Integer.parseInt(s2);
        LdkConfig.setDuan_video_time(i2);
        Toast.makeText(this, "保存成功", Toast.LENGTH_SHORT).show();
    }

    private void openlog() {
        File sdCardFile = Environment.getExternalStorageDirectory();
        File file = new File(sdCardFile + "/LDK");
        if (!file.exists()) {
            return;
        }

        Uri contentUri = FileProvider.getUriForFile(MainActivity.getMycontext(), ".fileprovider", file);
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(contentUri, "*/*");
        startActivity(intent);
    }

    private void save_read_time() {
        int read_time = 0;
        String str = edit_read.getText().toString();
        if (!isNumber(str)) {
            Toast.makeText(this, "请输入整数数字", Toast.LENGTH_SHORT).show();
            return;
        }
//        分钟
        read_time = Integer.parseInt(str);
//        毫秒
        LdkConfig.setRead_time(read_time * 60 * 1000);
        if (read_time >= 1000) {
            LdkConfig.setRead_time(read_time);
        }
    }

    private void save_video_time() {
        int video_time = 0;
        String str = edit_video.getText().toString();
        if (!isNumber(str)) {
            Toast.makeText(this, "请输入整数数字", Toast.LENGTH_SHORT).show();
            return;
        }
        video_time = Integer.parseInt(str);
        LdkConfig.setVideo_time(video_time * 60 * 1000);
        if (video_time >= 1000) {
            LdkConfig.setVideo_time(video_time);
        }
    }

    /**
     * 判断字符串是否是数字
     */
    public static boolean isNumber(String value) {
        return isInteger(value) || isDouble(value);
    }

    /**
     * 判断字符串是否是整数
     */
    public static boolean isInteger(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * 判断字符串是否是浮点数
     */
    public static boolean isDouble(String value) {
        try {
            Double.parseDouble(value);
            if (value.contains("."))
                return true;
            return false;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private void submit() {
        String log_text = null;
        log_text = SaveLog.getLog(SaveLog.getSave_file_name());
        if (log_text == null) {
            return;
        }
        edit_log.setText(log_text);
    }
}
