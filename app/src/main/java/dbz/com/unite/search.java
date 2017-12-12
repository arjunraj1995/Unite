package dbz.com.unite;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by kcarj on 28-12-2016.
 */

public class search extends Fragment {

    EditText searchu;
    ListView searchlist;
    Button searchbtn;
    String searchname;
    ProgressDialog pd;
    ArrayList<DataModel> sl = new ArrayList<>();
    int totals=0;
    public static CustomAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_search, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        searchu=(EditText)getView().findViewById(R.id.search_user);
        searchlist=(ListView)getView().findViewById(R.id.search_list);
        searchbtn=(Button)getView().findViewById(R.id.search_btn);


        searchbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchlist.setAdapter(null);
                sl.clear();
                searchname=searchu.getText().toString();
                if(searchname.equals(""))
                {
                    searchu.setError("Please enter a name first");
                }
                else if(searchname.toLowerCase().equals(userDetails.username.toLowerCase())){
                    Toast.makeText(getActivity(),"That's YOU",Toast.LENGTH_SHORT).show();
                }
                else { pd = new ProgressDialog(getContext());
                    pd.setMessage("Loading...");
                    pd.show();
                    String urlc = "https://unite-5b361.firebaseio.com/users.json";

                    StringRequest requestc = new StringRequest(Request.Method.GET, urlc, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            donOnSuccessc(response);
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            System.out.println("" + error);
                        }
                    });
                    RequestQueue rQueuec = Volley.newRequestQueue(getContext());
                    rQueuec.add(requestc);


                }

            }
        });


    }

    private void donOnSuccessc(String s) {
        try {
            JSONObject obj = new JSONObject(s);

            Iterator i = obj.keys();
            String key = "";
            int flag=0;
            while(i.hasNext()){
                key = i.next().toString();
                if(key.equals(userDetails.username))continue;
                else if(key.toLowerCase().contains(searchname.toLowerCase())){
                    Firebase ref=new Firebase("https://unite-5b361.firebaseio.com/users/"+key);
                    ref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String onlinecheck=dataSnapshot.child("online").getValue().toString();
                            String def_lang=dataSnapshot.child("language").getValue().toString();
                            String user=dataSnapshot.getKey();
                            sl.add(new DataModel(user,def_lang,onlinecheck));
                            adapter=new CustomAdapter(sl,getActivity());
                            searchlist.setAdapter(adapter);
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    });
                    flag=1;
                    totals++;
                }

            }
        if(flag==0){
          Toast.makeText(getContext(),"No Users Found",Toast.LENGTH_LONG).show();
            pd.dismiss();
        }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(totals==0){

            searchlist.setVisibility(View.GONE);
            pd.dismiss();
        }
        else{

            searchlist.setAdapter(adapter);
            pd.dismiss();
            searchlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    DataModel dataModel= sl.get(position);
                    userDetails.chatWith=dataModel.getName();
                    startActivity(new Intent(getContext(), chat.class));
                }
            });
        }


    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }
}
