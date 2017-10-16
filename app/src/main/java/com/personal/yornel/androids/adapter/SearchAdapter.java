package com.personal.yornel.androids.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.personal.yornel.androids.R;
import com.personal.yornel.androids.model.GoogleResult;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yornel on 23/9/2017.
 */

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {

    private ArrayList<GoogleResult> smartphones;
    private Context context;

    public static final String NOKIA = "nokia";
    public static final String SAMSUNG = "samsung";
    public static final String MOTOROLA = "motorola";
    public static final String HTC = "htc";
    public static final String BLU = "blu";
    public static final String ASUS = "asus";
    public static final String HUAWEI = "huawei";
    public static final String LENOVO = "lenovo";
    public static final String BLACKBERRY = "blackberry";
    public static final String LG = "lg";
    public static final String PIXEL = "pixel";
    public static final String NEXUS = "nexus";
    public static final String XIAOMI = "xiaomi";

    public SearchAdapter(Context context, ArrayList<GoogleResult> smartphones) {
        this.smartphones = smartphones;
        this.context = context;
    }
    @Override
    public SearchAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_search, parent, false);

        return new SearchAdapter.ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(SearchAdapter.ViewHolder viewHolder, int position) {
        if (!smartphones.isEmpty()) {
            GoogleResult googleResult = smartphones.get(position);
            viewHolder.title.setText(googleResult.getTitle());
            viewHolder.icon.setVisibility(View.VISIBLE);
            if (googleResult.getTitle().toLowerCase().contains(NOKIA.toLowerCase())) {
                viewHolder.icon.setImageDrawable(context.getResources().getDrawable(R.drawable.nokia_icon));
            } else if (googleResult.getTitle().toLowerCase().contains(SAMSUNG.toLowerCase())) {
                viewHolder.icon.setImageDrawable(context.getResources().getDrawable(R.drawable.samsung_icon));
            } else if (googleResult.getTitle().toLowerCase().contains(MOTOROLA.toLowerCase())) {
                viewHolder.icon.setImageDrawable(context.getResources().getDrawable(R.drawable.motorola_icon));
            } else if (googleResult.getTitle().toLowerCase().contains(HTC.toLowerCase())) {
                viewHolder.icon.setImageDrawable(context.getResources().getDrawable(R.drawable.htc_icon));
            } else if (googleResult.getTitle().toLowerCase().contains(XIAOMI.toLowerCase())) {
                viewHolder.icon.setImageDrawable(context.getResources().getDrawable(R.drawable.xiaomi_icon));
            } else if (googleResult.getTitle().toLowerCase().contains(ASUS.toLowerCase())) {
                viewHolder.icon.setImageDrawable(context.getResources().getDrawable(R.drawable.asus_icon));
            } else if (googleResult.getTitle().toLowerCase().contains(BLU.toLowerCase())) {
                viewHolder.icon.setImageDrawable(context.getResources().getDrawable(R.drawable.blu_icon));
            } else if (googleResult.getTitle().toLowerCase().contains(BLACKBERRY.toLowerCase())) {
                viewHolder.icon.setImageDrawable(context.getResources().getDrawable(R.drawable.blackberry_icon));
            } else if (googleResult.getTitle().toLowerCase().contains(HUAWEI.toLowerCase())) {
                viewHolder.icon.setImageDrawable(context.getResources().getDrawable(R.drawable.huawei_icon));
            } else if (googleResult.getTitle().toLowerCase().contains(LENOVO.toLowerCase())) {
                viewHolder.icon.setImageDrawable(context.getResources().getDrawable(R.drawable.lenovo_icon));
            } else if (googleResult.getTitle().toLowerCase().contains(LG.toLowerCase())) {
                viewHolder.icon.setImageDrawable(context.getResources().getDrawable(R.drawable.lg_icon));
            }  else if (googleResult.getTitle().toLowerCase().contains(PIXEL.toLowerCase())) {
                viewHolder.icon.setImageDrawable(context.getResources().getDrawable(R.drawable.pixel_icon));
            }  else if (googleResult.getTitle().toLowerCase().contains(NEXUS.toLowerCase())) {
                viewHolder.icon.setImageDrawable(context.getResources().getDrawable(R.drawable.pixel_icon));
            } else {
                viewHolder.icon.setVisibility(View.GONE);
            }
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout row;

        TextView title;
        ImageView icon;

        public ViewHolder(View itemView) {
            super(itemView);
            row = (LinearLayout) itemView;
            title = (TextView) itemView.findViewById(R.id.title);
            icon = (ImageView) itemView.findViewById(R.id.icon);
        }
    }

    @Override
    public int getItemCount() {
        return smartphones.size();
    }

    public void replaceAll(List<GoogleResult> products) {
        this.smartphones = new ArrayList<>(products);
        notifyDataSetChanged();
    }

    public GoogleResult removeItem(int position) {
        final GoogleResult model = smartphones.remove(position);
        notifyItemRemoved(position);
        return model;
    }

    public void addItems(List<GoogleResult> products) {
        this.smartphones.addAll(products);
        notifyDataSetChanged();
    }

    public void addItem(int position, GoogleResult model) {
        smartphones.add(position, model);
        notifyItemInserted(position);
    }

    public void moveItem(int fromPosition, int toPosition) {
        final GoogleResult model = smartphones.remove(fromPosition);
        smartphones.add(toPosition, model);
        notifyItemMoved(fromPosition, toPosition);
    }

    public GoogleResult getItem(int position) {
        return smartphones.get(position);
    }

}
