package com.excellence.ggz.parsetsplayer.binding_adapter;

import android.util.Log;

import androidx.databinding.BindingAdapter;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

/**
 * @author ggz
 * @date 2021/4/15
 */
public class SmartRefreshLayoutBindingAdapter {

    @BindingAdapter(value = {"refreshListener"}, requireAll = false)
    public static void refreshListener(SmartRefreshLayout view, OnRefreshListener listener) {
        view.setOnRefreshListener(listener);
    }

    @BindingAdapter(value = {"autoRefresh"}, requireAll = false)
    public static void autoRefresh(SmartRefreshLayout view, int delay) {
        Log.e("TAG", "autoRefresh: delay = " + delay);
        if (delay == 0) {
            view.autoRefresh();
        } else {
            view.autoRefresh(delay);
        }
    }

    @BindingAdapter(value = {"finishRefresh"}, requireAll = false)
    public static void finishRefresh(SmartRefreshLayout view, boolean status) {
        Log.e("TAG", "finishRefresh: status = " + status);
        view.finishRefresh(status);
    }
}
