package server;

import dto.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

public class Server {
    private ServerSocket server = null;
    private Socket socket = null;
    private PrintWriter out = null;
    private BufferedReader in = null;
    private static final String separator = "#";

    public void start(int port) throws IOException {
        server = new ServerSocket(port);
        while (true) {
            socket = server.accept();
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            while (processQuery()) ;
        }
    }

    private boolean processQuery() {
        String response;
        try {
            String query = in.readLine();
            if (query == null) {
                return false;
            }

            String [] fields = query.split(separator);
            if (fields.length == 0) {
                return true;
            } else {
                String action = fields[0];
                AuthorDTO authorDTO;
                BookDTO bookDTO;

                switch (action) {
                    case "AuthorFindById":
                        Long id = Long.parseLong(fields[1]);
                        authorDTO = AuthorDAO.findById(id);
                        response = authorDTO.getName();
                        out.println(response);
                        break;
                    case "BookFindByAuthorId":
                        id = Long.parseLong(fields[1]);
                        List<BookDTO> list = BookDAO.findByAuthorId(id);
                        StringBuilder str = new StringBuilder();
                        assert list != null;
                        booksToString(str, list);
                        response = str.toString();
                        out.println(response);
                        break;
                    case "BookFindByName":
                        String name = fields[1];
                        bookDTO = BookDAO.findByName(name);
                        assert bookDTO != null;
                        response = bookDTO.getBookId() + separator + bookDTO.getName() + separator + bookDTO.getReleaseYear() + separator + bookDTO.getAuthorId();
                        out.println(response);
                        break;
                    case "AuthorFindByName":
                        name = fields[1];
                        authorDTO = AuthorDAO.findByName(name);
                        assert authorDTO != null;
                        response = authorDTO.getId() + "";
                        out.println(response);
                        break;
                    case "BookUpdate":
                        id = Long.parseLong(fields[1]);
                        name = fields[2];
                        Integer year = Integer.parseInt(fields[3]);
                        Long authorId = Long.parseLong(fields[4]);
                        bookDTO = new BookDTO(id, name, year, authorId);
                        if (BookDAO.update(bookDTO))
                            response = "true";
                        else
                            response = "false";
                        System.out.println(response);
                        out.println(response);
                        break;
                    case "AuthorUpdate":
                        id = Long.parseLong(fields[1]);
                        name = fields[2];
                        authorDTO = new AuthorDTO(id, name);
                        if (AuthorDAO.update(authorDTO)) {
                            response = "true";
                        } else {
                            response = "false";
                        }
                        out.println(response);
                        break;
                    case "BookInsert":
                        name = fields[1];
                        year = Integer.parseInt(fields[2]);
                        authorId = Long.parseLong(fields[3]);
                        bookDTO = new BookDTO((long) 0, name, year, authorId);
                        if (BookDAO.insert(bookDTO)) {
                            response = "true";
                        } else {
                            response = "false";
                        }
                        out.println(response);
                        break;
                    case "AuthorInsert":
                        name = fields[1];
                        authorDTO = new AuthorDTO();
                        authorDTO.setName(name);
                        if (AuthorDAO.insert(authorDTO)) {
                            response = "true";
                        } else {
                            response = "false";
                        }
                        out.println(response);
                        break;
                    case "BookDelete":
                        id = Long.parseLong(fields[1]);
                        bookDTO = new BookDTO();
                        bookDTO.setBookId(id);
                        if (BookDAO.delete(bookDTO)) {
                            response = "true";
                        } else {
                            response = "false";
                        }
                        out.println(response);
                        break;
                    case "AuthorDelete":
                        id = Long.parseLong(fields[1]);
                        authorDTO = new AuthorDTO();
                        authorDTO.setId(id);
                        if (AuthorDAO.delete(authorDTO)) {
                            response = "true";
                        } else {
                            response = "false";
                        }
                        out.println(response);
                        break;
                    case "BookAll":
                        List<BookDTO> booksList = BookDAO.findAll();
                        str = new StringBuilder();
                        assert booksList != null;
                        booksToString(str, booksList);
                        response = str.toString();
                        out.println(response);
                        break;
                    case "AuthorAll":
                        List<AuthorDTO> authorsList = AuthorDAO.findAll();
                        str = new StringBuilder();
                        for (AuthorDTO author : authorsList) {
                            str.append(author.getId());
                            str.append(separator);
                            str.append(author.getName());
                            str.append(separator);
                        }
                        response = str.toString();
                        out.println(response);
                        break;
                }
            }
            return true;
        } catch (IOException ex) {
            return false;
        }
    }

    private void booksToString(StringBuilder str, List<BookDTO> list) {
        for (BookDTO book : list) {
            str.append(book.getBookId());
            str.append(separator);
            str.append(book.getName());
            str.append(separator);
            str.append(book.getReleaseYear());
            str.append(separator);
            str.append(book.getAuthorId());
            str.append(separator);
        }
    }

    public static void main(String[] args) {
        Server server = new Server();
        try {
            server.start(5433);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
