package com.tofabd.internetspeedmeter.ui.fragment;


import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.google.android.gms.ads.InterstitialAd;
import com.tofabd.internetspeedmeter.services.DataService;
import com.tofabd.internetspeedmeter.R;
import com.tofabd.internetspeedmeter.utils.StoredData;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class GraphWebViewFragment extends Fragment {
    LineChart mChart;
    private Thread dataUpdate;
    private Handler vHandler = new Handler();
    private ArrayList<Entry> e1, e2;
    private RadioGroup radioGroup1;

    private TextView dSpeed, uSpeed;

    private float xAxisMax = 59f;
    private int iStart = 240;

    private String content = null;

    boolean flag = false;


    protected List<Long> dList;
    protected List<Long> uList;
    protected ArrayList<Float> mDownload, mUpload;

    InterstitialAd mInterstitialAd;

    DecimalFormat df = new DecimalFormat("#.##");
    WebView webViewGraph;
    //String embeddedWeb = "http://chart.apis.google.com/chart?chxl=0:|Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec&chxt=x,y&chs=300x300&cht=r&chco=FF0000&chd=t:63,64,67,73,77,81,85,86,85,81,74,67,63&chls=2,4,0&chm=B,FF000080,0,0,0";


    public GraphWebViewFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_graph_webview, container, false);

        mDownload = new ArrayList<>();
        mUpload = new ArrayList<>();


        webViewGraph = (WebView) rootView.findViewById(R.id.webviewGraph);

        WebSettings webSettings = webViewGraph.getSettings();
        webSettings.setJavaScriptEnabled(true);


        webViewGraph.requestFocusFromTouch();

        if (Build.VERSION.SDK_INT >= 19) {
            webViewGraph.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        }
        else {
            webViewGraph.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }





        addDataSet();
        liveData();




        //webViewGraph.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);




        //webViewGraph.loadUrl("file:///android_asset/graph.html");  //alternative way


        return rootView;
    }


    public void liveData() {

        dataUpdate = new Thread(new Runnable() {
            @Override
            public void run() {

                while (!dataUpdate.getName().equals("stopped")) {

                    vHandler.post(new Runnable() {

                        @Override
                        public void run() {
                            addDataSet();

                        }
                    });

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }

            }
        });

        dataUpdate.setName("started");
        dataUpdate.start();

    }


    private void setGraph() {


    }

    private void addDataSet() {


        List<Double> listDownload = new ArrayList<>();
        List<Double> listUpload = new ArrayList<>();

        String data;

        dList = StoredData.downloadList;
        uList = StoredData.uploadList;


        float max = 0;
        float t1, t2;

        for (int i = iStart; i < dList.size(); i++) {
           // Log.e("testing", toString().valueOf(dList.size()));

            listDownload.add((double) dList.get(i) / 1024);  //convert o Kilobyte
            listUpload.add((double) uList.get(i) / 1024);

            //Log.e("testing: ", toString().valueOf(t1) + " " + toString().valueOf(t2));


        }
       data="";
        for (int i = 0; i < 60; i++) {
            data  =data+"[" + "'"+i+"'" + "," + listDownload.get(i) + "," + listUpload.get(i) + "]";
            if(i<59){
                data = data+",";
            }

        }

        content = "<html>"
                + "<head>"
                + "<script type=\"text/javascript\" src=\"file:///android_asset/loader.js\"></script>"
                + "<script type=\"text/javascript\">"
                + "  google.charts.load('current', {'packages':['corechart']});"
                + "  google.charts.setOnLoadCallback(drawChart);"

                + "  function drawChart() {"
                + "    var data = google.visualization.arrayToDataTable(["
                + "      ['Year', 'Sales', 'Expenses'],"
                + "      ['2013',  1000,      400],"
                + "      ['2014',  1170,      460],"
                + "      ['2015',  660,       1120],"
                + "      ['2016',  1030,      540]"
                + "    ]);"

                + "    var options = {"
                + "      title: 'Company Performance',"
                + "      hAxis: {title: 'Year',  titleTextStyle: {color: '#333'}},"
                + "      vAxis: {minValue: 0}"
                + "    };"

                + "    var chart = new google.visualization.AreaChart(document.getElementById('chart_div'));"
                + "    chart.draw(data, options);"
                + "  }"
                + "</script>"
                + "</head>"
                + "<body>"
                + "<div id=\"chart_div\" style=\"width: 100%; height: 100%;\"></div>"
                + "</body>"
                + "</html>";

        String content2 = "<html>"
                + "<head>"
                + "<script type=\"text/javascript\" src=\"file:///android_asset/loader.js\"></script>"
                + "<script type=\"text/javascript\">"
                + "  google.charts.load('current', {'packages':['corechart']});"
                + "  google.charts.setOnLoadCallback(drawChart);"

                + "  function drawChart() {"
                + "    var data = google.visualization.arrayToDataTable(["
                + "      ['Year', 'Sales', 'Expenses'],"
                +data
                + "    ]);"

                + "    var options = {"
                + "      title: 'Company Performance',"
                + "      hAxis: {title: 'Year',  titleTextStyle: {color: '#333'}},"
                + "      vAxis: {minValue: 0}"
                + "    };"

                + "    var chart = new google.visualization.AreaChart(document.getElementById('chart_div'));"
                + "    chart.draw(data, options);"
                + "  }"
                + "</script>"
                + "</head>"
                + "<body>"
                + "<div id=\"chart_div\" style=\"width: 100%; height: 100%;\"></div>"
                + "</body>"
                + "</html>";


        if(!flag) {
            webViewGraph.loadDataWithBaseURL("file:///android_asset/", content2, "text/html", "utf-8", null);
            flag = true;
        }
        else{

            webViewGraph.reload();
        }

        Log.e("dataset",content);
        Log.e("dataset",content2);


    }

    public void setSpeed() {


    }

    @Override
    public void onPause() {
        super.onPause();
        dataUpdate.setName("stopped");

        Log.e("astatus", "onPause");
        //  Log.e("astatus getState",dataUpdate.getState().toString());
        //finish();
    }


    @Override
    public void onResume() {
        super.onResume();
        DataService.notification_status = true;

        dataUpdate.setName("started");
        Log.e("astatus", "onResume");

        //dataUpdate.start();

        // Log.e("astatus getState",dataUpdate.getState().toString());
        // Log.e("astatus isAlive",Boolean.toString(dataUpdate.isAlive()));
        if (!dataUpdate.isAlive()) {
            //dataUpdate.run();
            liveData();

        }
        //dataUpdate.start();


    }


}
