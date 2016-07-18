package lock.lntelligence.gjx.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.aiton.administrator.shane_library.shane.ZjbBaseActivity;
import com.aiton.administrator.shane_library.shane.utils.ACache;
import com.aiton.administrator.shane_library.shane.utils.GsonUtils;
import com.aiton.administrator.shane_library.shane.utils.HTTPUtils;
import com.aiton.administrator.shane_library.shane.utils.Installation;
import com.aiton.administrator.shane_library.shane.utils.IsMobileNOorPassword;
import com.aiton.administrator.shane_library.shane.utils.VolleyListener;
import com.android.volley.VolleyError;

import java.util.HashMap;
import java.util.Map;

import lock.lntelligence.gjx.R;
import lock.lntelligence.gjx.constant.Constant;
import lock.lntelligence.gjx.model.Sms;
import lock.lntelligence.gjx.model.User;
import lock.lntelligence.gjx.utils.SmsContent;

public class LoginActivity extends ZjbBaseActivity implements View.OnClickListener {

    private EditText mEditText_phone_sms;
    private EditText mEditText_sms;
    private TextView mTextView_getSms;
    private String mPhone_sms;
    private int[] mI;
    private Runnable mR;
    private String mSuijiMath;
    private User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(lock.lntelligence.gjx.R.layout.activity_main);
        init();
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void setListeners() {
        findViewById(R.id.button_login).setOnClickListener(this);
        mTextView_getSms.setOnClickListener(this);
        mEditText_phone_sms.addTextChangedListener(new MyPhoneNumTextWatcher());
    }

    @Override
    protected void initSP() {

    }

    @Override
    protected void initIntent() {

    }

    @Override
    protected void findID() {
        mEditText_phone_sms = (EditText) findViewById(R.id.editText_phone_sms);
        mEditText_sms = (EditText) findViewById(R.id.editText_sms);
        mTextView_getSms = (TextView) findViewById(R.id.textView_getSms);
    }

    class MyPhoneNumTextWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s.length() == 11) {
                mEditText_sms.requestFocus();
            } else {
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.textView_getSms:
                if (ContextCompat.checkSelfPermission(LoginActivity.this, android.Manifest.permission.READ_SMS)
                        != PackageManager.PERMISSION_GRANTED) {
                    //申请WRITE_EXTERNAL_STORAGE权限
                    ActivityCompat.requestPermissions(LoginActivity.this, new String[]{android.Manifest.permission.READ_SMS},
                            Constant.PERMISSION.PERMISSION_READ_SMS);
                    Log.e("onClick", "请求权限");
                } else {
                    SmsContent smsContent = new SmsContent(LoginActivity.this, new Handler(), mEditText_sms);
                    this.getContentResolver().registerContentObserver(Uri.parse("content://sms/"), true, smsContent);
                    sendSMS();
                }
                break;
            case R.id.button_login:
                Log.e("onClick", "随机数"+mSuijiMath);
                if (mSuijiMath==null) {
                    toast("请先发送短信");
                } else {
                    Log.e("", "登录");
                    login(mPhone_sms);
                }
                break;
        }
    }

    /**
     * 登录接口
     *
     * @param phone
     */
    private void login(final String phone) {
        String loginCode = mEditText_sms.getText().toString().trim();
        if (mSuijiMath.equals(loginCode)) {
            showLoadingDialog("正在登陆……");
            toast("短信验证成功");
            //每次存储唯一标识
            final String DeviceId = Installation.id(LoginActivity.this);
            //向后台服务推送用户短信验证成功，发送手机号----start----//
            //取出channaleID
            String url = Constant.Url.LOGIN;
            Map<String, String> map = new HashMap<>();
            map.put("phone", phone + "");
            map.put("login_id", DeviceId + "");
            HTTPUtils.post(LoginActivity.this, url, map, new VolleyListener() {
                public void onErrorResponse(VolleyError volleyError) {
                }

                public void onResponse(String s) {
                    mUser = GsonUtils.parseJSON(s, User.class);
                    if (mUser.isSuccess()) {
                        ACache aCache = ACache.get(LoginActivity.this);
                        aCache.put(Constant.ACACHE.USER,mUser);
                        toast("登录成功");
                        startToLockAvtivity();
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(LoginActivity.this, mUser.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            });
        } else {
            toast("短信验证失败");
        }
    }

    private void startToLockAvtivity() {
        finish();
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setClass(LoginActivity.this, LockActivity.class);
        startActivity(intent);
    }

    /**
     * 权限请求操作
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Constant.PERMISSION.PERMISSION_READ_SMS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                SmsContent smsContent = new SmsContent(LoginActivity.this, new Handler(), mEditText_sms);
                // 注册短信变化监听
                this.getContentResolver().registerContentObserver(Uri.parse("content://sms/"), true, smsContent);
                sendSMS();
            } else {
                sendSMS();
            }
        }
    }

    /**
     * 发送短信操作
     */
    private void sendSMS() {
        Log.e("sendSMS", "发送短信");
        mPhone_sms = mEditText_phone_sms.getText().toString().trim();
        boolean mobileNO = IsMobileNOorPassword.isMobileNO(mPhone_sms);
        if (mobileNO) {
            mTextView_getSms.setEnabled(false);
            mI = new int[]{60};

            mR = new Runnable() {
                @Override
                public void run() {
                    mTextView_getSms.setText((mI[0]--) + "秒后重发");
                    if (mI[0] == 0) {
                        mTextView_getSms.setEnabled(true);
                        mTextView_getSms.setText("获取验证码");
                        return;
                    } else {
                    }
                    mTextView_getSms.postDelayed(mR, 1000);
                }
            };
            mTextView_getSms.postDelayed(mR, 0);
            getSms();
        } else {
            Toast.makeText(LoginActivity.this, "输入的手机格式有误", Toast.LENGTH_SHORT).show();
            mEditText_phone_sms.setText("");
        }
    }

    /**
     * 获取短信请求
     */
    private void getSms() {
        mSuijiMath = (int) (Math.random() * 9000 + 1000) + "";
        String url = Constant.Url.GET_SMS;
        Map<String, String> map = new HashMap<>();
        map.put("phone", mPhone_sms);
        map.put("code", mSuijiMath + "");
        HTTPUtils.post(LoginActivity.this, url, map, new VolleyListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                toast("短信发送失败");
            }

            @Override
            public void onResponse(String s) {
                toast("短信发送成功");
                try {
                    Sms sms = GsonUtils.parseJSON(s, Sms.class);
                    int status = sms.getStatus();
                    toast(sms.getMes());
                    if (status == 0) {
                        //短信发送成功
                    } else if (status == 1) {
                        //短信发送失败
                    }
                } catch (Exception e) {
                    toast("服务器出错");
                }
            }
        });
    }

    /**
     * 吐司
     */
    private void toast(final String str) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(LoginActivity.this, str, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
