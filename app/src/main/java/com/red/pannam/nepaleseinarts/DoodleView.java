package com.red.pannam.nepaleseinarts;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.print.PrintHelper;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by pannam on 4/15/2016.
 */
public class DoodleView extends View {

    //only if the user moves 10 pixel then only detect the movement
    private static final float TOUCH_TOLERANCE = 10;

    //drawing area
    private Bitmap bitmap;

    //used to draw on bitmap

    private Canvas bitmapCanvas;

    //used to draw bitmap on screen
    private final Paint paintScreen;

    //used to draw lines

    private final Paint paintLine;

    //maps of current paths being drawn and points on those paths

    private final Map<Integer, Path> pathMap = new HashMap<Integer, Path>();
    private final Map<Integer, Point> previousPointMap = new HashMap<Integer, Point>();


    //used to hide / show system bars

   // private GestureDetector singleTapDetector;

    public DoodleView(Context context, AttributeSet attrs) {
        super(context, attrs); // pass context to view's constructor
        paintLine = new Paint();
        paintScreen = new Paint();//set initial display for the painted line
        paintLine.setAntiAlias(true);//smooth edge
        paintLine.setColor(Color.BLACK);//default color is black
        paintLine.setStyle(Paint.Style.STROKE);//solid line
        paintLine.setStrokeWidth(5);//set the default line width
        paintLine.setStrokeCap(Paint.Cap.ROUND);//rounded line ends


        //GestureDetector for single taps
       // singleTapDetector = new GestureDetector(getContext(), singleTapDetector);

    }

//    public DoodleView(Context context, AttributeSet attrs, int defStyleAttr) {
//        super(context, attrs, defStyleAttr);
//    }

    //Method onSize changed creates Bitmap and Canvas after app displays
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {

        bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        bitmapCanvas = new Canvas(bitmap);
        bitmap.eraseColor(Color.WHITE); //ERASE THE COLOR WITH WHITE

    }

    //clear the painting from dialog
    public void clear() {
        pathMap.clear(); //remove all paths
        previousPointMap.clear();//remove all previous points
        bitmap.eraseColor(Color.WHITE);//CLEAR the bitmap
        invalidate(); //refresh the screen
    }

    //set paintline color
    public void setDrawingColor(int color) {
        paintLine.setColor(color);
    }

    //return paint line's color

    public int getDrawingColor() {
        return paintLine.getColor();
    }

    public void setLineWidth(int width) {
        paintLine.setStrokeWidth(width);
    }

    public int getLineWidth() {
        return (int) paintLine.getStrokeWidth();
    }

    //on Draw is called after  public void clear because the view needs to be repopulated
    protected void onDraw(Canvas canvas) {
        //draw the background screen
        canvas.drawBitmap(bitmap, 0, 0, paintScreen);

        //for each path currently being drawn

        for (Integer key : pathMap.keySet())
            canvas.drawPath(pathMap.get(key), paintLine);//draw line


    }

//    //hide systembar
//    public void hideSystemBars() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) setSystemUiVisibility(
//                View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
//                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
//                        View.SYSTEM_UI_FLAG_FULLSCREEN |
//                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
//                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
//                        View.SYSTEM_UI_FLAG_IMMERSIVE);
//
//    }
//
//    //show system bar
//    public void showSystemBars() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) setSystemUiVisibility(
//                View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
//                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
//
//                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
//
//    }
//
//    //single tap removes action bar etc
//    private GestureDetector.SimpleOnGestureListener singleTapListener =
//            new GestureDetector.SimpleOnGestureListener() {
//                @Override
//                public boolean onSingleTapUp(MotionEvent e) {
//                    if ((getSystemUiVisibility() & View.SYSTEM_UI_FLAG_HIDE_NAVIGATION) == 0) //system bar and action bar are on screen
//                        hideSystemBars();
//                    else
//                        showSystemBars();
//                    return true;
//                }
//            };
//

    //handle touch event
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //if a single tap event occured on KitKat or higher device
//        if(singleTapDetector.onTouchEvent(event))
//            return true;

        //get the event type and the ID of the pointer that caused the event

        int action = event.getActionMasked();//event type
        int actionIndex = event.getActionIndex(); //pointer (i.e finger)

        //determine whether touch started, ended or is moving

