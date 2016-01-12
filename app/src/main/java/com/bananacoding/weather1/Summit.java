package com.bananacoding.weather1;

import android.app.ProgressDialog;
        import android.content.Intent;
        import android.os.AsyncTask;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
import android.widget.TextView;

        import com.bananacoding.weather1.helper.RSSParser;
import com.bananacoding.weather1.model.RSSWeather;

public class Summit extends AppCompatActivity {

    // Progress Dialog
    private ProgressDialog pDialog;
    private static String weather_url = "http://weather.yahooapis.com/forecastrss?w=12756339&u=c";
    RSSParser parser = new RSSParser();
    RSSWeather weather;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summit);

        //create an instance of the view object.
        textView = (TextView) findViewById(R.id.text_view);
        //textView.setText(message);
        /**
         * Calling a backgroung thread will loads recent data of a website
         * @param rss url of website
         * */
        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        weather_url = "http://weather.yahooapis.com/forecastrss?w="+ message +"&u=c";
        new loadRSSFeedItems().execute(weather_url);

    }



    /**
     * Background Async Task to get RSS data from URL
     * */



    class loadRSSFeedItems extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Summit.this);
            pDialog.setMessage("Loading weather...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        /**
         * getting all recent data and showing them in text view.
         * */


        @Override
        protected String doInBackground(String... args) {

            // rss link url
            String rss_url = args[0];

            // weather object of rss.
            weather = parser.getRSSFeedWeather(rss_url);

            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {
                    /**
                     * Updating parsed data into text view.
                     * */
                    String description = String.format("title: %s \n pubdate: %s \n temp: %s \n link: %s " , weather.getTitle(),weather.getPubdate(),weather.getTemp(),weather.getLink());
                    textView.setText(description);

                }
            });

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String args) {
            // dismiss the dialog after getting all products
            pDialog.dismiss();
        }
    }
}