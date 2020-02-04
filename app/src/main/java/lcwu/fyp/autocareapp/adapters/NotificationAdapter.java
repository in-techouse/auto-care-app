package lcwu.fyp.autocareapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import lcwu.fyp.autocareapp.R;
import lcwu.fyp.autocareapp.model.Notification;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationHolder>
{
    private List<Notification> Data;

    public NotificationAdapter() {
        Data = new ArrayList<>();
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
        holder.date.setText(n.getDate());
        holder.message.setText(n.getMessage());
    }

    @Override
    public int getItemCount() {
        return Data.size();
    }

    class NotificationHolder extends RecyclerView.ViewHolder
    {
        TextView date;
        TextView message;

        public NotificationHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.Date);
            message = itemView.findViewById(R.id.textView_message);
        }
    }
}
