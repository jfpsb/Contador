package com.vandamodaintima.jfpsb.contador.util;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

public class EditTextMask implements TextWatcher {
    private Boolean isUpdating = true;
    private EditText editText;
    private String mask;
    public static final String CNPJ = "##.###.###/####-##";
    private static StringBuilder formatado = new StringBuilder();

    public EditTextMask(EditText editText, String mask) {
        this.editText = editText;
        this.mask = mask;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if(isUpdating) {
            String input = unmask(s.toString());
            formatado.setLength(0);
            int index = 0;

            for (int i = 0; i < mask.length(); i++) {
                char c = mask.charAt(i);

                try {
                    if (c == '#') {
                        formatado.append(input.charAt(index));
                        index++;
                    } else {
                        formatado.append(c);
                    }
                } catch (Exception e) {
                    break;
                }
            }
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
    }

    private static String unmask(final String s) {
        return s.replaceAll("[.]", "").replaceAll("[-]", "").replaceAll("[/]", "").replaceAll("[(]", "").replaceAll("[ ]","").replaceAll("[:]", "").replaceAll("[)]", "");
    }

    public static String mask(String input, String mask) {
        formatado.setLength(0);
        int index = 0;

        for (int i = 0; i < mask.length(); i++) {
            char c = mask.charAt(i);

            try {
                if (c == '#') {
                    formatado.append(input.charAt(index));
                    index++;
                } else {
                    formatado.append(c);
                }
            } catch (Exception e) {
                break;
            }
        }

        return formatado.toString();
    }
}
