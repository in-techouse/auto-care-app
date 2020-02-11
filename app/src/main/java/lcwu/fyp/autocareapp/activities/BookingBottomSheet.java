package lcwu.fyp.autocareapp.activities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import lcwu.fyp.autocareapp.R;

public class BookingBottomSheet extends BottomSheetDialogFragment implements View.OnClickListener {
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.bottom_sheet, container, false);
    }

    @Override
    public void onClick(View view) {

    }
}