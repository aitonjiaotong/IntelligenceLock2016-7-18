package lock.lntelligence.gjx.activity;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.LinearLayout;

import com.aiton.administrator.shane_library.shane.ZjbBaseActivity;

import lock.lntelligence.gjx.R;

public class MainActivity extends ZjbBaseActivity implements View.OnClickListener {
    private DrawerLayout drawer_layout;
    private LinearLayout list_left_drawer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
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
        findViewById(R.id.imageView_mine).setOnClickListener(this);
    }

    @Override
    protected void initSP() {

    }

    @Override
    protected void initIntent() {

    }

    @Override
    protected void findID() {
        drawer_layout = (DrawerLayout) findViewById(R.id.drawer_layout);
        list_left_drawer = (LinearLayout) findViewById(R.id.list_left_drawer);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imageView_mine:
                drawer_layout.openDrawer(list_left_drawer);
                break;
        }
    }
}
