package com.thefrenchtouch.pbtimelineview.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import com.thefrenchtouch.pbtimelineview.R;
import com.thefrenchtouch.pbtimelineview.object.PBItem;

/**
 * TODO: In a future release if we want design items programmatically no layout for items in the tree.
 */
public class PBItemView extends View {

    // Data objects
    private PBItem item;

    // UI objects
    private Paint mItemPaint;

    public PBItemView(Context context) {
        super(context);
        init(null, 0);
    }

    public PBItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public PBItemView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    public PBItemView(Context context, PBItem item) {
        super(context);
        this.item = item;
        init(null, 0);
    }

    /**
     * Initialize PBItemView
     * */
    private void init(AttributeSet attrs, int defStyle) {
        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.PBItemView, defStyle, 0);

        // Get attributes here
        // For now ... NONE

        a.recycle();

        // Set up a default Paint object
        mItemPaint = new Paint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Prepare paint for rectangle
        Paint mItemPaint = new Paint();
        mItemPaint.setColor(item.getBackgroundColor());
        mItemPaint.setStyle(Paint.Style.FILL);

        // Draw rectangle
        canvas.drawRect(0, 0, item.getItemWidth(), item.getItemHeight(), mItemPaint);

        // Prepare paint fo text
        mItemPaint.setColor(item.getTextColor());
        mItemPaint.setTextAlign(Paint.Align.CENTER);
        mItemPaint.setTextSize(item.getTextSize());
        Rect textSize = new Rect();
        mItemPaint.getTextBounds(item.getText(), 1, item.getText().length(), textSize);
        // And add some text in it
        canvas.drawText(item.getText(), (item.getItemWidth() / 2), (item.getItemHeight() / 2), mItemPaint);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // Re-measure
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        setMeasuredDimension(width, height);
    }
}
