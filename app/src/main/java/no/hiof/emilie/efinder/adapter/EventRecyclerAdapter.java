package no.hiof.emilie.efinder.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import no.hiof.emilie.efinder.R;
import no.hiof.emilie.efinder.model.Event;

public class EventRecyclerAdapter extends RecyclerView.Adapter<EventRecyclerAdapter.EventViewHolder>{
    private List<Event> data;
    private LayoutInflater inflater;

    private View.OnClickListener clickListener;

    public void setOnItemClickListener(View.OnClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public EventRecyclerAdapter(Context context, List<Event> data) {
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
        Event currentObj = data.get(position);
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

        public void setData(Event current) {
            this.name.setText(current.getName());
            String posterUrl = current.getPosterUrl();
            this.adresse.setText(current.getAdresse());
            this.date.setText(current.getDate());

            if (posterUrl != null && !posterUrl.equals("")) {
                Glide.with(poster.getContext())
                        .load(posterUrl)
                        .into(poster);
            }
            else
                poster.setImageResource(R.mipmap.ic_launcher_round);
        }

        @Override
        public void onClick(View v) {

        }
    }
}
