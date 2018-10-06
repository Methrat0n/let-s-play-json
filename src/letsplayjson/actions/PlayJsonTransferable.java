package letsplayjson.actions;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;

import org.jetbrains.annotations.NotNull;

public class PlayJsonTransferable implements Transferable {

  private final String json;

  public PlayJsonTransferable(String json) {
    this.json = json;
  }

  @Override
  public DataFlavor[] getTransferDataFlavors() {
    return new DataFlavor[]{DataFlavor.stringFlavor};
  }

  @Override
  public boolean isDataFlavorSupported(DataFlavor flavor) {
    return DataFlavor.stringFlavor.equals(flavor);
  }

  @NotNull
  @Override
  public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException {
    if(!isDataFlavorSupported(flavor))
      throw new UnsupportedFlavorException(flavor);

    return json;
  }
}
