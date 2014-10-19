package com.example.artsandcrafts;

import android.app.SearchManager;
import android.provider.SearchRecentSuggestions;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.*;
import android.widget.*;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class GridActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grid);

        List<Integer> thumbIds = new ArrayList<Integer>();
        GridView gridview = (GridView) findViewById(R.id.gridview);
        gridview.setAdapter(new ImageAdapter(this, thumbIds));
        String category = getIntent().getStringExtra("category");
        //new TokenTask().execute();
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                // Add Etsy intent here
                Toast.makeText(GridActivity.this, "" + position, Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
// If the nav drawer is open, hide action items related to the content view
        menu.findItem(R.id.search).setVisible(true);
        return super.onPrepareOptionsMenu(menu);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    // Handle item selection
        switch (item.getItemId()) {
            case R.id.search:
                Toast.makeText(GridActivity.this, "Searched", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public String postRequest() {
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost("https://api.artsy.net/api/tokens/xapp_token");

        post.setHeader("Authorization", "Basic (with a username and password)");
        post.setHeader("Content-type", "application/json");

        try {
            List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
            urlParameters.add(new BasicNameValuePair("client_id", "a24764bd3ae92bc348ae"));
            urlParameters.add(new BasicNameValuePair("client_secret", "3e3fa9b294423b17b6d674da242e03c7"));

            post.setEntity(new UrlEncodedFormEntity(urlParameters));

            HttpResponse response = client.execute(post);

            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

            StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }

            JSONObject o = new JSONObject(result.toString());
            String xapp_token = o.get("token").toString();
            System.out.println(xapp_token);
            return xapp_token;

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    class TokenTask extends AsyncTask<String, Void, String> {

        private Exception exception;

        protected String doInBackground(String... urls) {
            try {
                return postRequest();
            } catch (Exception e) {
                this.exception = e;
                return null;
            }
        }
    }

    public class ImageAdapter extends BaseAdapter {
        private Context mContext;
        private List<Integer> mThumbIds;

        public ImageAdapter(Context c, List<Integer> ids) {
            mContext = c;
            mThumbIds = ids;
        }

        public int getCount() {
            return mThumbIds.size();
        }

        public Object getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return 0;
        }

        // create a new ImageView for each item referenced by the Adapter
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView;
            if (convertView == null) {  // if it's not recycled, initialize some attributes
                imageView = new ImageView(mContext);
                imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setPadding(8, 8, 8, 8);
            } else {
                imageView = (ImageView) convertView;
            }

            imageView.setImageResource(mThumbIds.get(position));
            return imageView;
        }

    }
}