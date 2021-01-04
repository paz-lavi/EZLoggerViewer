package com.paz.ezloggerview.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.firebase.ui.firestore.paging.FirestorePagingAdapter;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.firebase.ui.firestore.paging.LoadingState;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.paz.ezloggerview.R;
import com.paz.ezloggerview.adapters.SessionViewHolder;
import com.paz.ezloggerview.callbacks.ShowLogsCallback;
import com.paz.ezloggerview.data.SavedQuery;
import com.paz.ezloggerview.data.SessionDocument;
import com.paz.ezloggerview.databinding.ActivityResultsBinding;
import com.paz.ezloggerview.helpers.Constants;

import java.lang.reflect.Type;
import java.util.ArrayList;

import static com.paz.ezloggerview.helpers.Constants.CHIP_INDEX;
import static com.paz.ezloggerview.helpers.Constants.COSTUMER_ID;
import static com.paz.ezloggerview.helpers.Constants.DATE_MAX;
import static com.paz.ezloggerview.helpers.Constants.DATE_MIN;
import static com.paz.ezloggerview.helpers.Constants.EZ_LOG_ID;
import static com.paz.ezloggerview.helpers.Constants.LOGS_SESSIONS;
import static com.paz.ezloggerview.helpers.Constants.MANUFACTURER_NAME;
import static com.paz.ezloggerview.helpers.Constants.PACKAGE_NAME;
import static com.paz.ezloggerview.helpers.Constants.SAVED_QUERY;
import static com.paz.ezloggerview.helpers.Constants.SESSION_COUNTER;
import static com.paz.ezloggerview.helpers.Constants.TIMESTAMP;
import static com.paz.ezloggerview.helpers.Constants.USERS;


public class ResultsActivity extends AppCompatActivity implements ShowLogsCallback {
    private ActivityResultsBinding binding;
    private Query query;

