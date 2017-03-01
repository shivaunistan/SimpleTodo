package com.codepath.simpletodo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class SimpleTodoActivity extends AppCompatActivity {
    ListView lvItems;
    ArrayAdapter<String> itemsAdapter;
    ArrayList<String> items;

    //Adapter - allows us to easily display the contents of an ArrayList within a ListView
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_todo);
        lvItems = (ListView) findViewById(R.id.lvItems);
        items = new ArrayList<>();
        readItems(); //load items during OnCreate()
        itemsAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, items);
        lvItems.setAdapter(itemsAdapter);
        items.add("First Item");
        items.add("Second Item");
        //need to figure out how to stop these two lines form being added if text already exists
        setupListViewListener(); // this should invoke the listener from OnCreate
        setupEditListener();
    }

    //this is the method for setting up the listener - LongClickListener attached to items in ListView that remove the item and refresh the adapter
    private void setupListViewListener() {
        lvItems.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapter,
                                                   View item, int pos, long id) {
                        items.remove(pos);
                        itemsAdapter.notifyDataSetChanged();
                        writeItems();
                        return true; //saves items when a list item is removed
                    }
                });
    }

    private final int REQUEST_CODE = 10;
    private void setupEditListener() {
        lvItems.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapter,
                                            View item, int pos, long id) {
                        Intent intent = new Intent(SimpleTodoActivity.this, EditItemActivity.class);
                        intent.putExtra("item", items.get(pos));
                        intent.putExtra("itemPos", pos);
                        startActivityForResult(intent, REQUEST_CODE);
                    }
                });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            String item = data.getStringExtra("item");
            int itemPos = data.getIntExtra("itemPos", -1);
            if (itemPos >= 0) {
                items.set(itemPos, item);
                itemsAdapter.notifyDataSetChanged();
            }
            //SimpleTodoActivity();
        }
    }
//this adds items to the action bar if present - inflates the menu
    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_simple_todo, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    */

    //this method allows adding items that are input into the list
    public void onAddItem(View v) {
        EditText etNewItem = (EditText) findViewById(R.id.etNewItem);
        //SimpleTodoActivity(etNewItem);
        String itemText = etNewItem.getText().toString();
        itemsAdapter.add(itemText);
        etNewItem.setText("");

        writeItems();
        //SimpleTodoActivity(); //save items when a new list item is added
    }

    // this is the method to open a file and read a new-line-delimited list of items
    private void readItems() {
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, "todo.txt");
        try {
            items = new ArrayList<>(FileUtils.readLines(todoFile));
        } catch (IOException e) {
            items = new ArrayList<>();
        }
    }
    // this is the method to save a file and write a newline-delimited list of items
    private void writeItems() {
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, "todo.txt");
        try {
            FileUtils.writeLines(todoFile, items);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}