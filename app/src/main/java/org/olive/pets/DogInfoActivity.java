package org.olive.pets;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.olive.pets.DB.DogProfile;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by KMJ on 2017-04-17.
 */

// 등록된 강아지의 리스트 출력해줌
public class DogInfoActivity extends AppCompatActivity {
    private Realm mRealm;
    // 어답터
    private MyListAdapter adapter;
    private Menu menu;
    ListView listView = null;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dog_info);

        //Realm 사용을 알림
        mRealm = Realm.getDefaultInstance();

        // RealmResults are "live" views, that are automatically kept up to date, even when changes happen
        // on a background thread. The RealmBaseAdapter will automatically keep track of changes and will
        // automatically refresh when a change is detected.
        RealmResults<DogProfile> dogprofiles = mRealm.where(DogProfile.class).findAllSorted(DogProfile.DOG_ID);
        adapter = new MyListAdapter(dogprofiles);

        listView = (ListView) findViewById(R.id.lv_dog_info);
        listView.setAdapter(adapter);
        //adapter.getView(1, null, listView);

        // 아이템들 클릭 리스너
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
    }

    private class ViewHolder {

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
        menu.setGroupVisible(R.id.group_delete_mode, false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_add:
                DataHelper.addItemAsync(mRealm);
                return true;
            case R.id.action_random:
                DataHelper.randomAddItemAsync(mRealm);
                return true;
            case R.id.action_start_delete_mode:
                adapter.enableDeletionMode(true);
                menu.setGroupVisible(R.id.group_normal_mode, false);
                menu.setGroupVisible(R.id.group_delete_mode, true);
                return true;
            case R.id.action_end_delete_mode:
                DataHelper.deleteItemsAsync(mRealm, adapter.getCountersToDelete());
                // Fall through
            case R.id.action_cancel_delete_mode:
                adapter.enableDeletionMode(false);
                menu.setGroupVisible(R.id.group_normal_mode, true);
                menu.setGroupVisible(R.id.group_delete_mode, false);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
