package jbnu.io.test;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Array;

public class GameWorld extends InputAdapter
{

    private Player player; //플레이어
    private Shadow shadow; //플레이어를 추격하는 적
    private Array<Ground> grounds; //게임 레벨에서 배치된 맵요소 모음

    private Level level;  //스테이지 번호
    private Array<Mirror> mirrors; //레벨 클리어 및 시작 상호작용 요소

    private int levelindex=0;  //게임 레벨 단계표시
    private Array<Boolean> checkLevelGroundSet; //게임 레벨에서 이용할 맵요소 배치여부 확인
    private enum GameState{Running,Pause,GameOver,Clear,Explain;} //게임상태여부
    private GameState gamestate;

    private Camera camera;
    private Stop stop;          //esc 눌렀을때 나오는 정지 이미지
    private Gameover gameover;  //게임오버일때 나오는 텍스트 이미지
    private Clear clear;        //게임클리어일때 나오는 텍스트 이미지

    private Texture B_Texture;
    private Texture M_Texture;
    private Sprite B_Sprite; //기본 배경화면
    private Sprite M_Sprite; //거울세계 배경화면
    private Sprite M_Sprite2; //거울세계 배경화면2

    private enum BackGroundState{Basic,Mirror;}
    private BackGroundState backgroundstate;

    private FlashEffect flash;  //섬광효과
    private Entering entering; //입장효과

    private float SpawnTimer=5f;  //장애물 생성시간
    private float SpawnTimerduration=5f; //장애물 생성주기

    private Array<Trap> traps; //장애물 객체 배열

    private SoundManager soundmanager; //음향 관리 클래스
    private Texture explainTexture;
    private Sprite explainSprite;


    private Vector2 mousePos;



    public GameWorld()
    {
        player = new Player(200f); //속도 200f인 플레이어 생성
        shadow = new Shadow();

        level=new Level();
        mirrors=new Array<>();
        grounds = new Array<>();
        checkLevelGroundSet=new Array<>();

        stop=new Stop();
        gameover=new Gameover();
        clear=new Clear();

        for(int i=0; i<4; i++)
        {
            checkLevelGroundSet.add(false);
        }

        gamestate=GameState.Explain;

        B_Texture=new Texture("B_Background.png");
        M_Texture=new Texture("M_Background.png");
        B_Sprite=new Sprite(B_Texture);
        M_Sprite=new Sprite(M_Texture);
        M_Sprite2=new Sprite(M_Texture);

        B_Sprite.setSize(1728f,720f);
        M_Sprite.setSize(1728f,720f);
        M_Sprite2.setSize(1728f,720f);
        M_Sprite2.setPosition(1728f,0f);

        backgroundstate=BackGroundState.Basic;

        flash=new FlashEffect();
        entering=new Entering();

        traps=new Array<>();

        soundmanager=new SoundManager();

        explainTexture=new Texture("Explain.png");
        explainSprite=new Sprite(explainTexture);

        explainSprite.setSize(900,480f);
        explainSprite.setPosition(200f,100f);

        mousePos=new Vector2();
    }

    public void update(float delta,Camera getcamera)
    {
        onClick();//처음 설명창 클릭 이벤트 함수
        SetLevelCamera(getcamera); //정지창이나 클리어 텍스트를 캐릭터를 비추는 카메라 기준으로 중앙에 배치된 객체 모음
        if(gamestate==GameState.Running)
        {
            if(levelindex==0) //스테이지 1단계
            {
                if(!checkLevelGroundSet.get(0))
                {

                    LevelSet(BackGroundState.Basic,new Vector2(0f,42f),new Vector2(100f,45f),new Vector2(1600f,45f));
                    level.loadMap1();
                }
                LevelUpdateSet(true,64*27,delta);
            }
            else if(levelindex==1) //스테이지 2단계
            {
                if(!checkLevelGroundSet.get(1))
                {
                    LevelSet(BackGroundState.Mirror,new Vector2(1600f,510f),new Vector2(1600f,510f),new Vector2(100f,510f));
                    level.loadMap2();
                }
                LevelUpdateSet(false,64*27,delta);
            }
            else if(levelindex==2) //스테이지 3단계
            {
                if(!checkLevelGroundSet.get(2))
                {

                    LevelSet(BackGroundState.Mirror,new Vector2(3200f,510f),new Vector2(3200f,510f),new Vector2(100f,510f));
                    level.loadMap3();
                }
                LevelUpdateSet(false,64*54,delta);
            }
            else if(levelindex==3) //스테이지 4단계
            {
                if(!checkLevelGroundSet.get(3))
                {
                    LevelSet(BackGroundState.Basic,new Vector2(100f,42f),new Vector2(100f,45f),new Vector2(1000f,45f));
                    level.loadMap4();
                }
                LevelUpdateSet(false,64*27,delta);
            }
        }
    }

