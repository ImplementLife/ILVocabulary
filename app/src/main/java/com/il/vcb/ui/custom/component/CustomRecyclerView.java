package com.il.vcb.ui.custom.component;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.il.vcb.ui.activity.MainActivity;
import com.il.vcb.ui.custom.adapter.RecyclerViewListAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.List;

import static com.il.vcb.ui.custom.adapter.RecyclerViewListAdapter.*;

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

    @NotNull
    public Holder onCreateViewHolder(@NotNull ViewGroup parent, int viewId) {
        return adapter.onCreateViewHolder(parent, viewId);
    }

    public void onBindViewHolder(Holder holder, int position) {
        adapter.onBindViewHolder(holder, position);
    }

    public int getItemCount() {
        return adapter.getItemCount();
    }

    public void add(Data item) {
        adapter.add(item);
    }

    public void add(Data item, int position) {
        adapter.add(item, position);
    }

    public void addAll(List<? extends Data> all) {
        adapter.addAll(all);
    }

    public void replaceAll(List<Data> replace) {
        adapter.replaceAll(replace);
    }

    public void clear() {
        adapter.clear();
    }

    public void remove(int position) {
        adapter.remove(position);
    }

    @SuppressLint("NotifyDataSetChanged")
    public void sort(Comparator<? super Data> comparator) {
        adapter.sort(comparator);
    }

    public List<Data> getData() {
        return adapter.getData();
    }

    public Data get(int position) {
        return adapter.get(position);
    }
}
