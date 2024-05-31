package com.acme.controller;

import org.apache.kafka.clients.consumer.ConsumerInterceptor;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.clients.producer.ProducerInterceptor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.header.Header;
import org.springframework.kafka.support.KafkaHeaders;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class Interceptor<K, V, R> implements ProducerInterceptor<K, V>, ConsumerInterceptor<K, R> {

    private static final ConcurrentHashMap<String, byte[]> map = new ConcurrentHashMap<>();

    @Override
    public void configure(Map<String, ?> configs) {
    }

    @Override
    public ProducerRecord<K, V> onSend(ProducerRecord<K, V> record) {
        //if (!record.topic().endsWith("replies")) {
        Header header = record.headers().lastHeader(KafkaHeaders.CORRELATION_ID);
        ByteBuffer bb = ByteBuffer.wrap(header.value());
        long firstLong = bb.getLong();
        long secondLong = bb.getLong();
        UUID uuid = new UUID(firstLong, secondLong);
        System.out.println("Producing header key " + header.key() + "header value " + uuid);
        map.put(uuid.toString(), header.value());
        record.headers().remove(KafkaHeaders.CORRELATION_ID);
        record.headers().add(KafkaHeaders.CORRELATION_ID, uuid.toString().getBytes(StandardCharsets.UTF_8));
        //}
        return record;
    }

    @Override
    public void onAcknowledgement(RecordMetadata metadata, Exception exception) {
    }

    @Override
    public void close() {
    }

    @Override
    public ConsumerRecords<K, R> onConsume(ConsumerRecords<K, R> records) {
        records.forEach(rec -> {
            //	if (rec.topic().endsWith("replies")) {

            Header header = rec.headers().lastHeader(KafkaHeaders.CORRELATION_ID);

            rec.headers().remove(KafkaHeaders.CORRELATION_ID);
            System.out.println("Consuming header key " + header.key() + "header value " + header.value());

            byte[] origValue = map.remove(new String(header.value(), StandardCharsets.UTF_8));
            rec.headers().add(KafkaHeaders.CORRELATION_ID, origValue);
            //		}
        });
        return records;
    }

    @Override
    public void onCommit(Map<TopicPartition, OffsetAndMetadata> offsets) {
    }
}
