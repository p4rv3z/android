package bd.com.parvez.asynctaskloaderexample;

import android.content.Context;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String> {
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.textView);
        textView.setText("");
    }

    public void asyncTask(View view) {
        //getSupportLoaderManager().initLoader(0, null, this).forceLoad();
        //getSupportLoaderManager().restartLoader(0, null, this).forceLoad();
        getSupportLoaderManager().restartLoader(0, null, this).forceLoad();
        getSupportLoaderManager().restartLoader(1, null, this).forceLoad();
        getSupportLoaderManager().restartLoader(2, null, this).forceLoad();
    }

    public void cancel(View view) {

    }

    public void clear(View view) {
        textView.setText("");
    }

    @Override
    public Loader<String> onCreateLoader(int id, Bundle args) {
        textView.append("Creating Loader");
        Log.d(getClass().getName().toString() + " :: ", "onCreateLoader");
        return new MyAsyncTaskLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {
        Log.d(getClass().getName().toString() + " :: ", "onLoadFinished");
        textView.append("load finishid, data " + data );
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {
        Log.d(getClass().getName().toString() + " :: ", "onLoaderReset");

    }

    private static class MyAsyncTaskLoader extends AsyncTaskLoader<String> {
        public MyAsyncTaskLoader(Context context) {
            super(context);
            Log.d(getClass().getName().toString() + " :: ", "MyAsyncTaskLoader");
        }

        @Override
        public String loadInBackground() {
            Log.d(getClass().getName().toString() + " :: ", "loadInBackground");
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "return from loadInBackground.";
        }

        @Override
        public void deliverResult(String data) {
            data += " from delevered Result.";
            super.deliverResult(data);
        }
    }
}
