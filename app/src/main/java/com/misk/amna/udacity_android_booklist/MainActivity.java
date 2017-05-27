package com.misk.amna.udacity_android_booklist;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;


////////////////////////////////////////////////
                 /*
Hello reviwer I have utlized code in :
             https://github.com/udacity/ud843-QuakeReport
and
https://github.com/udacity/ud843_Soonami
     */
////////////////////////////



public class MainActivity extends AppCompatActivity {

    private static String REQUEST_URL = "";
    public myAsyncTask task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button SearchBtn = (Button) findViewById(R.id.searchBtn);

        SearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText SearchView = (EditText) findViewById(R.id.searchView);
                String KeyWord = SearchView.getText().toString().replaceAll(" ", "");

                REQUEST_URL = "https://www.googleapis.com/books/v1/volumes?q=" + KeyWord.trim() + "&maxResults=40";
                task = new myAsyncTask();
                task.execute();

            }
        });
    }


    private class myAsyncTask extends AsyncTask<URL, Void, ArrayList<Book>> {

        @Override
        protected ArrayList<Book> doInBackground(URL... urls) {
            // Create URL object
            URL url = createUrl(REQUEST_URL);

            // Perform HTTP request to the URL and receive a JSON response back
            String jsonResponse = "";
            try {
                jsonResponse = makeHttpRequest(url);
            } catch (IOException e) {
                Log.e("HTTP request ::", "Problem making the HTTP request.", e);
            }

            ArrayList<Book> books = extractFeatureFromJson(jsonResponse);

            if (books != null)
                return books;
            else
                return null;
        }

        @Override
        protected void onPostExecute(ArrayList<Book> books) {
            UpdateList(books);

        }


        private void UpdateList(ArrayList<Book> books) {

            ListView bookListView = (ListView) findViewById(R.id.list);
            TextView EmptyTextView = (TextView) findViewById(R.id.empty_view);
            bookListView.setEmptyView(EmptyTextView);
            final BookAdapter adapter = new BookAdapter(getApplicationContext(), books);

            if (books != null) {

                bookListView.setAdapter(adapter);
                bookListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                        Book currentBook = adapter.getItem(position);

                        Uri bookUri = Uri.parse(currentBook.getmUrl());

                        Intent DetailsIntent = new Intent(Intent.ACTION_VIEW, bookUri);

                        startActivity(DetailsIntent);
                    }
                });

            }
            else {
                //clear if no result
                bookListView.setAdapter(null);
            }
        }


        private URL createUrl(String stringUrl) {
            URL url = null;
            try {
                url = new URL(stringUrl);
            } catch (MalformedURLException exception) {
                Log.e("URL:::", "Error with creating URL", exception);
                return null;
            }
            return url;
        }


        private String makeHttpRequest(URL url) throws IOException {
            String jsonResponse = "";

            // If the URL is null, then return early.
            if (url == null) {
                return jsonResponse;
            }

            HttpURLConnection urlConnection = null;
            InputStream inputStream = null;
            try {
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setReadTimeout(10000 /* milliseconds */);
                urlConnection.setConnectTimeout(15000 /* milliseconds */);
                urlConnection.connect();

                // If the request was successful (response code 200),
                // then read the input stream and parse the response.
                if (urlConnection.getResponseCode() == 200) {
                    inputStream = urlConnection.getInputStream();
                    jsonResponse = readFromStream(inputStream);
                } else {
                    Log.e("Error::", "Error response code: " + urlConnection.getResponseCode());
                }
            } catch (IOException e) {
                Log.e("retrieving::", "Problem retrieving the earthquake JSON results.", e);
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
            }
            return jsonResponse;
        }

        private String readFromStream(InputStream inputStream) throws IOException {
            StringBuilder output = new StringBuilder();
            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
                BufferedReader reader = new BufferedReader(inputStreamReader);
                String line = reader.readLine();
                while (line != null) {
                    output.append(line);
                    line = reader.readLine();
                }
            }
            return output.toString();
        }


        public ArrayList<Book> extractFeatureFromJson(String bookSON) {

            ArrayList<Book> books = new ArrayList<>();

            try {
                JSONObject baseJsonResponse = new JSONObject(bookSON);
                Integer totalitem = baseJsonResponse.getInt("totalItems");
                if (totalitem == 0) return null;

                JSONArray bookArray = baseJsonResponse.getJSONArray("items");

                for (int i = 0; i < bookArray.length(); i++) {

                    JSONObject currentBook = bookArray.getJSONObject(i);
                    JSONObject volumeInfo = currentBook.getJSONObject("volumeInfo");
                    String title = volumeInfo.getString("title");
                    String authors = "";
                    if (volumeInfo.has("authors")) {
                        JSONArray authorsArray = volumeInfo.getJSONArray("authors");
                        for (int j = 0; j < authorsArray.length(); j++) {
                            authors += authorsArray.getString(j);
                        }
                    }
                    String url = volumeInfo.getString("previewLink");

                    Book book = new Book(title, authors, url);
                    books.add(book);

                }
                return books;
            } catch (JSONException e) {

                Log.e("extractFromJson::", "Problem parsing the earthquake JSON results", e);
            }
            return null;

        }
    }

}