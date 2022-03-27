package iut.android.tpfinal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;

import iut.android.tpfinal.adapter.CustomAdapter;
import iut.android.tpfinal.objects.Defibrilator;

public class MainActivity extends AppCompatActivity {

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
        CustomAdapter adapter = new CustomAdapter(this.defibrilators);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter.notifyDataSetChanged();



    }
}