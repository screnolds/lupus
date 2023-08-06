package com.test.mylupusproject.ui.data;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import com.test.mylupusproject.R;
import com.test.mylupusproject.ui.adapters.DocumentAdapter;

import org.checkerframework.checker.nullness.qual.NonNull;

import javax.annotation.Nonnull;

public class DocumentFragment extends Fragment {

    private FirestoreRecyclerAdapter<DocumentModel, DocumentAdapter.ViewHolder> recyclerAdapter = null;
    private static final String TAG = "DataList";
    private Context context = null;

    public View onCreateView(@Nonnull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = this.getContext();
        View root = inflater.inflate(R.layout.frament_data_lists, container, false);
        RecyclerView recyclerView = root.findViewById(R.id.document_recycler_view);
        FragmentActivity fragmentActivity = getActivity();
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerAdapter = new DocumentAdapter(root, context, getChildFragmentManager(), "Root", fragmentActivity).getAdapter("root");
        recyclerView.setAdapter(recyclerAdapter);
        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        recyclerAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();

        if (recyclerAdapter != null) {
            recyclerAdapter.stopListening();
        }
    }
}