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
import com.excellence.ggz.parsetsplayer.adater.OnItemClickListener;
import com.excellence.ggz.parsetsplayer.base.BaseActivity;
import com.kunminx.architecture.ui.page.DataBindingConfig;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

/**
 * @author ggz
 * @date 2021/4/13
 */
public class DataSourceActivity extends BaseActivity {
    private static final String TAG = DataSourceActivity.class.getSimpleName();
    private static final int REQUEST_CODE_WRITE_EXTERNAL_PERMISSION = 1;

    private DataSourceViewModel mViewModel;

    @Override
    protected void initViewModel() {
        mViewModel = getActivityScopeViewModel(DataSourceViewModel.class);
    }

    @Override
    protected DataBindingConfig getDataBindingConfig() {
        DataSourceAdapter adapter = new DataSourceAdapter();
        adapter.setOnItemClickListener(new OnItemClickListener<DataSource>() {
            @Override
            public void onItemClick(DataSource entity) {
                Toast.makeText(DataSourceActivity.this,
                        entity.getFilePath(), Toast.LENGTH_SHORT).show();
            }
        });
        OnRefreshListener listener = new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                if (checkPermission()) {
                    String tsFolderPath = Environment.getExternalStorageDirectory().getPath() +
                            "/" + Environment.DIRECTORY_DOWNLOADS;
                    mViewModel.loadTsFile(tsFolderPath);
                } else {
                    mViewModel.getRefreshStatus().setValue(false);
                }
            }
        };
        return new DataBindingConfig(R.layout.data_source_activity, BR.vm, mViewModel)
                .addBindingParam(BR.adapter, adapter)
                .addBindingParam(BR.listener, listener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private boolean checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int checkPermission = ContextCompat.checkSelfPermission(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (checkPermission != PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_CODE_WRITE_EXTERNAL_PERMISSION);
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
        if (requestCode == REQUEST_CODE_WRITE_EXTERNAL_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PERMISSION_GRANTED) {
                requestFlag = true;
                mViewModel.getAutoRefresh().setValue(0);
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