package jbnu.io.test;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Gameover
{

    private Texture gameoverTexture;
    private Sprite gameoverSprite;

    private Texture pressTexture;
    private Sprite pressSprite;

    public Gameover()
    {
        gameoverTexture=new Texture("Gameover.png");
        gameoverSprite=new Sprite(gameoverTexture);
        gameoverSprite.setSize(1200f,1000f);

        pressTexture=new Texture("Press.png");
        pressSprite=new Sprite(pressTexture);
        pressSprite.setSize(800f,50f);
    }

    public void setCamera(float camX, float camY) //현재 보고있는 카메라의 중심을 기준으로 화면 가운데 배치
    {
        gameoverSprite.setPosition(camX - gameoverSprite.getWidth() / 2f,camY - gameoverSprite.getHeight() / 2f);
        pressSprite.setPosition(camX - pressSprite.getWidth() / 2f,(camY - pressSprite.getHeight() / 2f)-100f);
    }

    public void render(SpriteBatch batch)
    {
        gameoverSprite.draw(batch);
        pressSprite.draw(batch);
    }

    public void dispose() {
        gameoverTexture.dispose();
        pressTexture.dispose();
    }
}
