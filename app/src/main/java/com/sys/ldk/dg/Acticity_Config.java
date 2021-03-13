package com.sys.ldk.dg;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.sys.ldk.MainActivity;
import com.sys.ldk.R;
import com.sys.ldk.accessibility.util.LogUtil;
import com.sys.ldk.sdcard.SaveLog;

import java.io.File;

public class Acticity_Config extends AppCompatActivity implements View.OnClickListener {
    private EditText edit_read;
    private EditText edit_video;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private Switch sw_xin_wen;
    @SuppressLint("StaticFieldLeak")
    private EditText edit_log;
    private EditText edit_read_times;
    private EditText edit_video_times;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dg_config);
        initView();
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onResume() {
        super.onResume();
        if (LdkConfig.getRead_time_Mill() >= 1000 * 60) {
            int n = LdkConfig.getRead_time_second();
            LogUtil.D("---" + n);
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

        submit();
    }

    private void initView() {
        edit_read_times = (EditText) findViewById(R.id.edit_read_times);
        edit_video_times = (EditText) findViewById(R.id.edit_video_times);

        edit_read = (EditText) findViewById(R.id.edit_read);
        Button btn_save_read = (Button) findViewById(R.id.btn_save_read);
        edit_video = (EditText) findViewById(R.id.edit_video);
        Button btn_save_video = (Button) findViewById(R.id.btn_save_video);
        edit_log = (EditText) findViewById(R.id.edit_log);

        btn_save_read.setOnClickListener(this);
        btn_save_video.setOnClickListener(this);

        edit_log.setFocusable(false);
        edit_log.setFocusableInTouchMode(false);

        sw_xin_wen = (Switch) findViewById(R.id.sw_xin_wen);
        @SuppressLint("UseSwitchCompatOrMaterialCode") Switch sw_save_log = (Switch) findViewById(R.id.sw_save_log);

        sw_xin_wen.setChecked(LdkConfig.isIs_xin_wen_lian_bo());
        sw_xin_wen.setOnCheckedChangeListener((buttonView, isChecked) -> {
            LdkConfig.setIs_xin_wen_lian_bo(isChecked);
            if (isChecked) {
                Toast.makeText(Acticity_Config.this, "打开", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(Acticity_Config.this, "关闭", Toast.LENGTH_SHORT).show();
            }
        });

        sw_save_log.setChecked(LdkConfig.isSave());
        sw_save_log.setOnCheckedChangeListener((buttonView, isChecked) -> {
            LdkConfig.setSave(isChecked);
            if (isChecked) {
                Toast.makeText(Acticity_Config.this, "保存日志", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(Acticity_Config.this, "取消保存", Toast.LENGTH_SHORT).show();
            }
        });

        Button btn_log = findViewById(R.id.btn_log);
        btn_log.setOnClickListener(v -> openlog());

        Button btn_read_times = findViewById(R.id.btn_read_times);
        btn_read_times.setOnClickListener(v -> {
            String s = edit_read_times.getText().toString();
            int i = Integer.parseInt(s);
            LdkConfig.setReading_times(i);
            Toast.makeText(this, "保存成功：" + i + " 次", Toast.LENGTH_SHORT).show();
        });

        Button btn_video_times = findViewById(R.id.btn_video_times);
        btn_video_times.setOnClickListener(v -> {
            String s = edit_video_times.getText().toString();
            int i = Integer.parseInt(s);
            LdkConfig.setVideoing_times(i);
            Toast.makeText(this, "保存成功：" + i + " 次", Toast.LENGTH_SHORT).show();
        });
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


    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_save_read:
                save_read_time();
                break;
            case R.id.btn_save_video:
                save_video_time();
                break;
        }
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
            Toast.makeText(this, "保存成功：" + (int) read_time / 1000 + " 秒", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "保存成功：" + read_time + " 分钟", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(this, "保存成功：" + (int) video_time / 1000 + " 秒", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "保存成功：" + video_time + " 分钟", Toast.LENGTH_SHORT).show();
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
        LogUtil.D("file_name：" + SaveLog.getSave_file_name());
        String log_text = null;
        log_text = SaveLog.getLog(SaveLog.getSave_file_name());
        if (log_text == null) {
            return;
        }
        edit_log.setText(log_text);
    }
}
