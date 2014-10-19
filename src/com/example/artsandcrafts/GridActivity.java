package com.example.artsandcrafts;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.*;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GridActivity extends Activity {

    private String token = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grid);

        Map<String, String> hm = new HashMap<String, String>();
        hm.put("African", "contemporary-african-art");
        hm.put("Contemporary Pop", "contemporary-pop");
        hm.put("Greek and Roman", "greek-and-roman-art-and-architecture");
        hm.put("Impressionism", "impressionism");
        hm.put("Minimalism", "minimalism");
        hm.put("Post-War American", "post-war-american-art");
        hm.put("Post-War European", "post-war-european-art");

        List<Drawable> thumbIds = new ArrayList<Drawable>();
        GridView gridview = (GridView) findViewById(R.id.gridview);
        String category = getIntent().getStringExtra("category");
        setTitle(category);
        try {
            token = new TokenTask().execute().get();
        } catch (Exception e) {
            System.out.println("Y" + e.getMessage());
        }
        String query = hm.get(category);
        try {
            List<Drawable> thumbs = new TokenGetTask().execute(query).get();
        } catch (Exception e) {
            System.out.println("X" + e.getMessage());
        }
        gridview.setAdapter(new ImageAdapter(this, thumbIds));
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
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

    public List<Drawable> getRequest(String type) {
        String[] words = type.split(" ");
        String BASE_URL = "https://api.artsy.net/api/search?q=";
        for (String word : words) {
            BASE_URL += word + "+";
        }
        BASE_URL += "more:pagemap:metatags-og_type:artwork";

        Request request = new Request();
        request.setUrl(BASE_URL);
        request.setToken(token);

        try {
            request.makeGetCall();
        } catch (Exception e) {
            System.out.println(e);
        }

        try {
            ArrayList<Drawable> images = new ArrayList<Drawable>();
            for (int i = 0; i < 1; i++) {
                JSONObject object = new JSONObject(request.toString());
                String title = object.getJSONObject("_embedded").getJSONArray("results").getJSONObject(i).getString("title");
                title = title.split(", ")[1].split("\\(")[0];
                title.replaceAll(" ", "_");
                System.out.println("title: " + title);
                String url = object.getJSONObject("_embedded").getJSONArray("results").getJSONObject(i).getJSONObject("_links").getJSONObject("thumbnail").getString("href");
                System.out.println(url);
                images.add(LoadImageFromWebOperations(url));
            }
            return images;
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    public static Drawable LoadImageFromWebOperations(String url) {
        try {
            InputStream is = (InputStream) new URL(url).getContent();
            System.out.println(url);
            Drawable d = Drawable.createFromStream(is, "src name");
            System.out.println("done");
            return d;
        } catch (Exception e) {
            return null;
        }
    }

    public String postRequest() {
        String BASE_URL = "https://api.artsy.net/api/tokens/xapp_token";

        Request request = new Request();
        request.setUrl(BASE_URL);
        request.addParameter("client_id", "a24764bd3ae92bc348ae");
        request.addParameter("client_secret", "3e3fa9b294423b17b6d674da242e03c7");

        try {
            request.makeCall();
        } catch (Exception e) {
            System.out.println(e);
        }

        try {
            JSONObject object = new JSONObject(request.toString());
            return object.getString("token");
        } catch (Exception e) {
            System.out.println(e);
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

    class TokenGetTask extends AsyncTask<String, Void, List<Drawable>> {

        private Exception exception;

        protected List<Drawable> doInBackground(String... urls) {
            try {
                return getRequest(urls[0]);
            } catch (Exception e) {
                this.exception = e;
                return null;
            }
        }
    }

    public class ImageAdapter extends BaseAdapter {
        private Context mContext;
        private List<Drawable> mThumbIds;

        public ImageAdapter(Context c, List<Drawable> ids) {
            mContext = c;
            mThumbIds = ids;
            mThumbIds.add(getResources().getDrawable(R.drawable.bg));

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
                imageView.setLayoutParams(new GridView.LayoutParams(250, 250));
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setPadding(8, 8, 8, 8);
            } else {
                imageView = (ImageView) convertView;
            }
            imageView.setImageDrawable(mThumbIds.get(position));
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // ETSY HERE
                }
            });
            return imageView;
        }

    }
}