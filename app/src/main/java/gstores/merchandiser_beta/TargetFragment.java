package gstores.merchandiser_beta;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import gstores.merchandiser_beta.components.AppLiterals;
import gstores.merchandiser_beta.components.Util;
import gstores.merchandiser_beta.components.helpers.PreferenceHelpers;
import gstores.merchandiser_beta.components.models.UserTargetDetailsView;
import gstores.merchandiser_beta.components.models.UserTargets;
import gstores.merchandiser_beta.web.IWebClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TargetFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TargetFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TargetFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private List<UserTargets> targets;
    private boolean loadComplete = false;

    public TargetFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TargetFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TargetFragment newInstance(String param1, String param2) {
        TargetFragment fragment = new TargetFragment();
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
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_target, container, false);
        // Inflate the layout for this fragment
        final String userName = Util.getToken(getContext()).user_name;
        final ProgressDialog progress = new ProgressDialog(getContext());
        progress.setMessage("Loading...");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        //Construct View

        String baseUrl = PreferenceHelpers.getPreference(
                PreferenceManager.getDefaultSharedPreferences(getContext()),
                getContext().getResources().getString(R.string.pref_head_office_key),
                getResources().getString(R.string.pref_head_office_default));
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create(
                        AppLiterals.APPLICATION_GSON_BUILDER));

        Retrofit retrofit = builder.build();

        IWebClient client = retrofit.create(IWebClient.class);
        Call<List<UserTargetDetailsView>> call = client.UserTarget(userName);
        try {
            call.enqueue(new Callback<List<UserTargetDetailsView>>() {
                @Override
                public void onResponse(Call<List<UserTargetDetailsView>> call, Response<List<UserTargetDetailsView>> response) {
                    List<UserTargetDetailsView> target = response.body();
                    loadComplete = true;
                    if (view instanceof RecyclerView) {
                        Context context = view.getContext();
                        RecyclerView recyclerView = (RecyclerView) view;
                        recyclerView.setLayoutManager(new LinearLayoutManager(context));
                        recyclerView.setAdapter(new TargetRecyclerViewAdapter(response.body()));
                    }
                    progress.dismiss();
                }

                @Override
                public void onFailure(Call<List<UserTargetDetailsView>> call, Throwable t) {
                    loadComplete = true;
                    progress.dismiss();
                    nothingToShow();
                }

                private void nothingToShow() {
                    try {
                        Snackbar snackbar = Util.SimpleSnackBar(view, "Nothing to show.", Snackbar.LENGTH_LONG);
                        snackbar.show();
                    }catch (Exception ex){ex.printStackTrace();}
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
        if(!loadComplete)
        progress.show();
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
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
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
