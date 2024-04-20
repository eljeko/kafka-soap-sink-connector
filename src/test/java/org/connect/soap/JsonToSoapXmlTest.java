/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.connect.soap;

import org.connect.soap.util.HttpSoapClient;
import org.connect.soap.util.JsonToSoapXml;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import javax.xml.transform.TransformerException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JsonToSoapXmlTest {

    private SoapSinkTask task;
    private ByteArrayOutputStream os;
    private PrintStream printStream;

    @TempDir
    public Path topDir;
    private String outputFile;

    @BeforeEach
    public void setup() {

    }


    @Test
    public void testToSoap() throws IOException, TransformerException {
        String SOAP_REQUEST = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ser=\"http://services.acme.com/\"><soapenv:Header/><soapenv:Body><ser:hello><arg0>hello01</arg0></ser:hello></soapenv:Body></soapenv:Envelope>";

        JsonToSoapXml jsonToSoapXml = new JsonToSoapXml("soap-xslt/transform.xslt");

        String json = "{\"message\":\"hello01\"}";

        String soapXml = jsonToSoapXml.tosSoap(json);

        assertEquals(SOAP_REQUEST, soapXml);
    }

    @Test
    public void testToSendSoap() throws Exception {
        String SOAP_REQUEST = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ser=\"http://services.acme.com/\"><soapenv:Header/><soapenv:Body><ser:hello><arg0>hello01</arg0></ser:hello></soapenv:Body></soapenv:Envelope>";

        JsonToSoapXml jsonToSoapXml = new JsonToSoapXml("soap-xslt/transform.xslt");

        String json = "{\"message\":\"hello01\"}";

        String soapXml = jsonToSoapXml.tosSoap(json);

        assertEquals(SOAP_REQUEST, soapXml);

        HttpSoapClient httpSoapClient = new HttpSoapClient();
        String response = httpSoapClient.sendSoapRequest("http://localhost:8080/soap/acme",soapXml , "");
        System.out.println(response);
    }

}
