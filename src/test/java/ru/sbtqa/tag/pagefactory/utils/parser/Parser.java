package ru.sbtqa.tag.pagefactory.utils.parser;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.InputStream;

public interface Parser {
    Object getObject(File file, Class c) throws JAXBException;
    Object getObject(String xmlStr, Class c) throws JAXBException;
    Object getObject(InputStream stream, Class c) throws JAXBException;
    void saveObject(File file, Object o) throws JAXBException;

}
