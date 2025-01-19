package com.il.vcb.ui.custom.adapter;

import android.annotation.SuppressLint;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import com.il.vcb.ui.custom.component.BaseView;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static com.il.vcb.ui.custom.adapter.RecyclerViewListAdapter.*;

public class RecyclerViewListAdapter extends RecyclerView.Adapter<Holder> {
    private final List<Data> data;

    public RecyclerViewListAdapter() {
        this(new ArrayList<>());
    }
    public RecyclerViewListAdapter(List<Data> data) {
        this.data = data;
    }

    @Override
    public int getItemViewType(int position) {
        return data.get(position).getViewId();
    }
    @NotNull
    @Override
    public Holder onCreateViewHolder(@NotNull ViewGroup parent, int viewId) {
        return new Holder(new BaseView(viewId, parent), viewId);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        data.get(position).bindData(holder.getView());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    //region Data
    public void add(Data item) {
        data.add(item);
        link(item, data.size());
        notifyItemInserted(data.size());
    }

    public void add(Data item, int position) {
        data.add(position, item);
        linkAll();
        notifyItemInserted(position);
    }

    public void addAll(List<? extends Data> all) {
        int size = data.size();
        data.addAll(all);
        linkAll();
        notifyItemRangeInserted(size - 1, all.size());
    }

    public void replaceAll(List<Data> replace) {
        clear();
        addAll(replace);
    }

    public void clear() {
        unlinkAll();
        int size = data.size();
        data.clear();
        notifyItemRangeRemoved(0, size);
    }

    public void remove(int position) {
        unlink(data.get(position));
        data.remove(position);
        notifyItemRemoved(position);
    }

    @SuppressLint("NotifyDataSetChanged")
    public void sort(Comparator<? super Data> comparator) {
        data.sort(comparator);
        linkAll();
        notifyDataSetChanged();
    }

    private void link(Data item, int position) {
        item.setAdapter(this);
        item.setPosition(position);
    }

    private void unlink(Data item) {
        item.setAdapter(null);
        item.setPosition(-1);
    }

    private void linkAll() {
        for (int i = 0; i < data.size(); i++) {
            link(this.data.get(i), i);
        }
    }

    private void unlinkAll() {
        for (Data item : data) {
            item.setAdapter(null);
            item.setPosition(-1);
        }
    }

    //endregion Data

    public List<Data> getData() {
        return data;
    }
    public Data get(int position) {
        return data.get(position);
    }

    public static abstract class Data<D> {
        private RecyclerViewListAdapter adapter;
        protected int viewId;
        protected D data;
        private int position = -1;

        public Data(int viewId) {
            this.viewId = viewId;
        }

        public int getViewId() {
            return viewId;
        }

        private void setAdapter(RecyclerViewListAdapter adapter) {
            this.adapter = adapter;
        }

        public D getData() {
            return data;
        }
        public void setData(D data) {
            this.data = data;
            callBind();
        }

        public void setPosition(int position) {
            this.position = position;
        }

        public void callBind() {
            if (adapter != null && position >= 0) {
                adapter.notifyItemChanged(position);
            }
        }
        public abstract void bindData(BaseView view, D data);
        public void bindData(BaseView view) {
            bindData(view, data);
        }
    }
    public static class Holder extends RecyclerView.ViewHolder {
        protected int viewId;
        private final BaseView view;

        public Holder(BaseView view, int viewId) {
            super(view.getRoot());
            this.viewId = viewId;
            this.view = view;
        }

        public BaseView getView() {
            return view;
        }

        public int getViewId() {
            return viewId;
        }
    }
}