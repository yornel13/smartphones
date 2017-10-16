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
import com.personal.yornel.androids.model.Smartphone;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yornel on 03-jul-16.
 */
public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {

    private ArrayList<Smartphone> smartphones;
    private Context context;

    public MainAdapter(Context context, ArrayList<Smartphone> smartphones) {
        this.smartphones = smartphones;
        this.context = context;
    }
    @Override
    public MainAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_smartphone, parent, false);

        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(MainAdapter.ViewHolder viewHolder, int position) {
        if (!smartphones.isEmpty()) {
            Smartphone smartphone = smartphones.get(position);
            viewHolder.title.setText(smartphone.getTitle());
            Picasso.with(context)
                    .load(smartphone.getImage())
                    .error(R.drawable.google_nexus_512).into(viewHolder.smartphoneImage);
            viewHolder.processor.setText(smartphone.getProcessor().getModel());
            viewHolder.ram.setText(smartphone.getMemory().getRam());
            viewHolder.inches.setText(smartphone.getScreen().getSize()+" pulgadas");
            viewHolder.version.setText(smartphone.getPhone().getSystem());
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout row;

        ImageView smartphoneImage;

        TextView title;
        TextView processor;
        TextView ram;
        TextView inches;
        TextView version;

        public ViewHolder(View itemView) {
            super(itemView);
            row = (LinearLayout) itemView;
            smartphoneImage = (ImageView) itemView.findViewById(R.id.smartphone_image);
            title = (TextView) itemView.findViewById(R.id.title);
            processor = (TextView) itemView.findViewById(R.id.processor);
            ram = (TextView) itemView.findViewById(R.id.ram);
            inches = (TextView) itemView.findViewById(R.id.inches);
            version = (TextView) itemView.findViewById(R.id.version);
        }
    }

    @Override
    public int getItemCount() {
        return smartphones.size();
    }

    public void replaceAll(List<Smartphone> products) {
        this.smartphones = new ArrayList<>(products);
        notifyDataSetChanged();
    }

    public Smartphone removeItem(int position) {
        final Smartphone model = smartphones.remove(position);
        notifyItemRemoved(position);
        return model;
    }

    public void addItems(List<Smartphone> products) {
        this.smartphones.addAll(products);
        notifyDataSetChanged();
    }

    public void addItem(int position, Smartphone model) {
        smartphones.add(position, model);
        notifyItemInserted(position);
    }

    public void moveItem(int fromPosition, int toPosition) {
        final Smartphone model = smartphones.remove(fromPosition);
        smartphones.add(toPosition, model);
        notifyItemMoved(fromPosition, toPosition);
    }

    public Smartphone getItem(int position) {
        return smartphones.get(position);
    }

}
