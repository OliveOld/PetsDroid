package org.olive.pets;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import org.olive.pets.DB.DogProfile;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import io.realm.OrderedRealmCollection;
import io.realm.RealmBaseAdapter;

/**
 * Created by KMJ on 2017-04-17.
 */

public class MyListAdapter extends RealmBaseAdapter<DogProfile> implements ListAdapter {

    // 리스트 뷰에 들어갈 아이템들
    private static class ViewHolder {
        TextView tvDogName, tvDogInfo;
        ImageView ivDogImage;
        CheckBox cbDogDelete;
        Button btnDogEdit;
    }

    // 지우기 모드 : default = false
    private boolean inDeletionMode = false;
    private Set<Integer> countersToDelete = new HashSet<Integer>();

    // 수정 모드: default=false
    private boolean inEditionMode = false;

    MyListAdapter(OrderedRealmCollection<DogProfile> realmResults) {
        super(realmResults);
    }

    void enableDeletionMode(boolean enabled) {
        inDeletionMode = enabled;
        if (!enabled) {
            countersToDelete.clear();
        }
        notifyDataSetChanged();
    }

    void enableEditionMode(boolean enabled) {
        inEditionMode = enabled;
    }

    Set<Integer> getCountersToDelete() {
        return countersToDelete;
    }

    @Override
    // position 리스트뷰 cache 하는 부분
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_dog_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.tvDogName = (TextView) convertView.findViewById(R.id.tv_dog_name);
            viewHolder.tvDogInfo = (TextView) convertView.findViewById(R.id.tv_dog_info);
            viewHolder.ivDogImage = (ImageView) convertView.findViewById(R.id.iv_dog_image);
            viewHolder.cbDogDelete = (CheckBox) convertView.findViewById(R.id.cb_dog_delete);
            viewHolder.btnDogEdit = (Button) convertView.findViewById(R.id.btn_dog_edit);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (adapterData != null) {
            final DogProfile item = adapterData.get(position);

            viewHolder.tvDogName.setText(item.getDogName());
            viewHolder.tvDogInfo.setText(item.getDogSex() + "의 " +item.getDogAge() + "살 강아지");

            File imgFile = new  File(item.getDogPhoto());

            if(imgFile.exists()){
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                viewHolder.ivDogImage.setImageBitmap(myBitmap);
            }

            if (inDeletionMode) {
                // 삭제 모드 체크박스에 체크했을 경우 리스너 지정
                viewHolder.cbDogDelete.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        countersToDelete.add(item.getDogId());
                    }
                });
            } else {
                viewHolder.cbDogDelete.setOnCheckedChangeListener(null);
            }

            viewHolder.cbDogDelete.setChecked(countersToDelete.contains(item.getDogId()));
            viewHolder.cbDogDelete.setVisibility(inDeletionMode ? View.VISIBLE : View.GONE);

            viewHolder.btnDogEdit.setVisibility(inEditionMode ? View.VISIBLE : View.GONE);
        }
        return convertView;
    }

}
