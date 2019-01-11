package saiprojects.sai.com.sqlitedbexaple;

import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    DataBaseHelper myDb;
    EditText et_name, et_ph, et_add;
    Button btn_add, btn_view;
    LinearLayout ic_nodata;
    RecyclerView rv_data;
    ArrayList<Player> players = new ArrayList<>();
    public Dialog dialog1;
    TextView tv_rvtext;

    LinearLayoutManager listManager;
    ExemptChargesAdapter exemptChargesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dialog1 = new Dialog(MainActivity.this);
        myDb = new DataBaseHelper(this);
        et_name = findViewById(R.id.et_name);
        et_ph = findViewById(R.id.et_surname);
        et_add = findViewById(R.id.et_marks);
        btn_add = findViewById(R.id.btn_add);
        btn_view = findViewById(R.id.btn_view);
        rv_data = findViewById(R.id.rv_data);
        ic_nodata = findViewById(R.id.ic_nodata);
        tv_rvtext = findViewById(R.id.tv_rvtext);


        listManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        addData();
        viewData();

    }


    public void addData() {
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Boolean isInserted = myDb.insertData(et_name.getText().toString(),
                        et_ph.getText().toString(), et_add.getText().toString());


                if (isInserted == true) {
                    et_name.setText("");
                    et_ph.setText("");
                    et_add.setText("");
                    Toast toast = Toast.makeText(getApplicationContext(), "Data inserted Sucessfully", Toast.LENGTH_SHORT);
                    toast.show();
                    viewDataSeperate();
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(), "Data is not Inserted", Toast.LENGTH_SHORT);
                    toast.show();
                }


            }
        });
    }

    public void viewData() {
        btn_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                players.clear();
                Cursor res = myDb.getAllData();
                if (res.getCount() == 0) {

                    Toast toast = Toast.makeText(getApplicationContext(), "DataBase is Empty", Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }

                while (res.moveToNext()) {


                    Player play = new Player();
                    play.setIdd(res.getString(0));
                    play.setNamee(res.getString(1));
                    play.setPnumber(res.getString(2));
                    play.setPlace(res.getString(3));

                    players.add(play);
                    Log.i("players", "players --> " + players.toString());

                }

                if (players != null && players.size() > 0) {
                    // listManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
                    rv_data.setLayoutManager(listManager);
                    rv_data.setHasFixedSize(true);
                    exemptChargesAdapter = new ExemptChargesAdapter(players);
                    rv_data.setAdapter(exemptChargesAdapter);
                }

                Log.i("!!!", "Amb -->" + players);
                Log.i("!!!", "Amb -->" + players.size());
            }
        });
    }

    public void viewDataSeperate() {
        players.clear();
        Cursor res = myDb.getAllData();
        if (res.getCount() == 0) {

            Toast toast = Toast.makeText(getApplicationContext(), "DataBase is Empty", Toast.LENGTH_SHORT);
            toast.show();
            players.clear();
            rv_data.setLayoutManager(listManager);
            rv_data.setHasFixedSize(true);
            exemptChargesAdapter = new ExemptChargesAdapter(players);
            rv_data.setAdapter(exemptChargesAdapter);
            exemptChargesAdapter.notifyDataSetChanged();
            return;
        }

        while (res.moveToNext()) {


            Player play = new Player();
            play.setIdd(res.getString(0));
            play.setNamee(res.getString(1));
            play.setPnumber(res.getString(2));
            play.setPlace(res.getString(3));

            players.add(play);
            Log.i("players", "players --> " + players.toString());

        }

        if (players != null && players.size() > 0) {
            // listManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            rv_data.setLayoutManager(listManager);
            rv_data.setHasFixedSize(true);
            exemptChargesAdapter = new ExemptChargesAdapter(players);
            rv_data.setAdapter(exemptChargesAdapter);
            exemptChargesAdapter.notifyDataSetChanged();
        } else {
            exemptChargesAdapter.notifyDataSetChanged();
        }

        Log.i("!!!", "Amb -->" + players);
        Log.i("!!!", "Amb -->" + players.size());

    }


    public class ExemptChargesAdapter extends RecyclerView.Adapter<ExemptChargesAdapter.ViewHolder> {

        ArrayList<Player> players;

        public ExemptChargesAdapter(ArrayList<Player> players) {

            this.players = players;
            Log.i("!!!", "players -->" + players);

            if (players.size() > 0) {
                ic_nodata.setVisibility(View.GONE);
                rv_data.setVisibility(View.VISIBLE);
                tv_rvtext.setVisibility(View.VISIBLE);
            } else {
                ic_nodata.setVisibility(View.VISIBLE);
                rv_data.setVisibility(View.GONE);
                tv_rvtext.setVisibility(View.GONE);
            }

        }


        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_exempt_service_charge, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, final int i) {
            holder.rtv_name.setText(players.get(i).namee);
            holder.rtv_number.setText(players.get(i).pnumber);
            holder.rtv_place.setText(players.get(i).place);

            holder.ic_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String sname = players.get(i).namee;
                    String snum = players.get(i).pnumber;
                    String splae = players.get(i).place;

                    Integer deletedRows = myDb.deleteData(sname);

                    if (deletedRows > 0) {
                        et_name.setText("");
                        et_ph.setText("");
                        et_add.setText("");
                        exemptChargesAdapter.notifyDataSetChanged();
                        Toast.makeText(MainActivity.this, "Data Deleted", Toast.LENGTH_LONG).show();
                        viewDataSeperate();
                    } else {
                        Toast.makeText(MainActivity.this, "Data not Deleted give correct name", Toast.LENGTH_LONG).show();

                    }

                }
            });

            holder.ic_edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String sname = players.get(i).namee;
                    String snum = players.get(i).pnumber;
                    String splae = players.get(i).place;
                    String sid = players.get(i).idd;

                    initiatePopupWindow(sid, sname, snum, splae);

                }


            });

        }

        @Override
        public int getItemCount() {
            if (players.size() > 0) {
                ic_nodata.setVisibility(View.GONE);
                rv_data.setVisibility(View.VISIBLE);
                tv_rvtext.setVisibility(View.VISIBLE);
            } else {
                ic_nodata.setVisibility(View.VISIBLE);
                rv_data.setVisibility(View.GONE);
                tv_rvtext.setVisibility(View.GONE);
            }

            return players.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            TextView rtv_name, rtv_number, rtv_place;
            LinearLayout ic_delete, ic_edit;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);

                rtv_name = itemView.findViewById(R.id.rtv_name);
                rtv_number = itemView.findViewById(R.id.rtv_number);
                rtv_place = itemView.findViewById(R.id.rtv_place);

                ic_delete = itemView.findViewById(R.id.ic_delete);
                ic_edit = itemView.findViewById(R.id.ic_edit);

            }
        }
    }

    private void initiatePopupWindow(String sid, String sname, String snum, String splae) {
        LayoutInflater inflater = (LayoutInflater) MainActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        dialog1.setContentView(R.layout.popup_window);

        final TextView idvalue, texttoshow;
        final EditText et_name, et_number, et_place;
        Button btn_ok, btn_cancel;

        dialog1.show();

        btn_cancel = dialog1.findViewById(R.id.btn_cancel);
        btn_ok = dialog1.findViewById(R.id.btn_ok);
        et_place = dialog1.findViewById(R.id.place);
        et_number = dialog1.findViewById(R.id.number);
        et_name = dialog1.findViewById(R.id.name);
        idvalue = dialog1.findViewById(R.id.idvalue);
        texttoshow = dialog1.findViewById(R.id.texttoshow);

        idvalue.setText(sid);
        et_name.setText(sname);
        et_number.setText(snum);
        et_place.setText(splae);
        texttoshow.setText("Update data of " + sname);

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean isUpdate = myDb.updateData(idvalue.getText().toString(),
                        et_name.getText().toString(),
                        et_number.getText().toString(), et_place.getText().toString());
                if (isUpdate == true) {
                    exemptChargesAdapter.notifyDataSetChanged();
                    Toast.makeText(MainActivity.this, "Data Update", Toast.LENGTH_LONG).show();
                    viewDataSeperate();
                } else {
                    Toast.makeText(MainActivity.this, "Data not Updated", Toast.LENGTH_LONG).show();

                }

                dialog1.dismiss();
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog1.dismiss();
            }
        });

    }

    private class Player {
        public String idd;
        public String namee;
        public String pnumber;
        public String place;

        public String getPlace() {
            return place;
        }

        public void setPlace(String place) {
            this.place = place;
        }

        public String getIdd() {
            return idd;
        }

        public void setIdd(String idd) {
            this.idd = idd;
        }

        public String getNamee() {
            return namee;
        }

        public void setNamee(String namee) {
            this.namee = namee;
        }

        public String getPnumber() {
            return pnumber;
        }

        public void setPnumber(String pnumber) {
            this.pnumber = pnumber;
        }
    }
}