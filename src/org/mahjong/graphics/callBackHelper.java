package org.mahjong.graphics;

import java.util.List;

import org.mahjong.client.MahJongPresenter.MahJongMessage;

import com.googlecode.mgwt.ui.client.dialog.Dialogs;

public class callBackHelper implements Dialogs.OptionCallback{
	private   PopupChoices.OptionChosen eventTriggered;
	private MahJongMessage mahjongMessage;
	private List<Dialogs.OptionsDialogEntry> buttons;
	public callBackHelper( PopupChoices.OptionChosen eventTriggered, MahJongMessage mahjongMessage,List<Dialogs.OptionsDialogEntry> buttons)
	{
		this.eventTriggered =  eventTriggered;
		this.mahjongMessage = mahjongMessage;
		this.buttons = buttons;
	}
	public callBackHelper()
	{
		eventTriggered =null;
	}
	@Override
	public void onOptionSelected(int index) 
	{
		System.out.println("Index" + index);
		System.out.println("size: "+buttons.size());
		if (eventTriggered == null)
			return;
		if (index==1 && buttons.size()>1)
		{
			eventTriggered.optionChosen("Yes");
		}
		else
		{
			eventTriggered.optionChosen("No");
		}
		
	}

}
