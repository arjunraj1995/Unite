package dbz.com.unite;

import android.app.Service;
import android.content.Intent;
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

public class user_availability extends Service {

public user_availability(){

    }


    private final IBinder obj=new LocalBinder();
    @Override
    public IBinder onBind(Intent intent) {
        return obj;
    }

public void onlineornot(){

    Firebase.setAndroidContext(getBaseContext());
    String urlo="https://unite-5b361.firebaseio.com/users/"+userDetails.username+"/online.json";

    StringRequest requestf=new StringRequest(Request.Method.GET, urlo, new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            // doOnSuccessf(response);
            Firebase oref=new Firebase("https://unite-5b361.firebaseio.com/users/"+userDetails.username+"/online");

            oref.setValue("yes");


        }
    },new Response.ErrorListener(){
        @Override
        public void onErrorResponse(VolleyError volleyError) {
            System.out.println("" + volleyError);
        }
    });
    RequestQueue rQueue = Volley.newRequestQueue(getBaseContext());
    rQueue.add(requestf);
}
void notonlineor(){

        Firebase.setAndroidContext(getBaseContext());
        String urlo="https://unite-5b361.firebaseio.com/users/"+userDetails.username+"/online.json";

        StringRequest requestf=new StringRequest(Request.Method.GET, urlo, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // doOnSuccessf(response);
                Firebase oref=new Firebase("https://unite-5b361.firebaseio.com/users/"+userDetails.username+"/online");

                oref.setValue("no");


            }
        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                System.out.println("" + volleyError);
            }
        });
        RequestQueue rQueue = Volley.newRequestQueue(getBaseContext());
        rQueue.add(requestf);
    }
    @Override
    public boolean onUnbind(Intent intent) {
        notonlineor();
        stopSelf();
        return super.onUnbind(intent);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

public class LocalBinder extends Binder{
    user_availability getService(){
        return user_availability.this;
    }
}
}
