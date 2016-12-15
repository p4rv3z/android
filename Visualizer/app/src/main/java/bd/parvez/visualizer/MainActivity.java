package bd.parvez.visualizer;

import android.app.Activity;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.media.audiofx.Visualizer;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;


public class MainActivity extends Activity {

    VisualizerView mVisualizerView;
    GridView DTMFPianoView;
    static final String[] numbers = new String[] { "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "*", "0", "#" };

    static final int[] toneTypes = new int[] { ToneGenerator.TONE_DTMF_1,
            ToneGenerator.TONE_DTMF_2, ToneGenerator.TONE_DTMF_3,
            ToneGenerator.TONE_DTMF_4, ToneGenerator.TONE_DTMF_5,
            ToneGenerator.TONE_DTMF_6, ToneGenerator.TONE_DTMF_7,
            ToneGenerator.TONE_DTMF_8, ToneGenerator.TONE_DTMF_9,
            ToneGenerator.TONE_DTMF_S, ToneGenerator.TONE_DTMF_0,
            ToneGenerator.TONE_DTMF_P
    };

    private Visualizer mVisualizer;

    int streamType;
    int volume;
    int durationMs;
    ToneGenerator toneGenerator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mVisualizerView = (VisualizerView) findViewById(R.id.visualizerview);

        initAudio();

        DTMFPianoView = (GridView) findViewById(R.id.gridView);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_list_item_1, numbers);

        DTMFPianoView.setAdapter(adapter);

        DTMFPianoView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                toneGenerator.startTone(toneTypes[position], durationMs);

            }});

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isFinishing()) {
            mVisualizer.release();
        }
    }

    private void initAudio() {

        streamType = AudioManager.STREAM_MUSIC;
        volume = 50;
        durationMs = 500;
        toneGenerator = new ToneGenerator(streamType, volume);

        setupVisualizerFxAndUI();
        mVisualizer.setEnabled(true);

    }

    private void setupVisualizerFxAndUI() {

        // Creating a Visualizer on the output mix (audio session 0)
        // need permission MODIFY_AUDIO_SETTINGS
        mVisualizer = new Visualizer(0);

        mVisualizer.setCaptureSize(Visualizer.getCaptureSizeRange()[1]);
        mVisualizer.setDataCaptureListener(
                new Visualizer.OnDataCaptureListener() {
                    public void onWaveFormDataCapture(Visualizer visualizer,
                                                      byte[] bytes, int samplingRate) {
                        mVisualizerView.updateVisualizer(bytes);
                    }

                    public void onFftDataCapture(Visualizer visualizer,
                                                 byte[] bytes, int samplingRate) {
                    }
                }, Visualizer.getMaxCaptureRate() / 2, true, false);

    }
}
