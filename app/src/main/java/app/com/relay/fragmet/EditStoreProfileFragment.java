package app.com.relay.fragmet;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.koushikdutta.async.future.FutureCallback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import app.com.relay.R;
import app.com.relay.activity.AddProductActivity;
import app.com.relay.activity.EditProductActivity;
import app.com.relay.activity.StoreAccountActivity;
import app.com.relay.model.Store;
import app.com.relay.service.StoreService;
import app.com.relay.utils.Constant;
import app.com.relay.utils.Tools;

import static android.app.Activity.RESULT_OK;

public class EditStoreProfileFragment extends Fragment implements View.OnClickListener{
    View view;
    String id ;
    ImageView ImageStore ;
    String path;
    String ImageName ;
    StoreService service ;
    String Image ;
    FloatingActionButton uploadButton ;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        id = getArguments().getString("idStore");
        setDataStore();
        initToolbar();

    //return inflater.inflate(R.layout.fragment_edit_store_profile, container, false);
        View mView = inflater.inflate(R.layout.fragment_edit_store_profile, container, false);
        uploadButton=(FloatingActionButton)mView.findViewById(R.id.uploadImageStore);
        uploadButton.setOnClickListener(this);

        return mView;
    }


    private void initToolbar() {
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbarEditProfile);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("General information ");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
//       Tools.setSystemBarColor((Activity) this.getActivity().getApplicationContext(), R.color.blue_300);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_done, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            getActivity().finish();
        } else {
            upload();
            startActivity(new Intent(this.getActivity(), StoreAccountActivity.class));

        }
        return super.onOptionsItemSelected(item);
    }
