package com.shrikantelectronics;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;





import java.util.Map;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class StockVerificationBrand_view extends AppCompatActivity {

    private static String URL = Config.WEBSERVICE_URL;
    private static String IMAGEURL = Config.IMAGEURL_URL;

    String[] description;

    ProgressDialog prgDialog;
    ListViewAdapter_StockVerification_Brand adapter;
    private ListView lv;
    EditText inputSearch;
    ArrayList<HashMap<String, String>> Brand;

    String under;

    ArrayList<Brand> arraylist = new ArrayList<Brand>();


    protected static final int RESULT_SPEECH = 1;
    public ArrayList<String> text;

    public static TextToSpeech toSpeech;
    public int result;

    String name1 = "";
    public static boolean speachFlag = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brand_view);



        invokeWS_Brand();
    }

    public void invokeWS_Brand(){
        try {

            Map<String, String> paramsMap = new HashMap<>();
            paramsMap.put("sysbrandno", "");
            paramsMap.put("description", "");


            //AsyncHttpClient client = new AsyncHttpClient();
            ApiHelper.post(URL + "Service1.asmx/GetBrandList", paramsMap, new ApiHelper.ApiCallback()   {
            @Override

            public void onSuccess(JSONObject response) {
                try {
                    JSONObject obj = response;
                    JSONArray new_array = obj.getJSONArray("Brand");
                    for (int i = 0, count = new_array.length(); i < count; i++) {
                        try {
                            JSONObject jsonObject = new_array.getJSONObject(i);
                            Brand wp = new Brand(jsonObject.getString("sysbrandno"),jsonObject.getString("description"));
                            arraylist.add(wp);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    lv = (ListView) findViewById(R.id.list_view);
                    inputSearch = (EditText) findViewById(R.id.inputSearch);

                    adapter = new ListViewAdapter_StockVerification_Brand(StockVerificationBrand_view.this, arraylist);

                    // Binds the Adapter to the ListView
                    lv.setAdapter(adapter);

                    inputSearch.addTextChangedListener(new TextWatcher() {

                        @Override
                        public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                            // When user changed the Text
                         //   home.this.adapter.getFilter().filter(cs);
                        }

                        @Override
                        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                                      int arg3) {
                            // TODO Auto-generated method stub
                        }

                        @Override
                        public void afterTextChanged(Editable arg0) {
                            String text = inputSearch.getText().toString().toLowerCase(Locale.getDefault());
                            adapter.filter(text);
                        }
                    });

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    Toast.makeText(getApplicationContext(), "Error Occured [Server's JSON response might be invalid]!", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }

            // When the response returned by REST has Http response code other than '200'
            @Override
            public void onFailure(String errorMessage) {
                // Hide Progress Dialog
           //     prgDialog.hide();
                // When Http response code is '404'
                Toast.makeText(getApplicationContext(), "API Error: " + errorMessage, Toast.LENGTH_LONG).show();
                // When Http response code is '500'

                // When Http response code other than 404, 500
                            }

        });
        }catch (Exception e) {
            e.printStackTrace();
        }
    }



    //SETS UP THE MENU ON THE ACTIONBAR//
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "en-US");

        //noinspection SimplifiableIfStatement
        if (id == R.id.btnSpeak) {

            try {
                startActivityForResult(intent, RESULT_SPEECH);
            } catch (ActivityNotFoundException a) {
                Toast t = Toast.makeText(getApplicationContext(),
                        "Opps! Your device doesn't support Speech to Text",
                        Toast.LENGTH_SHORT);
                t.show();
            }

            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    //  THIS TAKES THE SPEECH AND PUTS IT IN A ARRAY LIST AND THEN DISPLAYS IT IN A TOAST FOR NOW//
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case RESULT_SPEECH: {
                if (resultCode == RESULT_OK && null != data) {

                    text = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                    if(result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED)
                        Toast.makeText(getApplicationContext(), "Feature not supported in your deavice", Toast.LENGTH_LONG).show();
                    else
                    {
                        Log.i("voice", text.get(0));
                        Toast.makeText(StockVerificationBrand_view.this,text.get(0), Toast.LENGTH_LONG).show();

                        inputSearch = (EditText) findViewById(R.id.inputSearch);

                        inputSearch.setText(text.get(0).toString().replace(" ",""));



                        /*

                        if(text.get(0).equals("hello"))
                        {
                            name = text.get(0);
                            toSpeech.speak("Hello what is your name",TextToSpeech.QUEUE_FLUSH,null);
                        }
                        //     if(name.equals("hello"))
                        //    {
                        //       toSpeech.speak("Hello" + name.get(0),TextToSpeech.QUEUE_FLUSH,null);
                        //  }
                        // name = "";

                        else if(text.get(0).equals("what level is science"))
                        {
                            toSpeech.speak("It is on level 14 ",TextToSpeech.QUEUE_FLUSH,null);
                        }
                        else if(text.get(0).equals("who made you"))
                        {
                            toSpeech.speak("My creater is group 1",TextToSpeech.QUEUE_FLUSH,null);
                        }
                        else if(text.get(0).equals("how can I pass"))
                        {
                            toSpeech.speak("You have to time manage and be consistent. But most importantly you have to love what you do. hash tag goodluck",TextToSpeech.QUEUE_FLUSH,null);
                        }

                        else if(text.get(0).equals("what do you think of Kaylee"))
                        {
                            toSpeech.speak("I think she is very sexy and that she is really smart. You should kiss her",TextToSpeech.QUEUE_FLUSH,null);
                        }

                        else {
                            speachFlag = false;
                           // popUpSearchResult("Book_table", "TITLE", text.get(0).toUpperCase());

                            if(speachFlag) {
                                toSpeech.speak("I dont understand that yet", TextToSpeech.QUEUE_FLUSH, null);
                                Toast.makeText(MainActivity.this ,text.get(0), Toast.LENGTH_LONG).show();
                            }
                            //Make this generic by reading whichever books descriptions
                            else if(text.get(0).equals("algorithms"))
                                toSpeech.speak("Robert Segwick's book for algorithms is targeted for people who are familiarized with coding at an advanced stage", TextToSpeech.QUEUE_FLUSH,null);


                            else
                                toSpeech.speak("Here's a list of books", TextToSpeech.QUEUE_FLUSH,null);

                        }

                        */
                    }

//                    Toast.makeText(this,text.get(0),Toast.LENGTH_LONG).show();
                }
                else
                {

                    //if(text == null)
                    //  toSpeech.speak("That was flames",TextToSpeech.QUEUE_FLUSH,null);
                    //else
                    toSpeech.speak("Sorry I could not hear you",TextToSpeech.QUEUE_FLUSH,null);




                }
                break;
            }
        }
    }



}


