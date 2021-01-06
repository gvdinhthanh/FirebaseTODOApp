package com.example.firebasetodoapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

public class EditTodoActivity extends AppCompatActivity {

    private EditText edtTitle;
    private EditText edtContent;
    private Button btnUpdate;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_edit_todo);

        db = FirebaseFirestore.getInstance();

        edtTitle = findViewById(R.id.edtTitle);
        edtContent = findViewById(R.id.edtContent);
        btnUpdate = findViewById(R.id.btnUpdate);

        Intent myIntent = getIntent();
        String id = myIntent.getStringExtra("id");
        String title = myIntent.getStringExtra("title");
        String content = myIntent.getStringExtra("content");
        edtTitle.setText(title);
        edtContent.setText(content);

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = edtTitle.getText().toString().trim();
                String content = edtContent.getText().toString().trim();
                if (title.isEmpty()) {
                    Toast.makeText(EditTodoActivity.this, "Vui lòng nhập title", Toast.LENGTH_SHORT).show();
                } if (content.isEmpty()) {
                    Toast.makeText(EditTodoActivity.this, "Vui lòng nhập content", Toast.LENGTH_SHORT).show();
                } else {
                    db.collection("todos").document(id)
                            .set((new TodoModel(
                                    title,
                                    content
                            )).toJson())
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(EditTodoActivity.this, "Đã cập nhật thành công", Toast.LENGTH_SHORT).show();
                                    setResult(RESULT_OK);
                                    finish();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(Exception e) {
                                    Toast.makeText(EditTodoActivity.this, "Thất bại!", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; go home
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}