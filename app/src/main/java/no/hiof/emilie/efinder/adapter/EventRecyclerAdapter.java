package no.hiof.emilie.efinder.adapter;

import android.content.Context;
import android.media.Image;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

import no.hiof.emilie.efinder.Fragments.FeedFragment;
import no.hiof.emilie.efinder.MainActivity;
import no.hiof.emilie.efinder.model.EventInformation;



import no.hiof.emilie.efinder.R;

public class EventRecyclerAdapter extends RecyclerView.Adapter<EventRecyclerAdapter.EventViewHolder>{
    private List<EventInformation> data;
    private LayoutInflater inflater;
    private Context context;
    private no.hiof.emilie.efinder.model.EventInformation event;
    private String imageName;
    private StorageReference imagePath;


    private View.OnClickListener clickListener;

    public void setOnItemClickListener(View.OnClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public EventRecyclerAdapter(Context context, List<EventInformation> data) {
        this.data = data;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public EventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.event_list_item, parent, false);
        EventViewHolder holder = new EventViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(EventViewHolder holder, int position) {
        EventInformation currentObj = data.get(position);
        holder.setData(currentObj);

        if (clickListener != null) {
            holder.itemView.setOnClickListener(clickListener);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class EventViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView name;
        TextView adresse;
        TextView date;
        ImageView poster;

        public EventViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.event_name);
            adresse = itemView.findViewById(R.id.event_adresse);
            date = itemView.findViewById(R.id.event_date);
            poster = itemView.findViewById(R.id.event_poster);
        }

        public void setData(EventInformation current) {
            this.name.setText(current.getEventTitle());
            this.adresse.setText(current.getEventAdress());
            this.date.setText(current.getEventDate());

            imageName = current.getEventImage();
            imagePath = FirebaseStorage.getInstance().getReferenceFromUrl(imageName);

            if (current.getEventImage() != null ) {
                imagePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Glide.with(poster.getContext())
                            .load(uri.toString())
                            .into(poster);
                    }
                });
            }
        }

        @Override
        public void onClick(View v) {

        }
    }
}
