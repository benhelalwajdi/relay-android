package app.com.relay.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import app.com.relay.fragmet.EditStoreProfileFragment;
import app.com.relay.R;
import app.com.relay.model.Store;
import app.com.relay.service.StoreService;
import app.com.relay.utils.Constant;
import app.com.relay.utils.Tools;

public class EditProfile extends AppCompatActivity {

    String id,userType ;

    @SuppressLint("CommitTransaction")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        SharedPreferences prfs = getSharedPreferences("AUTHENTICATION_FILE_NAME",
                Context.MODE_PRIVATE);
        userType = prfs.getString("USER_TYPE", "");

        if (userType.equals("STORE")) {
            id = "54";
            System.out.println(userType);
            Bundle bundle = new Bundle();
            bundle.putString("idStore", id);
            EditStoreProfileFragment firstFrag = new EditStoreProfileFragment();
            firstFrag.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().add(R.id.frameLayout, firstFrag).commit();
        }
        }

}
