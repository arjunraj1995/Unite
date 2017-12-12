package dbz.com.unite;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
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
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static dbz.com.unite.userDetails.chatWith;

public class search_groups extends Fragment {
int g_flag=0;
    ListView group_list;
    ArrayList<DataModelGroup> sgl = new ArrayList<>();
    ArrayList<DataModelGroup> sgl1 = new ArrayList<>();
    ImageButton sgrp;
    EditText sgrpname;
    String searchgroupstr="",password1="",password2="";
    ProgressDialog pd;
    TextView grtv;
    public static CustomAdapterGroup adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_search_groups, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        group_list=(ListView)getView().findViewById(R.id.groupList);
        sgrp=(ImageButton)getView().findViewById(R.id.grpsearch_btn);
        sgrpname=(EditText)getView().findViewById(R.id.grp_search);
        grtv=(TextView)getView().findViewById(R.id.grp_isder);

        String url = "https://unite-5b361.firebaseio.com/groups.json";

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>(){
            @Override
            public void onResponse(String s) {
                if(s.equals("null") )grtv.setText("NO GROUPS FORMED YET");
                else doOnSuccessgrp(s);
            }
        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                System.out.println("" + volleyError);
            }
        });

        RequestQueue rQueue = Volley.newRequestQueue(getContext());
        rQueue.add(request);

       /* group_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                userDetails.grname = sgl.get(position);
                startActivity(new Intent(getContext(), group_chat.class));
            }
        });*/

        sgrp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchgroupstr=sgrpname.getText().toString();

                if(searchgroupstr.equals("")) Toast.makeText(getActivity(),"Cannot be empty",Toast.LENGTH_SHORT).show();
                else{
                    sgl1.clear();
                    pd = new ProgressDialog(getContext());
                    pd.setMessage("Loading...");
                    pd.show();
                    String urlc = "https://unite-5b361.firebaseio.com/groups.json";
                    StringRequest requestc = new StringRequest(Request.Method.GET, urlc, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            donOnSuccessgrpsearch(response);
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
        group_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if(g_flag==1){
                    DataModelGroup dataModelGroup=sgl1.get(position);
                    userDetails.grname=dataModelGroup.getgName();

                }
                else {
                    DataModelGroup dataModelGroup=sgl.get(position);
                    userDetails.grname=dataModelGroup.getgName();
                }
                String urlc = "https://unite-5b361.firebaseio.com/users/"+userDetails.username+"/group_u.json";
                StringRequest requesto = new StringRequest(Request.Method.GET, urlc, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Firebase reference = new Firebase("https://unite-5b361.firebaseio.com/users/"+userDetails.username);
                        if(response.equals("null")){
                            Map<String, Object> map = new HashMap<String, Object>();
                            map.put("group_u", "");
                            reference.updateChildren(map);
                            passdialog();
                        }
                        else donOnSuccessclicklist(response);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("" + error);
                    }
                });
                RequestQueue rQueueo = Volley.newRequestQueue(getContext());
                rQueueo.add(requesto);
            }
        });
    }

    private void donOnSuccessgrpsearch(String response) {

        try {
            JSONObject obj = new JSONObject(response);

            Iterator i = obj.keys();
            String key = "";
            int flag=0,inflag=0;
            while(i.hasNext()){
                key = i.next().toString();

                if(key.toLowerCase().contains(searchgroupstr.toLowerCase())){
                    if(inflag==0)
                    {    group_list.setAdapter(null);
                         inflag=1;
                    }
                    Firebase ref1=new Firebase("https://unite-5b361.firebaseio.com/groups/"+key);
                    ref1.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String adminname=dataSnapshot.child("admin").getValue().toString();
                            String gname=dataSnapshot.getKey();
                            sgl1.add(new DataModelGroup(gname,adminname));
                            adapter=new CustomAdapterGroup(sgl1,getActivity());
                            group_list.setAdapter(adapter);
                            g_flag=1;
                            pd.dismiss();
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    });
                    flag=1;
                }

            }
            if(flag==0){
                pd.dismiss();
                Toast.makeText(getContext(),"No such group_create names Found",Toast.LENGTH_LONG).show();

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        }

    private void donOnSuccessclicklist(String response) {

        try {
            JSONObject obj = new JSONObject(response);

            int flag = 0;
            Iterator i = obj.keys();
            String key = "";
                while (i.hasNext()) {
                    key = i.next().toString();
                    if (userDetails.grname.equals(key)) {
                        flag = 1;
                        break;
                    }
                }
                if (flag == 1) {

                    startActivity(new Intent(getContext(), group_chat.class));
                } else {

                    passdialog();

                }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    void passdialog()
    {
        LayoutInflater li = LayoutInflater.from(getActivity());
        View promptsView = li.inflate(R.layout.password_prompt, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                getActivity());

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);

        final EditText userInput = (EditText) promptsView
                .findViewById(R.id.editTextPasswordUserInput);

        alertDialogBuilder.setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                password1 = userInput.getText().toString();

                                Firebase pasref = new Firebase("https://unite-5b361.firebaseio.com/groups/" + userDetails.grname);
                                pasref.child("password").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot snapshot) {
                                        password2=snapshot.getValue().toString();
                                        if (password1.equals(password2)) {
                                            Firebase addtoy = new Firebase("https://unite-5b361.firebaseio.com/users/" + userDetails.username + "/group_u");
                                            Map<String, Object> map = new HashMap<String, Object>();
                                            map.put(userDetails.grname, "");
                                            addtoy.updateChildren(map);

                                            startActivity(new Intent(getContext(), group_chat.class));
                                        } else {

                                            String message = "The password you have entered is incorrect." + " \n" + "Please try again";
                                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                            builder.setTitle("Error");
                                            builder.setMessage(message);
                                            builder.setPositiveButton("Cancel", null);
                                            builder.setNegativeButton("Retry", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    passdialog();
                                                }
                                            });
                                            builder.create().show();
                                        }

                                    }
                                    @Override public void onCancelled(FirebaseError error) { }
                                });

                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


    private void doOnSuccessgrp(String s) {
        try {
            JSONObject obj = new JSONObject(s);

            Iterator i = obj.keys();
            String key = "";

            while(i.hasNext()){
                key = i.next().toString();
                Firebase ref1=new Firebase("https://unite-5b361.firebaseio.com/groups/"+key);
                ref1.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String adminname=dataSnapshot.child("admin").getValue().toString();
                        String gname=dataSnapshot.getKey();
                        sgl.add(new DataModelGroup(gname,adminname));
                        adapter=new CustomAdapterGroup(sgl,getActivity());
                        group_list.setAdapter(adapter);

                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
