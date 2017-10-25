package com.theslof.jmdb.filemanagement;

import com.theslof.jmdb.Database;

public abstract class WriterFactory {
    public static IWriter<Database> createWriter(String fileType){
        switch (fileType) {
            case "ser":
                return createSerializeWriter();
            case "json":
                return createJSONWriter();
            case "xml":
                return createXMLWriter();
            case "html":
                return createHTMLWriter();
            default:
                throw new IllegalArgumentException("Unknown file type");
        }
    }
    public static IWriter<Database> createSerializeWriter() {
        return new SerializeWriter();
    }

    public static IWriter<Database> createJSONWriter() {
        return new JSONWriter();
    }

    public static IWriter<Database> createXMLWriter() {
        return new XMLWriter();
    }

    public static IWriter<Database> createHTMLWriter() {
        return new HTMLWriter();
    }
}
