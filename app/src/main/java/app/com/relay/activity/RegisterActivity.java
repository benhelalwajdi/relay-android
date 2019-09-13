package app.com.relay.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import app.com.relay.R;
import app.com.relay.fragment.ClientRegistrationFragment;
import app.com.relay.fragment.DelivererRegistrationFragment;
import app.com.relay.fragment.StoreRegistrationFragment;
import app.com.relay.fragment.UserTypeFragment;
import app.com.relay.utils.ViewAnimation;

public class RegisterActivity extends AppCompatActivity {

    private static final int MAX_STEP = 2;
    private int current_step = 1;
    private FrameLayout frameLayout;
    private String userType;
    private LinearLayout lyt_next;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initComponent();

        getSupportFragmentManager().beginTransaction().replace(R.id.placeholder,
                new UserTypeFragment()).commit();
    }

    private void initComponent() {
        frameLayout = findViewById(R.id.placeholder);
        ((LinearLayout) findViewById(R.id.lyt_back)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backStep(current_step);
                bottomProgressDots(current_step);
            }
        });

        lyt_next = (LinearLayout) findViewById(R.id.lyt_next);
        lyt_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!checkRegistrationUserType().equals("NULL")) {
                    nextStep(current_step);
                    bottomProgressDots(current_step);
                }
            }
        });

        bottomProgressDots(current_step);

    }

    private void nextStep(int progress) {
        if (progress < MAX_STEP) {
            progress++;
            current_step = progress;
            ViewAnimation.fadeOutIn(frameLayout);
        }
        switchFragment(progress);
        lyt_next.setClickable(false);
    }

    private void backStep(int progress) {
        if (progress > 1) {
            progress--;
            current_step = progress;
            ViewAnimation.fadeOutIn(frameLayout);
        }
        switchFragment(progress);
        lyt_next.setClickable(true);
    }

    private void bottomProgressDots(int current_index) {
        current_index--;
        LinearLayout dotsLayout = (LinearLayout) findViewById(R.id.layoutDots);
        ImageView[] dots = new ImageView[MAX_STEP];

        dotsLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new ImageView(this);
            int width_height = 15;
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(new ViewGroup.LayoutParams(width_height, width_height));
            params.setMargins(10, 10, 10, 10);
            dots[i].setLayoutParams(params);
            dots[i].setImageResource(R.drawable.shape_circle);
            dots[i].setColorFilter(getResources().getColor(R.color.grey_20), PorterDuff.Mode.SRC_IN);
            dotsLayout.addView(dots[i]);
        }

        if (dots.length > 0) {
            dots[current_index].setImageResource(R.drawable.shape_circle);
            dots[current_index].setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        }
    }

    private void switchFragment(int progress) {
        if (progress == 1) {
            getSupportFragmentManager().beginTransaction().replace(R.id.placeholder,
                    new UserTypeFragment()).commit();
        } else {
            if (checkRegistrationUserType().equals("CLIENT")) {
                getSupportFragmentManager().beginTransaction().replace(R.id.placeholder,
                        new ClientRegistrationFragment()).commit();
            } else if (checkRegistrationUserType().equals("STORE")) {
                getSupportFragmentManager().beginTransaction().replace(R.id.placeholder,
                        new StoreRegistrationFragment()).commit();
            } else if (checkRegistrationUserType().equals("DELIVERER")) {
                getSupportFragmentManager().beginTransaction().replace(R.id.placeholder,
                        new DelivererRegistrationFragment()).commit();
            }
        }
    }

    private String checkRegistrationUserType() {
        SharedPreferences prfs = getSharedPreferences("REGISTRATION",
                Context.MODE_PRIVATE);
        return prfs.getString("USER_TYPE", "");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }


}
