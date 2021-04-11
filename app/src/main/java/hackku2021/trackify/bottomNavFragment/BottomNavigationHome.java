package hackku2021.trackify.bottomNavFragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import hackku2021.trackify.ArActivity;
import hackku2021.trackify.R;
import hackku2021.trackify.qrScannerActivity;

public class BottomNavigationHome extends Fragment {

    private NestedScrollView nestedScrollView;
    private RecyclerView recyclerView;
    private List<String> vehicleList;
    private Stack<String> vehicleStack;
    private LinearLayoutManager layoutManager;
    private HomeAdapter homeAdapter;
    private DatabaseReference reference;
    private int loadLimit = 50;
    private ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_navigation_home, container, false);

        nestedScrollView = view.findViewById(R.id.bottom_nav_home_nestedScroll);
        recyclerView = view.findViewById(R.id.bottom_nav_home_recyclerView);
        progressBar = view.findViewById(R.id.home_nav_progress_bar);

        view.findViewById(R.id.qr_scan_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplicationContext(), qrScannerActivity.class);
                getActivity().startActivity(intent);
            }
        });

        view.findViewById(R.id.ar_camera_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplicationContext(), ArActivity.class);
                getActivity().startActivity(intent);
            }
        });

        reference = FirebaseDatabase.getInstance().getReference().child("maint");

        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        //    layoutManager.setReverseLayout(true);
        //    layoutManager.setStackFromEnd(true);
        layoutManager.setSmoothScrollbarEnabled(true);
        recyclerView.setLayoutManager(layoutManager);
        vehicleList = new ArrayList<>();
        vehicleStack = new Stack<>();
        homeAdapter = new HomeAdapter(getActivity().getApplicationContext(), vehicleList);
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setAdapter(homeAdapter);
        //initScrollListener();
        setNestedScrollView();
        getVehicle();


        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
     //   button.setOnClickListener(new View.OnClickListener() {
            /*
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplicationContext(), PostUploadActivity.class);
                startActivity(intent);
            }
        });
             */
    }

    private void getVehicle(){

        reference.limitToLast(loadLimit).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try{
                    vehicleList.clear();
                    vehicleStack.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String key = snapshot.getKey();
                        vehicleStack.push(key);
                    }
                    addToListFromStack();
                    homeAdapter.notifyDataSetChanged();
                    progressBar.setVisibility(View.GONE);
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                progressBar.setVisibility(View.GONE);
            }

        });

    }

    private void setNestedScrollView()
    {
        nestedScrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                View view = (View) nestedScrollView.getChildAt(nestedScrollView.getChildCount() - 1);

                int diff = (view.getBottom() - (nestedScrollView.getHeight() + nestedScrollView
                        .getScrollY()));

                if (diff == 0) {
                    progressBar.setVisibility(View.VISIBLE);
                    loadMoreVehicle();
                }
            }
        });
    }

    private void loadMoreVehicle()
    {
        if(vehicleList.size()<1)
            return;

        vehicleStack.clear();

        reference.orderByKey()//.startAfter(projectClassList.get(0).getProjectId())
                .endBefore(vehicleList.get(vehicleList.size() - 1))
                .limitToLast(loadLimit).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try{
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String key = snapshot.getKey();
                        vehicleStack.push(key);
                    }
                    addToListFromStack();
                    homeAdapter.notifyDataSetChanged();
                    progressBar.setVisibility(View.GONE);
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                progressBar.setVisibility(View.GONE);
            }

        });

    }

    private void addToListFromStack()
    {
        while (!vehicleStack.isEmpty())
        {
            vehicleList.add(vehicleStack.pop());
        }
    }


}
