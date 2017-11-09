package com.example.jedrzej.sortify.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.jedrzej.sortify.datamodels.Pack;
import com.example.jedrzej.sortify.R;

public class PackageActivity extends AppCompatActivity {

    private Bundle mPackInfo;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_package);

        TextView packageName = (TextView) findViewById(R.id.name_text_view);
        TextView visibleId = (TextView) findViewById(R.id.visible_id_text_view);
        TextView location = (TextView) findViewById(R.id.location_text_view);
        TextView contents = (TextView) findViewById(R.id.contents_text_view);


        mPackInfo = getIntent().getExtras().getBundle("packInfo");

        packageName.setText(mPackInfo.getString(Pack.KEY_NAME));
        visibleId.setText(mPackInfo.getString(Pack.KEY_VISIBLEID));
        location.setText(mPackInfo.getString(Pack.KEY_LOCATION));


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_package_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.action_edit:
                Intent intent = new Intent(PackageActivity.this, EditPackActivity.class);
                intent.putExtra("packInfo", mPackInfo );
                startActivity(intent);
                return true;
            default :
                return super.onOptionsItemSelected(item);


        }
    }
}
