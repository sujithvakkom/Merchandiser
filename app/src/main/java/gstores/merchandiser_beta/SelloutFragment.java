package gstores.merchandiser_beta;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import gstores.merchandiser_beta.components.AppLiterals;
import gstores.merchandiser_beta.components.Util;
import gstores.merchandiser_beta.components.helpers.PreferenceHelpers;
import gstores.merchandiser_beta.components.models.homedelivery.DeliveryHeader;
import gstores.merchandiser_beta.web.IWebClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.net.SocketTimeoutException;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class SelloutFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    private boolean loadComplete = false;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public SelloutFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static SelloutFragment newInstance(int columnCount) {
        SelloutFragment fragment = new SelloutFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_item_list, container, false);
        final String userName = Util.getToken(getContext()).user_name;
        final ProgressDialog progress = new ProgressDialog(getContext());
        progress.setMessage("Loading...");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        String baseUrl =
                Util.getSettingById(getContext(),R.string.pref_head_office_key);
        /*String baseUrl = PreferenceHelpers.getPreference(
                PreferenceManager.getDefaultSharedPreferences(getContext()),
                getContext().getResources().getString(R.string.pref_head_office_key),
                getResources().getString(R.string.pref_head_office_default));*/
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create(
                        AppLiterals.APPLICATION_GSON_BUILDER));

        Retrofit retrofit = builder.build();

        IWebClient client = retrofit.create(IWebClient.class);
        List<DeliveryHeader> orders = null;
        Call<List<DeliveryHeader>> call = client.GetMobileOrders(
                Util.getToken(getContext()).user_name
                , AppLiterals.MOB);
        try {
            call.enqueue(new Callback<List<DeliveryHeader>>() {
                @Override
                public void onResponse(Call<List<DeliveryHeader>> call, Response<List<DeliveryHeader>> response) {

                    if (view instanceof RecyclerView) {
                        Context context = view.getContext();
                        RecyclerView recyclerView = (RecyclerView) view;
                        if (mColumnCount <= 1) {
                            recyclerView.setLayoutManager(new LinearLayoutManager(context));
                        } else {
                            recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
                        }
                        recyclerView.setAdapter(new SelloutRecyclerViewAdapter(response.body()
                                , mListener));
                        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),1);
                        recyclerView.addItemDecoration(mDividerItemDecoration);
                    }
                    progress.dismiss();
                    loadComplete = true;
                    if(response.body()==null){
                        nothingToShow();
                    }else
                    if(response.body().size()==0){
                        nothingToShow();
                    }
                }

                private void nothingToShow() {
                    try {
                        loadComplete = true;
                        Snackbar snackbar= Util.SimpleSnackBar(view, "Nothing to show.", Snackbar.LENGTH_LONG);
                        snackbar.show();
                    }catch (Exception ex){ex.printStackTrace();}
                }

                @Override
                public void onFailure(Call<List<DeliveryHeader>> call, Throwable t) {
                    progress.dismiss();
                    loadComplete = true;
                    try {
                        throw t;
                    }
                    catch (SocketTimeoutException ex){
                        Snackbar snackbar = Util.SimpleSnackBar(view,
                                "Connection timeout",
                                Snackbar.LENGTH_LONG);
                        View view = snackbar.getView();
                        view.setBackgroundColor(getResources().getColor(R.color.error_background));
                        ((TextView) view.findViewById(android.support.design.R.id.snackbar_text)).setTextColor(
                                getResources().getColor(R.color.error_foreground)
                        );
                        snackbar.show();
                    }
                    catch (Exception ex) {
                        Snackbar snackbar = Util.SimpleSnackBar(view,
                                ex.getLocalizedMessage(),
                                Snackbar.LENGTH_LONG);
                        View view = snackbar.getView();
                        view.setBackgroundColor(getResources().getColor(R.color.error_background));
                        ((TextView) view.findViewById(android.support.design.R.id.snackbar_text)).setTextColor(
                                getResources().getColor(R.color.error_foreground)
                        );
                        snackbar.show();
                        ex.printStackTrace();
                    } catch (Throwable throwable) {
                        Snackbar snackbar = Util.SimpleSnackBar(view,
                                "Unknown Error occurred",
                                Snackbar.LENGTH_LONG);
                        View view = snackbar.getView();
                        view.setBackgroundColor(getResources().getColor(R.color.error_background));
                        ((TextView) view.findViewById(android.support.design.R.id.snackbar_text)).setTextColor(
                                getResources().getColor(R.color.error_foreground)
                        );
                        snackbar.show();
                        throwable.printStackTrace();
                    }
                }
            });
        } catch (Exception ex) {
            progress.dismiss();
            loadComplete = true;
            ex.printStackTrace();
            return null;
        }
        if(!loadComplete)
        progress.show();
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(DeliveryHeader item);
    }
}
