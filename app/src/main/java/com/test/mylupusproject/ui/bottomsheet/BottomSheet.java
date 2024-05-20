package com.test.mylupusproject.ui.bottomsheet;

import android.app.Dialog;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.test.mylupusproject.R;

public class BottomSheet extends BottomSheetDialogFragment {
    private final String title;
    private final boolean isExpanded;

    public BottomSheet(boolean isExpanded, String title) {
        super();
        this.title = title;
        this.isExpanded = isExpanded;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // set custom style for bottom sheet rounded top corners
        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.bottom_sheet, container, false);
        EditText editText = root.findViewById(R.id.userInput);
        Button doneButton = root.findViewById(R.id.doneButton);
        editText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                editText.setCursorVisible(false);
                doneButton.setEnabled(true);
            }
            return false;
        });
        editText.setOnClickListener(v -> {
            editText.setCursorVisible(true);
            doneButton.setEnabled(false);
        });

        ImageButton closeButton = root.findViewById(R.id.close);
        closeButton.setOnClickListener(v -> {
            dismiss();
        });
        return root;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView tvTitle = view.findViewById(R.id.title);
        tvTitle.setText(title);
    }
}
