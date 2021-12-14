import dao.BookDAO;
import dto.AuthorDTO;
import dto.BookDTO;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.util.List;

public class Server {
    private Connection connection;
    private Session session;
    private MessageProducer producer;
    private MessageConsumer consumer;

    private static final String SEPARATOR = "#";

    public void start() {
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory("tcp://localhost:61616");
        try {
            connection = factory.createConnection();
            connection.start();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            Destination queueTo = session.createQueue("toClient");
            Destination queueFrom = session.createQueue("fromClient");

            producer = session.createProducer(queueTo);
            producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

            consumer = session.createConsumer(queueFrom);

            while (processQuery()) {

            }
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    private boolean processQuery() {
        String response = "";
        String query = "";
        try {
            Message request = consumer.receive(500);
            if (request == null) {
                return true;
            }

            if (request instanceof TextMessage) {
                TextMessage message = (TextMessage) request;
                query = message.getText();
            } else { return true; }

            String [] fields = query.split(SEPARATOR);
            if (fields.length == 0) {
                return true;
            } else {
                String action = fields[0];
                AuthorDTO authorDTO;
                BookDTO bookDTO;

                switch (action) {
                    case "AuthorFindById":
                        Long id = Long.parseLong(fields[1]);
                        authorDTO = dao.AuthorDAO.findById(id);
                        response = authorDTO.getName();
                        TextMessage message = session.createTextMessage(response);
                        producer.send(message);
                        break;
                    case "BookFindByAuthorId":
                        id = Long.parseLong(fields[1]);
                        List<BookDTO> list = BookDAO.findByAuthorId(id);
                        StringBuilder str = new StringBuilder();
                        assert list != null;
                        booksToString(str, list);
                        response = str.toString();
                        message = session.createTextMessage(response);
                        producer.send(message);
                        break;
                    case "BookFindByName":
                        String name = fields[1];
                        bookDTO = BookDAO.findByName(name);
                        assert bookDTO != null;
                        response = bookDTO.getBookId() + SEPARATOR + bookDTO.getName() + SEPARATOR + bookDTO.getReleaseYear() + SEPARATOR + bookDTO.getAuthorId();
                        message = session.createTextMessage(response);
                        producer.send(message);
                        break;
                    case "AuthorFindByName":
                        name = fields[1];
                        authorDTO = dao.AuthorDAO.findByName(name);
                        assert authorDTO != null;
                        response = authorDTO.getId() + "";
                        message = session.createTextMessage(response);
                        producer.send(message);
                        break;
                    case "BookUpdate":
                        id = Long.parseLong(fields[1]);
                        name = fields[2];
                        Integer year = Integer.parseInt(fields[3]);
                        Long authorId = Long.parseLong(fields[4]);
                        bookDTO = new BookDTO(id, name, year, authorId);
                        response = BookDAO.update(bookDTO) ? "true" : "false";
                        System.out.println(response);
                        message = session.createTextMessage(response);
                        producer.send(message);
                        break;
                    case "AuthorUpdate":
                        id = Long.parseLong(fields[1]);
                        name = fields[2];
                        authorDTO = new AuthorDTO(id, name);
                        response = dao.AuthorDAO.update(authorDTO) ? "true" : "false";
                        message = session.createTextMessage(response);
                        producer.send(message);
                        break;
                    case "BookInsert":
                        name = fields[1];
                        year = Integer.parseInt(fields[2]);
                        authorId = Long.parseLong(fields[3]);
                        bookDTO = new BookDTO((long) 0, name, year, authorId);
                        response = BookDAO.insert(bookDTO) ? "true" : "false";
                        message = session.createTextMessage(response);
                        producer.send(message);
                        break;
                    case "AuthorInsert":
                        name = fields[1];
                        authorDTO = new AuthorDTO();
                        authorDTO.setName(name);
                        response = dao.AuthorDAO.insert(authorDTO) ? "true" : "false";
                        message = session.createTextMessage(response);
                        producer.send(message);
                        break;
                    case "BookDelete":
                        id = Long.parseLong(fields[1]);
                        bookDTO = new BookDTO();
                        bookDTO.setBookId(id);
                        response = BookDAO.delete(bookDTO) ? "true" : "false";
                        message = session.createTextMessage(response);
                        producer.send(message);
                        break;
                    case "AuthorDelete":
                        id = Long.parseLong(fields[1]);
                        authorDTO = new AuthorDTO();
                        authorDTO.setId(id);
                        response = dao.AuthorDAO.delete(authorDTO) ? "true" : "false";
                        message = session.createTextMessage(response);
                        producer.send(message);
                        break;
                    case "BookAll":
                        List<BookDTO> booksList = BookDAO.findAll();
                        str = new StringBuilder();
                        assert booksList != null;
                        booksToString(str, booksList);
                        response = str.toString();
                        message = session.createTextMessage(response);
                        producer.send(message);
                        break;
                    case "AuthorAll":
                        List<AuthorDTO> authorsList = dao.AuthorDAO.findAll();
                        str = new StringBuilder();
                        assert authorsList != null;
                        for (AuthorDTO author : authorsList) {
                            str.append(author.getId());
                            str.append(SEPARATOR);
                            str.append(author.getName());
                            str.append(SEPARATOR);
                        }
                        response = str.toString();
                        message = session.createTextMessage(response);
                        producer.send(message);
                        break;
                }
            }
            return true;
        } catch (JMSException ex) {
            return false;
        }
    }

    private void booksToString(StringBuilder str, List<BookDTO> list) {
        for (BookDTO book : list) {
            str.append(book.getBookId());
            str.append(SEPARATOR);
            str.append(book.getName());
            str.append(SEPARATOR);
            str.append(book.getReleaseYear());
            str.append(SEPARATOR);
            str.append(book.getAuthorId());
            str.append(SEPARATOR);
        }
    }

    public void disconnect() {
        try {
            session.close();
            connection.close();
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Server server = new Server();
        server.start();
    }
}