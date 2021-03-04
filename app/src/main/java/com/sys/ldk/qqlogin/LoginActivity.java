package com.sys.ldk.qqlogin;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.sys.ldk.R;
import com.tencent.connect.UserInfo;
import com.tencent.connect.common.Constants;
import com.tencent.open.log.SLog;
import com.tencent.tauth.DefaultUiListener;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import static com.tencent.connect.common.Constants.KEY_SCOPE;

public class LoginActivity extends AppCompatActivity {
    private final String TAG = "Login";
    public static Tencent mTencent;
    private TextView textview_login;
    private Button but_login;
    private ImageView imageView_user;
    private Bitmap bitmap;
    private String nickname;

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();

        mTencent = Tencent.createInstance(AppConstants.APP_ID, this.getApplicationContext(), AppConstants.APP_AUTHORITIES);
        if (mTencent == null) {
            SLog.e(TAG, "Tencent instance create fail!");
            finish();
        }

        but_login.setOnClickListener(v -> {
            Log.d(TAG, "onCreate: but_login");
            qq_login();
        });
    }

    private void qq_login() {
        if (!mTencent.isSessionValid()) {
            HashMap<String, Object> params = new HashMap<String, Object>();
            params.put(KEY_SCOPE, "all");
            mTencent = Tencent.createInstance(AppConstants.APP_ID, this.getApplicationContext(), AppConstants.APP_AUTHORITIES);
            mTencent.login(this, loginListener, params);
        } else {
            mTencent.logout(this);
        }
    }


    IUiListener loginListener = new BaseUiListener() {
        @Override
        protected void doComplete(Object object) {
            super.doComplete(object);
//初始化aoken...
            initOpenidAndToken((JSONObject) object);
            updateUserInfo();
        }
    };

    private void updateUserInfo() {
//      获取设置用户信息
        if (mTencent != null && mTencent.isSessionValid()) {
            IUiListener listener = new DefaultUiListener() {
                @Override
                public void onComplete(final Object response) {
                    JSONObject json = (JSONObject) response;
                    new Thread(() -> {
                        if (json.has("figureurl")) {
                            try {
                                bitmap = Util.getbitmap(json.getString("figureurl_qq_2"));
                                Log.d(TAG, "onComplete: " + json.getString("figureurl_qq_1"));
                                if (bitmap == null) {
                                    Log.d(TAG, "onComplete: 头像空");
                                }
                                imageView_user.post(() -> imageView_user.setImageBitmap(bitmap));
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Log.e(TAG, "onComplete: " + e.toString());
                            }
                        } else {
                            Log.w(TAG, "onComplete: " + "没有图片");
                        }
                    }).start();
                    new Thread(()->{
                        if(json.has("nickname")){
                            textview_login.post(() -> {
                                try {
                                    textview_login.setText(json.getString("nickname"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            });
                        }else {
                            Log.w(TAG, "onComplete: 没有昵称" );
                        }
                    }).start();
                }
            };
            UserInfo info = new UserInfo(this, mTencent.getQQToken());
            info.getUserInfo(listener);
        }

    }

    public void initOpenidAndToken(JSONObject jsonObject) {
        try {
            String token = jsonObject.getString(Constants.PARAM_ACCESS_TOKEN);
            String expires = jsonObject.getString(Constants.PARAM_EXPIRES_IN);
            String openId = jsonObject.getString(Constants.PARAM_OPEN_ID);
            if (!TextUtils.isEmpty(token) && !TextUtils.isEmpty(expires)
                    && !TextUtils.isEmpty(openId)) {
                mTencent.setAccessToken(token, expires);
                mTencent.setOpenId(openId);
                Log.d(TAG, "initOpenidAndToken: 设置token...成功");
            }
        } catch (Exception e) {
            Log.e(TAG, "initOpenidAndToken: " + e.toString());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "-->onActivityResult " + requestCode + " resultCode=" + resultCode);
        if (requestCode == Constants.REQUEST_LOGIN ||
                requestCode == Constants.REQUEST_APPBAR) {
            Tencent.onActivityResultData(requestCode, resultCode, data, loginListener);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private class BaseUiListener extends DefaultUiListener {

        protected void doComplete(Object object) {

        }

        @Override
        public void onComplete(Object o) {
            //            登陆成功调用的地方
            Log.d(TAG, "doComplete: 登录成功");
            if (null == o) {
                Util.showResultDialog(LoginActivity.this, "返回为空", "登录失败");
                return;
            }
            JSONObject jsonResponse = (JSONObject) o;
            if (jsonResponse.length() == 0) {
                Util.showResultDialog(LoginActivity.this, "返回为空", "登录失败");
                return;
            }

            Toast.makeText(LoginActivity.this, "登陆成功", Toast.LENGTH_SHORT).show();
            doComplete(o);
        }

        @Override
        public void onError(UiError e) {
          /*  showResult("onError:", "code:" + e.errorCode + ", msg:"
                    + e.errorMessage + ", detail:" + e.errorDetail);*/
            Log.d(TAG, "onError: " + e.errorCode + ", msg:"
                    + e.errorMessage + ", detail:" + e.errorDetail);
        }

        @Override
        public void onCancel() {
//            showResult("onCancel", "");
            Log.d(TAG, "onCancel: ");
        }

        @Override
        public void onWarning(int i) {
            Log.d(TAG, "onWarning: ");
        }
    }

    private void initView() {
        textview_login = (TextView) findViewById(R.id.textview_login);
        but_login = (Button) findViewById(R.id.btn_login);
        imageView_user = (ImageView) findViewById(R.id.image_user);
    }
}
