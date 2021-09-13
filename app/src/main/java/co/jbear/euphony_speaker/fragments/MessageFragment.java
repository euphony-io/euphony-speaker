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

import co.euphony.tx.EuTxManager;
import co.jbear.euphony_speaker.R;

public class MessageFragment extends Fragment {

    Spinner mCountSpinner;
    Spinner mEngineSpinner;
    EditText mSpeakText;
    Button mSpeakBtn;

    EuTxManager mTxManager = null;
    int count = 1;
    EuTxManager.PlayerEngine selectedEngineId = EuTxManager.PlayerEngine.ANDROID_DEFAULT_ENGINE;
    boolean speakOn = false;

    public MessageFragment() {
        // Required empty public constructor
    }

    public static MessageFragment newInstance() {
        return new MessageFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTxManager = new EuTxManager(getContext());
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

        mEngineSpinner = v.findViewById(R.id.engine_spinner);
        ArrayAdapter<CharSequence> engineAdapter = ArrayAdapter.createFromResource(this.getContext(),
                R.array.player_engine_array, android.R.layout.simple_spinner_dropdown_item);
        mEngineSpinner.setAdapter(engineAdapter);
        mEngineSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i == 1)
                    selectedEngineId = EuTxManager.PlayerEngine.EUPHONY_NATIVE_ENGINE;
                else
                    selectedEngineId = EuTxManager.PlayerEngine.ANDROID_DEFAULT_ENGINE;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                selectedEngineId = EuTxManager.PlayerEngine.ANDROID_DEFAULT_ENGINE;
            }
        });


        mSpeakText = v.findViewById(R.id.speakText);
        mSpeakBtn = v.findViewById(R.id.speakBtn);

        mSpeakBtn.setOnClickListener(view -> {
            if (speakOn) {
                mTxManager.stop();
                mSpeakBtn.setText("Speak :)");
                speakOn = false;
            } else {
                mTxManager.setCode(mSpeakText.getText().toString()); // To generate acoustic data "Hello, Euphony" for 5 times.
                mTxManager.play(count, selectedEngineId);
                mSpeakBtn.setText("Stop :(");
                speakOn = true;
            }
        });

        return v;
    }
}