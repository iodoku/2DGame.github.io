package jbnu.io.test;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import java.util.Vector;

public class Player {

    private Vector2 position;
    private Vector2 velocity;
    private Texture playerTexture;
    private Sprite playersprite;

    private float playerspeed;
    private float WORLD_GRAVITY = -9.8f*100f; //초기 중력값
    private float jumpforce=600f;
    private float correctionvalue=2f; //벽 이동 보정값 -> 왼쪽 충돌시 충돌위치에서 왼쪽으로 2f이동

    private Array<Ground> grounds;
    private Rectangle bounds;

    private int count=0;
    private int totalcount=0;

    private boolean isstop = false; //경직상태 여부
    private boolean isdamaged=false; //피격상태 여부
    private boolean onGround = true; //땅에 붙어있는지 여부
    private boolean isjump=false; //점프상태 여부
    private boolean isdeath=false; //죽었는지 여부
    private boolean deathcheck=false; //죽음이 시작되었는지 여부
    private boolean isleft=false; //함정 왼쪽에 부딪혔는지 여부
    private boolean isright=false; //함정 오른쪽에 부딪혔는지 여부

    private float damagedTimer=0f; //피격시 경직상태 타이머
    private float damagedduration=2f; // 경직상태 타이머 주기

    private SoundManager soundmanager; //효과음 클래스

    public Player(float speed) {
        playerTexture = new Texture("BasicPlayer.png");
        playersprite = new Sprite(playerTexture);

        position = new Vector2(playersprite.getX(), playersprite.getY());
        velocity = new Vector2(0f, 0f);

        playerspeed = speed;
        playersprite.setSize(150, 150);

        float realamount = 60f; // 스프라이트의 보이는 영역과 실제 영역을 조절하는 변수
        bounds = new Rectangle(playersprite.getX() + realamount / 2, playersprite.getY(),playersprite.getWidth() - realamount,playersprite.getHeight());

        grounds = new Array<>();
        playersprite.setOriginCenter();

        soundmanager=new SoundManager();
    }

    public void update(float delta, int levelindex)
    {
        CheckGround(levelindex); //땅에 붙어있는걸 확인하거나 땅의 벽면에 붙어있을때 보정값 주는 함수
        if (!onGround) //땅에 붙어있지 않으면 중력 작용
        {
            velocity.y += WORLD_GRAVITY * delta;
        }
        position.x += velocity.x * delta; //x속력을 이용해 위치 업데이트
        position.y += velocity.y * delta; //y속력을 이용해 위치 업데이트

        playersprite.setPosition(position.x, position.y);

        float realamount = 70f; //스프라이트의 보이는 영역과 실제 영역을 조절하는 변수
        bounds.set(position.x + realamount / 2,position.y,playersprite.getWidth() - realamount, playersprite.getHeight() );
    }

    public void TrapCheck(Array<Trap> traps, float delta) //함정 충돌여부 체크
    {
        for (int i=0; i<traps.size; i++)
        {
            if (bounds.overlaps(traps.get(i).getBounds()))
            {
                if (position.x < traps.get(i).getBounds().x) //충돌할때 플레이어가 함정보다 왼쪽에 있으면 왼쪽 체크
                {
                    isleft=true;
                }
                else//충돌할때 플레이어가 함정보다 오른쪽에 있으면 오른쪽 체크
                {
                    isright=true;
                }
                isdamaged=true; //데미지 받은 상태 체크
            }
            if(isdamaged)
            {
                isstop=true; //플레이어 이동을 멈춤
                damagedTimer+=delta; //damagedTimer가 damagedduration보다 클때까지 플레이어 이동 멈춤
                soundmanager.getHitSound(); // 피격음 재생
                if(isleft)
                {
                    position.x -= 0.5f; //함정의 왼쪽부분에서 맞으면 왼쪽으로 다시 플레이어 위치 이동
                }
                if(isright)
                {
                    position.x += 0.5f; //함정의 왼쪽부분에서 맞으면 왼쪽으로 다시 플레이어 위치 이동
                }
                position.y += 1f; //중력으로 떨어지는 속도보다 조금 빠르게 떨어지게 하기 위해 임의로 y축 조정
                playersprite.setPosition(position.x, position.y);
                if(damagedTimer>damagedduration)
                {
                    isstop=false; //경직상태 풀림
                    isdamaged=false; //피격상태 풀림
                    damagedTimer=0f;
                    isleft=false; //왼쪽 오른쪽 구분 풀림
                    isright=false;//왼쪽 오른쪽 구분 풀림

                }
            }
        }
    }

