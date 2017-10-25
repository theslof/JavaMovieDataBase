package com.theslof.jmdb.filemanagement;

import java.io.File;
import java.io.IOException;

public interface IWriter<E> {
    void writeToFile(E o, File file) throws IOException;
}
