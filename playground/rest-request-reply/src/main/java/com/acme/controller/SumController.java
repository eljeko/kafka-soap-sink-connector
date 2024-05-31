package com.acme.controller;

import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyFuture;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.support.SendResult;
import org.springframework.web.bind.annotation.*;



@RestController
public class SumController {

    @Autowired
    ReplyingKafkaTemplate<String, String, String> kafkaTemplate;

    @Value("${kafka.topic.request-topic}")
    String requestTopic;

    @Value("${kafka.topic.requestreply-topic}")
    String requestReplyTopic;


    @ResponseBody
    @RequestMapping(value = "/acmerest/{id}", method = RequestMethod.GET)
    public String sum(@PathVariable("id") String id) throws InterruptedException, ExecutionException {

        String request = "{\"message\":\"Hello here User-" + id + "\"}";
        System.out.println("Request >>>>" + request);


		// create producer record
		ProducerRecord<String, String> record = new ProducerRecord<>(requestTopic,id, request);
		// set reply topic in header
		record.headers().add(new RecordHeader(KafkaHeaders.REPLY_TOPIC, requestReplyTopic.getBytes()));
		// post in kafka topic
		RequestReplyFuture<String, String, String> sendAndReceive = kafkaTemplate.sendAndReceive(record);

		// confirm if producer produced successfully
		SendResult<String, String> sendResult = sendAndReceive.getSendFuture().get();

		//print all headers
		sendResult.getProducerRecord().headers().forEach(header -> System.out.println(header.key() + ":" + header.value().toString()));

		// get consumer record
		ConsumerRecord<String, String> consumerRecord = sendAndReceive.get();
		// return consumer value
        String response = consumerRecord.value();

        System.out.println("Response >>>>" + response);

        return response;


    }

}
