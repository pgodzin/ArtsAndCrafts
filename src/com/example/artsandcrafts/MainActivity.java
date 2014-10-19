package com.example.artsandcrafts;

import android.app.Activity;
import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.*;
import android.widget.*;

public class MainActivity extends ListActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String[] categories = getResources().getStringArray(R.array.categories);
        ListView lv = getListView();
        lv.setCacheColorHint(0);
        lv.setBackgroundResource(R.drawable.bg);
        setListAdapter(new ImageAdapter(this, categories));
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
        // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        // Assumes current activity is the searchable activity
        //searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.search:
                Toast.makeText(MainActivity.this, "Searched", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void switchToSearchFragment(String query) {
        Intent intent = new Intent(MainActivity.this, SearchActivity.class);
        intent.putExtra("query", query);
        startActivity(intent);
    }

    public class ImageAdapter extends BaseAdapter {
        private Activity activity;
        private LayoutInflater inflater;
        private String[] categories;

        public ImageAdapter(Activity activity, String[] categories) {
            this.activity = activity;
            this.categories = categories;
        }

        @Override
        public int getCount() {
            return categories.length;
        }

        @Override
        public Object getItem(int location) {
            return categories[location];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            if (inflater == null)
                inflater = (LayoutInflater) activity
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (convertView == null)
                convertView = inflater.inflate(R.layout.list_item, null);

            TextView name = (TextView) convertView.findViewById(R.id.category);
            name.setText(categories[position]);

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(MainActivity.this, GridActivity.class);
                    i.putExtra("category", categories[position]);
                    startActivity(i);
                }
            });
            return convertView;
        }

    }
}