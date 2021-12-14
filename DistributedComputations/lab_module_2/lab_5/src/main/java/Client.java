import dto.AuthorDTO;
import dto.BookDTO;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.util.ArrayList;
import java.util.List;

public class Client {
    private Connection connection;
    private Session session;
    private MessageProducer producer;
    private MessageConsumer consumer;
    private static final String separator = "#";

    public Client() {
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory("tcp://localhost:61616");
        try {
            connection = factory.createConnection();
            connection.start();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            Destination queueOut = session.createQueue("fromClient");
            Destination queueIn = session.createQueue("toClient");

            producer = session.createProducer(queueOut);
            producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

            consumer = session.createConsumer(queueIn);
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    private String handleMessage(String query, int timeout) throws JMSException {
        TextMessage message = session.createTextMessage(query);
        producer.send(message);
        Message mes = consumer.receive(timeout);
        if (mes == null) {
            return null;
        }

        if (mes instanceof TextMessage) {
            TextMessage textMessage = (TextMessage) mes;
            return textMessage.getText();
        }

        return "";
    }

    public AuthorDTO authorFindById(Long id) {
        String query = "AuthorFindById" + separator + id.toString();
        try {
            String response = handleMessage(query, 15000);
            return new AuthorDTO(id, response);
        } catch (JMSException e) {
            e.printStackTrace();
        }
        return null;
    }

    public BookDTO bookFindByName(String name) {
        String query = "BookFindByName" + separator + name;
        try {
            String response = handleMessage(query, 15000);
            String[] fields = response.split(separator);
            Long id = Long.parseLong(fields[0]);
            Integer year = Integer.parseInt(fields[2]);
            Long authorId = Long.parseLong(fields[3]);
            return new BookDTO(id, name, year, authorId);
        } catch (JMSException e) {
            e.printStackTrace();
        }
        return null;
    }

    public AuthorDTO authorFindByName(String name) {
        String query = "AuthorFindByName" + separator + name;
        try {
            String response = handleMessage(query, 15000);
            Long responseId = Long.parseLong(response);
            return new AuthorDTO(responseId, name);
        } catch (JMSException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean bookUpdate(BookDTO book) {
        String query = "BookUpdate" + separator + book.getBookId() +
                separator + book.getName() + separator + book.getReleaseYear()
                + separator + book.getAuthorId();
        try {
            String response = handleMessage(query, 15000);
            return "true".equals(response);
        } catch (JMSException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean authorUpdate(AuthorDTO author) {
        String query = "AuthorUpdate" + separator + author.getId() +
                separator + author.getName();
        try {
            String response = handleMessage(query, 15000);
            return "true".equals(response);
        } catch (JMSException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean bookInsert(BookDTO book) {
        String query = "BookInsert" +
                separator + book.getName() + separator + book.getReleaseYear()
                + separator + book.getAuthorId();
        try {
            String response = handleMessage(query, 15000);
            return "true".equals(response);
        } catch (JMSException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean authorInsert(AuthorDTO author) {
        String query = "AuthorInsert" +
                separator + author.getName();
        try {
            String response = handleMessage(query, 15000);
            return "true".equals(response);
        } catch (JMSException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean authorDelete(AuthorDTO author) {
        String query = "AuthorDelete" + separator + author.getId();
        try {
            String response = handleMessage(query, 15000);
            return "true".equals(response);
        } catch (JMSException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean bookDelete(BookDTO book) {
        String query = "BookDelete" + separator + book.getBookId();
        try {
            String response = handleMessage(query, 15000);
            return "true".equals(response);
        } catch (JMSException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<AuthorDTO> authorAll() {
        String query = "AuthorAll";
        List<AuthorDTO> list = new ArrayList<>();
        try {
            String response = handleMessage(query, 15000);
            String[] fields = response.split(separator);
            for (int i = 0; i < fields.length; i += 2) {
                Long id = Long.parseLong(fields[i]);
                String name = fields[i + 1];
                list.add(new AuthorDTO(id, name));
            }
            return list;
        } catch (JMSException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<BookDTO> bookAll() {
        String query = "BookAll";
        return getBookDTOS(query);
    }

    public List<BookDTO> bookFindByAuthorId(Long authorId) {
        String query = "BookFindByAuthorId" + separator + authorId.toString();
        return getBookDTOS(query);
    }

    private List<BookDTO> getBookDTOS(String query) {
        List<BookDTO> list = new ArrayList<>();
        try {
            String response = handleMessage(query, 15000);
            String[] fields = response.split(separator);
            for (int i = 0; i < fields.length; i += 4) {
                Long id = Long.parseLong(fields[i]);
                String name = fields[i + 1];
                Integer year = Integer.parseInt(fields[i + 2]);
                Long authorId = Long.parseLong(fields[i + 3]);
                list.add(new BookDTO(id,name, year, authorId));
            }
            return list;
        } catch (JMSException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void cleanMessages() {
        try {
            Message message = consumer.receiveNoWait();
            while (message!=null) {
                message = consumer.receiveNoWait();
            }
        } catch (JMSException e) {
            e.printStackTrace();
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
}
