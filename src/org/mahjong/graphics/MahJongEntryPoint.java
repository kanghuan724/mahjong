package org.mahjong.graphics;

import org.mahjong.client.MahJongLogic;
import org.mahjong.client.MahJongPresenter;
import org.mahjong.client.GameApi;
import org.mahjong.client.GameApi.Game;
import org.mahjong.client.GameApi.IteratingPlayerContainer;
import org.mahjong.client.GameApi.UpdateUI;
import org.mahjong.client.GameApi.VerifyMove;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class MahJongEntryPoint implements EntryPoint {
  IteratingPlayerContainer container;
  MahJongPresenter mahJongPresenter;

  @Override
  public void onModuleLoad() {
    Game game = new Game() {
      @Override
      public void sendVerifyMove(VerifyMove verifyMove) {
        container.sendVerifyMoveDone(new MahJongLogic().verify(verifyMove));
      }

      @Override
      public void sendUpdateUI(UpdateUI updateUI) {
        mahJongPresenter.updateUI(updateUI);
      }
    };
    container = new IteratingPlayerContainer(game, 4);
    MahJongGraphics mahJongGraphics = new MahJongGraphics();
    mahJongPresenter =
        new MahJongPresenter(mahJongGraphics, container);
    final ListBox playerSelect = new ListBox();
    playerSelect.addItem("1");
    playerSelect.addItem("2");
    playerSelect.addItem("3");
    playerSelect.addItem("4");
    playerSelect.addItem("Viewer");
    playerSelect.addChangeHandler(new ChangeHandler() {
      @Override
      public void onChange(ChangeEvent event) {
        int selectedIndex = playerSelect.getSelectedIndex();
        int playerId = selectedIndex == 4 ? GameApi.VIEWER_ID
            : container.getPlayerIds().get(selectedIndex);
        container.updateUi(playerId);
      }
    });
    FlowPanel flowPanel = new FlowPanel();
    flowPanel.add(mahJongGraphics);
    flowPanel.add(playerSelect);
    RootPanel.get("mainDiv").add(flowPanel);
    container.sendGameReady();
    container.updateUi(container.getPlayerIds().get(0));
  }
}