package lcwu.fyp.autocareapp.activities;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import lcwu.fyp.autocareapp.R;

public class BookingBottomSheet extends BottomSheetDialogFragment {

    public static BookingBottomSheet newInstance() {
        return new BookingBottomSheet();
    }

    @Nullable
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.bottom_sheet, container,
                false);

        // get the views and attach the listener

        return view;

    }
}