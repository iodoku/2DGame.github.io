package jbnu.io.test;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class Ground
{
    private Texture groundtexture;
    private Sprite groundSprite;

    private Rectangle topBounds;
    private Rectangle bottomBounds;
    private Rectangle leftBounds;
    private Rectangle rightBounds;

    private float width=64f;
    private float height=64f;
    private float posX;
    private float posY;
    private float detectArea= 5f;//충돌 감지영역 범위 좌우 충돌 박스 가로 5f, 상하 충돌 박스 세로 5f

    private float realwidth=60f; //실제 보이는 스프라이트 너비
    private float realheight=42f; //실제 보이는 스프라이트 높이

    private float marginwidth=10f;
    private float marginheight=25f;

    private int hideorshownum; //0이면 안보이는 블록 1이면 보이는 블록

    public Ground(float posx, float posy,Texture texture, int num)
    {
        posX=posx;
        posY=posy;
        groundtexture = texture;
        if(groundtexture!=null)
        {
            groundSprite = new Sprite(groundtexture);
            groundSprite.setSize(width, height);
            groundSprite.setPosition(posX, posY);
        }

        hideorshownum=num;

        topBounds = new Rectangle(posX + marginwidth * 2,posY + realheight - detectArea,width - 2 * marginwidth,detectArea); //지형의 상단 부분 충돌 범위
        bottomBounds = new Rectangle(posX + marginwidth * 2,posY+marginheight-4f,width - 2 * marginwidth,detectArea); //지형의 하단 부분 충돌 범위
        leftBounds = new Rectangle(posX-marginwidth, posY+marginheight, detectArea, realheight-2*marginheight); //지형의 왼쪽 부분 충돌 범위
        rightBounds = new Rectangle(posX +realwidth+marginwidth, posY+marginheight, detectArea, realheight-2*marginheight); //지형의 오른족 부분 충돌 범위
    }

    public Rectangle getTopBounds() {return topBounds;}
    public Rectangle getBottomBounds() {return bottomBounds;}
    public Rectangle getLeftBounds() {return leftBounds;}
    public Rectangle getRightBounds() {return rightBounds;}

    public int getHideorShownum(){return hideorshownum;}

    public void render(SpriteBatch batch)
    {
        if(hideorshownum==1&&groundSprite!=null)
        {
            groundSprite.draw(batch);

        }
    }
    public void dispose() {groundtexture.dispose();}

}
