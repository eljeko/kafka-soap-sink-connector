package org.connect.soap.util;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;


public class ReplyProducer {

    private KafkaProducer<Object, String> producer;
    private String replyTopic;


    public ReplyProducer(String bootstrapServer, String replyTopic) {
        Properties configs = new Properties();
        configs.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
        configs.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        configs.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        configs.put(ProducerConfig.ACKS_CONFIG, "all");
        producer = new KafkaProducer<>(configs);

        this.replyTopic = replyTopic;
    }

    public void sendRecord(Object key, String value) {
        ProducerRecord<Object, String> aRecord = getSinkRecord(key, value);
        producer.send(aRecord, new ProducerCallback());
    }

    private ProducerRecord<Object, String> getSinkRecord(Object key, String value) {
        return new ProducerRecord<>(replyTopic, key, value);
    }

    public void dismiss() {
        producer.flush();
        producer.close();
    }

}
