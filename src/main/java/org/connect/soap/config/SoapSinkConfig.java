package org.connect.soap.config;

import org.apache.kafka.common.config.AbstractConfig;
import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.common.config.ConfigDef.Importance;
import org.apache.kafka.common.config.ConfigDef.Type;

import java.util.Map;

public class SoapSinkConfig {//extends AbstractConfig {
/*
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

    public static final String FILE = "file";
    public static final String FILE_DEFAULT_VALUE = "/tmp/fileout";
    public static final String FILE_DOC = "File out";

    public static final String PRODUCER_LINGER_MS = "kafka.producer.linger.ms";
    public static final String PRODUCER_LINGER_MS_DEFAULT_VALUE = "0";
    public static final String PRODUCER_LINGER_MS_DOC = "Set producer linger.ms option";

    public static final String PRODUCER_BATCH_SIZE = "kafka.producer.batch.size";
    public static final String PRODUCER_BATCH_SIZE_DEFAULT_VALUE = "16384";
    public static final String PRODUCER_BATCH_SIZE_DOC = "Set producer batch.size option";


    public static ConfigDef CONFIG = new ConfigDef()
            // filtering

            .define(REPLY_MESSAGE_TOPIC, Type.STRING, REPLY_MESSAGE_TOPIC_DEFAULT_VALUE, Importance.HIGH, REPLY_MESSAGE_TOPIC_DOC)
            .define(REPLY_MESSAGE_BOOTSTRAP_SERVER, Type.STRING, REPLY_MESSAGE_BOOTSTRAP_SERVER_DEFAULT_VALUE, Importance.HIGH, REPLY_MESSAGE_BOOTSTRAP_SERVER_DOC)
            // producer additional options for batch send
            .define(PRODUCER_LINGER_MS, Type.STRING, PRODUCER_LINGER_MS_DEFAULT_VALUE, Importance.MEDIUM, PRODUCER_LINGER_MS_DOC)
            .define(PRODUCER_BATCH_SIZE, Type.STRING, PRODUCER_BATCH_SIZE_DEFAULT_VALUE, Importance.MEDIUM, PRODUCER_BATCH_SIZE_DOC)
            //
            .define(FILE, Type.STRING, FILE_DEFAULT_VALUE, Importance.HIGH, FILE_DOC)

            .define(XSLT_PATH, Type.STRING, XSLT_PATH_DEFAULT_VALUE, Importance.HIGH, XSLT_PATH_DOC)
            .define(SOAP_ENDPOINT_URL, Type.STRING, SOAP_ENDPOINT_URL_DEFAULT_VALUE, Importance.HIGH, SOAP_ENDPOINT_URL_DOC);

    public SoapSinkConfig(ConfigDef definition, Map<?, ?> originals) {
        super(CONFIG, originals);
    }*/
}
