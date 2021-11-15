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
package wheelview.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.squareup.picasso.Picasso;
import com.tnt.ibazaar.R;

import java.util.zip.Inflater;

import wheelview.common.WheelConstants;

/**
 * 滚轮Item布局，包含图片和文本
 *
 * @author venshine
 */
public class WheelItem extends FrameLayout {

    private ImageView mImage;
    private TextView mText;

    public WheelItem(Context context) {
        super(context);
        init();
    }

    public WheelItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public WheelItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /**
     * 初始化
     */
    private void init() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.item_shop_cat_quick, null);

        mText = (TextView) view.findViewById(R.id.txt_title);
        mImage = (ImageView) view.findViewById(R.id.img);
        addView(view);
//        LinearLayout layout = new LinearLayout(getContext());
//        LayoutParams layoutParams = new LayoutParams(200, ViewGroup.LayoutParams.WRAP_CONTENT);
//        layout.setOrientation(LinearLayout.VERTICAL);
//        layout.setTag(WheelConstants.WHEEL_LAYOUT_TAG);
//        layout.setGravity(Gravity.CENTER);
//        layoutParams.topMargin = WheelConstants.WHEEL_ITEM_MARGIN;
//
//
//        // 图片
//        mImage = new ImageView(getContext());
//        mImage.setTag(WheelConstants.WHEEL_ITEM_IMAGE_TAG);
//        mImage.setVisibility(View.VISIBLE);
//        mImage.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
//        LayoutParams imageParams =
//                new LayoutParams(getResources().getDimensionPixelOffset(R.dimen.height_item),
//                        getResources().getDimensionPixelOffset(R.dimen.width_item));
//        layout.addView(mImage, imageParams);
//
//        // 文本
//        mText = new TextView(getContext());
//        mText.setTag(WheelConstants.WHEEL_ITEM_TEXT_TAG);
//        mText.setEllipsize(TextUtils.TruncateAt.END);
//        mText.setSingleLine();
//        mText.setIncludeFontPadding(false);
//        mText.setGravity(Gravity.CENTER);
//
//        mText.setTypeface(null, Typeface.NORMAL);
//        mText.setTextColor(Color.BLUE);
//        LayoutParams textParams = new LayoutParams(LayoutParams.MATCH_PARENT,
//                LayoutParams.WRAP_CONTENT);
//        textParams.topMargin = WheelConstants.WHEEL_ITEM_MARGIN2;
//        layout.addView(mText, textParams);
//
//        addView(layout, layoutParams);
    }

    /**
     * 设置文本
     *
     * @param text
     */
    public void setText(CharSequence text) {
        mText.setText(text);
    }

    /**
     * image url
     *
     * @param url
     */
    public void setImage(String url) {
        mImage.setVisibility(View.VISIBLE);
        Picasso.with(getContext()).load(url).into(mImage);
    }

    public void setImageBack() {
        mImage.setVisibility(View.VISIBLE);
        mImage.setImageDrawable(
                new IconicsDrawable(getContext(), GoogleMaterial.Icon.gmd_arrow_back)
                        .color(Color.BLACK)
                        .sizeDp(40)
        );
    }


}
