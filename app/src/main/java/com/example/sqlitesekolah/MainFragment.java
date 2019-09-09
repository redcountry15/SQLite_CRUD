package com.example.sqlitesekolah;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MainFragment  extends Fragment implements
        View.OnClickListener,RecyclerViewAdapter.OnUserClickListener
{
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    EditText edtname,edtage;
    Button btnSubmit;
    Context context;
    List<Person> listPersonInfo;
    public MainFragment(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return  inflater.inflate(R.layout.fragment_main,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = getActivity();
        recyclerView = view.findViewById(R.id.recyclerview);
        layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);

        edtage = view.findViewById(R.id.edtAge);
        edtname = view.findViewById(R.id.edtName);
        btnSubmit =view.findViewById(R.id.btnSubmit);

        btnSubmit.setOnClickListener(this);
        setupRecyclerView();
    }

    private void setupRecyclerView(){
        DatabaseHelper db = new DatabaseHelper(context);
        listPersonInfo =db.selectedUserData();

        RecyclerViewAdapter adapt = new RecyclerViewAdapter(context,listPersonInfo,this) ;
        recyclerView.setAdapter(adapt);
        adapt.notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnSubmit){
            DatabaseHelper db = new DatabaseHelper(context);
            Person currentPerson = new Person();
            String btnStatus = btnSubmit.getText().toString();

            if (btnStatus.equals("Submit")){
                currentPerson.setName(edtname.getText().toString());
                currentPerson.setAge(Integer.parseInt(edtage.getText().toString()));
                db.insert(currentPerson);
            }
           if (btnStatus.equals("Update")){
               currentPerson.setName(edtname.getText().toString());
               currentPerson.setAge(Integer.parseInt(edtage.getText().toString()));
               db.update(currentPerson);
           }
           setupRecyclerView();
           edtname.setText("");
           edtage.setText("");
           edtname.setFocusable(true);
           btnSubmit.setText("Submit");

        }

    }

    @Override
    public void onUserClick(Person currentPerson, String Action) {
        if (Action.equals("Edit")){
            edtname.setText(currentPerson.getName());
            edtname.setFocusable(true);
            edtage.setText(currentPerson.getAge()+"");
            btnSubmit.setText("Update");
            
        }

        if (Action.equals("Delete")){
            DatabaseHelper db = new DatabaseHelper(context);
            db.delete(currentPerson.getName());
            setupRecyclerView();
        }
    }
}
