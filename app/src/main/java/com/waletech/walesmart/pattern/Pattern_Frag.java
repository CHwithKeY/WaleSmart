package com.waletech.walesmart.pattern;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.waletech.walesmart.R;
import com.waletech.walesmart.base.Base_Frag;
import com.waletech.walesmart.http.HttpSet;
import com.waletech.walesmart.http.HttpTag;
import com.waletech.walesmart.publicClass.BitmapCache;
import com.waletech.walesmart.publicClass.Methods;
import com.waletech.walesmart.publicClass.ScreenSize;
import com.waletech.walesmart.publicObject.ObjectShoe;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

/**
 * Created by KeY on 2016/7/4.
 */
public class Pattern_Frag extends Base_Frag {
    public final static int TYPE_COLOR = 0;
    public final static int TYPE_SIZE = 1;

    private ClickListener clickListener;
    private ColorListener colorListener;
    private SizeListener sizeListener;

    private PatternAction patternAction;

    private String epc_code;
    private String brand;
    private String design;

    private ArrayList<String> colorList;
    private ArrayList<String> sizeList;

    private ArrayList<Button> colorBtnList;
    private ArrayList<Button> sizeBtnList;

    // private ArrayList<String> filterColorList;
    private ArrayList<String> filterSizeList;

    // 迫于无奈，只能先将就实现“先选择颜色，再选择尺寸”的方案了
    private boolean isColorChosen = false;

    public void setIsColorChosen(boolean isColorChosen) {
        this.isColorChosen = isColorChosen;
    }

