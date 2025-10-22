package jbnu.io.test;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * 화면 전체에 짧은 섬광 효과(Flash)를 주는 클래스
 * startFlash()로 실행하면 흰색으로 번쩍이며 빠르게 사라짐
 */
public class FlashEffect {
    private final Sprite flashSprite;
    private float alpha = 0f;
    private boolean flashing = false; //섬광중인지 판단
    private boolean isflashingcheck=false; //섬광 시작한지 판단


    public FlashEffect()
    {
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);// 흰색 1x1 텍스처 생성
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        Texture whiteTexture = new Texture(pixmap);
        pixmap.dispose();

        flashSprite = new Sprite(whiteTexture);
        flashSprite.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        flashSprite.setPosition(0, 0);
        flashSprite.setSize(2000f,720f);
    }

    public void startFlash() //섬광효과 시작 알파값 1부터 0으로
    {
        alpha = 1f;
        flashing = true;
        isflashingcheck=true;
    }

    public void update(float delta) {
        if (flashing) {
            alpha -= delta * 0.5f;
            if (alpha <= 0f) {
                alpha = 1f;
                flashing = false;
            }
            flashSprite.setColor(1f, 1f, 1f, alpha);
        }
    }

    public void setCamera(float camX, float camY)//현재 보고있는 카메라의 중심을 기준으로 화면 가운데 배치
    {
        flashSprite.setPosition(camX - flashSprite.getWidth() / 2f,camY - flashSprite.getHeight() / 2f);
    }

    public void render(SpriteBatch batch) {
        if (flashing && alpha > 0f) {
            flashSprite.draw(batch);
        }
    }

    public boolean isFlashing() {
        return isflashingcheck;
    }
}
