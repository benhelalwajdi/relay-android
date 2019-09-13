package app.com.relay.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.mikhaellopez.circularimageview.CircularImageView;

import app.com.relay.R;
import app.com.relay.model.Client;
import app.com.relay.model.Store;
import app.com.relay.service.ClientService;
import app.com.relay.service.StoreService;
import app.com.relay.utils.Permissions;
import app.com.relay.utils.Tools;

public class StoreRegistrationFragment extends Fragment {

    private CircularImageView imageView;
    private Uri imageURI;
    private AppCompatEditText txtStoreName, txtPhoneNumber, txtMail, txtAddress, txtPassword;
    private static final int PICK_IMAGE = 100;
    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 13;


    private AppCompatButton single_choice_button;
    private String single_choice_selected;
    private static final String[] CATEGORIES = new String[]{
            "Supermarket", "Clothing", "Electronics", "Books, Movies, Music & Games", "Cosmetics & Body care",
            "Food & Drinks", "Bags & Accessories", "Household appliances",
            "Furniture & Household goods", "Sports & Outdoor", "Toys & Baby products",
            "Stationary & Hobby supplies", "Garden & Pets", "Other"};

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.store_registration_fragment, container, false);
        initToolbar();
        initComponent(view);
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("General information ");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);

    }

    private void initComponent(View view){
        txtStoreName = (AppCompatEditText) view.findViewById(R.id.txt_store_name);
        txtPhoneNumber = (AppCompatEditText) view.findViewById(R.id.txt_phone_number);
        txtMail = (AppCompatEditText) view.findViewById(R.id.txt_mail);
        txtAddress = (AppCompatEditText) view.findViewById(R.id.txt_address);
        txtPassword = (AppCompatEditText) view.findViewById(R.id.txt_password);

        single_choice_button = view.findViewById(R.id.single_choice_dialog);
        single_choice_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSingleChoiceDialog();
            }
        });

        imageView = (CircularImageView) view.findViewById(R.id.image);
        FloatingActionButton floatingActionButton =
                (FloatingActionButton) view.findViewById(R.id.fab_picture);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPermission();
            }
        });
    }

    private void showSingleChoiceDialog() {
        single_choice_selected = CATEGORIES[0];
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Categories");
        builder.setSingleChoiceItems(CATEGORIES, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                single_choice_selected = CATEGORIES[i];
            }
        });
        builder.setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                single_choice_button.setText(single_choice_selected);
            }
        });
        builder.setNegativeButton(R.string.CANCEL, null);
        builder.show();
    }

    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                        Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    Permissions.askForExternalStoragePermission(getActivity());
                } else {
                    Permissions.askForExternalStoragePermission(getActivity());

                }
            } else if (ContextCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Intent i = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, PICK_IMAGE);
            } else {
                Permissions.askForExternalStoragePermission(getActivity());
            }
        } else {
            Intent i = new Intent(Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(i, PICK_IMAGE);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
        }


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == PICK_IMAGE) {
            imageURI = data.getData();
            imageView.setImageURI(imageURI);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_done, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_done) {
            saveStore();
        }
        return super.onOptionsItemSelected(item);
    }


    private void saveStore() {
        if (single_choice_button.getText().toString().equals(String.valueOf("Category*"))){
            Tools.showToast("Please select a category", getActivity());
            return;
        }
        if (txtStoreName.getText().toString().matches("")) {
            Tools.showToast("Please enter your store name", getActivity());
            return;
        }
        if (txtPhoneNumber.getText().toString().matches("")) {
            Tools.showToast("Please enter your phone number", getActivity());
            return;
        }

        if (txtMail.getText().toString().matches("")) {
            Tools.showToast("Please enter your mail", getActivity());
        }
        if (!txtMail.getText().toString().matches("[a-zA-Z0-9._-]+@[a-z]+.[a-z]+")) {
            Tools.showToast("Please enter a valid mail", getActivity());
            return;
        }
        if (txtAddress.getText().toString().matches("")) {
            Tools.showToast("Please enter your address", getActivity());
            return;
        }
        if (txtPassword.getText().toString().matches("")) {
            Tools.showToast("Please enter your password", getActivity());
            return;
        }
        StoreService storeService = new StoreService(getContext(), getActivity());
        Store store = new Store(
                single_choice_selected,
                txtStoreName.getText().toString(),
                txtPhoneNumber.getText().toString(),
                txtAddress.getText().toString(),
                txtMail.getText().toString(),
                txtPassword.getText().toString(),
                "STORE IMAGE"
        );
        storeService.addNewStore(store);

    }
}
