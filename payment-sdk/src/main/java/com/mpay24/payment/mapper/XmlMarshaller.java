package com.mpay24.payment.mapper;

import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;

public class XmlMarshaller {
	public String transformOrderToString(com.mpay.mdxi.Order order) throws JAXBException, PropertyException {
		StringWriter writer = new StringWriter();
		JAXBContext jaxbContext = JAXBContext.newInstance(com.mpay.mdxi.Order.class);
		Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
		jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, false);
		jaxbMarshaller.marshal(order, writer);
		return writer.toString();
	}
}
