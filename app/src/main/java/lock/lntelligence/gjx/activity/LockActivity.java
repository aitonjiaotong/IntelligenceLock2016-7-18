package lock.lntelligence.gjx.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.aiton.administrator.shane_library.shane.ZjbBaseActivity;
import com.aiton.administrator.shane_library.shane.utils.ACache;
import com.aiton.administrator.shane_library.shane.widget.Lock9View;

import lock.lntelligence.gjx.R;
import lock.lntelligence.gjx.constant.Constant;

public class LockActivity extends ZjbBaseActivity implements View.OnClickListener {

    private Lock9View mLock9View;
    private TextView mTextView_tip;
    private String mPassword;
    private int paintCount = 0;
    private boolean isFrist = false;
    private String mPaintPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        intiPermission();
        init();
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
            intiPermission();
        }
    }

    private void intiPermission() {
        if (ContextCompat.checkSelfPermission(LockActivity.this, android.Manifest.permission.BLUETOOTH_ADMIN)
                != PackageManager.PERMISSION_GRANTED) {
            //申请WRITE_EXTERNAL_STORAGE权限
            ActivityCompat.requestPermissions(LockActivity.this, new String[]{android.Manifest.permission.BLUETOOTH_ADMIN},
                    Constant.PERMISSION.PERMISSION_BLUETOOTH_ADMIN);
        } else if (ContextCompat.checkSelfPermission(LockActivity.this, android.Manifest.permission.BLUETOOTH)
                != PackageManager.PERMISSION_GRANTED) {
            //申请WRITE_EXTERNAL_STORAGE权限
            ActivityCompat.requestPermissions(LockActivity.this, new String[]{android.Manifest.permission.BLUETOOTH},
                    Constant.PERMISSION.PERMISSION_BLUETOOTH);
        } else if (ContextCompat.checkSelfPermission(LockActivity.this, android.Manifest.permission.VIBRATE)
                != PackageManager.PERMISSION_GRANTED) {
            //申请WRITE_EXTERNAL_STORAGE权限
            ActivityCompat.requestPermissions(LockActivity.this, new String[]{android.Manifest.permission.VIBRATE},
                    Constant.PERMISSION.PERMISSION_VIBRATE);
        }
    }

    @Override
    protected void initData() {
        ACache aCache = ACache.get(LockActivity.this);
        mPaintPassword = aCache.getAsString(Constant.ACACHE.PAINT_PASSWORD);
        if (mPaintPassword==null) {
            isFrist = true;
        } else {
            isFrist = false;
        }
    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void setListeners() {
        mLock9View.setCallBack(new Lock9View.CallBack() {
            @Override
            public void onFinish(String password) {
                if (isFrist) {
                    if (password.length() >= 4 && password.length() <= 6) {
                        if (paintCount == 0) {
                            mPassword = password;
                            mTextView_tip.setText("请再次绘制");
                            paintCount++;
                        } else if (paintCount == 1) {
                            if (password.equals(mPassword)) {
                                Log.e("onFinish", "成功设置手势密码");
                                ACache aCache = ACache.get(LockActivity.this);
                                aCache.put(Constant.ACACHE.PAINT_PASSWORD, mPassword);
                                startToMainAvtivity();
                            } else {
                                paintCount = 0;
                                mTextView_tip.setText("两次不一样，重新绘制");
                            }
                        }

                    } else if (password.length() > 6) {
                        mTextView_tip.setText("连接的点过多，请重新绘制");
                    } else {
                        mTextView_tip.setText("请连接至少四个点");
                    }
                }else {
                    if (password.equals(mPaintPassword)){
                        startToMainAvtivity();
                    }else {
                        mTextView_tip.setText("错误，请重新绘制");
                    }
                }
            }
        });
        findViewById(R.id.textView_forget).setOnClickListener(this);
    }

    private void startToMainAvtivity() {
        finish();
        Intent intent = new Intent();
        intent.setClass(LockActivity.this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    protected void initSP() {

    }

    @Override
    protected void initIntent() {

    }

    @Override
    protected void findID() {
        mLock9View = (Lock9View) findViewById(R.id.lock9View);
        mTextView_tip = (TextView) findViewById(R.id.textView_tip);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.textView_forget:
                Toast.makeText(LockActivity.this,"待做(短信验证重设密码)",Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
