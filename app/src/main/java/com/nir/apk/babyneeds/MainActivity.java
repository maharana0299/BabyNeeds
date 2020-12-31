package com.nir.apk.babyneeds;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.nir.apk.babyneeds.data.DatabaseHandler;
import com.nir.apk.babyneeds.model.BabyItems;
import com.nir.apk.babyneeds.util.Constants;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "Main Activity";
    private AlertDialog.Builder builder;
    private AlertDialog dialog;
    private Button saveButton;
    private EditText babyItem;
    private EditText itemQuantity;
    private EditText itemColor;
    private EditText itemSize;
    private DatabaseHandler databaseHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initializeUi();

        byPassActivity();

//        startActivity(new Intent(MainActivity.this,ListActivity.class));
//        finish();
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createPopupDialog();
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
            }
        });

        //check if items are saved
//        List<BabyItems> items = databaseHandler.getAllItems();
//        for (BabyItems items1: items) {
//            Log.d(TAG, "onCreate: " + items1.toString());
//        }
//        Log.d(TAG, "onCreate: item :"+ databaseHandler.getItem(2));

    }

    private void byPassActivity() {

        if (databaseHandler.getItemsCount() > 0) {
            startActivity(new Intent(MainActivity.this,ListActivity.class));
            finish();
        }
    }

    private void initializeUi() {
        databaseHandler = new DatabaseHandler(this);
    }

    private void createPopupDialog() {

        builder = new AlertDialog.Builder(this);

        //inflate the view
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public void onClick(View view) {
            switch (view.getId()) {
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
        //TODO: save each item to database
        //TODO: move to details screen

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
       // dialog.dismiss();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();
                startActivity(new Intent(MainActivity.this,ListActivity.class));
            }
        }, 1200
                );


    }
}