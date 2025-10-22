package jbnu.io.test;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter {
    private SpriteBatch batch;
    private GameWorld gameworld;
    private OrthographicCamera camera;
    private Viewport viewport;


    @Override
    public void create() {
        batch = new SpriteBatch();
        gameworld=new GameWorld();
        camera=new OrthographicCamera();
        viewport = new FitViewport(1280,720, camera);
        camera.setToOrtho(false,1280,720);
    }

    @Override
    public void render() {
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
        input();
        logic();
        draw();
    }



    private void logic(){
        batch.setProjectionMatrix(camera.combined);
        gameworld.update(Gdx.graphics.getDeltaTime(),camera); //게임월드에 카메라 위치 넘겨줌 및 프레임별로 업데이트
    }

    private void draw(){

        // 플레이어 중심 카메라
        float camX = gameworld.getPlayer().getPosition().x + gameworld.getPlayer().getSprite().getWidth() / 2f;
        float camY = camera.position.y;

        float halfWidth = viewport.getWorldWidth() / 2f;
        float halfHeight = viewport.getWorldHeight() / 2f;

        float mapWidth = gameworld.getCameraWidth(); // 맵 가로
        float mapHeight = 720f; // 맵 높이

        camX = Math.max(halfWidth, Math.min(camX, mapWidth - halfWidth));//맵의 최소 최대 x값을 정함
        camY = Math.max(halfHeight, Math.min(camY, mapHeight - halfHeight));//맵의 최소 최대 y값을 정함

        camera.position.set(camX, camY, 0);
        camera.update();

        batch.setProjectionMatrix(camera.combined); // viewport 대신 camera.combined 사용
        batch.begin();
        gameworld.render(batch);
        batch.end();
    }
    private void input()
    {
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) //오른쪽 키 누르면 오른쪽 이동
        {
            gameworld.onPlayerRight();
        }
        else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) //왼쪽 키 누르면 왼쪽 이동
        {
            gameworld.onPlayerLeft();
        }
        else
        {
            gameworld.onPlayerStop(); //둘다 아니면 플레이어 멈춤 -> 속도를 0으로 설정
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) //esc누르면 게임 멈춤 상태
        {
            gameworld.onGameStop();
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) //space누르면 점프
        {
            gameworld.onPlayerJump();
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.R)&&(gameworld.IsGameOver()||gameworld.IsGameClear())) //게임 오버거나 게임 클리어상태에 r키 누르면 게임 재시작
        {
            gameworld.onPlayerReset();
        }
    }
    @Override
    public void dispose() {
        batch.dispose();
        gameworld.dispose();
    }
}
