package com.excellence.ggz.parsetsplayer.adater;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import com.excellence.ggz.parsetsplayer.BR;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ggz
 * @date 2020/11/3
 */
public class CommonRvAdapter<E, T extends ViewDataBinding> extends RecyclerView.Adapter<CommonRvAdapter.ViewHolder<T>> {

    private final int mLayoutId;
    private OnItemClickListener mListener;
    private List<E> mDataList = new ArrayList<>();

    public CommonRvAdapter(int layoutId) {
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
                    mListener.onItemClick(position);
                }
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder<T> holder, int position) {
        if (position < mDataList.size()) {
            E entity = mDataList.get(position);
            holder.binding.setVariable(BR.common, entity);
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

    public void setDataList(List<E> list) {
        mDataList = list;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }
}
