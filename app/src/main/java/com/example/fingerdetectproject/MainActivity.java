package com.example.fingerdetectproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.TextView;

import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener {

    private static final int TOP_RIGHT = 0;
    private static final int TOP_LEFT = 1;
    private static final int BOTTOM_RIGHT = 2;
    private static final int BOTTOM_LEFT = 3;
    private static final int CENTER = 4;
    private static final int CENTER_TOP = 5;
    private static final int CENTER_BOTTOM = 6;
    private TextView tvButton, tvContent;
    private ConstraintLayout root;
    private String directWay = "";
    private String lastDirectWay  = "";

    private HorizontalScrollView horizontalScrollView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvButton = findViewById(R.id.tv_button);
        tvContent = findViewById(R.id.content);
        root = findViewById(R.id.root);
        horizontalScrollView = findViewById(R.id.scroll_view);
        root.setOnTouchListener(this);

    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {

        float x = event.getX();
        float y = event.getY();
        float targetX = tvButton.getX();
        float targetY = tvButton.getY();
        float targetRightX = tvButton.getX() + tvButton.getWidth();
        float targetBottomY = tvButton.getY() + tvButton.getHeight();

        Log.i("Michael", "targetY : " + targetY + " targetBottomY : " + targetBottomY);

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (x < targetX || x > targetRightX) {
                    directWay = "位置已超出按鍵";
                    return true;
                } else if (y < targetY || y > targetBottomY) {
                    directWay = "位置已超出按鍵";
                    return true;
                } else if (getDirect(x, y, targetX, targetRightX, targetY, targetBottomY) == CENTER_TOP) {
                    directWay = "正上方,";
                } else if (getDirect(x, y, targetX, targetRightX, targetY, targetBottomY) == CENTER_BOTTOM) {
                    directWay = "正下方,";
                } else if (getDirect(x, y, targetX, targetRightX, targetY, targetBottomY) == CENTER) {
                    directWay = "中間,";
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (x < targetX || x > targetRightX) {
                    directWay = "位置已超出按鍵";
                    return true;
                } else if (y < targetY || y > targetBottomY) {
                    directWay = "位置已超出按鍵";
                    return true;
                } else if (getDirect(x, y, targetX, targetRightX, targetY, targetBottomY) == TOP_LEFT) {
                    directWay = "左上方,";
                } else if (getDirect(x, y, targetX, targetRightX, targetY, targetBottomY) == TOP_RIGHT) {
                    directWay = "右上方,";
                } else if (getDirect(x, y, targetX, targetRightX, targetY, targetBottomY) == BOTTOM_LEFT) {
                    directWay = "左下方,";
                } else if (getDirect(x, y, targetX, targetRightX, targetY, targetBottomY) == BOTTOM_RIGHT) {
                    directWay = "右下方,";
                }

//                tvContent.setText(directWay+"\n角度約 : "+calculateAngle(x,y,targetX,targetRightX,targetY,targetBottomY));
                break;
        }
        if (!directWay.equals(lastDirectWay)) {
            tvContent.setText(tvContent.getText().toString() + directWay);
            tvContent.post(new Runnable() {
                @Override
                public void run() {
                    horizontalScrollView.smoothScrollTo(tvContent.getWidth(), 0);
                }
            });
            lastDirectWay = directWay; // 更新記錄的方向
        }

        return true;
    }

    private int getDirect(float x, float y, float targetX, float targetRightX, float targetY, float targetBottomY) {
        float targetCenterX = targetX + (targetRightX - targetX) / 2;
        float targetCenterY = targetY + (targetBottomY - targetY) / 2;
        if ((x >= targetCenterX - 50 && x < targetCenterX + 50) && (y >= targetCenterY - 50 && y < targetCenterY + 50)) {
            return CENTER;
        } else if ((x >= targetCenterX - 50 && x < targetCenterX + 50) && y < targetCenterY) {
            return CENTER_TOP;
        } else if ((x >= targetCenterX - 50 && x < targetCenterX + 50) && y > targetCenterY) {
            return CENTER_BOTTOM;
        } else if (x < targetCenterX && y < targetCenterY) {
            return TOP_LEFT;
        } else if (x > targetCenterX && y < targetCenterY) {
            return TOP_RIGHT;
        } else if (x < targetCenterX && y > targetCenterY) {
            return BOTTOM_LEFT;
        } else if (x > targetCenterX && y > targetCenterY) {
            return BOTTOM_RIGHT;
        }

        return -1;
    }

    private double calculateAngle(float x, float y, float targetX, float targetRightX, float targetY, float targetBottomY) {
        float targetCenterX = targetX + (targetRightX - targetX) / 2;
        float targetCenterY = targetY + (targetBottomY - targetY) / 2;
        float deltaX = x - targetCenterX;
        float deltaY = y - targetCenterY;

        // 計算角度
        double angleInRadians = Math.atan2(deltaY, deltaX);
        double angleInDegrees = Math.toDegrees(angleInRadians);

        // 調整角度於正上方
        angleInDegrees += 90;
        // 確保角度再0~360度之間
        if (angleInDegrees < 0) {
            angleInDegrees += 360;
        } else if (angleInDegrees > 360) {
            angleInDegrees -= 360;
        }

        return angleInDegrees;
    }
}