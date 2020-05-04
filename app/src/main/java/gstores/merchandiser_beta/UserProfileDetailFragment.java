package gstores.merchandiser_beta;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;

import gstores.merchandiser2.components.models.homedelivery.Brand;
import gstores.merchandiser_beta.components.Util;
import gstores.merchandiser_beta.components.models.ItemModel;
import gstores.merchandiser_beta.components.viewhelpers.ItemModelArrayAdapter;
import gstores.merchandiser_beta.components.web.BusinessExcelService;
import gstores.merchandiser_beta.customviews.SingleRecyclerAdaptor;
import gstores.merchandiser_beta.dummy.DummyContent;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A fragment representing a single UserProfile detail screen.
 * This fragment is either contained in a {@link UserProfileListActivity}
 * in two-pane mode (on tablets) or a {@link UserProfileDetailActivity}
 * on handsets.
 */
public class UserProfileDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    /**
     * The dummy content this fragment is presenting.
     */
    private String mItem;
    private RecyclerView brandsRecyclerView;
    private List<Brand> brands;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public UserProfileDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            mItem = (String) getArguments().getString(ARG_ITEM_ID);

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(mItem);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = null;
        switch (mItem) {
            case "Brands":
                final ProgressDialog progress = new ProgressDialog(getContext());
                progress.setMessage("Loading...");
                progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progress.show();
                Thread thread = new Thread(
                        new Runnable() {
                            @Override
                            public void run() {
                                String baseUrl = getContext().getResources().getString(
                                        R.string.pref_head_office_default
                                );
                                Retrofit.Builder builder = new Retrofit.Builder()
                                        .baseUrl(baseUrl)
                                        .addConverterFactory(GsonConverterFactory.create());

                                Retrofit retrofit = builder.build();

                                BusinessExcelService client = retrofit.create(BusinessExcelService.class);

                                Call<List<Brand>> call = client.AllBrands();
                                //dialog.show();
                                try {
                                    brands = call.execute().body();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                progress.dismiss();
                            }
                        });
                thread.start();
                try {
                    thread.join();
                }catch (Exception ex){
                    ex.printStackTrace();
                }
                rootView = inflater.inflate(R.layout.userprofile_brands_detail, container, false);
                brandsRecyclerView = (RecyclerView) rootView.findViewById(R.id.brand_list);
                brandsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                final SingleRecyclerAdaptor adaptor = new SingleRecyclerAdaptor(brands);
                AppCompatEditText filter = (AppCompatEditText) rootView.findViewById(R.id.brand_list_filter);
                filter.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        adaptor.getFilter().filter(s.toString());
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
                brandsRecyclerView.setAdapter(adaptor);
                return rootView;
        }
        rootView = inflater.inflate(R.layout.userprofile_detail, container, false);

        // Show the dummy content as text in a TextView.
        if (mItem != null) {
            switch (mItem) {
                case "Brands":
                    brandsRecyclerView = (RecyclerView) rootView.findViewById(R.id.brand_list);
                    //final ProgressDialog dialog = new ProgressDialog(context);
                    //dialog.setMessage("Loading brands.");
                    assert brandsRecyclerView != null;
                    break;
                default:
                    ((TextView) rootView.findViewById(R.id.userprofile_detail)).setText(mItem);
            }
        }
        return rootView;
    }

    private void setBrands(Context context) {
    }

}