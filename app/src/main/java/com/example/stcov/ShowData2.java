package com.example.stcov;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class ShowData2 extends AppCompatActivity implements FirestoreAdapter.OnListItemClick {

    FirebaseFirestore firebaseFirestore;
    RecyclerView mFirestorelist;
    FirestoreAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_data2);

        firebaseFirestore = FirebaseFirestore.getInstance();
        mFirestorelist = findViewById(R.id.recyclerView);

        //Query
        Query query = firebaseFirestore.collection("users").whereEqualTo("role","admin");

        firebaseFirestore.collection("users").whereEqualTo("role","admin").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d("TAG", document.getId() + " => " + document.getData());
                    }
                } else {
                    Log.d("TAG", "Error getting documents: ", task.getException());
                }
            }
        });

        PagedList.Config config = new PagedList.Config.Builder()
                .setInitialLoadSizeHint(10)
                .setPageSize(3)
                .build();

        FirestorePagingOptions<User> options = new FirestorePagingOptions.Builder<User>()
                .setLifecycleOwner(this)
                .setQuery(query, config, User.class)
                .build();

        adapter = new FirestoreAdapter(options , this);

        mFirestorelist.setHasFixedSize(true);
        mFirestorelist.setLayoutManager(new LinearLayoutManager(this));
        mFirestorelist.setAdapter(adapter);

    }

    @Override
    public void onItemClick(DocumentSnapshot snapshot, int position) {
        Log.d("ITEM_CLICK","Clicked the item : "+position+" and the ID : "+snapshot.getId() );
        Intent i = new Intent(new Intent(getApplicationContext(),ManageAdmin.class));
        String uid = snapshot.getId();
        System.out.println(uid);
        i.putExtra("uid",uid);
        startActivity(i);
        finish();
    }
}
