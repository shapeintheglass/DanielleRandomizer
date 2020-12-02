package utils.generators;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import com.google.common.collect.ImmutableMap;

public class MoreProjectileGenerator {
  private static final String SOURCE = "data/entityarchetypes/arkprojectiles_backup.xml";
  private static final String DEST = "data/entityarchetypes/arkprojectiles.xml";
  
  private static final ImmutableMap<String, String> EXPLOSIVE_GRENADE = new ImmutableMap.Builder<String, String>()
      .put("", "")
      .build();
  
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
    
    Random r = new Random();
    
    
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
