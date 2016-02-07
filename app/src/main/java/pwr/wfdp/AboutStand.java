package pwr.wfdp;

import android.os.Bundle;
import android.support.design.widget.NavigationView;

public class AboutStand extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_stand);
        setFullscreen();
        makeMenu();
    }

}
