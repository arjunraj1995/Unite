package dbz.com.unite;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

public class notify_userup extends Service {
ValueEventListener hehe;
    final Firebase nref=new Firebase("https://unite-5b361.firebaseio.com/users/"+userDetails.username+"/wants_online");
    public notify_userup(){

    }


    private final IBinder obj=new LocalBinder();
    @Override
    public IBinder onBind(Intent intent) {

        Firebase.setAndroidContext(this);

        hehe=nref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String who=dataSnapshot.getValue().toString();
                if(!who.equals("")) {
                    who = who.replaceFirst("\\s++$", "");
                    userDetails.chatWith = who;
                    NotificationManager notif = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    Intent notificationIntent = new Intent(getBaseContext(), chat.class);
                    PendingIntent intent = PendingIntent.getActivity(getBaseContext(), 0, notificationIntent, 0);
                    Notification notify = new Notification.Builder
                            (getApplicationContext()).setContentTitle(who).setContentText("Come Online").
                            setContentTitle(who).setSmallIcon(R.drawable.ic_translate_black_24dp).setLargeIcon(BitmapFactory.decodeResource(getBaseContext().getResources(),
                            R.mipmap.ranicon)).setContentIntent(intent).build();
                    notify.flags |= Notification.FLAG_AUTO_CANCEL;
                    notify.defaults |= Notification.DEFAULT_SOUND;
                    notify.defaults |= Notification.DEFAULT_VIBRATE;

                    notif.notify(0, notify);
                    nref.setValue("");
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        return obj;
            }

    /*void not(){
        Firebase.setAndroidContext(this);
        final Firebase nref=new Firebase("https://unite-5b361.firebaseio.com/users/"+userDetails.username+"/wants_online");
        nref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String who=dataSnapshot.getValue().toString();
                if(!who.equals("")) {
                    who = who.replaceFirst("\\s++$", "");
                    userDetails.chatWith = who;
                    NotificationManager notif = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    Intent notificationIntent = new Intent(getBaseContext(), chat.class);
                    PendingIntent intent = PendingIntent.getActivity(getBaseContext(), 0, notificationIntent, 0);
                    Notification notify = new Notification.Builder
                            (getApplicationContext()).setContentTitle(who).setContentText("Come Online").
                            setContentTitle(who).setSmallIcon(R.drawable.final_ic).setContentIntent(intent).build();
                    notify.flags |= Notification.FLAG_AUTO_CANCEL;
                    notify.defaults |= Notification.DEFAULT_SOUND;
                    notify.defaults |= Notification.DEFAULT_VIBRATE;

                    notif.notify(0, notify);
                    nref.setValue("");
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

    }*/


    @Override
    public boolean onUnbind(Intent intent) {
        
        stopSelf();
        return super.onUnbind(intent);

    }

    @Override
    public void onDestroy() {

        nref.removeEventListener(hehe);
        super.onDestroy();
    }



    public class LocalBinder extends Binder{
        notify_userup getService(){
            return notify_userup.this;
        }
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {


        super.onTaskRemoved(rootIntent);
    }
}
