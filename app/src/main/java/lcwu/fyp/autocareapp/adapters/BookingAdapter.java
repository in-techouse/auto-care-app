package lcwu.fyp.autocareapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import lcwu.fyp.autocareapp.R;
import lcwu.fyp.autocareapp.activities.BookingActivity;
import lcwu.fyp.autocareapp.activities.BookingDetailActivity;
import lcwu.fyp.autocareapp.model.Booking;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.BookingHolder> {

    private List <Booking> Data;
    private String type;
    private Context context;

    public BookingAdapter(String t) {

        Data = new ArrayList<>();
        type = t;
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
        holder.status.setText(b.getStatus());
        holder.address.setText(b.getAddres());

//        holder.mainCard.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent it=new Intent(context, BookingDetailActivity.class);
//                it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                Bundle d=new Bundle();
//                d.putSerializable("booking", b);
//                it.putExtras(d);
//
//                context.startActivity(it);
//
//            }
//        });

    }

    @Override
    public int getItemCount() {
        return Data.size();
    }

    class BookingHolder extends RecyclerView.ViewHolder {
        TextView date,type, status,address;
        CardView mainCard;

        public BookingHolder(@NonNull View itemView) {
            super(itemView);
            date=itemView.findViewById(R.id.date);
            type=itemView.findViewById(R.id.type);
            mainCard=itemView.findViewById(R.id.mainCard);
            status=itemView.findViewById(R.id.status);
            address=itemView.findViewById(R.id.address);
        }
    }
}
