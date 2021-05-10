package gstores.merchandiser_beta;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.FileProvider;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageButton;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import gstores.merchandiser_beta.components.AppLiterals;
import gstores.merchandiser_beta.components.Util;
import gstores.merchandiser_beta.components.helpers.PreferenceHelpers;
import gstores.merchandiser_beta.components.models.homedelivery.DeliveryHeader;
import gstores.merchandiser_beta.components.models.homedelivery.DeliveryLine;
import gstores.merchandiser_beta.components.printutil.BluetoothUtil;
import gstores.merchandiser_beta.components.printutil.BytesUtil;
import gstores.merchandiser_beta.components.printutil.SunmiPrintHelper;
import gstores.merchandiser_beta.customviews.DeliveryLineAdaptor;
import gstores.merchandiser_beta.customviews.SaleConfigDialog;
import gstores.merchandiser_beta.customviews.SelectItemActivity;
import gstores.merchandiser_beta.dialog.RetailerDialog;
import gstores.merchandiser_beta.web.IWebClient;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static gstores.merchandiser_beta.components.AppLiterals.ExtraLabels.DELIVERY_HEADER;
import static gstores.merchandiser_beta.components.AppLiterals.ITEM;
import static gstores.merchandiser_beta.components.AppLiterals.RequestCodes.CUSTOMER_REQUEST;
import static gstores.merchandiser_beta.components.AppLiterals.RequestCodes.ITEM_REQUEST;
import static gstores.merchandiser_beta.components.AppLiterals.RequestCodes.REQUEST_IMAGE_CAPTURE;
import static gstores.merchandiser_beta.components.AppLiterals.RequestCodes.REQUEST_IMAGE_GALLERY;

