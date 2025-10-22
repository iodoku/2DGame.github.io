package jbnu.io.test;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Entering
{
    private Texture enteringTexture;
    private Sprite enteringSprite;
    private float alpha = 0f;
    private boolean isEntering = false;

    private float timer = 0f;
    private float duration = 3f;

    public Entering()
    {
        enteringTexture=new Texture("swirl.png");
        enteringSprite = new Sprite(enteringTexture);
        enteringSprite.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        enteringSprite.setPosition(450, 0);
        enteringSprite.setColor(1f, 1f, 1f, alpha); // 초기 알파 0
    }


    public void startEntering()
    {
        alpha = 0f;
        timer=0f;
        isEntering=true;
    }

    public void update(float delta)//이미지의 알파값을 점차 증가시켜 이미지를 완전히 보이게함
    {
    if (isEntering)
    {
        timer += delta;
        alpha = Math.min(1f, timer / 2f); // 2초동안 알파값 1로 증가

        enteringSprite.setColor(1f, 1f, 1f, alpha);

        if (timer >= duration)
        {
            alpha = 1f;
            isEntering = false;
        }
    }
}
    public void setCamera(float camX, float camY) //현재 보고있는 카메라의 중심을 기준으로 화면 가운데 배치
    {
        enteringSprite.setPosition(camX - enteringSprite.getWidth() / 2f,camY - enteringSprite.getHeight() / 2f);
    }

    public void render(SpriteBatch batch) {
        if (alpha > 0f) {
            enteringSprite.draw(batch);
        }
    }

    public boolean isEntering() {
        return isEntering;
    }
}
