package toong.vn.androidthousandnumberedittext;

import android.content.Context;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.EditText;
import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * Created by PhanVanLinh on 14/09/2017.
 * phanvanlinh.94vn@gmail.com
 */

public class ThousandNumberEditText extends android.support.v7.widget.AppCompatEditText {
    // TODO: 14/09/2017 change it if you want
    private static final int MAX_LENGTH = 20;
    private static final int MAX_DECIMAL = 3;

    public ThousandNumberEditText(Context context) {
        this(context, null);
    }

    public ThousandNumberEditText(Context context, AttributeSet attrs) {
        this(context, attrs, android.support.v7.appcompat.R.attr.editTextStyle);
    }

    public ThousandNumberEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        addTextChangedListener(new ThousandNumberTextWatcher(this));
        setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        setFilters(new InputFilter[] { new InputFilter.LengthFilter(MAX_LENGTH) });
        setHint("0"); // TODO: 14/09/2017 change it if you want
    }

    private static class ThousandNumberTextWatcher implements TextWatcher {

        private EditText mEditText;

        ThousandNumberTextWatcher(EditText editText) {
            mEditText = editText;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
            String originalString = editable.toString();
            String cleanString = originalString.replaceAll("[,]", "");
            if (cleanString.isEmpty()) {
                return;
            }
            String formattedString = getFormatString(cleanString);

            mEditText.removeTextChangedListener(this);
            mEditText.setText(formattedString);
            mEditText.setSelection(mEditText.getText().length());
            mEditText.addTextChangedListener(this);
        }

        /**
         * Return the format string
         */
        private String getFormatString(String cleanString) {
            if (cleanString.contains(".")) {
                return formatDecimal(cleanString);
            } else {
                return formatInteger(cleanString);
            }
        }

        private String formatInteger(String str) {
            BigDecimal parsed = new BigDecimal(str);
            DecimalFormat formatter;
            formatter = new DecimalFormat("#,###");
            return formatter.format(parsed);
        }

        private String formatDecimal(String str) {
            if (str.equals(".")) {
                return ".";
            }
            BigDecimal parsed = new BigDecimal(str);
            DecimalFormat formatter;
            formatter =
                    new DecimalFormat("#,###." + getDecimalPattern(str)); //example patter #,###.00
            return formatter.format(parsed);
        }

        /**
         * It will return suitable pattern for format decimal
         * For example: 10.2 -> return 0 | 10.23 -> return 00 | 10.235 -> return 000
         */
        private String getDecimalPattern(String str) {
            int decimalCount = str.length() - 1 - str.indexOf(".");
            StringBuilder decimalPattern = new StringBuilder();
            for (int i = 0; i < decimalCount && i < MAX_DECIMAL; i++) {
                decimalPattern.append("0");
            }
            return decimalPattern.toString();
        }
    }
}
