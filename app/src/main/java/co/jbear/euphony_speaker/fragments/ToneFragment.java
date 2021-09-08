package co.jbear.euphony_speaker.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import co.euphony.tx.EuTxManager;
import co.jbear.euphony_speaker.R;

public class ToneFragment extends Fragment {

    double frequencyValue = 18000.0;
    TextView mFreqView, mFreqStatusView;
    Button mFreqDownBtn, mFreqUpBtn, mGenerateBtn, mCallShortBtn, mCallLongBtn;
    SeekBar mFrequencySeekBar, mFrequencyIntervalSeekBar;

    boolean isGenerateBtnClicked = false;

    EuTxManager txManager;

    public ToneFragment() {
        // Required empty public constructor
    }

    public static ToneFragment newInstance() {
        return new ToneFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        txManager = new EuTxManager(this.getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tone, container, false);

        mFreqView = v.findViewById(R.id.frequency_view);
        mFreqStatusView = v.findViewById(R.id.frequency_status);

        mFreqDownBtn = v.findViewById(R.id.tone_freq_down_btn);
        mFreqDownBtn.setOnClickListener(v1 -> setFrequency(getFrequency() - 1));

        mFreqUpBtn = v.findViewById(R.id.tone_freq_up_btn);
        mFreqUpBtn.setOnClickListener(v13 -> setFrequency(getFrequency() + 1));

        mCallShortBtn = v.findViewById(R.id.tone_call_short_btn);
        mCallShortBtn.setOnClickListener(v12 ->
                txManager.callEuPI(mFrequencySeekBar.getProgress(), EuTxManager.EuPIDuration.LENGTH_SHORT));

        mCallLongBtn = v.findViewById(R.id.tone_call_long_btn);
        mCallLongBtn.setOnClickListener(v14 ->
                txManager.callEuPI(mFrequencySeekBar.getProgress(), EuTxManager.EuPIDuration.LENGTH_LONG));

        mGenerateBtn = v.findViewById(R.id.tone_generator_btn);
        mGenerateBtn.setOnClickListener(v15 -> {
            if(isGenerateBtnClicked) {
                mGenerateBtn.setText("GENERATE");
                mCallShortBtn.setEnabled(true);
                mCallLongBtn.setEnabled(true);
                txManager.stop();
            }
            else {
                mGenerateBtn.setText("STOP");
                mCallShortBtn.setEnabled(false);
                mCallLongBtn.setEnabled(false);
                txManager.stop();
                txManager.callEuPI(frequencyValue, EuTxManager.EuPIDuration.LENGTH_FOREVER);
            }
            isGenerateBtnClicked ^= true;
        });

        mFrequencySeekBar = v.findViewById(R.id.frequencyBar);
        mFrequencySeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mFreqStatusView.setText("" + progress + "hz");
                frequencyValue = (double) progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if(isGenerateBtnClicked) {
                    txManager.stop();
                    txManager.callEuPI((double)seekBar.getProgress(), EuTxManager.EuPIDuration.LENGTH_FOREVER);

                    Log.d("EUPHONY_SPEAKER", "frequency : " + seekBar.getProgress());
                }
            }
        });

        mFreqStatusView.setText("" + mFrequencySeekBar.getProgress() + "hz");

        mFrequencyIntervalSeekBar = v.findViewById(R.id.frequencyIntervalBar);
        mFrequencyIntervalSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                double stepSize = 43.06640625;
                progress = (int) (((int) Math.round(progress/stepSize)) * stepSize - 21.5332);
                mFreqStatusView.setText("" + progress + "hz");
                frequencyValue = (double) progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if(isGenerateBtnClicked) {
                    txManager.stop();
                    txManager.callEuPI((double)seekBar.getProgress(), EuTxManager.EuPIDuration.LENGTH_FOREVER);
                    Log.d("EUPHONY_SPEAKER", "frequency : " + seekBar.getProgress());
                }
            }
        });

        return v;
    }

    private double getFrequency() {
        return mFrequencySeekBar.getProgress();
    }

    private void setFrequency(double freq) {

        if(mFrequencySeekBar != null)
            mFrequencySeekBar.setProgress((int)freq);

        if(mFreqStatusView != null)
            mFreqStatusView.setText("" + freq + "hz");

    }
}