package iut.android.tpfinal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;

import iut.android.tpfinal.adapter.CustomAdapter;
import iut.android.tpfinal.objects.Defibrilator;

public class MainActivity extends AppCompatActivity implements CustomAdapter.RecyclerViewOnClickListener {

    private ArrayList<Defibrilator> defibrilators;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        defibrilators = (ArrayList<Defibrilator>) getIntent().getSerializableExtra("list");
        for (Defibrilator defibrilator: defibrilators) {
            Log.d("test main:", defibrilator.toString());
        }

        RecyclerView recyclerView = findViewById(R.id.RecyclerView);
        CustomAdapter adapter = new CustomAdapter(this.defibrilators, this);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter.notifyDataSetChanged();


    }

    @Override
    public void onClick(int position) {
        Intent intent = new Intent(MainActivity.this, MapsActivity.class);
        intent.putExtra("defibrilator", defibrilators.get(position));
        startActivity(intent);
    }
}