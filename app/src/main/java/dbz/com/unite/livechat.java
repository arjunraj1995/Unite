package dbz.com.unite;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
//import com.memetix.mst.language.Language;
//import com.memetix.mst.translate.Translate;
import dbz.com.rmtheis.yandtran.language.Language;
import dbz.com.rmtheis.yandtran.translate.Translate;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import static dbz.com.unite.userDetails.chatWith;
import static dbz.com.unite.userDetails.username;

public class livechat extends AppCompatActivity {
    LinearLayout layout;
    ImageView sendButton;
    EditText messageArea;
    ScrollView scrollView;
    Firebase reference1, reference2;
    Toolbar rChatTool;
    ImageButton ref;
    Spinner langspinner;
    String message="";
    Language[] languages = Language.values();
    int tranwant;
    user_availability onlineservice;
    boolean isBound=false;
    ChatAdapter adapter=new ChatAdapter(this, new ArrayList<ChatMessage>());;
    ListView messagesContainer;
    ArrayList<ChatMessage> chatHistory;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setContentView(R.layout.activity_livechat);
        super.onCreate(savedInstanceState);
        final ProgressDialog dialog = ProgressDialog.show(this, "", "Loading Messages...",
                true);
        dialog.show();
        Intent in=new Intent(this,user_availability.class);
        bindService(in,connection, Context.BIND_AUTO_CREATE);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                dialog.dismiss();
            }
        }, 2000);

        //ToolBar
        rChatTool=(Toolbar)findViewById(R.id.lchatToolbar);
        setSupportActionBar(rChatTool);
        setTitle("Live Chat");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        messagesContainer = (ListView) findViewById(R.id.lmessagesContainer);
        //Translation Checker
        SharedPreferences preftran = getApplicationContext().getSharedPreferences("Unite", android.content.Context.MODE_PRIVATE);
        tranwant=preftran.getInt("checkvaluetran",1);

        //Language Selector
        langspinner=(Spinner)findViewById(R.id.llangselect);
        langspinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, GetAllValues()));
        SharedPreferences preferencesint=getApplicationContext().getSharedPreferences("Unite", Context.MODE_PRIVATE);
        langspinner.setSelection(preferencesint.getInt("langchange",userDetails.of_name));


        if(tranwant==1)
        {
            langspinner.setVisibility(View.VISIBLE);
        }
        else
        {
            langspinner.setVisibility(View.GONE);
        }



        sendButton=(ImageView)findViewById(R.id.sendButton);
        messageArea=(EditText)findViewById(R.id.messageArea);
        Firebase.setAndroidContext(this);
        loadDummyHistory();



        reference1 = new Firebase("https://unite-5b361.firebaseio.com/live/");

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageText = messageArea.getText().toString();

                if(!messageText.equals("")){
                    String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("message", messageText);
                    map.put("user", userDetails.username);
                    map.put("time",currentDateTimeString);
                    reference1.push().setValue(map);
                }
                else Toast.makeText(livechat.this,"Write something..",Toast.LENGTH_SHORT).show();
                messageArea.setText("");
            }
        });

        langspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SharedPreferences preferences = getApplicationContext().getSharedPreferences("Unite", android.content.Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putInt("langchange",langspinner.getSelectedItemPosition());
                editor.commit();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        reference1.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                Map map = dataSnapshot.getValue(Map.class);
                message = map.get("message").toString();
                String userName = map.get("user").toString();
                String timein=map.get("time").toString();
                if(userName.equals(userDetails.username)){

                    addMessageBox( message, 1,timein);
                }
                else {
                    if (tranwant==1) {
                        final int langTrans=langspinner.getSelectedItemPosition();
                        class asyn extends AsyncTask<String, String, String> {
                            String translatedText = "";

                            @Override
                            protected String doInBackground(String... params) {
                                try {
                                    Translate.setKey("trnsl.1.1.20161201T050035Z.d0ef68d69de11639.2b1826ae8db57b70702c0128abdba51bcac2525a");
                                    translatedText = dbz.com.rmtheis.yandtran.translate.Translate.execute(message,Language.ENGLISH, languages[langTrans]);
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
                        addMessageBox(userName + ":-\n" + translate_result, 2,timein);

                    } else {
                        addMessageBox(userName + ":-\n" + message, 2,timein);
                    }
                }
            dialog.dismiss();
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

        dialog.dismiss();
    }


    //Add To Contacts



//Creating Message Box

    public void addMessageBox(String message, int type,String time){

        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setMessage(message);
        chatMessage.setDate(time);

        if(type==2)chatMessage.setMe(true);
        else chatMessage.setMe(false);
        displayMessage(chatMessage);
    }

    //Getting locales
    public void displayMessage(ChatMessage message) {
        adapter.add(message);
        adapter.notifyDataSetChanged();
        scroll();
    }
    private void scroll() {
        messagesContainer.setSelection(messagesContainer.getCount() - 1);
    }

    private void loadDummyHistory(){
        chatHistory = new ArrayList<ChatMessage>();
        ChatMessage msg = new ChatMessage();
        msg.setId(1);
        msg.setMe(false);
        msg.setMessage("");
        msg.setDate("");
        chatHistory.add(msg);
        ChatMessage msg1 = new ChatMessage();
        msg1.setId(2);
        msg1.setMe(false);
        msg1.setMessage("");
        msg1.setDate("");
        chatHistory.add(msg1);
        adapter = new ChatAdapter(this, new ArrayList<ChatMessage>());
        messagesContainer.setAdapter(adapter);
    }

    //Getting locales

    public String[] GetAllValues(){
        String lan[] = new String[languages.length];
        for(int i = 0; i < languages.length; i++){
            lan[i] = languages[i].name();
        }
        return lan;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.glchat_menu,menu);
        MenuItem item = menu.findItem(R.id.tran_menu);
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("Unite", android.content.Context.MODE_PRIVATE);
        item.setChecked(preferences.getBoolean("checkvaluetrans",false));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();break;
            case R.id.tran_menu:
                if (item.isChecked()) {
                    item.setChecked(false);
                    SharedPreferences preferences = getApplicationContext().getSharedPreferences("Unite", android.content.Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putBoolean("checkvaluetrans", false);
                    editor.putInt("checkvaluetran", 0);
                    editor.apply();

                }
                else {
                    item.setChecked(true);
                    SharedPreferences preferences = getApplicationContext().getSharedPreferences("Unite", android.content.Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putBoolean("checkvaluetrans", true);
                    editor.putInt("checkvaluetran", 1);
                    editor.apply();

                }
                Intent intent = getIntent();
                finish();
                startActivity(intent);break;
            case R.id.refmenu:
                Intent intent1=getIntent();
                finish();
                startActivity(intent1);break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStop() {
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
}












/*
                    class bgstuff extends AsyncTask<Void,Void,Void> {
                        String translatedText = "";

                        @Override
                        protected void onPreExecute() {
                            super.onPreExecute();
                        }

                        @Override
                        protected Void doInBackground(String... params) {
                            try { Translate.setClientId("unitetrans");
                                  Translate.setClientSecret("OYLQfSJm47FxjgbrE9RM8uB/yeihXAJG1ctcu6J2Kcg=");
                                translatedText = Translate.execute("CAT",Language.HINDI);
                            } catch (Exception e) {
                                // TODO Auto-generated catch block
                                translatedText = "Unable to translate";
                            }
                            return translatedText;
                        }

                        @Override
                        protected void onPostExecute(Void aVoid) {
                            trsutext=translatedText;
                            super.onPostExecute(aVoid);


                        }
                    }
                    Object str_result= null;
                    try {
                        str_result = new bgstuff().execute().get();
                    } catch (InterruptedException e) {

                    } catch (ExecutionException e) {e.printStackTrace();
                        e.printStackTrace();
                    }
                    addMessageBox(userDetails.chatWith + ":-\n" + str_result, 2);
                */

/*
        //Translation CHECKBOX
        tran_en=(CheckBox)findViewById(R.id.tranchecker);
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("Unite", android.content.Context.MODE_PRIVATE);
        tran_en.setChecked(preferences.getBoolean("checkvalue",false));
*/


/*
        fav=(ImageButton)findViewById(R.id.favbtn);
        fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Firebase favref=new Firebase("https://unite-5b361.firebaseio.com/fav/"+username);
                Map<String,Object> map=new HashMap<String, Object>();
                map.put(chatWith,"");
                favref.updateChildren(map);
                Toast.makeText(chat.this,"Successfully Added To Your Contacts",Toast.LENGTH_LONG).show();
            }
        });*/

/*
        tran_en.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean value;
                if(tran_en.isChecked())value=true;
                else value=false;
                SharedPreferences preferences = getApplicationContext().getSharedPreferences("Unite", android.content.Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean("checkvalue", value);
                editor.commit();
                Intent intent = getIntent();
                finish();
                startActivity(intent);

            }
        });*/

/*     tran_en.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Intent intent = getIntent();
                finish();
                startActivity(intent);
            }
        });*/