package com.logical.driverapp.Fragment;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.android.material.tabs.TabLayout;
import com.logical.driverapp.Adapter.TabsAdapter;
import com.logical.driverapp.R;
import com.logical.driverapp.Utils.CustomViewPager;

import static com.logical.driverapp.Activity.NavigationActivity.iv_drawer;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    LinearLayout homelinearlayout,location;
    ViewPager viewPager;
    TabLayout TabLayout;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

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
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View root = inflater.inflate(R.layout.fragment_home, container, false);
        homelinearlayout=root.findViewById(R.id.homelinearlayout);

        iv_drawer.setVisibility(View.GONE);

         final CustomViewPager viewPager =root.findViewById(R.id.viewpager);
        final TabLayout tabLayout = (TabLayout)root.findViewById(R.id.tablayout);
        tabLayout.addTab(tabLayout.newTab().setText("List"));
        tabLayout.addTab(tabLayout.newTab().setText("Map"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        TabsAdapter tabsAdapter =new TabsAdapter(getActivity().getSupportFragmentManager(),tabLayout.getTabCount());
        viewPager.setAdapter(tabsAdapter);
        viewPager.setPagingEnabled(false);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));


       tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
           @Override
           public void onTabSelected(com.google.android.material.tabs.TabLayout.Tab tab) {
               viewPager.setCurrentItem(tab.getPosition());
               int position = tab.getPosition();
               switch (position) {
                   case 0:

                       tabLayout.setSelectedTabIndicatorColor(Color.RED);
                       tabLayout.setTabTextColors(Color.parseColor("#111111"), Color.parseColor("#111111"));
                       /* tabLayout.setTabTextColors(ColorStateList.valueOf(Color.WHITE));*/
                       /* tabLayout.setTabTextColors(ColorStateList.valueOf(Color.parseColor("#fff")));*/
                       Log.e("Position", String.valueOf(0));
                       break;
                   case 1:
                       tabLayout.setSelectedTabIndicatorColor(Color.RED);
                       tabLayout.setTabTextColors(Color.parseColor("#111111"), Color.parseColor("#111111"));
                       /* tabLayout.setTabTextColors(ColorStateList.valueOf(Color.WHITE));*/
                       /* tabLayout.setTabTextColors(ColorStateList.valueOf(Color.parseColor("#fff")));*/
                       Log.e("Position", String.valueOf(1));
                       break;

                   default:
                       break;
               }
           }

           @Override
           public void onTabUnselected(com.google.android.material.tabs.TabLayout.Tab tab) {

           }

           @Override
           public void onTabReselected(com.google.android.material.tabs.TabLayout.Tab tab) {

           }
       });



   return root;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
