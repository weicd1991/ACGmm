package com.jsmy.acgmm.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Region;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.jsmy.acgmm.util.MyLog;

/**
 * Created by Administrator on 2017/6/14.
 */

public class DragImageView extends View {

    private Paint mPaint;
    private Drawable mDrawable;
    private Rect mDrawableRect = new Rect();
    // private Rect mDrawableOffsetRect = new Rect();
    private Context mContext;
    private float mRation_WH = 0;
    private float mOldX = 0;
    private float mOldY = 0;
    private float mOldX0, mOldY0, mOldX1, mOldY1, mOldK, mOldB, mOldHandsX,
            mOldHandsY;
    private double mD1;
    private boolean isFirst = true;
    private int SINGALDOWN = 1;// 单点按下
    private int MUTILDOWM = 2;// 双点按下
    private int MUTILMOVE = 3;// 双点拖拽
    private int mStatus = 0;
    private FloatDrawable mFloatDrawable;
    protected Rect mDrawableSrc = new Rect();// 图片Rect变换时的Rect
    protected Rect mDrawableDst = new Rect();// 图片Rect
    private Rect mDrawableFloat = new Rect();// 浮层的Rect
    protected boolean isFrist = true;
    protected float oriRationWH = 0;

    enum STATUS {
        SINGAL, MUTILDOWN, MUTILMOVE;
    }

    // 默认裁剪的宽高
    private int cropWidth;
    private int cropHeight;

    public DragImageView(Context context) {
        super(context);
        init(context);
    }

