package app.com.relay.model;

import org.json.JSONException;
import org.json.JSONObject;

public class Store {

    private int id;
    private String storeType;
    private String storeName;
    private String phoneNumber;
    private String address;
    private String mail;
    private String password;
    private String image;

    public Store(){

    }

    public Store(String storeType, String storeName, String phoneNumber, String address,
                 String mail, String password, String image) {
        this.storeType = storeType;
        this.storeName = storeName;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.mail = mail;
        this.password = password;
        this.image = image;

    }

    public Store(JSONObject jsonObject) {
        try {
            this.id = jsonObject.getInt("id");
            this.storeName = jsonObject.getString("store_name");
            this.storeType = jsonObject.getString("store_type");
            this.phoneNumber = jsonObject.getString("phone_number");
            this.address = jsonObject.getString("address");
            this.mail = jsonObject.getString("mail");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
        public String getStoreType() {
        return storeType;
    }

    public void setStoreType(String storeType) {
        this.storeType = storeType;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "Store{" +
                "storeType='" + storeType + '\'' +
                ", storeName='" + storeName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", address='" + address + '\'' +
                ", mail='" + mail + '\'' +
                ", password='" + password + '\'' +
                ", image='" + image + '\'' +
                '}';
    }

}
