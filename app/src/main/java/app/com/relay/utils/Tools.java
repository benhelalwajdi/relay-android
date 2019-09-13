package app.com.relay.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.CardView;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.google.android.gms.maps.GoogleMap;

import app.com.relay.R;

public class Tools {
    public static void setSystemBarColor(Activity act, @ColorRes int color) {

        //TODO : review version control
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = act.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(act.getResources().getColor(color));
        }
    }


    public static void displayImageOriginal(Context ctx, ImageView img, @DrawableRes int drawable) {
        try {
            Glide.with(ctx).load(drawable)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(img);
        } catch (Exception e) {
        }
    }

    public static void displayImageRound(final Context ctx, final ImageView img, @DrawableRes int drawable) {
        try {
            Glide.with(ctx).load(drawable).asBitmap().centerCrop().into(new BitmapImageViewTarget(img) {
                @Override
                protected void setResource(Bitmap resource) {
                    RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(ctx.getResources(), resource);
                    circularBitmapDrawable.setCircular(true);
                    img.setImageDrawable(circularBitmapDrawable);
                }
            });
        } catch (Exception e) {
        }
    }

    public static void nestedScrollTo(final NestedScrollView nested, final View targetView) {
        nested.post(new Runnable() {
            @Override
            public void run() {
                nested.scrollTo(500, targetView.getBottom());
            }
        });
    }


    public static int dpToPx(Context c, int dp) {
        Resources r = c.getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    public static void showToast(String message, Activity activity) {
        View layout = activity.getLayoutInflater().inflate(R.layout.toast_custom,
                (ViewGroup) activity.findViewById(R.id.custom_toast_layout_id));
        TextView text = (TextView) layout.findViewById(R.id.text);
        text.setTextColor(Color.WHITE);
        text.setText(message);
        CardView lyt_card = (CardView) layout.findViewById(R.id.lyt_card);
        lyt_card.setCardBackgroundColor(activity.getResources().getColor(R.color.yellow_800));

        Toast toast = new Toast(activity);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.setGravity(Gravity.TOP, 0, 0);
        toast.show();
    }

    public static GoogleMap configActivityMaps(GoogleMap googleMap) {
          // set map type
            googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            // Enable / Disable zooming controls
            googleMap.getUiSettings().setZoomControlsEnabled(false);

            // Enable / Disable Compass icon
            googleMap.getUiSettings().setCompassEnabled(true);
            // Enable / Disable Rotate gesture
            googleMap.getUiSettings().setRotateGesturesEnabled(true);
            // Enable / Disable zooming functionality
            googleMap.getUiSettings().setZoomGesturesEnabled(true);

            googleMap.getUiSettings().setScrollGesturesEnabled(true);
            googleMap.getUiSettings().setMapToolbarEnabled(true);

            return googleMap;

    }
}
