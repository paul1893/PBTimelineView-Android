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

        //On récupère les attributs définis par l'utilisateur
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

        radiusRoundRectX = 5;
        radiusRoundRectY = radiusRoundRectX;

        step = (2 * heightItem) + (2 * padding);

        if (isCalendarEnabled) {
            updateCalendar();
        }


    }

    private void updateCalendar() {
        numberOfPoints = 7;
        texts = new String[numberOfPoints];
        Calendar calendar = Calendar.getInstance();
        String[] days = new String[]{"Dimanche", "Lundi", "Mardi", "Mercredi", "Jeudi", "Vendredi", "Samedi"};

        //Example for title: Ven. 4 Avril
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

    public void drawBoxes() {
        if (textItems == null) {
            return;
        }

        listRecyclerView = new ArrayList<RecyclerView>();
        // On dessine les rectangles en ligne et on écrit dedans /* Be careful addView recall onDraw method infinite loop*/

        int viewWidth = getMeasuredWidth();
        int viewHeight = getMeasuredHeight();

        for (int i = 1; i < numberOfPoints; i++) {

            try {

                /* // Place horizontal items view -> not scrollable
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

                /* Place horizontal items view in recycler view -> scrollable */
                RecyclerView mRecyclerView = new RecyclerView(getContext());
                mRecyclerView.setHasFixedSize(true);

                // Set adapter of the recycler view
                ArrayList<PBItem> items = new ArrayList<PBItem>();
                for (int j = 0; j < textItems[i - 1].length; j++) {
                    String text = textItems[i - 1][j];
                    PBItem item = new PBItem(widthItem, heightItem, i, j, text, backgroudColorItems, textSizeItem, textColorItem);
                    items.add(item);
                }
                PBTimelineAdapter mAdapter = new PBTimelineAdapter(getContext(), items, listener);

                // Set layout manager of the recycler view
                LinearLayoutManager mLinearLayoutManager
                        = new LinearLayoutManager(getContext());
                mLinearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

                mRecyclerView.setLayoutManager(mLinearLayoutManager);
                mRecyclerView.setAdapter(mAdapter);

                this.addView(mRecyclerView);
                this.listRecyclerView.add(mRecyclerView);

            } catch (ArrayIndexOutOfBoundsException e) {
                Log.i("DrawBoxes", "PBTimelineView section " + i + " has no texts");
            }
        }

    }

    public void setTextItems(String[][] textItems) {
        this.textItems = textItems;

        if (listRecyclerView != null) {
            notifyDataSetChanged();
        } else {
            drawBoxes();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Paint mLinePaint = new Paint();
        mLinePaint.setColor(mainTextColor);
        mLinePaint.setStyle(Paint.Style.FILL);
        mLinePaint.setTextSize(mainTextSize);
        mLinePaint.setStrokeWidth(strokeLine);

        // On écrit le titre
        Rect textBounds = new Rect();
        mLinePaint.getTextBounds(title, 1, title.length(), textBounds);
        canvas.drawText(title, leftOffset + padding, textBounds.height() + padding, mLinePaint);

        //On dessine la ligne verticale
        mLinePaint.setColor(lineColor);
        canvas.drawLine(leftOffset, 0, leftOffset, height, mLinePaint);

        //On dessine les points

        for (int i = 1; i < numberOfPoints; i++) {

            // On dessine la date ou le texte
            if (texts != null) {
                mLinePaint.setColor(textColor);
                mLinePaint.setTextSize(textSize);
                float textWidth = mLinePaint.measureText(texts[i - 1]);
                String currentText = texts[i - 1];
                mLinePaint.getTextBounds(currentText, 1, currentText.length(), textBounds);
                canvas.drawText(currentText, leftOffset - textWidth - padding, (i * step) + (textBounds.height() / 2), mLinePaint);
            }

            // On dessine le petit cercle
            mLinePaint.setColor(pointColor);
            canvas.drawCircle(leftOffset, i * step, radiusPoint, mLinePaint);
        }
    }

    public void notifyDataSetChanged() {
        for (RecyclerView rv : listRecyclerView) {
            rv.getAdapter().notifyDataSetChanged();
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        final int count = getChildCount();
        int curWidth, curHeight, curLeft, curTop, maxHeight;

        //get the available size of child view
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

            //Get the maximum size of the child
            child.measure(MeasureSpec.makeMeasureSpec(childWidth, MeasureSpec.AT_MOST), MeasureSpec.makeMeasureSpec(childHeight, MeasureSpec.AT_MOST));
            curWidth = child.getMeasuredWidth();
            curHeight = child.getMeasuredHeight();
            //wrap is reach to the end
            if (curLeft + curWidth >= childRight) {
                curLeft = childLeft;
                curTop += maxHeight;
                maxHeight = 0;
            }
            //do the layout
            int recyclerViewWidth = getMeasuredWidth() - leftOffset - padding;
            int recyclerViewHeight = heightItem;
            int left = leftOffset + padding;
            int top = ((i + 1) * step) - (heightItem / 2);
            int right = getMeasuredWidth();
            int bottom = ((i + 1) * step) + (heightItem / 2);

            child.layout(left, top, right, bottom);
            //store the max height
            if (maxHeight < curHeight)
                maxHeight = curHeight;
            curLeft += curWidth;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // On recadre la vue pour s'adapter au contenu
        width = MeasureSpec.getSize(widthMeasureSpec);
        height = numberOfPoints * step;

        setMeasuredDimension(width, height);
    }

    public void setOnItemClickListener(onItemClickListener listener) {
        this.listener = listener;
    }

    public interface onItemClickListener {
        void onItemClick(int section, int num, String text, PBItem item);
    }
}
