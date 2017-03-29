package com.waletech.walesmart;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.waletech.walesmart.base.BaseAction;
import com.waletech.walesmart.http.HttpAction;
import com.waletech.walesmart.http.HttpSet;
import com.waletech.walesmart.product.Product_Act;
import com.waletech.walesmart.publicClass.BitmapCache;
import com.waletech.walesmart.publicClass.Methods;
import com.waletech.walesmart.publicObject.ObjectShoe;
import com.waletech.walesmart.publicSet.IntentSet;

import java.util.ArrayList;

/**
 * Created by KeY on 2016/6/29.
 */
public abstract class BaseShoeRycAdapter extends RecyclerView.Adapter<BaseShoeRycAdapter.ViewHolder> implements View.OnClickListener {

    public final static String GENDER_MALE = "男鞋";
    public final static String GENDER_FEMALE = "女鞋";
    public final static String GENDER_GENERAL = "通用";

    protected ArrayList<ObjectShoe> shoeList;
    private BitmapCache cache;

    protected Context context;

    protected boolean isShowLoad;
    protected BaseAction baseAction;

    public BaseShoeRycAdapter(ArrayList<ObjectShoe> shoeList, Context context) {
        this.shoeList = shoeList;
        this.context = context;

        cache = new BitmapCache();
    }

    public void setShoeList(ArrayList<ObjectShoe> shoeList) {
        this.shoeList = shoeList;
    }

    public void setIsShowLoad(boolean isShowLoad) {
        this.isShowLoad = isShowLoad;
    }

    public boolean getIsShowLoad() {
        return isShowLoad;
    }

    public void setBaseAction(BaseAction baseAction) {
        this.baseAction = baseAction;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.base_shoe_item, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        if (shoeList == null) {
            return;
        }

        ObjectShoe shoe = shoeList.get(position);
        holder.item_ll.setTag(position);
        holder.item_ll.setOnClickListener(this);

        String name_str = shoe.getBrand() + "\t" + shoe.getDesign();
        holder.name_tv.setText(name_str);

        String params_str = shoe.getColor() + "\t" + shoe.getSize();
        holder.params_tv.setText(params_str);

        switch (shoe.getGender()) {
            case GENDER_MALE:
            case "Male":
                holder.gender_img.setVisibility(View.VISIBLE);
                holder.gender_img.setImageResource(R.drawable.shoe_gender_male);
                break;

            case GENDER_FEMALE:
            case "Female":
                holder.gender_img.setVisibility(View.VISIBLE);
                holder.gender_img.setImageResource(R.drawable.shoe_gender_female);
                break;

            case GENDER_GENERAL:
            case "General":
                holder.gender_img.setVisibility(View.GONE);
                break;

            default:
                break;
        }

        holder.shoe_img.setTag(HttpSet.BASE_URL + shoe.getImagePath());
        loadImage(holder, position);
    }

    @Override
    public int getItemCount() {
        return shoeList == null ? 0 : shoeList.size();
    }

    @Override
    public void onClick(View v) {
        Context context = v.getContext();

        if (!HttpAction.checkNet(context)) {
            return;
        }

        int position = Methods.cast(v.getTag());

        ObjectShoe shoe = shoeList.get(position);
        String epc_code_str = shoe.getEpcCode();

        Intent product_int = new Intent(context, Product_Act.class);
        product_int.putExtra(IntentSet.KEY_EPC_CODE, epc_code_str);
        context.startActivity(product_int);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public LinearLayout item_ll;

        public ImageView shoe_img;
        public ImageView gender_img;

        public TextView name_tv;
        public TextView params_tv;

        public ViewHolder(View itemView) {
            super(itemView);

            item_ll = (LinearLayout) itemView.findViewById(R.id.rv_ll);

            shoe_img = (ImageView) itemView.findViewById(R.id.rv_img);
            gender_img = (ImageView) itemView.findViewById(R.id.rv_gender_img);

            name_tv = (TextView) itemView.findViewById(R.id.rv_name_tv);
            params_tv = (TextView) itemView.findViewById(R.id.rv_params_tv);
        }
    }

    private void loadImage(ViewHolder holder, int position) {

        // 先判断缓存中是否缓存了这个item的imageview所在的url的图片
        String img_url = holder.shoe_img.getTag().toString();

        // 如果没有
        if (cache.getBitmap(img_url) == null) {
            // 请求下载图片
            // downloadImage(holder, position, cache);
            Methods.downloadImage(holder.shoe_img, HttpSet.BASE_URL + shoeList.get(position).getImagePath(), cache);
        } else {
            holder.shoe_img.setImageBitmap(cache.getBitmap(img_url));
        }

    }

//    private void downloadImage(ViewHolder holder, int position, BitmapCache cache) {
//        RequestQueue queue = Volley.newRequestQueue(holder.name_tv.getContext());
//
//        ImageLoader loader = new ImageLoader(queue, cache);
//
//        ImageLoader.ImageListener listener = ImageLoader.getImageListener(holder.shoe_img, R.mipmap.ic_launcher, R.mipmap.ic_launcher);
//
//        loader.get(HttpSet.BASE_URL + shoeList.get(position).getImagePath(), listener);
//    }

}
