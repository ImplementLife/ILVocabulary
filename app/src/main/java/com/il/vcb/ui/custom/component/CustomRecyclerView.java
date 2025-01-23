package com.il.vcb.ui.custom.component;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.il.vcb.ui.activity.MainActivity;
import com.il.vcb.ui.custom.adapter.RecyclerViewListAdapter;

import java.util.Comparator;
import java.util.List;

import static com.il.vcb.ui.custom.adapter.RecyclerViewListAdapter.ViewDataBinder;

public class CustomRecyclerView extends RecyclerView {
    private RecyclerViewListAdapter adapter = new RecyclerViewListAdapter();

    public CustomRecyclerView() {
        super(MainActivity.getInstance());
        setAdapter(this.adapter);
    }
    public CustomRecyclerView(Context context) {
        super(context);
        setAdapter(this.adapter);
    }
    public CustomRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setAdapter(this.adapter);
    }
    public CustomRecyclerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setAdapter(this.adapter);
    }

    @Override
    public void setAdapter(Adapter adapter) {
        if (getLayoutManager() == null) {
            setLayoutManager(new LinearLayoutManager(getContext()));
        }
        super.setAdapter(this.adapter);
    }

    public int getItemViewType(int position) {
        return adapter.getItemViewType(position);
    }

    public int getItemCount() {
        return adapter.getItemCount();
    }

    public void add(ViewDataBinder item) {
        adapter.add(item);
    }

    public void add(ViewDataBinder item, int position) {
        adapter.add(item, position);
    }

    public void addAll(List<? extends ViewDataBinder> all) {
        adapter.addAll(all);
    }

    public void replaceAll(List<ViewDataBinder> replace) {
        adapter.replaceAll(replace);
    }

    public void clear() {
        adapter.clear();
    }

    public void remove(int position) {
        adapter.remove(position);
    }

    @SuppressLint("NotifyDataSetChanged")
    public void sort(Comparator<? super ViewDataBinder> comparator) {
        adapter.sort(comparator);
    }

    public List<ViewDataBinder> getData() {
        return adapter.getViewDataBinders();
    }

    public ViewDataBinder get(int position) {
        return adapter.get(position);
    }
}
