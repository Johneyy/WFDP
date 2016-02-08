package pwr.wfdp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.PointF;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Toolbar;

public class FestiwalMap extends BaseActivity {
	private static final float SCALE_MIN = 1f;
	private static final float SCALE_MAX = 2f;

    private static final int NONE = 0;
    private static final int DRAG = 1;
    private static final int ZOOM = 2;

    private ImageView imageDetail;
    private ImageView buttonOne;
    private Matrix matrix = new Matrix();
    private Matrix savedMatrix = new Matrix();
    private PointF startPoint = new PointF();
    private PointF midPoint = new PointF();
    private float oldDist = 1f;
    private int mode = NONE;

    private float buttonX = 0;
    private float buttonY = 0;
    private int buttonSize = 40;

    private void popUpWinInformation( int x, int y ) {
        LayoutInflater inflater  = (LayoutInflater)getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        View layoutt = inflater.inflate(R.layout.map_pop_up, null);
        final PopupWindow popupWindow = new PopupWindow(
                layoutt,
                Toolbar.LayoutParams.WRAP_CONTENT,
                Toolbar.LayoutParams.WRAP_CONTENT);
        popupWindow.showAtLocation(findViewById(R.id.mapActivity), Gravity.NO_GRAVITY, x, y);

        Button closed = (Button) layoutt.findViewById(R.id.btn_close_popup);
        closed.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });

        Button about_beer = (Button) layoutt.findViewById(R.id.btn_about_beer);
        about_beer.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(getBaseContext(), AboutStand.class);
                startActivity(i);
            }
        });
    }

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_festiwal_map );
        setFullscreen();

        buttonOne = (ImageView) findViewById(R.id.ButtonClick);
        // Pozycja jest zjebana, w zaden sposob nie jest zwiazana z pozycja tego punktu na grafice...
        buttonX = 396;
        buttonY = 316;

        // Trzeba zahardkodowac 20px przesuniecia na osi X, inaczej button zle sie ustawia...
        buttonOne.setX( buttonX - buttonSize / 2 - 20 );
        buttonOne.setY( buttonY - buttonSize / 2 );

        buttonOne.setOnClickListener( new Button.OnClickListener()
        {
            public void onClick( View v )
            {
                popUpWinInformation( (int)v.getX(), (int)v.getY() );
            }
        } );

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE); // orientation Landscape

        Display display = getWindowManager().getDefaultDisplay();
        Point displaySize = new Point();
        display.getSize( displaySize );

        imageDetail = (ImageView)findViewById( R.id.festival_map );

        Bitmap bmp = BitmapFactory.decodeResource( getResources(), R.drawable.stoiska );
        Point imageSize = new Point( bmp.getWidth(), bmp.getHeight() );

        // TODO: Trzeba uwzglednic wysokosc tego paska z bateria/godzina etc, wtedy beda dobre wymiary
        float imageRatio = imageSize.x / (float)imageSize.y;
        float displayRatio = displaySize.x / (float)displaySize.y;
        float ratio = displayRatio * imageRatio;

        imageDetail.setScaleX(2.4F);
        imageDetail.setScaleY(2);

        // TODO: Wykminic jak wycentrowac mapke...
        imageDetail.setX(1240);
        imageDetail.setY(540);

        /**
         * set on touch listner on image
         */
        imageDetail.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ImageView view = (ImageView) v;

                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:
                        savedMatrix.set(matrix);
                        startPoint.set(event.getX(), event.getY());
                        mode = DRAG;
                        break;

                    case MotionEvent.ACTION_POINTER_DOWN:
                        oldDist = spacing(event);

                        if (oldDist > 10f) {
                            savedMatrix.set(matrix);
                            midPoint(midPoint, event);
                            mode = ZOOM;
                        }
                        break;

                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_POINTER_UP:
                        mode = NONE;
                        break;

                    case MotionEvent.ACTION_MOVE:
                        if (mode == DRAG) {
                            matrix.set(savedMatrix);
                            matrix.postTranslate(event.getX() - startPoint.x,
                                    event.getY() - startPoint.y);
                        } else if (mode == ZOOM) {
                            float newDist = spacing(event);
                            if (newDist > 10f) {
								matrix.set(savedMatrix);

								// Clamp scale
								float[] f = new float[9];
								matrix.getValues( f );
								float scale = f[Matrix.MSCALE_X]; // assume scale is uniform
								float scaleFactor = newDist / oldDist;

								float newScale = scale * scaleFactor;

								if ( newScale > SCALE_MAX ) {
									scaleFactor = SCALE_MAX / scale;
								} else if ( newScale < SCALE_MIN ) {
									scaleFactor = SCALE_MIN / scale;
								}

								matrix.postScale(scaleFactor, scaleFactor, midPoint.x, midPoint.y);
                            }
                        }
                        break;
                }

                view.setImageMatrix( matrix );

                float[] f = new float[9];
                matrix.getValues(f);

                // Trzeba zahardkodowac 20px przesuniecia na osi X, inaczej button zle sie ustawia...
                buttonOne.setX( buttonX * f[Matrix.MSCALE_X] - buttonSize / 2 + f[Matrix.MTRANS_X] * 2.4f - 20 );
                buttonOne.setY( buttonY * f[Matrix.MSCALE_Y] - buttonSize / 2 + f[Matrix.MTRANS_Y] * 2 );
                buttonOne.setScaleX( f[Matrix.MSCALE_X] );
                buttonOne.setScaleY( f[Matrix.MSCALE_Y] );

                return true;
            }

            @SuppressLint("FloatMath")
            private float spacing(MotionEvent event) {
                float x = event.getX(0) - event.getX(1);
                float y = event.getY(0) - event.getY(1);
                return (float)Math.sqrt(x * x + y * y);
            }

            private void midPoint(PointF point, MotionEvent event) {
                float x = event.getX(0) + event.getX(1);
                float y = event.getY(0) + event.getY(1);
                point.set(x / 2, y / 2);
            }
        });

    }

}



