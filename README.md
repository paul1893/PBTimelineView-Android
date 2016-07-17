# PBTimelineView-Android

PBTimelineView is a view which enable you to clearly see what's going to happen.

<img src="https://raw.githubusercontent.com/paul1893/PBTimelineView-Android/master/Screenshots/demo.gif" width="275" />  
<img src="https://raw.githubusercontent.com/paul1893/PBTimelineView-Android/master/Screenshots/blue.png" width="275" /><img src="https://raw.githubusercontent.com/paul1893/PBTimelineView-Android/master/Screenshots/purple.png" width="275" /><img src="https://raw.githubusercontent.com/paul1893/PBTimelineView-Android/master/Screenshots/green.png" width="275" />

## Version
1.0.0  
## Installation

With Gradle simple add this to your build.gradle file
```sh
compile 'com.thefrenchtouch.lib:pbtimelineview:1.0.0'
```
or  Copy paste the lib folder on your project
## How to use
```java
PBTimelineView mTimelineView = (PBTimelineView) findViewById(R.id.pbtimelineview);
        mTimelineView.setOnItemClickListener(this);
        mTimelineView.setTextItems(new String[][]{
                {"Boxe", "Dance", "Boxe", "Dance", "Boxe", "Dance"},
                {"MMA", "Step", "Muscu"},
                {},
                {"Course", "Aviron", "Boxe", "Dance"},
                {"Volley-ball", "Soccer", "Boxe", "Dance", "Boxe", "Dance"},
                {"BasketBall"},
                {"Rally", "Cuisine", "F1"}
        });
```
or directly with XML
```xml
<com.thefrenchtouch.pbtimelineview.widget.PBTimelineView xmlns:app="http://schemas.android.com/apk/res-auto"
        android:id="@+id/pbtimelineview"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:background="@android:color/white"
        app:backgroudColorItems="@color/colorPrimaryDark"
        app:isCalendarEnabled="false"
        app:leftOffset="100dp"
        app:lineColor="@color/colorPrimaryDark"
        app:mainText="Title"
        app:mainTextColor="@color/colorPrimaryDark"
        app:mainTextSize="28sp"
        app:numberOfPoints="7"
        app:pointColor="@color/colorAccent"
        app:radiusPoint="5dp"
        app:strokeLine="3dp"
        app:textColor="@color/colorPrimaryDark"
        app:textColorItem="@color/colorAccent"
        app:textSize="15sp"
        app:textSizeItem="15sp"
        app:texts="@array/array_example"
        app:widthItem="100dp" />
```
Note: For now you can only add texts programmatically.
## Listeners
If you have set an item click listener to the PBTimelineView you can handle the callback like this:
```java
@Override
    public void onItemClick(int section, int num, String text, PBItem item) {
        // Do stuff with the item
        Toast.makeText(this, "Section: " + section + " Item: " + num, Toast.LENGTH_SHORT).show();
        Log.i("Item Click", "You click on item " + num + " in section " + section + " : " + text);
    }
```
## License

MIT License

Copyright (c) [2016] [Paul Bancarel]

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