    public DragImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public DragImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @SuppressLint("NewApi")
    private void init(Context context) {
        this.mContext = context;
        try {
            if (android.os.Build.VERSION.SDK_INT >= 11) {
                this.setLayerType(LAYER_TYPE_SOFTWARE, null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        mFloatDrawable = new FloatDrawable(context);
        this.mContext = context;
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setTextSize(35.0f);
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        super.onDraw(canvas);
        if (mDrawable == null || mDrawable.getIntrinsicHeight() == 0
                || mDrawable.getIntrinsicWidth() == 0) {
            return;
        }
        configureBounds();
        setBounds();
        mDrawable.draw(canvas);
        canvas.save();
        // 在画布上画浮层FloatDrawable,Region.Op.DIFFERENCE是表示Rect交集的补集
        canvas.clipRect(mDrawableFloat, Region.Op.DIFFERENCE);
        // 在交集的补集上画上灰色用来区分
        canvas.drawColor(Color.parseColor("#a0000000"));
        canvas.restore();
        // 画浮层
        mFloatDrawable.draw(canvas);

    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub
        switch (event.getPointerCount()) {
            case 1:

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mStatus = SINGALDOWN;
                        mOldX = event.getX();
                        mOldY = event.getY();
                        // Log.i("x_y_down", event.getX() + "__" + event.getY());
                        break;
                    case MotionEvent.ACTION_UP:
                        checkBounds();
                        // Log.i("x_y_up", event.getX() + "__" + event.getY());
                        break;
                    case MotionEvent.ACTION_MOVE:
                        // Log.i("x_y_move", event.getX() + "__" + event.getY());
                        if (mStatus == SINGALDOWN) {
                            int offsetWidth = (int) (event.getX() - mOldX);
                            int offsetHeight = (int) (event.getY() - mOldY);
                            // Log.i("x_y_offset", offsetWidth + "__" + offsetHeight);
                            mOldX = event.getX();
                            mOldY = event.getY();
                            mDrawableRect.offset(offsetWidth, offsetHeight);
                            invalidate();
                        }

                        break;
                    default:
                        break;
                }
                break;
            default:
   /*
    * mStatus = MUTILDOWM; if (mStatus == MUTILDOWM) { mOldX0 =
    * event.getX(0); mOldY0 = event.getY(0); mOldX1 = event.getX(1);
    * mOldY1 = event.getY(1); mOldK = (mOldY1 - mOldY0) / (mOldX1 -
    * mOldX0); mOldB = (mOldY0 * mOldX1 - mOldY1 * mOldX0) / (mOldX1 -
    * mOldX0); mOldHandsX = (mOldX0 + mOldX1) / 2; mOldHandsY =
    * mOldHandsX * mOldK + mOldB; mD1 = Math.sqrt(Math.pow(mOldX0 -
    * mOldX1, 2) + Math.pow(mOldY0 - mOldY1, 2)); Log.i("mD1", mD1 +
    * "________________"); }
    */
                switch (event.getAction()) {
                    case MotionEvent.ACTION_POINTER_DOWN:
                        MyLog.showLog(getClass().getName(), "true");
                        break;
                    case MotionEvent.ACTION_MOVE:
                        // Log.i("x_y_move", event.getX(0) + "__" + event.getY(0) +
                        // "___"
                        // + event.getX(1) + "__" + event.getY(1));
                        mStatus = MUTILMOVE;
                        float X0 = event.getX(0);
                        float Y0 = event.getY(0);
                        float X1 = event.getX(1);
                        float Y1 = event.getY(1);
                        float k = (Y1 - Y0) / (X1 - X0);
                        float b = (Y0 * X1 - Y1 * X0) / (X1 - X0);
                        int RectCenterX = mDrawableRect.centerX();
                        int RectCenterY = mDrawableRect.centerY();
                        float mHandsX = (X0 + X1) / 2;
                        float mHandsY = mHandsX * k + b;
                        double mD2 = Math.sqrt(Math.pow(X0 - X1, 2)
                                + Math.pow(Y0 - Y1, 2));

                        MyLog.showLog(getClass().getName(), mD2 + "________________X:" + mHandsX + "___Y:"
                                + mHandsY);
                        if (mD1 < mD2) {

                            // double mMultiple = mD2 / mD1;
                            // int newWidth = (int) (mDrawableRect.width() * mMultiple);
                            // int newHeight = (int) (newWidth / mRation_WH);
                            //
                            // int newleft = mDrawableRect.left / 2;
                            // int newtop = mDrawableRect.top / 2;
                            // int newright = mDrawableRect.right * (3 / 2);
                            // int newbotto = mDrawableRect.bottom * (3 / 2);
                            // // mDrawableRect.set(newleft, newtop, newright,
                            // newbotto);
                            //
                            // mDrawableRect.set(RectCenterX - newWidth / 2, RectCenterY
                            // - newHeight / 2, RectCenterX + newWidth / 2,
                            // RectCenterY + newHeight / 2);
                            // invalidate();

                            if (mDrawableRect.width() < mContext.getResources()
                                    .getDisplayMetrics().widthPixels * 2) {
                                int offsetwidth = 20;
                                int offsettop = (int) (offsetwidth / mRation_WH);
                                mDrawableRect.set(mDrawableRect.left - offsetwidth,
                                        mDrawableRect.top - offsettop,
                                        mDrawableRect.right + offsetwidth,
                                        mDrawableRect.bottom + offsettop);
                                MyLog.showLog(getClass().getName(),"aaaaaaaaaaaaaaa");

                                invalidate();
                            }
                            // mDrawableRect.offset((int) mHandsX, (int) mHandsY);

                        } else {
                            if (mDrawableRect.width() > mContext.getResources()
                                    .getDisplayMetrics().widthPixels / 3) {
                                int offsetwidth = 20;
                                int offsettop = (int) (offsetwidth / mRation_WH);
                                mDrawableRect.set(mDrawableRect.left + offsetwidth,
                                        mDrawableRect.top + offsettop,
                                        mDrawableRect.right - offsetwidth,
                                        mDrawableRect.bottom - offsettop);
                                invalidate();
                                MyLog.showLog(getClass().getName(), "bbbbbbbbbbbbbbb");
                            }
                        }
                        mD1 = mD2;
                        if (mHandsX < RectCenterX) {
                            if (mHandsY < RectCenterY) {
                                MyLog.showLog(getClass().getName(), "1");

                            } else {
                                MyLog.showLog(getClass().getName(), "3");
                            }
                        } else {
                            if (mHandsY < RectCenterY) {
                                MyLog.showLog(getClass().getName(), "2");
                            } else {
                                MyLog.showLog(getClass().getName(), "4");
                            }
                        }

                        //

                        break;
                    case MotionEvent.ACTION_UP:
                        MyLog.showLog(getClass().getName(), "mutildouble_up");
                        mStatus = 0;
                        break;
                    default:
                        break;
                }
                break;
        }

        return true;
    }

    public void setBounds() {
        if (isFirst) {
            mRation_WH = (float) mDrawable.getIntrinsicWidth()
                    / (float) mDrawable.getIntrinsicHeight();
            int px_w = Math.min(getWidth(),
                    dip2px(mContext, mDrawable.getIntrinsicWidth()));
            int px_h = (int) (px_w / mRation_WH);
            int left = (getWidth() - px_w) / 2;
            int top = (getHeight() - px_h) / 2;
            int right = px_w + left;
            int bottom = px_h + top;
            mDrawableRect.set(left, top, right, bottom);
            // mDrawableOffsetRect.set(mDrawableRect);
            isFirst = false;
            MyLog.showLog(getClass().getName(),"rect1______"+ mDrawableRect.left + "," + mDrawableRect.top
                    + "," + mDrawableRect.right + "," + mDrawableRect.bottom);
        }
        mDrawable.setBounds(mDrawableRect);
        MyLog.showLog(getClass().getName(),"rect2______"+mDrawableRect.left + "," + mDrawableRect.top + ","
                + mDrawableRect.right + "," + mDrawableRect.bottom);
        MyLog.showLog(getClass().getName(),"center_______"+
                mDrawableRect.centerX() + "," + mDrawableRect.centerY());

    }

    public void checkBounds() {
        int newLeft = mDrawableRect.left;
        int newTop = mDrawableRect.top;
        boolean isChange = false;
        if (newLeft < -mDrawableRect.width()) {
            newLeft = -mDrawableRect.width();
            isChange = true;
        }
        if (newTop < -mDrawableRect.height()) {
            newTop = -mDrawableRect.height();
            isChange = true;
        }
        if (newLeft > getWidth()) {
            newLeft = getWidth();
            isChange = true;
        }
        if (newTop > getHeight()) {
            newTop = getHeight();
            isChange = true;
        }
        if (isChange) {
            mDrawableRect.offsetTo(newLeft, newTop);
            invalidate();
        }
    }

    public void configureBounds() {
        // configureBounds在onDraw方法中调用
        // isFirst的目的是下面对mDrawableSrc和mDrawableFloat只初始化一次，
        // 之后的变化是根据touch事件来变化的，而不是每次执行重新对mDrawableSrc和mDrawableFloat进行设置
        if (isFrist) {
            oriRationWH = ((float) mDrawable.getIntrinsicWidth())
                    / ((float) mDrawable.getIntrinsicHeight());

            final float scale = mContext.getResources().getDisplayMetrics().density;
            int w = Math.min(getWidth(), (int) (mDrawable.getIntrinsicWidth()
                    * scale + 0.5f));
            int h = (int) (w / oriRationWH);

            int left = (getWidth() - w) / 2;
            int top = (getHeight() - h) / 2;
            int right = left + w;
            int bottom = top + h;

            mDrawableSrc.set(left, top, right, bottom);
            mDrawableDst.set(mDrawableSrc);

            int floatWidth = dip2px(mContext, cropWidth);
            int floatHeight = dip2px(mContext, cropHeight);

            if (floatWidth > getWidth()) {
                floatWidth = getWidth();
                floatHeight = cropHeight * floatWidth / cropWidth;
            }

            if (floatHeight > getHeight()) {
                floatHeight = getHeight();
                floatWidth = cropWidth * floatHeight / cropHeight;
            }

            int floatLeft = (getWidth() - floatWidth) / 2;
            int floatTop = (getHeight() - floatHeight) / 2;
            mDrawableFloat.set(floatLeft, floatTop, floatLeft + floatWidth, floatTop + floatHeight);

            isFrist = false;
        }

        mDrawable.setBounds(mDrawableDst);
        mFloatDrawable.setBounds(mDrawableFloat);
    }

    public Drawable getmDrawable() {
        return mDrawable;
    }

    public void setmDrawable(Drawable mDrawable, int cropWidth, int cropHeight) {
        this.mDrawable = mDrawable;
        this.cropWidth = cropWidth;
        this.cropHeight = cropHeight;
        this.isFrist = true;
        invalidate();
    }

    // 进行图片的裁剪，所谓的裁剪就是根据Drawable的新的坐标在画布上创建一张新的图片
    public Bitmap getCropImage() {
        if (mDrawable != null) {
            Bitmap tmpBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.RGB_565);
            Canvas canvas = new Canvas(tmpBitmap);
            mDrawable.draw(canvas);

            Matrix matrix = new Matrix();
            float scale = (float) (mDrawableSrc.width()) / (float) (mDrawableDst.width());
            matrix.postScale(scale, scale);
            Bitmap ret = Bitmap.createBitmap(tmpBitmap, mDrawableFloat.left,
                    mDrawableFloat.top, mDrawableFloat.width(),
                    mDrawableFloat.height(), matrix, true);
            MyLog.showLog(getClass().getName(), " left=" + mDrawableFloat.left + " top=" + mDrawableFloat.top + " width=" + mDrawableFloat.width() + " height=" + mDrawableFloat.height());
            tmpBitmap.recycle();
            tmpBitmap = null;

            return ret;
        } else {
            return null;
        }
    }

    public int dip2px(Context context, int value) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (value * scale + 0.5f);
    }

}
