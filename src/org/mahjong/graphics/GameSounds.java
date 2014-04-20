package org.mahjong.graphics;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.DataResource;

public interface GameSounds extends ClientBundle{
	
    @Source("Images/sound/pieceCaptured.mp3")
    DataResource pieceCapturedMp3();
    @Source("Images/sound/pieceCaptured.wav")
    DataResource pieceCapturedWav();
    

    @Source("Images/sound/pieceDown.wav")
    DataResource pieceDownWav();
    @Source("Images/sound/pieceDown.mp3")
    DataResource pieceDownMp3();

}
