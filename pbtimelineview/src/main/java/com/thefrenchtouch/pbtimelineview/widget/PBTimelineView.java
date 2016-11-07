package com.thefrenchtouch.pbtimelineview.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RelativeLayout;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import com.thefrenchtouch.pbtimelineview.R;
import com.thefrenchtouch.pbtimelineview.adapter.PBTimelineAdapter;
import com.thefrenchtouch.pbtimelineview.object.PBItem;

/**
 * Created by Paul on 09/04/16.
 */
public class PBTimelineView extends RelativeLayout {

    /* Sizes */
    private int width;
    private int height;
    private int widthItem;
    private int heightItem;
    private int mainTextSize;
    private int textSize;
    private int textSizeItem;
    private int deviceWidth;
    private int deviceHeight;

    private int radiusRoundRectX;
    private int radiusRoundRectY;
    private int padding = 20;

    /* Structure */
    private int numberOfPoints;
    private int radiusPoint;
    private int strokeLine;
    private int leftOffset;
    private int step;

    /* Calendar */
    private boolean isCalendarEnabled = false;
    private String[] texts;
    private String title;

    /* Colors */
    private int backgroudColorItems;
    private int lineColor;
    private int pointColor;
    private int mainTextColor;
    private int textColor;
    private int textColorItem;

    /* Texts */
    private String[][] textItems;
    private ArrayList<RecyclerView> listRecyclerView;