        if (action == MotionEvent.ACTION_DOWN || action == MotionEvent.ACTION_POINTER_DOWN)
        {
            touchStarted(event.getX(actionIndex),event.getY(actionIndex),event.getPointerId(actionIndex));
        }

        else if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_POINTER_UP)
        {
            touchEnded(event.getPointerId(actionIndex));

        }
        else
        {
            touchMoved(event);
        }

        invalidate();//redraw
        return true;
    } //end method onTouchEvent


    //CALLED when the user touches the screen

    //called when the user touches the screen
    private void touchStarted(float x, float y, int lineID) {
        Path path; //used to store the path for the given touch ID
        Point point ; //used to store the last point in path

        //if there is already a path for lineID

        if (pathMap.containsKey(lineID))
        {
            path = pathMap.get(lineID);
            path.reset();//reset the path because a new touch has started
            point = previousPointMap.get(lineID); // get path's last point


        }else {
            path = new Path();
            pathMap.put(lineID, path); //add the path to a map
            point = new Point(); //create a new point
            previousPointMap.put(lineID, point); //add the point to the Map

        }

        //move to the coordinates of the touch

        path.moveTo(x,y);
        point.x = (int)x;
        point.y = (int)y;

    }//end method


    //called when the user drags along the screen
    private void touchMoved(MotionEvent event) {

        //for each of the pointers in the given MotionEvent

        for (int  i = 0 ; i < event.getPointerCount();i++)
        {
            //get pointer ID and pointer index

            int pointerID = event.getPointerId(i);
            int pointerIndex = event.findPointerIndex(pointerID);

            //if there is a path associated with the pointer

            if (pathMap.containsKey(pointerID))
            {
                //get the new coordinates for the pointer

                float newX = event.getX(pointerIndex);
                float newY = event.getY(pointerIndex);

                //get the path and previous point associated with this pointer

                Path path = pathMap.get(pointerID);
                Point point = previousPointMap.get(pointerID);

                //calcualte how far the user moved from the last update

                float deltaX = Math.abs(newX - point.x);
                float deltaY = Math.abs(newY - point.y);

                //if the distance is significant enough to matter

                if(deltaX >= TOUCH_TOLERANCE || deltaY >= TOUCH_TOLERANCE){
                    path.quadTo(point.x, point.y, (newX + point.x)/2,
                            (newY + point.y)/2);

                    //store the new  coordinates

                    point.x = (int) newX;
                    point.y = (int) newY;
                }
            }

        }


    }

    //called when the user finishes the touch
    private void touchEnded(int pointerId) {

        Path path = pathMap.get(pointerId);//get the corresponding path
        bitmapCanvas.drawPath(path, paintLine);//draw to bitmapCanvas
        path.reset();//reset the path


    }
//save the current image to gallery

    public void saveImage(){
        //use "Nepalese in arts" followed by the current time as the image name

       final  String name = "Nepalese in arts" + System.currentTimeMillis() + ".jpg";

        //insert the image in the devices gallery

        String location = MediaStore.Images.Media.insertImage(getContext().getContentResolver(),bitmap,name,"Nepalese in arts");
        if(location != null) //image was saved
        {
            //display a message indicating the image was saved

            Toast message = Toast.makeText(getContext(),R.string.message_saved,Toast.LENGTH_LONG);
            message.setGravity(Gravity.CENTER,message.getXOffset()/2,message.getYOffset()/2);
            message.show();
        }else
        {
            //display a message image was not saved
            Toast message = Toast.makeText(getContext(),R.string.message_error_saving,Toast.LENGTH_LONG);
            message.setGravity(Gravity.CENTER,message.getXOffset()/2,message.getYOffset()/2);
            message.show();
        }
    }

        // print the current image
    public void printImage(){
        if (PrintHelper.systemSupportsPrint())
        {
            //use android support librarry#s printer helper to print image
            PrintHelper printHelper = new PrintHelper(getContext());

            //fit the image bounds and print the image

            printHelper.setScaleMode(printHelper.SCALE_MODE_FIT);
            printHelper.printBitmap("Nepalese in arts", bitmap);

        }else{
            //display message indicating the system doesn't allow printing
            Toast message = Toast.makeText(getContext(),R.string.message_error_saving,Toast.LENGTH_SHORT);
            message.setGravity(Gravity.CENTER,message.getXOffset()/2,message.getYOffset()/2);
            message.show();
        }
    }





}
