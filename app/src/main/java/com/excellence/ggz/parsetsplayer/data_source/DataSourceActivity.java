package com.excellence.ggz.parsetsplayer.data_source;

import android.Manifest;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;

import com.excellence.ggz.parsetsplayer.BR;
import com.excellence.ggz.parsetsplayer.R;
import com.excellence.ggz.parsetsplayer.adater.CommonRvAdapter;
import com.excellence.ggz.parsetsplayer.base.BaseActivity;
import com.excellence.ggz.parsetsplayer.databinding.DataSourceActivityBinding;
import com.excellence.ggz.parsetsplayer.databinding.DataSourceItemBinding;
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

    private DataSourceViewModel mViewModel;
    private DataSourceActivityBinding mBinding;
    private SmartRefreshLayout mSmartRefreshLayout;
    private CommonRvAdapter<DataSource, DataSourceItemBinding> mAdapter;

    @Override
    protected void initViewModel() {
        mViewModel = getActivityScopeViewModel(DataSourceViewModel.class);
    }

    @Override
    protected DataBindingConfig getDataBindingConfig() {
        mAdapter = new CommonRvAdapter<>(R.layout.data_source_item);
        return new DataBindingConfig(R.layout.data_source_activity, BR.vm, mViewModel)
                .addBindingParam(BR.adapter, mAdapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = (DataSourceActivityBinding) getBinding();
        initView();
        initData();
    }

    private void initView() {
        mSmartRefreshLayout = mBinding.srlDataSourceRefreshLayout;
        mSmartRefreshLayout.setOnRefreshListener(refreshLayout -> {
            if (requestPermission()) {
                String tsFolderPath = Environment.getExternalStorageDirectory().getPath() +
                        "/" + Environment.DIRECTORY_DOWNLOADS;
                mViewModel.loadTsFile(tsFolderPath);
            } else {
                refreshLayout.finishRefresh(false);
            }
        });
        mSmartRefreshLayout.autoRefresh();
    }

    private void initData() {
        mViewModel.getDataSourceLiveData().observe(this, new Observer<List<DataSource>>() {
            @Override
            public void onChanged(List<DataSource> dataSources) {
                mAdapter.setDataList(dataSources);
                mSmartRefreshLayout.finishRefresh(true);
            }
        });
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean requestFlag;
        if (requestCode == WRITE_EXTERNAL_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PERMISSION_GRANTED) {
                requestFlag = true;
                mSmartRefreshLayout.autoRefresh();
            } else {
                requestFlag = false;
            }
            String tips = getResources().getString(requestFlag ?
                    R.string.data_source_tips_request_permission_successful :
                    R.string.data_source_tips_request_permission_failed);
            Toast.makeText(this, tips, Toast.LENGTH_SHORT).show();
        }
    }
}