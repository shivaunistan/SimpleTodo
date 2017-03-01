package com.codepath.simpletodo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

public class EditItemActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        final int pos = getIntent().getIntExtra("itemPos", -1);
        final String old = getIntent().getStringExtra("item");
        final EditText et = (EditText) findViewById(R.id.editText2);
        et.setText(old);
        View button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("item", et.getText().toString());
                intent.putExtra("itemPos", pos);
                setResult(Activity.RESULT_OK, intent);
                EditItemActivity.this.finish();
            }
        });
    }

}