public class AddSalesActivity extends AppCompatActivity implements  DatePickerDialog.OnDateSetListener,
DeliveryLineAdaptor.onDeleteItem
{
    private static final String SALE_DATE = "SALE_DATE";
    private static final String PREFERRED_DATE = "PREFERRED_DATE";
    private static final String TAG = "AddSalesActivity";
    private AppCompatButton buttonSalesType;
    private AppCompatButton buttonDeliveryType;
    private AppCompatButton buttonCustomer;
    private ListView listViewItems;
    private AppCompatButton buttonSaveSales;
    private FloatingActionButton buttonAddItem;

    private DeliveryHeader deliveryHeader;
    private String[] deliveryTypes;
    private String[] saleTypes;
    private AppCompatImageButton buttonSelectSaleDate;
    private AppCompatImageButton buttonSelectPreferredDeliveryDate;
    private String DateSelection;
    private Date toDay;
    private String deliveryType;
    private String saleType;
    private AppCompatImageButton buttonAttachPhoto;
    private AppCompatEditText editTextRemark;

    private String errorMessage ="";
    private ContentLoadingProgressBar mProgressView;
    private AppCompatImageButton buttonAttachGallery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_sales);
        /*Bundle extraction and View Model int*/
        Calendar calendar = Calendar.getInstance();
        this.toDay =
                new Date(calendar.get(Calendar.YEAR)-1900,
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)
                );

        deliveryTypes = getResources().getStringArray(R.array.delivery_types);
        saleTypes = getResources().getStringArray(R.array.sale_types);
        if (savedInstanceState != null) {
            this.deliveryHeader = (DeliveryHeader) savedInstanceState.getSerializable(DELIVERY_HEADER);
        }
        if (this.deliveryHeader == null) {
            deliveryHeader = new DeliveryHeader();
            deliveryHeader.setSaleType(saleTypes[0]);
            deliveryHeader.setDeliveryType(deliveryTypes[0]);

            if(this.deliveryHeader.SaleDate==null) {
                this.deliveryHeader.SaleDate =
                        new Date(calendar.get(Calendar.YEAR)-1900,
                                calendar.get(Calendar.MONTH),
                                calendar.get(Calendar.DAY_OF_MONTH)
                        );
            }
            calendar.add(Calendar.DATE, 1);
            if(this.deliveryHeader.dateMinPreferredDate==null) {
                this.deliveryHeader.dateMinPreferredDate =
                        new Date(calendar.get(Calendar.YEAR)-1900,
                                calendar.get(Calendar.MONTH),
                                calendar.get(Calendar.DAY_OF_MONTH)
                        );
            }
            int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
            calendar.add(Calendar.DATE,
                    dayOfWeek == Calendar.THURSDAY?2:1);
            this.deliveryHeader.datePreferredDate =
                    new Date(calendar.get(Calendar.YEAR)-1900,
                            calendar.get(Calendar.MONTH),
                            calendar.get(Calendar.DAY_OF_MONTH)
                    );
            if(this.deliveryHeader.ScheduledDate==null) {
                this.deliveryHeader.ScheduledDate =
                        new Date(calendar.get(Calendar.YEAR)-1900,
                                calendar.get(Calendar.MONTH),
                                calendar.get(Calendar.DAY_OF_MONTH)
                        );
            }
            this.deliveryHeader.UserName = Util.getToken(getApplicationContext()).user_name;
        }

        /*Setup views*/
        buttonSalesType = findViewById(R.id.buttonSalesType);
        buttonDeliveryType = findViewById(R.id.buttonDeliveryType);
        buttonCustomer = findViewById(R.id.buttonCustomer);
        listViewItems = findViewById(R.id.listViewItems);
        buttonSaveSales = findViewById(R.id.buttonSaveSales);
        buttonAddItem = findViewById(R.id.buttonAddItem);
        buttonSelectSaleDate = findViewById(R.id.buttonSelectSaleDate);
        buttonSelectPreferredDeliveryDate = findViewById(R.id.buttonSelectPreferedDeliveryDate);
        buttonAttachPhoto = findViewById(R.id.buttonAttachPhoto);
        buttonAttachGallery = findViewById(R.id.buttonAttachGallary);
        editTextRemark = findViewById(R.id.editTextRemark);
        mProgressView = findViewById(R.id.progressBar);
        /*Setup views events*/
        buttonSalesType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadSaleTypeSelector();
            }
        });
        buttonDeliveryType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadDeliveryTypeSelector();
            }
        });

        buttonSelectSaleDate.setOnClickListener(new View.OnClickListener() {

            @SuppressLint("NewApi")
            @Override
            public void onClick(View v) {
                AddSalesActivity.this.DateSelection = SALE_DATE;
                selectDate();
            }
        });

        buttonSelectPreferredDeliveryDate.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onClick(View v) {
                AddSalesActivity.this.DateSelection = PREFERRED_DATE;
                selectDate();
            }
        });

        buttonCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callCustomerEditor();
            }
        });

        buttonAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectItem();
            }
        });

        buttonAttachPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePhoto();
            }
        });

        buttonAttachGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_IMAGE_GALLERY);
            }
        });

        buttonSaveSales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deliveryHeader.Remarks = editTextRemark.getText().toString();
                if(deliveryHeader.Retailer==null && deliveryHeader.deliveryType == deliveryTypes[1]) {
                    selectRetailer();
                }
                else {
                    checkAndSave();
                }
            }
        });
    }

    private void checkAndSave() {

        boolean valid;
        try {
            valid = validToSave();
        } catch (Exception ex) {
            valid = false;
        }
        if (valid) {
            saveWithAttachment(deliveryHeader);
        } else {
            Snackbar snackbar =
                    Util.SimpleSnackBar(listViewItems, errorMessage, Snackbar.LENGTH_LONG);
            View view = snackbar.getView();
            view.setBackgroundColor(getResources().getColor(R.color.error_background));
            ((TextView) view.findViewById(android.support.design.R.id.snackbar_text)).setTextColor(
                    getResources().getColor(R.color.error_foreground)
            );
            snackbar.show();
        }
    }

    private void selectRetailer() {
        try {
            final RetailerDialog dialog = new RetailerDialog();
            dialog.setRetailer(this.deliveryHeader.Retailer);
            dialog.setRetailerDialogListner(new RetailerDialog.RetailerDialogListner() {
                @Override
                public void success(String selectedRetailer) {
                    AddSalesActivity.this.deliveryHeader.Retailer = selectedRetailer;
                    if (AddSalesActivity.this.deliveryHeader.Retailer != null)
                        checkAndSave();
                }
            });
            dialog.show(getSupportFragmentManager(),
                    getString(R.string.select_retailer));
        }catch (Exception ex){
            ex.printStackTrace();
            Snackbar snackbar =  Util.SimpleSnackBar(listViewItems,ex.getMessage(),Snackbar.LENGTH_LONG);
            View viewSanck = snackbar.getView();
            viewSanck.setBackgroundColor(getResources().getColor(R.color.error_background));
            ((TextView) viewSanck.findViewById(android.support.design.R.id.snackbar_text)).setTextColor(
                    getResources().getColor(R.color.error_foreground)
            );
            snackbar.show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        /*Setup View*/
        if (deliveryHeader != null) {
            setSaleTypesLabel();
            setDeliveryTypesLabel();
            setCustomerLabel();
            setListViewItemAdaptor();
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.i(TAG,"Restore");
        this.deliveryHeader = (DeliveryHeader)savedInstanceState.getSerializable(DELIVERY_HEADER);
        deliveryTypes = getResources().getStringArray(R.array.delivery_types);
        saleTypes = getResources().getStringArray(R.array.sale_types);
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        Log.i(TAG,"Save Instance.");
        outState.putSerializable(DELIVERY_HEADER, deliveryHeader);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i(TAG,"Save Instance.");
        outState.putSerializable(DELIVERY_HEADER, deliveryHeader);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Date selected = new Date(year - 1900, month, dayOfMonth);
        switch (this.DateSelection) {
            case SALE_DATE:
                if (!(selected.compareTo(toDay) >= 0)) {
                    AddSalesActivity.this.deliveryHeader.SaleDate = selected;
                    setSaleTypesLabel();
                } else {
                    Snackbar snackbar =  Util.SimpleSnackBar(listViewItems,"Wrong date.",Snackbar.LENGTH_LONG);
                    View viewSanck = snackbar.getView();
                    viewSanck.setBackgroundColor(getResources().getColor(R.color.error_background));
                    ((TextView) viewSanck.findViewById(android.support.design.R.id.snackbar_text)).setTextColor(
                            getResources().getColor(R.color.error_foreground)
                    );
                    snackbar.show();
                    selectDate();
                }
                break;
            case PREFERRED_DATE:
                if (!(selected.compareTo(this.deliveryHeader.dateMinPreferredDate) <= 0)) {
                    AddSalesActivity.this.deliveryHeader.ScheduledDate = selected;
                    setDeliveryTypesLabel();
                } else {
                    Snackbar snackbar =  Util.SimpleSnackBar(listViewItems,"Wrong date.",Snackbar.LENGTH_LONG);
                    View viewSanck = snackbar.getView();
                    viewSanck.setBackgroundColor(getResources().getColor(R.color.error_background));
                    ((TextView) viewSanck.findViewById(android.support.design.R.id.snackbar_text)).setTextColor(
                            getResources().getColor(R.color.error_foreground)
                    );
                    snackbar.show();
                    selectDate();
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == CUSTOMER_REQUEST) {
                DeliveryHeader temp = (DeliveryHeader) data.getSerializableExtra(DELIVERY_HEADER);
                AddSalesActivity.this.deliveryHeader.CustomerName = temp.CustomerName;
                AddSalesActivity.this.deliveryHeader.Phone = temp.Phone;
                AddSalesActivity.this.deliveryHeader.Receipt = temp.Receipt;
                AddSalesActivity.this.deliveryHeader.Emirate = temp.Emirate;
                AddSalesActivity.this.deliveryHeader.DeliveryAddress = temp.DeliveryAddress;
                setCustomerLabel();
            }
            else if (requestCode == ITEM_REQUEST) {
                DeliveryLine line = (DeliveryLine) data.getSerializableExtra(ITEM);
                if (this.deliveryHeader.DeliveryLines == null) {
                    this.deliveryHeader.DeliveryLines = new ArrayList<>();
                    setListViewItemAdaptor();
                }
                try {
                    this.deliveryHeader.DeliveryLines.add(line);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            else if (requestCode == REQUEST_IMAGE_CAPTURE){
                //Bundle extras = data.getExtras();
                //Bitmap imageBitmap = (Bitmap) extras.get("data");
                //this.deliveryHeader.setAttachment(imageBitmap);
            }else if (requestCode == REQUEST_IMAGE_GALLERY) {
                try {
                    Uri selectedImageUri = data.getData();
                    // Get the path from the Uri
                    final String filePath = getPathFromURI(selectedImageUri);
                    if (filePath != null) {
                        this.deliveryHeader.resetAttachment(filePath);
                    }
                }catch (Exception ex){
                    ex.printStackTrace();
                }
            }
        }
        else if (requestCode == REQUEST_IMAGE_CAPTURE){
            this.deliveryHeader.resetAttachment();
        }else if (requestCode == REQUEST_IMAGE_GALLERY) {
            try {
                Uri selectedImageUri = data.getData();
                // Get the path from the Uri
                final String filePath = getPathFromURI(selectedImageUri);
                if (filePath != null) {
                    this.deliveryHeader.resetAttachment(filePath);
                }
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
    }

    public String getPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }

    private void callCustomerEditor() {
        Intent intent = new Intent(getApplicationContext(),CustomerDetailsActivity.class);
        intent.putExtra(DELIVERY_HEADER,AddSalesActivity.this.deliveryHeader);
        startActivityForResult(intent, CUSTOMER_REQUEST);
    }

    private void selectItem() {
        Intent intent = new Intent(getApplicationContext(),SelectItemActivity.class);
        startActivityForResult(intent, ITEM_REQUEST);
    }

    private void setCustomerLabel() {
        this.buttonCustomer.setText(deliveryHeader.getCustomerLabel());
    }

    private void setListViewItemAdaptor() {
        if (this.deliveryHeader.DeliveryLines == null) {
            this.deliveryHeader.DeliveryLines = new ArrayList<>();
        }

        DeliveryLineAdaptor deliveryLineAdaptor = new DeliveryLineAdaptor(
                getApplicationContext(),
                R.layout.delivery_line_list_item,
                this.deliveryHeader.DeliveryLines);
        deliveryLineAdaptor.setOnDeleteItem(this);
        this.listViewItems.setAdapter(deliveryLineAdaptor);
    }

    private void loadSaleTypeSelector() {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(AddSalesActivity.this);
        int selection = Arrays.asList(AddSalesActivity.this.saleTypes).indexOf(
                AddSalesActivity.this.deliveryHeader.getSaleType()
        );
        if(AddSalesActivity.this.saleType==null) {
            AddSalesActivity.this.saleType = AddSalesActivity.this.deliveryHeader.getSaleType();
        }
        mBuilder.setTitle(R.string.sale_type);
        mBuilder.setSingleChoiceItems(
                saleTypes,
                selection,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AddSalesActivity.this.saleType = saleTypes[which];
                    }
                }
        );
        mBuilder.setCancelable(true);
        mBuilder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (AddSalesActivity.this.saleType.isEmpty()) {
                    AddSalesActivity.this.deliveryHeader.setSaleType(saleTypes[0]);
                }else {
                    AddSalesActivity.this.deliveryHeader.setSaleType(saleType);
                }
                setSaleTypesLabel();
            }
        });
        mBuilder.setNeutralButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }
        );
        AlertDialog mDialog = mBuilder.create();
        mDialog.show();
    }

    private void loadDeliveryTypeSelector() {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(AddSalesActivity.this);
        int selection = Arrays.asList(AddSalesActivity.this.deliveryTypes).indexOf(
                AddSalesActivity.this.deliveryHeader.getDeliveryType()
        );
        mBuilder.setTitle(R.string.delivery_method);
        mBuilder.setSingleChoiceItems(
                deliveryTypes,
                selection,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AddSalesActivity.this.deliveryType = deliveryTypes[which];
                    }
                }
        );
        mBuilder.setCancelable(true);
        mBuilder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                boolean shouldSelectDate =false;
                if(AddSalesActivity.this.deliveryType!=null)
                if (AddSalesActivity.this.deliveryType.isEmpty()) {
                    AddSalesActivity.this.deliveryHeader.setSaleType(deliveryTypes[0]);
                }else {
                    shouldSelectDate =AddSalesActivity.this.deliveryType!=AddSalesActivity.this.deliveryHeader.getDeliveryType()
                            && AddSalesActivity.this.deliveryType == deliveryTypes[1];
                    AddSalesActivity.this.deliveryHeader.setDeliveryType(AddSalesActivity.this.deliveryType);
                }
                setDeliveryTypesLabel();
                if(shouldSelectDate){
                    AddSalesActivity.this.DateSelection = PREFERRED_DATE;
                    AddSalesActivity.this.selectDate();
                }
            }
        });
        mBuilder.setNeutralButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }
        );
        AlertDialog mDialog = mBuilder.create();
        mDialog.show();
    }

    void setDeliveryTypesLabel() {
        this.buttonDeliveryType.setText(deliveryHeader.getDeliveryTypeLabel(deliveryTypes[1]));
    }

    void setSaleTypesLabel() {
        this.buttonSalesType.setText(deliveryHeader.getSaleTypeLabel());
    }

    private void selectDate() {
        try {
            DatePickerDialog dialog = null;
            try {
                switch (this.DateSelection) {
                    case SALE_DATE:
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(toDay);
                        calendar.add(Calendar.DATE, -30);
                        Date tempDate =
                                new Date(calendar.get(Calendar.YEAR) - 1900,
                                        calendar.get(Calendar.MONTH),
                                        calendar.get(Calendar.DAY_OF_MONTH)
                                );
                        dialog = new DatePickerDialog(AddSalesActivity.this,
                                AddSalesActivity.this,
                                AddSalesActivity.this.deliveryHeader.SaleDate.getYear(),
                                AddSalesActivity.this.deliveryHeader.SaleDate.getMonth(),
                                AddSalesActivity.this.deliveryHeader.SaleDate.getDate());
                        dialog.getDatePicker().setMinDate(tempDate.getTime());
                        dialog.getDatePicker().setMaxDate(this.toDay.getTime());
                        dialog.updateDate(AddSalesActivity.this.deliveryHeader.SaleDate.getYear() + 1900,
                                AddSalesActivity.this.deliveryHeader.SaleDate.getMonth(),
                                AddSalesActivity.this.deliveryHeader.SaleDate.getDate());
                        dialog.setTitle(R.string.select_sales_date);
                        break;
                    case PREFERRED_DATE:
                        dialog = new DatePickerDialog(AddSalesActivity.this,
                                AddSalesActivity.this,
                                AddSalesActivity.this.deliveryHeader.ScheduledDate.getYear(),
                                AddSalesActivity.this.deliveryHeader.ScheduledDate.getMonth(),
                                AddSalesActivity.this.deliveryHeader.ScheduledDate.getDate());
                        dialog.getDatePicker().setMinDate(
                                AddSalesActivity.this.deliveryHeader.datePreferredDate.getTime());
                        dialog.updateDate(
                                AddSalesActivity.this.deliveryHeader.ScheduledDate.getYear() + 1900,
                                AddSalesActivity.this.deliveryHeader.ScheduledDate.getMonth(),
                                AddSalesActivity.this.deliveryHeader.ScheduledDate.getDate());
                        dialog.setTitle(R.string.select_prefered_date);
                        break;
                }
            } catch (Exception ex) {
                Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
            }
            dialog.show();

        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG);
        }
    }

    private void takePhoto() {
        /*
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(takePictureIntent,REQUEST_IMAGE_CAPTURE);
            }*/
        try {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            // Ensure that there's a camera activity to handle the intent
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                // Create the File where the photo should go
                File photoFile = null;
                try {
                    photoFile = this.deliveryHeader.getAttachmentFile(getApplicationContext());
                } catch (Exception ex) {
                    // Error occurred while creating the File
                    ex.printStackTrace();
                }
                // Continue only if the File was successfully created
                if (photoFile != null) {
                    Uri photoURI = FileProvider.getUriForFile(this,
                            "gstores.merchandiser_beta.fileprovider",
                            photoFile);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
            }
        }catch (Exception ex) {
            ex.printStackTrace();
            Snackbar snackbar = Util.SimpleSnackBar(listViewItems,
                    "Error Launching camera." +
                            System.getProperty("line.separator") +
                            ex.getMessage()
                    ,
                    Snackbar.LENGTH_LONG);
            View view = snackbar.getView();
            view.setBackgroundColor(getResources().getColor(R.color.error_background));
            ((TextView) view.findViewById(android.support.design.R.id.snackbar_text)).setTextColor(
                    getResources().getColor(R.color.error_foreground)
            );
            snackbar.show();
        }
    }

    private void openSalesConfigDialog(int position) {
        final SaleConfigDialog dialog = new SaleConfigDialog();
        dialog.setDeliveryLine(this.deliveryHeader.DeliveryLines.get(position));
        dialog.setPosition(position);
        dialog.setOnSaleConfigDialogListener(new SaleConfigDialog.SaleConfigDialogListner() {
            @Override
            public void success(double quantity, double value) {
                AddSalesActivity.this.deliveryHeader.DeliveryLines.get(dialog.getPosition()).OrderQuantity = quantity;
                AddSalesActivity.this.deliveryHeader.DeliveryLines.get(dialog.getPosition()).SelleingPrice = value;
                setListViewItemAdaptor();
            }
        });
        dialog.show(getSupportFragmentManager(),"Item Select");
    }

    private void saveWithAttachment(final DeliveryHeader deliveryHeader) {
        if (deliveryHeader.deliveryType.equals(deliveryTypes[0])) {
            this.deliveryHeader.Status = 999;
            for (DeliveryLine lin : this.deliveryHeader.DeliveryLines) {
                lin.Status = 999;
            }
        } else {
            this.deliveryHeader.Status = 1;
            for (DeliveryLine lin : this.deliveryHeader.DeliveryLines) {
                lin.Status = 1;
            }
        }

        deliveryHeader.UserName = Util.getToken(getApplicationContext()).user_name;

        showProgress(true);
        if (deliveryHeader.Validate()) {
            String baseUrl = PreferenceHelpers.getPreference(
                    PreferenceManager.getDefaultSharedPreferences(getApplicationContext()),
                    getApplicationContext().getResources().getString(R.string.pref_head_office_key),
                    getResources().getString(R.string.pref_head_office_key));
            /*String baseUrl = PreferenceHelpers.getPreference(
                    PreferenceManager.getDefaultSharedPreferences(getApplicationContext()),
                    getApplicationContext().getResources().getString(R.string.pref_head_office_key),
                    getResources().getString(R.string.pref_head_office_default));*/

            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(1, TimeUnit.MINUTES)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(15, TimeUnit.SECONDS)
                    .build();

            Retrofit.Builder builder = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create(
                            AppLiterals.APPLICATION_GSON_BUILDER));

            Retrofit retrofit = builder.build();
            final IWebClient client = retrofit.create(IWebClient.class);
            try {
                String attachmentFile = null;
                attachmentFile = deliveryHeader.getAttachmentFilePath();
                if (attachmentFile != null) {
                    final MultipartBody.Part attachment = prepareFilePart(attachmentFile);
                    Call<String> callSaveAttachment = client.SaveAttachment(attachment);
                    callSaveAttachment.enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            if ((response.body() != null && !response.body().isEmpty()) ) {
                                deliveryHeader.attachmentName = response.body();
                                saveSales(client);
                            } else {
                                showProgress(false);
                                Snackbar snackbar = Util.SimpleSnackBar(listViewItems, "Error Saving attachment", Snackbar.LENGTH_LONG);
                                View view = snackbar.getView();
                                view.setBackgroundColor(getResources().getColor(R.color.error_background));
                                ((TextView) view.findViewById(android.support.design.R.id.snackbar_text)).setTextColor(
                                        getResources().getColor(R.color.error_foreground)
                                );
                                snackbar.show();
                            }
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            showProgress(false);
                            if (t != null)
                                t.printStackTrace();

                            Snackbar snackbar = Util.SimpleSnackBar(listViewItems, "Error updating sales.", Snackbar.LENGTH_LONG);
                            View view = snackbar.getView();
                            view.setBackgroundColor(getResources().getColor(R.color.error_background));
                            ((TextView) view.findViewById(android.support.design.R.id.snackbar_text)).setTextColor(
                                    getResources().getColor(R.color.error_foreground)
                            );
                            snackbar.show();
                        }
                    });
                } else if(deliveryHeader.deliveryType == deliveryTypes[0]){
                    deliveryHeader.attachmentName="";
                    saveSales(client);
                } else {
                    Snackbar snackbar = Util.SimpleSnackBar(this.listViewItems, "Please attach the receipt", Snackbar.LENGTH_LONG);
                    View view = snackbar.getView();
                    view.setBackgroundColor(getResources().getColor(R.color.error_background));
                    ((TextView) view.findViewById(android.support.design.R.id.snackbar_text)).setTextColor(
                            getResources().getColor(R.color.error_foreground)
                    );
                    snackbar.show();
                    showProgress(false);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                showProgress(false);
                Snackbar snackbar = Util.SimpleSnackBar(this.listViewItems, ex.getMessage(), Snackbar.LENGTH_LONG);
                View view = snackbar.getView();
                view.setBackgroundColor(getResources().getColor(R.color.error_background));
                ((TextView) view.findViewById(android.support.design.R.id.snackbar_text)).setTextColor(
                        getResources().getColor(R.color.error_foreground)
                );
                snackbar.show();
            }
        } else {
            showProgress(false);
            Snackbar snackbar = Util.SimpleSnackBar(this.listViewItems, "Invalid data", Snackbar.LENGTH_LONG);
            View view = snackbar.getView();
            view.setBackgroundColor(getResources().getColor(R.color.error_background));
            ((TextView) view.findViewById(android.support.design.R.id.snackbar_text)).setTextColor(
                    getResources().getColor(R.color.error_foreground)
            );
            snackbar.show();
        }
    }

    private void saveSales(IWebClient client){
        Call<String> callSaveSales = client.SaveSales(deliveryHeader);
        callSaveSales.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                showProgress(false);
                if (response.body() != null && !response.body().isEmpty()) {
                    AlertDialog dialog = new AlertDialog.Builder(AddSalesActivity.this)
                            .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent intent = new Intent();
                                            setResult(RESULT_OK,intent);
                                            AddSalesActivity.this.finish();
                                        }
                                    }
                            )
                            .setMessage("Order Number :" + response.body())
                            .setTitle("Order success.")
                            .setCancelable(false).create();
                    try{
                        String rv = deliveryHeader.getPrintableReceipt(response.body());

                        if (!BluetoothUtil.isBlueToothPrinter) {
                            SunmiPrintHelper.getInstance().printText(rv, 24, false, false);
                            SunmiPrintHelper.getInstance().feedPaper();
                        } else {
                        }
                    }catch (Exception ex){
                        ex.printStackTrace();
                    }
                    dialog.show();
                } else {
                    Snackbar snackbar = Util.SimpleSnackBar(listViewItems, "Error updating sales.", Snackbar.LENGTH_LONG);
                    View view = snackbar.getView();
                    view.setBackgroundColor(getResources().getColor(R.color.error_background));
                    ((TextView) view.findViewById(android.support.design.R.id.snackbar_text)).setTextColor(
                            getResources().getColor(R.color.error_foreground)
                    );
                    snackbar.show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                showProgress(false);
                if (t != null)
                    t.printStackTrace();

                Snackbar snackbar = Util.SimpleSnackBar(listViewItems, "Error updating sales.", Snackbar.LENGTH_LONG);
                View view = snackbar.getView();
                view.setBackgroundColor(getResources().getColor(R.color.error_background));
                ((TextView) view.findViewById(android.support.design.R.id.snackbar_text)).setTextColor(
                        getResources().getColor(R.color.error_foreground)
                );
                snackbar.show();
            }
        });
    }

    @NonNull
    private MultipartBody.Part prepareFilePart(String attachmentFile) {
        MediaType MEDIA_TYPE_JPG = MediaType.parse("image/jpg");
        /*Need to resize 1240 x 1754	*/
        File file =new File(attachmentFile);

        try {
            Bitmap original = BitmapFactory.decodeStream(getAssets().open("imagg1.jpg"));
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            original.compress(Bitmap.CompressFormat.JPEG, 100, out);
            Bitmap decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));
        }catch (Exception ex){
            ex.printStackTrace();
        }

        // create RequestBody instance from file
        RequestBody requestFile =
                RequestBody.create(
                        MEDIA_TYPE_JPG,
                        file
                );

        // MultipartBody.Part is used to send also the actual file name
        return MultipartBody.Part.createFormData(attachmentFile, file.getName(), requestFile);
    }

    private boolean validToSave() throws NullPointerException {
        boolean valid = true;
        errorMessage="";
        try {
            if(this.deliveryHeader.getDeliveryType() == deliveryTypes[1]) {
                if (this.deliveryHeader.CustomerName.isEmpty()) {
                    valid = false;
                    errorMessage += "Customer Name blank.";
                } else if (this.deliveryHeader.Phone.isEmpty()) {
                    valid = false;
                    errorMessage += (
                            (errorMessage.length()>0)?System.lineSeparator():""+
                                    "Phone number blank."
                    );
                } else if (this.deliveryHeader.Emirate.isEmpty()) {
                    valid = false;
                    errorMessage += (
                            (errorMessage.length()>0)?System.lineSeparator():""+
                                    "Please select emirate.");
                } else if (this.deliveryHeader.Remarks.isEmpty()) {
                    valid = false;
                    errorMessage += (
                            (errorMessage.length()>0)?System.lineSeparator():""+
                                    "Enter remark.");
                }
                else {
                    String temp = null;
                    try {
                        temp = this.deliveryHeader.getAttachmentFilePath();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    if (temp == null) {
                        valid = false;
                        errorMessage += (
                                (errorMessage.length() > 0) ? System.lineSeparator() : "" +
                                        "Please attach PO.");
                    }
                }
            }
            if(this.deliveryHeader.DeliveryLines.size()==0) {
                valid = false;
                errorMessage = "No items added.";
            }
        }catch (Exception ex){
            ex.printStackTrace();
            valid = false;
            errorMessage = "Please enter all the fields.";
            throw ex;
        }
        return  valid;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {

            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
            buttonSaveSales.setVisibility(show ? View.GONE : View.VISIBLE);
            buttonSaveSales.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    buttonSaveSales.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            buttonSaveSales.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    /*Method Implementations*/
    @Override
    public void deleteItem(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(AddSalesActivity.this );
        builder.setTitle(R.string.confirm_delete_item);
        builder.setMessage(deliveryHeader.DeliveryLines.get(position).Description
        +System.lineSeparator()
        +"Confirm delete?");
        builder.setCancelable(true);
        builder.setPositiveButton(R.string.yes,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deliveryHeader.DeliveryLines.remove(position);
                        setListViewItemAdaptor();
                    }
                });
        builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void editItem(int position) {
        openSalesConfigDialog(position);
    }
}
