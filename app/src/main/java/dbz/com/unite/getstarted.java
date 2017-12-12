package dbz.com.unite;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.memetix.mst.language.Language;
import com.memetix.mst.translate.Translate;

public class getstarted extends AppCompatActivity {
    private Button rSignin;
    private Button rRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_getstarted);
        SharedPreferences preferencesint=getApplicationContext().getSharedPreferences("Unite", Context.MODE_PRIVATE);
        int check=preferencesint.getInt("login_success",0);
        if(check==1){
            Intent s=new Intent(getBaseContext(),navmain.class);
            SharedPreferences preferencesstr=getApplicationContext().getSharedPreferences("Unite", Context.MODE_PRIVATE);
            userDetails.username=preferencesstr.getString("username_refer","");
            startActivity(s);
            finish();
        }
        rSignin=(Button)findViewById(R.id.SignInbtn);
        rRegister=(Button)findViewById(R.id.Registerbtn);


        rSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent s=new Intent(getBaseContext(),SignIn.class);
                startActivity(s);
            }
        });


        rRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent r=new Intent(getBaseContext(),Register.class);
                startActivity(r);
            }
        });


    }
}
