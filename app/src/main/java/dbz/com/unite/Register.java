package dbz.com.unite;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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

import dbz.com.rmtheis.yandtran.language.Language;

public class Register extends AppCompatActivity {
    private Toolbar rRegistertool;
    EditText username, password;
    Button registerButton;
    String user, pass;
    Spinner osp;
    Language[] languages = Language.values();
    //String []langs;
    int g_flag=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //langs =getResources().getStringArray(R.array.langs_sup);
        rRegistertool=(Toolbar)findViewById(R.id.registerToolbar);
        setSupportActionBar(rRegistertool);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("New User");
        username = (EditText)findViewById(R.id.register_name);
        password = (EditText)findViewById(R.id.register_password);
        registerButton = (Button)findViewById(R.id.register_btn);
        osp=(Spinner)findViewById(R.id.offlangspinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item, GetAllValues());
        osp.setAdapter(adapter);
        osp.setSelection(13);
        Firebase.setAndroidContext(this);

        osp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int index=parent.getSelectedItemPosition();
                Toast.makeText(getBaseContext(),"Selected "+parent.getItemAtPosition(position).toString()+" as your default language...",Toast.LENGTH_SHORT).show();
                userDetails.preferred_language=parent.getItemAtPosition(position).toString();
                userDetails.of_name=index;
                g_flag=1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user = username.getText().toString();
                pass = password.getText().toString();
                if(g_flag==0){
                    Toast.makeText(getBaseContext(),"Please select a language ",Toast.LENGTH_SHORT).show();
                }
                if(user.equals("")){
                    username.setError("Can't be blank");
                }
                else if(pass.equals("")){
                    password.setError("Can't be blank");
                }
                else if(!user.matches("[A-Za-z0-9]+")){
                    username.setError("Only Alphabets,number combinations allowed");
                }
                else if(user.length()<5){
                    username.setError("Atleast 5 characters long");
                }
                else if(pass.length()<5){
                    password.setError("at least 5 characters long");
                }
                else {
                    final ProgressDialog pd = new ProgressDialog(Register.this);
                    pd.setMessage("Registering ...");
                    pd.show();

                    String url = "https://unite-5b361.firebaseio.com/users.json";

                    StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>(){
                        @Override
                        public void onResponse(String s) {
                            Firebase reference = new Firebase("https://unite-5b361.firebaseio.com/users");
                            int flag=0;
                            if(s.equals("null")) {
                                reference.child(user).child("password").setValue(pass);
                                reference.child(user).child("language").setValue(osp.getSelectedItem().toString());
                                reference.child(user).child("online").setValue("yes");
                                reference.child(user).child("wants_online").setValue("");
                                reference.child(user).child("group_u").child("LIVE2").setValue("");
                                Toast.makeText(Register.this, "Registration Successful", Toast.LENGTH_LONG).show();
                                flag=1;
                            }
                            else {
                                try {
                                    JSONObject obj = new JSONObject(s);

                                    if (!obj.has(user)) {
                                        reference.child(user).child("password").setValue(pass);
                                        reference.child(user).child("language").setValue(osp.getSelectedItem().toString());
                                        reference.child(user).child("online").setValue("yes");
                                        reference.child(user).child("wants_online").setValue("");
                                        reference.child(user).child("group_u").child("LIVE2").setValue("");
                                        Toast.makeText(Register.this, "Registration successful", Toast.LENGTH_SHORT).show();
                                        flag=1;
                                    } else {
                                        Toast.makeText(Register.this, "Username already exists,try another name", Toast.LENGTH_LONG).show();
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            pd.dismiss();
                            if(flag==1)
                            {
                                Intent i=new Intent(getBaseContext(),SignIn.class);
                                startActivity(i);
                                Toast.makeText(getBaseContext(), "Login with the new details", Toast.LENGTH_SHORT).show();
                                finish();

                            }

                        }
                    },new Response.ErrorListener(){
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            System.out.println("" + volleyError );
                            pd.dismiss();
                        }
                    });

                    RequestQueue rQueue = Volley.newRequestQueue(Register.this);
                    rQueue.add(request);
                }






            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:finish();break;
        }
        return super.onOptionsItemSelected(item);
    }
    public String[] GetAllValues(){
        String lan[] = new String[languages.length];
        for(int i = 0; i < languages.length; i++){
            lan[i] = languages[i].name();
        }
        return lan;
    }
}
