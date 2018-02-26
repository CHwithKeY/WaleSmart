package com.waletech.walesmart.shop.exp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.waletech.walesmart.R;
import com.waletech.walesmart.http.HttpSet;
import com.waletech.walesmart.product.Product_Act;
import com.waletech.walesmart.publicClass.BitmapCache;
import com.waletech.walesmart.publicClass.Methods;
import com.waletech.walesmart.publicObject.ObjectShoe;
import com.waletech.walesmart.publicSet.IntentSet;

import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by spilkaka on 2017/8/6.
 */

public class CabinetMatchUtil implements View.OnClickListener {

//    private ArrayList<ObjectShoe> shoeList;

    private Context context;
    private Activity activity;

    private ArrayList<ObjectSmark> smarkList;

    private ExpCabinetAction expCabinetAction;
    private BitmapCache cache;

    public CabinetMatchUtil(ArrayList<ObjectSmark> smarkList, Context context) {
        this.smarkList = smarkList;

        this.context = context;
        activity = (Activity) context;

        expCabinetAction = new ExpCabinetAction(context);
        cache = new BitmapCache();
    }

    public void match() {
        for (int i = 0; i < smarkList.size(); i++) {
            ObjectSmark smark = smarkList.get(i);

            String smark_num = smark.getSmarkNum();
            String id_name = "cab_" + smark_num;

            int id = context.getResources().getIdentifier(id_name, "id", context.getApplicationContext().getPackageName());

            View view = activity.findViewById(id);

            ImageView shoe_img = (ImageView) view.findViewById(R.id.exp_rv_img);
            TextView smark_num_tv = (TextView) view.findViewById(R.id.exp_rv_smark_num_tv);
            TextView status_tv = (TextView) view.findViewById(R.id.exp_rv_status_tv);
            TextView params_tv = (TextView) view.findViewById(R.id.exp_rv_params_tv);

            //
            {
                smark_num_tv.setText(smark_num);
            }

            if (smark.getSmarkType().equals("st0002")) {
//                setCabinetExpand(view, smark_num);
                ArrayList<ObjectShoe> shoeList = smark.getShoeList();
//                Log.i("Result", "size is : " + shoeList.size());
//                Log.i("Result", "shoeList is : " + shoeList);

                if (shoeList != null && shoeList.size() == 2) {
                    ObjectShoe shoe = shoeList.get(0);

                    if (shoe.getImagePath() != null && (!shoe.getImagePath().equals(""))) {
                        Methods.downloadImage(shoe_img, HttpSet.BASE_URL + shoe.getImagePath(), cache);
                    }
                    {
                        String params_str = shoe.getBrand() + "\t" + shoe.getSize();
                        params_tv.setText(params_str);
                    }

                    ObjectShoe shoe2 = shoeList.get(1);
                    ImageView shoe_img2 = (ImageView) view.findViewById(R.id.exp_rv_img2);
                    TextView params_tv2 = (TextView) view.findViewById(R.id.exp_rv_params_tv2);

                    if (shoe2.getImagePath() != null && (!shoe2.getImagePath().equals(""))) {
                        Methods.downloadImage(shoe_img2, HttpSet.BASE_URL + shoe2.getImagePath(), cache);
                    }
                    //
                    {
                        String params_str = shoe2.getBrand() + "\t" + shoe2.getSize();
                        params_tv2.setText(params_str);
                    }

                    {
                        switch (shoe.getStatus()) {
                            case "暂无鞋":
                            case "No shoes":
                                status_tv.setTextColor(context.getResources().getColor(android.R.color.darker_gray));
                                break;

                            case "在柜":
                            case "In cabinet":
                                status_tv.setTextColor(context.getResources().getColor(R.color.colorSpecial));
                                shoe_img.setBackgroundColor(context.getResources().getColor(android.R.color.transparent));
                                shoe_img2.setBackgroundColor(context.getResources().getColor(android.R.color.transparent));
                                break;

                            case "试鞋中":
                            case "Enjoying":
                                status_tv.setTextColor(context.getResources().getColor(android.R.color.holo_red_light));
                                shoe_img.setBackgroundColor(context.getResources().getColor(android.R.color.transparent));
                                shoe_img2.setBackgroundColor(context.getResources().getColor(android.R.color.transparent));
                                break;
                        }

                        status_tv.setText(shoe.getStatus());
                    }

                    shoe_img.setTag(shoe.getEpcCode());
                    shoe_img2.setTag(shoe2.getEpcCode());

                    shoe_img.setOnClickListener(this);
                    shoe_img2.setOnClickListener(this);
                }
            } else {
                Log.i("Reuslt", "setup normal smark");
                ArrayList<ObjectShoe> shoeList = smark.getShoeList();
                for (int j = 0; j < shoeList.size(); j++) {
                    ObjectShoe shoe = shoeList.get(j);

                    if (shoe.getEpcCode() == null) {
                        continue;
                    }

                    //
                    {
                        if (shoe.getImagePath() != null && (!shoe.getImagePath().equals(""))) {
                            Methods.downloadImage(shoe_img, HttpSet.BASE_URL + shoe.getImagePath(), cache);
                        }
                    }

                    //
                    {
                        switch (shoe.getStatus()) {
                            case "暂无鞋":
                            case "No shoes":
                                status_tv.setTextColor(context.getResources().getColor(android.R.color.darker_gray));
                                break;

                            case "在柜":
                            case "In cabinet":
                                status_tv.setTextColor(context.getResources().getColor(R.color.colorSpecial));
                                shoe_img.setBackgroundColor(context.getResources().getColor(android.R.color.transparent));

                                break;

                            case "试鞋中":
                            case "Enjoying":
                                status_tv.setTextColor(context.getResources().getColor(android.R.color.holo_red_light));
                                shoe_img.setBackgroundColor(context.getResources().getColor(android.R.color.transparent));

                                break;
                        }

                        status_tv.setText(shoe.getStatus());
                    }

                    //
                    {
                        String params_str = shoe.getBrand() + "\t" + shoe.getSize();
                        params_tv.setText(params_str);
                    }

                    shoe_img.setTag(shoe.getEpcCode());
                    shoe_img.setOnClickListener(this);
                }
            }
        }
    }

