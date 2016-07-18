package lock.lntelligence.gjx.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.aiton.administrator.shane_library.shane.ZjbBaseActivity;
import com.aiton.administrator.shane_library.shane.utils.ACache;

import lock.lntelligence.gjx.R;
import lock.lntelligence.gjx.constant.Constant;
import lock.lntelligence.gjx.model.User;

public class WelcomeActivity extends ZjbBaseActivity {
    Handler hand = new Handler();
    private boolean isLogin = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        init();
    }

    @Override
    protected void initData() {
        ACache aCache = ACache.get(WelcomeActivity.this);
        User user = (User) aCache.getAsObject(Constant.ACACHE.USER);
        if (user == null) {
            isLogin = false;
            Log.e("initData", "未登录");
        } else {
            isLogin = true;
        }
    }

    @Override
    protected void initViews() {
        hand.postDelayed(new Runnable() {

            @Override
            public void run() {
                Intent intent = new Intent();
                if (isLogin) {
                    intent.setClass(WelcomeActivity.this, LockActivity.class);
                } else {
                    intent.setClass(WelcomeActivity.this, LoginActivity.class);
                }
                startActivity(intent);
                finish();
            }
        }, 2000);
    }

    @Override
    protected void setListeners() {

    }

    @Override
    protected void initSP() {

    }

    @Override
    protected void initIntent() {

    }

    @Override
    protected void findID() {

    }

}
