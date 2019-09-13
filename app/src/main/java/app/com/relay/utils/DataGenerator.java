package app.com.relay.utils;

import android.content.Context;
import android.content.res.TypedArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import app.com.relay.R;
import app.com.relay.model.Category;
import app.com.relay.model.Order;
import app.com.relay.model.Product;
import app.com.relay.model.Review;

public class DataGenerator {

    /**
     * Generate dummy data shopping product
     * @return list of object
     */
  /*  public static List<Product> getShoppingProduct(Context ctx) {
        List<Product> items = new ArrayList<>();
       // TypedArray drw_arr = ctx.getResources().obtainTypedArray(R.array.shop_product_image);
        String title_arr[] = ctx.getResources().getStringArray(R.array.shop_product_title);
        String price_arr[] = ctx.getResources().getStringArray(R.array.shop_product_price);
        for (int i = 0; i < 8 ; i++) {
            Product obj = new Product();
           //  obj.image = drw_arr.getResourceId(i, -1);
            obj.title = title_arr[i];
            obj.price = price_arr[i];
            //obj.imageDrw = ctx.getResources().getDrawable(R.drawable.image_shop_2);
            items.add(obj);
        }
        return items;
    }
*/
    /**
     * Generate dummy data shopping category
     *
     * @param ctx android context
     * @return list of object
     */
    public static List<Category> getShoppingCategory(Context ctx) {
        List<Category> items = new ArrayList<>();
        TypedArray drw_arr = ctx.getResources().obtainTypedArray(R.array.shop_category_icon);
        TypedArray drw_arr_bg = ctx.getResources().obtainTypedArray(R.array.shop_category_bg);
        String title_arr[] = ctx.getResources().getStringArray(R.array.shop_category_title);
        String brief_arr[] = ctx.getResources().getStringArray(R.array.shop_category_brief);
        for (int i = 0; i < drw_arr.length(); i++) {
            Category obj = new Category();
            obj.image = drw_arr.getResourceId(i, -1);
            obj.image_bg = drw_arr_bg.getResourceId(i, -1);
            obj.title = title_arr[i];
            obj.brief = brief_arr[i];
            obj.imageDrw = ctx.getResources().getDrawable(obj.image);
            items.add(obj);
        }
        return items;
    }

    public static List<Order> getOrder(Context ctx, ArrayList<HashMap<String, String>> list) {
        List<Order> items = new ArrayList<>();
     /*    "id_product": 9,
        "date": "2018-12-09T23:00:00.000Z",
        "state": "IN PROGRESS",
        "reference": "017FA40856",
        "quantity": 1
    */
        for (HashMap<String, String> order : list) {
            Order obj = new Order();
            obj.name = order.get("reference");
            obj.email = order.get("state")+" "+order.get("date")+" "+order.get("quantity");
            items.add(obj);
        }
        return items;
    }
    public static List<Product> getProd(Context ctx, ArrayList<HashMap<String, String>> list, HashMap<String, String> storeMap) {
        System.out.println("data generator 79: "+list.toString());
        List<Product> items = new ArrayList<>();
        for (HashMap<String, String> product : list) {
            Product obj = new Product();
            System.out.println("product line 83 : "+product.toString());
            obj.setDescription(product.get("description"));
            obj.setName(product.get("name"));
            obj.setDate(product.get("date"));
            obj.setImage(product.get("image"));
            obj.setId(product.get("id"));
            obj.setQuantity(product.get("quantity"));
            obj.setSize(product.get("size"));
            obj.setPrice(product.get("price"));
            System.out.println("obj line 87: "+obj.toString());
            items.add(obj);
        }
        return items;
    }



    public static List<Review> getReview(Context ctx, ArrayList<HashMap<String, String>> list) {
        System.out.println("data generator 101: "+list.toString());
        List<Review> items = new ArrayList<>();
        for (HashMap<String, String> review : list) {
            Review obj = new Review();
            System.out.println("product line 106 : "+review.toString());
            obj.setId(review.get("id"));
            obj.setRating(review.get("rating"));
            obj.setComment(review.get("comment"));
            System.out.println("obj line 114: "+obj.toString());
            items.add(obj);
        }
        return items;
    }
}