    public void onPlayerJump()
    {
        if(!player.isJumpCheck()&& player.getOnGround()){ player.jump();}
    }
    public void onPlayerLeft() {player.moveLeft(levelindex); }
    public void onPlayerRight() {player.moveRight(levelindex);}
    public void onPlayerStop() { player.moveStop();}
    public void onPlayerReset()
    {
        gamestate=GameState.Running;
        levelindex=0;
        checkLevelGroundSet.clear();
        for(int i=0; i<4; i++)
        {
            checkLevelGroundSet.add(false);
        }
        player=new Player(200f);
        shadow=new Shadow();
        flash=new FlashEffect();
        entering=new Entering();
        backgroundstate=BackGroundState.Basic;
        traps.clear();

    }
    public void onGameStop()
    {
        if(gamestate==GameState.Running){gamestate=GameState.Pause;}
        else
        {
            if(!(gamestate==GameState.GameOver))
            {
                gamestate=GameState.Running;

            }
        }
    }
    public void onClick() //마우스 클릭시 해당 좌표에 버튼이 있으면 설명창을 닫는 함수
    {
        if(Gdx.input.justTouched())
        {
            if(gamestate==GameState.Explain)
            {
                mousePos.set(Gdx.input.getX(),Gdx.input.getY());
                if((mousePos.x>538&&mousePos.x<725)&&(mousePos.y>574&&mousePos.y<619))
                {
                    gamestate=GameState.Running;
                }
            }
        }
    }


    public Boolean IsGameOver()
    {
        if(gamestate==gamestate.GameOver)
        {
            return true;
        }
        return false;
    }
    public Boolean IsGameClear()
    {
        if(gamestate==gamestate.Clear)
        {
            return true;
        }
        return false;
    }

