package dataaccess;
import exception.ResponseException;

import java.util.Collection;

public interface DataAccess {

    Object add(Object dataModelObject) throws ResponseException;

//    Void update(Object dataModelObject) throws ResponseException;

    Collection<Record> listAll() throws ResponseException;

//    some daos will get by id, gameid, username etc so not sure what to replace int with
    Object get(int id) throws ResponseException;

//    some daos will get by id, gameid, username etc so not sure what to replace int with
    void delete(Integer id) throws ResponseException;

    void deleteAll() throws ResponseException;

}