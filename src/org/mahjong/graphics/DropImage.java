package org.mahjong.graphics;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.Image;
public class DropImage extends Image{
	private int index;
	public DropImage(ImageResource resource,int index)
	{
		super(resource);
		this.index=index;
	}
	public int getIndex()
	{
		return index;
	}

}
