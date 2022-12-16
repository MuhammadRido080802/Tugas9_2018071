package com.example.a2018071_tugas3prak;

import androidx.appcompat.app.AppCompatActivity;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import com.example.a2018071_tugas3prak.databinding.ActivityMainBinding;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
public class MainActivity extends AppCompatActivity implements
        View.OnClickListener{
    //declaration variable
    private ActivityMainBinding binding;
    String index;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setup view binding
        binding =
                ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.fetchButton.setOnClickListener(this);
    }
    //onclik button fetch
    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.fetch_button){
            index = binding.inputId.getText().toString();
            try {
                getData();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
    }
    //get data using api link
    public void getData() throws MalformedURLException {
        Uri uri = Uri.parse("https://www.thecocktaildb.com/api/json/v1/1/search.php?s=margarita")
                .buildUpon().build();
        URL url = new URL(uri.toString());
        new DOTask().execute(url);
    }
    class DOTask extends AsyncTask<URL, Void, String>{
        //connection request
        @Override
        protected String doInBackground(URL... urls) {
            URL url = urls [0];
            String data = null;
            try {
                data = NetworkUtlis.makeHTTPRequest(url);
            }catch (IOException e){
                e.printStackTrace();
            }
            return data;
        }
        @Override
        protected void onPostExecute(String s){
            try {
                parseJson(s);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        //get data json
        public void parseJson(String data) throws JSONException{
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(data);
            }catch (JSONException e){
                e.printStackTrace();
            }

            JSONArray cityArray = jsonObject.getJSONArray("drinks");
            for (int i =0; i <cityArray.length(); i++){
                JSONObject obj = cityArray.getJSONObject(i);
                String Sobj = obj.get("idDrink").toString();
                if (Sobj.equals(index)){
                    String id = obj.get("idDrink").toString();
                    binding.resultId.setText(id);
                    String name = obj.get("strDrink").toString();
                    binding.resultName.setText(name);
                    String date = obj.get("dateModified").toString();
                    binding.resultDate.setText(date);
                    String category = obj.get("strCategory").toString();
                    binding.resultCategory.setText(category);
                    String intructions = obj.get("strInstructions").toString();
                    binding.resultIntructions.setText(intructions);
                    String measure1 = obj.get("strMeasure1").toString();
                    binding.resultMeasure1.setText(measure1);
                    String imageattribution = obj.get("strImageAttribution").toString();
                    binding.resultAttribution.setText(imageattribution);
                    String imagesource = obj.get("strImageSource").toString();
                    binding.resultImage.setText(imagesource);
                    String drinkthumb = obj.get("strDrinkThumb").toString();
                    binding.resultDrinkthumb.setText(drinkthumb);
                    break;
                }
                else{
                    binding.resultName.setText("Not Found");
                }
            }
        }
    }
}

//package com.example.a2018071_tugas3prak;
//import static com.example.a2018071_tugas3prak.DBmain.TABLENAME;
//import androidx.core.content.ContextCompat;
//import androidx.annotation.Nullable;
//import android.Manifest;
//import android.content.ContentValues;
//import android.content.pm.PackageManager;
//import android.database.sqlite.SQLiteDatabase;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.graphics.drawable.BitmapDrawable;
//import android.net.Uri;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.RequiresApi;
//import androidx.appcompat.app.ActionBarDrawerToggle;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.drawerlayout.widget.DrawerLayout;
//import androidx.recyclerview.widget.RecyclerView;
//
//import android.app.AlarmManager;
//import android.app.NotificationChannel;
//import android.app.NotificationManager;
//import android.app.PendingIntent;
//import android.content.Context;
//import android.content.Intent;
//import android.os.Build;
//import android.os.Bundle;
//import android.view.MenuItem;
//import android.view.View;
//import android.widget.Toast;
//import com.example.a2018071_tugas3prak.databinding.ActivityMainBinding;
//import com.google.android.material.navigation.NavigationView;
//import com.google.android.material.timepicker.MaterialTimePicker;
//import com.google.android.material.timepicker.TimeFormat;
//import java.util.Calendar;
//
//public class MainActivity extends AppCompatActivity {
//    private ActivityMainBinding binding;
//    private MaterialTimePicker picker;
//    private Calendar calendar;
//    private AlarmManager alarmManager;
//    private PendingIntent pendingIntent;
//    private DrawerLayout dl;
//    private ActionBarDrawerToggle abdt;
//
//    RecyclerView recylerView;
//
//    String s1[], s2[],s3[];
//    int images[] = {R.drawable.rolex,R.drawable.gucci,R.drawable.guess,R.drawable.gshock};
//
//    @RequiresApi(api = Build.VERSION_CODES.O)
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        //mengaktifkan view binding
//        binding = ActivityMainBinding.inflate(getLayoutInflater());
//        setContentView(binding.getRoot());
//        createNotificationChannel();
//        //Action Bar
//        dl = (DrawerLayout)findViewById(R.id.dl);
//        abdt = new ActionBarDrawerToggle(this,dl,R.string.Open,R.string.Close);
//        abdt.setDrawerIndicatorEnabled(true);
//
//        dl.addDrawerListener(abdt);
//        abdt.syncState();
//
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        NavigationView nav_view = (NavigationView)findViewById(R.id.nav_view);
//        nav_view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                int id = item.getItemId();
//                if (id == R.id.nav_message){
//                    Intent a = new Intent(MainActivity.this, MainActivity.class);
//                    startActivity(a);
//                }else if (id == R.id.nav_chat){
//                    Intent a = new Intent(MainActivity.this, DestinationActivity.class);
//                    startActivity(a);
//                }
//                else if (id == R.id.nav_profile){
//                    Intent a = new Intent(MainActivity.this, Biodataa.class);
//                    startActivity(a);
//                    //bertanya
//                } else if (id == R.id.nav_sell) {
//                    Intent a = new Intent(MainActivity.this, Datamenu.class);
//                    startActivity(a);
//                }
//                return true;
//            }
//        });
//        //button Alarm Manager
//        binding.selectedTimeBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                showTimePicker();
//            }
//        });
//
//        binding.setAlarmBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                setAlarm();
//            }
//        });
//
//        binding.cancelAlarmBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                cancelAlarm();
//            }
//        });
//    }
//    //action Bar
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        return abdt.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
//    }
//    //button cancel
//    private void cancelAlarm() {
//        //untuk menggagalkan alarm yang sudah disetel
//        Intent intent = new Intent(this, AlarmReceiver.class);
//        pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
//
//        if (alarmManager == null) {
//            alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//        }
//        alarmManager.cancel(pendingIntent);
//        Toast.makeText(this, "Alarm Cancelled", Toast.LENGTH_SHORT).show();
//    }
//
//    private void setAlarm() {
//        //untuk menjalankan alarm yang sudah disetel
//        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//        Intent intent = new Intent(this, AlarmReceiver.class);
//        pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
//
//        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
//                AlarmManager.INTERVAL_DAY, pendingIntent);
//        Toast.makeText(this, "Alarm Set Successfully", Toast.LENGTH_SHORT).show();
//
//
//    }
//
//    private void showTimePicker() {
//        //memunculkan dialog timepicker menggunakan library dari android
//        picker = new MaterialTimePicker.Builder()
//                .setTimeFormat(TimeFormat.CLOCK_24H)
//                .setHour(12)
//                .setMinute(0)
//                .setTitleText("Select Alarm Time")
//                .build();
//        picker.show(getSupportFragmentManager(), "AlarmManager");
//
//        //mengeset waktu didalam view
//        picker.addOnPositiveButtonClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (picker.getHour() > 12) {
//                    binding.selectedTime.setText(
//                            // String.format("%02d", (picker.getHour())+" : "+String.format("%02d",picker.getMinute())+" PM")
//                            String.format("%02d : %02d", picker.getHour(), picker.getMinute())
//                    );
//                } else {
//                    binding.selectedTime.setText(picker.getHour() + " : " + picker.getMinute() + " ");
//                }
//                //menangkap inputan jam kalian lalu memulai alarm
//                calendar = Calendar.getInstance();
//                calendar.set(Calendar.HOUR_OF_DAY, picker.getHour());
//                calendar.set(Calendar.MINUTE, picker.getMinute());
//                calendar.set(Calendar.SECOND, 0);
//                calendar.set(Calendar.MILLISECOND, 0);
//            }
//        });
//    }
//
//    @RequiresApi(api = Build.VERSION_CODES.O)
//    private void createNotificationChannel() {
//        //mendeskripsikan channel notifikasi yang akan dibangun
//        CharSequence name = "INI ALARM MANAGER";
//        String description = "PRAKTIKUM BAB5 TENTANG ALARM MANAGER";
//
//        //tingkat importance = high ( penting sekali )
//        int importance = NotificationManager.IMPORTANCE_HIGH;
//        NotificationChannel channel = new NotificationChannel("AlarmManager", name, importance);
//        channel.setDescription(description);
//
//        //membuka izin pengaturan dari aplikasi untuk memulai service notifikasi
//        NotificationManager notificationManager = getSystemService(NotificationManager.class);
//        notificationManager.createNotificationChannel(channel);
//    }
//}
