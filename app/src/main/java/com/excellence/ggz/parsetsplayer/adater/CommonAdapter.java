package com.excellence.ggz.parsetsplayer.adater;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ggz
 * @date 2020/11/3
 */
public class CommonAdapter<T extends ViewDataBinding> extends RecyclerView.Adapter<CommonAdapter.ViewHolder<T>> {

    private int mLayoutId;
    private OnItemClickListener mListener;
    private List<String> mDataList = new ArrayList<>();

    public CommonAdapter(int layoutId) {
        this.mLayoutId = layoutId;
    }

    @NonNull
    @Override
    public ViewHolder<T> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        T binding = DataBindingUtil.inflate(inflater, mLayoutId, parent, false);
        ViewHolder<T> holder = new ViewHolder<>(binding);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                if (mListener != null) {
                    mListener.onItemClick();
                }
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder<T> holder, int position) {
        String entity = mDataList.get(position);
        if (entity != null) {
//            holder.binding.setVariable(BR.common, entity);
        }
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    static class ViewHolder<T extends ViewDataBinding> extends RecyclerView.ViewHolder {
        T binding;

        public ViewHolder(T binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public void setDataList(List<String> list) {
        mDataList = list;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }
}
