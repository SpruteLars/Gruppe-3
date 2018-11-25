package no.hiof.emilie.efinder;

import android.app.Notification;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

public class NotificationListActivity extends AppCompatActivity {
    ArrayList<Notification> notificationList;
    ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_list);

        notificationList = new ArrayList<Notification>();
    }

    public void notificationCreate() {
        notificationList.add(new Notification());
        arrayAdapter.notifyDataSetChanged();
    }
}
