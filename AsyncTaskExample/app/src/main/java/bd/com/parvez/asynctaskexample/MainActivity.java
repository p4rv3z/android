package bd.com.parvez.asynctaskexample;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private TextView textView;
    private MyAsyncTask myAsyncTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.textView);
        textView.setText("");
    }

    public void asyncTask(View view) {
        myAsyncTask = new MyAsyncTask();
        myAsyncTask.execute("Parvez", "Arfan", "Sudip");
    }

    public void clear(View view) {
        textView.setText("");
    }

    public void cancel(View view) {
        if (myAsyncTask!=null){
            myAsyncTask.onCancelled();
        }
    }


    /**
     * Class Type AsyncTask
     * for sort time data task
     */
    private class MyAsyncTask extends AsyncTask<String, String, Void>{

        /**
         * COnstractor
         */
        public MyAsyncTask() {
            super();
        }

        //call :: 2
        @Override
        protected Void doInBackground(String... strings) {
            Log.d(getClass().getName().toString() + " :: AsyncTask::","doInBackground");
            for(String string: strings){
                //call progress update
                publishProgress(string);
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            //publishProgress(strings);
            return null;
        }
        //call :: 1
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d(getClass().getName().toString() + " :: AsyncTask::","onPreExecute");

        }

        //call :: 3
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Log.d(getClass().getName().toString() + " :: AsyncTask::","onPostExecute");

        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            Log.d(getClass().getName().toString() + " :: AsyncTask::","onProgressUpdate");
            textView.append(values[0].toString()+"\n");

        }

        @Override
        protected void onCancelled(Void aVoid) {
            super.onCancelled(aVoid);
            Log.d(getClass().getName().toString() + " :: AsyncTask::","onCancelled with param");

        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            Log.d(getClass().getName().toString() + " :: AsyncTask::","onCancelled");

        }
    }
}
