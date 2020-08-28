package co.jbear.euphony_speaker.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;

import co.jbear.euphony_speaker.R;
import euphony.lib.transmitter.EuDataEncoder;
import euphony.lib.transmitter.EuTxManager;

public class MessageFragment extends Fragment {


    Spinner mCountSpinner;
    Switch mLiveSwt;
    EditText mSpeakText;
    Button mSpeakBtn;

    EuTxManager mTxManager = new EuTxManager();
    EuDataEncoder mEuDataEncoder;
    int count = 1;
    boolean speakOn = false;

    public MessageFragment() {
        // Required empty public constructor
    }

    public static MessageFragment newInstance(String param1, String param2) {
        MessageFragment fragment = new MessageFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_message, container, false);

        mCountSpinner = v.findViewById(R.id.count_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this.getContext(),
                R.array.count_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mCountSpinner.setAdapter(adapter);
        mCountSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                count = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                count = -1;
            }
        });

        mLiveSwt = v.findViewById(R.id.live_swt);
        mSpeakText = v.findViewById(R.id.speakText);
        mSpeakBtn = v.findViewById(R.id.speakBtn);

        mSpeakBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (speakOn) {
                    mTxManager.stop();
                    mSpeakBtn.setText("Speak :)");
                    speakOn = false;
                } else {
                    mTxManager.setCode(mSpeakText.getText().toString()); // To generate acoustic data "Hello, Euphony" for 5 times.
                    mTxManager.process(count);
                    mSpeakBtn.setText("Stop :(");
                    speakOn = true;
                }
            }
        });

        return v;
    }
}