package randomizers.generators;

import java.io.IOException;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;

import proto.RandomizerSettings.Settings;
import utils.ZipHelper;

public class OptionsMenuGenerator {

  public static void addOptionsMenu(Settings settings, ZipHelper zipHelper) throws IOException, JDOMException {
    Document d = zipHelper.getDocument(ZipHelper.OPTIONS_FILE);

    Element pages = d.getRootElement().getChild("Pages");
    Element newPage = new Element("ArkOptionLayoutPage");
    newPage.setAttribute("Label", "Randomizer");
    Element subPages = new Element("SubPages");

    Element cosmeticLayout = new Element("ArkOptionLayoutSubPage");
    cosmeticLayout.setAttribute("Label", "Cosmetic");
    cosmeticLayout.setAttribute("WriteConfigFile", "false");
    cosmeticLayout.setAttribute("RequiresApply", "false");
    Element cosmeticAttributes = new Element("Attributes");
    cosmeticAttributes.addContent(createOptionAttribute("foo", "bar"));
    cosmeticLayout.addContent(cosmeticAttributes);
    subPages.addContent(cosmeticLayout);
    newPage.addContent(subPages);
    pages.addContent(newPage);

    zipHelper.copyToPatch(d, ZipHelper.OPTIONS_FILE);
  }

  private static Element createOptionAttribute(String name, String value) {
    Element e = new Element("ArkOptionAttribute");
    e.setAttribute("Label", name);

    Element option = new Element("ArkOption");
    option.setAttribute("Label", value);
    option.setAttribute("Value", "0");

    Element options = new Element("Options").addContent(option);
    Element optionList = new Element("ArkOptionList").addContent(options);
    Element widget = new Element("Widget").addContent(optionList);
    e.addContent(widget);

    return e;
  }
}
