package jbnu.io.test;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Trap
{
    private Texture trapTexture;
    private Sprite trapSprite;

    private float posX;
    private float posY;

    private float trapspeed;
    private Vector2 position;
    private Vector2 velocity;

    private Rectangle bounds;

    public Trap(float x, float y, float speed)
    {
        posX=x;
        posY=y;
        trapspeed=speed; //함정이 움직이는 속도설정

        position=new Vector2(posX,posY);
        velocity=new Vector2(0f,0f);

        trapTexture=new Texture("Robot.png");
        trapSprite=new Sprite(trapTexture);

        trapSprite.setSize(100f,100f);
        trapSprite.setPosition(posX,posY);

        bounds = new Rectangle(trapSprite.getX(), trapSprite.getY(),trapSprite.getWidth(),trapSprite.getHeight());
    }

    public void Update(float delta) //함정이 점차 velocity.y값에 따라 움직이는 업데이트
    {
        velocity.y += trapspeed * delta;

        position.x += velocity.x * delta;
        position.y += velocity.y * delta;

        trapSprite.setPosition(position.x, position.y);

        bounds = new Rectangle(trapSprite.getX(), trapSprite.getY(),trapSprite.getWidth(),trapSprite.getHeight()); //위치에 따른 함정의 충돌영역 설정

    }

    public Rectangle getBounds() { return bounds; }
    public void render(SpriteBatch batch) {  trapSprite.draw(batch); }
    public void dispose() { trapTexture.dispose(); }
}
