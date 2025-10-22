package jbnu.io.test;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class Mirror {
    private Sprite mirrorsprite;
    private Rectangle bounds; // 충돌 박스
    private boolean ismirrorcheck=false; //입장 거울 충돌여부 판단
    private boolean isentercheck=false; //퇴장 거울 충돌여부 판단
    private boolean isclear=false; //클리어 판단

    private float duration=2f;
    private float timer=0f;

    private float cleardurtaion=4f;
    private float cleartimer;

    private int Animnum;

    private SoundManager soundmanager;

    public Mirror(float posX, float posY,int num)  //레벨에 배치되는 거울의 좌표와 몇 레벨인지 가져옴
    {
        Texture texture = new Texture("mirror.png");
        mirrorsprite = new Sprite(texture);
        mirrorsprite.setPosition(posX, posY);
        mirrorsprite.setSize(150, 150);

        float realamount = 30f; //스프라이트의 보이는 영역과 실제 영역을 조절하는 변수
        bounds = new Rectangle(posX + realamount / 2, posY, mirrorsprite.getWidth()-realamount, mirrorsprite.getHeight()); //realamount로 실제 보이는 스프라이트의 영역과 비슷하게 조절

        Animnum=num; //플레이어의 레벨 확인 변수
        mirrorsprite.setOriginCenter(); //스프라이트의 좌표를 중심기준으로 바꿈
        soundmanager=new SoundManager();
    }

    public void playerCheck(Player player, Shadow shadow,float delta, FlashEffect flash, Boolean isflash) //플레이어가 레벨의 처음 거울에 충돌하고 일정거리 이동시 실행
    {
        if (getBounds().overlaps(player.getBounds())) //레벨에 있는 거울과 충돌여부
        {
            ismirrorcheck=true;
        }
        if(ismirrorcheck)
        {
            if(Animnum==0)
            {
                LevelShadowAnimSet(player,flash,shadow,524f,delta); //플레이어가 충돌하는 x축을 지나가면 실행
            }
            else if (Animnum==1)
            {
                LevelShadowAnimSet(player,flash,shadow,1300f,delta);
            }
            else if (Animnum==2)
            {
                LevelShadowAnimSet(player,flash,shadow,2700f,delta);
            }
            else if (Animnum==3)
            {
                LevelShadowAnimSet(player,flash,shadow,350f,delta);
            }
        }
    }

    public Boolean StageCheck(Player player, Shadow shadow, float delta, Entering entering, Mirror startmirror)
    {
        if (getBounds().overlaps(player.getBounds())) //맵 끝의 거울 충돌시 작동
        {
            if(Animnum!=3) //마지막 스테이지의 거울이 아닐때만 작동
            {
                if(!entering.isEntering()&&!isentercheck)
                {
                    entering.startEntering(); //맵 이동 연출
                    startmirror.StopShadowUpdate();//적 이동 멈춤
                    isentercheck=true; // 입구에 충돌
                    player.setIsStop(true); //플레이어 이동 멈춤
                    shadow.setAnimCheck(false,Animnum);
                    soundmanager.getPortalSound();
                }
                else if (entering.isEntering()&&isentercheck)
                {
                    entering.update(delta);
                    player.setIsStop(true);
                }
                else
                {
                    player.setIsStop(false); //플레이어 이동 풀고 다음 스테이지에 움직일 수 있도록
                    isentercheck=false;
                    return true;
                }
            }
            else
            {
                return true;
            }
        }
        return false;
    }

    public Boolean Clear(FlashEffect flash, Player player, Shadow shadow,float delta)//클리어 연출 및 클리어 여부 반환
    {
        if(!isclear) //처음 클리어시 플레이어, 적 이동 멈춤 및 사운드 및 섬광 연출
        {
            flash.startFlash();
            player.setIsStop(true);
            shadow.Stop();
            isclear=true;
            soundmanager.getMirrorBreakSound();
        }
        flash.update(delta);
        cleartimer+=delta;
        if(cleartimer>cleardurtaion-2f)
        {
            shadow.setAnimCheck(false,Animnum);
        }
        if(cleartimer>cleardurtaion)
        {
            cleartimer=0f;
            return true;
        }
        return false;
    }

    public void RotateMirror(float num){mirrorsprite.rotate(num);}
    public void StopShadowUpdate() {ismirrorcheck=false;}
    public void LevelShadowAnimSet(Player player, FlashEffect flash, Shadow shadow,float posX ,float delta)
    {
        if(Animnum==0)
        {
            if(player.getPosition().x>posX&& !flash.isFlashing()&&!shadow.getAnimCheck(Animnum)) //처음 적 등장시 플래시 연출 조건(플래시 시작x, 적 움직임 시작x, 플레이어가 특정좌표 지나갔을때)
            {
                flash.startFlash();
                shadow.setAnimCheck(true,Animnum); //적의 움직임 시작
                player.setIsStop(true); //섬광 연출동안 잠시 멈춤
            }
            flash.update(delta); //섬광연출
            if(shadow.getAnimCheck(Animnum)) //적의 움직이고 있을시 작동
            {
                timer+=delta;
                if(timer>duration)
                {
                    shadow.update(delta,Animnum);
                    player.setIsStop(false); //플레이어 다시 이동 가능
                }
            }
        }
        else if(Animnum==1||Animnum==2)
        {
            if(player.getPosition().x<posX&&!shadow.getAnimCheck(Animnum))
            {
                shadow.setAnimCheck(true,Animnum);
            }
            if(shadow.getAnimCheck(Animnum))
            {
                shadow.update(delta,Animnum);
            }
        }
        else
        {
            if(player.getPosition().x>posX&&!shadow.getAnimCheck(Animnum))
            {
                shadow.setAnimCheck(true,Animnum);
            }
            if(shadow.getAnimCheck(Animnum))
            {
                shadow.update(delta,Animnum);
            }
        }
    }


    public Sprite getSprite() {return mirrorsprite;}
    public Rectangle getBounds() {return bounds;}

    public void render(SpriteBatch batch)
    {
        if(!isclear)
        {
            mirrorsprite.draw(batch);
        }
    }
    public void dispose()
    {
        mirrorsprite.getTexture().dispose();
    }

}