/*
    protected void upload(){
        System.out.println("Url image :"+ path);
        if(path != null){
        File f = new File(path);
        ImageName = f.getName();
        Date currentTime = Calendar.getInstance().getTime();
        System.out.println(f.getParent());
        System.out.println("Nom d'image : "+ImageName);
        File to = new File(f.getParent(),ImageName);
        f.renameTo(to);
            System.out.println("hello ");
        com.koushikdutta.async.future.Future uploading = com.koushikdutta.ion.Ion.with(this.getContext())
                .load(Constant.URL_UPLOAD_IMAGE)
                .setMultipartFile("profile", f)
                .asString()
                .withResponse()
                .setCallback(new FutureCallback<com.koushikdutta.ion.Response<String>>() {
                    @Override
                    public void onCompleted(Exception e, com.koushikdutta.ion.Response<String> result) {
                        try {
                            System.out.println("resultat of upload image "+result);
                            JSONObject jobj = new JSONObject(result.getResult());
                            System.out.println("Json Image : "+     jobj.getString("image"));
                            updateStore(jobj.getString("image"));
                            startActivity(new Intent(getActivity().getApplicationContext(), StoreAccountActivity.class));

                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }
                    }
                });
        }else{
            updateStore(Image);
            startActivity(new Intent(getActivity().getApplicationContext(), StoreAccountActivity.class));
        }
    }
*/
    protected void upload(){
        if(path!= null){
            File f = new File(path);
            ImageName = f.getName();
            Date currentTime = Calendar.getInstance().getTime();
            System.out.println(f.getParent());
            System.out.println("Nom d image : "+ImageName);
            File to = new File(f.getParent(),ImageName);
            f.renameTo(to);
            com.koushikdutta.async.future.Future uploading = com.koushikdutta.ion.Ion.with(getContext().getApplicationContext())
                    .load(Constant.URL_UPLOAD_IMAGE)
                    .setMultipartFile("profile", f)
                    .asString()
                    .withResponse()
                    .setCallback(new FutureCallback<com.koushikdutta.ion.Response<String>>() {
                        @Override
                        public void onCompleted(Exception e, com.koushikdutta.ion.Response<String> result) {
                            try {
                                JSONObject jobj = new JSONObject(result.getResult());
                                System.out.println("Json Image : "+     jobj.getString("image"));
                                updateStore(jobj.getString("image"));
                            } catch (JSONException e1) {
                                e1.printStackTrace();
                            }
                        }
                    });
        }else{
            updateStore(Image);
        }
    }

    private void updateStore( String image) {
        service = new StoreService(this.getContext(),getActivity());
        Store store = new Store();
        EditText phone = getActivity().findViewById(R.id.phoneStoreEdit);
        store.setPhoneNumber(String.valueOf(phone.getText()));
        EditText name_store = getActivity().findViewById(R.id.nameStoreEdit);
        store.setStoreName(String.valueOf(name_store.getText()));
        EditText address = getActivity().findViewById(R.id.addressStoreEdit);
        store.setAddress(String.valueOf(address.getText()));
        EditText mail = getActivity().findViewById(R.id.mailStoreEdit);
        store.setMail(String.valueOf(mail.getText()));
        System.out.print(image + " ffffff");
        store.setImage(image);
        store.setStoreType("STORE");
        System.out.println(store.toString());
        service.editStore(Constant.URL_UPDATE_STORE, store, id, getActivity().getApplicationContext());
        Toast.makeText(getActivity().getApplicationContext(), name_store.getText(), Toast.LENGTH_SHORT).show();
        startActivity(new Intent(getActivity().getApplicationContext(), StoreAccountActivity.class));
    }


    public void setDataStore(){
        String URL = Constant.URL_STORES + id  ;
        System.out.println(URL);
        JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(
                Request.Method.GET,
                URL,
                null,
                new Response.Listener<JSONObject>() {
                    HashMap<String, String> storeMap = new HashMap<>();

                    @Override
                    public void onResponse(JSONObject response) {
                        ArrayList<HashMap<String, String>> list2 = new ArrayList<>();
                        try {
                                // product = new Product(jsonObject);
                                Log.e("Respence Json: ", String.valueOf(response));
                                Log.e("Respence Store: ",  response.getString("store_name"));

                                storeMap.put("store_name", response.getString("store_name"));
                                EditText storeName  = getActivity().findViewById(R.id.nameStoreEdit);
                                storeName.setText(storeMap.get("store_name"));

                                storeMap.put("phone_number",response.getString("phone_number"));
                                EditText storePhone  = getActivity().findViewById(R.id.phoneStoreEdit);
                                storePhone.setText(storeMap.get("phone_number"));

                                storeMap.put("address", response.getString("address"));
                                EditText addressStore  = getActivity().findViewById(R.id.addressStoreEdit);
                                addressStore.setText(storeMap.get("address"));

                                storeMap.put("mail",response.getString("mail"));
                                EditText mailStore  = getActivity().findViewById(R.id.mailStoreEdit);
                                mailStore.setText(storeMap.get("mail"));

                                storeMap.put("image",Constant.URL_IMAGE+response.getString("image"));
                                ImageStore = getActivity().findViewById(R.id.imageStoreEdit);
                                Picasso.with(getActivity().getApplicationContext()).load(storeMap.get("image")).resize(200, 200).into(ImageStore);

                                storeMap.put("user_type",response.getString("user_type"));
                                storeMap.put("store_type",response.getString("store_type"));
                                storeMap.put("id", String.valueOf(response.getString("id")));
                                //System.out.println("Size of list is  : "+ list.size());
                                Image = response.getString("image");
                                list2.add(storeMap);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("ERROR", "Oups Something went wrong !");
                    }
                }
        );
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        queue.add(jsonArrayRequest);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        System.out.println("path : image after this ");
        if (data == null)
            return;
        switch (requestCode) {
            case 100:
                if (resultCode == RESULT_OK) {
                    path = getPathFromURI(data.getData());
                    System.out.println("path : "+path);
                    ImageStore.setImageURI(data.getData());
                }
        }
    }

    private String getPathFromURI(Uri contentUri) {
        String[] proj = { MediaStore.Images.Media.DATA };
        CursorLoader loader = new CursorLoader(getContext(), contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.uploadImageStore:
                Intent fintent = new Intent(Intent.ACTION_GET_CONTENT);
                fintent.setType("image/jpeg");
                try {
                    startActivityForResult(fintent, 100);
                } catch (ActivityNotFoundException e) {

                }
                break;
            default:
                break;
        }
    }
}
