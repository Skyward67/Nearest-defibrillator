package iut.android.tpfinal.adapter;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import iut.android.tpfinal.R;
import iut.android.tpfinal.objects.Defibrilator;

public class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView commune;
    public TextView distance;
    public TextView indications;
    CustomAdapter.RecyclerViewOnClickListener listener;

    public CustomViewHolder(View view, CustomAdapter.RecyclerViewOnClickListener listener) {
        super(view);
        this.listener = listener;
        commune = (TextView) view.findViewById(R.id.item_list_commune);
        distance = (TextView) view.findViewById(R.id.item_list_distance);
        indications = (TextView) view.findViewById(R.id.item_list_indications);

        view.setOnClickListener(this);
    }

    public void updateWithItem(Defibrilator defibrilator){
        this.commune.setText(defibrilator.getCommune());
    }

    @Override
    public void onClick(View view) {
        listener.onClick(getAdapterPosition());
    }
}
