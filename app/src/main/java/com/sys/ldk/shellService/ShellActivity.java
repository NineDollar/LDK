package com.sys.ldk.shellService;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.sys.ldk.R;

public class ShellActivity extends AppCompatActivity {
    public TextView mOutputTv;
    private Button mRunShellBtn;
    private EditText mCmdInputEt;
    public TextView getmOutputTv() {
        return mOutputTv;
    }
    public void setmOutputTv(TextView mOutputTv) {
        this.mOutputTv = mOutputTv;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shell);

        mOutputTv = findViewById(R.id.tv_output);
        mRunShellBtn = findViewById(R.id.btn_execute);
        mCmdInputEt = findViewById(R.id.et_enter);

        mRunShellBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cmd = mCmdInputEt.getText().toString();
                if (TextUtils.isEmpty(cmd)) {
                    Toast.makeText(ShellActivity.this, "输入内容为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                runShell(cmd);
            }
        });
    }

    private void runShell(final String cmd) {
        if (TextUtils.isEmpty(cmd)) return;
        new Thread(new Runnable() {
            @Override
            public void run() {
                new SocketClient(cmd, new SocketClient.onServiceSend() {
                    @Override
                    public void getSend(String result) {
                        showTextOnTextView(result);
                    }
                });
            }
        }).start();
    }

    private void showTextOnTextView(final String text) {
        ShellActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (TextUtils.isEmpty(mOutputTv.getText())) {
                    mOutputTv.setText(text);
                } else {
                    mOutputTv.setText(mOutputTv.getText() + "\n" + text);
                }
            }
        });
    }

}