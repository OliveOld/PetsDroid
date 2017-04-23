package org.olive.pets.Profile;

import android.content.Context;
import android.content.Intent;
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
import android.widget.Toast;

import org.olive.pets.DB.DogProfile;
import org.olive.pets.R;

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

    // 상위 클래스의 컨텍스트 받아오기
    Context m_Context;

    // 지우기 모드 : default = false
    private boolean inDeletionMode = false;
    private Set<Integer> countersToDelete = new HashSet<Integer>();

    // 수정 모드: default=false
    private boolean inEditionMode = true;

    MyListAdapter(OrderedRealmCollection<DogProfile> realmResults, Context ctx) {
        super(realmResults);
        // 해당 어댑터 호출하는 activity의 context 얻어옴
        m_Context=ctx;
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

            // 수정모드일 시 -> 버튼 클릭 -> 수정 페이지로 이동
            if (inEditionMode) {
                viewHolder.btnDogEdit.setOnClickListener(new View.OnClickListener(){

                   @Override
                   public void onClick(View v) {
                       // activity 이동 시 해당 아이템의 id를 넘겨주어야 한다.\

                       Intent intent = new Intent(m_Context, DogProfileEditActivity.class);
                       //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                       intent.putExtra("DOG_ID", item.getDogId());
                       Toast toast = Toast.makeText(m_Context, "넘겨줄 id:"+item.getDogId(), Toast.LENGTH_SHORT);
                       m_Context.startActivity(intent);
                   }
                });
            }

            viewHolder.cbDogDelete.setChecked(countersToDelete.contains(item.getDogId()));
            viewHolder.cbDogDelete.setVisibility(inDeletionMode ? View.VISIBLE : View.GONE);

            viewHolder.btnDogEdit.setVisibility(inEditionMode ? View.VISIBLE : View.GONE);
        }
        return convertView;
    }

}
