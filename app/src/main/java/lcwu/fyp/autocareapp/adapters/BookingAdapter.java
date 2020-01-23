package lcwu.fyp.autocareapp.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import lcwu.fyp.autocareapp.R;
import lcwu.fyp.autocareapp.model.Booking;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.BookingHolder> {

    private List <Booking> Data;

    public BookingAdapter() {

        Data = new ArrayList<>();
    }

    public void setData(List<Booking> data) {
        Log.e("Bookings", "Data set to adapter");
        Data = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BookingHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_booking,parent, false);
        return new BookingHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull BookingHolder holder, int position) {
        final Booking b = Data.get(position);
        holder.date.setText(b.getDate());
        holder.type.setText(b.getType());


    }

    @Override
    public int getItemCount() {
        return Data.size();
    }

    class BookingHolder extends RecyclerView.ViewHolder {
        TextView date,type;

        public BookingHolder(@NonNull View itemView) {
            super(itemView);
            date=itemView.findViewById(R.id.date);
            type=itemView.findViewById(R.id.type);

        }
    }
}
