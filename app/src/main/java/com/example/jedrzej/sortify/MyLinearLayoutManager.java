package com.example.jedrzej.sortify;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;

/**
 * Created by jedrz on 29.07.2016.
 */

public class MyLinearLayoutManager extends LinearLayoutManager {

        public MyLinearLayoutManager(Context context) {
            super(context);

        }

        // it will always pass false to RecyclerView when calling "canScrollVertically()" method.
        @Override
        public boolean canScrollVertically() {
            return false;
        }
    }

