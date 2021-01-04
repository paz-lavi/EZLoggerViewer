package com.paz.ezloggerview.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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
import com.paz.ezloggerview.R;
import com.paz.ezloggerview.adapters.HistoryViewHolder;
import com.paz.ezloggerview.callbacks.SearchCallback;
import com.paz.ezloggerview.data.SavedQuery;
import com.paz.ezloggerview.databinding.FragmentSavedBinding;

import java.util.Objects;

import static com.paz.ezloggerview.helpers.Constants.SAVED_QUERY;
import static com.paz.ezloggerview.helpers.Constants.TIMESTAMP;
import static com.paz.ezloggerview.helpers.Constants.USERS;

public class SavedFragment extends Fragment {
    private FragmentSavedBinding binding;
    private SearchCallback searchCallback;
    private Query query;

    private FirestorePagingAdapter<SavedQuery, HistoryViewHolder> mAdapter;


    public SavedFragment setSearchCallback(SearchCallback searchCallback) {
        Log.d("pttt", "setSearchCallback: ");

        this.searchCallback = searchCallback;
        if (searchCallback == null)
            Log.d("pttt", "setSearchCallback: null");
        return this;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSavedBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        query = FirebaseFirestore.getInstance()
                .collection(USERS)
                .document(FirebaseAuth
                        .getInstance()
                        .getCurrentUser().getUid())
                .collection(SAVED_QUERY).orderBy(TIMESTAMP, Query.Direction.DESCENDING);
        setView();
        setupAdapter();

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
        FirestorePagingOptions options = new FirestorePagingOptions.Builder<SavedQuery>()
                .setLifecycleOwner(this)
                .setQuery(query, config, SavedQuery.class)
                .build();

        // Instantiate Paging Adapter
        mAdapter = new FirestorePagingAdapter<SavedQuery, HistoryViewHolder>(options) {
            @NonNull
            @Override
            public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = getLayoutInflater().inflate(R.layout.card_saved, parent, false);
                Log.e("pttt", "onCreateViewHolder");

                return new HistoryViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull HistoryViewHolder viewHolder, int i, @NonNull SavedQuery query) {
                // Bind to ViewHolder
                Log.e("pttt", "onBindViewHolder");

                viewHolder.bind(Objects.requireNonNull(getActivity()), query, searchCallback);
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
                        binding.savedSPSwipeRefreshLayout.setRefreshing(true);
                        break;

                    case LOADED:
                        binding.savedSPSwipeRefreshLayout.setRefreshing(false);
                        break;

                    case ERROR:
                        Toast.makeText(
                                Objects.requireNonNull(getActivity()).getApplicationContext(),
                                "Error Occurred!",
                                Toast.LENGTH_SHORT
                        ).show();

                        binding.savedSPSwipeRefreshLayout.setRefreshing(false);
                        break;

                    case FINISHED:
                        binding.savedSPSwipeRefreshLayout.setRefreshing(false);
                        break;
                }
            }

        };

        // Finally Set the Adapter to mRecyclerView
        binding.savedLSTCards.setAdapter(mAdapter);

    }


    private void setView() {
        binding.savedLSTCards.setHasFixedSize(true);
        binding.savedLSTCards.setLayoutManager(new LinearLayoutManager(getActivity()));

        binding.savedSPSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mAdapter.refresh();
            }
        });

    }


    @Override
    public void onStart() {
        super.onStart();
        Log.d("pttt", "onStart: saved");
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
    public void onStop() {
        super.onStop();
        mAdapter.stopListening();
    }


}
