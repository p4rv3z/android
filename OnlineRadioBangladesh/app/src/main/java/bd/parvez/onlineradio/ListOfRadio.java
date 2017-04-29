package bd.parvez.onlineradio;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.io.File;
import java.util.ArrayList;
import java.util.Locale;


public class ListOfRadio extends Activity {
    private AdView bannerAd;
    private AdRequest bannerAdRequest;
    private TabHost tabHost;
    private TabHost.TabSpec tabSpec;
    private CustomList customList;
    private CustomPathList customPathList;
    private ListView listView;
    private ListView listViewFav;
    private ListView listViewRecord;
    private EditText search;
    private TextView path;
    private RadioDB radioDB;
    private SharedPreferences sp;
    private SharedPreferences.Editor spEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_radio);
        sp = getSharedPreferences("position", MODE_PRIVATE);
        init();
        initTab();
        bannerAdRequest();
        tabChanged();
        radioList();
        search();
    }

    private void search() {
        search.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable arg0) {

                String text = search.getText().toString().toLowerCase(Locale.getDefault());
                customList.filter(text);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {

            }

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {

            }
        });
    }

    private void radioList() {
        customList = new CustomList(this, radioDB.getAllData(), true);
        listView.setAdapter(customList);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder favAlert = new AlertDialog.Builder(ListOfRadio.this);
                String name = customList.getCuctomItem().get(position).getName().toString();
                final int rowId = customList.getCuctomItem().get(position).getId();
                favAlert.setMessage("Are you sure?\nYou want to delete " + name + " from your database.");
                favAlert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        radioDB.deleteRow(rowId);
                        setPosition();
                        radioList();
                        dialog.dismiss();
                    }
                });
                favAlert.setNeutralButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                favAlert.create();
                favAlert.show();
                return true;
            }
        });
    }

    private void setPosition() {
        int p = getPosition();
        int dbp = radioDB.getAllData().size() - 1;
        if (p > dbp) {
            spEditor = sp.edit();
            spEditor.putInt("position", dbp);
            spEditor.commit();
        } else {

        }


    }

    private int getPosition() {
        return sp.getInt("position", 0);
    }

    private void tabChanged() {
        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabName) {
                if (tabName.equals("ListTag")) {
                    radioList();
                    hideKeyboard();
                } else if (tabName.equals("FavouriteTag")) {
                    CustomList cl;
                    ArrayList<RadioClass> favData = new ArrayList<RadioClass>();
                    for (int i = 0; i < radioDB.getAllData().size(); i++) {
                        if (radioDB.getAllData().get(i).getFav() == 1) {
                            favData.add(radioDB.getAllData().get(i));
                        }
                    }
                    cl = new CustomList(ListOfRadio.this, favData, false);
                    listViewFav.setAdapter(cl);
                    hideKeyboard();
                } else if (tabName.equals("RecordTag")) {
                    customPathList();
                    hideKeyboard();
                } else {
                    Toast.makeText(getApplicationContext(), "else " + tabName, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(tabHost.getApplicationWindowToken(), 0);
    }

    private void customPathList() {
        String filesPath = (MyServices.getPath(this) + "/OnlineRadioBangladesh/").toString();
        path.setText(filesPath);
        ArrayList<String> nameList = getName(filesPath);
        if (nameList != null) {
            customPathList = new CustomPathList(ListOfRadio.this, nameList);
            listViewRecord.setAdapter(customPathList);
        } else {
            Toast.makeText(this, "File Not Found.", Toast.LENGTH_LONG).show();
        }
    }

    private void initTab() {
        tabHost = (TabHost) findViewById(R.id.tabHost);
        tabHost.setup();
        tabSpec = tabHost.newTabSpec("ListTag");
        tabSpec.setContent(R.id.tab1);
        tabSpec.setIndicator("List");
        tabHost.addTab(tabSpec);
        tabSpec = tabHost.newTabSpec("FavouriteTag");
        tabSpec.setContent(R.id.tab2);
        tabSpec.setIndicator("Favourite");
        tabHost.addTab(tabSpec);
        tabSpec = tabHost.newTabSpec("RecordTag");
        tabSpec.setContent(R.id.tab3);
        tabSpec.setIndicator("Recorded");
        tabHost.addTab(tabSpec);
        tabHost.setCurrentTab(0);
    }

    private void init() {
        radioDB = RadioDB.getInstance(this);//database
        bannerAd = (AdView) findViewById(R.id.banner_ad_list);
        listView = (ListView) findViewById(R.id.list_list_view);
        listViewFav = (ListView) findViewById(R.id.ll_custom_list_fav);
        listViewRecord = (ListView) findViewById(R.id.list_list_files);
        search = (EditText) findViewById(R.id.et_list_search);
        path = (TextView) findViewById(R.id.et_list_path);
    }

    private void bannerAdRequest() {
        //todo Testing Banner Ad
        //bannerAdRequest = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build();//test ad
        bannerAdRequest = new AdRequest.Builder().build(); // real
        bannerAd.loadAd(bannerAdRequest);
    }

    /**
     * Called when leaving the activity
     */
    @Override
    public void onPause() {
        if (bannerAd != null) {
            bannerAd.pause();
        }
        super.onPause();
    }

    /**
     * Called when returning to the activity
     */
    @Override
    public void onResume() {
        super.onResume();
        if (bannerAd != null) {
            bannerAd.resume();
        }
    }

    /**
     * Called before the activity is destroyed
     */
    @Override
    public void onDestroy() {
        if (bannerAd != null) {
            bannerAd.destroy();
        }
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        //overridePendingTransition(R.anim.slide_to, R.anim.slide_from);
        super.onBackPressed();
        finish();
        ListOfRadio.this.overridePendingTransition(R.anim.slide_out_from, R.anim.slide_out_to);
    }

    public void addtoDB(View view) {
        final Dialog addDb = new Dialog(this);
        addDb.requestWindowFeature(Window.FEATURE_NO_TITLE);
        addDb.setContentView(R.layout.addtodb_custom_alertbox);

        final EditText nameRadio = (EditText) addDb.findViewById(R.id.et_radioName_custom);
        final EditText urlRadio = (EditText) addDb.findViewById(R.id.et_radioUrl_custom);
        final Button ok = (Button) addDb.findViewById(R.id.btn_ok_custom_add);
        final Button cancel = (Button) addDb.findViewById(R.id.btn_cancel_custom_add);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameRadio.getText().toString();
                String url = urlRadio.getText().toString().trim();
                if (!name.isEmpty() && !url.isEmpty() && (url.startsWith("http://") || url.startsWith("https://"))) {
                    if (insertDB(name, url) > 0) {
                        String msg = "successfully insert into database.";
                        Toast.makeText(ListOfRadio.this, msg, Toast.LENGTH_LONG).show();
                        addDb.dismiss();
                        radioList();
                    } else {
                        Toast.makeText(ListOfRadio.this, "failed to insert.", Toast.LENGTH_LONG).show();
                        addDb.dismiss();
                    }
                } else {
                    Toast.makeText(ListOfRadio.this, "Please fill correctly.", Toast.LENGTH_LONG).show();
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addDb.dismiss();
            }
        });
        addDb.show();


    }

    private long insertDB(String radioName, String radioUrl) {
        RadioClass radioClass = new RadioClass(radioName, radioUrl, 1, 0);
        return radioDB.insertRadio(radioClass);
    }

    public void changePath(View view) {
        final Dialog pathDia = new Dialog(this);
        pathDia.requestWindowFeature(Window.FEATURE_NO_TITLE);
        pathDia.setContentView(R.layout.custom_path);
        final String xx = MyServices.getPath(ListOfRadio.this).toString();
        final Button ok = (Button) pathDia.findViewById(R.id.btn_ok_custom_path);
        final Button cancel = (Button) pathDia.findViewById(R.id.btn_cancel_custom_path);
        final EditText etPath = (EditText) pathDia.findViewById(R.id.et_custom_path);
        etPath.setText(xx);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String path_name = etPath.getText().toString();
                if (path_name.isEmpty()) {
                    path_name = xx;
                }
                if (new File(path_name).isDirectory()) {
                    MyServices.setPath(ListOfRadio.this, path_name);
                    path.setText((path_name + "/OnlineRadioBangladesh/").toString());
                } else {
                    try {
                        new File(path_name).mkdirs();
                        if (new File(path_name).exists()) {
                            MyServices.setPath(ListOfRadio.this, path_name);
                            path.setText((path_name + "/OnlineRadioBangladesh/").toString());
                        }else {
                            Toast.makeText(getApplicationContext(),"Can't make directory like this name.",Toast.LENGTH_LONG).show();
                        }
                    }catch (Exception e){
                        Toast.makeText(getApplicationContext(),"!!!",Toast.LENGTH_LONG).show();
                    }
                }
                customPathList();
                pathDia.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pathDia.dismiss();
            }
        });
        pathDia.show();
    }

    public ArrayList<String> getName(String DirectoryPath) {
        ArrayList<String> MyFiles = new ArrayList<String>();
        File f = new File(DirectoryPath);
        if (!f.exists()) {
            f.mkdirs();
        }
        File[] files = f.listFiles();
        if (files.length == 0)
            return null;
        else {
            for (int i = 0; i < files.length; i++) {
                if (!files[i].isHidden()) {
                    MyFiles.add(files[i].getName());
                }
            }
        }
        return MyFiles;
    }
}
