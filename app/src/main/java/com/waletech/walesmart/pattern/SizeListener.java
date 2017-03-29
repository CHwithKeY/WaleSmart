package com.waletech.walesmart.pattern;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Button;

import com.waletech.walesmart.R;
import com.waletech.walesmart.base.BaseAction;
import com.waletech.walesmart.base.BaseClickListener;
import com.waletech.walesmart.product.Product_Act;
import com.waletech.walesmart.publicClass.LineToast;

import java.util.ArrayList;

/**
 * Created by KeY on 2016/7/13.
 */
public class SizeListener extends BaseClickListener {

    private ArrayList<Button> buttonList;
    private PatternAction patternAction;

    private Pattern_Frag pattern_frag;

    private LineToast toast;

    public SizeListener(Context context, BaseAction baseAction) {
        super(context, baseAction);

        toast = new LineToast(context);
    }

    public void setButtonList(ArrayList<Button> buttonList) {
        this.buttonList = buttonList;
    }

    public void setPatternFrag(Pattern_Frag pattern_frag) {
        this.pattern_frag = pattern_frag;
    }

    public void setPatternAction(PatternAction patternAction) {
        this.patternAction = patternAction;
    }

    @Override
    public void onClick(View v) {

        if (!pattern_frag.getIsColorChosen()) {
            toast.showToast(context.getString(R.string.pattern_toast_size_btn));
            return;
        }

        Button btn = (Button) v;
        btn.setBackgroundResource(R.drawable.main_check_btn);
        // patternAction.getColorMatch(btn.getText().toString());
        patternAction.getShoeDetails(btn.getText().toString());

        int tag = (int) v.getTag();
        for (int i = 0; i < buttonList.size(); i++) {
            int btn_tag = (int) buttonList.get(i).getTag();

            if (btn_tag != tag) {
                buttonList.get(i).setBackgroundResource(R.drawable.main_uncheck_btn);
            }
        }
    }
}
