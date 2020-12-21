package utils.generators;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

public class EverythingCanBeMimickedGenerator {
  
  private static final String SOURCE = "data/entityarchetypes/arknpcs.xml";
  private static final String DEST = "arknpcs.xml";
  
  private static void makeMimickable(Element originalRoot) {
    for (Element e : originalRoot.getChildren()) {
      Element properties = e.getChild("Properties");
      properties.setAttribute("bIsMimicable", "1");
    }
  }
  
  public static void main(String[] args) {
    File file = new File(SOURCE);
    SAXBuilder saxBuilder = new SAXBuilder();
    Document document;
    try {
      document = saxBuilder.build(file);
    } catch (JDOMException | IOException e1) {
      e1.printStackTrace();
      return;
    }
    Element originalRoot = document.getRootElement();

    makeMimickable(originalRoot);

    File out = new File(DEST);
    XMLOutputter xmlOutput = new XMLOutputter();
    xmlOutput.setFormat(Format.getPrettyFormat());
    try {
      xmlOutput.output(document, new FileOutputStream(out));
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
}
