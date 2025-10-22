package jbnu.io.test;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

public class SoundManager
{
    private Sound clearsound;
    private Sound gameoversound;
    private Sound hitsound;
    private Sound jumpsound;
    private Sound mirrorbreaksound;
    private Sound portalsound;

    public SoundManager()
    {
        clearsound= Gdx.audio.newSound(Gdx.files.internal("Sound/ClearSound.mp3"));
        gameoversound= Gdx.audio.newSound(Gdx.files.internal("Sound/GameOverSound.mp3"));
        hitsound= Gdx.audio.newSound(Gdx.files.internal("Sound/HitSound.mp3"));
        jumpsound= Gdx.audio.newSound(Gdx.files.internal("Sound/JumpSound.mp3"));
        mirrorbreaksound= Gdx.audio.newSound(Gdx.files.internal("Sound/MirrorBreakSound.mp3"));
        portalsound= Gdx.audio.newSound(Gdx.files.internal("Sound/PortalSound.mp3"));
    }

    public void getClearSound(){clearsound.play();}
    public void getGameOverSound(){gameoversound.play();}
    public void getHitSound(){hitsound.play();}
    public void getJumpSound(){jumpsound.play();}
    public void getMirrorBreakSound(){mirrorbreaksound.play();}
    public void getPortalSound(){portalsound.play();}


}
