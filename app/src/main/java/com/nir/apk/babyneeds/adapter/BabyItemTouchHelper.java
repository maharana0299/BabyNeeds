package com.nir.apk.babyneeds.adapter;

import android.telephony.ims.ImsMmTelManager;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

public class BabyItemTouchHelper {
    public static class  SwipeToDeleteCallback extends ItemTouchHelper.SimpleCallback {

        private RecyclerViewAdapter adapter;
        public SwipeToDeleteCallback(RecyclerViewAdapter adapter) {
            super(0, ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT);
            this.adapter = adapter;
        }

        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAdapterPosition();
            adapter.deleteItem(position, (RecyclerViewAdapter.ViewHolder) viewHolder);

        }


    }
}