    private FirestorePagingAdapter<SessionDocument, SessionViewHolder> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_results);
        binding = ActivityResultsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        Bundle b = getIntent().getExtras();
        setView();
        if (b != null) {
            search(b);
            setupAdapter();
            saveQuery(b);
        }

    }

    private void saveQuery(Bundle b) {
        FirebaseFirestore.getInstance()
                .collection(USERS)
                .document(FirebaseAuth
                        .getInstance()
                        .getCurrentUser().getUid())
                .collection(SAVED_QUERY)
                .add(new SavedQuery().fromBundle(b));
    }

    private void setView() {
        binding.resultsLSTCards.setHasFixedSize(true);
        binding.resultsLSTCards.setLayoutManager(new LinearLayoutManager(this));

        binding.resultsSPSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mAdapter.refresh();
            }
        });

    }


    private void search(Bundle b) {
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<String>>() {
        }.getType();
        long d1 = b.getLong(DATE_MIN);
        long d2 = b.getLong(DATE_MAX);

        query = FirebaseFirestore.getInstance().collection(USERS)
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .collection(LOGS_SESSIONS)
                .whereGreaterThan(TIMESTAMP, d1)
                .whereLessThan(TIMESTAMP, d2)
                .orderBy(TIMESTAMP, Query.Direction.DESCENDING);


        query = query.whereGreaterThanOrEqualTo(TIMESTAMP, d1).whereLessThanOrEqualTo(TIMESTAMP, d2);

        int counter = b.getInt(SESSION_COUNTER, -1);
        if (counter != -1) {
            Log.d("pttt", "search: SESSION_COUNTER " + counter);

            switch (b.getInt(CHIP_INDEX, -1)) {
                case 0:
                    query = query.whereGreaterThan(SESSION_COUNTER, counter);
                    break;
                case 1:
                    query = query.whereEqualTo(SESSION_COUNTER, counter);
                    break;
                case 2:
                    query = query.whereLessThan(SESSION_COUNTER, counter);
                    break;
            }
        }

        String idJSON = b.getString(EZ_LOG_ID, "");
        if (!idJSON.isEmpty()) {
            Log.d("pttt", "search: id " + idJSON);
            ArrayList<String> ids = gson.fromJson(idJSON, type);
            if (!ids.isEmpty())
                query = ids.size() > 1 ? query.whereIn(EZ_LOG_ID, ids) : query.whereEqualTo(EZ_LOG_ID, ids.get(0));

        }
        String costumerJSON = b.getString(COSTUMER_ID, "");
        if (!costumerJSON.isEmpty()) {
            Log.d("pttt", "search: COSTUMER_ID " + costumerJSON);
            ArrayList<String> costumers = gson.fromJson(costumerJSON, type);
            if (!costumers.isEmpty())
                query = costumers.size() > 1 ? query.whereIn(COSTUMER_ID, costumers) : query.whereEqualTo(COSTUMER_ID, costumers.get(0));

        }

        String manufacturerJSON = b.getString(MANUFACTURER_NAME, "");
        if (!manufacturerJSON.isEmpty()) {
            Log.d("pttt", "search: manufacturer " + manufacturerJSON);
            ArrayList<String> manufacturers = gson.fromJson(manufacturerJSON, type);
            if (!manufacturers.isEmpty())
                query = manufacturers.size() > 1 ? query.whereIn(MANUFACTURER_NAME, manufacturers) : query.whereEqualTo(MANUFACTURER_NAME, manufacturers.get(0));
        }

        String packagesJSON = b.getString(PACKAGE_NAME, "");
        if (!packagesJSON.isEmpty()) {
            Log.d("pttt", "search: id " + packagesJSON);
            ArrayList<String> packages = gson.fromJson(packagesJSON, type);
            if (!packages.isEmpty())
                query = packages.size() > 1 ? query.whereIn(PACKAGE_NAME, packages) : query.whereEqualTo(PACKAGE_NAME, packages.get(0));

        }

    }


    private void setupAdapter() {

        // Init Paging Configuration
        PagedList.Config config = new PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setPrefetchDistance(2)
                .setPageSize(10)
                .build();
        Log.d("pttt", "setupAdapter: ");
        // Init Adapter Configuration
        FirestorePagingOptions options = new FirestorePagingOptions.Builder<SessionDocument>()
                .setLifecycleOwner(this)
                .setQuery(query, config, SessionDocument.class)
                .build();

        // Instantiate Paging Adapter
        mAdapter = new FirestorePagingAdapter<SessionDocument, SessionViewHolder>(options) {
            @NonNull
            @Override
            public SessionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = getLayoutInflater().inflate(R.layout.card_session, parent, false);
                Log.e("pttt", "onCreateViewHolder");

                return new SessionViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull SessionViewHolder viewHolder, int i, @NonNull SessionDocument doc) {
                // Bind to ViewHolder
                Log.e("pttt", "onBindViewHolder");

                viewHolder.bind(ResultsActivity.this, doc, ResultsActivity.this);
            }

            @Override
            protected void onError(@NonNull Exception e) {
                super.onError(e);
                Log.e("pttt", e.getMessage());
            }

            @Override
            protected void onLoadingStateChanged(@NonNull LoadingState state) {
                switch (state) {
                    case LOADING_INITIAL:
                    case LOADING_MORE:
                        binding.resultsSPSwipeRefreshLayout.setRefreshing(true);
                        break;

                    case LOADED:
                        binding.resultsSPSwipeRefreshLayout.setRefreshing(false);
                        break;

                    case ERROR:
                        Toast.makeText(
                                getApplicationContext(),
                                "Error Occurred!",
                                Toast.LENGTH_SHORT
                        ).show();

                        binding.resultsSPSwipeRefreshLayout.setRefreshing(false);
                        break;

                    case FINISHED:
                        binding.resultsSPSwipeRefreshLayout.setRefreshing(false);
                        break;
                }
            }

        };

        // Finally Set the Adapter to mRecyclerView
        binding.resultsLSTCards.setAdapter(mAdapter);

    }


    @Override
    protected void onStart() {
        super.onStart();
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d("pttt", document.getId() + " => " + document.getData());
                    }
                } else {
                    Log.d("pttt", "Error getting documents: ", task.getException());
                }
            }
        });
        mAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAdapter.stopListening();
    }

    @Override
    public void showLogs(SessionDocument document) {
        Intent i = new Intent(ResultsActivity.this, LogActivity.class);
        String json = new Gson().toJson(document);
        i.putExtra(Constants.DOC, json);
        startActivity(i);
    }
}