package com.luseen.spacenavigation;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.PorterDuff.Mode;
import android.graphics.Typeface;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SpaceNavigationView extends RelativeLayout {
    private static final String BADGE_FULL_TEXT_KEY = "badgeFullTextKey";
    private static final String BUDGES_ITEM_BUNDLE_KEY = "budgeItem";
    private static final String CENTRE_BUTTON_COLOR_KEY = "centreButtonColorKey";
    private static final String CENTRE_BUTTON_ICON_KEY = "centreButtonIconKey";
    private static final String CHANGED_ICON_AND_TEXT_BUNDLE_KEY = "changedIconAndText";
    private static final String CURRENT_SELECTED_ITEM_BUNDLE_KEY = "currentItem";
    private static final int MAX_SPACE_ITEM_SIZE = 4;
    private static final int MIN_SPACE_ITEM_SIZE = 2;
    private static final int NOT_DEFINED = -777;
    private static final String SPACE_BACKGROUND_COLOR_KEY = "backgroundColorKey";
    private static final String TAG = "SpaceNavigationView";
    private int activeCentreButtonBackgroundColor;
    private int activeCentreButtonIconColor;
    private int activeSpaceItemColor;
    private List<RelativeLayout> badgeList;
    private HashMap<Integer, Object> badgeSaveInstanceHashMap;
    private RelativeLayout centreBackgroundView;
    public CentreButton centreButton;
    private int centreButtonColor;
    private int centreButtonIcon;
    private int centreButtonRippleColor;
    private final int centreButtonSize;
    private BezierView centreContent;
    private final int centreContentWight;
    private HashMap<Integer, SpaceItem> changedItemAndIconHashMap;
    private int contentWidth;
    private Context context;
    private int currentSelectedItem;
    private Typeface customFont;
    private int inActiveCentreButtonIconColor;
    private int inActiveSpaceItemColor;
    private boolean isCentreButtonIconColorFilterEnabled;
    private boolean isCentreButtonSelectable;
    private boolean isCustomFont;
    private boolean isIconOnlyMode;
    private boolean isTextOnlyMode;
    private LinearLayout leftContent;
    private final int mainContentHeight;
    private LinearLayout rightContent;
    private Bundle savedInstanceState;
    private boolean shouldShowBadgeWithNinePlus;
    private int spaceBackgroundColor;
    private int spaceItemIconOnlySize;
    private int spaceItemIconSize;
    private List<View> spaceItemList;
    private int spaceItemTextSize;
    private List<SpaceItem> spaceItems;
    private final int spaceNavigationHeight;
    private SpaceOnClickListener spaceOnClickListener;
    private SpaceOnLongClickListener spaceOnLongClickListener;

    class C03631 implements OnClickListener {
        C03631() {
        }

        public void onClick(View view) {
            if (SpaceNavigationView.this.spaceOnClickListener != null) {
                SpaceNavigationView.this.spaceOnClickListener.onCentreButtonClick();
            }
            if (SpaceNavigationView.this.isCentreButtonSelectable) {
                SpaceNavigationView.this.updateSpaceItems(-1);
            }
        }
    }

    class C03642 implements OnLongClickListener {
        C03642() {
        }

        public boolean onLongClick(View v) {
            if (SpaceNavigationView.this.spaceOnLongClickListener != null) {
                SpaceNavigationView.this.spaceOnLongClickListener.onCentreButtonLongClick();
            }
            return true;
        }
    }

    class C03675 implements Runnable {
        C03675() {
        }

        public void run() {
            SpaceNavigationView.this.requestLayout();
        }
    }

    public SpaceNavigationView(Context context) {
        this(context, null);
    }

    public SpaceNavigationView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SpaceNavigationView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.spaceNavigationHeight = (int) getResources().getDimension(R.dimen.space_navigation_height);
        this.mainContentHeight = (int) getResources().getDimension(R.dimen.main_content_height);
        this.centreContentWight = (int) getResources().getDimension(R.dimen.centre_content_width);
        this.centreButtonSize = (int) getResources().getDimension(R.dimen.space_centre_button_default_size);
        this.spaceItems = new ArrayList();
        this.spaceItemList = new ArrayList();
        this.badgeList = new ArrayList();
        this.badgeSaveInstanceHashMap = new HashMap();
        this.changedItemAndIconHashMap = new HashMap();
        this.spaceItemIconSize = NOT_DEFINED;
        this.spaceItemIconOnlySize = NOT_DEFINED;
        this.spaceItemTextSize = NOT_DEFINED;
        this.spaceBackgroundColor = NOT_DEFINED;
        this.centreButtonColor = NOT_DEFINED;
        this.activeCentreButtonIconColor = NOT_DEFINED;
        this.inActiveCentreButtonIconColor = NOT_DEFINED;
        this.activeCentreButtonBackgroundColor = NOT_DEFINED;
        this.centreButtonIcon = NOT_DEFINED;
        this.activeSpaceItemColor = NOT_DEFINED;
        this.inActiveSpaceItemColor = NOT_DEFINED;
        this.centreButtonRippleColor = NOT_DEFINED;
        this.currentSelectedItem = -1;
        this.isCentreButtonSelectable = true;
        this.isTextOnlyMode = false;
        this.isIconOnlyMode = false;
        this.isCustomFont = false;
        this.isCentreButtonIconColorFilterEnabled = true;
        this.shouldShowBadgeWithNinePlus = true;
        this.context = context;
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        if (attrs != null) {
            Resources resources = getResources();
            TypedArray typedArray = this.context.obtainStyledAttributes(attrs, R.styleable.SpaceNavigationView);
            this.spaceItemIconSize = typedArray.getDimensionPixelSize(R.styleable.SpaceNavigationView_space_item_icon_size, resources.getDimensionPixelSize(R.dimen.space_item_icon_default_size));
            this.spaceItemIconOnlySize = typedArray.getDimensionPixelSize(R.styleable.SpaceNavigationView_space_item_icon_only_size, resources.getDimensionPixelSize(R.dimen.space_item_icon_only_size));
            this.spaceItemTextSize = typedArray.getDimensionPixelSize(R.styleable.SpaceNavigationView_space_item_text_size, resources.getDimensionPixelSize(R.dimen.space_item_text_default_size));
            this.spaceItemIconOnlySize = typedArray.getDimensionPixelSize(R.styleable.SpaceNavigationView_space_item_icon_only_size, resources.getDimensionPixelSize(R.dimen.space_item_icon_only_size));
            this.spaceBackgroundColor = typedArray.getColor(R.styleable.SpaceNavigationView_space_background_color, resources.getColor(R.color.space_default_color));
            this.centreButtonColor = typedArray.getColor(R.styleable.SpaceNavigationView_centre_button_color, resources.getColor(R.color.centre_button_color));
            this.activeSpaceItemColor = typedArray.getColor(R.styleable.SpaceNavigationView_active_item_color, resources.getColor(R.color.space_white));
            this.inActiveSpaceItemColor = typedArray.getColor(R.styleable.SpaceNavigationView_inactive_item_color, resources.getColor(R.color.default_inactive_item_color));
            this.centreButtonIcon = typedArray.getResourceId(R.styleable.SpaceNavigationView_centre_button_icon, R.drawable.near_me);
            this.activeCentreButtonIconColor = typedArray.getColor(R.styleable.SpaceNavigationView_active_centre_button_icon_color, resources.getColor(R.color.space_white));
            this.inActiveCentreButtonIconColor = typedArray.getColor(R.styleable.SpaceNavigationView_inactive_centre_button_icon_color, resources.getColor(R.color.default_inactive_item_color));
            this.activeCentreButtonBackgroundColor = typedArray.getColor(R.styleable.SpaceNavigationView_active_centre_button_background_color, resources.getColor(R.color.centre_button_color));
            typedArray.recycle();
        }
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (this.spaceBackgroundColor == NOT_DEFINED) {
            this.spaceBackgroundColor = ContextCompat.getColor(this.context, R.color.space_default_color);
        }
        if (this.centreButtonColor == NOT_DEFINED) {
            this.centreButtonColor = ContextCompat.getColor(this.context, R.color.centre_button_color);
        }
        if (this.centreButtonIcon == NOT_DEFINED) {
            this.centreButtonIcon = R.drawable.near_me;
        }
        if (this.activeSpaceItemColor == NOT_DEFINED) {
            this.activeSpaceItemColor = ContextCompat.getColor(this.context, R.color.space_white);
        }
        if (this.inActiveSpaceItemColor == NOT_DEFINED) {
            this.inActiveSpaceItemColor = ContextCompat.getColor(this.context, R.color.default_inactive_item_color);
        }
        if (this.spaceItemTextSize == NOT_DEFINED) {
            this.spaceItemTextSize = (int) getResources().getDimension(R.dimen.space_item_text_default_size);
        }
        if (this.spaceItemIconSize == NOT_DEFINED) {
            this.spaceItemIconSize = (int) getResources().getDimension(R.dimen.space_item_icon_default_size);
        }
        if (this.spaceItemIconOnlySize == NOT_DEFINED) {
            this.spaceItemIconOnlySize = (int) getResources().getDimension(R.dimen.space_item_icon_only_size);
        }
        if (this.centreButtonRippleColor == NOT_DEFINED) {
            this.centreButtonRippleColor = ContextCompat.getColor(this.context, R.color.colorBackgroundHighlightWhite);
        }
        if (this.activeCentreButtonIconColor == NOT_DEFINED) {
            this.activeCentreButtonIconColor = ContextCompat.getColor(this.context, R.color.space_white);
        }
        if (this.inActiveCentreButtonIconColor == NOT_DEFINED) {
            this.inActiveCentreButtonIconColor = ContextCompat.getColor(this.context, R.color.default_inactive_item_color);
        }
        ViewGroup.LayoutParams params = getLayoutParams();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = this.spaceNavigationHeight;
        setBackgroundColor(ContextCompat.getColor(this.context, R.color.space_transparent));
        setLayoutParams(params);
    }

    @RequiresApi(api = 16)
    protected void onSizeChanged(int width, int height, int oldWidth, int oldHeight) {
        super.onSizeChanged(width, height, oldWidth, oldHeight);
        restoreCurrentItem();
        if (this.spaceItems.size() < 2 && !isInEditMode()) {
            throw new NullPointerException("Your space item count must be greater than 1 , your current items count isa : " + this.spaceItems.size());
        } else if (this.spaceItems.size() <= 4 || isInEditMode()) {
            this.contentWidth = (width - this.centreContentWight) / 2 + 1;
            removeAllViews();
            initAndAddViewsToMainView();
            postRequestLayout();
        } else {
            throw new IndexOutOfBoundsException("Your items count maximum can be 4, your current items count is : " + this.spaceItems.size());
        }
    }

    private void initAndAddViewsToMainView() {
        RelativeLayout mainContent = new RelativeLayout(this.context);
        this.centreBackgroundView = new RelativeLayout(this.context);
        this.leftContent = new LinearLayout(this.context);
        this.rightContent = new LinearLayout(this.context);
        this.centreContent = buildBezierView();
        if (this.centreButton == null) {
            this.centreButton = new CentreButton(this.context);
        }
        this.centreButton.setSize(FloatingActionButton.SIZE_NORMAL);
        this.centreButton.setUseCompatPadding(false);
        this.centreButton.setRippleColor(this.centreButtonRippleColor);
        this.centreButton.setBackgroundTintList(ColorStateList.valueOf(this.centreButtonColor));
        this.centreButton.setImageResource(this.centreButtonIcon);
        if (this.isCentreButtonIconColorFilterEnabled || this.isCentreButtonSelectable) {
            this.centreButton.getDrawable().setColorFilter(this.inActiveSpaceItemColor, Mode.SRC_IN);
        }
        this.centreButton.setOnClickListener(new C03631());
        this.centreButton.setOnLongClickListener(new C03642());
        RelativeLayout.LayoutParams fabParams = new RelativeLayout.LayoutParams(this.centreButtonSize, this.centreButtonSize);
        fabParams.addRule(13);
        RelativeLayout.LayoutParams mainContentParams = new RelativeLayout.LayoutParams(-1, this.mainContentHeight);
        mainContentParams.addRule(12);
        RelativeLayout.LayoutParams centreContentParams = new RelativeLayout.LayoutParams(this.centreContentWight, this.spaceNavigationHeight);
        centreContentParams.addRule(14);
        centreContentParams.addRule(12);
        RelativeLayout.LayoutParams centreBackgroundViewParams = new RelativeLayout.LayoutParams(this.centreContentWight, this.mainContentHeight);
        centreBackgroundViewParams.addRule(14);
        centreBackgroundViewParams.addRule(12);
        RelativeLayout.LayoutParams leftContentParams = new RelativeLayout.LayoutParams(this.contentWidth, -1);
        leftContentParams.addRule(9);
        leftContentParams.addRule(0);
        RelativeLayout.LayoutParams rightContentParams = new RelativeLayout.LayoutParams(this.contentWidth, -1);
        rightContentParams.addRule(11);
        rightContentParams.addRule(0);
        setBackgroundColors();
        this.centreContent.addView(this.centreButton, fabParams);


        this.leftContent.setBackground(ContextCompat.getDrawable(this.context, R.drawable.top_border));
        this.rightContent.setBackground(ContextCompat.getDrawable(this.context, R.drawable.top_border));


        mainContent.addView(this.leftContent, leftContentParams);
        mainContent.addView(this.rightContent, rightContentParams);
        addView(this.centreBackgroundView, centreBackgroundViewParams);
        addView(this.centreContent, centreContentParams);
        addView(mainContent, mainContentParams);
        restoreChangedIconsAndTexts();
        addSpaceItems(this.leftContent, this.rightContent);
        updateSpaceItems(this.currentSelectedItem);
    }

    private void addSpaceItems(LinearLayout leftContent, LinearLayout rightContent) {
        if (leftContent.getChildCount() > 0 || rightContent.getChildCount() > 0) {
            leftContent.removeAllViews();
            rightContent.removeAllViews();
        }
        this.spaceItemList.clear();
        this.badgeList.clear();
        LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        for (int i = 0; i < this.spaceItems.size(); i++) {
            int targetWidth;
            final int index = i;
            if (this.spaceItems.size() > 2) {
                targetWidth = this.contentWidth / 2;
            } else {
                targetWidth = this.contentWidth;
            }
            RelativeLayout.LayoutParams textAndIconContainerParams = new RelativeLayout.LayoutParams(targetWidth, this.mainContentHeight);
            RelativeLayout textAndIconContainer = (RelativeLayout) inflater.inflate(R.layout.space_item_view, this, false);
            textAndIconContainer.setLayoutParams(textAndIconContainerParams);
            ImageView spaceItemIcon = (ImageView) textAndIconContainer.findViewById(R.id.space_icon);
            TextView spaceItemText = (TextView) textAndIconContainer.findViewById(R.id.space_text);
            RelativeLayout badgeContainer = (RelativeLayout) textAndIconContainer.findViewById(R.id.badge_container);
            spaceItemIcon.setImageResource((this.spaceItems.get(i)).getItemIcon());
            spaceItemText.setText((this.spaceItems.get(i)).getItemName());
            spaceItemText.setTextSize(0, (float) this.spaceItemTextSize);
            if (this.isCustomFont) {
                spaceItemText.setTypeface(this.customFont);
            }
            if (this.isTextOnlyMode) {
                Utils.changeViewVisibilityGone(spaceItemIcon);
            }
            ViewGroup.LayoutParams iconParams = spaceItemIcon.getLayoutParams();
            if (this.isIconOnlyMode) {
                iconParams.height = this.spaceItemIconOnlySize;
                iconParams.width = this.spaceItemIconOnlySize;
                spaceItemIcon.setLayoutParams(iconParams);
                Utils.changeViewVisibilityGone(spaceItemText);
            } else {
                iconParams.height = this.spaceItemIconSize;
                iconParams.width = this.spaceItemIconSize;
                spaceItemIcon.setLayoutParams(iconParams);
            }
            this.spaceItemList.add(textAndIconContainer);
            this.badgeList.add(badgeContainer);
            if (this.spaceItems.size() == 2 && leftContent.getChildCount() == 1) {
                rightContent.addView(textAndIconContainer, textAndIconContainerParams);
            } else if (this.spaceItems.size() <= 2 || leftContent.getChildCount() != 2) {
                leftContent.addView(textAndIconContainer, textAndIconContainerParams);
            } else {
                rightContent.addView(textAndIconContainer, textAndIconContainerParams);
            }
            if (i == this.currentSelectedItem) {
                spaceItemText.setTextColor(this.activeSpaceItemColor);
                Utils.changeImageViewTint(spaceItemIcon, this.activeSpaceItemColor);
            } else {
                spaceItemText.setTextColor(this.inActiveSpaceItemColor);
                Utils.changeImageViewTint(spaceItemIcon, this.inActiveSpaceItemColor);
            }
            textAndIconContainer.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    SpaceNavigationView.this.updateSpaceItems(index);
                }
            });
            textAndIconContainer.setOnLongClickListener(new OnLongClickListener() {
                public boolean onLongClick(View v) {
                    if (SpaceNavigationView.this.spaceOnLongClickListener != null) {
                        SpaceNavigationView.this.spaceOnLongClickListener.onItemLongClick(index, ((SpaceItem) SpaceNavigationView.this.spaceItems.get(index)).getItemName());
                    }
                    return true;
                }
            });
        }
        restoreBadges();
    }

    public void updateSpaceItems(int selectedIndex) {
        if (this.currentSelectedItem != selectedIndex || this.spaceOnClickListener == null || selectedIndex < 0) {
            if (selectedIndex == -1 && this.centreButton != null) {
                this.centreButton.getDrawable().setColorFilter(this.activeCentreButtonIconColor, Mode.SRC_IN);
                if (this.activeCentreButtonBackgroundColor != NOT_DEFINED) {
                    this.centreButton.setBackgroundTintList(ColorStateList.valueOf(this.activeCentreButtonBackgroundColor));
                }
            }
            if (!(this.currentSelectedItem != -1 || selectedIndex == -1 || this.centreButton == null)) {
                this.centreButton.getDrawable().setColorFilter(this.inActiveCentreButtonIconColor, Mode.SRC_IN);
                if (this.activeCentreButtonBackgroundColor != NOT_DEFINED) {
                    this.centreButton.setBackgroundTintList(ColorStateList.valueOf(this.centreButtonColor));
                }
            }
            for (int i = 0; i < this.spaceItemList.size(); i++) {
                RelativeLayout textAndIconContainer;
                ImageView spaceItemIcon;
                if (i == selectedIndex) {
                    textAndIconContainer = (RelativeLayout) this.spaceItemList.get(selectedIndex);
//                    ((LinearLayout) textAndIconContainer.findViewById(R.id.main_content)).setBackground(ContextCompat.getDrawable(this.context, R.drawable.bottom_border));
                    spaceItemIcon = (ImageView) textAndIconContainer.findViewById(R.id.space_icon);
                    ((TextView) textAndIconContainer.findViewById(R.id.space_text)).setTextColor(this.activeSpaceItemColor);
                    Utils.changeImageViewTint(spaceItemIcon, this.activeSpaceItemColor);
                } else if (i == this.currentSelectedItem) {
                    textAndIconContainer = (RelativeLayout) this.spaceItemList.get(i);
//                    ((LinearLayout) textAndIconContainer.findViewById(R.id.main_content)).setBackground(null);
                    spaceItemIcon = (ImageView) textAndIconContainer.findViewById(R.id.space_icon);
                    ((TextView) textAndIconContainer.findViewById(R.id.space_text)).setTextColor(this.inActiveSpaceItemColor);
                    Utils.changeImageViewTint(spaceItemIcon, this.inActiveSpaceItemColor);
                }
            }
            if (this.spaceOnClickListener != null && selectedIndex >= 0) {
                this.spaceOnClickListener.onItemClick(selectedIndex, ((SpaceItem) this.spaceItems.get(selectedIndex)).getItemName());
            } else if (this.spaceOnClickListener != null && selectedIndex == -1) {
                SpaceNavigationView.this.spaceOnClickListener.onCentreButtonClick();
            }
            this.currentSelectedItem = selectedIndex;
            return;
        }
        this.spaceOnClickListener.onItemReselected(selectedIndex, ((SpaceItem) this.spaceItems.get(selectedIndex)).getItemName());
    }

    public void changeActiveItem(int index) {
        if (index == -1) {
            this.centreButton.getDrawable().setColorFilter(this.activeCentreButtonIconColor, Mode.SRC_IN);
            if (this.activeCentreButtonBackgroundColor != NOT_DEFINED) {
                this.centreButton.setBackgroundTintList(ColorStateList.valueOf(this.activeCentreButtonBackgroundColor));
            }
            RelativeLayout textAndIconContainer;
            ImageView spaceItemIcon;
            for (int i = 0; i < spaceItemList.size(); i++) {
                textAndIconContainer = (RelativeLayout) this.spaceItemList.get(i);
//                    ((LinearLayout) textAndIconContainer.findViewById(R.id.main_content)).setBackground(null);
                spaceItemIcon = (ImageView) textAndIconContainer.findViewById(R.id.space_icon);
                ((TextView) textAndIconContainer.findViewById(R.id.space_text)).setTextColor(this.inActiveSpaceItemColor);
                Utils.changeImageViewTint(spaceItemIcon, this.inActiveSpaceItemColor);
//                Log.e("spaceItemChangeColor", "inactivated");
            }
        } else {
            this.centreButton.getDrawable().setColorFilter(this.inActiveCentreButtonIconColor, Mode.SRC_IN);
            if (this.centreButtonColor != NOT_DEFINED) {
                this.centreButton.setBackgroundTintList(ColorStateList.valueOf(this.centreButtonColor));
            }
            for (int i = 0; i < this.spaceItemList.size(); i++) {
                RelativeLayout textAndIconContainer;
                ImageView spaceItemIcon;
                if (i == index) {
                    textAndIconContainer = (RelativeLayout) this.spaceItemList.get(i);
//                    ((LinearLayout) textAndIconContainer.findViewById(R.id.main_content)).setBackground(ContextCompat.getDrawable(this.context, R.drawable.bottom_border));
                    spaceItemIcon = (ImageView) textAndIconContainer.findViewById(R.id.space_icon);
                    ((TextView) textAndIconContainer.findViewById(R.id.space_text)).setTextColor(this.activeSpaceItemColor);
                    Utils.changeImageViewTint(spaceItemIcon, this.activeSpaceItemColor);
                } else if (i == this.currentSelectedItem) {
                    textAndIconContainer = (RelativeLayout) this.spaceItemList.get(i);
//                    ((LinearLayout) textAndIconContainer.findViewById(R.id.main_content)).setBackground(null);
                    spaceItemIcon = (ImageView) textAndIconContainer.findViewById(R.id.space_icon);
                    ((TextView) textAndIconContainer.findViewById(R.id.space_text)).setTextColor(this.inActiveSpaceItemColor);
                    Utils.changeImageViewTint(spaceItemIcon, this.inActiveSpaceItemColor);
                }
            }
        }
        currentSelectedItem = index;
    }

    public void centerButtonChangeColor(boolean active) {
//        Log.e("centerButtonChangeColor", "" + active);
        if (active && centreButton != null) {
            this.centreButton.getDrawable().setColorFilter(this.activeCentreButtonIconColor, Mode.SRC_IN);
            if (this.activeCentreButtonBackgroundColor != NOT_DEFINED) {
                this.centreButton.setBackgroundTintList(ColorStateList.valueOf(this.activeCentreButtonBackgroundColor));
            }
//            Log.e("centerButtonChangeColor", "activated");
        } else if (!active && centreButton != null) {
            this.centreButton.getDrawable().setColorFilter(this.inActiveCentreButtonIconColor, Mode.SRC_IN);
            if (this.centreButtonColor != NOT_DEFINED) {
                this.centreButton.setBackgroundTintList(ColorStateList.valueOf(this.centreButtonColor));
            }
//            Log.e("centerButtonChangeColor", "inactivated");
        }

    }

    public void spaceItemChangeColor(int index, boolean active) {
//        Log.e("spaceItemChangeColor", "" + active);
        RelativeLayout textAndIconContainer;
        ImageView spaceItemIcon;
        if (active) {
            textAndIconContainer = (RelativeLayout) this.spaceItemList.get(index);
//                    ((LinearLayout) textAndIconContainer.findViewById(R.id.main_content)).setBackground(ContextCompat.getDrawable(this.context, R.drawable.bottom_border));
            spaceItemIcon = (ImageView) textAndIconContainer.findViewById(R.id.space_icon);
            ((TextView) textAndIconContainer.findViewById(R.id.space_text)).setTextColor(this.activeSpaceItemColor);
            Utils.changeImageViewTint(spaceItemIcon, this.activeSpaceItemColor);
//            Log.e("spaceItemChangeColor", "activated");
        } else {
            textAndIconContainer = (RelativeLayout) this.spaceItemList.get(index);
//                    ((LinearLayout) textAndIconContainer.findViewById(R.id.main_content)).setBackground(null);
            spaceItemIcon = (ImageView) textAndIconContainer.findViewById(R.id.space_icon);
            ((TextView) textAndIconContainer.findViewById(R.id.space_text)).setTextColor(this.inActiveSpaceItemColor);
            Utils.changeImageViewTint(spaceItemIcon, this.inActiveSpaceItemColor);
//            Log.e("spaceItemChangeColor", "inactivated");
        }
    }

    private void setBackgroundColors() {
        this.rightContent.setBackgroundColor(this.spaceBackgroundColor);
        this.centreBackgroundView.setBackgroundColor(this.spaceBackgroundColor);
        this.leftContent.setBackgroundColor(this.spaceBackgroundColor);
    }

    private void postRequestLayout() {
        getHandler().post(new C03675());
    }

    private void restoreCurrentItem() {
        Bundle restoredBundle = this.savedInstanceState;
        if (restoredBundle != null && restoredBundle.containsKey(CURRENT_SELECTED_ITEM_BUNDLE_KEY)) {
            this.currentSelectedItem = restoredBundle.getInt(CURRENT_SELECTED_ITEM_BUNDLE_KEY, 0);
        }
    }

    private void restoreBadges() {
        Bundle restoredBundle = this.savedInstanceState;
        if (restoredBundle != null) {
            if (restoredBundle.containsKey(BADGE_FULL_TEXT_KEY)) {
                this.shouldShowBadgeWithNinePlus = restoredBundle.getBoolean(BADGE_FULL_TEXT_KEY);
            }
            if (restoredBundle.containsKey(BUDGES_ITEM_BUNDLE_KEY)) {
                this.badgeSaveInstanceHashMap = (HashMap) this.savedInstanceState.getSerializable(BUDGES_ITEM_BUNDLE_KEY);
                if (this.badgeSaveInstanceHashMap != null) {
                    for (Integer integer : this.badgeSaveInstanceHashMap.keySet()) {
                        BadgeHelper.forceShowBadge((RelativeLayout) this.badgeList.get(integer.intValue()), (BadgeItem) this.badgeSaveInstanceHashMap.get(integer), this.shouldShowBadgeWithNinePlus);
                    }
                }
            }
        }
    }

    private void restoreChangedIconsAndTexts() {
        Bundle restoredBundle = this.savedInstanceState;
        if (restoredBundle != null) {
            if (restoredBundle.containsKey(CHANGED_ICON_AND_TEXT_BUNDLE_KEY)) {
                this.changedItemAndIconHashMap = (HashMap) restoredBundle.getSerializable(CHANGED_ICON_AND_TEXT_BUNDLE_KEY);
                if (this.changedItemAndIconHashMap != null) {
                    for (int i = 0; i < this.changedItemAndIconHashMap.size(); i++) {
                        SpaceItem spaceItem = (SpaceItem) this.changedItemAndIconHashMap.get(Integer.valueOf(i));
                        ((SpaceItem) this.spaceItems.get(i)).setItemIcon(spaceItem.getItemIcon());
                        ((SpaceItem) this.spaceItems.get(i)).setItemName(spaceItem.getItemName());
                    }
                }
            }
            if (restoredBundle.containsKey(CENTRE_BUTTON_ICON_KEY)) {
                this.centreButtonIcon = restoredBundle.getInt(CENTRE_BUTTON_ICON_KEY);
                this.centreButton.setImageResource(this.centreButtonIcon);
            }
            if (restoredBundle.containsKey(SPACE_BACKGROUND_COLOR_KEY)) {
                changeSpaceBackgroundColor(restoredBundle.getInt(SPACE_BACKGROUND_COLOR_KEY));
            }
        }
    }

    private BezierView buildBezierView() {
        BezierView bezierView = new BezierView(this.context, this.spaceBackgroundColor);
        bezierView.build(centreContentWight, spaceNavigationHeight - mainContentHeight);
        return bezierView;
    }

    private void throwArrayIndexOutOfBoundsException(int itemIndex) {
        throw new ArrayIndexOutOfBoundsException("Your item index can't be 0 or greater than space item size, your items size is " + this.spaceItems.size() + ", your current index is :" + itemIndex);
    }

    public void initWithSaveInstanceState(Bundle savedInstanceState) {
        this.savedInstanceState = savedInstanceState;
    }

    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(CURRENT_SELECTED_ITEM_BUNDLE_KEY, this.currentSelectedItem);
        outState.putInt(CENTRE_BUTTON_ICON_KEY, this.centreButtonIcon);
        outState.putInt(SPACE_BACKGROUND_COLOR_KEY, this.spaceBackgroundColor);
        outState.putBoolean(BADGE_FULL_TEXT_KEY, this.shouldShowBadgeWithNinePlus);
        if (this.badgeSaveInstanceHashMap.size() > 0) {
            outState.putSerializable(BUDGES_ITEM_BUNDLE_KEY, this.badgeSaveInstanceHashMap);
        }
        if (this.changedItemAndIconHashMap.size() > 0) {
            outState.putSerializable(CHANGED_ICON_AND_TEXT_BUNDLE_KEY, this.changedItemAndIconHashMap);
        }
    }

    public void setCentreButtonColor(@ColorInt int centreButtonColor) {
        this.centreButtonColor = centreButtonColor;
    }

    public void setSpaceBackgroundColor(@ColorInt int spaceBackgroundColor) {
        this.spaceBackgroundColor = spaceBackgroundColor;
    }

    public void setCentreButtonIcon(int centreButtonIcon) {
        this.centreButtonIcon = centreButtonIcon;
    }

    public void setActiveCentreButtonBackgroundColor(@ColorInt int activeCentreButtonBackgroundColor) {
        this.activeCentreButtonBackgroundColor = activeCentreButtonBackgroundColor;
    }

    public void setActiveSpaceItemColor(@ColorInt int activeSpaceItemColor) {
        this.activeSpaceItemColor = activeSpaceItemColor;
    }

    public void setInActiveSpaceItemColor(@ColorInt int inActiveSpaceItemColor) {
        this.inActiveSpaceItemColor = inActiveSpaceItemColor;
    }

    public void setSpaceItemIconSize(int spaceItemIconSize) {
        this.spaceItemIconSize = spaceItemIconSize;
    }

    public void setSpaceItemIconSizeInOnlyIconMode(int spaceItemIconOnlySize) {
        this.spaceItemIconOnlySize = spaceItemIconOnlySize;
    }

    public void setSpaceItemTextSize(int spaceItemTextSize) {
        this.spaceItemTextSize = spaceItemTextSize;
    }

    public void setCentreButtonRippleColor(int centreButtonRippleColor) {
        this.centreButtonRippleColor = centreButtonRippleColor;
    }

    public void showTextOnly() {
        this.isTextOnlyMode = true;
    }

    public void showIconOnly() {
        this.isIconOnlyMode = true;
    }

    public void setCentreButtonSelectable(boolean isSelectable) {
        this.isCentreButtonSelectable = isSelectable;
    }

    public void addSpaceItem(SpaceItem spaceItem) {
        this.spaceItems.add(spaceItem);
    }

    public void setCentreButtonSelected() {
        if (this.isCentreButtonSelectable) {
            updateSpaceItems(-1);
            return;
        }
        throw new ArrayIndexOutOfBoundsException("Please be more careful, you must set the centre button selectable");
    }

    public void setSpaceOnClickListener(SpaceOnClickListener spaceOnClickListener) {
        this.spaceOnClickListener = spaceOnClickListener;
    }

    public void setSpaceOnLongClickListener(SpaceOnLongClickListener spaceOnLongClickListener) {
        this.spaceOnLongClickListener = spaceOnLongClickListener;
    }

    public void changeCurrentItem(int indexToChange) {
        if (indexToChange < -1 || indexToChange > this.spaceItems.size()) {
            throw new ArrayIndexOutOfBoundsException("Please be more careful, we do't have such item : " + indexToChange);
        }
        updateSpaceItems(indexToChange);
    }

    public void showBadgeAtIndex(int itemIndex, int badgeText, @ColorInt int badgeColor) {
        if (itemIndex < 0 || itemIndex > this.spaceItems.size()) {
            throwArrayIndexOutOfBoundsException(itemIndex);
            return;
        }
        RelativeLayout badgeView = (RelativeLayout) this.badgeList.get(itemIndex);
        if (VERSION.SDK_INT >= 16) {
            badgeView.setBackground(BadgeHelper.makeShapeDrawable(badgeColor));
        } else {
            badgeView.setBackgroundDrawable(BadgeHelper.makeShapeDrawable(badgeColor));
        }
        BadgeItem badgeItem = new BadgeItem(itemIndex, badgeText, badgeColor);
        BadgeHelper.showBadge(badgeView, badgeItem, this.shouldShowBadgeWithNinePlus);
        this.badgeSaveInstanceHashMap.put(Integer.valueOf(itemIndex), badgeItem);
    }

    public void hideBudgeAtIndex(int index) {
        if (((RelativeLayout) this.badgeList.get(index)).getVisibility() == GONE) {
            Log.d(TAG, "Budge at index: " + index + " already hidden");
            return;
        }
        BadgeHelper.hideBadge((View) this.badgeList.get(index));
        this.badgeSaveInstanceHashMap.remove(Integer.valueOf(index));
    }

    public void hideAllBudges() {
        for (RelativeLayout badge : this.badgeList) {
            if (badge.getVisibility() == VISIBLE) {
                BadgeHelper.hideBadge(badge);
            }
        }
        this.badgeSaveInstanceHashMap.clear();
    }

    public void changeBadgeTextAtIndex(int badgeIndex, int badgeText) {
        if (this.badgeSaveInstanceHashMap.get(Integer.valueOf(badgeIndex)) != null && ((BadgeItem) this.badgeSaveInstanceHashMap.get(Integer.valueOf(badgeIndex))).getIntBadgeText() != badgeText) {
            BadgeItem badgeItemForSave = new BadgeItem(badgeIndex, badgeText, ((BadgeItem) this.badgeSaveInstanceHashMap.get(Integer.valueOf(badgeIndex))).getBadgeColor());
            BadgeHelper.forceShowBadge((RelativeLayout) this.badgeList.get(badgeIndex), badgeItemForSave, this.shouldShowBadgeWithNinePlus);
            this.badgeSaveInstanceHashMap.put(Integer.valueOf(badgeIndex), badgeItemForSave);
        }
    }

    public void setFont(Typeface customFont) {
        this.isCustomFont = true;
        this.customFont = customFont;
    }

    public void setCentreButtonIconColorFilterEnabled(boolean enabled) {
        this.isCentreButtonIconColorFilterEnabled = enabled;
    }

    public void changeCenterButtonIcon(int icon) {
        if (this.centreButton == null) {
            Log.e(TAG, "You should call setCentreButtonIcon() instead, changeCenterButtonIcon works if space navigation already set up");
            return;
        }
        this.centreButton.setImageResource(icon);
        this.centreButtonIcon = icon;
    }

    public void changeItemIconAtPosition(int itemIndex, int newIcon) {
        if (itemIndex < 0 || itemIndex > this.spaceItems.size()) {
            throwArrayIndexOutOfBoundsException(itemIndex);
            return;
        }
        SpaceItem spaceItem = (SpaceItem) this.spaceItems.get(itemIndex);
        ((ImageView) ((RelativeLayout) this.spaceItemList.get(itemIndex)).findViewById(R.id.space_icon)).setImageResource(newIcon);
        spaceItem.setItemIcon(newIcon);
        this.changedItemAndIconHashMap.put(Integer.valueOf(itemIndex), spaceItem);
    }

    public void changeItemTextAtPosition(int itemIndex, String newText) {
        if (itemIndex < 0 || itemIndex > this.spaceItems.size()) {
            throwArrayIndexOutOfBoundsException(itemIndex);
            return;
        }
        SpaceItem spaceItem = (SpaceItem) this.spaceItems.get(itemIndex);
        ((TextView) ((RelativeLayout) this.spaceItemList.get(itemIndex)).findViewById(R.id.space_text)).setText(newText);
        spaceItem.setItemName(newText);
        this.changedItemAndIconHashMap.put(Integer.valueOf(itemIndex), spaceItem);
    }

    public void changeSpaceBackgroundColor(@ColorInt int color) {
        if (color == this.spaceBackgroundColor) {
            Log.d(TAG, "changeSpaceBackgroundColor: color already changed");
            return;
        }
        this.spaceBackgroundColor = color;
        setBackgroundColors();
        this.centreContent.changeBackgroundColor(color);
    }

    public void shouldShowFullBadgeText(boolean shouldShowBadgeWithNinePlus) {
        this.shouldShowBadgeWithNinePlus = shouldShowBadgeWithNinePlus;
    }

    public void setActiveCentreButtonIconColor(@ColorInt int color) {
        this.activeCentreButtonIconColor = color;
    }

    public void setInActiveCentreButtonIconColor(@ColorInt int color) {
        this.inActiveCentreButtonIconColor = color;
    }


}
