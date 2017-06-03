package olive.Pets.Activity;

import olive.Pets.R;
import olive.Pets.DB.DogProfile;
import olive.Pets.Profile.*;
import olive.Pets.Activity.*;
import olive.Pets.Activity.Tutorial.*;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by KMJ on 2017-04-17.
 */

// 등록된 강아지의 리스트 출력해줌
public class ProfileList
        extends AppCompatActivity
{
    private Button btnDailyReport, btnMain, btnDogInfo, btnSetting;

    private Realm mRealm;
    private olive.Pets.Profile.ProfileListAdapter adapter;
    private Menu menu;
    ListView listView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dog_info);

        //**********************actionbar_start**************************//

        // 액션바 투명하게 해주기
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        // 색상넣기(투명색상 들어감)
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00ff0000")));
        // 왼쪽 화살표 버튼
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // 액션바 title 지정
        getSupportActionBar().setTitle("Pet'Droid");

        //**********************actionbar_start**************************//


        //Realm 사용을 알림
        mRealm = Realm.getDefaultInstance();

        RealmResults<DogProfile> puppies = mRealm.where(DogProfile.class).findAll();

        adapter = new ProfileListAdapter(puppies, this);

        // 어댑터를 연결
        listView = (ListView) findViewById(R.id.lv_dog_info);
        listView.setAdapter(adapter);

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
                finish();
            }
        });

        //btn_daily_report
        btnDailyReport = (Button) findViewById(R.id.btn_daily_report_di);
        btnDailyReport.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                try {
                    Intent i = new Intent(ProfileList.this, DailyReport.class);
                    startActivity(i);
                    finish();
                } catch (Exception e) {
                    Toast toast = Toast.makeText(ProfileList.this, "pie.java 연결안됨", Toast.LENGTH_SHORT);
                    toast.show();
                }

            }
        });

        //btn_dog_info
        btnDogInfo = (Button) findViewById(R.id.btn_dog_info_di);
        btnDogInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileList.this, ProfileList.class);
                finish();
                startActivity(intent);

            }
        });

        //btn_setting
        btnSetting = (Button) findViewById(R.id.btn_setting_di);
        btnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileList.this, Setting.class);
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
                // 강아지 추가 액티비티 이동 -> 튜토리얼 부분으로 이동하도록
                Intent intent = new Intent(this, InitDogProfile.class);
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