    public void moveLeft(int levelindex)
    {
        if(!isstop) //경직상태가 아니라면 움직일 수 있음
        {
            if(levelindex==1||levelindex==2) //레벨1,2단계에는 키보드 입력과 움직이는 방향을 다르게 설정
            {
                velocity.x = playerspeed;
            }
            else//레벨0,3단계에는 키보드 입력과 움직이는 방향을 같게 설정
            {
                velocity.x = -playerspeed;
            }
        }
        else //경직상태면 속도0으로 만들어 위치 업데이트 x
        {
            velocity.x=0;
        }

    }
    public void moveRight(int levelindex)
    {
        if(!isstop)//경직상태가 아니라면 움직일 수 있음
        {
            if(levelindex==1||levelindex==2)//레벨1,2단계에는 키보드 입력과 움직이는 방향을 다르게 설정
            {
                velocity.x = -playerspeed;
            }
            else//레벨0,3단계에는 키보드 입력과 움직이는 방향을 같게 설정
            {
                velocity.x = playerspeed;
            }
        }
        else//경직상태면 속도0으로 만들어 위치 업데이트 x
        {
            velocity.x=0;
        }
    }
    public void jump()
    {
        if(!isstop) //경직 상태면 점프x
        {
            if(WORLD_GRAVITY>0f) //중력이 0보다 크면 중력이 위로 작동하는 상태 즉 점프도 반대로 힘을 주는 상태
            {
                velocity.y = -jumpforce;
            }
            else//중력이 0보다 작으면 중력이 아래로 점프 방향도 그대로
            {
                velocity.y = jumpforce;
            }
            soundmanager.getJumpSound();
            isjump=true;//점프상태 돌입
            onGround=false;//땅에 떨어져있는 상태
        }
    }
    public void setPosition(float x, float y)
    {
        position.set(x, y);
        playersprite.setPosition(x, y);
        // 충돌 박스도 함께 이동
        float realAmount = 70f; // 스프라이트의 보이는 영역과 실제 영역을 조절하는 변수
        bounds.set(position.x + realAmount / 2, position.y,playersprite.getWidth() - realAmount,playersprite.getHeight());
    }