    public boolean getIsColorChosen() {
        return isColorChosen;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.public_pattern_page, container, false);
    }

    @Override
    public void setArguments(Bundle bundle) {
        super.setArguments(bundle);

        epc_code = bundle.getString("epc_code");
        brand = bundle.getString("brand");
        design = bundle.getString("design");
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        varInit();

        setupAssistView(view);
    }

    private void varInit() {
        patternAction = new PatternAction(getContext(), this);

        clickListener = new ClickListener(getContext(), patternAction);

        colorListener = new ColorListener(getContext(), patternAction);
        colorListener.setPatternAction(patternAction);
        colorListener.setPatternFrag(this);

        sizeListener = new SizeListener(getContext(), patternAction);
        sizeListener.setPatternAction(patternAction);
        sizeListener.setPatternFrag(this);

        // patternAction.getDefaultStock(epc_code);
        patternAction.getPattern(brand, design);

        cache = new BitmapCache();
    }

    private void setupAssistView(View view) {
        final TextView shadow_tv = (TextView) view.findViewById(R.id.pattern_tv0);
        shadow_tv.setTag(this);
        shadow_tv.setOnClickListener(clickListener);

        final ImageButton close_imgbtn = (ImageButton) view.findViewById(R.id.pattern_imgbtn);
        close_imgbtn.setTag(this);
        close_imgbtn.setOnClickListener(clickListener);
    }

    private void setupShoeDefaultData(ObjectShoe shoe) {
        if (getView() != null) {
            View view = getView();

            final TextView price_tv = (TextView) view.findViewById(R.id.pattern_tv1);
            final TextView stock_tv = (TextView) view.findViewById(R.id.pattern_tv2);
            final ImageView shoe_img = (ImageView) view.findViewById(R.id.pattern_img);

            String price_str = shoe.getPrice();
            String stock_str = shoe.getCount();

            price_tv.setText(price_str);
            stock_tv.setText(stock_str);

            Methods.downloadImage(shoe_img, HttpSet.BASE_URL + shoe.getImagePath(), cache);
        }
    }

    private void initPatternData(ArrayList<ObjectShoe> shoeList) {
        colorList = new ArrayList<>();
        sizeList = new ArrayList<>();

        HashSet<String> colorSet = new HashSet<>();
        HashSet<String> sizeSet = new HashSet<>();

        for (int i = 0; i < shoeList.size(); i++) {
            ObjectShoe shoe = shoeList.get(i);

            colorSet.add(shoe.getColor());
            sizeSet.add(shoe.getSize());
        }

        Iterator<String> colorIterator = colorSet.iterator();
        Iterator<String> sizeIterator = sizeSet.iterator();

        while (colorIterator.hasNext()) {
            colorList.add(colorIterator.next());
        }

        while (sizeIterator.hasNext()) {
            sizeList.add(sizeIterator.next());
        }

        setupPatternView(colorList, sizeList);
    }

    private void setupPatternView(ArrayList<String> colorList, ArrayList<String> sizeList) {
        if (getView() != null) {
            View view = getView();

            ScreenSize size = new ScreenSize(getContext());
            int parent_width = size.getWidth();

            final LinearLayout color_ll = (LinearLayout) view.findViewById(R.id.pattern_color_ll);
            final LinearLayout size_ll = (LinearLayout) view.findViewById(R.id.pattern_size_ll);

            colorBtnList = setupPatternItem(TYPE_COLOR, parent_width, color_ll, colorList);
            sizeBtnList = setupPatternItem(TYPE_SIZE, parent_width, size_ll, sizeList);

            colorListener.setButtonList(colorBtnList);
            sizeListener.setButtonList(sizeBtnList);
        }
    }

    private BitmapCache cache;

    private void setupShoeImageView(String image_path) {
        if (getView() != null) {
            View view = getView();

            final ImageView shoe_img = (ImageView) view.findViewById(R.id.pattern_img);
            Methods.downloadImage(shoe_img, HttpSet.BASE_URL + image_path, cache);
        }
    }

    private void setupShoeDetailsView(ObjectShoe shoe) {
        if (getView() != null) {
            View view = getView();

            final TextView price_tv = (TextView) view.findViewById(R.id.pattern_tv1);
            final TextView stock_tv = (TextView) view.findViewById(R.id.pattern_tv2);

            String price_str = "价格：" + shoe.getPrice();
            price_tv.setText(price_str);

            String count_str = "库存：" + shoe.getCount();
            stock_tv.setText(count_str);

            final Button add_cart_btn = (Button) view.findViewById(R.id.pattern_btn0);
            add_cart_btn.setTag(shoe.getRemark());
            add_cart_btn.setOnClickListener(clickListener);
        }
    }

    private ArrayList<Button> setupPatternItem(int itemType, int parent_width, LinearLayout itemLinear, ArrayList<String> itemList) {
        if (getView() != null) {
            View view = getView();

            final Button brand_standard_btn = (Button) view.findViewById(R.id.pattern_item_standard_btn);
            brand_standard_btn.setVisibility(View.VISIBLE);

            int btn_width = brand_standard_btn.getLayoutParams().width;
            int btn_height = brand_standard_btn.getLayoutParams().height;

            // 所需要的行数
            // 获取标准的一行所能放下的 button 的个数
            int line_count = 1;
            int single_line_count = parent_width / btn_width;

            // 然后自减 1，使得这一行两边可以多一点距离出来，更美观
            single_line_count = single_line_count - 1;

            // 判断实际情况的 list 的个数
            if (itemList.size() < single_line_count) {
                single_line_count = itemList.size();
                line_count = 1;
            }

            if (itemList.size() == single_line_count) {
                line_count = 1;
            }

            if (itemList.size() > single_line_count) {
                line_count = itemList.size() / single_line_count + 1;
            }

//            if (single_line_count > 4) {
//                single_line_count = 4;
//            }
//
//            if (line_count == 0) {
//                line_count = 1;
//            }
            brand_standard_btn.setVisibility(View.GONE);

            int index = 0;
            ArrayList<Button> buttonList = new ArrayList<>();

            for (int k = 0; k < line_count; k++) {
                Log.i("Result", "add linear");
                LinearLayout linear = new LinearLayout(getContext());
                linear.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                linear.setOrientation(LinearLayout.HORIZONTAL);
                linear.setGravity(Gravity.CENTER);
                linear.setPadding(0, 5, 0, 5);

//                int step_count = itemList.size();
//                if (line_count != 1) {
//                    step_count = single_line_count;
//                }

                if (k == line_count - 1) {
                    single_line_count = itemList.size() - k * single_line_count;
                }

                for (int i = 0; i < single_line_count; i++) {
                    Button button = new Button(getContext());
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(btn_width, btn_height);
                    params.setMargins(5, 5, 5, 5);
                    button.setLayoutParams(params);
                    button.setText(itemList.get(index));

                    button.setTextSize(12);
                    button.setPadding(5, 5, 5, 5);
                    button.setBackgroundResource(R.drawable.main_uncheck_btn);

                    button.setTag(index);
                    switch (itemType) {
                        case TYPE_COLOR:
                            button.setOnClickListener(colorListener);
                            break;

                        case TYPE_SIZE:
                            button.setOnClickListener(sizeListener);
                            break;

                        default:
                            break;
                    }

                    buttonList.add(button);

                    linear.addView(button);
                    index++;
                }

                itemLinear.addView(linear);
            }

            return buttonList;
        } else {
            return null;
        }
    }

    @Override
    public void onMultiHandleResponse(String tag, String result) throws JSONException {
        switch (tag) {
            case HttpTag.PATTERN_GET_DEFAULT_STOCK:
                ObjectShoe d_shoe = patternAction.handleDefaultStockResponse(result);
                setupShoeDefaultData(d_shoe);
                break;

            case HttpTag.PATTERN_GET_PATTERN:
                ArrayList<ObjectShoe> shoeList = patternAction.handlePatternResponse(result);
                initPatternData(shoeList);
                break;

            case HttpTag.PATTERN_GET_COLOR:
                patternAction.handleColorResponse(result);
                break;

            case HttpTag.PATTERN_GET_SHOE_DETAILS:
                ObjectShoe shoe = patternAction.handleShoeDetailsResponse(result);
                setupShoeDetailsView(shoe);
                break;

            case HttpTag.PATTERN_GET_SIZE:
                filterSizeList = patternAction.handleSizeResponse(result);

                for (int i = 0; i < sizeBtnList.size(); i++) {
                    sizeBtnList.get(i).setBackgroundResource(R.drawable.main_uncheck_btn);

                    String size_str = sizeBtnList.get(i).getText().toString();
                    Log.i("Result", "size_str is : " + size_str);
                    for (int k = 0; k < filterSizeList.size(); k++) {
                        if (size_str.equals(filterSizeList.get(k))) {
                            Log.i("Result", "get same str is : " + filterSizeList.get(k) + " and btn str is : " + size_str);
                            sizeBtnList.get(i).setEnabled(true);
                            break;
                        } else {
                            sizeBtnList.get(i).setEnabled(false);
                        }
                    }
                }
                break;

            case HttpTag.PATTERN_GET_IMAGE:
                String image_path = patternAction.handleImageResponse(result);
                setupShoeImageView(image_path);
                break;

            case HttpTag.PATTERN_ADD_CART:
                patternAction.handleAddCartResponse(result);
                break;

            default:
                break;
        }
    }

    @Override
    public void onNullResponse() throws JSONException {

    }

    @Override
    public void setCustomTag(String tag) {

    }

    @Override
    public String getCustomTag() {
        return null;
    }

}
