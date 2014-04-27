package org.mahjong.graphics;

import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.googlecode.mgwt.ui.client.MGWTStyle;
import com.googlecode.mgwt.ui.client.dialog.Dialogs;
import com.googlecode.mgwt.ui.client.dialog.OptionsDialog;
import com.googlecode.mgwt.ui.client.theme.base.DialogCss;

public class PopupChoices extends  OptionsDialog {
	  public interface OptionChosen {
	    void optionChosen(String option);
	  }

	  private Button firstChoice;
	 // private static Css css =new Css();
    
	  public PopupChoices(String mainText, List<String> options, final OptionChosen optionChosen) {
	   // super(false, true);
	    //Dialogs test = new Dialogs();
	    //setText(mainText);
	   // setAnimationEnabled(true);
		super (MGWTStyle.getTheme().getMGWTClientBundle().getDialogCss());		
	    HorizontalPanel buttons = new HorizontalPanel();
	    final PopupChoices box=this;
	    for (String option : options) {
	      final String optionF = option;
	      Button btn = new Button(option);
	      if (firstChoice == null) {
	        firstChoice = btn; 
	      }
	      btn.addClickHandler(new ClickHandler() {
	        @Override
	        public void onClick(ClickEvent event) {
	       	  box.hide();
	          optionChosen.optionChosen(optionF);
	        }
	      });
	      buttons.add(btn);
	      //add(btn);
	      // adding separator space
	      if (option != options.get(options.size() - 1)) {
	        Label label = new Label();
	        label.setStyleName("withMargin");
	        buttons.add(label);
	       //add (label);
	      }
	    }
	    add(buttons);
	    //setWidget(buttons);
	  }
	 
	//  @Override
	//  public void center() {
	 //   super.center();
	 //   firstChoice.setFocus(true);
	 // }
	}