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

import co.jbear.euphony_speaker.R;

import euphony.lib.transmitter.EuphonyTx;

public class ToneFragment extends Fragment {

    TextView mFreqView, mFreqStatusView;
    Button mFreqDownBtn, mFreqUpBtn, mGenerateBtn, mCallShortBtn, mCallLongBtn;
    SeekBar mFrequencySeekBar, mFrequencyIntervalSeekBar;

    boolean isGenerateBtnClicked = false;

    EuphonyTx mEuphonyTx;

    public ToneFragment() {
        // Required empty public constructor
    }

    public static ToneFragment newInstance(String param1, String param2) {
        ToneFragment fragment = new ToneFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mEuphonyTx = new EuphonyTx(this.getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tone, container, false);

        mFreqView = v.findViewById(R.id.frequency_view);
        mFreqStatusView = v.findViewById(R.id.frequency_status);

        mFreqDownBtn = v.findViewById(R.id.tone_freq_down_btn);
        mFreqDownBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFrequency(getFrequency() - 1);
            }
        });

        mFreqUpBtn = v.findViewById(R.id.tone_freq_up_btn);
        mFreqUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFrequency(getFrequency() + 1);
            }
        });

        mCallShortBtn = v.findViewById(R.id.tone_call_short_btn);
        mCallShortBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEuphonyTx.callAPI(mFrequencySeekBar.getProgress(), EuphonyTx.EpnyAPIDuration.LENGTH_SHORT);
            }
        });

        mCallLongBtn = v.findViewById(R.id.tone_call_long_btn);
        mCallLongBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEuphonyTx.callAPI(mFrequencySeekBar.getProgress(), EuphonyTx.EpnyAPIDuration.LENGTH_LONG);
            }
        });

        mGenerateBtn = v.findViewById(R.id.tone_generator_btn);
        mGenerateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("EUPHONY_SPEAKER", "STATUS : " + mEuphonyTx.getStatus());
                if(isGenerateBtnClicked) {
                    mGenerateBtn.setText("GENERATE");
                    mCallLongBtn.setEnabled(true);
                }
                else {
                    mGenerateBtn.setText("STOP");
                    mCallLongBtn.setEnabled(false);
                }
                isGenerateBtnClicked ^= true;

                mEuphonyTx.setToneOn(isGenerateBtnClicked);

            }
        });

        mFrequencySeekBar = v.findViewById(R.id.frequencyBar);
        mFrequencySeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mFreqStatusView.setText("" + progress + "hz");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mEuphonyTx.setAudioFrequency((double)seekBar.getProgress());
                Log.d("EUPHONY_SPEAKER", "frequency : " + seekBar.getProgress());
                if(isGenerateBtnClicked) {


                    /*switch (mEuphonyTx.getStatus()) {
                        case RUNNING:
                            //mRxManager.setFrequencyForDetect(seekBar.getProgress());
                            break;
                        case STOP:
                            //mEuphonyTx.setToneOn(true);
                            break;
                    }*/
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
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mEuphonyTx.setAudioFrequency((double)seekBar.getProgress());
                Log.d("EUPHONY_SPEAKER", "frequency : " + seekBar.getProgress());
                if(isGenerateBtnClicked) {


                    /*switch (mEuphonyTx.getStatus()) {
                        case RUNNING:
                            //mRxManager.setFrequencyForDetect(seekBar.getProgress());
                            break;
                        case STOP:
                            //mEuphonyTx.setToneOn(true);
                            break;
                    }*/
                }
            }
        });



        return v;
    }

    private double getFrequency() {
        return mFrequencySeekBar.getProgress();
    }

    private void setFrequency(double freq) {
        if(mEuphonyTx != null)
            mEuphonyTx.setAudioFrequency(freq);

        if(mFrequencySeekBar != null)
            mFrequencySeekBar.setProgress((int)freq);

        if(mFreqStatusView != null)
            mFreqStatusView.setText("" + freq + "hz");

    }
}