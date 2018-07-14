package com.xml.validator;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.util.JAXBSource;
import javax.xml.transform.Source;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.io.IOException;

@Service
public class Validate {
    public void validateSchema(Object obj, String schemaPath) throws SAXException, JAXBException, IOException {
        SchemaFactory sf=SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

        Schema schema = sf.newSchema(getFile(schemaPath));
        JAXBContext jc = JAXBContext.newInstance(obj.getClass());
        Validator validator = schema.newValidator();
        Source source = new JAXBSource(jc,obj);
        validator.validate(source);
    }

    private File getFile(String schemaPath) throws IOException {
        return new ClassPathResource(schemaPath).getFile();
    }
}
