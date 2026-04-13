package com.cookandroid.project9_1;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    static final int LINE = 1, CIRCLE = 2, RECT = 3;    static int curShape = LINE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(new MyGraphicView(this));
        setTitle("간단 그림판");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        menu.add(0, 1, 0, "선 추가");
        menu.add(0, 2, 0, "원 추가");
        menu.add(0, 3, 0, "사각형 추가");

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 1:
                curShape = LINE;
                return true;

            case 2:
                curShape = CIRCLE;
                return true;

            case 3:
                curShape = RECT;
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private static class MyGraphicView extends View {
        int startX = -1, startY = -1, stopX = -1, stopY = -1;
        Paint paint = new Paint();
        java.util.ArrayList<Shape> shapeList = new java.util.ArrayList<>();

        public MyGraphicView(Context context) {
            super(context);

            paint.setAntiAlias(true);
            paint.setStrokeWidth(5);
            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(Color.RED);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            for (MainActivity.Shape shape : shapeList) {
                shape.draw(canvas, paint);
            }
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    startX = (int) event.getX();
                    startY = (int) event.getY();
                    break;

                case MotionEvent.ACTION_UP:
                    stopX = (int) event.getX();
                    stopY = (int) event.getY();

                    MainActivity.Shape shape = null;

                    switch (curShape) {
                        case LINE:
                            shape = new Line(startX, startY, stopX, stopY);
                            break;

                        case CIRCLE:
                            shape = new Circle(startX, startY, stopX, stopY);
                            break;

                        case RECT:
                            shape = new Rect(startX, startY, stopX, stopY);
                            break;
                    }

                    if (shape != null) {
                        shapeList.add(shape);
                    }

                    invalidate();
                    break;
            }
            return true;
        }
    }
    private static abstract class Shape {
        int startX, startY, stopX, stopY;

        public Shape(int startX, int startY, int stopX, int stopY) {
            this.startX = startX;
            this.startY = startY;
            this.stopX = stopX;
            this.stopY = stopY;
        }

        abstract void draw(Canvas canvas, Paint paint);
    }

    private static class Line extends MainActivity.Shape {
        public Line(int startX, int startY, int stopX, int stopY) {
            super(startX, startY, stopX, stopY);
        }

        @Override
        void draw(Canvas canvas, Paint paint) {
            canvas.drawLine(startX, startY, stopX, stopY, paint);
        }
    }

    private static class Circle extends MainActivity.Shape {
        public Circle(int startX, int startY, int stopX, int stopY) {
            super(startX, startY, stopX, stopY);
        }

        @Override
        void draw(Canvas canvas, Paint paint) {
            int dx = stopX - startX;
            int dy = stopY - startY;
            int radius = (int) Math.sqrt(dx * dx + dy * dy);
            canvas.drawCircle(startX, startY, radius, paint);
        }
    }

    private static class Rect extends MainActivity.Shape {
        public Rect(int startX, int startY, int stopX, int stopY) {
            super(startX, startY, stopX, stopY);
        }

        @Override
        void draw(Canvas canvas, Paint paint) {
            canvas.drawRect(startX, startY, stopX, stopY, paint);
        }
    }
}