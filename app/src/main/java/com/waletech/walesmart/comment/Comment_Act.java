package com.waletech.walesmart.comment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.waletech.walesmart.R;
import com.waletech.walesmart.base.Base_Act;
import com.waletech.walesmart.http.HttpSet;
import com.waletech.walesmart.http.HttpTag;
import com.waletech.walesmart.product.Product_Act;
import com.waletech.walesmart.publicClass.BitmapCache;
import com.waletech.walesmart.publicClass.LineToast;
import com.waletech.walesmart.publicClass.Methods;
import com.waletech.walesmart.publicObject.ObjectShoe;
import com.waletech.walesmart.publicSet.IntentSet;
import com.waletech.walesmart.publicSet.MapSet;

import org.json.JSONException;

import java.util.HashMap;

public class Comment_Act extends Base_Act {

    private LineToast toast;
    private BitmapCache cache;

    private HashMap<String, String> comment_map;

    private String epc_code;

    private CommentAction commentAction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_layout);

        varInit();

        setupToolbar();

        setupRatingBar();

        setupAdviceEdit();

        setupCommentBtn();

    }

    @Override
    protected void setupToolbar() {
        setTbTitle(getString(R.string.com_toolbar_title));
    }

    private void varInit() {
        commentAction = new CommentAction(this);

        cache = new BitmapCache();
        toast = new LineToast(Comment_Act.this);

        if (getIntent() != null) {
            epc_code = getIntent().getStringExtra(IntentSet.KEY_EPC_CODE);
//            epc_code = "30382FF10082C6C0000101B2";

            HttpSet.setBaseIP(sharedAction.getNetIP(), this);
            HttpSet.setBaseService(sharedAction.getNetService(), this);

            commentAction.getShoeDetails(epc_code);
        } else {
            epc_code = "";
        }

        comment_map = new HashMap<>();
    }

    private void setupRatingBar() {
        final RatingBar width_rat = (RatingBar) findViewById(R.id.com_rat0);
        final RatingBar material_rat = (RatingBar) findViewById(R.id.com_rat1);
        final RatingBar sport_rat = (RatingBar) findViewById(R.id.com_rat2);

        width_rat.setOnRatingBarChangeListener(ratingListener);
        material_rat.setOnRatingBarChangeListener(ratingListener);
        sport_rat.setOnRatingBarChangeListener(ratingListener);
    }

    private void setupAdviceEdit() {
        final LinearLayout rating_ll = (LinearLayout) findViewById(R.id.com_ll);
        rating_ll.setOnClickListener(clickListener);
    }

    private void setupCommentBtn() {
        final Button commit_btn = (Button) findViewById(R.id.com_commit_btn);
        commit_btn.setOnClickListener(clickListener);

        final Button shop_btn = (Button) findViewById(R.id.com_shop_btn);
        shop_btn.setOnClickListener(clickListener);
    }

    @Override
    public void onMultiHandleResponse(String tag, String result) throws JSONException {
        switch (tag) {
            case HttpTag.COMMENT_COMMIT:
                commentAction.handleCommentResponse(result);
                finish();
                break;

            case HttpTag.PRODUCT_GET_SHOE_DETAILS:
                ObjectShoe shoe = commentAction.handleShoeDetails(result);
                setupDetailsText(shoe);
                break;

            default:
                break;
        }
    }

    @Override
    public void onNullResponse() throws JSONException {
        finish();
    }

    @Override
    public void onPermissionAccepted(int permission_code) {

    }

    @Override
    public void onPermissionRefused(int permission_code) {

    }

    private void setupDetailsText(ObjectShoe shoe) {
        String brand = shoe.getBrand();
        String design = shoe.getDesign();
        String color = shoe.getColor();
        String size = shoe.getSize();
        String img_url = shoe.getImagePath();

        final ImageView pro_img = (ImageView) findViewById(R.id.com_img);
        final TextView pro_tv = (TextView) findViewById(R.id.com_tv1);

        Methods.downloadImage(pro_img, HttpSet.BASE_URL + img_url, cache);

        String params = brand + "\n" + design + "\n" + color + "\n" + size;
        pro_tv.setText(params);
    }

    private final static String TAG_RATING_WIDTH = "tag_rating_width";
    private final static String TAG_RATING_MATERIAL = "tag_rating_materail";
    private final static String TAG_RATING_SPORT = "tag_rating_sport";

    private RatingBar.OnRatingBarChangeListener ratingListener = new RatingBar.OnRatingBarChangeListener() {
        @Override
        public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {

            int rating_int = (int) rating;

            if (fromUser) {
                switch (ratingBar.getId()) {
                    case R.id.com_rat0:
                        comment_map.put(MapSet.KEY_EXP_WIDTH, "" + rating_int);
                        ExplainAction.showExplain(Comment_Act.this, TAG_RATING_WIDTH, rating_int);
                        break;

                    case R.id.com_rat1:
                        comment_map.put(MapSet.KEY_EXP_MATERIAL, "" + rating_int);
                        ExplainAction.showExplain(Comment_Act.this, TAG_RATING_MATERIAL, rating_int);
                        break;

                    case R.id.com_rat2:
                        comment_map.put(MapSet.KEY_EXP_SPORT, "" + rating_int);
                        ExplainAction.showExplain(Comment_Act.this, TAG_RATING_SPORT, rating_int);
                        break;

                    default:
                        break;
                }
            }
        }
    };


    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.com_ll:
                    Methods.collapseIME(Comment_Act.this);
                    break;

                case R.id.com_commit_btn:
                    onCommit();
                    break;

                case R.id.com_shop_btn:
                    onShop();
                    break;

                default:
                    break;
            }
        }
    };

    private void onCommit() {
        final EditText advice_et = (EditText) findViewById(R.id.com_et);
        comment_map.put(MapSet.KEY_EXP_ADVICE, advice_et.getText().toString());
        commentAction.commitComment(epc_code, comment_map);
    }

    private void onShop() {
        onCommit();

        Intent product_int = new Intent(Comment_Act.this, Product_Act.class);
        product_int.putExtra(IntentSet.KEY_EPC_CODE, epc_code);
        product_int.putExtra(IntentSet.KEY_IS_FROM_SHOP, false);
        startActivity(product_int);
    }

}
