package rmi;

import dto.AuthorDTO;
import dto.BookDTO;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface Backend extends Remote {
    public AuthorDTO authorFindById(long id) throws RemoteException;
    public AuthorDTO authorFindByName(String name) throws RemoteException;
    public boolean authorUpdate(AuthorDTO author) throws RemoteException;
    public boolean authorInsert(AuthorDTO author) throws RemoteException;
    public boolean authorDelete(AuthorDTO author) throws RemoteException;
    public List<AuthorDTO> authorFindAll() throws RemoteException;
    public BookDTO bookFindById(long id) throws RemoteException;
    public BookDTO bookFindByName(String name) throws RemoteException;
    public boolean bookUpdate(BookDTO book) throws RemoteException;
    public boolean bookInsert(BookDTO book) throws RemoteException;
    public boolean bookDelete(BookDTO book) throws RemoteException;
    public List<BookDTO> bookFindAll() throws RemoteException;
    public List<BookDTO> bookFindByAuthorId(Long id) throws RemoteException;
}

