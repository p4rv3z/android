package bd.parvez.onlineradio;


import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Calendar;
import java.util.TimeZone;

public class RecordData extends AsyncTask<String, String, String> {
    private InputStream input;
    private OutputStream output;
    private Activity activity;
    private int count;
    private String fileName;
    public boolean p = true;

    public RecordData(Activity activity, String name) {
        this.activity = activity;
        Calendar c = Calendar.getInstance(TimeZone.getDefault());

        String date = c.get(Calendar.DAY_OF_MONTH) + "-" + c.get(Calendar.MONTH) + "-" + c.get(Calendar.YEAR)
                + "-at-" + c.get(Calendar.HOUR) + "-" + c.get(Calendar.MINUTE) + "-" + c.get(Calendar.SECOND);
        fileName = name + "-" + date.toString();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        // Shows Progress Bar Dialog and then call doInBackground method
        // showDialog(progress_bar_type);
    }

    @Override
    protected String doInBackground(String... rUrl) {
        Log.d("BD", "doInBackground");

        try {
            URL url = new URL(rUrl[0]);
            URLConnection conection = url.openConnection();
            conection.connect();
            // Get Music file length
            int lenghtOfFile = conection.getContentLength();
            // input stream to read file - with 8k buffer
            input = new BufferedInputStream(url.openStream(), 10 * 1024);
            File f = new File(MyServices.getPath(activity.getApplicationContext()) + "/OnlineRadioBangladesh");
            f.mkdirs();
            Log.d("BD", f.toString());
            // Output stream to write file in SD card
            output = new FileOutputStream(f + ("/" + fileName + ".mp3").toString());
            Log.d("BD", output.toString());
            byte data[] = new byte[1024];
            long total = 0;
            while (stop() && (count = input.read(data)) != -1) {
                total += count;
                // Publish the progress which triggers onProgressUpdate method
                publishProgress("" + (int) ((total * 100) / lenghtOfFile));
                // Write data to file
                output.write(data, 0, count);
                Log.d("BD", count + "");
                Thread.sleep(110);//slowing download
            }
            Log.d("BD", "loop out");
        } catch (Exception e) {
            Log.e("Error: ", e.getMessage());
        }
        return null;
    }

    private boolean stop() {
        boolean x = p;
        return x;
    }

    @Override
    protected void onPostExecute(String file_url) {
        Log.d("BD", "onPostExecute");
        try {
            input.close();
            // output.flush();
            // Close streams
            output.close();

            Log.d("BD", "close");
        } catch (Exception e) {
            Log.d("BD", "AsynTask catch close");
        }
    }
}