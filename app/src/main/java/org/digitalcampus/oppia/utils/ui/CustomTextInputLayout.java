package org.digitalcampus.oppia.utils.ui;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.google.android.material.textfield.TextInputLayout;

import org.digitalcampus.mobile.learning.R;

public class CustomTextInputLayout extends TextInputLayout {
    public CustomTextInputLayout(@NonNull Context context) {
        super(context);
        init();
    }


    public CustomTextInputLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomTextInputLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    private void init() {

        setDefaultHintTextColor(ContextCompat.getColorStateList(getContext(), R.color.form_label));

        addOnEditTextAttachedListener(new OnEditTextAttachedListener() {
            @Override
            public void onEditTextAttached(TextInputLayout textInputLayout) {

                getEditText().setFocusable(true);
                getEditText().setFocusableInTouchMode(true);

                getEditText().setOnFocusChangeListener(new OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {

                        boolean selected = !TextUtils.isEmpty(getEditText().getText().toString());
                        CustomTextInputLayout.this.setSelected(selected);

                    }
                });
            }
        });

    }

}
