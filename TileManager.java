package jbnu.io.test;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;

public class TileManager {
    private Array<Texture> BG_texture;
    private Array<Texture> MG_texture;

    public TileManager()
    {
        BG_texture = new Array<>();
        MG_texture = new Array<>();

        BG_texture.add(new Texture("BG/BG_UL.png"));
        BG_texture.add(new Texture("BG/BG_U.png"));
        BG_texture.add(new Texture("BG/BG_UR.png"));
        BG_texture.add(new Texture("BG/BG_ML.png"));
        BG_texture.add(new Texture("BG/BG_M.png"));
        BG_texture.add(new Texture("BG/BG_MR.png"));
        BG_texture.add(new Texture("BG/BG_DL.png"));
        BG_texture.add(new Texture("BG/BG_D.png"));
        BG_texture.add(new Texture("BG/BG_DR.png"));

        MG_texture.add(new Texture("MG/MG_UL.png"));
        MG_texture.add(new Texture("MG/MG_U.png"));
        MG_texture.add(new Texture("MG/MG_UR.png"));
        MG_texture.add(new Texture("MG/MG_ML.png"));
        MG_texture.add(new Texture("MG/MG_M.png"));
        MG_texture.add(new Texture("MG/MG_MR.png"));
        MG_texture.add(new Texture("MG/MG_DL.png"));
        MG_texture.add(new Texture("MG/MG_D.png"));
        MG_texture.add(new Texture("MG/MG_DR.png"));

    }

    public Array<Texture> getBGTexture()
    {
        return BG_texture;
    }

    public Array<Texture> getMGTexture()
    {
        return MG_texture;
    }


}