    private void setCabinetExpand(View view, final String smark_num) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expCabinetAction.getCabinetStuff(smark_num);
//                ArrayList<ObjectShoe> shoeList = new ArrayList<ObjectShoe>();
//
//                View view = View.inflate(context, R.layout.dialog_exp_shop_cabinet_expand_view, null);
//
//                for (int i = 0; i < 3; i++) {
//                    ObjectShoe shoe = new ObjectShoe();
////                    shoe.
//                }
////                shoeList.set(0, )
//                AreaRycAdapter adapter = new AreaRycAdapter(shoeList, 3, 1);
//
//                RecyclerView expand_rv = (RecyclerView) view.findViewById(R.id.dialog_exp_shop_cabinet_expand_rv);
//                expand_rv.setAdapter(adapter);
//
//                new AlertDialog.Builder(context).setView(view).show();
////                new AlertDialog.Builder(context).setTitle("Large Cab").setMessage("More Goods").show();
            }
        });
    }

    public void ExpandCabinet(String result) throws JSONException {
        ArrayList<ObjectShoe> shoeList = expCabinetAction.handleCabinetStuffResponse(result);
        final View view = View.inflate(context, R.layout.dialog_exp_shop_cabinet_expand_view, null);

        Log.i("Result", "shoeList is : " + shoeList.size());
        ExpAreaRycAdapter adapter = new ExpAreaRycAdapter(shoeList, 2, 3);
        adapter.setCabinetDialog(true);
        final RecyclerView record_rv = (RecyclerView) view.findViewById(R.id.dialog_exp_shop_cabinet_expand_rv);
        record_rv.setLayoutManager(new GridLayoutManager(context, 3));
        record_rv.setAdapter(adapter);

        new AlertDialog.Builder(context).setView(view).show();
    }

    @Override
    public void onClick(View v) {
        String epc_code = Methods.cast(v.getTag());

        Intent product_int = new Intent(context, Product_Act.class);
        product_int.putExtra(IntentSet.KEY_IS_FROM_SHOP, true);
        product_int.putExtra(IntentSet.KEY_EPC_CODE, epc_code);
        context.startActivity(product_int);
    }
}
