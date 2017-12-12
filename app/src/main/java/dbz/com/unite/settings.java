package dbz.com.unite;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.Switch;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import dbz.com.rmtheis.yandtran.language.Language;

public class settings extends AppCompatActivity {
    Button btn;
    EditText t1,t2;
    Switch tbtn;
    Spinner sp;
   // String []langs;
    String oldp="",newp="";
    user_availability onlineservice;
    boolean isBound=false;
    Toolbar rsetTool;
    Language[] languages = Language.values();
    @Override
    protected void onResume() {
        super.onResume();
        Intent in=new Intent(this,user_availability.class);
        bindService(in,connection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {

        super.onStop();

        unbindService(connection);


    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Intent in=new Intent(this,user_availability.class);
        bindService(in,connection, Context.BIND_AUTO_CREATE);
        //langs =getResources().getStringArray(R.array.langs_sup);
        btn=(Button)findViewById(R.id.change_pass);
        t1=(EditText)findViewById(R.id.old_pass);
        t2=(EditText)findViewById(R.id.new_pass);
        sp=(Spinner)findViewById(R.id.spinnerset);
        tbtn=(Switch) findViewById(R.id.toggleButtonset);
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("Unite", android.content.Context.MODE_PRIVATE);
        tbtn.setChecked(preferences.getBoolean("checkvaluetrans",false));
        rsetTool=(Toolbar)findViewById(R.id.settingsToolbar);
        setSupportActionBar(rsetTool);
        setTitle("Settings");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, GetAllValues());
        String compareValue = userDetails.preferred_language;
        int spinnerPosition = adapter.getPosition(compareValue);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp.setAdapter(adapter);
        sp.setSelection(spinnerPosition);


        SharedPreferences preft=getApplicationContext().getSharedPreferences("Unite", Context.MODE_PRIVATE);
        Boolean check=preft.getBoolean("checkvaluetrans",true);
        tbtn.setChecked(check);
        tbtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!tbtn.isChecked()){
                    SharedPreferences preft=getApplicationContext().getSharedPreferences("Unite", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor=preft.edit();
                    editor.putBoolean("checkvaluetrans",false);
                    editor.putInt("checkvaluetran", 0);
                    editor.commit();
                    Toast.makeText(getBaseContext(),"Translations set to OFF",Toast.LENGTH_SHORT).show();
                }
                else{
                    SharedPreferences preft=getApplicationContext().getSharedPreferences("Unite", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor=preft.edit();
                    editor.putBoolean("checkvaluetrans",true);
                    editor.putInt("checkvaluetran", 1);
                    editor.commit();
                    Toast.makeText(getBaseContext(),"Translations set to ON",Toast.LENGTH_SHORT).show();
                }
            }
        });
        /*
        tbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!tbtn.isChecked()){
                    tbtn.toggle();
                    SharedPreferences preft=getApplicationContext().getSharedPreferences("Unite", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor=preft.edit();
                    editor.putBoolean("checkvaluetrans",false);
                    editor.putInt("checkvaluetran", 0);
                    editor.commit();
                    Toast.makeText(getBaseContext(),"Translations set to OFF",Toast.LENGTH_SHORT).show();
                }
                else{

                    SharedPreferences preft=getApplicationContext().getSharedPreferences("Unite", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor=preft.edit();
                    editor.putBoolean("checkvaluetrans",true);
                    editor.putInt("checkvaluetran", 1);
                    editor.commit();
                    Toast.makeText(getBaseContext(),"Translations set to ON",Toast.LENGTH_SHORT).show();
                }
            }
        });*/
        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int index=parent.getSelectedItemPosition();

                userDetails.preferred_language=parent.getItemAtPosition(position).toString();
                userDetails.of_name=index;
                Firebase reference = new Firebase("https://unite-5b361.firebaseio.com/users/"+userDetails.username);
                reference.child("language").setValue(parent.getItemAtPosition(position).toString());
                Toast.makeText(getBaseContext(),"Selected "+parent.getItemAtPosition(position).toString()+" as your default language...",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oldp=t1.getText().toString();
                newp=t2.getText().toString();
                if(oldp.equals("")&&newp.equals(""))Toast.makeText(getBaseContext(),"Please no empty blanks",Toast.LENGTH_SHORT).show();
                else{
                    final Firebase reference = new Firebase("https://unite-5b361.firebaseio.com/users/"+userDetails.username+"/password");
                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.getValue().toString().equals(oldp)){
                                reference.setValue(newp);
                                Toast.makeText(getBaseContext(),"Password changed successfully",Toast.LENGTH_SHORT).show();
                            }
                            else Toast.makeText(getBaseContext(),"Enter the old password correctly",Toast.LENGTH_SHORT).show();

                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    });




                }
            }
        });

    }
    ServiceConnection connection=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            user_availability.LocalBinder binder=(user_availability.LocalBinder) service;
            onlineservice=binder.getService();
            isBound=true;
            onlineservice.onlineornot();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBound=false;
        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
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
