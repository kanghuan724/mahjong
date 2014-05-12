package org.mahjong.graphics;

import com.allen_sauer.gwt.voices.client.Sound;
import com.allen_sauer.gwt.voices.client.SoundController;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.AbsolutePanel;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.VerticalPanel;

import org.mahjong.client.MahJongPresenter;
public class discardClick implements ClickHandler{
	  public discardClick(AbsolutePanel animation,HorizontalPanel acrossDeclaredArea,
			  HorizontalPanel acrossAtHandArea,	HorizontalPanel myAtHandArea, HorizontalPanel selectedArea,
			  VerticalPanel leftAtHandArea,VerticalPanel leftDeclaredArea, boolean enableClicks,
			  MahJongPresenter presenter,TileImage imgFinal,Image image,
			  ImageResource temp ,SoundController soundController)
 {
		this.animation = animation;
		this.acrossDeclaredArea = acrossDeclaredArea;
		this.acrossAtHandArea = acrossAtHandArea;
		this.myAtHandArea = myAtHandArea;
		this.selectedArea = selectedArea;
		this.leftAtHandArea = leftAtHandArea;
		this.leftDeclaredArea = leftDeclaredArea;
		this.enableClicks = enableClicks;
		this.presenter = presenter;
		this.imgFinal = imgFinal;
		this.image = image;
		this.temp = temp;
		this.soundController = soundController;
	}

	private AbsolutePanel animation;
	private HorizontalPanel acrossDeclaredArea;
	private HorizontalPanel acrossAtHandArea;
	private HorizontalPanel myAtHandArea;
	private HorizontalPanel selectedArea;
	private VerticalPanel leftAtHandArea;
	private VerticalPanel leftDeclaredArea;
	private boolean enableClicks;
	private MahJongPresenter presenter;
	private TileImage imgFinal;
	private Image image;
	private ImageResource temp;
	private SoundController soundController;

	@Override
	public void onClick(ClickEvent event) {
		int originalWidth = 44;
		int currentWidth = leftAtHandArea.getAbsoluteLeft()
				- leftDeclaredArea.getAbsoluteLeft();
		double xScale = (double) (originalWidth) / currentWidth;
		int originalHeight = 44;
		int currentHeight = acrossAtHandArea.getAbsoluteTop()
				- acrossDeclaredArea.getAbsoluteTop();
		double yScale = (double) (originalHeight) / currentHeight;

		if (enableClicks) {

			int startX, startY, endX, endY;
			boolean tilePosition = presenter.tilePosition(imgFinal.tile);

			if (tilePosition == true) {

				startX = image.getAbsoluteLeft();
				startY = image.getAbsoluteTop();
				endX = selectedArea.getAbsoluteLeft();
				endY = selectedArea.getAbsoluteTop();
			} else {

				startX = image.getAbsoluteLeft();
				startY = image.getAbsoluteTop();

				endX = myAtHandArea.getAbsoluteLeft();
				endY = myAtHandArea.getAbsoluteTop();

			}

			ImageResource context = temp;

			pieceMoveAnimation anime = new pieceMoveAnimation(image, presenter,
					imgFinal, startX, startY, endX, endY, context, animation,
					xScale, yScale);
			anime.run(1500);
			Sound sound = soundController.createSound(
					Sound.MIME_TYPE_AUDIO_WAV_PCM,

					"http://huan-kang.appspot.com/pieceCaptured.wav");

			sound.play();

		}
	}


}
