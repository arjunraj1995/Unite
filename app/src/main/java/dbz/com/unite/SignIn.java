package dbz.com.unite;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class SignIn extends AppCompatActivity {
    private Toolbar rSigntool;
    EditText signInusername,signInpassword;
    Button signInbtn;
    String user, pass;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        rSigntool=(Toolbar)findViewById(R.id.signinToolbar);
        setSupportActionBar(rSigntool);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Login");

        signInbtn=(Button)findViewById(R.id.signin_btn);
        signInusername=(EditText)findViewById(R.id.signin_name);
        signInpassword=(EditText)findViewById(R.id.signin_password);

        signInbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                user = signInusername.getText().toString();
                pass = signInpassword.getText().toString();

                if(user.equals("")){
                    signInusername.setError("Can't be blank");
                }
                else if(pass.equals("")){
                    signInpassword.setError("Can't be blank");
                }

                else{
                    String url ="https://unite-5b361.firebaseio.com/users.json";
                    final ProgressDialog pd = new ProgressDialog(SignIn.this);
                    pd.setMessage("Loading...");
                    pd.show();

                    StringRequest request=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {

                            if(s.equals("null")){
                                Toast.makeText(SignIn.this, "User not found", Toast.LENGTH_LONG).show();
                            }
                            else{
                                try {
                                    JSONObject obj = new JSONObject(s);

                                    if(!obj.has(user)){
                                        Toast.makeText(SignIn.this, "User not found", Toast.LENGTH_LONG).show();
                                    }
                                    else if(obj.getJSONObject(user).getString("password").equals(pass)){
                                        userDetails.username = user;
                                        //userDetails.password = pass;
                                        SharedPreferences preferencesuser = getApplicationContext().getSharedPreferences("Unite", android.content.Context.MODE_PRIVATE);
                                        SharedPreferences.Editor editor = preferencesuser.edit();
                                        editor.putString("username_refer",userDetails.username);
                                        editor.commit();
                                        SharedPreferences preferenceslog = getApplicationContext().getSharedPreferences("Unite", android.content.Context.MODE_PRIVATE);
                                        editor = preferenceslog.edit();
                                        editor.putInt("login_success",1);
                                        editor.commit();
                                        Intent intent = new Intent(SignIn.this, navmain.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                    }
                                    else {
                                        Toast.makeText(SignIn.this, "Incorrect Password/ wrong User Name", Toast.LENGTH_LONG).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            pd.dismiss();
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            System.out.println("" + error);
                            pd.dismiss();
                        }
                    });
                    RequestQueue rQueue = Volley.newRequestQueue(SignIn.this);
                    rQueue.add(request);
                }

            }
        });


    }

}
