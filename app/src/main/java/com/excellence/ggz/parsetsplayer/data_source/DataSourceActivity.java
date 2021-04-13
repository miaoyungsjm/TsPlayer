package com.excellence.ggz.parsetsplayer.data_source;

import android.Manifest;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.excellence.ggz.parsetsplayer.BR;
import com.excellence.ggz.parsetsplayer.R;
import com.excellence.ggz.parsetsplayer.base.BaseActivity;
import com.excellence.ggz.parsetsplayer.databinding.DataSourceActivityBinding;
import com.kunminx.architecture.ui.page.DataBindingConfig;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.List;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

/**
 * @author ggz
 * @date 2021/4/13
 */
public class DataSourceActivity extends BaseActivity {
    private static final String TAG = DataSourceActivity.class.getSimpleName();
    private static final int WRITE_EXTERNAL_PERMISSION = 1;
    private static final String TS_FOLDER_PATH =
            Environment.getExternalStorageDirectory().getPath() + "/Download/";
    ;

    private DataSourceViewModel mViewModel;
    private DataSourceActivityBinding mBinding;
    private SmartRefreshLayout mSmartRefreshLayout;

    private List<String> mFileNameList;

    @Override
    protected void initViewModel() {
        mViewModel = getActivityScopeViewModel(DataSourceViewModel.class);
    }

    @Override
    protected DataBindingConfig getDataBindingConfig() {
        return new DataBindingConfig(R.layout.data_source_activity, BR.vm, mViewModel);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = (DataSourceActivityBinding) getBinding();

        initView();
    }

    private void initView() {
        mSmartRefreshLayout = mBinding.srlDataSourceRefreshLayout;
        mSmartRefreshLayout.setOnRefreshListener(refreshLayout -> {
            if (requestPermission()) {
                mViewModel.loadTsFile(TS_FOLDER_PATH);
            } else {
                refreshLayout.finishRefresh(false);
            }
        });
        mSmartRefreshLayout.autoRefresh();
    }

    private boolean requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int checkPermission = ContextCompat.checkSelfPermission(
                    this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (checkPermission != PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        WRITE_EXTERNAL_PERMISSION);
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == WRITE_EXTERNAL_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PERMISSION_GRANTED) {
                mSmartRefreshLayout.autoRefresh();
                Toast.makeText(this, "GET WRITE_EXTERNAL_PERMISSION", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "DENY WRITE_EXTERNAL_PERMISSION", Toast.LENGTH_SHORT).show();
            }
        }
    }
}