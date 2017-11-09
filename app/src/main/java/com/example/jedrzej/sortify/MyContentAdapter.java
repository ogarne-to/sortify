package com.example.jedrzej.sortify;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Created by jedrz on 14.07.2016.
 */
public class MyContentAdapter extends RecyclerView.Adapter<MyContentAdapter.ViewHolder> {

    ArrayList<String> mContents;
    Context context;

    public MyContentAdapter(ArrayList<String> contents, Context context) {
        this.mContents = contents;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_element, parent, false);
        ViewHolder vh = new ViewHolder(view, this);
        return vh;


    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.position = position;
        holder.mEdiText.setText(mContents.get(position));


    }



    @Override
    public int getItemCount() {
        return mContents.size();
    }




    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnFocusChangeListener {

        private int position;
        private MyContentAdapter adapter;
        private ImageView mDragHandle1;
        private ImageView mDragHandle2;
        private EditText mEdiText;
        private ImageView mRemoveImageView;

        public ViewHolder(View itemView, MyContentAdapter adapter) {
            super(itemView);

            this.adapter = adapter;


            mDragHandle1 = (ImageView) itemView.findViewById(R.id.drag_handle_1);
            mDragHandle2 = (ImageView) itemView.findViewById(R.id.drag_handle_2);

            mEdiText = (EditText) itemView.findViewById(R.id.content_element_edit_text);
            mEdiText.setOnFocusChangeListener(this);
            

            mRemoveImageView = (ImageView) itemView.findViewById(R.id.remove_image_view);
            mRemoveImageView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            mEdiText.setOnFocusChangeListener(null);
            mContents.remove(position);
            adapter.notifyDataSetChanged();
        }

        @Override
        public void onFocusChange(View view, boolean b) {
            if (!b) {
                Log.d("MyContentAdpter", "onFocusChange(), adapter position: " + position );
                String string = ((EditText) view).getText().toString();
                if (!string.isEmpty()) {
                    mContents.set(position, string);
                }

            }
        }



    }

}
