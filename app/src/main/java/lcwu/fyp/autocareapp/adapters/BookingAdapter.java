package lcwu.fyp.autocareapp.adapters;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import lcwu.fyp.autocareapp.model.Booking;
import lcwu.fyp.autocareapp.model.Notification;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.BookingHolder> {

    private List <Booking> Data;

    public BookingAdapter() {

        Data = new ArrayList<>();
    }

    public void setData(List<Booking> data) {
        Data = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BookingHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull BookingHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class BookingHolder extends RecyclerView.ViewHolder {

        public BookingHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
