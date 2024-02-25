package il.co.ILRD.command_factory;

import com.sun.media.sound.InvalidDataException;
import il.co.ILRD.DB_CRUD.MongoManagerCRUD;

public interface Command {
    void exec(MongoManagerCRUD DB_Manager) throws InvalidDataException;
}
