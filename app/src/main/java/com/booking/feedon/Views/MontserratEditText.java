package com.booking.feedon.Views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

import com.booking.feedon.R;
import com.booking.feedon.Utils.Montserrat;

/**
 * Created by Ajeet Kumar Meena on 18-06-2016.
 */
public class MontserratEditText extends EditText {

    public MontserratEditText(Context context) {
        super(context);
        if( isInEditMode() ) return;
        parseAttributes(null);
    }

    public MontserratEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        if( isInEditMode() ) return;
        parseAttributes(attrs);
    }

    public MontserratEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        if( isInEditMode() ) return;
        parseAttributes(attrs);
    }

    private void parseAttributes(AttributeSet attrs) {
        int typeface;
        if( attrs == null ) { //Not created from xml
            typeface = Montserrat.MONTSERRAT_REGULAR;
        } else {
            TypedArray values = getContext().obtainStyledAttributes(attrs, R.styleable.MontserratTextView);
            typeface = values.getInt(R.styleable.MontserratTextView_typeface, Montserrat.MONTSERRAT_REGULAR);
            values.recycle();
        }
        setTypeface(getRoboto(typeface));
    }

    public void setRobotoTypeface(int typeface) {
        setTypeface(getRoboto(typeface));
    }

    private Typeface getRoboto(int typeface) {
        return Montserrat.getRoboto(getContext(), typeface);
    }
}
