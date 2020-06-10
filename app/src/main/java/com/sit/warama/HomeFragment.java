package com.sit.warama;

import android.app.Activity;
import android.icu.text.IDNA;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Button enterInfo;
    private InfoFragment infoFragment;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle("Home");
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);



        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        getActivity().setTitle("Home");

        infoFragment = new InfoFragment();
//        Fragment curFrag = getFragmentManager().getPrimaryNavigationFragment();
//        System.out.println("HOME FRAGMENT TAG!!"+ curFrag.getClass().getSimpleName());


        // Inflate the layout for this fragment
        enterInfo = (Button) rootView.findViewById(R.id.buttonEnterInfo);
        enterInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragment(infoFragment);
//                Activity activity = getActivity();
//                if (activity instanceof MainActivity){
//                    infoFragment = new InfoFragment();
//                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
//                    transaction.replace(R.id.infoFragment, infoFragment);
//                    transaction.commit();
//                }
////
//
//                    FragmentTransaction fr = getFragmentManager().beginTransaction();
//
//
//                    fr.add(R.id.main_frame, new InfoFragment());
//                    fr.commit();
            }
        });

        return rootView;

    }
//    public void showHideFragment(final Fragment fragment){
//
//        FragmentTransaction ft = getFragmentManager().beginTransaction();
//        ft.setCustomAnimations(android.R.animator.fade_in,
//                android.R.animator.fade_out);
//
//        if (fragment.isHidden()) {
//            ft.show(fragment);
//            Log.d("hidden","Show");
//        } else {
//            ft.hide(fragment);
//            Log.d("Shown","Hide");
//        }
//
//        ft.commit();
//    }

    protected void setFragment(Fragment fragment) {
        String tag = fragment.getClass().getSimpleName();

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
}