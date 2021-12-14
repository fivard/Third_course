import java.io.File;
import java.io.IOException;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import models.*;

public class DOMParser {
    public static class SimpleErrorHandler implements ErrorHandler {
        public void warning(SAXParseException e) throws SAXException {
            System.out.println("Line " +e.getLineNumber() + ":");
            System.out.println(e.getMessage());
        }
        public void error(SAXParseException e) throws SAXException {
            System.out.println("Line " +e.getLineNumber() + ":");
            System.out.println(e.getMessage());
        }
        public void fatalError(SAXParseException e) throws SAXException {
            System.out.println("Line " +e.getLineNumber() + ":");
            System.out.println(e.getMessage());
        }
    }

    public static Library parse(String path) throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setValidating(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        builder.setErrorHandler(new SimpleErrorHandler());
        Document doc = builder.parse(new File(path));
        doc.getDocumentElement().normalize();

        Library library = new Library();
        NodeList nodes = doc.getElementsByTagName("Author");

        for(int i = 0; i < nodes.getLength(); ++i) {
            Element n = (Element)nodes.item(i);
            Author author = new Author();
            author.setAuthorId(n.getAttribute("id"));
            author.setName(n.getAttribute("name"));
            library.addAuthor(author);
        }

        nodes = doc.getElementsByTagName("Book");
        for(int j =0; j < nodes.getLength(); ++j) {
            Element e = (Element) nodes.item(j);
            Book book = new Book();
            book.setBookId(e.getAttribute("id"));
            book.setAuthorId(e.getAttribute("authorId"));
            book.setName(e.getAttribute("name"));
            book.setReleaseYear(Integer.parseInt(e.getAttribute("releaseYear")));
            library.addBook(book, e.getAttribute("authorId"));
        }

        return library;
    }

    public static void write(Library library, String path) throws ParserConfigurationException, TransformerException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setValidating(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.newDocument();
        Element root = doc.createElement("Library");
        doc.appendChild(root);

        Map<String, Author> authors = library.getAuthors();
        for(Map.Entry<String, Author> entry : authors.entrySet()) {
            Element gnr = doc.createElement("Author");
            gnr.setAttribute("id", entry.getValue().getAuthorId());
            gnr.setAttribute("name", entry.getValue().getName());
            root.appendChild(gnr);

            for(Book book: entry.getValue().getBooks()) {
                Element mv = doc.createElement("Book");
                mv.setAttribute("id", book.getBookId());
                mv.setAttribute("authorId", book.getAuthorId());
                mv.setAttribute("name", book.getName());
                mv.setAttribute("releaseYear", String.valueOf(book.getReleaseYear()));
                gnr.appendChild(mv);
            }
        }
        Source domSource = new DOMSource(doc);
        Result fileResult = new StreamResult(new File(path));
        TransformerFactory tfactory = TransformerFactory.newInstance();
        Transformer transformer = tfactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.ENCODING,"UTF-8");
        transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, "library.dtd");
        transformer.transform(domSource, fileResult);
    }
}