package org.olive.pets;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import org.olive.pets.DB.DogProfile;

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
    }

    // 지우기 모드 : default = false
    private boolean inDeletionMode = false;
    private Set<Integer> countersToDelete = new HashSet<Integer>();

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

    Set<Integer> getCountersToDelete() {
        return countersToDelete;
    }

    @Override
    // position 리스트뷰 순서 Converview 한 아이템의 화면,
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_dog_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.tvDogName = (TextView) convertView.findViewById(R.id.tv_dog_name);
            viewHolder.tvDogInfo = (TextView) convertView.findViewById(R.id.tv_dog_info);
            viewHolder.ivDogImage = (ImageView) convertView.findViewById(R.id.iv_dog_image);
            viewHolder.cbDogDelete = (CheckBox) convertView.findViewById(R.id.cb_dog_delete);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (adapterData != null) {
            final DogProfile item = adapterData.get(position);
            //viewHolder.countText.setText(item.getCountString());
            if (inDeletionMode) {
                // 체크박스에 체크하면
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
        }
        return convertView;
    }

}