    public void LevelSet(BackGroundState background,Vector2 playerVec, Vector2 mirror1Vec,Vector2 mirror2Vec)
    {
        backgroundstate=background;  //배경화면 상태 결정

        player.setPosition(playerVec.x, playerVec.y); //플레이어 위치 설정
        if(levelindex==1||levelindex==3) //2,4단계의 레벨에서 플레이어 회전상태로 배치 및 중력 반대로 작용
        {
            player.RotatePlayer(180f);
            player.ChangeGravity();
        }

        mirrors.clear(); //mirror객체들 초기화 및 새롭게 생성
        mirrors.add(new Mirror(mirror1Vec.x,mirror1Vec.y,levelindex));
        mirrors.add(new Mirror(mirror2Vec.x,mirror2Vec.y,levelindex));
        if(levelindex==1||levelindex==2) //2,3단계에서 회전상태로 배치
        {
            mirrors.get(0).RotateMirror(180f);
            mirrors.get(1).RotateMirror(180f);
        }

        checkLevelGroundSet.set(levelindex, true); //1,2,3,4중 현재 레벨의 맵 요소 가져옴 표시
        grounds=level.getGrounds(); //현제 레벨의 지형 가져옴
        player.getGrounds(grounds); //플레이어가 지형 정보를 얻어 충돌여부 판단
        shadow.getGrounds(grounds); //적도 지형 정보를 얻어 충돌여부 판단
    }
    public void LevelUpdateSet(Boolean flashcheck, float maxpositionwidth, float delta)
    {
        if(levelindex==2)
        {
            SpawnTimer+=delta;
            if(SpawnTimer>SpawnTimerduration) //스폰 타이머 마다 함정 발사
            {
                traps.clear(); //기존 함정 제거후 새롭게 배치

                traps.add(new Trap(2570f,0f, 300f));
                traps.add(new Trap(1950f,0f, 300f));
                traps.add(new Trap(1300f,0f, 300f));
                SpawnTimer=0f;
            }
            else
            {
                for(int i=0; i<traps.size; i++) //update동안 함정이 발사됨
                {
                    traps.get(i).Update(delta);
                }
            }
            player.TrapCheck(traps,delta); //플레이어가 함정하고 충돌여부 판단
        }
        player.update(delta,levelindex); //플레이어 이동 및 지형 충돌여부 판단
        if(player.Death(shadow)&&player.isDeathcheck()) //플레이어 죽었는지 판단하여 true라면 게임오버
        {
            soundmanager.getGameOverSound();
            gamestate=GameState.GameOver;
        }
        player.SetMaxPosition(maxpositionwidth); //플레이어가 최대로 이동할 수 있는 영역 설정
        mirrors.get(0).playerCheck(player,shadow,delta,flash,flashcheck); //시작 mirror가 캐릭터와 충돌했는지 여부 및 적의 움직임 통제
        if(mirrors.get(1).StageCheck(player,shadow,delta,entering,mirrors.get(0))) // 끝 mirror가 캐릭터와 충돌했는지 여부 및 다음 레벨 전환
        {
            if(levelindex==3) //4레벨일때 게임 클리어
            {
                if(mirrors.get(1).Clear(flash, player, shadow,delta))
                {
                    soundmanager.getClearSound();
                    gamestate=GameState.Clear;
                }
            }
            else
            {
                levelindex++;
            }
        }
    }
    public void SetLevelCamera(Camera getcamera)
    {
        camera=getcamera;
        stop.setCamera(camera.position.x,camera.position.y);
        gameover.setCamera(camera.position.x,camera.position.y);
        flash.setCamera(camera.position.x,camera.position.y);
        entering.setCamera(camera.position.x,camera.position.y);
        clear.setCamera(camera.position.x,camera.position.y);
    }

    public Player getPlayer() {return player;}
    public float getCameraWidth()
    {
        if(levelindex==0||levelindex==1)
        {
            return 64*27f;
        }
        else if(levelindex==2)
        {
            return  64*27*2f;
        }
        return 0;
    }


    public void render(SpriteBatch batch)
    {
        if(backgroundstate==BackGroundState.Basic)
        {
            B_Sprite.draw(batch);
        }
        if(backgroundstate==BackGroundState.Mirror)
        {
            if(levelindex==1)
            {
                M_Sprite.draw(batch);

            }
            else if (levelindex==2)
            {
                M_Sprite.draw(batch);
                M_Sprite2.draw(batch);
            }
        }
        level.render(batch);
        for(Mirror m : mirrors) { m.render(batch);}
        if(gamestate!=GameState.Explain){player.render(batch);}
        shadow.render(batch);
        for(int i=0; i<traps.size; i++)
        {
            traps.get(i).render(batch);
        }
        if (flash != null && flash.isFlashing()) {flash.render(batch);}
        if (entering != null && entering.isEntering()) {entering.render(batch);}
        if(gamestate==GameState.GameOver){gameover.render(batch);}
        if(gamestate==GameState.Pause){stop.render(batch);}
        if(gamestate==GameState.Clear){clear.render(batch);}
        if(gamestate==GameState.Explain) {explainSprite.draw(batch);}
    }
    public void dispose()
    {
        B_Texture.dispose();
        M_Texture.dispose();
        level.dispose();
        for(Mirror m : mirrors) {m.dispose();}
        shadow.dispose();
        player.dispose();
        for(int i=0; i<traps.size; i++)
        {
            traps.get(i).dispose();
        }
        gameover.dispose();
        stop.dispose();
        clear.dispose();
    }
}
