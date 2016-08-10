package record;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;  
import java.util.Date;

import android.graphics.Canvas;  
import android.graphics.Color;  
import android.graphics.Paint;  
import android.graphics.Rect;  
import android.media.AudioRecord;  
import android.os.AsyncTask;
import android.view.SurfaceView;  


public class ClsOscilloscope {
	
    private ArrayList<Short> inBuf = new ArrayList<Short>();
    private ArrayList<byte[]> write_data = new ArrayList<byte[]>();
    private boolean isRecording = false;
    private boolean isWriting = false;
    
   
    public int rateX = 352; 
    
   
    public int rateY = 0;  
    
   
    public int baseLine = 0;
    
    @SuppressWarnings("unused")
	private AudioRecord audioRecord;
    
    private String audioName;
    
    int recBufSize;
    
    int sampleRateInHz = 44100;
    
 
    int draw_time = 1000 / 15;
    

    int divider = 2;
    
    long c_time;
    
    private String tempName = U.DATA_DIRECTORY + "/temp.pcm";
    

    public void Start(AudioRecord audioRecord, int recBufSize, SurfaceView sfv,  
            Paint mPaint, String audioName) {  
    	this.audioRecord = audioRecord;
        isRecording = true;
        isWriting = true;
        this.audioName = audioName;
        this.recBufSize = recBufSize;
        new Thread(new WriteRunnable()).start();
        new RecordTask(audioRecord, recBufSize, sfv, mPaint).execute();
    } 
    

    public void Stop() {  
        isRecording = false;  
        inBuf.clear();
    }
    

    class RecordTask extends AsyncTask<Object, Object, Object> {

    	private int recBufSize;  
        private AudioRecord audioRecord;  
        private SurfaceView sfv; 
        private Paint mPaint;  
        
        public RecordTask(AudioRecord audioRecord, int recBufSize,
        		SurfaceView sfv, Paint mPaint) {  
            this.audioRecord = audioRecord;  
            this.recBufSize = recBufSize;  
            this.sfv = sfv;
            this.mPaint = mPaint;
        }
    	
		@Override
		protected Object doInBackground(Object... params) {
			try {
                byte[] buffer = new byte[recBufSize];
                audioRecord.startRecording();
                while (isRecording) {
              
                    int readsize = audioRecord.read(buffer, 0,  
                            recBufSize);
                    synchronized (inBuf) {
	                
	                	int len = readsize / rateX;
	                    for (int i = 0; i < len; i += rateX) {
	                    	inBuf.add((short)((0x0000 | buffer[i + 1]) << 8 | buffer[i]));
	                    }
                    }
                    publishProgress();
                    if (AudioRecord.ERROR_INVALID_OPERATION != readsize) {
                    	synchronized (write_data) {
                    		write_data.add(buffer);
                        }
        			}
                }
    			isWriting = false;
                audioRecord.stop();
            } catch (Throwable t) {
            }
			return null;
		}

		@SuppressWarnings("unchecked")
		@Override
		protected void onProgressUpdate(Object... values) {
            long time = new Date().getTime();
            if(time - c_time >= draw_time){
            	ArrayList<Short> buf = new ArrayList<Short>();
    			synchronized (inBuf) {
    				if (inBuf.size() == 0)  
    	                return;
    	            while(inBuf.size() > sfv.getWidth() / divider){
    	            	inBuf.remove(0);
    	            }
    	            buf = (ArrayList<Short>) inBuf.clone();  
    			}
            	SimpleDraw(buf, baseLine);
            	c_time = new Date().getTime();
            }
			super.onProgressUpdate(values);
		}
		
 
        void SimpleDraw(ArrayList<Short> buf, int baseLine) { 
        	if(rateY == 0){
        		rateY = 50000 / sfv.getHeight();
        		baseLine = sfv.getHeight() / 2;
        	}
            Canvas canvas = sfv.getHolder().lockCanvas(  
                    new Rect(0, 0, sfv.getWidth(), sfv.getHeight()));  
            canvas.drawColor(Color.BLACK);
            int start = sfv.getWidth() - buf.size() * divider;
            int py = baseLine;
            if(buf.size() > 0)
            	py += buf.get(0) / rateY;
            int y;
            canvas.drawLine(0, baseLine, start - divider, baseLine, mPaint);
            for (int i = 0; i < buf.size(); i++) {
                y = buf.get(i) / rateY + baseLine;
                canvas.drawLine(start + (i - 1 ) * divider, py, start + i * divider, y, mPaint);
                py = y;
            }  
            sfv.getHolder().unlockCanvasAndPost(canvas);
        }
    }
    

