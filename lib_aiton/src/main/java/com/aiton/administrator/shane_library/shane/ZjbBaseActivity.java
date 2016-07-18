package com.aiton.administrator.shane_library.shane;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.telephony.gsm.SmsManager;
import android.util.Log;
import android.view.KeyEvent;

import com.aiton.administrator.shane_library.shane.app.SysApplication;
import com.aiton.administrator.shane_library.shane.utils.AppLog;
import com.aiton.administrator.shane_library.shane.utils.NetChecker;
import com.umeng.analytics.MobclickAgent;

import java.lang.Thread.UncaughtExceptionHandler;


public abstract class ZjbBaseActivity extends Activity {

    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (VERSION.SDK_INT >= 14) {
            setTheme(android.R.style.Theme_Holo_Light_NoActionBar);
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

    public void init() {
        SysApplication.getInstance().addActivity(this);
        initSP();
        initIntent();
        initData();
        findID();
        initViews();
        setListeners();
    }

    protected abstract void initData();

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(getRunningActivityName());
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(getRunningActivityName());
        MobclickAgent.onPause(this);
    }

    private String getRunningActivityName() {
        String contextString = this.toString();
        try {
            return contextString.substring(contextString.lastIndexOf(".") + 1, contextString.indexOf("@"));
        } catch (Exception e) {
        }
        return "";
    }

    public void showLoadingDialog(String text) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(text);
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
            mProgressDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
                        finish();
                    }
                    return false;
                }
            });
        }
    }

    public void cancelLoadingDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing())
            mProgressDialog.dismiss();
    }

    protected abstract void initViews();

    protected abstract void setListeners();

    protected abstract void initSP();

    protected abstract void initIntent();

    protected abstract void findID();

    protected boolean isNetAvailable() {
        NetChecker nc = NetChecker.getInstance(this.getApplicationContext());
        if (nc.checkNetwork()) {
            return true;
        } else {
            return false;
        }
    }

    private void setUncaughtExceotion() {
        if (AppLog.DEBUG) {
            final UncaughtExceptionHandler subclass = Thread.currentThread().getUncaughtExceptionHandler();
            Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler() {
                @Override
                public void uncaughtException(Thread paramThread, Throwable paramThrowable) {
                    Log.getStackTraceString(paramThrowable);
                    AppLog.LogD(paramThrowable.getMessage());
                    AppLog.LogE("uncaughtException", paramThrowable.getMessage());
                    subclass.uncaughtException(paramThread, paramThrowable);
                }
            });
        }
    }


    protected void sendSms(String mobile, String msg) {
        SmsManager smsManager = SmsManager.getDefault();
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        smsManager.sendTextMessage(mobile, null, msg, null, null);
    }

    protected void startRegActivity() {
//        Intent intent = new Intent(this, RegisterActivity.class);
//        startActivity(intent);
    }

}
