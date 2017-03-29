package com.waletech.walesmart.pattern;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.waletech.walesmart.R;
import com.waletech.walesmart.base.BaseAction;
import com.waletech.walesmart.base.BaseClickListener;

import java.util.ArrayList;

/**
 * Created by KeY on 2016/7/13.
 */
public class ColorListener extends BaseClickListener {

    private ArrayList<Button> buttonList;
    private PatternAction patternAction;

    private Pattern_Frag pattern_frag;

    public void setPatternFrag(Pattern_Frag pattern_frag) {
        this.pattern_frag = pattern_frag;
    }

    public ColorListener(Context context, BaseAction baseAction) {
        super(context, baseAction);
    }

    public void setButtonList(ArrayList<Button> buttonList) {
        this.buttonList = buttonList;
    }

    public void setPatternAction(PatternAction patternAction) {
        this.patternAction = patternAction;
    }

    @Override
    public void onClick(View v) {
        Button btn = (Button) v;
        btn.setBackgroundResource(R.drawable.main_check_btn);

        pattern_frag.setIsColorChosen(true);

        Log.i("Result", "btn getText is : " + btn.getText());
        patternAction.getSizeMatch(btn.getText().toString());
        patternAction.getImageMatch(btn.getText().toString());
        patternAction.setSaveSize("");

        // brand = btn.getText().toString();

        int tag = (int) v.getTag();
        for (int i = 0; i < buttonList.size(); i++) {
            int btn_tag = (int) buttonList.get(i).getTag();

            if (btn_tag != tag) {
                buttonList.get(i).setBackgroundResource(R.drawable.main_uncheck_btn);
            }
        }
    }
}
