package com.test.mylupusproject.ui.data;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.view.MenuHost;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Lifecycle;
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

    private FirestoreRecyclerAdapter<DocumentModel, DocumentAdapter.ViewHolder> causesRecyclerAdapter = null;
    private FirestoreRecyclerAdapter<DocumentModel, DocumentAdapter.ViewHolder> eventRecyclerAdapter = null;
    private RecyclerView recyclerView = null;
    private RecyclerView eventRecyclerView = null;
    private static final String TAG = "DataList";
    private Context context = null;
    private DocumentAdapter causesDocumentAdapter = null;
    private DocumentAdapter eventDocumentAdapter = null;

    public View onCreateView(@Nonnull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = this.getContext();
        View root = inflater.inflate(R.layout.fragment_data_lists, container, false);
        TextView eventLabel = root.findViewById(R.id.event_label);
        eventLabel.getPaint().setUnderlineText(true);
        TextView causesLabel = root.findViewById(R.id.causes_label);
        causesLabel.getPaint().setUnderlineText(true);

        recyclerView = root.findViewById(R.id.document_recycler_view);
        ExpandHelper expandHelper = new ExpandHelper();
        recyclerView.setLayoutManager(new LinearLayoutManager(context) {
            @Override
            public void onLayoutCompleted(RecyclerView.State state) {
                //Log.d("DocumentFragment", "onLayoutCompleted: " + state);
                if (expandHelper.getExpandValue() == true) {
                    DocumentAdapter.ViewHolder viewHolder = (DocumentAdapter.ViewHolder) recyclerView.findViewHolderForAdapterPosition(expandHelper.getPosition());
                    Log.d("DocumentFragment", "onLayoutCompleted: ViewHolder ContainerId: " + viewHolder.getContainerId());
                    viewHolder.getListenerHelper().expandCollapseCardView(false);
                    expandHelper.reset();
                }
            }
        });
        FragmentActivity fragmentActivity = getActivity();
        causesDocumentAdapter = new DocumentAdapter(root, context, getChildFragmentManager(), "Causes", fragmentActivity, expandHelper);
        causesRecyclerAdapter = causesDocumentAdapter.getAdapter("root");
        recyclerView.setAdapter(causesRecyclerAdapter);

        eventRecyclerView = root.findViewById(R.id.event_document_recycler_view);
        ExpandHelper eventExpandHelper = new ExpandHelper();
        eventRecyclerView.setLayoutManager(new LinearLayoutManager(context) {
            @Override
            public void onLayoutCompleted(RecyclerView.State state) {
                //Log.d("DocumentFragment", "onLayoutCompleted: " + state);
                if (eventExpandHelper.getExpandValue() == true) {
                    DocumentAdapter.ViewHolder viewHolder = (DocumentAdapter.ViewHolder) eventRecyclerView.findViewHolderForAdapterPosition(eventExpandHelper.getPosition());
                    Log.d("DocumentFragment Event", "onLayoutCompleted: ViewHolder ContainerId: " + viewHolder.getContainerId());
                    viewHolder.getListenerHelper().expandCollapseCardView(false);
                    eventExpandHelper.reset();
                }
            }
        });
        eventDocumentAdapter = new DocumentAdapter(root, context, getChildFragmentManager(), "Events", fragmentActivity, eventExpandHelper);
        eventRecyclerAdapter = eventDocumentAdapter.getAdapter("root");
        eventRecyclerView.setAdapter(eventRecyclerAdapter);

        MenuHost menuHost = requireActivity();
        menuHost.addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.menu_main,menu);
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                switch(menuItem.getItemId()) {
                    case R.id.add:
                        Log.d("DocumentFragment", "addRootItem: Adding new document place holder: AAAAAAAA");
                        DocumentModel newDocument = new DocumentModel(null, "null", "Causes", "AAAAAAAA");
                        FirebaseFirestore.getInstance().collection("Causes").add(newDocument)
                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        Log.d("DocumentFragment", "addRootItem: Document place holder added: " +  documentReference.getId());
                                        //FirebaseFirestore.getInstance().document(documentReference.getPath()).update("path", documentReference.getPath());
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
        },getViewLifecycleOwner());

        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        causesRecyclerAdapter.startListening();
        eventRecyclerAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();

        if (causesRecyclerAdapter != null) {
            causesRecyclerAdapter.stopListening();
        }
        if (eventRecyclerAdapter != null) {
            eventRecyclerAdapter.stopListening();
        }
    }
}