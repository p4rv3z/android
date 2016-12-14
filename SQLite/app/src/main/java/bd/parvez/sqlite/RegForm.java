package bd.parvez.sqlite;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import bd.parvez.database.MyDatabaseHelper;

public class RegForm extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "SQLite RegForm";
    EditText etName, etId, etInstitution, etDepartment;
    Button btnSave, btnLoad, btnEdit, btnDelete;
    private MyDatabaseHelper myDatabaseHelper;
    private Student student;
    private static long back_pressed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_reg_form);
        etName = (EditText) findViewById(R.id.etName);
        etId = (EditText) findViewById(R.id.etId);
        etInstitution = (EditText) findViewById(R.id.etInstitution);
        etDepartment = (EditText) findViewById(R.id.etDepartment);

        btnSave = (Button) findViewById(R.id.btnSave);
        btnLoad = (Button) findViewById(R.id.btnLoad);
        btnEdit = (Button) findViewById(R.id.btnEdit);
        btnDelete = (Button) findViewById(R.id.btnDelete);

        btnSave.setOnClickListener(this);
        btnLoad.setOnClickListener(this);
        btnEdit.setOnClickListener(this);
        btnDelete.setOnClickListener(this);


        Student st = (Student) getIntent().getSerializableExtra("student");
        if (st != null) {
            setData(st);
        }

        myDatabaseHelper = new MyDatabaseHelper(this);


    }

    private void setIconInMenu(MenuItem item, String id) {
        if (favourite(id)) {
            item.setIcon(R.mipmap.ic_fav);
        } else {
            item.setIcon(R.mipmap.ic_fav_un);
        }
    }

    private boolean favourite(String id) {
        // is id is available in favourite database
        return myDatabaseHelper.isAvailable(id);
    }

    private void setData(Student student) {
        etName.setText(student.getName());
        etId.setText(student.getId());
        etInstitution.setText(student.getInstitution());
        etDepartment.setText(student.getDepartment());
    }

    @Override
    protected void onResume() {
        super.onResume();
        myDatabaseHelper.openDB();
    }

    @Override
    protected void onPause() {
        super.onPause();
        myDatabaseHelper.closeDB();
    }

    @Override
    public void onClick(View v) {

        if (v == btnSave) {
            insertInDB();
        } else if (v == btnLoad) {
            String id = etId.getText().toString();
            if (!id.isEmpty()) {
                Student student = myDatabaseHelper.loadSingleDataFromStdTable(id);
                if(student!=null) {
                    etName.setText(student.getName());
                    etId.setText(student.getId());
                    etInstitution.setText(student.getInstitution());
                    etDepartment.setText(student.getDepartment());
                }else {
                    Toast.makeText(this, "Data not found.", Toast.LENGTH_LONG).show();
                }
            }
        } else if (v == btnEdit) {
            updateDB();
        } else if (v == btnDelete) {
            String id = etId.getText().toString();
            if (!id.isEmpty()) {
                // myDatabaseHelper.delFromFavTable(st);
                myDatabaseHelper.delFromStdTable(id);
                etName.setText("");
                etId.setText("");
                etInstitution.setText("");
                etDepartment.setText("");
                Log.i(TAG, "Delete Button Clicked :" + id);
            }
        }
    }

    private void updateDB() {
        String name = etName.getText().toString();
        String id = etId.getText().toString();
        String institution = etInstitution.getText().toString();
        String department = etDepartment.getText().toString();
        if (!name.isEmpty() && !id.isEmpty() && !institution.isEmpty() && !department.isEmpty()) {
            student = new Student();
            student.setName(name);
            student.setId(id);
            student.setInstitution(institution);
            student.setDepartment(department);
            boolean update = myDatabaseHelper.updateDB(student);
            if (update) {
                Toast.makeText(this, "Data updated", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Data update failed.", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "Please Insert your all information.", Toast.LENGTH_LONG).show();
        }

    }


    private void insertInDB() {
        String name = etName.getText().toString();
        String id = etId.getText().toString();
        String institution = etInstitution.getText().toString();
        String department = etDepartment.getText().toString();
        if (!name.isEmpty() && !id.isEmpty() && !institution.isEmpty() && !department.isEmpty()) {
            student = new Student();
            student.setName(name);
            student.setId(id);
            student.setInstitution(institution);
            student.setDepartment(department);
            boolean insert = myDatabaseHelper.insertInDB(student);
            if (insert) {
                Toast.makeText(this, "Data inserted", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Data insert failed.", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "Please Insert your all information.", Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_reg, menu);
        setIconInMenu(menu.getItem(0), etId.getText().toString());
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        String studentId = etId.getText().toString();
        int id = item.getItemId();
        if (!studentId.isEmpty()) {
            setIconInMenu(item, studentId);
            if (id == R.id.set_fav_un) {
                if (favourite(studentId)) {
                    //delete from favourite database
                    Student student = new Student();
                    student.setId(studentId);
                    myDatabaseHelper.delFromFavTable(student);
                } else {
                    //insert to favourite database
                    Student student = new Student();
                    student.setId(studentId);
                    myDatabaseHelper.addToFavourite(student);

                }
                setIconInMenu(item, studentId);
            }
        } else {
            Toast.makeText(this, "Please insert your ID", Toast.LENGTH_LONG).show();
        }
        return super.onOptionsItemSelected(item);
    }


}