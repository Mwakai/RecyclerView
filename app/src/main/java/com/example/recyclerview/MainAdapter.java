package com.example.recyclerview;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Color;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {
    //INITIALIZE VARIABLES
    Activity activity;
    ArrayList<String> arrayList;
    TextView tvEmpty;
    MainViewModel mainViewModel;
    boolean isEnable = false;
    boolean isSelectAll = false;
    ArrayList<String> selectList = new ArrayList<>();

    //CREATE CONSTRUCTOR
    public MainAdapter(Activity activity, ArrayList<String> arrayList, TextView tvEmpty){
        this.activity = activity;
        this.arrayList = arrayList;
        this.tvEmpty = tvEmpty;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //INITIALIZE VIEW
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_main,parent,false);
        //INITIALIZE VIEW MODEL
        mainViewModel = ViewModelProviders.of((FragmentActivity) activity)
                .get(MainViewModel.class);
        //RETURN VIEW

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //SET TEXT ON TEXT VIEW
        holder.textView.setText(arrayList.get(position));

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                //CHECK CONDITION
                if (!isEnable) {
                    //WHEN ACTION MODE IS NOT ENABLED
                    //INITIALIZE ACTION MODE
                    ActionMode.Callback callback = new ActionMode.Callback() {
                        @Override
                        public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
                            //INITIALIZE MENU INFLATER
                            MenuInflater menuInflater = actionMode.getMenuInflater();
                            //INFLATE MENU
                            menuInflater.inflate(R.menu.menu,menu);
                            //RETURN TRUE
                            return true;
                        }

                        @Override
                        public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
                            //WHEN ACTION MODE IS PREPARE
                            //SET isEnable TRUE
                            isEnable = true;
                            //CREATE METHOD
                            ClickItem(holder);

                            //SET OBSERVER ON GET TEXT METHOD
                            mainViewModel.getText().observe((LifecycleOwner) activity
                                    , new Observer<String>() {
                                        @Override
                                        public void onChanged(String s) {
                                            //WHEN TEXT CHANGE
                                            //SET TEXT ON ACTION MODE TITTLE
                                            actionMode.setTitle(String.format("%s Selected",s));
                                        }
                                    });
                            return true;
                        }

                        @Override
                        public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
                            //WHEN CLICK ON ACTION MOE ITEM
                            //GET ITEM ID
                            int id = menuItem.getItemId();
                            //USE SWITCH CONDITION
                            switch (id){
                                case R.id.menu_delete:
                                    /* WHEN CLICK ON DELETE
                                        USE FOR LOOP
                                     */
                                    for (String s : selectList){
                                        //REMOVE SELECTED ITEM FROM ARRAY LIST
                                        arrayList.remove(s);
                                    }
                                    //CHECK CONDITION
                                    if (arrayList.size() == 0){
                                        //WHEN ARRAY LIST IS EMPTY
                                        //VISIBLE TEXT VIEW
                                        tvEmpty.setVisibility(View.VISIBLE);
                                    }
                                    //FINISH ACTION MODE
                                    actionMode.finish();
                                    break;
                                case R.id.menu_select_all:
                                    //WHEN CLICK ON SELECT ALL
                                    //CHECK CONDITION
                                    if(selectList.size() == arrayList.size()) {
                                        //WHEN ALL ITEM SELECTED
                                        //SET isSelectAll FALSE
                                        isSelectAll = false;
                                        //CLEAR SELECT ARRAY LIST
                                        selectList.clear();
                                    }else {
                                        //WHEN ALL ITEMS ARE UNSELECTED
                                        //SET isSelectAll TRUE
                                        isSelectAll = true;
                                        //CLEAR SELECT ARRAY LIST
                                        selectList.clear();
                                        //ADD ALL VALUE IN SELECT ARRAY
                                        selectList.addAll(arrayList);
                                    }

                                    //SET TEXT ON VIEW MODEL
                                    mainViewModel.setText(String.valueOf(selectList.size()));
                                    //NOTIFY ADAPTER
                                    notifyDataSetChanged();
                                    break;
                            }
                            return true;
                        }

                        @Override
                        public void onDestroyActionMode(ActionMode mode) {
                            //WHEN ACTION MODE IS DESTROY
                            //SET isEnable FALSE
                            isEnable = false;
                            //SET isSelectAll FALSE
                            isSelectAll = false;
                            //CLEAR SELECT ARRAY LIST
                            selectList.clear();
                            //NOTIFY ADAPTER
                            notifyDataSetChanged();

                        }
                    };
                    //START ACTION MODE
                    ((AppCompatActivity) view.getContext()).startActionMode(callback);
                }else {
                    //WHEN ACTION MODE IS ALREADY ENABLE
                    //CALL METHOD
                    ClickItem(holder);
                }
                return true;
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //CHECK CONDITION
                if(isEnable) {
                    //WHEN ACTION MODE IS ENABLE
                    //CALL METHOD
                    ClickItem(holder);
                }else {
                    //WHEN ACTION MODE IS NOT ENABLE
                    //DISPLAY TOAST
                    Toast.makeText(activity, "You Clicked" + arrayList.get(holder.getAdapterPosition()),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        //CHECK CONDITION
        if (isSelectAll){
            //WHEN ALL VALUES ARE SELECTED
            //VISIBLE ALL CHECK BOX IMAGE
            holder.ivCheckBox.setVisibility(View.VISIBLE);
            //SET BACKGROUND COLOR
            holder.itemView.setBackgroundColor(Color.LTGRAY);
        }else {
            //WHEN ALL VALUES ARE UNSELECTED
            //HIDE ALL CHECK BOX IMAGE
            holder.ivCheckBox.setVisibility(View.GONE);
            //SET BACKGROUND COLOR
            holder.itemView.setBackgroundColor(Color.TRANSPARENT);

        }

    }

    private void ClickItem(ViewHolder holder) {
        //GET SELECTED ITEM VALUE
        String s =arrayList.get(holder.getAdapterPosition());
        //CHECK CONDITION
        if (holder.ivCheckBox.getVisibility() == View.GONE){
            //WHEN ITEM IS NOT SELECTED
            //VISIBLE CHECK BOX IMAGE
            holder.ivCheckBox.setVisibility(View.VISIBLE);
            //SET BACKGROUND COLOR
            holder.itemView.setBackgroundColor(Color.LTGRAY);
            //ADD VALUES IN SELECT ARRAY LIST
            selectList.add(s);
        }else {
            //WHEN ITEM SELECTED
            //HIDE CHECKBOX IMAGE
            holder.ivCheckBox.setVisibility(View.GONE);
            //SET BACKGROUND COLOR
            holder.itemView.setBackgroundColor(Color.TRANSPARENT);
            //REMOVE VALUE FROM SELECT ARRAY LIST
            selectList.remove(s);

        }
        //SET TEXT ON VIEW MODEL
        mainViewModel.setText(String.valueOf(selectList.size()));
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        //INITIALIZE VARIABLE
        TextView textView;
        ImageView ivCheckBox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            //ASSIGN VARIABLES
            textView = itemView.findViewById(R.id.text_view);
            ivCheckBox = itemView.findViewById(R.id.iv_check_box);
        }
    }
}
