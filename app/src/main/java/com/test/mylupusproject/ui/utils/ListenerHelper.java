package com.test.mylupusproject.ui.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.ObservableSnapshotArray;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.test.mylupusproject.R;
import com.test.mylupusproject.ui.data.DocumentGroupsFragment;
import com.test.mylupusproject.ui.data.DocumentModel;
import com.test.mylupusproject.ui.data.DocumentValuesFragment;
import com.test.mylupusproject.ui.data.ExpandHelper;

import org.w3c.dom.Text;

public class ListenerHelper {
    private View root;
    private View view;
    private View parentView;
    FragmentActivity mainFragmentActivity;
    DocumentModel documentModel;
    Context context;
    ConstraintLayout bottomSlideMenu = null;
    BottomNavigationView navBar = null;
    View disabledBackground = null;
    int animateTransitionMs = 250;
    String docType = null;
    ImageButton arrow = null;
    LinearLayout hiddenView = null;
    ConstraintLayout moreMenu = null;
    FragmentManager fragmentManager = null;
    EditText addNewDocEditText = null;
    FirebaseFirestore db = null;
    ExpandHelper expandHelper = null;
    int position = 0;
    int containerId = 0;

    public ListenerHelper(View root, View view, FragmentActivity mainFragmentActivity, DocumentModel documentModel, Context context,
                          String docType, int containerId, FragmentManager fragmentManager, ExpandHelper expandHelper, int position) {
        this.root = root;
        this.view = view;
        this.mainFragmentActivity = mainFragmentActivity;
        this.documentModel = documentModel;
        this.context = context;
        this.docType = docType;
        this.fragmentManager = fragmentManager;
        this.containerId = containerId;
        this.position = position;
        this.expandHelper = expandHelper;
        bottomSlideMenu = mainFragmentActivity.findViewById(R.id.bottom_slide_menu);
        navBar = mainFragmentActivity.findViewById(R.id.nav_view);
        disabledBackground = mainFragmentActivity.findViewById(R.id.disabled_background);
        arrow = view.findViewById(R.id.arrow_button);
        hiddenView = view.findViewById(R.id.hidden_view);
        moreMenu = view.findViewById(R.id.fixed_layout_more_menu);
        moreMenu.setVisibility(View.GONE);
        addNewDocEditText = view.findViewById(R.id.document_name_edit);
        db = FirebaseFirestore.getInstance();
    }

    public void addArrowButtonClickListeners() {
        arrow.setOnClickListener(view -> {
            Log.d("ListenerHelper", "addArrowButtonClickListeners: Clicked for document " + documentModel.getDocId() + " ContainerId: " + containerId);
            animateSlideMenuRight();
            expandCollapseCardView(true);
        });
    }

    public void expandCollapseCardView(boolean toggle) {
        // If the CardView is already expanded, set its visibility to gone and change the expand less icon to expand more.
        boolean reloadMoreMenu = false;

        if (moreMenu.getVisibility() == View.VISIBLE) {
            reloadMoreMenu = true;
        }
        moreMenu.setVisibility(View.GONE);
        if (toggle && hiddenView.getVisibility() == View.VISIBLE) {
            hiddenView.setVisibility(View.GONE);
            arrow.setImageResource(R.drawable.ic_baseline_keyboard_arrow_right_24);
        } else {
            // If the CardView is not expanded, set its visibility to visible and change the expand more icon to expand less.
//            expandCardView(containerId, fragmentManager);
            expandCardView();
            if (reloadMoreMenu == true) {
                moreMenu.setVisibility(View.VISIBLE);
            }
        }
    }

    void expandCardView() {
        String docId = documentModel.getDocId();
        Log.d("ListenerHelper", "expandCardView: Document: " + docId + " ContainerId: " + containerId);
        hiddenView.setVisibility(View.VISIBLE);
        arrow.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_24);

