package id.co.qualitas.qubes.helper;


import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class RupiahTextView extends TextView {

    String rawText;

    public RupiahTextView(Context context) {
        super(context);

    }

    public RupiahTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RupiahTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        rawText = text.toString();
        String formatString = text.toString();
        StringBuilder rupiah = new StringBuilder();
        try {

            DecimalFormatSymbols symbols = new DecimalFormatSymbols();
            symbols.setDecimalSeparator(',');
            DecimalFormat decimalFormat = new DecimalFormat("###,###,###,###", symbols);
            formatString = decimalFormat.format(Integer.parseInt(text.toString()));
            rupiah.append("Rp. ");
            rupiah.append(formatString).toString();
            rupiah.append(",-");

        }catch (Exception e){}

        super.setText(rupiah.toString(), type);
    }

    @Override
    public CharSequence getText() {

        return rawText;
    }
}