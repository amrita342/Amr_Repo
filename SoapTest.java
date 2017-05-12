package com.test;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
public class SoapTest {

	public static void main(String args[]) {
        try {
            // Create SOAP Connection
            SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
            SOAPConnection soapConnection = soapConnectionFactory.createConnection();
            // Send SOAP Message to SOAP Server
            String url = "http://172.25.25.29:8084/PBExternalServices/v1/soap?wsdl";
            SOAPMessage soapResponse = soapConnection.call(createSOAPRequest(), url);
            // Process the SOAP Response
            printSOAPResponse(soapResponse);
            soapConnection.close();
        } catch (Exception e) {
            System.err.println("Error occurred while sending SOAP Request to Server");
            e.printStackTrace();
        }
    }
	 /**
     * Method used to print the Create SOAP Request
     */
		private static SOAPMessage createSOAPRequest() throws Exception {
        MessageFactory messageFactory = MessageFactory.newInstance();
        SOAPMessage soapMessage = messageFactory.createMessage();
        SOAPPart soapPart = soapMessage.getSOAPPart();
        //*************************Service URI************************************
        String serverURI = "http://www.payback.net/lmsglobal/ws/v1/extint/types";
        String serverURI2 = "http://www.payback.net/lmsglobal/xsd/v1/types";
        //********************************SOAP Envelope***************************
        SOAPEnvelope envelope = soapPart.getEnvelope();
        envelope.addNamespaceDeclaration("typ", serverURI);
        envelope.addNamespaceDeclaration("typ1", serverURI2);
        //*************************SOAP Request Body***************************************
        SOAPBody soapBody = envelope.getBody();
        SOAPElement soapBodyElem = soapBody.addChildElement("OrderStatusEnquiryRequest", "typ");
        SOAPElement soapBodyElem2 = soapBodyElem.addChildElement("Order", "typ");
        SOAPElement soapBodyElem3 = soapBodyElem2.addChildElement("OrderId", "typ1");
        SOAPElement soapBodyElem4 = soapBodyElem2.addChildElement("RedemptionChannel", "typ1");
        SOAPElement soapBodyElem99= soapBodyElem3.addTextNode("1234567891988");
        SOAPElement soapBodyElem991= soapBodyElem4.addTextNode("PONL");
        MimeHeaders headers = soapMessage.getMimeHeaders();
        headers.addHeader("SOAPAction", serverURI  + "OrderStatusEnquiry");
        soapMessage.saveChanges();
        System.out.print("Request SOAP Message = ");
        soapMessage.writeTo(System.out);
        System.out.println();
        return soapMessage;
    }
	 /**
     * Method used to print the SOAP Response
     */
    private static void printSOAPResponse(SOAPMessage soapResponse) throws Exception {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        Source sourceContent = soapResponse.getSOAPPart().getContent();
        System.out.print("\nResponse SOAP Message = ");
        StreamResult result = new StreamResult(System.out);
        transformer.transform(sourceContent, result);
    }
    
}
