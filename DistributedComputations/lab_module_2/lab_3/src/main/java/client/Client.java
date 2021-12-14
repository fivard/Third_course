package client;

import dto.AuthorDTO;
import dto.BookDTO;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Client {
    private final Socket socket;
    private final PrintWriter out;
    private final BufferedReader in;
    private static final String separator = "#";

    public Client(String ip, int port) throws IOException {
        socket = new Socket(ip, port);
        in = new BufferedReader(
                new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
    }

    public AuthorDTO authorFindById(Long id) {
        String query = "AuthorFindById" + separator + id.toString();
        out.println(query);
        String response;
        try {
            response = in.readLine();
            return new AuthorDTO(id, response);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public BookDTO bookFindByName(String name) {
        String query = "BookFindByName" + separator + name;
        out.println(query);
        String response = "";
        try {
            response = in.readLine();
            String[] fields = response.split(separator);
            Long id = Long.parseLong(fields[0]);
            Integer year = Integer.parseInt(fields[2]);
            Long authorId = Long.parseLong(fields[3]);
            return new BookDTO(id, name, year, authorId);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public AuthorDTO authorFindByName(String name) {
        String query = "AuthorFindByName" + separator + name;
        out.println(query);
        try {
            Long response = Long.parseLong(in.readLine());
            return new AuthorDTO(response, name);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean bookUpdate(BookDTO book) {
        String query = "BookUpdate" + separator + book.getBookId() +
                separator + book.getName() + separator + book.getReleaseYear()
                + separator + book.getAuthorId();
        out.println(query);
        try {
            String response = in.readLine();
            return "true".equals(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean authorUpdate(AuthorDTO author) {
        String query = "AuthorUpdate" + separator + author.getId() +
                separator + author.getName();
        out.println(query);
        try {
            String response = in.readLine();
            return "true".equals(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean bookInsert(BookDTO book) {
        String query = "BookInsert" +
                separator + book.getName() + separator + book.getReleaseYear()
                + separator + book.getAuthorId();
        out.println(query);
        try {
            String response = in.readLine();
            return "true".equals(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean authorInsert(AuthorDTO author) {
        String query = "AuthorInsert" +
                separator + author.getName();
        out.println(query);
        try {
            String response = in.readLine();
            return "true".equals(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean authorDelete(AuthorDTO author) {
        String query = "AuthorDelete" + separator + author.getId();
        out.println(query);
        try {
            String response = in.readLine();
            return "true".equals(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean bookDelete(BookDTO book) {
        String query = "BookDelete" + separator + book.getBookId();
        out.println(query);
        try {
            String response = in.readLine();
            return "true".equals(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<AuthorDTO> authorAll() {
        String query = "AuthorAll";
        out.println(query);
        List<AuthorDTO> list = new ArrayList<>();
        try {
            String response = in.readLine();
            String[] fields = response.split(separator);
            for (int i = 0; i < fields.length; i += 2) {
                Long id = Long.parseLong(fields[i]);
                String name = fields[i + 1];
                list.add(new AuthorDTO(id, name));
            }
            return list;
        } catch (IOException e) {
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
        out.println(query);
        List<BookDTO> list = new ArrayList<>();
        try {
            String response = in.readLine();
            String[] fields = response.split(separator);
            for (int i = 0; i < fields.length; i += 4) {
                Long id = Long.parseLong(fields[i]);
                String name = fields[i + 1];
                Integer year = Integer.parseInt(fields[i + 2]);
                Long authorId = Long.parseLong(fields[i + 3]);
                list.add(new BookDTO(id,name, year, authorId));
            }
            return list;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void disconnect() {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}