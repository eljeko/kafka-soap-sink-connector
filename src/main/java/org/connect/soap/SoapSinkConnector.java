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

import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.config.AbstractConfig;
import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.common.config.ConfigDef.Importance;
import org.apache.kafka.common.config.ConfigDef.Type;
import org.apache.kafka.common.utils.AppInfoParser;
import org.apache.kafka.connect.connector.Task;
import org.apache.kafka.connect.sink.SinkConnector;
import org.connect.soap.config.SoapSinkConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Very simple sink connector that works with stdout or a file.
 */
public class SoapSinkConnector extends SinkConnector {

    private static final Logger log = LoggerFactory.getLogger(SoapSinkConnector.class);
    public static final String FILE_CONFIG = "file";
    public static final String REPLY_MESSAGE_TOPIC = "kafka.sink.topic";
    public static final String REPLY_MESSAGE_TOPIC_DEFAULT_VALUE = "soap_reply";
    public static final String REPLY_MESSAGE_TOPIC_DOC = "Define reply topic";

    public static final String SOAP_ENDPOINT_URL = "soap.endpoint.url";
    public static final String SOAP_ENDPOINT_URL_DEFAULT_VALUE = "http://localhost:8080";
    public static final String SOAP_ENDPOINT_URL_DOC = "Web service endpoint url";

    public static final String XSLT_PATH = "xslt.file.path";
    public static final String XSLT_PATH_DEFAULT_VALUE = "/tmp/soap.xslt";
    public static final String XSLT_PATH_DOC = "Path to xslt file";

    public static final String REPLY_MESSAGE_BOOTSTRAP_SERVER = "kafka.sink.bootstrap";
    public static final String REPLY_MESSAGE_BOOTSTRAP_SERVER_DEFAULT_VALUE = "reply-kafka:9092";
    public static final String REPLY_MESSAGE_BOOTSTRAP_SERVER_DOC = "Define reply kafka cluster bootstrap";

    public static final String MESSAGE_CORRELATION_ID_KEY = "correlationid.header.key";
    public static final String MESSAGE_CORRELATION_ID_KEY_DEFAULT_VALUE = "correlationId";
    public static final String MESSAGE_CORRELATION_ID_KEY_DOC = "Define the correlation id in messages";


    public static final String FILE = "file";
    public static final String FILE_DEFAULT_VALUE = "/tmp/fileout";
    public static final String FILE_DOC = "File out";

    public static final String PRODUCER_LINGER_MS = "kafka.producer.linger.ms";
    public static final String PRODUCER_LINGER_MS_DEFAULT_VALUE = "0";
    public static final String PRODUCER_LINGER_MS_DOC = "Set producer linger.ms option";

    public static final String PRODUCER_BATCH_SIZE = "kafka.producer.batch.size";
    public static final String PRODUCER_BATCH_SIZE_DEFAULT_VALUE = "16384";
    public static final String PRODUCER_BATCH_SIZE_DOC = "Set producer batch.size option";


    static final ConfigDef CONFIG_DEF = new ConfigDef()
            // filtering

            .define(MESSAGE_CORRELATION_ID_KEY, Type.STRING, MESSAGE_CORRELATION_ID_KEY_DEFAULT_VALUE, Importance.MEDIUM, MESSAGE_CORRELATION_ID_KEY_DOC)

            .define(REPLY_MESSAGE_TOPIC, Type.STRING, REPLY_MESSAGE_TOPIC_DEFAULT_VALUE, Importance.HIGH, REPLY_MESSAGE_TOPIC_DOC)
            .define(REPLY_MESSAGE_BOOTSTRAP_SERVER, Type.STRING, REPLY_MESSAGE_BOOTSTRAP_SERVER_DEFAULT_VALUE, Importance.HIGH, REPLY_MESSAGE_BOOTSTRAP_SERVER_DOC)
            // producer additional options for batch send
            .define(PRODUCER_LINGER_MS, Type.STRING, PRODUCER_LINGER_MS_DEFAULT_VALUE, Importance.MEDIUM, PRODUCER_LINGER_MS_DOC)
            .define(PRODUCER_BATCH_SIZE, Type.STRING, PRODUCER_BATCH_SIZE_DEFAULT_VALUE, Importance.MEDIUM, PRODUCER_BATCH_SIZE_DOC)
            //
            .define(FILE, Type.STRING, FILE_DEFAULT_VALUE, Importance.HIGH, FILE_DOC)

            .define(XSLT_PATH, Type.STRING, XSLT_PATH_DEFAULT_VALUE, Importance.HIGH, XSLT_PATH_DOC)
            .define(SOAP_ENDPOINT_URL, Type.STRING, SOAP_ENDPOINT_URL_DEFAULT_VALUE, Importance.HIGH, SOAP_ENDPOINT_URL_DOC);


    private Map<String, String> props;

    @Override
    public String version() {
        return AppInfoParser.getVersion();
    }

    @Override
    public void start(Map<String, String> props) {
        this.props = props;
        AbstractConfig config = new AbstractConfig(CONFIG_DEF, props);
        String filename = config.getString(FILE_CONFIG);
        filename = (filename == null || filename.isEmpty()) ? "standard output" : filename;
        log.info("Starting file sink connector writing to {}", filename);
    }

    @Override
    public Class<? extends Task> taskClass() {
        return SoapSinkTask.class;
    }

    @Override
    public List<Map<String, String>> taskConfigs(int maxTasks) {
        ArrayList<Map<String, String>> configs = new ArrayList<>();
        for (int i = 0; i < maxTasks; i++) {
            configs.add(props);
        }
        return configs;
    }

    @Override
    public void stop() {
        // Nothing to do since FileStreamSinkConnector has no background monitoring.
    }

    @Override
    public ConfigDef config() {
        return CONFIG_DEF;
    }

    @Override
    public boolean alterOffsets(Map<String, String> connectorConfig, Map<TopicPartition, Long> offsets) {
        // Nothing to do here since FileStreamSinkConnector does not manage offsets externally nor does it require any
        // custom offset validation
        return true;
    }
}
