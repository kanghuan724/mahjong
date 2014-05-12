package org.mahjong.graphics;

import org.mahjong.client.MahJongPresenter;
import org.mahjong.client.MahJongPresenter.MahJongMessage;
import org.mahjong.graphics.PopupChoices.OptionChosen;


import java.util.List;

public class eventFactory {
   public static PopupChoices.OptionChosen build(final MahJongPresenter presenter,MahJongMessage mahjongMessage)
   {
	   switch (mahjongMessage)
	   {
	      case PICK:
	    	return new PopupChoices.OptionChosen() {
	  	      @Override
		      public void optionChosen(String option) {
	  	    	
		          presenter.pickUpTile();	  	    	  
		        }};
	      case HU:
	    		  return new PopupChoices.OptionChosen() {
	    	  	      @Override
	    		      public void optionChosen(String option) {
	    	  	    	//System.out.println("option: "+option);
	    	  	    	if (option.charAt(0)=='Y') {
	    	  	          //presenter.Hu();
	    	  	    	  //boolean ableToHu=presenter.huHelper();
	    	  	    	  //if (ableToHu==true)
	    	  	    		//System.out.println("presenter is gonna choose");
	    	  	    		presenter.huChoose();
	    	  	    		//presenter.Hu();
	    	  	        }
	    	  	    	else
	    	  	    	{
	    	  	    		
	    	  	    		presenter.refusehu();
	    	  	    	}
	    	  	    	  
	    		        }
	    		      };
	      case GANG:
	    	  return new PopupChoices.OptionChosen() {
    	  	      @Override
    		      public void optionChosen(String option) {
    	  	    	if (option.charAt(0)=='Y') {
    	  	          //presenter.Gang();
    	  	    	  List<Integer> comboToGang=presenter.gangHelper();
    	  	    	  if (comboToGang.size()==4)
    	  	    		  presenter.gang(comboToGang); 	    	  
    	  	        }
    	  	    	else
    	  	    	{
    	  	    		
    	  	    	  presenter.refusegang();
    	  	    	}
    	  	    	  
    		        }
    		      };
	    	
	      case PENG:
	    	  return new PopupChoices.OptionChosen() {
    	  	      @Override
    		      public void optionChosen(String option) {
    	  	    	System.out.println("Option"+option);
    	  	    	if (option.charAt(0)=='Y') {
    	  	          //presenter.Peng();
    	  	    	  List<Integer> comboToPeng=presenter.pengHelper();
    	  	   
       	  	    	  if (comboToPeng.size()==3)
       	  	    		  presenter.peng(comboToPeng); 	    
    	  	        }
    	  	    	else
    	  	    	{
    	  	    		
    	  	    		presenter.refusepeng();
    	  	    	}
    		        }
    		      };
	    	
	      case CHI:
	    	  return new PopupChoices.OptionChosen() {
    	  	      @Override
				public void optionChosen(String option) {
					System.out.println("Option" + option);
					if (option.charAt(0) == 'Y') {

					} else {
						presenter.refusechi();
					}
				}
			};
	   }
	     return null;
   }
}
