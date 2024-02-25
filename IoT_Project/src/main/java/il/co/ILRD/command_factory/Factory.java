package il.co.ILRD.command_factory;

import java.security.InvalidKeyException;
import java.util.*;
import java.util.function.Function;

public class Factory <K,D>{
    private Map<K, Function<D, Command>> commands;

            public Factory() {
                this.commands = new HashMap<>();
            }

            public void add(K key, Function<D, Command> command) {
                commands.put(key, command);
            }

            public Command create(K key, D data) throws InvalidKeyException {
                Function<D, Command> value = commands.get(key);
                if (null == value) { // key is not valid
                    throw new InvalidKeyException("No match for the key: " + key.toString());
                }

                return value.apply(data);
            }
}
