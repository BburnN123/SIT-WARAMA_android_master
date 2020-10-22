package com.sit.warama;

import com.sit.warama.account.*;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link InfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InfoFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private HomeFragment homeFragment;
    private Account account;

    public InfoFragment() {
        // Required empty public constructor
    }

    public InfoFragment(Context context) {

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment InfoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static InfoFragment newInstance(String param1, String param2) {
        InfoFragment fragment = new InfoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle("Info");
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_info, container, false);

        getActivity().setTitle("Info");

        account = new Account(getActivity().getApplicationContext());
        homeFragment = new HomeFragment();
        Button button = (Button) rootView.findViewById(R.id.savebutton);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ((MainActivity)getActivity()).updateInfo(rootView);
                Toast toast = Toast. makeText(getActivity().getApplicationContext(), "SAVED", Toast. LENGTH_SHORT);
                toast.show();

            }
        });
        ImageView cancel = (ImageView) rootView.findViewById(R.id.cancel_action);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 setFragment(homeFragment);
            }
        });
        set_info(rootView);

        return rootView;
    }

    protected void setFragment(Fragment fragment) {
        String tag = fragment.getClass().getSimpleName();

        assert getFragmentManager() != null;
        FragmentTransaction tr = getFragmentManager().beginTransaction();
        Fragment curFrag = getFragmentManager().getPrimaryNavigationFragment();
        Fragment cacheFrag = getFragmentManager().findFragmentByTag(tag);
        if (curFrag != null)
            tr.hide(curFrag);

        if (cacheFrag == null) {
            tr.add(R.id.main_frame, fragment, tag);
        } else {
            tr.show(cacheFrag);
            fragment = cacheFrag;
        }
        tr.setPrimaryNavigationFragment(fragment);
        tr.commit();
    }

    public void set_info( View rootView)
    {
        EditText editName = (EditText) rootView.findViewById(R.id.editTextPersonName);
        EditText editNumber = (EditText) rootView.findViewById(R.id.editTextMobileNumber);
        EditText editClinic = (EditText) rootView.findViewById(R.id.editTextClinic);

        switch(account.getReflex()){
            case "weak":
                RadioButton reflex_radio1 = (RadioButton) rootView.findViewById(R.id.firstWeak);
                reflex_radio1.setChecked(true);
                break;
            case "adequate":
                RadioButton reflex_radio2 = (RadioButton) rootView.findViewById(R.id.firstAdequate);
                reflex_radio2.setChecked(true);
                break;
            case "strong":
                RadioButton reflex_radio3 = (RadioButton) rootView.findViewById(R.id.firstStrong);
                reflex_radio3.setChecked(true);
                break;
        }

        switch(account.getVisual()){
            case "weak":
                RadioButton reflex_radio1 = (RadioButton) rootView.findViewById(R.id.secondWeak);
                reflex_radio1.setChecked(true);
                break;
            case "adequate":
                RadioButton reflex_radio2 = (RadioButton) rootView.findViewById(R.id.secondAdequate);
                reflex_radio2.setChecked(true);
                break;
            case "strong":
                RadioButton reflex_radio3 = (RadioButton) rootView.findViewById(R.id.secondStrong);
                reflex_radio3.setChecked(true);
                break;
        }

        switch(account.getHearing()){
            case "weak":
                RadioButton reflex_radio1 = (RadioButton) rootView.findViewById(R.id.thirdWeak);
                reflex_radio1.setChecked(true);
                break;
            case "adequate":
                RadioButton reflex_radio2 = (RadioButton) rootView.findViewById(R.id.thirdAdequate);
                reflex_radio2.setChecked(true);
                break;
            case "strong":
                RadioButton reflex_radio3 = (RadioButton) rootView.findViewById(R.id.thirdStrong);
                reflex_radio3.setChecked(true);
                break;
        }


        editName.setText(account.getName());
        editNumber.setText(account.getContact());
        editClinic.setText(account.getClinic());
    }
}