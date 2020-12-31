package com.nir.apk.babyneeds;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.nir.apk.babyneeds.adapter.BabyItemTouchHelper;
import com.nir.apk.babyneeds.adapter.RecyclerViewAdapter;
import com.nir.apk.babyneeds.data.DatabaseHandler;
import com.nir.apk.babyneeds.model.BabyItems;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity
implements View.OnClickListener{
    private static final String TAG = "Activity List";
    private RecyclerView recyclerView;
    private RecyclerViewAdapter adapter;
    private List<BabyItems> itemsList;
    private DatabaseHandler databaseHandler;
    private FloatingActionButton fab;
    private AlertDialog dialog;
    private AlertDialog.Builder builder;
    //for popup view
    private Button saveButton;
    private EditText babyItem;
    private EditText itemQuantity;
    private EditText itemColor;
    private EditText itemSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        initializeUi();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        itemsList = databaseHandler.getAllItems();
        adapter = new RecyclerViewAdapter(this,itemsList);

        // Get items from db
        Log.d(TAG, "onCreate: " + " we are here");
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        for (BabyItems items: itemsList) {
            Log.d(TAG, "onCreate: " + items.toString());
        }

        fab.setOnClickListener(this);
        BabyItemTouchHelper.SwipeToDeleteCallback helper =new  BabyItemTouchHelper.SwipeToDeleteCallback(adapter);

        new ItemTouchHelper(helper).attachToRecyclerView(recyclerView);
//
//        helper = new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT) {
//            @Override
//            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
//                return false;
//            }
//
//            @Override
//            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
//                adapter.onSwipeDelete(viewHolder,viewHolder.getAdapterPosition());
//
//            }
//        };

//        new ItemTouchHelper(helper).attachToRecyclerView(recyclerView);



    }

    private void initializeUi() {

        databaseHandler = new DatabaseHandler(this);
        fab = findViewById(R.id.floating_button);
        recyclerView = findViewById(R.id.recylerView);
        itemsList =new ArrayList<>();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.floating_button :
                createPopDialog();
                break;
            case R.id.saveButton:
                if(!babyItem.getText().toString().isEmpty() &&
                        !itemColor.getText().toString().isEmpty() &&
                        !itemQuantity.getText().toString().isEmpty() &&
                        !itemSize.getText().toString().isEmpty()
                ) {
                    saveItem(view);
                } else {
                    Snackbar.make(view,"Empty Fields not allowed",Snackbar.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void saveItem(View view) {
        BabyItems items = new BabyItems();
        Log.d(TAG, "saveItem: " + "button clicked");

        String newItem = babyItem.getText().toString().trim();
        String newColor = itemColor.getText().toString().trim();
        int quantity = Integer.parseInt(itemQuantity.getText().toString().trim());
        int size = Integer.parseInt(itemSize.getText().toString().trim());

        items.setItems(newItem);
        items.setColor(newColor);
        items.setQuantity(quantity);
        items.setSize(size);
        databaseHandler.addItem(items);
        Snackbar.make(view,"Item Saved ",Snackbar.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();
                startActivity(new Intent(ListActivity.this,ListActivity.class));
                finish();
            }
        },1200);

    }

    private void createPopDialog() {

        builder = new AlertDialog.Builder(this);
        //inflating the view
        View view = getLayoutInflater().inflate(R.layout.popup,null);

        babyItem = view.findViewById(R.id.babyItem);
        itemQuantity = view.findViewById(R.id.itemQuantity);
        itemColor = view.findViewById(R.id.itemColor);
        itemSize = view.findViewById(R.id.itemSize);
        saveButton = view.findViewById(R.id.saveButton);

        saveButton.setOnClickListener(this);

        builder.setView(view);
        dialog = builder.create();
        dialog.show();
    }
}