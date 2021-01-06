package com.example.firebasetodoapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ListView todoListView;
    private Button addButton;
    private Button updateButton;
    private Button deleteButton;
    private int idKhoa;
    private EditText edtTitle;
    private EditText edtContent;
    private ArrayList<TodoModel> todos;
    private FirebaseFirestore db;
    private MyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = FirebaseFirestore.getInstance();

        todoListView = findViewById(R.id.listViewTodo);
        addButton = findViewById(R.id.buttonAddTodo);
        edtTitle = findViewById(R.id.editTextTitle);
        edtContent = findViewById(R.id.editTextContent);

        todos = new ArrayList<TodoModel>();

        loadTodos();

        adapter = new MyAdapter(MainActivity.this, todos);

        todoListView.setAdapter(adapter);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = edtTitle.getText().toString().trim();
                String content = edtContent.getText().toString().trim();

                if (title.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Vui lòng nhập title!", Toast.LENGTH_SHORT).show();
                } else if (content.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Vui lòng nhập content!", Toast.LENGTH_SHORT).show();
                } else {
                    TodoModel todo = new TodoModel(title, content);
                    db.collection("todos").add(todo.toJson())
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Toast.makeText(MainActivity.this, "Thêm thành công!", Toast.LENGTH_SHORT).show();
                                    edtTitle.setText("");
                                    edtContent.setText("");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(Exception e) {
                                    Toast.makeText(MainActivity.this, "Thất bại!", Toast.LENGTH_SHORT).show();
                                    Log.d("Failed", e.getMessage());
                                }
                            });
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2409 && resultCode == RESULT_OK) {
            loadTodos();
            adapter.notifyDataSetChanged();
        }
    }

    private void loadTodos() {
        CollectionReference todosRef = db.collection("todos");
        todosRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w("WARN", "Listen failed.", e);
                    return;
                }
                todos.clear();
                for (QueryDocumentSnapshot doc:value) {
                    todos.add(new TodoModel(
                            doc.getId(),
                            doc.getString("title"),
                            doc.getString("content")
                    ));
                }
                adapter.notifyDataSetChanged();
            }
        });
    }
}