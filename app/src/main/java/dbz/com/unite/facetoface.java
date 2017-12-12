package dbz.com.unite;

import android.annotation.TargetApi;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Build;
import android.os.IBinder;
import android.speech.RecognizerIntent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

//import com.memetix.mst.language.Language;
//import com.memetix.mst.translate.Translate;
import dbz.com.rmtheis.yandtran.language.Language;
import dbz.com.unite.LanguageVoice;
import dbz.com.rmtheis.yandtran.translate.TranslateVoice;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

public class facetoface extends AppCompatActivity {
    TextToSpeech t1;
    Toolbar tf;
    TextView res,speakt;
    ImageButton speak_btn,swap_btn,repeat;
    Spinner sp,lsp;
    String[] langs;
    String lang_pref="",list_pref="";
    String final_text="";
    String translatedText="";
    LanguageVoice[] languages = LanguageVoice.values();
    user_availability onlineservice;
    boolean isBound=false;
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facetoface);

        tf=(Toolbar)findViewById(R.id.ftfToolbar);
        setSupportActionBar(tf);
        setTitle("Face to Face");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent in=new Intent(this,user_availability.class);
        bindService(in,connection, Context.BIND_AUTO_CREATE);
        speakt=(TextView)findViewById(R.id.speaker_text);
        speak_btn=(ImageButton)findViewById(R.id.iB) ;
        //langs =getResources().getStringArray(R.array.langs_sup);
        swap_btn=(ImageButton)findViewById(R.id.swap);
        repeat=(ImageButton)findViewById(R.id.repeat_btn);
        sp=(Spinner)findViewById(R.id.spinner);
        lsp=(Spinner)findViewById(R.id.listener_lang);
        res=(TextView)findViewById(R.id.tranresult);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, GetAllValues());
        sp.setAdapter(adapter);
        SharedPreferences preferencess=getApplicationContext().getSharedPreferences("Unite", Context.MODE_PRIVATE);
        sp.setSelection(preferencess.getInt("speaklang",7));

        lsp.setAdapter(adapter);
        SharedPreferences preferencesl=getApplicationContext().getSharedPreferences("Unite", Context.MODE_PRIVATE);
        lsp.setSelection(preferencesl.getInt("listenlang",13));


        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int index = parent.getSelectedItemPosition();
                Toast.makeText(getBaseContext(), "Speak in " +parent.getItemAtPosition(position).toString()+" please...", Toast.LENGTH_SHORT).show();
                switch (index) {
                    case 0:
                        lang_pref = "ar";
                        break;
                    case 1:
                        lang_pref = "bg";
                        break;
                    case 2:
                        lang_pref = "ca";
                        break;
                    case 3:
                        lang_pref = "zh";
                        break;
                    case 4:
                        lang_pref = "cs";
                        break;
                    case 5:
                        lang_pref = "da";
                        break;
                    case 6:
                        lang_pref = "nl";
                        break;
                    case 7:
                        lang_pref = "en";
                        break;
                    case 8:
                        lang_pref = "et";
                        break;
                    case 9:lang_pref = "fi";
                        break;
                        case 10:lang_pref = "fr";break;
                        case 11:lang_pref = "de";break;
                        case 12:lang_pref = "el";break;
                        case 13:lang_pref = "hi";break;
                        case 14:lang_pref = "hu";break;
                        case 15:lang_pref = "id";break;
                        case 16:lang_pref = "it";break;
                        case 17:lang_pref = "ja";break;
                        case 18:lang_pref = "ko";break;
                        case 19:lang_pref = "lv";break;
                        case 20: lang_pref = "lt";break;
                        case 21:lang_pref = "no";break;
                        case 22:lang_pref = "pl";break;
                        case 23:lang_pref = "pt";break;
                        case 24:lang_pref = "ro";break;
                        case 25:lang_pref = "ru";break;
                        case 26:lang_pref = "sk";break;
                        case 27:lang_pref = "sl";break;
                        case 28:lang_pref = "es";break;
                        case 29:lang_pref = "sv";break;
                        case 30:lang_pref = "th";break;
                        case 31:lang_pref = "tr";break;
                        case 32:lang_pref = "uk";break;
                        case 33:lang_pref="vi";break;
                }
                SharedPreferences preferences = getApplicationContext().getSharedPreferences("Unite", android.content.Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putInt("speaklang",sp.getSelectedItemPosition());
                editor.commit();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });

        lsp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int index = parent.getSelectedItemPosition();
                Toast.makeText(getBaseContext(), "Listener language : " +parent.getItemAtPosition(position).toString(), Toast.LENGTH_SHORT).show();
                switch (index) {
                    case 0:
                        list_pref = "ar";
                        break;
                    case 1:
                        list_pref = "bg";
                        break;
                    case 2:
                        list_pref = "ca";
                        break;
                    case 3:
                        list_pref = "zh";
                        break;
                    case 4:
                        list_pref = "cs";
                        break;
                    case 5:
                        list_pref = "da";
                        break;
                    case 6:
                        list_pref = "nl";
                        break;
                    case 7:
                        list_pref = "en";
                        break;
                    case 8:
                        list_pref = "et";
                        break;
                    case 9:list_pref = "fi";
                        break;
                    case 10:list_pref = "fr";break;
                    case 11:list_pref = "de";break;
                    case 12:list_pref = "el";break;
                    case 13:list_pref = "hi";break;
                    case 14:list_pref = "hu";break;
                    case 15:list_pref = "id";break;
                    case 16:list_pref = "it";break;
                    case 17:list_pref = "ja";break;
                    case 18:list_pref = "ko";break;
                    case 19:list_pref = "lv";break;
                    case 20: list_pref = "lt";break;
                    case 21:list_pref = "no";break;
                    case 22:list_pref = "pl";break;
                    case 23:list_pref = "pt";break;
                    case 24:list_pref = "ro";break;
                    case 25:list_pref = "ru";break;
                    case 26:list_pref = "sk";break;
                    case 27:list_pref = "sl";break;
                    case 28:list_pref = "es";break;
                    case 29:list_pref = "sv";break;
                    case 30:list_pref = "th";break;
                    case 31:list_pref = "tr";break;
                    case 32:list_pref = "uk";break;
                    case 33:list_pref="vi";break;
                }
                t1.setLanguage(new Locale(list_pref));
                SharedPreferences preferences = getApplicationContext().getSharedPreferences("Unite", android.content.Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putInt("listenlang",lsp.getSelectedItemPosition());
                editor.commit();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });

        swap_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int a=lsp.getSelectedItemPosition();
                int b=sp.getSelectedItemPosition();
                lsp.setSelection(b);
                sp.setSelection(a);
            }
        });
        speak_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                promptspeechinput();
            }
        });






        t1 = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    t1.setLanguage(new Locale(list_pref));
                }
            }
        });
       /* b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String toSpeak = ed1.getText().toString();
                Toast.makeText(getApplicationContext(), toSpeak, Toast.LENGTH_SHORT).show();

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    ttsGreater21(toSpeak);
                } else {
                    ttsUnder20(toSpeak);
                }
            }
        });*/
        repeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            finalise(res.getText().toString());
            }
        });
    }

    void promptspeechinput(){
        Intent i=new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE,lang_pref);
        i.putExtra(RecognizerIntent.EXTRA_PROMPT,"Say Something");
        try{
            startActivityForResult(i,100);

        }
        catch (ActivityNotFoundException e)
        {
            Toast.makeText(this,"SORRY",Toast.LENGTH_LONG).show();

        }
    }

    public void onActivityResult(int request_code, int result_code, Intent i)
    {
        super.onActivityResult(request_code,result_code,i);
        switch (request_code)
        {
            case 100:if(result_code == RESULT_OK && i!=null)
            {   final int langTrans=lsp.getSelectedItemPosition();
                ArrayList<String> result=i.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                final_text=result.get(0);
                speakt.setText(final_text);
                class asyn extends AsyncTask<String, String, String> {
                    @Override
                    protected String doInBackground(String... params) {
                        try {
                            TranslateVoice.setKey("trnsl.1.1.20161201T050035Z.d0ef68d69de11639.2b1826ae8db57b70702c0128abdba51bcac2525a");
                            translatedText = TranslateVoice.execute(final_text, LanguageVoice.ENGLISH,languages[langTrans]);
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            translatedText = "Unable to translate";
                        }
                        return translatedText;
                    }
                }
                String translate_result = "";
                try {
                    translate_result = new asyn().execute().get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                res.setText(translate_result);
               String toSpeak = translate_result;
                Toast.makeText(getApplicationContext(), toSpeak, Toast.LENGTH_SHORT).show();
                finalise(toSpeak);
                repeat.setVisibility(View.VISIBLE);

            }
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
        case android.R.id.home:
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void finalise(String st)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ttsGreater21(st);
        } else {
            ttsUnder20(st);
        }
    }
    @SuppressWarnings("deprecation")
    private void ttsUnder20(String text) {
        HashMap<String, String> map = new HashMap<>();
        map.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "MessageId");
        t1.speak(text, TextToSpeech.QUEUE_FLUSH, map);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void ttsGreater21(String text) {
        String utteranceId=this.hashCode() + "";
        t1.speak(text, TextToSpeech.QUEUE_FLUSH, null, utteranceId);
    }
   // public void onPause
    public void onStop() {

        if (t1 != null) {
            t1.stop();
            t1.shutdown();
        }
        super.onStop();
        unbindService(connection);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent in=new Intent(this,user_availability.class);
        bindService(in,connection, Context.BIND_AUTO_CREATE);
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
    public String[] GetAllValues(){
        String lan[] = new String[languages.length];
        for(int i = 0; i < languages.length; i++){
            lan[i] = languages[i].name();
        }
        return lan;
    }
}