        Fragment fragment = fragmentManager.findFragmentByTag(docId);
        if (fragment == null) {
            if (documentModel.getChildrenType().equals("groups")) {
                Log.d("ListenerHelper", "expandCardView: Adding new group fragment for Document: " + docId + " ContainerId: " + containerId);
                fragment = new DocumentGroupsFragment(documentModel.getPath()  + "/" + documentModel.getDocId() + "/groups");
                fragmentManager.beginTransaction().add(containerId, fragment, docId).commit();
            } else if (documentModel.getChildrenType().equals("values")) {
                Log.d("ListenerHelper", "expandCardView: Adding new value fragment for Document: " + docId + " ContainerId: " + containerId);
                fragment = new DocumentValuesFragment(documentModel.getPath()  + "/" + documentModel.getDocId() + "/values");
                fragmentManager.beginTransaction().add(containerId, fragment, docId).commit();
            }
        } else {
            if (!fragment.isVisible()) {
                Log.d("ListenerHelper", "expandCardView: Fragment already exists but not visible for Document: " + docId  + " ContainerId: " + containerId);
                fragmentManager.beginTransaction().show(fragment).commit();
            }
        }
    }

    public void addMoreButtonClickListeners() {
        LinearLayout addFolderButtonContainer =  view.findViewById(R.id.add_folder_button_container);
        if (documentModel.getChildrenType().contentEquals("null") || (docType.contentEquals("root") && documentModel.getChildrenType().contentEquals("groups"))) {
            ImageButton addFolderButtonContainerOverlay =  view.findViewById(R.id.add_folder_button_container_overlay);
            addFolderButtonContainerOverlay.setOnClickListener(addFolderButtonContainerOverlayView -> {
                Log.d("ListenerHelper", "addFolderButtonContainerOverlay: Clicked for Document: " + documentModel.getDocId() + " ContainerId: " + containerId);
                animateSlideMenuRight();

                Log.d("ListenerHelper", "addFolderButtonContainerOverlay: Adding new document place holder: AAAAAAAA ContainerId: " + containerId);
                DocumentModel newDocument = new DocumentModel(null, "values", documentModel.getPath() + "/" + documentModel.getDocId() + "/groups", "AAAAAAAA");
                db.collection(documentModel.getPath() + "/" + documentModel.getDocId() + "/groups").add(newDocument)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Log.d("ListenerHelper", "addFolderButtonContainerOverlay: Document place holder added: " +  documentReference.getId() + "ContainerId: " + containerId);
                                //db.document(documentReference.getPath() + "/" + documentModel.getDocId()).update("path", documentReference.getPath());
                                if (documentModel.getChildrenType().contentEquals("null")) {
                                    Log.d("ListenerHelper", "addFolderButtonContainerOverlay: Updating Document childrenType ContainerId: " + containerId);
                                    db.document(documentModel.getPath() + "/" + documentModel.getDocId()).update("childrenType", "groups");
                                    expandHelper.setExpand(position);
                                    documentModel.setChildrenType("groups");
                                } else {
                                    expandCollapseCardView(false);
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("ListenerHelper", "Error adding document", e);
                            }
                        });
            });
        } else {
            if (addFolderButtonContainer != null) {
                addFolderButtonContainer.setVisibility(View.GONE);
            }
        }

        LinearLayout addButtonContainer =  view.findViewById(R.id.add_button_container);
        if (documentModel.getChildrenType().contentEquals("null") || docType.contentEquals("groups") || (docType.contentEquals("root") && documentModel.getChildrenType().contentEquals("values"))) {
            ImageButton addButtonContainerOverlay =  view.findViewById(R.id.add_button_container_overlay);
            addButtonContainerOverlay.setOnClickListener(addFolderButtonContainerOverlayView -> {
                Log.d("ListenerHelper", "addButtonContainerOverlay: Clicked for Document: " + documentModel.getDocId()  + "ContainerId: " + containerId);
                animateSlideMenuRight();

                Log.d("ListenerHelper", "addButtonContainerOverlay: Adding new document place holder: AAAAAAAA" +  "ContainerId: " + containerId);
                DocumentModel newDocument = new DocumentModel(null, "none", documentModel.getPath() + "/" + documentModel.getDocId() + "/values", "AAAAAAAA");
                db.collection(documentModel.getPath() + "/" + documentModel.getDocId() + "/values").add(newDocument)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Log.d("ListenerHelper", "addButtonContainerOverlay: New document place holder added for:" + documentReference.getId() + "ContainerId: " + containerId);
                                //db.document(documentReference.getPath()).update("path", documentReference.getPath());
                                if (documentModel.getChildrenType().contentEquals("null")) {
                                    db.document(documentModel.getPath()  + "/" + documentModel.getDocId()).update("childrenType", "values");
                                    expandHelper.setExpand(position);
                                    documentModel.setChildrenType("values");
                                }
                                expandCollapseCardView(false);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("ListenerHelper", "Error adding document", e);
                            }
                        });
            });
        } else {
            if (addButtonContainer != null) {
                addButtonContainer.setVisibility(View.GONE);
            }
        }

        ImageButton editButtonContainerOverlay =  view.findViewById(R.id.edit_button_container_overlay);
        if (editButtonContainerOverlay != null) {
            editButtonContainerOverlay.setOnClickListener(editButtonContainerOverlayView -> {
                Log.d("ListenerHelper", "editButtonContainerOverlay: Clicked for Document: " + documentModel.getDocId() + "ContainerId: " + containerId);
                animateSlideMenuRight();
                togglePlacholderView();
            });
        }

        ImageButton deleteButtonContainerOverlay = view.findViewById(R.id.delete_button_container_overlay);
        if (deleteButtonContainerOverlay != null) {
            String documentName = documentModel.getName();
            deleteButtonContainerOverlay.setOnClickListener(deleteButtonContainerOverlayView -> {
                Log.d("ListenerHelper", "deleteButtonContainerOverlay: Clicked for Document: " + documentName + "ContainerId: " + containerId);
                animateSlideMenuRight();
                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                alert.setTitle("Are you sure you want to delete?").setCancelable(false);
                alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Log.d("ListenerHelper", "deleteButton Confirmed: Clicked for Document: " + documentName + "ContainerId: " + containerId);
                            db.document(documentModel.getPath()  + "/" + documentModel.getDocId()).delete()
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Log.d("ListenerHelper", "Document deleted: " + documentName + "ContainerId: " + containerId);
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            //TODO
                                        }
                                    });
                        }
                    });
                alert.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
                alert.show();
            });
        }

        ImageButton moreButton = view.findViewById(R.id.more_button);
        moreButton.setOnClickListener(moreButtonView -> {
            Log.d("ListenerHelper", "moreButton: Clicked for Document: " + documentModel.getName() + "ContainerId: " + containerId);
            moreMenu.requestFocus();
            moreMenu.setVisibility(View.INVISIBLE);
            TranslateAnimation animateSlideMenuLeft = new TranslateAnimation(
                    moreMenu.getWidth(),
                    0,
                    0,
                    0);
            animateSlideMenuLeft.setDuration(animateTransitionMs);
            moreMenu.startAnimation(animateSlideMenuLeft);
            moreMenu.postDelayed(new Runnable() {
                @Override
                public void run() {
                    moreMenu.setVisibility(View.VISIBLE);
                }
            }, animateTransitionMs);
        });
    }

    public void addLessButtonClickListeners() {
        ImageButton lessButton = view.findViewById(R.id.less_button);
        lessButton.setOnClickListener(view -> {
            animateSlideMenuRight();
        });
    }

    void animateSlideMenuRight() {
        if(moreMenu.getVisibility() == View.VISIBLE) {
            TranslateAnimation animateSlideMenuRight = new TranslateAnimation(
                    0,
                    moreMenu.getWidth(),
                    0,
                    0);
            animateSlideMenuRight.setDuration(animateTransitionMs);
            moreMenu.startAnimation(animateSlideMenuRight);
            moreMenu.postDelayed(new Runnable() {
                @Override
                public void run() {
                    moreMenu.setVisibility(View.GONE);
                }
            }, animateTransitionMs);
        }
    }

    public void addEditTextDoneListener() {
        addNewDocEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                switch (actionId) {
                    case EditorInfo.IME_ACTION_DONE:
                        String text = addNewDocEditText.getText().toString().trim();
                        if (text.length() > 0) {
                            //String collectionPath = documentModel.getPath().replace("/" + documentModel.getDocId(), "");
                            String collectionPath = documentModel.getPath();
                            db.collection(collectionPath).whereEqualTo("name", text).get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful()) {
                                                if (task.getResult().size() <= 0) {
                                                    db.document(documentModel.getPath() + "/" + documentModel.getDocId()).update("name", text)
                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void unused) {
                                                                    Log.d("ListenerHelper", "Updated new document name to: " + text + " ContainerId: " + containerId);
                                                                }
                                                            })
                                                            .addOnFailureListener(new OnFailureListener() {
                                                                @Override
                                                                public void onFailure(@NonNull Exception e) {
                                                                    //TODO
                                                                }
                                                            });
                                                } else {
                                                    AlertDialog.Builder alert = new AlertDialog.Builder(context);
                                                    alert.setTitle("The specified name already exists. Please enter a different name.").setCancelable(false);
                                                    alert.setPositiveButton(android.R.string.yes, null);
                                                    alert.show();
                                                }
                                            }
                                        }
                                    });
                        }
                    default:
                        return false;
                }
            }
        });
    }

    void togglePlacholderView() {
        TextView textView = view.findViewById(R.id.document_name);

        if (textView.getVisibility() == View.VISIBLE) {
            textView.setVisibility(View.GONE);
        }
        if (addNewDocEditText.getVisibility() == View.GONE) {
            addNewDocEditText.setVisibility(View.VISIBLE);
        }
        addNewDocEditText.setHint(documentModel.getName());
        addNewDocEditText.setText(documentModel.getName());
        addNewDocEditText.requestFocus();
        InputMethodManager imm = (InputMethodManager) context.getSystemService(context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(addNewDocEditText, InputMethodManager.SHOW_IMPLICIT);
    }
}
