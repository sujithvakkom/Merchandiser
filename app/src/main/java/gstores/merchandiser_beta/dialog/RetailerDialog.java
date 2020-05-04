package gstores.merchandiser_beta.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import gstores.merchandiser_beta.R;
import gstores.merchandiser_beta.components.AppLiterals;
import gstores.merchandiser_beta.components.helpers.PreferenceHelpers;
import gstores.merchandiser_beta.web.IRetailerProvider;
import gstores.merchandiser_beta.web.IWebClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetailerDialog extends AppCompatDialogFragment {
    private static final String TAG = "RetailerDialog";
    private AppCompatEditText filterText;
    private ListView list;
    private ArrayAdapter<String> adapter;
    private List<String> retailers;
    private String selectedRetailer;
    private RetailerDialogListner onRetailerDialogListner;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder((getActivity()));

        LayoutInflater inflater =getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.retailer_dialog,null);

        filterText =  view.findViewById(R.id.EditBox);
        filterText.addTextChangedListener(filterTextWatcher);
        list = (ListView) view.findViewById(R.id.List);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                RetailerDialog.this.selectedRetailer =
                        RetailerDialog.this.retailers.get(position);
                ((AlertDialog)RetailerDialog.this.getDialog()).getButton(
                        AlertDialog.BUTTON_POSITIVE
                ).setEnabled(true);
                list.setItemChecked(position,true);
            }
        });
        builder.setView(view).setPositiveButton(R.string.ok,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(RetailerDialog.this.selectedRetailer!=null){
                            onRetailerDialogListner.success(
                                    RetailerDialog.this.selectedRetailer
                            );
                        }
                    }
                });

        AlertDialog dialog = builder.create();
        /*
        dialog.getButton(
                AlertDialog.BUTTON_POSITIVE).setEnabled(selectedRetailer != null);
                */
        return dialog;
    }

    @Override
    public void onStart() {
        super.onStart();
        final ProgressBar progressBar = this.getDialog().findViewById(R.id.progressBarRetailer);
        progressBar.setVisibility(View.VISIBLE);
        String baseUrl = PreferenceHelpers.getPreference(
                PreferenceManager.getDefaultSharedPreferences(getContext()),
                getContext().getResources().getString(R.string.pref_head_office_key),
                getResources().getString(R.string.pref_head_office_default));
        Retrofit.Builder builderWeb = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create(
                        AppLiterals.APPLICATION_GSON_BUILDER));

        Retrofit retrofit = builderWeb.build();

        IRetailerProvider client = retrofit.create(IRetailerProvider.class);
        Call<List<String>> call = client.GetRetailer();
        try {
            call.enqueue(new Callback<List<String>>() {
                @Override
                public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                    retailers = response.body();
                    adapter = new ArrayAdapter<String>(getContext(),
                            android.R.layout.simple_list_item_single_choice,
                            retailers);
                    list.setAdapter(adapter);
                    progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onFailure(Call<List<String>> call, Throwable t) {
                    progressBar.setVisibility(View.GONE);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "onCreateDialog: "+e.getMessage());
            progressBar.setVisibility(View.GONE);
        }
    }

    private TextWatcher filterTextWatcher = new TextWatcher() {

        public void afterTextChanged(Editable s) {
        }

        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
            adapter.getFilter().filter(s);
        }
    };

    public void setRetailerDialogListner(RetailerDialogListner onRetailerDialogListner) {
        this.onRetailerDialogListner = onRetailerDialogListner;
    }

    public void setRetailer(String retailer) {
        this.selectedRetailer=retailer;
    }

    public interface RetailerDialogListner{
        void success(String selectedRetailer);
    }
}
