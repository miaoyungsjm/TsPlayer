package com.excellence.ggz.parsetsplayer.data_source;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * @author ggz
 * @date 2021/4/13
 */
public class DataSourceViewModel extends ViewModel {
    private static final String TAG = DataSourceViewModel.class.getSimpleName();

    private final CompositeDisposable mCompositeDisposable;
    private final List<DataSource> mDataSourceList;

    private final MutableLiveData<List<DataSource>> mDataSourceLiveData = new MutableLiveData<>();

    public DataSourceViewModel() {
        mDataSourceList = new ArrayList<>();
        mCompositeDisposable = new CompositeDisposable();
    }

    public void loadTsFile(String path) {
        Disposable disposable = Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Boolean> emitter) throws Exception {
                mDataSourceList.clear();
                traverseTsFile(path);
                for (DataSource dataSource : mDataSourceList) {
                    Log.d(TAG, dataSource.toString());
                }
                emitter.onNext(true);
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) throws Exception {
                mDataSourceLiveData.setValue(mDataSourceList);
            }
        });
        mCompositeDisposable.add(disposable);
    }

    private void traverseTsFile(String path) {
        File dir = new File(path);
        File[] files = dir.listFiles();
        if (files == null) {
            return;
        }
        for (File file : files) {
            if (file.isDirectory()) {
                traverseTsFile(file.getAbsolutePath());
            } else {
                String filePath = file.getAbsolutePath();
                String fileName = file.getName();
                int pointIndex = fileName.lastIndexOf(".");
                String suffix = fileName.substring(pointIndex + 1);
                if ("ts".equalsIgnoreCase(suffix)) {
                    DataSource dataSource = new DataSource(fileName, filePath);
                    mDataSourceList.add(dataSource);
                }
            }
        }
    }

    @Override
    protected void onCleared() {
        mCompositeDisposable.dispose();
        super.onCleared();
    }

    public MutableLiveData<List<DataSource>> getDataSourceLiveData() {
        return mDataSourceLiveData;
    }
}
