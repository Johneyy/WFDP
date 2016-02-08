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

public class AboutStand extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ListView beerList = null;

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
                "Orthodox Mild",
                "Black Hope",
                "So Far So Dark AleBrowar i Artezan"
        };

        beerList = (ListView) findViewById(R.id.listView);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.beers_list_item, R.id.beer_name, products);
        beerList.setAdapter( adapter );

        justifyListViewHeightBasedOnChildren( beerList );

        final ScrollView sv = (ScrollView)findViewById( R.id.about_stand_scrollView );
        sv.getViewTreeObserver().addOnGlobalLayoutListener( new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                // Ready, move up
                sv.fullScroll( View.FOCUS_UP );
            }
        } );
    }

    private void justifyListViewHeightBasedOnChildren (ListView listView) {
        ListAdapter adapter = listView.getAdapter();

        if (adapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < adapter.getCount(); i++) {
            View listItem = adapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams par = listView.getLayoutParams();
        par.height = totalHeight + (listView.getDividerHeight() * (adapter.getCount() - 1));
        listView.setLayoutParams( par );
        listView.requestLayout();
    }
}
