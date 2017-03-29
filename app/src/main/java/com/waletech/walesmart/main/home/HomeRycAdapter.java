package com.waletech.walesmart.main.home;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.waletech.walesmart.R;
import com.waletech.walesmart.publicAdapter.ImageRycAdapter;
import com.waletech.walesmart.publicAdapter.TextRycAdapter;
import com.waletech.walesmart.publicClass.Methods;
import com.waletech.walesmart.publicClass.ScreenSize;

import java.util.ArrayList;

/**
 * Created by KeY on 2016/6/28.
 */
public class HomeRycAdapter extends RecyclerView.Adapter<HomeRycAdapter.ViewHolder> {
    private Context context;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.recycler_home_item, null);
        context = parent.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        setupKeyWordRv(holder);

        setupEntranceRv(holder);

        setupAdsViewPager(holder, position);

        setupAdsImageView(holder);

    }

    @Override
    public int getItemCount() {
        return 1;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        RecyclerView key_word_rv;

        RecyclerView entrance_rv;
        // advertisements for ads
        ViewPager ads_vp;

        // Ads Template A Image
        ImageView ad_temp_a_img0;
        ImageView ad_temp_a_img1;
        ImageView ad_temp_a_img2;
        ImageView ad_temp_a_img3;

        // Ads Template B Image
        ImageView ad_temp_b_img0;
        ImageView ad_temp_b_img1;
        ImageView ad_temp_b_img2;

        ArrayList<ImageView> ad_imgList = new ArrayList<>();

        public ViewHolder(View itemView) {
            super(itemView);

            key_word_rv = (RecyclerView) itemView.findViewById(R.id.home_rv0);

            entrance_rv = (RecyclerView) itemView.findViewById(R.id.home_rv1);
            // ads_vp = (ViewPager) itemView.findViewById(R.id.home_vp);

            ad_temp_a_img0 = (ImageView) itemView.findViewById(R.id.template_a_img0);
            ad_temp_a_img1 = (ImageView) itemView.findViewById(R.id.template_a_img1);
            ad_temp_a_img2 = (ImageView) itemView.findViewById(R.id.template_a_img2);
            ad_temp_a_img3 = (ImageView) itemView.findViewById(R.id.template_a_img3);

            ad_temp_b_img0 = (ImageView) itemView.findViewById(R.id.template_b_img0);
            ad_temp_b_img1 = (ImageView) itemView.findViewById(R.id.template_b_img1);
            ad_temp_b_img2 = (ImageView) itemView.findViewById(R.id.template_b_img2);

            ImageView[] ad_img_array = {
                    ad_temp_a_img0, ad_temp_a_img1,
                    ad_temp_a_img2, ad_temp_a_img3,

                    ad_temp_b_img0, ad_temp_b_img1,
                    ad_temp_b_img2};

            for (int i = 0; i < ad_img_array.length; i++) {
                ad_imgList.add(i, ad_img_array[i]);
            }
        }
    }

    private void setupKeyWordRv(ViewHolder holder) {
        String[] textArray = {
                "Cat", "耐克", "黑白经典款",
                "Adidas FlyIn", "运动鞋", "奥运会",
                "登山鞋", "限量版", "工装时尚"};

        TextRycAdapter adapter = new TextRycAdapter();
        adapter.setTextArray(textArray);

        GridLayoutManager manager = new GridLayoutManager(context, 5);
        holder.key_word_rv.setLayoutManager(manager);
        holder.key_word_rv.setAdapter(adapter);
    }

    private void setupEntranceRv(ViewHolder holder) {
        int[] imageResIdArray = {
                R.drawable.ic_home_ent_new_product, R.drawable.ic_home_ent_category,
                R.drawable.ic_home_ent_ticket, R.drawable.ic_home_ent_notice,
                R.drawable.ic_home_ent_track
        };

        ImageRycAdapter adapter = new ImageRycAdapter();
        adapter.setImageResIdArray(imageResIdArray);

        GridLayoutManager manager = new GridLayoutManager(context, imageResIdArray.length);
        ScreenSize size = new ScreenSize(context);
        int s_h = size.getHeight();
        int l_h = s_h / 10;
        holder.entrance_rv.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, l_h + 20));
        holder.entrance_rv.setLayoutManager(manager);
        holder.entrance_rv.setAdapter(adapter);
    }

    private void setupAdsViewPager(ViewHolder holder, int position) {

    }

    private void setupAdsImageView(ViewHolder holder) {
        int[] drawableResIdArray = {
                R.drawable.ads_img2, R.drawable.ads_img1,
                R.drawable.ads_img8, R.drawable.ads_img7,

                R.drawable.ads_img3, R.drawable.ads_img4,
                R.drawable.ads_img6};

        for (int i = 0; i < holder.ad_imgList.size(); i++) {
            holder.ad_imgList.get(i).setImageBitmap(
                    Methods.decodeSampledBitmapFromResource(context.getResources(), drawableResIdArray[i], View.MeasureSpec.EXACTLY, View.MeasureSpec.EXACTLY));
        }
    }
}
