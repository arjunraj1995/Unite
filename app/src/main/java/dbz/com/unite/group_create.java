package dbz.com.unite;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.firebase.client.Firebase;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static dbz.com.unite.userDetails.chatWith;

public class group_create extends Fragment {
Button grpbtn;
    EditText guser,gpass;
    String gname,gpasstext;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_group, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        grpbtn=(Button)getView().findViewById(R.id.group_create);
        guser=(EditText)getView().findViewById(R.id.group_name);
        gpass=(EditText)getView().findViewById(R.id.group_password);

grpbtn.setOnClickListener(new View.OnClickListener() {

    @Override
    public void onClick(View v) {
        gname=guser.getText().toString();
        gpasstext=gpass.getText().toString();
        if(gname.equals("")||gpasstext.equals("")) {
            Toast.makeText(getActivity(), "No empty Blanks please", Toast.LENGTH_SHORT).show();
        }
            else if(!gname.matches("[A-Za-z0-9]+")){
                guser.setError("Only Alphabets,number combinations allowed");
            }
        else if(gname.length()<5){
            guser.setError("Atleast 5 characters long");
        }
        else if(gpasstext.length()<5){
            gpass.setError("at least 5 characters long");
        }
        else{
            final ProgressDialog pd = new ProgressDialog(getActivity());
            pd.setMessage("Creating the group ...");
            pd.show();

            String url = "https://unite-5b361.firebaseio.com/groups.json";

            StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>(){
                @Override
                public void onResponse(String s) {
                    Firebase reference = new Firebase("https://unite-5b361.firebaseio.com/groups");
                    Firebase groupreference=new Firebase("https://unite-5b361.firebaseio.com/users/"+userDetails.username+"/group_u");
                    int flag=0;
                    if(s.equals("null")) {
                        reference.child(gname).child("password").setValue(gpasstext);
                        reference.child(gname).child("admin").setValue(userDetails.username);
                        Toast.makeText(getActivity(), "Group Created Successfully...", Toast.LENGTH_LONG).show();
                        flag=1;
                        guser.setText("");
                        gpass.setText("");
                        Map<String,Object> map=new HashMap<String, Object>();
                        map.put(gname,"");
                        groupreference.updateChildren(map);
                    }
                    else {
                        try {
                            JSONObject obj = new JSONObject(s);

                            if (!obj.has(gname)) {
                                reference.child(gname).child("password").setValue(gpasstext);
                                reference.child(gname).child("admin").setValue(userDetails.username);
                                Toast.makeText(getActivity(), "Group Created successful...", Toast.LENGTH_SHORT).show();
                                guser.setText("");
                                gpass.setText("");
                                Map<String,Object> map=new HashMap<String, Object>();
                                map.put(gname,"");
                                groupreference.updateChildren(map);
                                flag=1;
                            } else {
                                Toast.makeText(getActivity(), "Group name already exists,try another name", Toast.LENGTH_LONG).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    pd.dismiss();
                    if(flag==1)
                    {
                     //   Intent i=new Intent(getActivity(),SignIn.class);
                      //  startActivity(i);
                        Toast.makeText(getActivity(), "Group Added to your groups", Toast.LENGTH_LONG).show();
                    }

                }
            },new Response.ErrorListener(){
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    System.out.println("" + volleyError );
                    pd.dismiss();
                }
            });

            RequestQueue rQueue = Volley.newRequestQueue(getActivity());
            rQueue.add(request);
        }

    }
});



    }




}
