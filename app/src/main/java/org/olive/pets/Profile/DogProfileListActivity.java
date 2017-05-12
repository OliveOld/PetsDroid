package org.olive.pets.Profile;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.olive.pets.DB.DogProfile;
import org.olive.pets.DailyReportActivity;
import org.olive.pets.MainActivity;
import org.olive.pets.ManagerInfoActivity;
import org.olive.pets.R;
import org.olive.pets.Tutorial.InitDogProfileActivity;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by KMJ on 2017-04-17.
 */

// 등록된 강아지의 리스트 출력해줌
public class DogProfileListActivity extends AppCompatActivity {
    private Button btnDailyReport, btnMain, btnDogInfo, btnSetting;

    private Realm mRealm;
    private MyListAdapter adapter;
    private Menu menu;
    ListView listView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dog_info);

        //Realm 사용을 알림
        mRealm = Realm.getDefaultInstance();

        RealmResults<DogProfile> puppies = mRealm.where(DogProfile.class).findAll();

        adapter = new MyListAdapter(puppies, this);

        // 어댑터를 연결
        listView = (ListView) findViewById(R.id.lv_dog_info);
        listView.setAdapter(adapter);
        //adapter.getView(1, null, listView);

        // 아이템들 클릭 리스너 - 롱 클릭: 삭제
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            // 길게 클릭 할 경우
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                DogProfile dogprofile = adapter.getItem(i);
                if (dogprofile == null) {
                    return true;
                }

                // 해당 아이디
                final int id = dogprofile.getDogId();
                mRealm.executeTransactionAsync(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        realm.where(DogProfile.class).equalTo(DogProfile.DOG_ID, id).findAll().deleteAllFromRealm();
                    }
                });
                return true;
            }
        });

        //btn_main
        btnMain = (Button) findViewById(R.id.btn_main_di);
        btnMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(DogProfileListActivity.this, MainActivity.class);
                Toast toast = Toast.makeText(DogProfileListActivity.this, "액티비티 넘어간다", Toast.LENGTH_SHORT);
                toast.show();
               // startActivity(intent);
                finish();
            }
        });

        //btn_daily_report
        btnDailyReport = (Button) findViewById(R.id.btn_daily_report_di);
        btnDailyReport.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                try {
                    Intent i = new Intent(DogProfileListActivity.this, DailyReportActivity.class);
                    startActivity(i);
                    finish();
                    // Toast toast = Toast.makeText(MainActivity.this, "pie.java 연결성공", Toast.LENGTH_SHORT);
                    // toast.show();
                } catch (Exception e) {
                    Toast toast = Toast.makeText(DogProfileListActivity.this, "pie.java 연결안됨", Toast.LENGTH_SHORT);
                    toast.show();
                }

            }
        });

        //btn_dog_info
        btnDogInfo = (Button) findViewById(R.id.btn_dog_info_di);
        btnDogInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DogProfileListActivity.this, DogProfileListActivity.class);
                finish();
                startActivity(intent);

            }
        });

        //btn_setting
        btnSetting = (Button) findViewById(R.id.btn_setting_di);
        btnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DogProfileListActivity.this, ManagerInfoActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRealm.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        getMenuInflater().inflate(R.menu.menu_dog_info, menu);
        menu.setGroupVisible(R.id.group_normal_mode, true);
        menu.setGroupVisible(R.id.group_edit_mode, false);
        menu.setGroupVisible(R.id.group_delete_mode, false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_add:
                Intent intent = new Intent(this, DogProfileAddActivity.class);
                startActivity(intent);
                //DataHelper.addItemAsync(mRealm);
                return true;
            case R.id.action_start_edit_mode:
                adapter.enableEditionMode(true);
                menu.setGroupVisible(R.id.group_normal_mode, false);
                menu.setGroupVisible(R.id.group_edit_mode, true);
                menu.setGroupVisible(R.id.group_delete_mode, false);
                return true;
            case R.id.action_end_edit_mode:
                //DogInfoEditActivity로..
                return true;
            case R.id.action_cancel_edit_mode:
                adapter.enableEditionMode(false);
                menu.setGroupVisible(R.id.group_normal_mode, true);
                menu.setGroupVisible(R.id.group_edit_mode, false);
                menu.setGroupVisible(R.id.group_delete_mode, false);
                return true;
            case R.id.action_start_delete_mode:
                adapter.enableDeletionMode(true);
                menu.setGroupVisible(R.id.group_normal_mode, false);
                menu.setGroupVisible(R.id.group_edit_mode, false);
                menu.setGroupVisible(R.id.group_delete_mode, true);
                return true;
            case R.id.action_end_delete_mode:
                // 강아지 프로필 삭제
                DataHelper.deleteItemsAsync(mRealm, adapter.getCountersToDelete());
                // Fall through
            case R.id.action_cancel_delete_mode:
                adapter.enableDeletionMode(false);
                menu.setGroupVisible(R.id.group_normal_mode, true);
                menu.setGroupVisible(R.id.group_edit_mode, false);
                menu.setGroupVisible(R.id.group_delete_mode, false);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}