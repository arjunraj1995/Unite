package dbz.com.unite;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Iterator;

public class contacts extends Fragment {
    TextView check;
    TextView noUser;
    ListView favlist;
    ArrayList<DataModel> fl = new ArrayList<>();
    int totalf=0;

    public static CustomAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_contacts, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        favlist=(ListView)getView().findViewById(R.id.fli);
        noUser=(TextView) getView().findViewById(R.id.nof);

        String urlf="https://unite-5b361.firebaseio.com/fav/"+userDetails.username+".json";
        StringRequest requestf=new StringRequest(Request.Method.GET, urlf, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
               // doOnSuccessf(response);
                if(response.equals("null")){
                    noUser.setVisibility(View.VISIBLE);
                    favlist.setVisibility(View.GONE);
                }
                else
                {
                    Firebase ref=new Firebase("https://unite-5b361.firebaseio.com/fav/"+userDetails.username);
                    ref.addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            final String key=dataSnapshot.getKey();
                            Firebase red=new Firebase("https://unite-5b361.firebaseio.com/users/"+key);
                            red.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    String onlinecheck=dataSnapshot.child("online").getValue().toString();
                                    String def_lang=dataSnapshot.child("language").getValue().toString();
                                    fl.add(new DataModel(key,def_lang,onlinecheck));
                                    adapter=new CustomAdapter(fl,getActivity());
                                    favlist.setAdapter(adapter);
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
                }
            }
        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                System.out.println("" + volleyError);
            }
        });
        RequestQueue rQueue = Volley.newRequestQueue(getContext());
        rQueue.add(requestf);

        favlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DataModel dataModel=fl.get(position);
                userDetails.chatWith = dataModel.getName();
                startActivity(new Intent(getContext(), chat.class));
            }
        });


    }
/*
    private void doOnSuccessf(String s) {
        try {
            JSONObject obj = new JSONObject(s);

            Iterator i = obj.keys();
            String key = "";

            while(i.hasNext()){
                key = i.next().toString();

                    fl.add(key);


                totalf++;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(totalf==0){
            noUser.setVisibility(View.VISIBLE);
            favlist.setVisibility(View.GONE);
        }
        else{
            noUser.setVisibility(View.GONE);
            noUser.setVisibility(View.VISIBLE);
            favlist.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, fl));
        }

    }
*/

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }
}
