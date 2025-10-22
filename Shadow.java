package jbnu.io.test;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class Shadow {
    private Texture shadowTexture;
    private Sprite shadowSprite;

    private Array<Boolean> isAnimCheck; //적이 움직임 시작 여부
    private Array<Boolean> isSetPosition; //적이 월드에 배치됬는지 여부

    private Vector2 position;
    private Vector2 velocity;
    private float shadowspeed = 160f;
    private float shadowjumppower = 600f;
    private Rectangle bounds;

    private Array<Ground> grounds;
    private float WORLD_GRAVITY = -9.8f*100f;//처음 중력 적용값
    private boolean onGround = true;

    private int count=0;
    private int totalcount=0;


    public Shadow()
    {
        shadowTexture=new Texture("BlackPlayer.png");
        shadowSprite=new Sprite(shadowTexture);
        position = new Vector2(100f, 42f);
        velocity = new Vector2(0f, 0f);
        shadowSprite.setPosition(position.x,position.y);
        shadowSprite.setSize(150f,150f);

        float realAmount = 60f; // 스프라이트의 보이는 영역과 실제 영역을 조절하는 변수
        bounds = new Rectangle(shadowSprite.getX() + realAmount / 2, shadowSprite.getY(),shadowSprite.getWidth() - realAmount,shadowSprite.getHeight());

        grounds = new Array<>();
        isAnimCheck = new Array<>();
        isSetPosition = new Array<>();

        for(int i=0; i<4; i++)
        {
            isAnimCheck.add(false);
            isSetPosition.add(false);
        }

        shadowSprite.setOriginCenter(); //적의 좌표를 중앙기준으로 만듬
    }

    public void update(float delta, int levelindex)
    {
        if(!isSetPosition.get(0)&&isAnimCheck.get(0))
        {
            load1();
            isSetPosition.set(0,true);
        }
        if(!isSetPosition.get(1)&&isAnimCheck.get(1))
        {
            load2();
            isSetPosition.set(1,true);
        }
        if(!isSetPosition.get(2)&&isAnimCheck.get(2))
        {
            load3();
            isSetPosition.set(2,true);
        }
        if(!isSetPosition.get(3)&&isAnimCheck.get(3))
        {
            load4();
            isSetPosition.set(3,true);
        }
        if(isAnimCheck.get(0)||isAnimCheck.get(1)||isAnimCheck.get(2)||isAnimCheck.get(3))//적의 활동중에만 지형검사 및 움직임 업데이트
        {
            groundCheck(levelindex);
            MoveUpdate(delta,levelindex);
        }
    }

    //load1~4는 처음 적의 상태 설정 함수
    public void load1()
    {
        velocity.x = shadowspeed;
    }

    public void load2()
    {
        velocity.x = shadowspeed;
        setPosition(1600f,510f);
        WORLD_GRAVITY=-WORLD_GRAVITY;
        shadowSprite.rotate(180f);
    }

    public void load3()
    {
        velocity.x = shadowspeed+25f;
        setPosition(3200f,510f);
    }

    public void load4()
    {
        velocity.x = shadowspeed+40f;
        setPosition(100f,45f);
        WORLD_GRAVITY=-WORLD_GRAVITY;
        shadowSprite.rotate(180f);
    }

    public void groundCheck(int levelindex)
    {
        float realAmount = 60f; // 스프라이트의 보이는 영역과 실제 영역을 조절하는 변수
        bounds.set(position.x + realAmount / 2,position.y,shadowSprite.getWidth() - realAmount, shadowSprite.getHeight() );

        count=0;
        for (Ground ground : grounds)
        {
            if(ground.getHideorShownum()==1)//1이면 맵에 보이는 땅
            {
                if(getBounds().overlaps(ground.getLeftBounds())&&getOnGround())//왼쪽 충돌영역에 부딪히면 점프
                {
                    velocity.y = shadowjumppower;
                }
                if(getBounds().overlaps(ground.getRightBounds())&&getOnGround())//오른쪽 충돌영역에 부딪히면 점프
                {
                    velocity.y = shadowjumppower;
                }
                if (getBounds().overlaps(ground.getTopBounds())&&(levelindex==0||levelindex==3))//0,3레벨이면 땅의 상단 부분에 충돌검사
                {
                    setOnGround(true);

                    getPosition().y = ground.getTopBounds().y + ground.getTopBounds().height;
                    getVelocity().y = 0;
                    count++;
                }
                if (getBounds().overlaps(ground.getBottomBounds())&&(levelindex==1||levelindex==2))//1,2레벨이면 땅의 하단 부분에 충돌검사
                {
                    setOnGround(true); //setonground가 true면 땅에 붙어있는 상태

                    getPosition().y = ground.getBottomBounds().y - getBounds().height;
                    getVelocity().y = 0;
                    count++;
                }
            }
            if(ground.getHideorShownum()==0)//맵에 보이지 않는 땅 충돌 영역만 존재 부딪히면 발판이 없는 상태여서 바로 점프 시작
            {
                if(getBounds().overlaps(ground.getTopBounds())&&(levelindex==0||levelindex==3))
                {
                    velocity.y = shadowjumppower;
                }
                else if(getBounds().overlaps(ground.getBottomBounds())&&(levelindex==1||levelindex==2))
                {
                    velocity.y = -shadowjumppower;
                }
            }
        }
        if(count==0) //한번이라도 count값 증가 안되면 공중에있음
        {
            totalcount++; //플레이어와 ground가 붙어있어도 인식이 안될때가 있어 예외적으로 처리 (ex/땅에 붙어있어도 count값이 0,2,0,2,이런식으로 나와 연속적으로 0이 나오는게 아니라면 땅에 붙어있는걸로 판단)
            if(totalcount>5) //넉넉하게 5로 판단하여 땅에 붙어있어도 점프 못하는 상황을 막음
            {
                setOnGround(false); //setonground가 false면 땅에 붙어있지 않은 상태 ->점프중 또는 떨어지는중 즉 점프를 못하는상태
            }
        }
        else
        {
            totalcount=0;
        }
    }

    public void MoveUpdate(float delta,int levelindex)
    {
        if (!onGround)//땅에 안붙어있으면 중력 작용
        {
            velocity.y += WORLD_GRAVITY * delta;
        }
        if(levelindex==0||levelindex==3)//0,3단계 레벨에서 오른쪽 이동(플레이어가 도망치는 방향)
        {
            position.x += velocity.x * delta;
        }
        else//아니라면 왼쪽이동(플레이어가 도망치는 방향)
        {
            position.x -= velocity.x * delta;

        }
        position.y += velocity.y * delta;

        shadowSprite.setPosition(position.x, position.y);
    }

    public void Stop()
    {
        velocity.x=0f;
    }

    public void setAnimCheck(boolean animcheck, int num) //움직이는 상태인지 설정
    {
        isAnimCheck.set(num,animcheck);
    }
    public void setOnGround(boolean isground){ onGround=isground; } //땅에 붙어있는지 설정
    public void setPosition(float posx, float posy)
    {
        position.set(posx, posy);
        shadowSprite.setPosition(posx, posy);

        float realAmount = 60f; // 스프라이트의 보이는 영역과 실제 영역을 조절하는 변수
        bounds.set(position.x + realAmount / 2, position.y,shadowSprite.getWidth() - realAmount,shadowSprite.getHeight());
    }

    public Vector2 getPosition() { return position; }
    public Vector2 getVelocity() { return velocity; }public boolean getAnimCheck(int num)
    {
        return isAnimCheck.get(num);
    }
    public void getGrounds(Array<Ground> ground) { grounds=ground;}
    public Rectangle getBounds() {    return bounds; }

    public Boolean getOnGround(){ return onGround; }


    public void render(SpriteBatch batch)
    {
        if(isAnimCheck.get(0)||isAnimCheck.get(1)||isAnimCheck.get(2)||isAnimCheck.get(3)) //적이 움직일때만 모습 보이게 실행
        {

            shadowSprite.draw(batch);
        }
    }
    public void dispose() {
        shadowTexture.dispose();
    }
}
