package pwr.wfdp;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_stand);
        setFullscreen();
        makeMenu();
        String products[] = {"Rowing Jack", "Crazy Mike", "Sweet Cow", "Ortodox Mild", "Black Hope",
                "So Far So Dark AleBrowar i Artezan"};

        lv = (ListView) findViewById(R.id.listView);

        adapter = new ArrayAdapter<String>(this, R.layout.beers_list_item, R.id.beer_name, products);
        lv.setAdapter(adapter);
    }

}
