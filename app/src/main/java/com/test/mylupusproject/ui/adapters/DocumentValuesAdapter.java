package com.test.mylupusproject.ui.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.test.mylupusproject.R;
import com.test.mylupusproject.ui.data.DocumentModel;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Random;

public class DocumentValuesAdapter {
    private static final String TAG = "DocumentValuesAdapter";
    private FirestoreRecyclerAdapter<DocumentModel, DocumentValuesAdapter.ViewHolder> documentAdapter;
    private View root = null;
    private Context context = null;
    private FragmentManager fragmentManager = null;
    private String queryString = null;
    private boolean opened;
    private FragmentActivity mainFragmentActivity = null;

public DocumentValuesAdapter(View root, Context context, FragmentManager fragmentManager, String queryString, FragmentActivity mainFragmentActivity) {
        this.root = root;
        this.context = context;
        this.fragmentManager = fragmentManager;
        this.queryString = queryString;
        this.mainFragmentActivity = mainFragmentActivity;
    }

    public FirestoreRecyclerAdapter<DocumentModel, DocumentValuesAdapter.ViewHolder> getAdapter() {
        CollectionReference query = FirebaseFirestore.getInstance().collection(queryString);
        FirestoreRecyclerOptions<DocumentModel> options = new FirestoreRecyclerOptions.Builder<DocumentModel>().setQuery(query, DocumentModel.class).build();
        documentAdapter = new FirestoreRecyclerAdapter<DocumentModel, DocumentValuesAdapter.ViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull DocumentModel documentModel) {
                Log.d(TAG, "Binding: " + documentModel.getDocId() + " Position: " + position);
                holder.setItemName(documentModel.getDocId());
                holder.updateContainerId();
                holder.setListeners(documentModel);
            }

            @NonNull
            @Override
            public DocumentValuesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.document_value_item, parent, false);
                return new DocumentValuesAdapter.ViewHolder(view, context);
            }
        };
        return documentAdapter;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private View view;
        private String documentName;
        private Context context;
        private int containerId;
        private Random r;

        public ViewHolder(@NonNull View itemView, Context context) {
            super(itemView);
            view = itemView;
            this.context = context;
            r = new Random();
        }

        void updateContainerId() {
            FragmentContainerView fragmentContainerView = view.findViewById(R.id.fragment_container);
            if (fragmentContainerView != null) {
                containerId = r.nextInt(1000000);
                while(view.findViewById(containerId) != null) {
                    containerId = r.nextInt(1000000);
                }
                fragmentContainerView.setId(containerId);
                Log.d(TAG, "New ContainerId: " + fragmentContainerView.getId());
            }
        }

        void setItemName(String documentName) {
            this.documentName = documentName;
            TextView textView = view.findViewById(R.id.document_name);
            textView.setText(documentName);
        }

        void setListeners(@NonNull DocumentModel documentModel) {
            ImageButton moreButton = view.findViewById(R.id.more_button);
            moreButton.setOnClickListener(view -> {
                moreButton.setClickable(false);
                BottomNavigationView navBar = mainFragmentActivity.findViewById(R.id.nav_view);
                ConstraintLayout bottomSlideMenu = mainFragmentActivity.findViewById(R.id.bottom_slide_menu);
                if(!opened) {
                    TranslateAnimation animateNavBarDown = new TranslateAnimation(
                            0,
                            0,
                            0,
                            navBar.getHeight());
                    animateNavBarDown.setDuration(500);
                    navBar.startAnimation(animateNavBarDown);
                    navBar.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            navBar.setVisibility(View.GONE);
                            bottomSlideMenu.setVisibility(View.INVISIBLE);
                            bottomSlideMenu.setVisibility(View.VISIBLE);
                            TranslateAnimation animateSlideMenuUp = new TranslateAnimation(
                                    0,
                                    0,
                                    bottomSlideMenu.getHeight(),
                                    0);
                            animateSlideMenuUp.setDuration(500);
                            bottomSlideMenu.startAnimation(animateSlideMenuUp);
                            bottomSlideMenu.setVisibility(View.VISIBLE);
                            moreButton.setClickable(true);
                        }
                    }, 500);
                } else {
                    TranslateAnimation animateSlideMenuDown = new TranslateAnimation(
                            0,
                            0,
                            0,
                            bottomSlideMenu.getHeight());
                    animateSlideMenuDown.setDuration(500);
                    bottomSlideMenu.startAnimation(animateSlideMenuDown);
                    bottomSlideMenu.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            bottomSlideMenu.setVisibility(View.GONE);
                            navBar.setVisibility(View.INVISIBLE);
                            TranslateAnimation animateNavBarUp = new TranslateAnimation(
                                    0,
                                    0,
                                    navBar.getHeight(),
                                    0);
                            animateNavBarUp.setDuration(500);
                            navBar.startAnimation(animateNavBarUp);
                            navBar.setVisibility(View.VISIBLE);
                            moreButton.setClickable(true);
                        }
                    }, 500);
                }
                opened = !opened;
            });

//            ImageButton addButton = mainFragmentActivity.findViewById(R.id.add_button);
//            addButton.setOnClickListener(view -> {
//                Toast.makeText(context, "Add button Clicked! " + this.documentName, Toast.LENGTH_LONG).show();
//            });
//
//            ImageButton editButton = mainFragmentActivity.findViewById(R.id.edit_button);
//            editButton.setOnClickListener(view -> {
//                Toast.makeText(context, "Edit button Clicked! " + this.documentName, Toast.LENGTH_LONG).show();
//            });
//
//            ImageButton deleteButton = mainFragmentActivity.findViewById(R.id.delete_button);
//            deleteButton.setOnClickListener(view -> {
//                Toast.makeText(context, "Delete button Clicked! " + this.documentName, Toast.LENGTH_LONG).show();
//            });
        }
    }
}
