package letsplayjson.actions;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.ide.CopyPasteManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import letsplayjson.json.JsonParser;
import letsplayjson.json.NotJsonException;

public class ShouldTranscodeJson extends AnAction {

  private final CopyPasteManager manager = CopyPasteManager.getInstance();

  @Override
  public void actionPerformed(AnActionEvent event) {
    final Transferable transferable =  manager.getContents();
    if(transferable != null && transferable.isDataFlavorSupported(DataFlavor.stringFlavor)) {
      try {
        final String transfered = (String)transferable.getTransferData(DataFlavor.stringFlavor);
        if(JsonParser.isJson(transfered)) {
          final boolean wasTranscodingAccepted = askForJsonTranscoding(event);
          if(wasTranscodingAccepted) {
            try {
              String playJson = JsonParser.transcodeJson(transfered);
              manager.setContents(new PlayJsonTransferable(playJson));
            } catch (NotJsonException e) {
              Messages.showWarningDialog("Could not parse your input as json", "Lets-Play-Json");
            }
          }
        }
      } catch (UnsupportedFlavorException | ClassCastException e) {
        Messages.showWarningDialog("Incoherent clipboard state", "Lets-Play-Json");
      } catch (IOException e) {
        Messages.showWarningDialog("Could not read from clipboard", "Lets-Play-Json");
      }
    }
  }

  private boolean askForJsonTranscoding(AnActionEvent event) {
    final Project project = event.getData(PlatformDataKeys.PROJECT);
    final int response = Messages.showOkCancelDialog(project, "Do you want to transform this json to play json ?", "Lets-Play-Json", Messages.getQuestionIcon());
    Messages.showWarningDialog(response + "", "This Was Your Response"); //TODO remove
    return response == 0;
  }
}
