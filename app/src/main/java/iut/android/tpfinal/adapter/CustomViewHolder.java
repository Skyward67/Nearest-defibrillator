package iut.android.tpfinal.adapter;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import iut.android.tpfinal.R;
import iut.android.tpfinal.objects.Defibrilator;

public class CustomViewHolder extends RecyclerView.ViewHolder {
    public TextView commune;
    public TextView distance;
    public TextView indications;

    public CustomViewHolder(View view) {
        super(view);
        commune = (TextView) view.findViewById(R.id.item_list_commune);
        distance = (TextView) view.findViewById(R.id.item_list_distance);
        indications = (TextView) view.findViewById(R.id.item_list_indications);
    }

    public void updateWithItem(Defibrilator defibrilator){
        this.commune.setText(defibrilator.getCommune());
    }
}
