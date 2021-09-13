package co.jbear.euphony_speaker.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import co.euphony.tx.EuTxManager;
import co.jbear.euphony_speaker.R;

public class EuPIFragment extends Fragment {

    EuTxManager txManager = null;

    public EuPIFragment() {
        // Required empty public constructor
    }

    public static EuPIFragment newInstance() {
        return new EuPIFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        txManager = new EuTxManager(this.getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_eu_p_i, container, false);

        Button eupi18500Btn = v.findViewById(R.id.eupi18500);
        Button eupi19000Btn = v.findViewById(R.id.eupi19000);
        Button eupi20000Btn = v.findViewById(R.id.eupi20000);

        eupi18500Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txManager.callEuPI(18500, EuTxManager.EuPIDuration.LENGTH_LONG);
            }
        });

        eupi19000Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txManager.callEuPI(19000, EuTxManager.EuPIDuration.LENGTH_LONG);
            }
        });

        eupi20000Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txManager.callEuPI(20000, EuTxManager.EuPIDuration.LENGTH_LONG);
            }
        });

        return v;
    }
}