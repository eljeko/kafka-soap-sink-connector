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

import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.config.AbstractConfig;
import org.apache.kafka.connect.errors.ConnectException;
import org.apache.kafka.connect.sink.SinkRecord;
import org.apache.kafka.connect.sink.SinkTask;
import org.connect.soap.config.SoapSinkConfig;

import org.connect.soap.util.HttpSoapClient;
import org.connect.soap.util.JsonToSoapXml;
import org.connect.soap.util.ReplyProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * SoapSinkTask sends records to SOAP web service
 */
public class SoapSinkTask extends SinkTask {
    private static final Logger log = LoggerFactory.getLogger(SoapSinkTask.class);

    private String filename;
    private String soapUrl;
    private PrintStream outputStream;
    private JsonToSoapXml jsonToXml;
    private ReplyProducer replyProducer;

    public SoapSinkTask() throws TransformerConfigurationException {
    }


    // for testing
    public SoapSinkTask(PrintStream outputStream) throws TransformerConfigurationException {
        filename = null;
        this.outputStream = outputStream;
    }

    @Override
    public String version() {
        return new SoapSinkConnector().version();
    }

    @Override
    public void start(Map<String, String> props) {
        AbstractConfig config = new AbstractConfig(SoapSinkConnector.CONFIG_DEF, props);
        try {
            jsonToXml = new JsonToSoapXml(config.getString(SoapSinkConnector.XSLT_PATH));

        } catch (TransformerConfigurationException e) {
            throw new RuntimeException(e);
        }

        soapUrl = config.getString(SoapSinkConnector.SOAP_ENDPOINT_URL);

        filename = config.getString(SoapSinkConnector.FILE_CONFIG);
        if (filename == null || filename.isEmpty()) {
            outputStream = System.out;
        } else {
            try {
                outputStream = new PrintStream(
                        Files.newOutputStream(Paths.get(filename), StandardOpenOption.CREATE, StandardOpenOption.APPEND),
                        false,
                        StandardCharsets.UTF_8.name());
            } catch (IOException e) {
                throw new ConnectException("Couldn't find or create file '" + filename + "' for FileStreamSinkTask", e);
            }
        }

        replyProducer = new ReplyProducer(config.getString(SoapSinkConnector.REPLY_MESSAGE_BOOTSTRAP_SERVER), config.getString(SoapSinkConnector.REPLY_MESSAGE_TOPIC));
    }


    @Override
    public void put(Collection<SinkRecord> sinkRecords) {
        for (SinkRecord record : sinkRecords) {
            String xml = null;
            try {
                String soapXml = jsonToXml.tosSoap((HashMap) record.value());
                HttpSoapClient httpSoapClient = new HttpSoapClient();
                String soapResponse = httpSoapClient.sendSoapRequest(soapUrl, soapXml, "");
                replyProducer.sendRecord(record.key(),soapResponse);
                log.debug("Sending response {}", soapResponse);
            } catch (TransformerException e) {
                throw new RuntimeException(e);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            outputStream.println(xml);
        }
    }


    @Override
    public void flush(Map<TopicPartition, OffsetAndMetadata> offsets) {
        log.trace("Flushing output stream for {}", logFilename());
        outputStream.flush();
    }

    @Override
    public void stop() {
        if (outputStream != null && outputStream != System.out)
            outputStream.close();

        replyProducer.dismiss();
    }

    private String logFilename() {
        return filename == null ? "stdout" : filename;
    }
}
