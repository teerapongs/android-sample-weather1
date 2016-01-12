package com.bananacoding.weather1;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.bananacoding.weather1.helper.RSSParser;
import com.bananacoding.weather1.model.RSSWeather;

public class MainActivity extends AppCompatActivity {
    public static String EXTRA_MESSAGE = "lat";
    private ProgressDialog pDialog;
    RSSParser parser2 = new RSSParser();
    RSSWeather woeid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnClick = (Button) findViewById(R.id.btnClick);
        btnClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Summit Parameter
                EditText txtLatitude = (EditText) findViewById(R.id.txtLatitude);
                String latitude = txtLatitude.getText().toString();
                EditText txtLongitude = (EditText) findViewById(R.id.txtLongitude);
                String longitude = txtLongitude.getText().toString();


                String url = "http://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20geo.placefinder%20where%20text=%22" + latitude + "," + longitude + "%22%20and%20gflags=%22R%22";
                //String url ="http://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20geo.placefinder%20where%20text=%2213.7563,100.5018%22%20and%20gflags=%22R%222";

                new loadRSSFeedItems().execute(url);
            }

            class loadRSSFeedItems extends AsyncTask<String, String, String> {
                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    pDialog = new ProgressDialog(MainActivity.this);
                    pDialog.setMessage("Loading weather...");
                    pDialog.setIndeterminate(false);
                    pDialog.setCancelable(false);
                    pDialog.show();
                }

                /**
                 * getting all recent data and showing them in text view.
                 */
                @Override
                protected String doInBackground(String... args) {
                    // rss link url
                    String query_url = args[0];

                    // weather object of rss.
                    woeid = parser2.getRSSFeedWeather2(query_url);

                    // updating UI from Background Thread
                    runOnUiThread(new Runnable() {
                        public void run() {
                            /**
                             * Updating parsed data into text view.
                             * */
                            Intent intent = new Intent(MainActivity.this, Summit.class);
                            String description = woeid.getWoeid();
                            intent.putExtra(EXTRA_MESSAGE, description);
                           // Log.d("Long", description);
                            startActivity(intent);
                        }
                    });
                    return null;
                }

                /**
                 * After completing background task Dismiss the progress dialog
                 **/
                protected void onPostExecute(String args) {
                    // dismiss the dialog after getting all products
                    pDialog.dismiss();
                }
            }

        });
    }
}
