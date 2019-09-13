package app.com.relay.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

import java.util.ArrayList;

import app.com.relay.model.Client;
import app.com.relay.model.Product;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Relay";

    /* Table names */
    private static final String TABLE_CART_PRODUCT = "cart_product_table";
    private static final String TABLE_USER = "user_table";

    /* Common column names */

    /* PRODUCT Table - column names */
    private static final String CART_PRODUCT_ID = "id_product";
    private static final String CART_PRODUCT_NAME = "name";
    private static final String CART_PRODUCT_DESCRIPTION = "description";
    private static final String CART_PRODUCT_PRICE = "price";
    private static final String CART_PRODUCT_IMAGE = "image";
    private static final String CART_PRODUCT_SIZE = "size";
    private static final String CART_PRODUCT_QTE = "quantity";
    private static final String CART_PRODUCT_STORE_ID = "store_id";


    /* USER Table - column names */
    private static final String USER_ID = "user_id";
    private static final String FIRST_NAME = "first_name";
    private static final String LAST_NAME = "last_name";
    private static final String STORE_NAME = "store_name";
    private static final String PHONE_NUMBER = "phone_number";
    private static final String ADDRESS = "address";
    private static final String MAIL = "mail";
    private static final String PASSWORD = "password";
    private static final String USER_TYPE = "user_type";
    private static final String STORE_TYPE = "store_type";
    private static final String VEHICLE = "vehicle";


    /* PRODUCT TABLE create statements */
    private static final String CREATE_TABLE_PRODUCT =
            "CREATE TABLE IF NOT EXISTS " + TABLE_CART_PRODUCT + "(" +
                    "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                    CART_PRODUCT_ID + " TEXT," +
                    CART_PRODUCT_NAME + " TEXT," +
                    CART_PRODUCT_DESCRIPTION + " TEXT," +
                    CART_PRODUCT_PRICE + " TEXT," +
                    CART_PRODUCT_IMAGE + " TEXT," +
                    CART_PRODUCT_SIZE + " TEXT," +
                    CART_PRODUCT_QTE + " INTEGER," +
                    CART_PRODUCT_STORE_ID + " TEXT)";


    /* USER TABLE create statement */
    private static final String CREATE_TABLE_USER =
            "CREATE TABLE IF NOT EXISTS " + TABLE_USER + "(" +
                    "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                    USER_ID + " TEXT," +
                    FIRST_NAME + " TEXT," +
                    LAST_NAME + " TEXT," +
                    STORE_NAME + " TEXT," +
                    PHONE_NUMBER + " TEXT," +
                    ADDRESS + " TEXT," +
                    MAIL + " TEXT," +
                    PASSWORD + " TEXT," +
                    USER_TYPE + " TEXT," +
                    STORE_TYPE + " TEXT," +
                    VEHICLE + " TEXT)";

    /* INSERT USER WITH ID = 1 */
    private static final String INSERT_USER = "" +
            "INSERT INTO " + TABLE_USER + " (id) VALUES (1)";

    /* SELECT USER */
    private static final String SELECT_USER = "SELECT * FROM " + TABLE_USER + " WHERE ID = 1";

    /* SELECT PRODUCT */
    private static final String SELECT_PRODUCT = "SELECT * FROM " +
            TABLE_CART_PRODUCT + " WHERE " + CART_PRODUCT_ID + " = ?";

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_PRODUCT);
        db.execSQL(CREATE_TABLE_USER);
        db.execSQL(INSERT_USER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP  TABLE IF EXISTS " + TABLE_CART_PRODUCT);
        db.execSQL("DROP  TABLE IF EXISTS " + TABLE_USER);
        onCreate(db);
    }

    public void addClient(Client client) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(USER_ID, client.getId());
        contentValues.put(FIRST_NAME, client.getFirstName());
        contentValues.put(LAST_NAME, client.getLastName());
        contentValues.put(PHONE_NUMBER, client.getPhoneNumber());
        contentValues.put(ADDRESS, client.getAddress());
        contentValues.put(MAIL, client.getMail());
        contentValues.put(PASSWORD, client.getPassword());
        contentValues.put(USER_TYPE, "CLIENT");
        db.update(TABLE_USER, contentValues, "id" + " = ?",
                new String[]{"1"});
    }

    public Client getCurrentClient() {
        Client client = new Client();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor data = db.rawQuery(SELECT_USER, null);
        while (data.moveToNext()) {
            client = new Client(
                    data.getString(1),
                    data.getString(2),
                    data.getString(3),
                    data.getString(5),
                    data.getString(6),
                    data.getString(7),
                    data.getString(8)
            );
        }
        return client;
    }

    public String getUserType() {
        String userType = "";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor data = db.rawQuery(SELECT_USER, null);
        while (data.moveToNext()) {
            userType = data.getString(9);
        }
        return userType;
    }

    public String getClientFullName() {
        String fullName = "";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor data = db.rawQuery(SELECT_USER, null);
        while (data.moveToNext()) {
            fullName = data.getString(2) + " " + data.getString(3);
        }
        return fullName;
    }

    public boolean addProductToCart(Product product) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(CART_PRODUCT_ID, product.getId());
        contentValues.put(CART_PRODUCT_NAME, product.getName());
        contentValues.put(CART_PRODUCT_DESCRIPTION, product.getDescription());
        contentValues.put(CART_PRODUCT_PRICE, product.getPrice());
        contentValues.put(CART_PRODUCT_IMAGE, product.getImage());
        contentValues.put(CART_PRODUCT_SIZE, product.getSize());
        contentValues.put(CART_PRODUCT_QTE, 1);
        contentValues.put(CART_PRODUCT_STORE_ID, product.getIdStore());

        long result = db.insert(TABLE_CART_PRODUCT, null, contentValues);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }


    public ArrayList<Product> getProductList() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_CART_PRODUCT;
        Cursor data = db.rawQuery(query, null);

        ArrayList<Product> listData = new ArrayList<>();
        while (data.moveToNext()) {
            listData.add(new Product(
                    data.getString(1),
                    data.getString(2),
                    data.getString(3),
                    data.getString(4),
                    data.getString(5),
                    data.getString(6),
                    data.getString(7),
                    data.getString(8)));
        }

        return listData;
    }

    public Product getProductById(String id) {
        Product product;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor data = db.rawQuery(SELECT_PRODUCT, new String[]{id});
        if (data.moveToFirst()) {
            product = new Product(
                    data.getString(1),
                    data.getString(2),
                    data.getString(3),
                    data.getString(4),
                    data.getString(5),
                    data.getString(6),
                    data.getString(7),
                    data.getString(8));
            return product;
        } else {
            return null;
        }
    }

    public boolean deleteProductFromCart(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.delete(TABLE_CART_PRODUCT, CART_PRODUCT_ID + "=?",
                new String[]{id}) > 0;
    }

    public void updateQuantity(Product product) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CART_PRODUCT_QTE, product.getQuantity());

        db.update(TABLE_CART_PRODUCT, contentValues, CART_PRODUCT_ID + " = ?",
                new String[]{product.getId()});
    }

}
