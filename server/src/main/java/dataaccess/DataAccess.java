package dataaccess;
import exception.ResponseException;

import java.util.Collection;

public interface DataAccess {
    Object add(Object dataModelObject) throws ResponseException;
    Collection<Record> listAll() throws ResponseException;
    Object get(int id) throws ResponseException;
    void delete(Integer id) throws ResponseException;
    void deleteAll() throws ResponseException;
}