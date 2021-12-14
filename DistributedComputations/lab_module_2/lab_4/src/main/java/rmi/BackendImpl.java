package rmi;

import dao.AuthorDAO;
import dao.BookDAO;
import dto.AuthorDTO;
import dto.BookDTO;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class BackendImpl extends UnicastRemoteObject implements Backend {
    protected BackendImpl() throws RemoteException {
        super();
    }

    @Override
    public AuthorDTO authorFindById(long id) throws RemoteException {
        return AuthorDAO.findById(id);
    }

    @Override
    public AuthorDTO authorFindByName(String name) throws RemoteException {
        return AuthorDAO.findByName(name);
    }

    @Override
    public boolean authorUpdate(AuthorDTO author) throws RemoteException {
        return AuthorDAO.update(author);
    }

    @Override
    public boolean authorInsert(AuthorDTO author) throws RemoteException {
        return AuthorDAO.insert(author);
    }

    @Override
    public boolean authorDelete(AuthorDTO author) throws RemoteException {
        return AuthorDAO.delete(author);
    }

    @Override
    public List<AuthorDTO> authorFindAll() throws RemoteException {
        return AuthorDAO.findAll();
    }

    @Override
    public BookDTO bookFindById(long id) throws RemoteException {
        return BookDAO.findById(id);
    }

    @Override
    public BookDTO bookFindByName(String name) throws RemoteException {
        return BookDAO.findByName(name);
    }

    @Override
    public boolean bookUpdate(BookDTO book) throws RemoteException {
        return BookDAO.update(book);
    }

    @Override
    public boolean bookInsert(BookDTO book) throws RemoteException {
        return BookDAO.insert(book);
    }

    @Override
    public boolean bookDelete(BookDTO book) throws RemoteException {
        return BookDAO.delete(book);
    }

    @Override
    public List<BookDTO> bookFindAll() throws RemoteException {
        return BookDAO.findAll();
    }

    @Override
    public List<BookDTO> bookFindByAuthorId(Long id) throws RemoteException {
        return BookDAO.findByAuthorId(id);
    }

    public static void main(String[] args) throws RemoteException {
        BackendImpl bck = new BackendImpl();
        Registry registry = LocateRegistry.createRegistry(1234);
        registry.rebind("videoshop", bck);
        System.out.println("Server started!");
    }
}

