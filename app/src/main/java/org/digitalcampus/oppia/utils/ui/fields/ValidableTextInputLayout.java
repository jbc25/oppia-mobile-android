package org.digitalcampus.oppia.utils.ui.fields;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;

import androidx.core.content.ContextCompat;

import com.google.android.material.textfield.TextInputLayout;

import org.digitalcampus.mobile.learning.R;

public class ValidableTextInputLayout extends TextInputLayout implements ValidableField {

    private boolean required = false;
    private boolean cantContainSpaces = false;
    private int customTextColorHint;

    public ValidableTextInputLayout(Context context) {
        super(context);
    }

    public ValidableTextInputLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        updateAttrs(context, attrs);
    }

    public ValidableTextInputLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        updateAttrs(context, attrs);
    }

    private void updateAttrs(Context context, AttributeSet attrs){
        TypedArray styledAttrs = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.ValidableTextInputLayout,
                0, 0);
        try {
            required = styledAttrs.getBoolean(R.styleable.ValidableTextInputLayout_required, false);
            cantContainSpaces = styledAttrs.getBoolean(R.styleable.ValidableTextInputLayout_cantContainSpaces, false);
            customTextColorHint = styledAttrs.getResourceId(R.styleable.ValidableTextInputLayout_customTextColorHint, 0);
        } finally {
            styledAttrs.recycle();
        }
        initialize();
    }

    public void setRequired(boolean required){
        this.required = required;
    }

    public void initialize(){

        initializeCustomTextColorHint();
    }

    private void initializeCustomTextColorHint() {

        if (customTextColorHint != 0) {

            setDefaultHintTextColor(ContextCompat.getColorStateList(getContext(), customTextColorHint));

            addOnEditTextAttachedListener(new OnEditTextAttachedListener() {
                @Override
                public void onEditTextAttached(TextInputLayout textInputLayout) {

                    getEditText().setFocusable(true);
                    getEditText().setFocusableInTouchMode(true);

                    getEditText().setOnFocusChangeListener(new OnFocusChangeListener() {
                        @Override
                        public void onFocusChange(View v, boolean hasFocus) {

                            boolean selected = !TextUtils.isEmpty(getEditText().getText().toString());
                            ValidableTextInputLayout.this.setSelected(selected);

                        }
                    });
                }
            });
        }
    }

    public void setCustomtHintTextColor(int color) {
        this.customTextColorHint = color;
        initializeCustomTextColorHint();
    }

    public boolean validate(){
        EditText input = getEditText();
        if (input == null || this.getVisibility() == GONE){
            return true;
        }
        String text = input.getText().toString().trim();
        boolean valid = true;
        if (required && (text.length() == 0)){
            this.setErrorEnabled(true);
            this.setError(getContext().getString(R.string.field_required));
            valid = false;
        }
        else if (cantContainSpaces && (text.contains(" ") )){
            this.setErrorEnabled(true);
            this.setError(getContext().getString(R.string.field_spaces_error));
            valid = false;
        }
        if (valid){
            this.setError(null);
            this.setErrorEnabled(false);
        }
        return valid;
    }

    public String getCleanedValue(){
        EditText input = getEditText();
        if (input == null){
            return null;
        }
        return input.getText().toString().trim();
    }

    @Override
    public void setChangeListener(onChangeListener listener) {

    }

    public void setText(String text) {
        EditText input = getEditText();
        if (input != null){
            input.setText(text);
        }
    }


}
