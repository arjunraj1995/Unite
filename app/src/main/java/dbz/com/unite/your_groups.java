package dbz.com.unite;


import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import com.firebase.client.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class your_groups extends Fragment {
    ListView ygrp_list;
    TextView noGrp;
    int totalGroups = 0;
    ArrayList<DataModelGroup> ygl = new ArrayList<>();
    public static CustomAdapterGroup adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_your_groups, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ygrp_list = (ListView) getView().findViewById(R.id.ygroup_list);
        noGrp=(TextView)getView().findViewById(R.id.nogrp) ;
        String urlf = "https://unite-5b361.firebaseio.com/users/" + userDetails.username + "/group_u.json";
        StringRequest requestf = new StringRequest(Request.Method.GET, urlf, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.equals("null")) noGrp.setText("No groups yet");
              //  else doOnSuccessf(response);
                else{
                    Firebase ref=new Firebase("https://unite-5b361.firebaseio.com/users/"+userDetails.username+"/group_u");
                    if(getActivity()!=null){
                    ref.addChildEventListener(new ChildEventListener() {
                        @Override

                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            String key=dataSnapshot.getKey();
                            Firebase ref1=new Firebase("https://unite-5b361.firebaseio.com/groups/"+key);
                            ref1.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    String adminname=dataSnapshot.child("admin").getValue().toString();
                                    String gname=dataSnapshot.getKey();
                                    ygl.add(new DataModelGroup(gname,adminname));
                                    adapter=new CustomAdapterGroup(ygl,getActivity());
                                    ygrp_list.setAdapter(adapter);

                                }

                                @Override
                                public void onCancelled(FirebaseError firebaseError) {

                                }
                            });

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
                }}
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                System.out.println("" + volleyError);
            }
        });
        RequestQueue rQueue = Volley.newRequestQueue(getContext());
        rQueue.add(requestf);

        ygrp_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DataModelGroup dataModelGroup=ygl.get(position);
                userDetails.grname = dataModelGroup.getgName();
                startActivity(new Intent(getContext(), group_chat.class));
            }
        });


    }
/*
    public void doOnSuccessf(String s) {
        try {
            JSONObject obj = new JSONObject(s);
            Iterator i = obj.keys();
            String key = "";
            while(i.hasNext()){
                key = i.next().toString();
                    ygl.add(key);
                totalGroups++;
            }
            if(totalGroups ==0){
                noGrp.setText("No groups yet");
            }
            else{
                noGrp.setText("Your Groups");
                ygrp_list.setAdapter(new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,ygl));
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }*/
}