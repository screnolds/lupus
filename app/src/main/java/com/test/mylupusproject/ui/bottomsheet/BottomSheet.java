package com.test.mylupusproject.ui.bottomsheet;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.test.mylupusproject.R;

public class BottomSheet extends BottomSheetDialogFragment {
    private final String message;
    private final boolean isExpanded;

    public BottomSheet(boolean isExpanded, String message) {
        super();
        this.message = message;
        this.isExpanded = isExpanded;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // set custom style for bottom sheet rounded top corners
        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.bottom_sheet, container, false);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        if (isExpanded) {
            //  if you wanna show the bottom sheet as full screen,
            BottomSheetDialog bottomSheetDialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
            bottomSheetDialog.setOnShowListener(dialog -> {
                FrameLayout bottomSheet = ((BottomSheetDialog) dialog)
                        .findViewById(com.google.android.material.R.id.design_bottom_sheet);
                if (bottomSheet != null)
                    BottomSheetBehavior
                            .from(bottomSheet)
                            .setState(BottomSheetBehavior.STATE_EXPANDED);
            });
            return bottomSheetDialog;
        }
        return super.onCreateDialog(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView tvHello = view.findViewById(R.id.tv_hello);
        tvHello.setText(message);
    }
}