    class WriteRunnable implements Runnable {
		@Override
		public void run() {
			try {
                FileOutputStream fos = null;
                File file = null;
        		try {
        			file = new File(tempName);
        			if (file.exists()) {
        				file.delete();
        			}
        			fos = new FileOutputStream(file);
        		} catch (Exception e) {
        			e.printStackTrace();
        		}
                while (isWriting || write_data.size() > 0) {
                	byte[] buffer = null;
                    synchronized (write_data) {
                    	if(write_data.size() > 0){
                    		buffer = write_data.get(0);
                        	write_data.remove(0);
                    	}
                    }
                    try {
                    	if(buffer != null){
        					fos.write(buffer);
        					fos.flush();
                    	}
    				} catch (IOException e) {
    					e.printStackTrace();
    				}
                }
    			fos.close();
                copyWaveFile(tempName, U.DATA_DIRECTORY + "/" + audioName);
            } catch (Throwable t) {
            }
		}
    }
    
 	private void copyWaveFile(String inFilename, String outFilename) {
 		FileInputStream in = null;
 		FileOutputStream out = null;
 		long totalAudioLen = 0;
 		long totalDataLen = totalAudioLen + 36;
 		long longSampleRate = sampleRateInHz;
 		int channels = 2;
 		long byteRate = 16 * sampleRateInHz * channels / 8;
 		byte[] data = new byte[recBufSize];
 		try {
 			in = new FileInputStream(inFilename);
 			out = new FileOutputStream(outFilename);
 			totalAudioLen = in.getChannel().size();
 			totalDataLen = totalAudioLen + 36;
 			WriteWaveFileHeader(out, totalAudioLen, totalDataLen,
 					longSampleRate, channels, byteRate);
 			while (in.read(data) != -1) {
 				out.write(data);
 			}
 			in.close();
 			out.close();
 		} catch (FileNotFoundException e) {
 			e.printStackTrace();
 		} catch (IOException e) {
 			e.printStackTrace();
 		}
 	}

	private void WriteWaveFileHeader(FileOutputStream out, long totalAudioLen,
			long totalDataLen, long longSampleRate, int channels, long byteRate)
			throws IOException {
		byte[] header = new byte[44];
		header[0] = 'R'; // RIFF/WAVE header
		header[1] = 'I';
		header[2] = 'F';
		header[3] = 'F';
		header[4] = (byte) (totalDataLen & 0xff);
		header[5] = (byte) ((totalDataLen >> 8) & 0xff);
		header[6] = (byte) ((totalDataLen >> 16) & 0xff);
		header[7] = (byte) ((totalDataLen >> 24) & 0xff);
		header[8] = 'W';
		header[9] = 'A';
		header[10] = 'V';
		header[11] = 'E';
		header[12] = 'f'; // 'fmt ' chunk
		header[13] = 'm';
		header[14] = 't';
		header[15] = ' ';
		header[16] = 16; // 4 bytes: size of 'fmt ' chunk
		header[17] = 0;
		header[18] = 0;
		header[19] = 0;
		header[20] = 1; // format = 1
		header[21] = 0;
		header[22] = (byte) channels;
		header[23] = 0;
		header[24] = (byte) (longSampleRate & 0xff);
		header[25] = (byte) ((longSampleRate >> 8) & 0xff);
		header[26] = (byte) ((longSampleRate >> 16) & 0xff);
		header[27] = (byte) ((longSampleRate >> 24) & 0xff);
		header[28] = (byte) (byteRate & 0xff);
		header[29] = (byte) ((byteRate >> 8) & 0xff);
		header[30] = (byte) ((byteRate >> 16) & 0xff);
		header[31] = (byte) ((byteRate >> 24) & 0xff);
		header[32] = (byte) (2 * 16 / 8); // block align
		header[33] = 0;
		header[34] = 16; // bits per sample
		header[35] = 0;
		header[36] = 'd';
		header[37] = 'a';
		header[38] = 't';
		header[39] = 'a';
		header[40] = (byte) (totalAudioLen & 0xff);
		header[41] = (byte) ((totalAudioLen >> 8) & 0xff);
		header[42] = (byte) ((totalAudioLen >> 16) & 0xff);
		header[43] = (byte) ((totalAudioLen >> 24) & 0xff);
		out.write(header, 0, 44);
	}
}   
