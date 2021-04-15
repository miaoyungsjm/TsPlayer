package com.excellence.ggz.parsetsplayer.data_source;

import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.excellence.ggz.parsetsplayer.BR;
import com.excellence.ggz.parsetsplayer.R;
import com.excellence.ggz.parsetsplayer.adater.BaseListAdapter;
import com.excellence.ggz.parsetsplayer.adater.OnItemClickListener;
import com.excellence.ggz.parsetsplayer.databinding.DataSourceItemBinding;

/**
 * @author ggz
 * @date 2021/4/15
 */
public class DataSourceAdapter extends BaseListAdapter<DataSource, DataSourceItemBinding> {

    protected DataSourceAdapter() {
        super(new DiffUtil.ItemCallback<DataSource>() {
            @Override
            public boolean areItemsTheSame(@NonNull DataSource oldItem, @NonNull DataSource newItem) {
                return oldItem.equals(newItem);
            }

            @Override
            public boolean areContentsTheSame(@NonNull DataSource oldItem, @NonNull DataSource newItem) {
                return oldItem.getFilePath().equals(newItem.getFilePath());
            }
        });
    }

    @Override
    protected int getLayoutResId(int viewType) {
        return R.layout.data_source_item;
    }

    @Override
    protected void onBindItem(ViewHolder<DataSourceItemBinding> holder, DataSource entity) {
        DataSourceItemBinding binding = holder.binding;
        binding.setVariable(BR.common, entity);
    }
}
