package com.sys.ldk;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.sys.ldk.accessibility.api.User;
import com.sys.ldk.accessibility.util.ApiUtil;
import com.sys.ldk.clock.AddclockActivity;
import com.sys.ldk.clock.ClockBean;
import com.sys.ldk.clock.ClockService;
import com.sys.ldk.clock.MyDatabaseHelper;
import com.sys.ldk.clock.MySimpleAdaptey;
import com.sys.ldk.clock.ServiceUtil;
import com.sys.ldk.serverset.MainServer;
import com.sys.ldk.serverset.MessengerService;
import com.sys.ldk.serverset.Permission;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ListView clockItmesList;
    private MySimpleAdaptey mySimpleAdaptey;
    private MyDatabaseHelper myDatabaseHelper;
    private SQLiteDatabase db;
    private List<ClockBean> clockBeanList = new ArrayList<>();
    private ImageView addclock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getSupportActionBar().hide();//隐藏ActionBar
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);//透明化通知栏
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        setContentView(R.layout.activity_clock);
//        启动服务
        Intent intent = new Intent(MainActivity.this, MainServer.class);
        startService(intent);

        FloatingWindow floatingWindow = new FloatingWindow();
        floatingWindow.chekPermission();
//        naozhongserver();
//        fuzhuserer();

        /*Intent in = new Intent(this, MessengerService.class);
        startService(in);*/

        myDatabaseHelper = new MyDatabaseHelper(getApplicationContext(), "Items.db", null, 1);
        db = myDatabaseHelper.getWritableDatabase();
        clockItmesList = findViewById(R.id.clockItmesList);
        initDatabase();
        mySimpleAdaptey = new MySimpleAdaptey(getApplicationContext(), clockBeanList);
        clockItmesList.setAdapter(mySimpleAdaptey);

        addclock = findViewById(R.id.addclock);
        addclock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddclockActivity.class);
                startActivity(intent);
                finish();
            }
        });

        clockItmesList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog alertDialog;
                alertDialog = new AlertDialog.Builder(MainActivity.this)   //提示信息
                        .setTitle("您确定删除吗?")   //标题
                        .setNegativeButton("取消", null)    //取消按钮
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (deleteItem(clockBeanList.get(position)) == 1) {
                                    Toast.makeText(getApplicationContext(), "删除成功", Toast.LENGTH_SHORT).show();
                                }
                                dialog.dismiss();  //对话框消失
                            }
                        })
                        .create();  //创建
                alertDialog.show();  //显示
                return true;
            }
        });
        Intent jumpType = getIntent();
        if (jumpType.getStringExtra("addType") != null) {
            if (jumpType.getStringExtra("addType").equals("yes")) {
                initDatabase();
                mySimpleAdaptey.notifyDataSetChanged();
                stopService(new Intent(MainActivity.this, ClockService.class));
                startService(new Intent(MainActivity.this, ClockService.class).putExtra("flag", "MainActivity"));
            } else if (jumpType.getStringExtra("addType").equals("cancel")) {

            }
        }
    }

    public void fuzhuserer() {
        if (!ApiUtil.isAccessibilityServiceOn(this, MainAccessService.class)) {
            User.authority_alerdialog(this);
        }
    }

    public void naozhongserver() {
        if (ServiceUtil.isRunning(this, "com.sys.ldk.clock.ClockService")) {
        } else {
            startService(new Intent(this, ClockService.class).putExtra("flag", "MainActivity"));
            Toast.makeText(this, "开启服务", Toast.LENGTH_SHORT).show();
        }
    }

    private int deleteItem(ClockBean clockBean) {
        db.delete("clocks", "id=?", new String[]{clockBean.getId() + ""});
        initDatabase();
        mySimpleAdaptey.notifyDataSetChanged();
        return 1;
    }

    private void initDatabase() {
        clockBeanList.clear();
        Cursor cursor = db.query("clocks", null, null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            ClockBean clockBean = new ClockBean(
                    cursor.getInt(cursor.getColumnIndex("id")),
                    cursor.getString(cursor.getColumnIndex("time")),
                    cursor.getString(cursor.getColumnIndex("repeat")),
                    cursor.getInt(cursor.getColumnIndex("isSwitchOn")),
                    cursor.getString(cursor.getColumnIndex("apptype")),
                    cursor.getString(cursor.getColumnIndex("create_time"))
            );
            clockBeanList.add(clockBean);
            Log.i("MainActivity", clockBeanList.toString());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        initDatabase();
        mySimpleAdaptey.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.root_test:
                show_root();
                return true;
            case R.id.shell:
                startActivity(new Intent(this, ShellActivity.class));
                return true;
            case R.id.test:
                startActivity(new Intent(this, MainActivityTest.class));
                return true;
            case R.id.setverset:
                startActivity(new Intent(this, Permission.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void show_root() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                new SocketClient("###AreYouOK", new SocketClient.onServiceSend() {
                    @Override
                    public void getSend(String result) {
                        showTextOnTextView(result);
                    }
                });
            }
        }).start();
    }

    private void showTextOnTextView(final String text) {
        MainActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (text.equals("###IamOK#")) {
                    Toast.makeText(MainActivity.this, "以提权", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "未提权", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
