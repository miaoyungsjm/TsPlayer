package com.excellence.ggz.parsetsplayer.adater;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @author ggz
 * @date 2021/4/15
 */
public abstract class BaseListAdapter<E, B extends ViewDataBinding> extends ListAdapter<E, BaseListAdapter.ViewHolder<B>> {

    private OnItemClickListener<E> mListener;

    protected BaseListAdapter(@NonNull DiffUtil.ItemCallback diffCallback) {
        super(diffCallback);
    }

    @NonNull
    @Override
    public ViewHolder<B> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        B binding = DataBindingUtil.inflate(inflater, getLayoutResId(viewType), parent, false);
        ViewHolder<B> holder = new ViewHolder<>(binding);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                if (mListener != null) {
                    mListener.onItemClick(getItem(position));
                }
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder<B> holder, int position) {
        onBindItem(holder, getItem(position));
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    protected abstract int getLayoutResId(int viewType);

    protected abstract void onBindItem(ViewHolder<B> holder, E entity);

    protected static class ViewHolder<B extends ViewDataBinding> extends RecyclerView.ViewHolder {
        public B binding;

        public ViewHolder(B binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public void setOnItemClickListener(OnItemClickListener<E> listener) {
        mListener = listener;
    }
}
