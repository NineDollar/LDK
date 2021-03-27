package com.sys.ldk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.sys.ldk.accessibility.util.LogUtil;
import com.sys.ldk.serverset.MainService;

import org.jetbrains.annotations.NotNull;

public class ActivityJuDou extends AppCompatActivity {
    private EditText edit_address;
    private EditText edit_key;
    private Button btn_save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ju_dou);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        Init();
        btn_save.setOnClickListener(v -> {
            String address = edit_address.getText().toString();
            String key = edit_key.getText().toString();
            LogUtil.D("add:" + address);
            if (address.equals("") || key .equals("")) {
                Toast.makeText(this, "不能为空", Toast.LENGTH_SHORT).show();
                return;
            }
            MainConfig.setAddresses(address);
            MainConfig.setKey(key);
            Toast.makeText(this, "保存成功", Toast.LENGTH_SHORT).show();
            MainService.notification();
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull @NotNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish(); // back button
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void Init() {
        edit_address = findViewById(R.id.edit_address);
        edit_key = findViewById(R.id.edit_key);
        btn_save = findViewById(R.id.btn_all);
    }
}