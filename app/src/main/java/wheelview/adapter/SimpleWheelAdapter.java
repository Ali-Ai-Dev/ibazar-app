/*
 * Copyright (C) 2016 venshine.cn@gmail.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package wheelview.adapter;

import android.view.View;
import android.view.ViewGroup;

import com.tnt.ibazaar.Act_Main;

import models.Category;
import wheelview.widget.WheelItem;

/**
 * 滚轮图片和文本适配器
 *
 * @author venshine
 */
public class SimpleWheelAdapter extends BaseWheelAdapter<Category> {

    private Act_Main mAct;

    public SimpleWheelAdapter(Act_Main context) {
        mAct = context;
    }

    @Override
    public View bindView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = new WheelItem(mAct);
        }

        WheelItem item = (WheelItem) convertView;

        if (position == 0) {
//            Log.e("Qcat pos", position + "");

//            holder.img.setImageDrawable(
//                    new IconicsDrawable(mAct, GoogleMaterial.Icon.gmd_arrow_back)
//                            .color(Color.BLACK)
//                            .sizeDp(40)
//            );
            item.setImageBack();
            item.setText("");
            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    mAct.quickCategoriesBack(mList.get(0).getParent());
                }
            });
            return convertView;
        }

        item.setImage(mList.get(position-1).getImage_name());
        item.setText(mList.get(position-1).getTitle());

        item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                mAct.loadQuickCategories(mList.get(position-1).getId());
            }
        });
        return convertView;
    }

}