    public void CheckGround(int levelindex)
    {
        count=0;
        for (Ground ground : grounds)
        {
            if(ground.getHideorShownum()==1)//getHideorShownum가 1이면 플레이어가 보이는 땅
            {
                if(getBounds().overlaps(ground.getLeftBounds())&&getOnGround())//왼쪽 땅에 플레이어가 충돌시
                {
                    getPosition().x -= correctionvalue;//땅에 더 파고들지 못하게 보정값으로 플레이어 왼쪽으로 다시 이동
                    getVelocity().x = 0;
                }
                if(getBounds().overlaps(ground.getRightBounds())&&getOnGround())//오른쪽 땅에 플레이어가 충돌시
                {
                    getPosition().x += correctionvalue;//땅에 더 파고들지 못하게 보정값으로 플레이어 오른쪽으로 다시 이동
                    getVelocity().x = 0;
                }
                if (getBounds().overlaps(ground.getTopBounds())&&(levelindex==0||levelindex==3))//레벨0,3단계에서는 중력이 아래로 작용해 땅의 상단 충돌로 땅에 붙어있는지 검사
                {
                    setOnGround(true); //setonground가 true면 땅에 붙어있는 상태
                    setIsJump();

                    getPosition().y = ground.getTopBounds().y + ground.getTopBounds().height;//땅에 더 파고들지 못하게 y값 조절
                    getVelocity().y = 0;
                    count++;
                }
                if (getBounds().overlaps(ground.getBottomBounds())&&(levelindex==1||levelindex==2))//레벨1,2단계에서는 중력이 위로 작용해 땅의 하단 충돌로 땅에 붙어있는지 검사
                {
                    setOnGround(true); //setonground가 true면 땅에 붙어있는 상태
                    setIsJump();

                    getPosition().y = ground.getBottomBounds().y - getBounds().height; //땅에 더 파고들지 못하게 y값 조절
                    getVelocity().y = 0;
                    count++;
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

    public void SetMaxPosition(float width)
    {
        float playerWidth = getSprite().getWidth();

        float mapWidth = width;

        float clampedX = Math.max(0, Math.min(getPosition().x, mapWidth - playerWidth));

        setPosition(clampedX, getPosition().y);
    }

    public Boolean Death(Shadow shadow)
    {
        if((position.y<-200f||position.y>800f)&&!deathcheck) //플레이어가 맵밖으로 나가고 처음 죽으면 실행
        {
            deathcheck=true;
            isdeath=true;
            return true;
        }
        if((shadow.getAnimCheck(0)||shadow.getAnimCheck(3))) //레벨0,3단계에 적이 움직이고 있으면 실행
        {
            if(position.x<=shadow.getPosition().x&&position.x>524f) //플레이어가 일정거리를 넘어갔을때 적보다 x위치 값이 작다면 적과 부딪히도록 설정
            {
                position.x=shadow.getPosition().x; //일부로 적과 x값이 같도록 만듬
            }
        }
        else if((shadow.getAnimCheck(1)||shadow.getAnimCheck(2)))
        {
            if(position.x>=shadow.getPosition().x&&position.x<1300f) //플레이어가 일정거리를 넘어갔을때 적보다 x위치 값이 크다면 적과 부딪히도록 설정
            {
                position.x=shadow.getPosition().x; //일부로 적과 x값이 같도록 만듬
            }
        }
        if(getBounds().overlaps(shadow.getBounds())) //적과 충돌시 플레이어 죽음 상태
        {
            if(((shadow.getAnimCheck(0)||shadow.getAnimCheck(3))&&position.y<shadow.getPosition().y+shadow.getBounds().height-40f))
            {
                deathcheck=true;
                isdeath=true;
                return true;
            }
            if(((shadow.getAnimCheck(1)||shadow.getAnimCheck(2))&&position.y+ getSprite().getHeight()-50f>shadow.getPosition().y))
            {
                deathcheck=true;
                isdeath=true;
                return true;
            }
        }

        return false;
    }


    public void setIsStop(boolean setisstop) //경직상태 설정
    {
        isstop=setisstop;
    }
    public void setIsJump() { isjump=false; } //점프상태 설정
    public void setOnGround(boolean isground){ onGround=isground; } //땅에 붙어있는지 설정

    public void getGrounds(Array<Ground> ground) { grounds=ground;}
    public Vector2 getPosition() { return position; }
    public Vector2 getVelocity() { return velocity; }
    public Sprite getSprite() { return playersprite; }
    public Rectangle getBounds() { return bounds; }
    public Boolean getOnGround(){ return onGround; }

    public void moveStop() {velocity.x = 0;}
    public void RotatePlayer(float num){playersprite.rotate(num);} //플레이어 스프라이트를 num만큼 회전
    public void ChangeGravity() { WORLD_GRAVITY=-WORLD_GRAVITY;} //중력을 반대로 바꾸는 함수

    public Boolean isJumpCheck(){ return isjump;}
    public Boolean isDeathcheck() {return  isdeath;}

    public void render(SpriteBatch batch) {  playersprite.draw(batch); }
    public void dispose() { playerTexture.dispose(); }


}
