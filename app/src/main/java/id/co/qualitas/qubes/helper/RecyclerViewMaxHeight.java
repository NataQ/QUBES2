package id.co.qualitas.qubes.helper;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import id.co.qualitas.qubes.utils.Utils;

public class RecyclerViewMaxHeight extends RecyclerView {

    public RecyclerViewMaxHeight(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {
        heightSpec = MeasureSpec.makeMeasureSpec(Utils.dpsToPixels(300, getResources()), MeasureSpec.AT_MOST);
        super.onMeasure(widthSpec, heightSpec);
    }
}
