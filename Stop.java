package jbnu.io.test;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;

public class Stop
{
    private Texture stopTexture;
    private Sprite stopSprite;
    public Stop() {
        stopTexture = new Texture("pause.png");
        stopSprite = new Sprite(stopTexture);
        stopSprite.setSize(500f,300f);

    }

    public void setCamera(float camX, float camY) //현재 보고있는 카메라의 중심을 기준으로 화면 가운데 배치
    {
        stopSprite.setPosition(camX - stopSprite.getWidth() / 2f,camY - stopSprite.getHeight() / 2f);
    }

    public void render(SpriteBatch batch)
    {
        stopSprite.draw(batch);
    }

    public void dispose() {
        stopTexture.dispose();
    }
}
