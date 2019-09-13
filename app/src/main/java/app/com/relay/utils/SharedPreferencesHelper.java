package app.com.relay.utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import app.com.relay.activity.HomeActivity;
import app.com.relay.activity.StoreAccountActivity;

public class SharedPreferencesHelper {

    public static void checkUserTypeAndRedirect(Context context) {
        SharedPreferences prfs = context.getSharedPreferences("AUTHENTICATION_FILE_NAME",
                Context.MODE_PRIVATE);
        String userType = prfs.getString("USER_TYPE", "");
        System.out.println(userType);
        if (userType.equals("CLIENT")) {
            context.startActivity(new Intent(context, HomeActivity.class));
        }
        if (userType.equals("STORE")) {
            context.startActivity(new Intent(context, StoreAccountActivity.class));
        }
    }


    public static void saveUserName(String userName, Context context) {
        SharedPreferences preferences =
                context.getSharedPreferences("AUTHENTICATION_FILE_NAME",
                        Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("USER_NAME", userName);
        editor.apply();
    }

    public  static String checkUserName(Context context){
        SharedPreferences prfs = context.getSharedPreferences("AUTHENTICATION_FILE_NAME",
                Context.MODE_PRIVATE);
         return prfs.getString("USER_NAME", "");
    }



}
