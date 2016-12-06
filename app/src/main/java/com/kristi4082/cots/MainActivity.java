package com.kristi4082.cots;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, JSONDownloader.OnDownloadFinishListener, StorageUtil.OnSaveFinish, View.OnClickListener {
    private AlertDialog addAnimalDialog;
    private ArrayList<Animal> animals = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        animals = StorageUtil.with(this).getAnimalNames();
        if (animals != null && animals.size() > 0)
            initList();
        else
            downloadList();
        initDialog();
        findViewById(R.id.button_add).setOnClickListener(this);
    }

    private void initDialog(){
        View addAnimalView = getLayoutInflater().inflate(R.layout.add_animal_dialog, null);
        final EditText etAdd = (EditText) addAnimalView.findViewById(R.id.edittext_add_animal);
        addAnimalDialog = new AlertDialog.Builder(this)
                .setTitle(R.string.tambah_data)
                .setView(addAnimalView)
                .setNegativeButton(R.string.batal, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).setPositiveButton(R.string.simpan, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String animalName = etAdd.getText().toString();
                        addAnimal(animalName);
                        etAdd.setText("");
                    }
                })
                .create();

    }

    private void addAnimal(String animalName){
        if(!TextUtils.isEmpty(animalName))
            JsonUtil.with(this).add(animalName, this);
        else
            Toast.makeText(MainActivity.this, R.string.nama_hewan_tidak_boleh_kosong, Toast.LENGTH_SHORT).show();
    }

    private void initList(){
        animals = StorageUtil.with(this).getAnimalNames();
        String[] names = new String[animals.size()];
        for (int i=0; i<animals.size(); i++)
            names[i]=animals.get(i).getName();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, names);
        ListView listView = (ListView) findViewById(R.id.list_view);
        listView.setAdapter(adapter);
        listView.setEmptyView(findViewById(R.id.empty));
        listView.setOnItemClickListener(this);

        findViewById(R.id.progress_bar).setVisibility(View.GONE);
    }

    private void downloadList(){
        JSONDownloader jsonDownloader = new JSONDownloader();
        jsonDownloader.setOnDownloadFinishListener(this);
        jsonDownloader.execute();
    }

    @Override
    public void onSuccess(JSONObject json) {
        StorageUtil.with(this).saveJson(json, this);
    }

    @Override
    public void onError() {

    }

    @Override
    public void onSuccess() {
        initList();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(animals != null && animals.size() > 0){
            new AnimalClickDialog(position, animals.get(position)).show();
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.button_add)
            addAnimalDialog.show();
    }

    private class AnimalClickDialog{
        private Animal animal;
        private String animalName;
        private AlertDialog alertDialog;

        public AnimalClickDialog(final int position, final Animal animal) {
            this.animal = animal;
            this.animalName = animal.getName();
            this.alertDialog = new AlertDialog.Builder(MainActivity.this)
                    .setTitle(animal.getName())
                    .setMessage(R.string.mau_diapakan_hewan_ini)
                    .setNeutralButton(R.string.lihat_detail, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(getApplicationContext(), AnimalDetailActivity.class);
                            intent.putExtra("animal", AnimalClickDialog.this.animal);
                            startActivity(intent);
                            dialog.cancel();
                        }
                    }).setNegativeButton(R.string.hapus, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            JsonUtil.with(getApplicationContext())
                                    .delete(position, MainActivity.this);
                            String snkTitle = animalName + " " + getString(R.string._telah_dipahus);
                            final String toastText = getString(R.string.tidak_ada_undo_) + " " + animalName;
                            Snackbar.make(findViewById(R.id.button_add), snkTitle, Snackbar.LENGTH_LONG)
                                    .setAction(R.string.undo, new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Toast.makeText(MainActivity.this, toastText, Toast.LENGTH_SHORT).show();
                                        }
                                    }).show();
                            dialog.cancel();
                        }
                    }).setPositiveButton(R.string.ubah, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            new AnimalEditDialog(position, animal).show();
                            dialog.cancel();
                        }
                    })
                    .create();
        }

        public void show(){
            alertDialog.show();
        }
    }

    private class AnimalEditDialog{
        private AlertDialog alertDialog;

        public AnimalEditDialog(final int position, Animal animal) {
            View addAnimalView = getLayoutInflater().inflate(R.layout.add_animal_dialog, null);
            final EditText etAdd = (EditText) addAnimalView.findViewById(R.id.edittext_add_animal);
            etAdd.setText(animal.getName());
            this.alertDialog = new AlertDialog.Builder(MainActivity.this)
                    .setTitle(R.string.ubah_data)
                    .setView(addAnimalView)
                    .setNegativeButton(R.string.batal, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    }).setPositiveButton(R.string.ubah, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String animalName = etAdd.getText().toString();
                            if(!TextUtils.isEmpty(animalName))
                                JsonUtil.with(getApplicationContext()).edit(position, animalName, MainActivity.this);
                            else
                                Toast.makeText(MainActivity.this, R.string.nama_hewan_tidak_boleh_kosong, Toast.LENGTH_SHORT).show();
                            etAdd.setText("");
                            dialog.cancel();
                        }
                    })
                    .create();
        }

        public void show(){
            alertDialog.show();
        }
    }
}

