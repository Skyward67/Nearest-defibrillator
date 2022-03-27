package iut.android.tpfinal.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import iut.android.tpfinal.R;
import iut.android.tpfinal.objects.Defibrilator;

public class CustomAdapter extends RecyclerView.Adapter<CustomViewHolder> {

    private ArrayList<Defibrilator> defibrilators;

    public CustomAdapter(ArrayList<Defibrilator> defibrilators) {
        this.defibrilators = defibrilators;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_item, parent, false);

        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        Defibrilator defibrilator = defibrilators.get(position);

        holder.commune.setText(defibrilator.getCommune());
        holder.distance.setText(Double.toString(defibrilator.getDistance()).split("\\.")[0] + " meters");
        holder.indications.setText(defibrilator.getAcc() + ", " + defibrilator.getAcc_complt());
        holder.itemView.setTag(defibrilator);
    }

    @Override
    public int getItemCount() {
        return defibrilators.size();
    }
}
