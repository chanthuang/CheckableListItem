package com.chant.checkablelistitem;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.chant.checkablelistitem.view.CheckableLinearLayout;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ListView listView = new ListView(this);
        initListView(listView);
        setContentView(listView);
    }

    private void initListView(ListView listView) {

        final Adapter adapter = new Adapter(this);
        listView.setAdapter(adapter);

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                boolean isEditing = adapter.isEditing();
                adapter.setEditing(!isEditing);
                if (isEditing) {
                    adapter.clearCheckedItems();
                }
                return true;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Adapter adapter = (Adapter) parent.getAdapter();
                if (adapter.isEditing()) {
                    adapter.setCheckedItem(position);
                }
            }
        });
    }

    private static class Adapter extends BaseAdapter {

        private boolean mIsEditing = false;
        private Context mContext;
        private Map<Integer, Boolean> mCheckedItems = new HashMap<>();

        public Adapter(Context context) {
            mContext = context;
        }

        @Override
        public int getCount() {
            return 20;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ItemView itemView;
            if (convertView != null) {
                itemView = (ItemView) convertView;
            } else {
                itemView = new ItemView(mContext);
            }
            itemView.render(String.valueOf(position));
            itemView.setEdit(isEditing());
            if (isEditing()) {
                itemView.setChecked(mCheckedItems.containsKey(position));
            }
            return itemView;
        }

        public boolean isEditing() {
            return mIsEditing;
        }

        public void setEditing(boolean editing) {
            if (mIsEditing != editing) {
                mIsEditing = editing;
                notifyDataSetChanged();
            }
        }

        public void setCheckedItem(int position) {
            if (mCheckedItems.containsKey(position)) {
                mCheckedItems.remove(position);
            } else {
                mCheckedItems.put(position, true);
            }
            notifyDataSetChanged();
        }

        public void clearCheckedItems() {
            mCheckedItems.clear();
        }
    }

    private static class ItemView extends CheckableLinearLayout {

        private TextView mTitleView;

        public ItemView(Context context) {
            super(context);
            init();
        }

        private void init() {
            setPadding(20, 20, 20, 20);

            mTitleView = new TextView(getContext());
            mTitleView.setTextColor(0xff000000);
            mTitleView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            addView(mTitleView);
        }

        public void render(String title) {
            mTitleView.setText(title);
        }

    }
}
