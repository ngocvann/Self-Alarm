package hcmute.edu.vn.selfalarm.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import hcmute.edu.vn.selfalarm.R;
import hcmute.edu.vn.selfalarm.model.SMS;
import java.util.List;

public class SMSAdapter extends RecyclerView.Adapter<SMSAdapter.SMSViewHolder> {
    private List<SMS> smsList;

    public SMSAdapter(List<SMS> smsList) {
        this.smsList = smsList;
    }

    @NonNull
    @Override
    public SMSViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sms, parent, false);
        return new SMSViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SMSViewHolder holder, int position) {
        SMS sms = smsList.get(position);
        holder.senderTextView.setText(sms.getSender());
        holder.messageTextView.setText(sms.getMessage());
    }

    @Override
    public int getItemCount() {
        return smsList.size();
    }

    static class SMSViewHolder extends RecyclerView.ViewHolder {
        TextView senderTextView;
        TextView messageTextView;

        SMSViewHolder(@NonNull View itemView) {
            super(itemView);
            senderTextView = itemView.findViewById(R.id.sms_sender);
            messageTextView = itemView.findViewById(R.id.sms_message);
        }
    }
}