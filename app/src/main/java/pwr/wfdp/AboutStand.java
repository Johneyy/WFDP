package pwr.wfdp;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;

import java.util.ArrayList;
import java.util.HashMap;

public class AboutStand extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener{

    // List view
    private ListView lv;

    // Listview Adapter
    ArrayAdapter<String> adapter;

    // Search EditText


    // ArrayList for Listview
    ArrayList<HashMap<String, String>> productList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_about_stand );
        setFullscreen();
        makeMenu();
        String products[] = {
                "Rowing Jack",
                "Crazy Mike",
                "Sweet Cow",
                "Ortodox Mild",
                "Black Hope",
                "So Far So Dark AleBrowar i Artezan"
        };

        lv = (ListView) findViewById(R.id.listView);

        adapter = new ArrayAdapter<String>(this, R.layout.beers_list_item, R.id.beer_name, products);
        lv.setAdapter( adapter );

        justifyListViewHeightBasedOnChildren( lv );

        final ScrollView sv = (ScrollView)findViewById( R.id.about_stand_scrollView );
        sv.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                // Ready, move up
                sv.fullScroll(View.FOCUS_UP);
            }
        });
    }

    private void justifyListViewHeightBasedOnChildren (ListView listView) {

        ListAdapter adapter = listView.getAdapter();

        if (adapter == null) {
            return;
        }
        ViewGroup vg = listView;
        int totalHeight = 0;
        for (int i = 0; i < adapter.getCount(); i++) {
            View listItem = adapter.getView(i, null, vg);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams par = listView.getLayoutParams();
        par.height = totalHeight + (listView.getDividerHeight() * (adapter.getCount() - 1));
        listView.setLayoutParams(par);
        listView.requestLayout();
    }
}
