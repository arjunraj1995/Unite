package dbz.com.unite;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by kcarj on 28-12-2016.
 */

public class global extends Fragment {
    ListView usersList;
    TextView noUsersText;
    ArrayList<DataModel> al = new ArrayList<>();
    int totalUsers = 0;
    ProgressDialog pd;
    public static CustomAdapter adapter;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
            usersList = (ListView)getView().findViewById(R.id.globlist);
        noUsersText = (TextView)getView().findViewById(R.id.noUsersText);
Firebase.setAndroidContext(getActivity());
        pd = new ProgressDialog(getContext());
        pd.setMessage("Loading...");
        pd.show();

        String url = "https://unite-5b361.firebaseio.com/users.json";

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>(){
            @Override
            public void onResponse(String s) {
                //doOnSuccess(s);
                Firebase ref=new Firebase("https://unite-5b361.firebaseio.com/users");
                ref.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        final String key=dataSnapshot.getKey();
                        String url ="https://unite-5b361.firebaseio.com/users/"+key+".json";

                        StringRequest request2=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String s) {
                                    try {
                                        if(!userDetails.username.equals(key)){
                                        JSONObject obj = new JSONObject(s);
                                        String onlinecheck=obj.getString("online");
                                        String def_lang=obj.getString("language");

                                        al.add(new DataModel(key,def_lang,onlinecheck));
                                        adapter=new CustomAdapter(al,getActivity());
                                        usersList.setAdapter(adapter);
                                    }
                                    }
                                    catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                System.out.println("" + error);
                                pd.dismiss();
                            }
                        });
                        RequestQueue rQueue2 = Volley.newRequestQueue(getContext());
                        rQueue2.add(request2);

                        pd.dismiss();
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });
            }
        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                System.out.println("" + volleyError);
            }
        });

        RequestQueue rQueue = Volley.newRequestQueue(getActivity());
        rQueue.add(request);

        usersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DataModel dataModel= al.get(position);
                userDetails.chatWith = dataModel.getName();
                startActivity(new Intent(getContext(), chat.class));
            }
        });

    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_global, container, false);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }
    /*
    public void doOnSuccess(String s){
        try {
            JSONObject obj = new JSONObject(s);

            Iterator i = obj.keys();
            String key = "";

            while(i.hasNext()){
                key = i.next().toString();

                if(!key.equals(userDetails.username)) {
                    al.add(key);
                }

                totalUsers++;
            }
            pd.dismiss();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(totalUsers <=1){
            noUsersText.setVisibility(View.VISIBLE);
            usersList.setVisibility(View.GONE);
        }
        else{
            noUsersText.setVisibility(View.GONE);
            usersList.setVisibility(View.VISIBLE);
            usersList.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, al));
        }
        pd.dismiss();
    }
*/


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
    }



}
