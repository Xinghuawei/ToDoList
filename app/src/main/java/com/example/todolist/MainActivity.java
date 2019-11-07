package com.example.todolist;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.todolist.db.Task;
import com.example.todolist.db.TaskHelper;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private static final String TAG ="MainActivity";
    private static final int REQUEST_CODE_SPEECH_INPUT = 1000;
    private static final int MAX_LIST = 50;
    private TaskHelper mHelper;
    private ListView mTaskListView;
    private ArrayAdapter<String> mAdapter;
    private String[] list = new String[MAX_LIST];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mHelper = new TaskHelper(this);
        mTaskListView = findViewById(R.id.list_todo);

        updateUI();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        switch (item.getItemId()){
            case R.id.action_add_task:
                final EditText taskEditText = new EditText(this);
                AlertDialog dialog = new AlertDialog.Builder(this)
                        .setTitle("New Task")
                        .setMessage("Add a new task: ")
                        .setView(taskEditText)
                        .setPositiveButton("Add", new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which){
                                String task = String.valueOf(taskEditText.getText());
                                SQLiteDatabase db = mHelper.getWritableDatabase();
                                ContentValues values = new ContentValues();
                                values.put(Task.TaskEntry.COL_TASK_TITLE, task);
                                db.insertWithOnConflict(Task.TaskEntry.TABLE,null,values,SQLiteDatabase.CONFLICT_REPLACE);
                                db.close();
                                updateUI();
                            }
                        })
                        .setNeutralButton("Voice", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                speak();
                            }
                        })
                        .setNegativeButton("Cancel",null)
                        .create();
                dialog.show();
                return true;
            case R.id.action_pdf:
                createPDF();
                default:
                    return super.onOptionsItemSelected(item);
        }
    }

    public void createPDF(){
        SQLiteDatabase db = mHelper.getWritableDatabase();
        Cursor  cursor = db.rawQuery("select * from task",null);
        int i = 0;
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                String name = cursor.getString(1);
                list[i]=name;
                i++;
                cursor.moveToNext();
            }
        }
        Document doc = new Document();
        String outPath = Environment.getExternalStorageDirectory()+"/mylist.pdf";
        try{
            PdfWriter.getInstance(doc,new FileOutputStream(outPath));
            doc.open();
            for(String listItem : list){
                doc.add(new Paragraph(listItem));
            }
            doc.close();
            Toast.makeText(getApplicationContext(),"Create PDF success",Toast.LENGTH_SHORT).show();
        }catch (DocumentException e){
            e.printStackTrace();
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }
    }

    private void speak() {
        Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        i.putExtra(RecognizerIntent.EXTRA_PROMPT,"Hi Speak something");
        try{
            startActivityForResult(i,REQUEST_CODE_SPEECH_INPUT);
        }catch(Exception e){
            Toast.makeText(this,""+e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        switch (requestCode){
            case REQUEST_CODE_SPEECH_INPUT:{
                if(resultCode==RESULT_OK && null!=data){
                    ArrayList<String> result  = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    Log.i(result.get(0),"voice");
                    //voiceT = result.get(0);
                    SQLiteDatabase db = mHelper.getWritableDatabase();
                    ContentValues values = new ContentValues();
                    values.put(Task.TaskEntry.COL_TASK_TITLE, result.get(0));
                    db.insertWithOnConflict(Task.TaskEntry.TABLE,null,values,SQLiteDatabase.CONFLICT_REPLACE);
                    db.close();
                    updateUI();
                }
                break;
            }
        }
    }
    private void updateUI(){
        ArrayList<String> taskList = new ArrayList<>();
        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor cursor = db.query(Task.TaskEntry.TABLE,
                new String[] {Task.TaskEntry._ID, Task.TaskEntry.COL_TASK_TITLE},null,null,null,null,null);

        while (cursor.moveToNext()){
            int index = cursor.getColumnIndex(Task.TaskEntry.COL_TASK_TITLE);
            taskList.add(cursor.getString(index));
        }

        if(mAdapter==null){
            mAdapter=new ArrayAdapter<>(this,R.layout.item_todo,R.id.task_title,taskList);
            mTaskListView.setAdapter(mAdapter);
        }else{
            mAdapter.clear();
            mAdapter.addAll(taskList);
            mAdapter.notifyDataSetChanged();
        }

        cursor.close();
        db.close();
    }

    public void deleteTask(View view){
        View parent = (View)view.getParent();
        TextView taskTextView = parent.findViewById(R.id.task_title);
        String task = String.valueOf(taskTextView.getText());
        SQLiteDatabase db = mHelper.getWritableDatabase();
        db.delete(Task.TaskEntry.TABLE,Task.TaskEntry.COL_TASK_TITLE+" = ?",new String[]{task});
        db.close();
        updateUI();
    }
}
