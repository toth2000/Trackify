package hackku2021.trackify.bottomNavFragment;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import hackku2021.trackify.MapsActivity;
import hackku2021.trackify.R;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ImageViewHolder> {

    private Context mContext;
    private List<String> vehicleIdList;

    public HomeAdapter(Context context, List<String> project){
        mContext = context;
        vehicleIdList = project;
    }

    @NonNull
    @Override
    public HomeAdapter.ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vehicile_maintaince_item, parent, false);
        return new HomeAdapter.ImageViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final HomeAdapter.ImageViewHolder holder, final int position) {

         FirebaseDatabase.getInstance().getReference()
                .child("maint").child(vehicleIdList.get(position)).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        try{
                            String vehicleId = "Vehicle ID: ";
                            vehicleId += snapshot.child("vehicle_id").getValue().toString();
                            String year = "Year: " + snapshot.child("year").getValue().toString();
                            String month = "Month: " + snapshot.child("month").getValue().toString();

                            holder.textViewVehicleId.setText(vehicleId);
                            holder.textViewYear.setText(year);
                            holder.textViewMonth.setText(month);
                        }catch (Exception e)
                        {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


         holder.cardView.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Intent intent = new Intent(mContext, MapsActivity.class);
                 intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                 mContext.startActivity(intent);
             }
         });

    }



    @Override
    public int getItemCount() {
        return vehicleIdList.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {

        public CardView cardView;
        public TextView textViewVehicleId, textViewYear, textViewMonth;

        public ImageViewHolder(View itemView) {
            super(itemView);

            textViewVehicleId = itemView.findViewById(R.id.vehicleItemVehicleId);
            textViewYear = itemView.findViewById(R.id.vehicleItemYear);
            textViewMonth = itemView.findViewById(R.id.vehicleItemVehicleMonth);
            cardView = itemView.findViewById(R.id.vehicleItemCardView);
        }
    }

}