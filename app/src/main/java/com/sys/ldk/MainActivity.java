package com.sys.ldk;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.sys.ldk.http.Http;
import com.sys.ldk.accessibility.util.LogUtil;
import com.sys.ldk.clock.ClockBean;
import com.sys.ldk.clock.ClockService;
import com.sys.ldk.clock.MyDatabaseHelper;
import com.sys.ldk.clock.MySimpleAdaptey;
import com.sys.ldk.dg.SandTimer;
import com.sys.ldk.easyfloat.EasyFloat;
import com.sys.ldk.qqlogin.LoginActivity;
import com.sys.ldk.serverset.MainService;
import com.sys.ldk.serverset.Permission;
import com.sys.ldk.dg.ActicityConfig;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    @SuppressLint("StaticFieldLeak")
    private static Context mcontext;
    private MySimpleAdaptey mySimpleAdaptey;
    private SQLiteDatabase db;
    private final List<ClockBean> clockBeanList = new ArrayList<>();
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private Switch xufuchuang;
    private final LinkedList activityList = new LinkedList();
    private int passwordsCount = 3;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mcontext = this;

        SandTimer sandTimer = new SandTimer();
        sandTimer.timerRun();

        SharedPreferences sp = getSharedPreferences("data", MODE_PRIVATE);
        String local_password = sp.getString("password", "");
        password = Http.get_http("http://www.songyun.work/ldk/ldk.php");

        if (!local_password.equals(password)) {
            LogUtil.V("开始验证");
            Init();
        } else {
            LogUtil.V("不需要验证");
            requestMyPermissions();
//        启动服务
            Intent intent = new Intent(MainActivity.this, MainService.class);
            startService(intent);
        }

//        requestMyPermissions();

//        启动服务
      /*  Intent intent = new Intent(MainActivity.this, MainService.class);
        startService(intent);*/

        /*new Thread(() -> {
            LogUtil.D("启动悬浮窗");
            FloatingWindow floatingWindow = new FloatingWindow();
            floatingWindow.chekPermission();
        }).start();*/

//        naozhongserver();
//        fuzhuserer();

        /*Intent in = new Intent(this, MessengerService.class);
        startService(in);*/

        MyDatabaseHelper myDatabaseHelper = new MyDatabaseHelper(getApplicationContext(), "Items.db", null, 1);
        db = myDatabaseHelper.getWritableDatabase();
        ListView clockItmesList = findViewById(R.id.clockItmesList);
        initDatabase();
        mySimpleAdaptey = new MySimpleAdaptey(getApplicationContext(), clockBeanList);
        clockItmesList.setAdapter(mySimpleAdaptey);

        ImageView addclock = findViewById(R.id.addclock);
        addclock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* Intent intent = new Intent(MainActivity.this, AddclockActivity.class);
                startActivity(intent);
                finish();*/

                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        clockItmesList.setOnItemLongClickListener((parent, view, position, id) -> {
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

        xufuchuang = findViewById(R.id.sw_float);
        xufuchuang.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                EasyFloat.showAppFloat("1");
            } else {
                EasyFloat.hideAppFloat("1");
            }
        });
    }

    private void setpassword() {
        SharedPreferences sp = getSharedPreferences("data", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("password", password);
        if (editor.commit()) {
            LogUtil.V("保存成功");
        }
    }

    private void Init() {
        if (password.equals("0")) {
            Toast.makeText(mcontext, "错误", Toast.LENGTH_LONG).show();
            new Thread(() -> {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                exit();
            }).start();
        } else {
            AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
            final EditText editText = new EditText(MainActivity.this);
            editText.setTransformationMethod(new PasswordTransformationMethod());
            dialog.setTitle("请输入邀请码");
            dialog.setView(editText);
            dialog.setCancelable(false);
            dialog.setPositiveButton("确定", (dialog1, which) -> {
                String m = editText.getText().toString();
                if (m.equals(password)) {
                    Toast.makeText(MainActivity.this, "验证成功", Toast.LENGTH_SHORT).show();
//                    存储
                    requestMyPermissions();
//        启动服务
                    Intent intent = new Intent(MainActivity.this, MainService.class);
                    startService(intent);
                    setpassword();
                } else {
                    if (passwordsCount <= 1) {
                        exit();
                    }
                    passwordsCount--;
                    Toast.makeText(MainActivity.this, "你还有 " + passwordsCount + " 次机会", Toast.LENGTH_SHORT).show();
                    Init();
                }
            });
            dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    exit();
                }
            });
            dialog.show();
        }
    }

    public void exit() {
        for (Object act : activityList) {
            act.notifyAll();
        }
        System.exit(0);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initDatabase();
        mySimpleAdaptey.notifyDataSetChanged();
        xufuchuang.setChecked(EasyFloat.appFloatIsShow("1"));
//        xufuchuang.setChecked(EasyFloat.appFloatIsShow());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }

    public static Context getMycontext() {
        return mcontext;
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            /*case R.id.root_test:
                Checkshell.show_root(this);
                return true;*/
            /*case R.id.shell:
                startActivity(new Intent(this, ShellActivity.class));
                return true;*/
            case R.id.activity_config:
                startActivity(new Intent(this, ActicityConfig.class));
                return true;
            case R.id.setverset:
                startActivity(new Intent(this, Permission.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void requestMyPermissions() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            //没有授权，编写申请权限代码
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
        } else {
            Log.d("TAG", "requestMyPermissions: 有写SD权限");
        }
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            //没有授权，编写申请权限代码
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
        } else {
            Log.d("TAG", "requestMyPermissions: 有读SD权限");
        }
    }

}
