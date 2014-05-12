package org.mahjong.graphics;

import java.util.List;
import org.mahjong.client.MahJongPresenter;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Image;

public class chiClick implements ClickHandler{
	private MahJongGraphics graphics;
	private Image image;
	private int imageIndex;
	private MahJongPresenter presenter;
	@Override
	public void onClick(ClickEvent event) {
		//System.out.println("I am chi now");
		//System.out.println(graphics.chiSelected.size());
		if (graphics.chiSelected.contains(imageIndex) == true)
		{
			//System.out.println("I am chi now 1");
			image.setStyleName("imgContainer");
			for (int i =0;i<graphics.chiSelected.size();i++)
			{
				if (graphics.chiSelected.get(i)==imageIndex)
					graphics.chiSelected.remove(i);
			}
			graphics.claimBtn.setEnabled(false);
		}
		else
		{
			if (graphics.chiSelected.size() < 2) {
				//System.out.println("I am chi now 2");
				graphics.chiSelected.add(imageIndex);
				image.setStyleName("imgBigger");
				List<Integer> chiSequence = presenter.chiLegal(graphics.chiSelected);
				if (chiSequence.size()==3)
				{
					graphics.chiSequence = chiSequence;
					graphics.claimBtn.setEnabled(true);
					
				}
				
				//System.out.println(graphics.chiSelected.size());
				//presenter.chooseTileToChi();

			}
		}
			
	}
	public chiClick(MahJongGraphics graphics,Image image,int imageIndex, MahJongPresenter presenter)
	{
		this.graphics = graphics;
		this.image = image;
		this.imageIndex = imageIndex;
		this.presenter = presenter;
	}

}
