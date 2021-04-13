package com.excellence.ggz.parsetsplayer.data_source;

import androidx.lifecycle.ViewModel;

import java.io.File;

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

    private CompositeDisposable mCompositeDisposable;

    public DataSourceViewModel() {
        mCompositeDisposable = new CompositeDisposable();
    }

    public void loadTsFile(String path) {
        Disposable disposable = Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Boolean> emitter) throws Exception {
                traverseTsFile(path);
                emitter.onNext(true);
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) throws Exception {
                // todo: post live data
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
        for (int i = 0; i < files.length; i++) {
            if (files[i].isDirectory()) {
                traverseTsFile(files[i].getAbsolutePath());
            } else {
                String filePath = files[i].getAbsolutePath();
                String fileName = files[i].getName();
//                int j = fileName.lastIndexOf(".");
//                String suffix = fileName.substring(j + 1);
//                if (suffix.equalsIgnoreCase("ts")) {
//                    mFileNameList.add(fileName);
//                    mFilePathList.add(files[i].getAbsolutePath());
//                }
            }
        }
    }

    @Override
    protected void onCleared() {
        if (mCompositeDisposable != null) {
            mCompositeDisposable.dispose();
        }
        super.onCleared();
    }
}
