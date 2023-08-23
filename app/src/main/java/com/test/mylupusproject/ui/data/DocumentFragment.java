package com.test.mylupusproject.ui.data;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.test.mylupusproject.R;
import com.test.mylupusproject.ui.adapters.DocumentAdapter;

import javax.annotation.Nonnull;

public class DocumentFragment extends Fragment {

    private FirestoreRecyclerAdapter<DocumentModel, DocumentAdapter.ViewHolder> recyclerAdapter = null;
    private static final String TAG = "DataList";
    private Context context = null;

    public View onCreateView(@Nonnull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = this.getContext();
        View root = inflater.inflate(R.layout.fragment_data_lists, container, false);
        RecyclerView recyclerView = root.findViewById(R.id.document_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        FragmentActivity fragmentActivity = getActivity();
        recyclerAdapter = new DocumentAdapter(root, context, getChildFragmentManager(), "Root", fragmentActivity).getAdapter("root");
        recyclerView.setAdapter(recyclerAdapter);
        setHasOptionsMenu(true);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.add:
                Log.d("DocumentFragment", "addRootItem: Clicked");
                DocumentModel newDocument = new DocumentModel(null, "null", "Root", "AAAAAAAA");
                FirebaseFirestore.getInstance().collection("Root").add(newDocument)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Log.d("DocumentFragment", "addRootItem: Adding new document place holder: " +  documentReference.getId());
                                FirebaseFirestore.getInstance().document(documentReference.getPath()).update("path", documentReference.getPath());
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("DocumentFragment", "Error adding document", e);
                            }
                        });

                return true;
        }
        return false;
    }
}