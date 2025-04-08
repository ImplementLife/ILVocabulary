package com.il.lexicon.ui.custom.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import com.il.lexicon.ui.activity.MainActivity;
import com.il.lexicon.ui.custom.component.BaseViewAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static com.il.lexicon.ui.custom.adapter.RecyclerViewListAdapter.*;

public class RecyclerViewListAdapter extends RecyclerView.Adapter<Holder> {
    private Context context;
    private final List<ViewDataBinder> viewDataBinders;

    public RecyclerViewListAdapter() {
        this(new ArrayList<>());
    }
    public RecyclerViewListAdapter(List<ViewDataBinder> viewDataBinders) {
        this.viewDataBinders = viewDataBinders;
        context = MainActivity.getInstance();
    }

    @Override
    public int getItemViewType(int position) {
        return viewDataBinders.get(position).getViewId();
    }
    @NotNull
    @Override
    public Holder onCreateViewHolder(@NotNull ViewGroup parent, int viewId) {
        return new Holder(new BaseViewAdapter(viewId, parent));
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        ViewDataBinder viewDataBinder = viewDataBinders.get(position);
        viewDataBinder.setContext(context);
        viewDataBinder.bindData(holder.getViewAdapter());
    }

    @Override
    public int getItemCount() {
        return viewDataBinders.size();
    }

    //region Data
    public void add(ViewDataBinder item) {
        viewDataBinders.add(item);
        link(item, viewDataBinders.size());
        notifyItemInserted(viewDataBinders.size());
    }

    public void add(ViewDataBinder item, int position) {
        viewDataBinders.add(position, item);
        linkAll();
        notifyItemInserted(position);
    }

    public void addAll(List<? extends ViewDataBinder> all) {
        int size = viewDataBinders.size();
        viewDataBinders.addAll(all);
        linkAll();
        notifyItemRangeInserted(size - 1, all.size());
    }

    public void replaceAll(List<? extends ViewDataBinder> replace) {
        clear();
        addAll(replace);
    }

    public void clear() {
        unlinkAll();
        int size = viewDataBinders.size();
        viewDataBinders.clear();
        notifyItemRangeRemoved(0, size);
    }

    public void remove(int position) {
        unlink(viewDataBinders.get(position));
        viewDataBinders.remove(position);
        notifyItemRemoved(position);
    }

    @SuppressLint("NotifyDataSetChanged")
    public void sort(Comparator<? super ViewDataBinder> comparator) {
        viewDataBinders.sort(comparator);
        linkAll();
        notifyDataSetChanged();
    }

    private void link(ViewDataBinder item, int position) {
        item.setAdapter(this);
        item.setPosition(position);
    }

    private void unlink(ViewDataBinder item) {
        item.setAdapter(null);
        item.setPosition(-1);
    }

    private void linkAll() {
        for (int i = 0; i < viewDataBinders.size(); i++) {
            link(this.viewDataBinders.get(i), i);
        }
    }

    private void unlinkAll() {
        for (ViewDataBinder item : viewDataBinders) {
            item.setAdapter(null);
            item.setPosition(-1);
        }
    }

    //endregion Data

    public List<ViewDataBinder> getViewDataBinders() {
        return viewDataBinders;
    }
    public ViewDataBinder get(int position) {
        return viewDataBinders.get(position);
    }

    public static abstract class ViewDataBinder<D> {
        private Context context;
        private RecyclerViewListAdapter adapter;
        protected int viewId;
        protected D data;
        private int position = -1;

        public ViewDataBinder(int viewId) {
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
        }

        public void setPosition(int position) {
            this.position = position;
        }

        public void callBind() {
            if (adapter != null && position >= 0) {
                adapter.notifyItemChanged(position);
            }
        }
        public abstract void bindData(BaseViewAdapter view, D data);
        public void bindData(BaseViewAdapter view) {
            bindData(view, data);
        }

        public Context getContext() {
            return context;
        }

        private void setContext(Context context) {
            this.context = context;
        }

        public Resources getResources() {
            return context.getResources();
        }
    }
    protected static class Holder extends RecyclerView.ViewHolder {
        private final BaseViewAdapter viewAdapter;

        public Holder(BaseViewAdapter view) {
            super(view.getRoot());
            this.viewAdapter = view;
        }

        public BaseViewAdapter getViewAdapter() {
            return viewAdapter;
        }
    }
}