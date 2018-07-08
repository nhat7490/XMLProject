package com.xml.utils;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.StringWriter;

public class JAXBUtils {

    public static String marshall(Object obj) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(obj.getClass());
        Marshaller marshaller = jaxbContext.createMarshaller();

        StringWriter writer = new StringWriter();
        marshaller.marshal(obj, writer);

        return writer.toString();
    }
}
