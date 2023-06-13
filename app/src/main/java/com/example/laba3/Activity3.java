package com.example.laba3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Activity3 extends AppCompatActivity {//
    ContactItem contact;
    String FILE_NAME = "contacts.json";
    List<ContactItem> contacts = new ArrayList<>();
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_3);
        Bundle arguments = getIntent().getExtras();
        contact = (ContactItem) arguments.getParcelable("contact");
        TextView itemText = findViewById(R.id.itemView);
        itemText.setText(contact.toString());
        intent = new Intent(this, SeeContactActivity.class);
        read();
        resetItemsView();
    }

    private void resetItemsView() {
        ListView list = findViewById(R.id.itemsView);
        ContactsAdapter adapter = new ContactsAdapter(this, R.layout.countact_view, (ArrayList<ContactItem>) contacts);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ContactItem selected = (ContactItem) adapterView.getItemAtPosition(i);
                intent.putExtra("contact", selected);//
                startActivity(intent);
            }
        });
    }

    private File getExternalPath() {
        return new File(getExternalFilesDir(null), FILE_NAME);
    }

    //Кнопка "Запомнить"
    public void save(View view) {
        contacts.add(contact);
        FileOutputStream fos = null;
        try {
            Gson gson = new Gson();
            ContactItem.DataItems dataItems = new ContactItem.DataItems();
            dataItems.setData(contacts);
            String text = gson.toJson(dataItems);
            Log.d("toJson", text);
            fos = new FileOutputStream(getExternalPath());
            fos.write(text.getBytes());
            Toast.makeText(this, "Файл сохранен", Toast.LENGTH_SHORT).show();
        }
        catch (IOException ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();//
        }
        finally {
            try {
                if (fos != null)//
                    fos.close();//
            }
            catch (IOException ex) {//

                Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
        resetItemsView();
    }

    public void read() {
        try (FileInputStream fileInputStream = new FileInputStream(getExternalPath());
             InputStreamReader streamReader = new InputStreamReader(fileInputStream)) {

            Gson gson = new Gson();
            ContactItem.DataItems dataItems = gson.fromJson(streamReader, ContactItem.DataItems.class);
            Toast.makeText(this, "Файл загружен", Toast.LENGTH_SHORT).show();
            contacts = dataItems.getData();
        } catch (IOException ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();//
        }

    }
}