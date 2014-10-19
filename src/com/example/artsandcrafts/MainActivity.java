package com.example.artsandcrafts;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

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
        public View getView(int position, View convertView, ViewGroup parent) {

            if (inflater == null)
                inflater = (LayoutInflater) activity
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (convertView == null)
                convertView = inflater.inflate(R.layout.list_item, null);

            TextView name = (TextView) convertView.findViewById(R.id.category);
            name.setText(categories[position]);

            return convertView;
        }

    }
}