package gstores.merchandiser_beta;

import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import gstores.merchandiser_beta.components.Util;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BonusFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BonusFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private int mParam1;
    private String mParam2;

    public BonusFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param view Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BonusFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BonusFragment newInstance(int view, String param2) {
        BonusFragment fragment = new BonusFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, view);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getInt(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final  String baseUrl =
                Util.getSettingById(getContext(),R.string.pref_head_office_key);
        View view =inflater.inflate(R.layout.fragment_bonus, container, false);
        WebView browser = (WebView) view.findViewById(R.id.bonusWeb);
        final ContentLoadingProgressBar mProgressView = view.findViewById(R.id.progressBar);
        browser.getSettings().setDomStorageEnabled(true);
        browser.getSettings().setJavaScriptEnabled(true);
        mProgressView.setVisibility( View.VISIBLE );
        final FragmentActivity activity = getActivity();
        browser.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                mProgressView.setVisibility(newProgress!=100 ? View.VISIBLE : View.GONE);
            }
        });

        final String userName = Util.getToken(getContext()).user_name;
        String finalUrl = "";

        switch (mParam1){
            case R.id.bonus:
                finalUrl=baseUrl+"/Target/BonusStaffView/?StaffCode="+userName;
                break;
            case R.id.dashboard:
                finalUrl=baseUrl+"/Target/Dashboard/?StaffCode="+userName;
                break;
        }


        browser.loadUrl(finalUrl);
        // Inflate the layout for this fragment
        return view;
    }
}