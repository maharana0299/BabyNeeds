package com.nir.apk.babyneeds.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.nir.apk.babyneeds.ListActivity;
import com.nir.apk.babyneeds.R;
import com.nir.apk.babyneeds.data.DatabaseHandler;
import com.nir.apk.babyneeds.model.BabyItems;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>  {

    private static final String TAG ="Adapter" ;
    private Context context;
    private List<BabyItems> itemsList;
    private AlertDialog dialog;
    private AlertDialog.Builder builder;
    private LayoutInflater layoutInflater;
    public RecyclerViewAdapter(Context context, List<BabyItems> itemsList) {
        Log.d(TAG, "RecyclerViewAdapter: ");

        this.context = context;
        this.itemsList = itemsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row,
                parent,false);

        return new ViewHolder(view,context);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //binding data
        BabyItems items = itemsList.get(position); //object item
//        Log.d(TAG, "onBindViewHolder: "  + items.getItems());
        holder.itemName.setText("Item: " + items.getItems());
        holder.itemColor.setText("Color: " + items.getColor());
        holder.itemSize.setText("Size: " + Integer.toString(items.getSize()));
        holder.itemQuantity.setText("Qty: " + Integer.toString(items.getQuantity()));
//        Log.d(TAG, "onBindViewHolder: "  + items.getSize());
        holder.itemDate.setText("Date:" + items.getDateItemAdded());
        holder.id = items.getId();

    }

    @Override
    public int getItemCount() {
        return itemsList.size();
    }

    public void deleteItem(final int position, RecyclerViewAdapter.ViewHolder holder) {

//        if (holder instanceof RecyclerViewAdapter.ViewHolder){
//            ViewHolder viewHolder = holder;
//            Log.d(TAG, "deleteItem: " + viewHolder.itemDate.getText().toString().trim());
//        }

//        Snackbar.make(holder.getView(),R.string.sanackbar_undo_text,Snackbar.LENGTH_LONG).show();
        builder = new AlertDialog.Builder(context);
        layoutInflater = LayoutInflater.from(context);
        View v = layoutInflater.inflate(R.layout.confirmation_pop,null);

        Button confirmButton = v.findViewById(R.id.confirm_button);
        Button cancelButton  = v.findViewById(R.id.cancel_button);

        builder.setView(v);
        dialog = builder.create();
        dialog.show();

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BabyItems items = itemsList.get(position);
                DatabaseHandler db = new DatabaseHandler(context);
                db.deleteItem(items.getId());
                itemsList.remove(position);
                notifyItemRemoved(position);
                dialog.dismiss();
            }
        });

    }

//    public void onSwipeDelete(ViewHolder viewHolder) {
//        viewHolder.deleteItem(viewHolder.getAdapterPosition());
//    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView itemName;
        public TextView itemColor;
        public TextView itemQuantity;
        public TextView itemSize;
        public TextView itemDate;
        public int id;
        public Button editButton;
        public Button deleteButton;

        public ViewHolder(@NonNull View itemView , Context cntxt) {
            super(itemView);
            context = cntxt;
           // Log.d(TAG, "ViewHolder: ");
            Log.d("Adapter", "ViewHolder: ");
            itemName = itemView.findViewById(R.id.item_name);
            itemColor = itemView.findViewById(R.id.item_color);
            itemQuantity = itemView.findViewById(R.id.item_quantity_single);
            itemSize = itemView.findViewById(R.id.item_size_single);
            itemDate = itemView.findViewById(R.id.item_date);
            editButton = itemView.findViewById(R.id.edit_button);
            deleteButton = itemView.findViewById(R.id.delete_button);
//            Log.d(TAG, "ViewHolder: " + itemView.findViewById(R.id.item_size_single) );

            editButton.setOnClickListener(this);
            deleteButton.setOnClickListener(this);

        }

        public View getView(){
            return itemView;
        }

        @Override
        public void onClick(View view) {
            Log.d(TAG, "onClick: ");
            switch (view.getId()) {
                case R.id.edit_button:
                    //editItem
                    int position = getAdapterPosition();
                    BabyItems items = itemsList.get(position);
                    editItem(items);
                    break;
                case R.id.delete_button :
                    //delete item

                    deleteItem();
                    break;
                case R.id.cancel_button:
                    dialog.dismiss();
                    break;
                case R.id.confirm_button:
                    DatabaseHandler db = new DatabaseHandler(context);
                    db.deleteItem(id);
                    //removing from list
                    itemsList.remove(getAdapterPosition());
                    //notifying the adapter
                    notifyItemRemoved(getAdapterPosition());
                    dialog.dismiss();
                    break;

            }
        }

        public void deleteItem() {
            Log.d(TAG, "deleteItem: " + id);

            builder = new AlertDialog.Builder(context);
            layoutInflater = LayoutInflater.from(context);
            View v = layoutInflater.inflate(R.layout.confirmation_pop,null);

            Button confirmButton = v.findViewById(R.id.confirm_button);
            Button cancelButton  = v.findViewById(R.id.cancel_button);

            builder.setView(v);
            dialog = builder.create();
            dialog.show();

            cancelButton.setOnClickListener(this);
            confirmButton.setOnClickListener(this);


        }

        private void editItem(final BabyItems newItem) {

          //  BabyItems item = itemsList.get(getAdapterPosition());
            Button saveButton;
            final EditText babyItem;
            final EditText itemQuantity;
            final EditText itemColor;
            final EditText itemSize;
            TextView title;

            builder = new AlertDialog.Builder(context);
            layoutInflater = LayoutInflater.from(context);

            View view = layoutInflater.inflate(R.layout.popup,null);

            babyItem = view.findViewById(R.id.babyItem);
            itemQuantity = view.findViewById(R.id.itemQuantity);
            itemColor = view.findViewById(R.id.itemColor);
            itemSize = view.findViewById(R.id.itemSize);
            saveButton = view.findViewById(R.id.saveButton);
            title = view.findViewById(R.id.title);

            babyItem.setText(newItem.getItems());
            itemQuantity.setText(Integer.toString(newItem.getQuantity()));
            itemColor.setText(newItem.getColor());
            itemSize.setText(Integer.toString(newItem.getSize()));

            title.setText("Edit Item");
//            Log.d(TAG, "editItem: " + title.getText().toString().trim());

            builder.setView(view);
            dialog = builder.create();
            dialog.show();

            saveButton.setText("Update");

            saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    DatabaseHandler databaseHandler = new DatabaseHandler(context);

                    //update
                    newItem.setItems(babyItem.getText().toString());
                    newItem.setColor(itemColor.getText().toString());
                    newItem.setSize(Integer.parseInt(itemSize.getText().toString()));
                    newItem.setSize(Integer.parseInt(itemQuantity.getText().toString().trim()));

                    if (!babyItem.getText().toString().isEmpty()
                    && !itemColor.getText().toString().isEmpty()
                    && !itemSize.getText().toString().isEmpty()
                    && !itemQuantity.getText().toString().isEmpty()) {
                        databaseHandler.updateItem(newItem);
                        notifyItemChanged(getAdapterPosition(),newItem); //important
                    } else {
                        Snackbar.make(view,"Fields are empty",Snackbar.LENGTH_SHORT).show();
                    }

                    dialog.dismiss();
                }
            });

        }

    }



}
