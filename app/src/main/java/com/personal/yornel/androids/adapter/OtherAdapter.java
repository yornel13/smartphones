package com.personal.yornel.androids.adapter;

import android.content.Context;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.personal.yornel.androids.R;
import com.personal.yornel.androids.model.GoogleResult;
import com.personal.yornel.androids.model.Version;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yornel on 23/9/2017.
 */

public class OtherAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {

    private List<Version> versions;
    private Context context;

    public OtherAdapter(Context context, List<Version> versions) {
        this.versions = versions;
        this.context = context;
    }
    @Override
    public MainAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_other_version, parent, false);

        return new MainAdapter.ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(MainAdapter.ViewHolder viewHolder, int position) {
        if (!versions.isEmpty()) {
            Version version = versions.get(position);
            viewHolder.title.setText(version.getTitle());

            if (version.getActive().equalsIgnoreCase("true")) {
                viewHolder.title.setEnabled(false);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    viewHolder.title.setBackground(context.getResources().getDrawable(R.drawable.other_background_disable));
                } else {
                    viewHolder.title.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.other_background_disable) );
                }
            } else {
                viewHolder.title.setEnabled(true);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    viewHolder.title.setBackground(context.getResources().getDrawable(R.drawable.other_background_enable));
                } else {
                    viewHolder.title.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.other_background_enable) );
                }
            }
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout row;

        TextView title;

        public ViewHolder(View itemView) {
            super(itemView);
            row = (LinearLayout) itemView;
            title = (TextView) itemView.findViewById(R.id.title);
        }
    }

    @Override
    public int getItemCount() {
        return versions.size();
    }

    public void replaceAll(List<Version> products) {
        this.versions = new ArrayList<>(products);
        notifyDataSetChanged();
    }

    public Version removeItem(int position) {
        final Version model = versions.remove(position);
        notifyItemRemoved(position);
        return model;
    }

    public void addItems(List<Version> products) {
        this.versions.addAll(products);
        notifyDataSetChanged();
    }

    public void addItem(int position, Version model) {
        versions.add(position, model);
        notifyItemInserted(position);
    }

    public void moveItem(int fromPosition, int toPosition) {
        final Version model = versions.remove(fromPosition);
        versions.add(toPosition, model);
        notifyItemMoved(fromPosition, toPosition);
    }

    public Version getItem(int position) {
        return versions.get(position);
    }

}
