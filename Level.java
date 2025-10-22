package jbnu.io.test;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

public class Level {

    private TileManager tilemanager;

    private Array<Texture> BG_texture;
    private Array<Texture> MG_texture;

    private Array<Ground> grounds;
    public Level()
    {
        tilemanager=new TileManager();

        BG_texture=tilemanager.getBGTexture();
        MG_texture=tilemanager.getMGTexture();

        grounds = new Array<>();
    }

    public void loadMap1() {
        grounds.clear();

        String[] map = new String[] {
            "............0112...........", // 맨 위 플랫폼
            "11112xxx0111444411111111111"  // 바닥
        };
        LevelSetGround(map,BG_texture);
    }


    public void loadMap2()
    {
        String[] map = new String[] {
            "44444444445...3445...344444", // 맨 위 플랫폼
            "77777777445...3445xxx677777",
            "........678xxx6778.........",
            "...........................",
            "...........................",
            "...........................",
            "...........................",
            "...........................",
            "...........................",
            "...........................",
            "...........................",
            "..........................."  // 바닥
        };
        LevelSetGround(map,MG_texture);
    }


    public void loadMap3()
    {
        grounds.clear();

        String[] map = new String[] {
            "44444444445..344445.......................35...3444444", // 맨 위 플랫폼
            "77777777778..344445xx...................xx68xxx6777777",
            "............x677778xxx77777777xxx7777777..............",
            "......................................................",
            "......................................................",
            "......................................................",
            "......................................................",
            "......................................................",
            "......................................................",
            "......................................................",
            "......................................................",
            "......................................................"  // 바닥
        };

        LevelSetGround(map,MG_texture);
    }

    public void loadMap4()
    {

        String[] map = new String[] {
            "..........................", // 맨 위 플랫폼
            "111111111111111112........."  // 바닥
        };

        LevelSetGround(map,BG_texture);
    }
    public Array<Ground> getGrounds() {
        return grounds;
    }

    public void LevelSetGround(String[] groundmap, Array<Texture> groundtexture)
    {
        grounds.clear();

        int tileWidth = 64;
        int tileHeight = 64;

        int rows = groundmap.length;
        int cols = groundmap[0].length();

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                char c = groundmap[row].charAt(col);

                if (c == '.') continue; //아무것도 없고 충돌도 없는 영역

                int x = col * tileWidth;
                int y = (rows - 1 - row) * tileHeight;

                if (c == 'x') //블록이없이 충돌만 하는 영역
                {
                    grounds.add(new Ground(x, y, null, 0));
                }
                else //블록존재하고 충돌도 하는 영역
                {
                    int tileIndex = Character.getNumericValue(c);
                    grounds.add(new Ground(x, y, groundtexture.get(tileIndex), 1));
                }
            }
        }
    }

    public void render(SpriteBatch batch) {
        for (Ground ground : grounds)
            ground.render(batch);
    }

    public void dispose() {
        for (Ground g : grounds) g.dispose();
    }

}
