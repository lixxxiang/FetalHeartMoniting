package record;

import java.sql.Date;
import java.text.SimpleDateFormat;
import login.LoginActivity;
import com.infzm.slidingmenu.demo.R;

import fragment.ChooseDocFragment;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;


@SuppressLint("SimpleDateFormat") public class RecordActivity extends Activity implements OnClickListener {
	
	@SuppressWarnings("unused")
	private static final String TAG = "RecordActivity";
	Button startButton,stopButton;
	MediaRecorder mMediaRecorder;
	SurfaceView energy;
	
	public String resultCity=null;
	
	
	ClsOscilloscope clsOscilloscope = new ClsOscilloscope();  
	
    static final int frequency = 44100;
    static final int channelConfiguration = AudioFormat.CHANNEL_IN_STEREO;
    static final int audioEncoding = AudioFormat.ENCODING_PCM_16BIT;
    int recBufSize;
    AudioRecord audioRecord;  
    Paint mPaint;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.record);
		stopButton = (Button) this.findViewById(R.id.stop);
		startButton = (Button) this.findViewById(R.id.start);
		energy = (SurfaceView) findViewById(R.id.music_record);
		stopButton.setOnClickListener(this);
		startButton.setOnClickListener(this);
        recBufSize = AudioRecord.getMinBufferSize(frequency,  
                channelConfiguration, audioEncoding);  
        audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, frequency,  
                channelConfiguration, audioEncoding, recBufSize);
        
        mPaint = new Paint();    
        mPaint.setColor(Color.WHITE);
        mPaint.setStrokeWidth(2); 
	}
	
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.start:
			
			startButton.setText("正在录音...");
			startButton.setEnabled(false);
			startRecording();
			
			break;
			
		case R.id.stop:
			
			startButton.setText("开始录音");
			startButton.setEnabled(true);
			stopRecording();
			
			break;

		default:
			
			break;
		}
	}
	
	private void startRecording() {  
		if(!U.sdCardExists()){
			startButton.setText("开始录音");
			startButton.setEnabled(true);
			return;
		}
	
		String fileName =LoginActivity.name+"-"+new SimpleDateFormat("yyyyMMdd_hhmmss").format(new Date(System.currentTimeMillis()))+"docIs-"+ChooseDocFragment.DocName+".wav";
		Log.e("a", LoginActivity.name);
		clsOscilloscope.baseLine = energy.getHeight() / 2;
        clsOscilloscope.Start(audioRecord, recBufSize, energy, mPaint, fileName);
    }  
	
	private void stopRecording() {
		clsOscilloscope.Stop();
    }



}
