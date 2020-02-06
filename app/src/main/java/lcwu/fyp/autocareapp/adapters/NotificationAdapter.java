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
import lcwu.fyp.autocareapp.model.Notification;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationHolder>
{
    private List <Notification> Data;
    private Context context;

    public NotificationAdapter(Context c) {
        Data = new ArrayList<>();
        context = c;
    }

    public void setData(List<Notification> data) {
        Data = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public NotificationHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View V = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification,parent,false);
        return new NotificationHolder(V);

    }

    @Override
    public void onBindViewHolder(@NonNull NotificationHolder holder, int position) {
        final Notification n = Data.get(position);
        if(n != null){
            Log.e("Notification", "Date" + n.getDate());
            Log.e("Notification", "Message" + n.getMessage());
            holder.date.setText(n.getDate());
            holder.message.setText(n.getMessage());
            holder.mainCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent it = new Intent(context, BookingDetailActivity.class);
                    it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    Bundle b = new Bundle();
                    b.putSerializable("notification", n);
                    it.putExtras(b);



                    context.startActivity(it);


                }
            });
        }
        else{
            Log.e("Notification", "Notification obj is null");
        }

    }

    @Override
    public int getItemCount() {
        return Data.size();

    }

    class NotificationHolder extends RecyclerView.ViewHolder
    {
        TextView date;
        TextView message;
        CardView mainCard;

        public NotificationHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.Date);
            message = itemView.findViewById(R.id.message);
            mainCard = itemView.findViewById(R.id.mainCard);

        }
    }
}
