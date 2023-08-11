package com.test.mylupusproject.ui.utils;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.test.mylupusproject.R;
import com.test.mylupusproject.ui.data.DocumentModel;

public class ListenerHelper {
    private View view;
    FragmentActivity mainFragmentActivity;
    DocumentModel documentModel;
    Context context;
    ConstraintLayout bottomSlideMenu = null;
    BottomNavigationView navBar = null;
    View disabledBackground = null;
    int animateTransitionMs = 250;
    String docType = null;

    public ListenerHelper(View view, FragmentActivity mainFragmentActivity, DocumentModel documentModel, Context context, String docType) {
        this.view = view;
        this.mainFragmentActivity = mainFragmentActivity;
        this.documentModel = documentModel;
        this.context = context;
        this.docType = docType;
        bottomSlideMenu = mainFragmentActivity.findViewById(R.id.bottom_slide_menu);
        navBar = mainFragmentActivity.findViewById(R.id.nav_view);
        disabledBackground = mainFragmentActivity.findViewById(R.id.disabled_background);
    }

    public void addMoreButtonClickListners() {
        ImageButton moreButton = view.findViewById(R.id.more_button);
        moreButton.setOnClickListener(view -> {
            Animation aniFade = AnimationUtils.loadAnimation(context, R.anim.fade_in);
            TranslateAnimation animateNavBarDown = new TranslateAnimation(
                    0,
                    0,
                    0,
                    navBar.getHeight());
            animateNavBarDown.setDuration(animateTransitionMs);
            disabledBackground.setVisibility(View.INVISIBLE);
            disabledBackground.startAnimation(aniFade);
            navBar.startAnimation(animateNavBarDown);
            navBar.postDelayed(new Runnable() {
                @Override
                public void run() {
                    disabledBackground.setVisibility(View.VISIBLE);
                    setDisabledBackgroundListener();
                    navBar.setVisibility(View.GONE);
                    bottomSlideMenu.setVisibility(View.INVISIBLE);
                    bottomSlideMenu.setVisibility(View.VISIBLE);
                    TranslateAnimation animateSlideMenuUp = new TranslateAnimation(
                            0,
                            0,
                            bottomSlideMenu.getHeight(),
                            0);
                    animateSlideMenuUp.setDuration(animateTransitionMs);
                    bottomSlideMenu.startAnimation(animateSlideMenuUp);
                    bottomSlideMenu.setVisibility(View.VISIBLE);
                    //moreButton.setClickable(true);
                    TextView selectedItemName = mainFragmentActivity.findViewById(R.id.selected_item_name);
                    selectedItemName.setText(documentModel.getDocId());
                    TextView selectedItemType = mainFragmentActivity.findViewById(R.id.selected_item_type);
                    if (docType.contentEquals("Item")) {
                        selectedItemType.setText(docType);
                    } else {
                        selectedItemType.setText("Group");
                    }
                    setSlideMenuListeners();
                }
            }, animateTransitionMs);
        });
    }

    void setSlideMenuListeners() {
        addCloseButtonListener();
        ImageButton addGroupContainerOverlay = mainFragmentActivity.findViewById(R.id.add_group_button_container_overlay);
        ImageButton addGroup = mainFragmentActivity.findViewById(R.id.add_group_button);
        TextView addGroupText = mainFragmentActivity.findViewById(R.id.add_group_text);
        if(docType.contentEquals("root")) {
            addGroup.setColorFilter(Color.BLACK);
            addGroupText.setTextColor(Color.BLACK);
            addGroupContainerOverlay.setOnClickListener(view -> {
                Toast.makeText(context, "Add Group button Clicked! " + documentModel.getDocId(), Toast.LENGTH_SHORT).show();
            });
        } else {
            addGroup.setColorFilter(Color.parseColor("#E6EAEC"));
            addGroupText.setTextColor(Color.parseColor("#E6EAEC"));
            addGroupContainerOverlay.setOnClickListener(null);
        }

        ImageButton addButton = mainFragmentActivity.findViewById(R.id.add_button);
        TextView addButtonText = mainFragmentActivity.findViewById(R.id.add_button_text);
        View addButtonContainerOverlay = mainFragmentActivity.findViewById(R.id.add_button_container_overlay);
        View divider = mainFragmentActivity.findViewById(R.id.divider1);
        if(docType.contentEquals("Item")) {
            addButton.setColorFilter(Color.parseColor("#E6EAEC"));
            addButtonText.setTextColor(Color.parseColor("#E6EAEC"));
            divider.setBackgroundColor(Color.parseColor("#E6EAEC"));
            addButtonContainerOverlay.setOnClickListener(null);
        } else {
            addButton.setColorFilter(Color.BLACK);
            addButtonText.setTextColor(Color.BLACK);
            divider.setBackgroundColor(Color.BLACK);
            addButtonContainerOverlay.setOnClickListener(view -> {
                Toast.makeText(context, "Add button Clicked! " + documentModel.getDocId(), Toast.LENGTH_SHORT).show();
            });
        }

        View editButtonContainerOverlay = mainFragmentActivity.findViewById(R.id.edit_button_container_overlay);
        editButtonContainerOverlay.setOnClickListener(view -> {
            Toast.makeText(context, "Edit button Clicked! " + documentModel.getDocId(), Toast.LENGTH_SHORT).show();
        });
        View deleteButtonContainerOverlay = mainFragmentActivity.findViewById(R.id.delete_button_container_overlay);
        deleteButtonContainerOverlay.setOnClickListener(view -> {
            Toast.makeText(context, "Delete button Clicked! " + documentModel.getDocId(), Toast.LENGTH_SHORT).show();
        });
    }

    void addCloseButtonListener() {
        ImageButton closeButton = mainFragmentActivity.findViewById(R.id.close_button);
        closeButton.setOnClickListener(view -> {
            animateSlideMenuDown();
        });
    }

    void setDisabledBackgroundListener() {
        disabledBackground.setOnClickListener(view -> {
            animateSlideMenuDown();
        });
    }

    void animateSlideMenuDown() {
        TranslateAnimation slideMenuDownAnimation = new TranslateAnimation(
                0,
                0,
                0,
                bottomSlideMenu.getHeight());
        slideMenuDownAnimation.setDuration(animateTransitionMs);
        bottomSlideMenu.startAnimation(slideMenuDownAnimation);
        bottomSlideMenu.setVisibility(View.GONE);
        bottomSlideMenu.postDelayed(new Runnable() {
            @Override
            public void run() {
                navBar.setVisibility(View.INVISIBLE);
                TranslateAnimation animateNavBarUp = new TranslateAnimation(
                        0,
                        0,
                        navBar.getHeight(),
                        0);
                animateNavBarUp.setDuration(animateTransitionMs);
                navBar.startAnimation(animateNavBarUp);
                navBar.setVisibility(View.VISIBLE);
                Animation aniFade = AnimationUtils.loadAnimation(context, R.anim.fade_out);
                disabledBackground.startAnimation(aniFade);
                disabledBackground.setVisibility(View.GONE);
            }
        }, animateTransitionMs);
    }
}
