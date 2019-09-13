package app.com.relay.model;

import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

public class Client {
    private String id;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String address;
    private String mail;
    private String password;
    private String image;

    public Client() {
    }

    public Client(String id,String firstName, String lastName, String phoneNumber, String address,
                  String mail, String password) {

        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.mail = mail;
        this.password = password;
    }

    public Client(JSONObject jsonObject) {
        try {
            this.id = jsonObject.getString("id");
            this.firstName = jsonObject.getString("first_name");
            this.lastName = jsonObject.getString("last_name");
            this.phoneNumber = jsonObject.getString("phone_number");
            this.address = jsonObject.getString("address");
            this.mail = jsonObject.getString("mail");
            this.password = jsonObject.getString("password");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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
        return "Client{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", address='" + address + '\'' +
                ", mail='" + mail + '\'' +
                ", password='" + password + '\'' +
                ", image='" + image + '\'' +
                '}';
    }
}
