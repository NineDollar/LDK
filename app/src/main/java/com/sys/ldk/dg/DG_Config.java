package com.sys.ldk.dg;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.sys.ldk.R;

public class DG_Config extends AppCompatActivity implements View.OnClickListener {
    private EditText edit_read;
    private Button btn_save_read;
    private EditText edit_video;
    private Button btn_save_video;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private Switch sw_xin_wen;
    public static EditText edit_log;

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
        edit_read.setText((int)Config.read_time/60/1000+"");
        edit_video.setText((int)Config.video_max/60/1000+"");
        sw_xin_wen.setChecked(Config.is_xin_wen_lian_bo);
    }

    private void initView() {
        edit_read = (EditText) findViewById(R.id.edit_read);
        btn_save_read = (Button) findViewById(R.id.btn_save_read);
        edit_video = (EditText) findViewById(R.id.edit_video);
        btn_save_video = (Button) findViewById(R.id.btn_save_video);
        sw_xin_wen = (Switch) findViewById(R.id.sw_xin_wen);
        edit_log = (EditText) findViewById(R.id.edit_log);

        btn_save_read.setOnClickListener(this);
        btn_save_video.setOnClickListener(this);

        edit_log.setFocusable(false);
        edit_log.setFocusableInTouchMode(false);

        sw_xin_wen.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Config.is_xin_wen_lian_bo = isChecked;
                if (isChecked) {
                    Toast.makeText(DG_Config.this, "打开", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(DG_Config.this, "关闭", Toast.LENGTH_SHORT).show();
                }
            }
        });
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
        Config.read_time = read_time * 60 * 1000;
        Toast.makeText(this, "保存成功：" + read_time, Toast.LENGTH_SHORT).show();
    }

    private void save_video_time() {
        int video_time = 0;
        String str = edit_video.getText().toString();
        if (!isNumber(str)) {
            Toast.makeText(this, "请输入整数数字", Toast.LENGTH_SHORT).show();
            return;
        }
        video_time = Integer.parseInt(str);
        Config.video_max = video_time * 60 * 1000;
        Toast.makeText(this, "保存成功：" + video_time, Toast.LENGTH_SHORT).show();
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
        String log = edit_log.getText().toString().trim();
        if (TextUtils.isEmpty(log)) {
            Toast.makeText(this, "日志", Toast.LENGTH_SHORT).show();
            return;
        }



    }
}
