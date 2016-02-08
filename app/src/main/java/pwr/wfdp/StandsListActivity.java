package pwr.wfdp;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;

public class StandsListActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    // List view
    private ListView lv;

    // Listview Adapter
    ArrayAdapter<String> adapter;

    // Search EditText
    EditText inputSearch;


    // ArrayList for Listview
    ArrayList<HashMap<String, String>> productList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stands_list);
        makeMenu();
        setFullscreen();


        // Listview Data
        String products[] = {"Stanowisko 1 - 100 Mostów","Stanowisko 2 - Against The Grain",
                "Stanowisko 3 - Alebrowar","Stanowisko 4 - Amager", "Stanowisko 5 - Amber",
                "Stanowisko 6 - Anchor Brewing", "Stanowisko 7 - Artezan",
                "Stanowisko 8 - Bakalar","Stanowisko 9 - Bazyliszek",
                "Stanowisko 10 - Beavertown", "Stanowisko 11 - Bednary","Stanowisko 12 - Beer City",
                "Stanowisko 22 - Brasserie de Pays Flamand", "Stanowisko 26 - BrewByNumbers",
                "Stanowisko 32 - Cernovar", "Stanowisko 45 - Dziedzice",
                "Stanowisko 84 - Miedzianka", "Stanowisko 94 - Opat"};

        lv = (ListView) findViewById(R.id.stands_list_view);
        inputSearch = (EditText) findViewById(R.id.inputSearch_standsList);

        // Adding items to listview
        adapter = new ArrayAdapter<String>(this, R.layout.event_list_item, R.id.product_name, products);
        lv.setAdapter(adapter);

        inputSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
                StandsListActivity.this.adapter.getFilter().filter(cs);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
            }
        });
    }
}
