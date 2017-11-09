package com.example.jedrzej.sortify;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.jedrzej.sortify.datamodels.Pack;
import com.example.jedrzej.sortify.ui.MainActivity;

import java.util.ArrayList;

/**
 * Created by jedrz on 03.07.2016.
 */
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private ArrayList<Pack> mPacks;
    private MainActivity mMainActivity;

    public MyAdapter(ArrayList<Pack> packs, MainActivity mainActivity) {
        this.mPacks = packs;
        this.mMainActivity = mainActivity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view, parent, false);
        ViewHolder vh = new ViewHolder(v, mPacks);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Pack pack = mPacks.get(position);

        // Generate QR code and assign to imageView

        holder.mVisibleId.setText(pack.getVisibleId());
        holder.mName.setText(pack.getName());
        holder.mContents.setText(pack.getStringOfContents());
        holder.mLocation.setText(pack.getLocation());
    }

    @Override
    public int getItemCount() {
        return mPacks.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView mVisibleId;
        TextView mName;
        TextView mContents;
        TextView mLocation;
        ArrayList<Pack> mPacks;
        MainActivity mContext;

        public ViewHolder(View itemView, ArrayList<Pack> packs) {
            super(itemView);

            mPacks = packs;
            mContext = (MainActivity)itemView.getContext();

            mVisibleId = (TextView) itemView.findViewById(R.id.visible_id_text_view);
            mName = (TextView) itemView.findViewById(R.id.name_text_view);
            mContents = (TextView) itemView.findViewById(R.id.contents_text_view);
            mLocation = (TextView) itemView.findViewById(R.id.location_text_view);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = this.getAdapterPosition();
            Pack pack = mPacks.get(position);
            mContext.goToDetails(pack);
        }
    }
}
