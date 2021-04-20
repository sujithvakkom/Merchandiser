package gstores.merchandiser_beta;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.annotation.RequiresPermission;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

import gstores.merchandiser_beta.components.Util;
import gstores.merchandiser_beta.components.helpers.LocationHelper;
import gstores.merchandiser_beta.components.models.UserLocation;
import gstores.merchandiser_beta.components.web.BusinessExcelService;
import gstores.merchandiser_beta.localdata.models.LoginToken;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CheckinFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CheckinFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Button checkinButton;
    private Button checkoutButton;
    private View view;
    private AppCompatTextView checkinStauts;
    private AppCompatTextView checkinAddress;
    private Button breakInButton;
    private Button breakOutButton;
    private ContentLoadingProgressBar progressBar;
    private LinearLayout checkinControls;

    private static final int PERMISSION_CALLBACK_CONSTANT = 101;
    private static final int REQUEST_PERMISSION_SETTING = 102;

    public CheckinFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CheckinFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CheckinFragment newInstance(String param1, String param2) {
        CheckinFragment fragment = new CheckinFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_checkin, container, false);
        // Inflate the layout for this fragment
        checkinButton = view.findViewById(R.id.buttonCheckin);
        checkoutButton = view.findViewById(R.id.buttonCheckout);
        breakInButton = view.findViewById(R.id.buttonBreakIn);
        breakOutButton = view.findViewById(R.id.buttonBreakOut);
        checkinStauts = view.findViewById(R.id.checkinStauts);
        checkinAddress = view.findViewById(R.id.checkinAddress);
        progressBar = view.findViewById(R.id.progressBar);
        checkinControls = view.findViewById(R.id.checkinControls);

        checkinStauts.setCompoundDrawables(null, null, null, null);
        checkinAddress.setCompoundDrawables(null, null, null, null);

        checkinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatePunching(1);
            }
        });
        checkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatePunching(0);
            }
        });
        breakInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatePunching(2);
            }
        });
        breakOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatePunching(3);
            }
        });

        try {
            getCurrentStatus();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return view;
    }

    private void getCurrentStatus() {
        final String baseUrl = getContext().getResources().getString(
                R.string.business_excel_end
        );

        final LoginToken token = Util.getToken(getActivity().getApplicationContext());

        checkinControls.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit = builder.build();

        BusinessExcelService client = retrofit.create(BusinessExcelService.class);

        Call<UserLocation> call = client.getUserLocation(token.user_name);

        call.enqueue(new Callback<UserLocation>() {
            @Override
            public void onResponse(Call<UserLocation> call, Response<UserLocation> response) {
                if (response.body() != null) {
                    try {
                        updateStatus(response.body().Type, response.body().Address);
                    } catch (Exception ex) {
                        checkinButton.setEnabled(true);
                        checkoutButton.setEnabled(false);
                        breakInButton.setEnabled(false);
                        breakOutButton.setEnabled(false);
                        ex.printStackTrace();
                    }
                    checkinControls.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<UserLocation> call, Throwable t) {
                try {
                    updateStatus(-1, "");
                } catch (Exception ex) {
                    checkinButton.setEnabled(true);
                    checkoutButton.setEnabled(false);
                    breakInButton.setEnabled(false);
                    breakOutButton.setEnabled(false);
                    ex.printStackTrace();
                }
                checkinControls.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void updateStatus(int type, String address) {
        switch (type) {
            case 0:
                checkinStauts.setText("Shift ended");
                checkinAddress.setText(address == null ? "Cannot find location" : address);
                checkinStauts.setTextColor(getResources().getColor(R.color.error_background));
                checkinAddress.setTextColor(getResources().getColor(R.color.error_background));
                checkinStauts.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_shift_end
                        , 0, 0, 0);
                checkinAddress.setCompoundDrawablesWithIntrinsicBounds(R.drawable.mapbox_marker_icon_default
                        , 0, 0, 0);

                checkinButton.setEnabled(true);
                checkoutButton.setEnabled(false);
                breakInButton.setEnabled(false);
                breakOutButton.setEnabled(false);

                break;
            case 1:
                checkinStauts.setText("Shift started");
                checkinAddress.setText((address == null ? "" : address));
                checkinStauts.setTextColor(getResources().getColor(R.color.success_background));
                checkinAddress.setTextColor(getResources().getColor(R.color.success_background));
                checkinStauts.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_shift_start
                        , 0, 0, 0);
                checkinAddress.setCompoundDrawablesWithIntrinsicBounds(R.drawable.mapbox_marker_icon_default
                        , 0, 0, 0);
                checkinButton.setEnabled(false);
                checkoutButton.setEnabled(true);
                breakInButton.setEnabled(true);
                breakOutButton.setEnabled(false);
                break;
            case 2:
                checkinStauts.setText("Break started");
                checkinAddress.setText((address == null ? "" : address));
                checkinStauts.setTextColor(getResources().getColor(android.R.color.holo_orange_dark));
                checkinAddress.setTextColor(getResources().getColor(android.R.color.holo_orange_dark));
                checkinStauts.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_break_out
                        , 0, 0, 0);
                checkinAddress.setCompoundDrawablesWithIntrinsicBounds(
                        getResources().getDrawable(R.drawable.mapbox_marker_icon_default)
                        , null, null, null);
                checkinButton.setEnabled(false);
                checkoutButton.setEnabled(false);
                breakInButton.setEnabled(false);
                breakOutButton.setEnabled(true);
                break;
            case 3:
                checkinStauts.setText("Break ended");
                checkinAddress.setText((address == null ? "" : address));
                checkinStauts.setTextColor(getResources().getColor(android.R.color.holo_orange_dark));
                checkinAddress.setTextColor(getResources().getColor(android.R.color.holo_orange_dark));
                checkinStauts.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_break_end
                        , 0, 0, 0);
                checkinAddress.setCompoundDrawablesWithIntrinsicBounds(R.drawable.mapbox_marker_icon_default
                        , 0, 0, 0);
                checkinButton.setEnabled(false);
                checkoutButton.setEnabled(true);
                breakInButton.setEnabled(true);
                breakOutButton.setEnabled(false);
                break;
            default:
                checkinStauts.setText("Cannot find last status");
                checkinAddress.setText((address == null ? "" : address));
                checkinStauts.setTextColor(getResources().getColor(android.R.color.holo_orange_dark));
                checkinAddress.setTextColor(getResources().getColor(android.R.color.holo_orange_dark));
                checkinStauts.setCompoundDrawablesWithIntrinsicBounds(0
                        , 0, 0, 0);
                checkinAddress.setCompoundDrawablesWithIntrinsicBounds(0
                        , 0, 0, 0);
        }
    }

    private void updatePunching(Integer i) {
        //Checking Permission
        SharedPreferences permissionStatus = getActivity().getSharedPreferences("permissionStatus", getActivity().MODE_PRIVATE);
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)) {
                //Show Information about why you need the permission
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(getString(R.string.grant_permission));
                builder.setMessage(getString(R.string.permission_camera));
                builder.setPositiveButton(R.string.grant, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_CALLBACK_CONSTANT);
                    }
                });
                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            } else if (permissionStatus.getBoolean(Manifest.permission.ACCESS_FINE_LOCATION, false)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(getString(R.string.grant_permission));
                builder.setMessage(getString(R.string.permission_location));
                builder.setPositiveButton(R.string.grant, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
                        intent.setData(uri);
                        startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
                    }
                });
                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            } else {
                //just request the permission
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_CALLBACK_CONSTANT);
            }


            SharedPreferences.Editor editor = permissionStatus.edit();
            editor.putBoolean(Manifest.permission.ACCESS_FINE_LOCATION, true);
            editor.apply();
        } else {

            //You already have the permission, just go ahead.

            //LoginToken user = Util.getToken(getActivity().getApplicationContext());
            final String baseUrl = getContext().getResources().getString(
                    R.string.business_excel_end
            );
            final Integer type = i;

            checkinControls.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
            LocationHelper locationHelper = new LocationHelper(getActivity().getApplicationContext());
            if (locationHelper.isEnabled()) {
                locationHelper.getLastKnownLocation(new LocationHelper.onLocationHelperSuccessListener() {
                    @Override
                    public void lastKnownLocation(Location location) {
                        Double longitude = location.getLongitude();
                        Double lattitude = location.getLatitude();
                        LoginToken token = Util.getToken(getActivity().getApplicationContext());

                        final UserLocation userLocation = new UserLocation(token.user_name, lattitude, longitude, type);

                        Geocoder geocoder;
                        List<Address> addresses;
                        geocoder = new Geocoder(getActivity().getApplicationContext(), Locale.getDefault());
                        try {
                            addresses = geocoder.getFromLocation(lattitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                            userLocation.Address = addresses.get(0).getAddressLine(0);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            ;
                        }

                        Retrofit.Builder builder = new Retrofit.Builder()
                                .baseUrl(baseUrl)
                                .addConverterFactory(GsonConverterFactory.create());

                        Retrofit retrofit = builder.build();

                        BusinessExcelService client = retrofit.create(BusinessExcelService.class);

                        Call<UserLocation> call = client.postUserLocation(userLocation);
                        call.enqueue(new Callback<UserLocation>() {
                            @Override
                            public void onResponse(Call<UserLocation> call, Response<UserLocation> response) {
                                Snackbar snackbar =
                                        Util.SimpleSnackBar(view, "Success", Snackbar.LENGTH_LONG);
                                View view = snackbar.getView();
                                view.setBackgroundColor(getResources().getColor(R.color.success_background));
                                ((TextView) view.findViewById(android.support.design.R.id.snackbar_text)).setTextColor(
                                        getResources().getColor(R.color.success_foreground)
                                );

                                getCurrentStatus();
                                checkinControls.setVisibility(View.VISIBLE);
                                progressBar.setVisibility(View.GONE);
                                snackbar.show();
                            }

                            @Override
                            public void onFailure(Call<UserLocation> call, Throwable t) {
                                Snackbar snackbar =
                                        Util.SimpleSnackBar(view, t.getLocalizedMessage(), Snackbar.LENGTH_LONG);
                                View view = snackbar.getView();
                                view.setBackgroundColor(getResources().getColor(R.color.error_background));
                                ((TextView) view.findViewById(android.support.design.R.id.snackbar_text)).setTextColor(
                                        getResources().getColor(R.color.error_foreground)
                                );
                                checkinControls.setVisibility(View.VISIBLE);
                                progressBar.setVisibility(View.GONE);
                                snackbar.show();
                            }
                        });
                    }
                });
            } else {
                Snackbar snackbar =
                        Util.SimpleSnackBar(view, "Location is disabled", Snackbar.LENGTH_LONG);
                View view = snackbar.getView();
                view.setBackgroundColor(getResources().getColor(R.color.error_background));
                ((TextView) view.findViewById(android.support.design.R.id.snackbar_text)).setTextColor(
                        getResources().getColor(R.color.error_foreground)
                );
                checkinControls.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                checkinControls.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                snackbar.show();
            }
        }
    }
}