package randomizers.generators;

import java.io.IOException;
import java.util.Map;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;

import utils.ZipHelper;

/*
 * Overwrites the books file with custom subjects and content.
 */
public class BookInfoHelper {

  private static final String OUT = "ark/campaign/books.xml";

  private ZipHelper zipHelper;

  public BookInfoHelper(ZipHelper zipHelper) {
    this.zipHelper = zipHelper;
  }

  public void installNewBooks(Map<String, Book> books) {
    try {
      Document document = zipHelper.getDocument(ZipHelper.BOOKS_XML);
      Element root = document.getRootElement();
      Element notes = root.getChild("Notes");

      for (Element e : notes.getChildren()) {
        String name = e.getAttributeValue("Name");
        if (books.containsKey(name)) {
          Book b = books.get(name);
          e.setAttribute("Subject", b.getSubject());
          e.setAttribute("Content", b.getContent());
        }
      }

      zipHelper.copyToPatch(document, OUT);
    } catch (IOException | JDOMException e) {
      e.printStackTrace();
    }
  }

  public static class Book {
    private String name;
    private String subject;
    private String content;

    public Book(String name, String subject, String content) {
      this.name = name;
      this.subject = subject;
      this.content = content;
    }

    public String getName() {
      return name;
    }

    public String getSubject() {
      return subject;
    }

    public String getContent() {
      return content;
    }
  }
}