    private onItemClickListener listener;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public PBTimelineView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setupConfiguration(attrs);
    }

    public PBTimelineView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setupConfiguration(attrs);
    }

    public PBTimelineView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setupConfiguration(attrs);
    }

    public PBTimelineView(Context context) {
        super(context);
        setupConfiguration(null);
    }

    /**
     * Initialize the view with some configurations
     */
    private void setupConfiguration(AttributeSet attrs) {
        final Display display = ((WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        Point deviceDisplay = new Point();
        display.getSize(deviceDisplay);
        deviceWidth = deviceDisplay.x;
        deviceHeight = deviceDisplay.y;

        TypedArray a = getContext().obtainStyledAttributes(
                attrs,
                R.styleable.PBTimelineView,
                0, 0
        );

        // Get attributes
        try {
            widthItem = a.getLayoutDimension(R.styleable.PBTimelineView_widthItem, 100);
            heightItem = a.getLayoutDimension(R.styleable.PBTimelineView_heightItem, widthItem / 2);

            numberOfPoints = a.getInteger(R.styleable.PBTimelineView_numberOfPoints, 7);
            radiusPoint = a.getLayoutDimension(R.styleable.PBTimelineView_radiusPoint, 5);
            strokeLine = a.getLayoutDimension(R.styleable.PBTimelineView_strokeLine, 3);
            leftOffset = a.getLayoutDimension(R.styleable.PBTimelineView_leftOffset, 50);

            backgroudColorItems = a.getColor(R.styleable.PBTimelineView_backgroudColorItems, Color.BLACK);
            lineColor = a.getColor(R.styleable.PBTimelineView_lineColor, Color.BLACK);
            pointColor = a.getColor(R.styleable.PBTimelineView_pointColor, Color.BLACK);
            mainTextColor = a.getColor(R.styleable.PBTimelineView_mainTextColor, Color.BLACK);
            textColor = a.getColor(R.styleable.PBTimelineView_textColor, Color.BLACK);
            textColorItem = a.getColor(R.styleable.PBTimelineView_textColorItem, Color.WHITE);

            mainTextSize = a.getDimensionPixelSize(R.styleable.PBTimelineView_mainTextSize, 28);
            textSize = a.getDimensionPixelSize(R.styleable.PBTimelineView_textSize, 15);
            textSizeItem = a.getDimensionPixelSize(R.styleable.PBTimelineView_textSizeItem, 15);

            isCalendarEnabled = a.getBoolean(R.styleable.PBTimelineView_isCalendarEnabled, true);
            title = a.getString(R.styleable.PBTimelineView_mainText);

            texts = getResources().getStringArray((a.getResourceId(R.styleable.PBTimelineView_texts, 0)));

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            a.recycle();
        }

        // Size for items and compute steps
        radiusRoundRectX = 5;
        radiusRoundRectY = radiusRoundRectX;
        step = (2 * heightItem) + (2 * padding);

        // If the user has choosen calendar timeline view then update the text with calendar information automatically
        if (isCalendarEnabled) {
            updateCalendar();
        }
    }

    private void updateCalendar() {
        numberOfPoints = 7; // Represent the next days, here the 7 nest days
        texts = new String[numberOfPoints];
        Calendar calendar = Calendar.getInstance();
        String[] days = new String[]{"Dimanche", "Lundi", "Mardi", "Mercredi", "Jeudi", "Vendredi", "Samedi"}; // TODO: Handle multilanguage support

        // Example for title: Ven. 4 Avril
        String dayName = new SimpleDateFormat("EE").format(calendar.getTime());
        int dayNum = calendar.get(Calendar.DAY_OF_MONTH);
        String monthName = new SimpleDateFormat("MMM").format(calendar.getTime());
        title = dayName + " " + dayNum + " " + monthName;

        for (int i = 1; i < numberOfPoints; i++) {
            calendar.add(Calendar.DAY_OF_MONTH, 1);

            String day = new SimpleDateFormat("EE").format(calendar.getTime());
            ;
            this.texts[i - 1] = day + ".";
        }
    }

    /**
     * Draw the items of the timeline view
     */
    public void drawBoxes() {
        // If NONE then return
        if (textItems == null) {
            return;
        }

        listRecyclerView = new ArrayList<RecyclerView>();

        for (int i = 1; i < numberOfPoints; i++) {

            try {
                /* DEPRECATED - Method 1 : (Not Scrollable Without RecyclerViews)
                // Place horizontal items view -> not scrollable
                // Be careful addView recall onDraw method infinite loop
                    int viewWidth = getMeasuredWidth();
                    int viewHeight = getMeasuredHeight();

                    for(int j = 0; j < textItems[i-1].length; j++) {
                    Item item = new Item(getContext(), i, j, textItems[i - 1][j]);
                    item.setLeft(leftOffset + padding + (j * widthItem) + (j*padding));
                    item.setTop((i * step) - heightItem / 2);
                    item.setRight(leftOffset + ((j + 1) * widthItem));
                    item.setBottom((i * step) + heightItem / 2);
                    item.layout((int) leftOffset + padding + +(j * widthItem) + (j*padding),
                            (int) (i * step) - heightItem / 2,
                            (int) leftOffset + widthItem + ((j + 1) * widthItem),
                            (int) (i * step) + heightItem / 2);
                    this.addView(item);
                }*/

                /* Method 2 : (Scrollable) */
                RecyclerView mRecyclerView = new RecyclerView(getContext());
                mRecyclerView.setHasFixedSize(true);
                mRecyclerView.setId(("rv" + i).hashCode());

                // Get all the items from texts
                ArrayList<PBItem> items = new ArrayList<PBItem>();
                for (int j = 0; j < textItems[i - 1].length; j++) {
                    String text = textItems[i - 1][j];
                    // For each text create an item
                    PBItem item = new PBItem(widthItem, heightItem, i, j, text, backgroudColorItems, textSizeItem, textColorItem);
                    // and add it
                    items.add(item);
                }

                // Create the adapter to handle the items
                PBTimelineAdapter mAdapter = new PBTimelineAdapter(getContext(), items, listener);

                // Set layout manager of the recycler view
                LinearLayoutManager mLinearLayoutManager
                        = new LinearLayoutManager(getContext());
                mLinearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

                mRecyclerView.setLayoutManager(mLinearLayoutManager); // Set the layout manager
                mRecyclerView.setAdapter(mAdapter); // Set adapter of the recycler view

                // Do not forget to add the recylcler views
                this.addView(mRecyclerView);
                this.listRecyclerView.add(mRecyclerView);

            } catch (ArrayIndexOutOfBoundsException e) {
                Log.i("DrawBoxes", "PBTimelineView section " + i + " has no texts");
            }
        }
    }

    /**
     * Change the texts of the items in the timeline view
     * */
    public void setTextItems(String[][] textItems) {
        this.textItems = textItems;

        if (listRecyclerView != null) {
            notifyDataSetChanged();
        } else {
            drawBoxes();
        }
    }

    /**
     * Draw the timelineview
     * @param canvas The canvas where the timelineview is drawned
     * */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Prepare paint to write title
        Paint mLinePaint = new Paint();
        mLinePaint.setColor(mainTextColor);
        mLinePaint.setStyle(Paint.Style.FILL);
        mLinePaint.setTextSize(mainTextSize);
        mLinePaint.setStrokeWidth(strokeLine);

        // Write title
        Rect textBounds = new Rect();
        mLinePaint.getTextBounds(title, 1, title.length(), textBounds);
        canvas.drawText(title, leftOffset + padding, textBounds.height() + padding, mLinePaint);

        // Draw vertical line
        mLinePaint.setColor(lineColor);
        canvas.drawLine(leftOffset, 0, leftOffset, height, mLinePaint);

        // Draw dots to the intersection
        for (int i = 1; i < numberOfPoints; i++) {
            // Draw date or user's custom text at the intersection
            if (texts != null) {
                // Prepare paint
                mLinePaint.setColor(textColor);
                mLinePaint.setTextSize(textSize);
                float textWidth = mLinePaint.measureText(texts[i - 1]);
                String currentText = texts[i - 1];
                mLinePaint.getTextBounds(currentText, 1, currentText.length(), textBounds);
                // Draw date|text
                canvas.drawText(currentText, leftOffset - textWidth - padding, (i * step) + (textBounds.height() / 2), mLinePaint);
            }

            // Draw dot
            mLinePaint.setColor(pointColor);
            canvas.drawCircle(leftOffset, i * step, radiusPoint, mLinePaint);
        }
    }

    /**
     * Notify the adapter that something changed on the timeline view
     * */
    public void notifyDataSetChanged() {
        for (RecyclerView rv : listRecyclerView) {
            rv.getAdapter().notifyDataSetChanged();
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        final int count = getChildCount();
        int curWidth, curHeight, curLeft, curTop, maxHeight;

        // Get the available size of child view
        final int childLeft = this.getPaddingLeft();
        final int childTop = this.getPaddingTop();
        final int childRight = this.getMeasuredWidth() - this.getPaddingRight();
        final int childBottom = this.getMeasuredHeight() - this.getPaddingBottom();
        final int childWidth = childRight - childLeft;
        final int childHeight = childBottom - childTop;

        maxHeight = 0;
        curLeft = childLeft;
        curTop = childTop;

        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);

            if (child.getVisibility() == GONE)
                continue;

            // Get the maximum size of the child
            child.measure(MeasureSpec.makeMeasureSpec(childWidth, MeasureSpec.AT_MOST), MeasureSpec.makeMeasureSpec(childHeight, MeasureSpec.AT_MOST));
            curWidth = child.getMeasuredWidth();
            curHeight = child.getMeasuredHeight();
            // Wrap is reach to the end
            if (curLeft + curWidth >= childRight) {
                curLeft = childLeft;
                curTop += maxHeight;
                maxHeight = 0;
            }
            // Do the layout
            int recyclerViewWidth = getMeasuredWidth() - leftOffset - padding;
            int recyclerViewHeight = heightItem;
            int left = leftOffset + padding;
            int top = ((i + 1) * step) - (heightItem / 2);
            int right = getMeasuredWidth();
            int bottom = ((i + 1) * step) + (heightItem / 2);

            child.layout(left, top, right, bottom);
            // Store the max height
            if (maxHeight < curHeight)
                maxHeight = curHeight;
            curLeft += curWidth;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // Re-measure
        width = MeasureSpec.getSize(widthMeasureSpec);
        height = numberOfPoints * step;

        setMeasuredDimension(width, height);
    }

    public void setOnItemClickListener(onItemClickListener listener) {
        this.listener = listener;
    }

    /**
     * Called when an item of the timeline is clicked
     */
    public interface onItemClickListener {
        void onItemClick(int section, int num, String text, PBItem item);
    }

    /**
     * Save state on configuration change
     *
     * @return parcelable
     */
    @Override
    public Parcelable onSaveInstanceState() {
        //begin boilerplate code that allows parent classes to save state
        Parcelable superState = super.onSaveInstanceState();

        SavedState ss = new SavedState(superState);
        //end

        ss.widthItem = this.widthItem;
        ss.heightItem = this.heightItem;
        ss.mainTextSize = this.mainTextSize;
        ss.textSize = this.textSize;
        ss.textSizeItem = this.textSizeItem;
        ss.radiusRoundRectX = this.radiusRoundRectX;
        ss.radiusRoundRectY = this.radiusRoundRectY;
        ss.numberOfPoints = this.numberOfPoints;
        ss.radiusPoint = this.radiusPoint;
        ss.strokeLine = this.strokeLine;
        ss.leftOffset = this.leftOffset;
        ss.isCalendarEnabled = this.isCalendarEnabled;
        ss.texts = this.texts;
        ss.title = this.title;
        ss.backgroudColorItems = this.backgroudColorItems;
        ss.lineColor = this.lineColor;
        ss.pointColor = this.pointColor;
        ss.mainTextColor = this.mainTextColor;
        ss.textColor = this.textColor;
        ss.textColorItem = this.textColorItem;

        return ss;
    }

    /**
     * Restore state on configuration change
     *
     * @param state
     */
    @Override
    public void onRestoreInstanceState(Parcelable state) {

        if (!(state instanceof SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }

        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());


        this.widthItem = ss.widthItem;
        this.heightItem = ss.heightItem;
        this.mainTextSize = ss.mainTextSize;
        this.textSize = ss.textSize;
        this.textSizeItem = ss.textSizeItem;
        this.radiusRoundRectX = ss.radiusRoundRectX;
        this.radiusRoundRectY = ss.radiusRoundRectY;
        this.numberOfPoints = ss.numberOfPoints;
        this.radiusPoint = ss.radiusPoint;
        this.strokeLine = ss.strokeLine;
        this.leftOffset = ss.leftOffset;
        this.isCalendarEnabled = ss.isCalendarEnabled;
        this.texts = ss.texts;
        this.title = ss.title;
        this.backgroudColorItems = ss.backgroudColorItems;
        this.lineColor = ss.lineColor;
        this.pointColor = ss.pointColor;
        this.mainTextColor = ss.mainTextColor;
        this.textColor = ss.textColor;
        this.textColorItem = ss.textColorItem;
    }

    static class SavedState extends BaseSavedState {
        int widthItem;
        int heightItem;
        int mainTextSize;
        int textSize;
        int textSizeItem;

        int radiusRoundRectX;
        int radiusRoundRectY;

        int numberOfPoints;
        int radiusPoint;
        int strokeLine;
        int leftOffset;

        boolean isCalendarEnabled = false;
        String[] texts;
        String title;

        int backgroudColorItems;
        int lineColor;
        int pointColor;
        int mainTextColor;
        int textColor;
        int textColorItem;

        String[][] textItems;

        SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            this.widthItem = in.readInt();
            this.heightItem = in.readInt();
            this.mainTextSize = in.readInt();
            this.textSize = in.readInt();
            this.textSizeItem = in.readInt();
            this.radiusRoundRectX = in.readInt();
            this.radiusRoundRectY = in.readInt();
            this.numberOfPoints = in.readInt();
            this.radiusPoint = in.readInt();
            this.strokeLine = in.readInt();
            this.leftOffset = in.readInt();
            this.isCalendarEnabled = in.readByte() != 0;
            this.texts = in.createStringArray();
            this.title = in.readString();
            this.backgroudColorItems = in.readInt();
            this.lineColor = in.readInt();
            this.pointColor = in.readInt();
            this.mainTextColor = in.readInt();
            this.textColor = in.readInt();
            this.textColorItem = in.readInt();

            int N = in.readInt();
            if (N > 0) {
                String[][] array = new String[N][];
                for (int i = 0; i < N; i++) {
                    array[i] = in.createStringArray();
                }
                this.textItems = array;
            }
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(this.widthItem);
            out.writeInt(this.heightItem);
            out.writeInt(this.mainTextSize);
            out.writeInt(this.textSize);
            out.writeFloat(this.textSizeItem);
            out.writeFloat(this.radiusRoundRectX);
            out.writeInt(this.radiusRoundRectY);
            out.writeInt(this.numberOfPoints);
            out.writeInt(this.radiusPoint);
            out.writeInt(this.strokeLine);
            out.writeInt(this.leftOffset);
            out.writeByte((byte) (this.isCalendarEnabled ? 1 : 0));
            out.writeStringArray(this.texts);
            out.writeString(this.title);
            out.writeInt(this.backgroudColorItems);
            out.writeInt(this.lineColor);
            out.writeInt(this.pointColor);
            out.writeInt(this.mainTextColor);
            out.writeInt(this.textColor);
            out.writeInt(this.textColorItem);

            if (textItems != null) {
                out.writeInt(textItems.length); /*Register array two dimension*/
                for (int i = 0; i < textItems.length; i++) {
                    out.writeStringArray(textItems[i]);
                }
            }

        }

        // Required field that makes Parcelables from a Parcel
        public static final Parcelable.Creator<SavedState> CREATOR =
                new Parcelable.Creator<SavedState>() {
                    public SavedState createFromParcel(Parcel in) {
                        return new SavedState(in);
                    }

                    public SavedState[] newArray(int size) {
                        return new SavedState[size];
                    }
                };
    }
}