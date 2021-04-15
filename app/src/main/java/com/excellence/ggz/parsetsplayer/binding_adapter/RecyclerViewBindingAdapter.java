package com.excellence.ggz.parsetsplayer.binding_adapter;

import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * @author ggz
 * @date 2021/4/15
 */
public class RecyclerViewBindingAdapter {

    @BindingAdapter(value = {"adapter"}, requireAll = false)
    public static void setAdapter(RecyclerView recyclerView, RecyclerView.Adapter adapter) {
        recyclerView.setAdapter(adapter);
    }

    @BindingAdapter(value = {"submitList"}, requireAll = false)
    public static void submitList(RecyclerView recyclerView, List list) {
        if (recyclerView.getAdapter() != null) {
            ListAdapter adapter = (ListAdapter) recyclerView.getAdapter();
            adapter.submitList(list);
        }
    }

    @BindingAdapter(value = {"notifyCurrentListChanged"}, requireAll = false)
    public static void notifyCurrentListChanged(RecyclerView recyclerView, boolean notifyCurrentListChanged) {
        if (notifyCurrentListChanged) {
            recyclerView.getAdapter().notifyDataSetChanged();
        }
    }
}
