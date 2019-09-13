package app.com.relay.model;

import android.graphics.drawable.Drawable;

public class Order {

        public int image;
        public Drawable imageDrw;
        public String name;
        public String email;
        public boolean section = false;

        public Order() {
        }

        public Order(String name, boolean section) {
            this.name = name;
            this.section = section;
        }


}
