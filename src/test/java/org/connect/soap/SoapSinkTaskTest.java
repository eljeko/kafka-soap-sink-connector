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
import org.apache.kafka.connect.data.Schema;
import org.apache.kafka.connect.sink.SinkRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import javax.xml.transform.TransformerConfigurationException;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SoapSinkTaskTest {

    private SoapSinkTask task;
    private ByteArrayOutputStream os;
    private PrintStream printStream;

    @TempDir
    public Path topDir;
    private String outputFile;

    @BeforeEach
    public void setup() throws TransformerConfigurationException {
        os = new ByteArrayOutputStream();
        printStream = new PrintStream(os);
        task = new SoapSinkTask(printStream);
        task.start(new HashMap<>());
        outputFile = topDir.resolve("connect.output").toAbsolutePath().toString();
    }

    @Test
    public void testPutFlush() {
        HashMap<TopicPartition, OffsetAndMetadata> offsets = new HashMap<>();
        final String newLine = System.getProperty("line.separator");

        // We do not call task.start() since it would override the output stream

        task.put(Arrays.asList(
                new SinkRecord("topic1", 0, null, null, Schema.STRING_SCHEMA, "{\"name\":\"test1\"}", 1)
        ));
        offsets.put(new TopicPartition("topic1", 0), new OffsetAndMetadata(1L));
        task.flush(offsets);
        assertEquals("<name>test1</name>" + newLine, os.toString());

        task.put(Arrays.asList(
                new SinkRecord("topic1", 0, null, null, Schema.STRING_SCHEMA, "{\"name\":\"test2\"}", 2),
                new SinkRecord("topic2", 0, null, null, Schema.STRING_SCHEMA, "{\"name\":\"test3\"}", 1)
        ));
        offsets.put(new TopicPartition("topic1", 0), new OffsetAndMetadata(2L));
        offsets.put(new TopicPartition("topic2", 0), new OffsetAndMetadata(1L));
        task.flush(offsets);
        assertEquals("<name>test1</name>" + newLine + "<name>test2</name>" + newLine + "<name>test3</name>" + newLine, os.toString());
    }

    @Test
    public void testStart() throws IOException, TransformerConfigurationException {
        task = new SoapSinkTask();
        Map<String, String> props = new HashMap<>();
        props.put(SoapSinkConnector.FILE_CONFIG, outputFile);
        task.start(props);

        HashMap<TopicPartition, OffsetAndMetadata> offsets = new HashMap<>();

        task.put(Arrays.asList(
                new SinkRecord("topic1", 0, null, null, Schema.STRING_SCHEMA, "{\"name\":\"test1\"}", 1)
        ));

        offsets.put(new TopicPartition("topic1", 0), new OffsetAndMetadata(1L));
        task.flush(offsets);

        int numLines = 3;
        String[] lines = new String[numLines];
        int i = 0;
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(outputFile))) {
            lines[i++] = reader.readLine();
            task.put(Arrays.asList(
                    new SinkRecord("topic1", 0, null, null, Schema.STRING_SCHEMA, "{\"name\":\"test1\"}", 2),
                    new SinkRecord("topic2", 0, null, null, Schema.STRING_SCHEMA, "{\"name\":\"test2\"}", 1)
            ));
            offsets.put(new TopicPartition("topic1", 0), new OffsetAndMetadata(2L));
            offsets.put(new TopicPartition("topic2", 0), new OffsetAndMetadata(1L));
            task.flush(offsets);
            lines[i++] = reader.readLine();
            lines[i++] = reader.readLine();
        }


        assertEquals("<name>test1</name>", lines[1]);
        assertEquals("<name>test2</name>", lines[2]);

    }

    @Test
    public void testConvert() throws IOException, TransformerConfigurationException {
        task = new SoapSinkTask();
        Map<String, String> props = new HashMap<>();
        props.put(SoapSinkConnector.FILE_CONFIG, outputFile);
        task.start(props);

        HashMap<TopicPartition, OffsetAndMetadata> offsets = new HashMap<>();

        task.put(Arrays.asList(
                new SinkRecord("request", 0, null, null, Schema.STRING_SCHEMA, "{\"message\":\"hello\"}", 1)
        ));

        offsets.put(new TopicPartition("request", 0), new OffsetAndMetadata(1L));
        task.flush(offsets);

        int numLines = 3;
        String[] lines = new String[numLines];
        int i = 0;
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(outputFile))) {
            lines[i++] = reader.readLine();
            task.put(Arrays.asList(
                    new SinkRecord("topic1", 0, null, null, Schema.STRING_SCHEMA, "{\"name\":\"test1\"}", 2),
                    new SinkRecord("topic2", 0, null, null, Schema.STRING_SCHEMA, "{\"name\":\"test2\"}", 1)
            ));
            offsets.put(new TopicPartition("topic1", 0), new OffsetAndMetadata(2L));
            offsets.put(new TopicPartition("topic2", 0), new OffsetAndMetadata(1L));
            task.flush(offsets);
            lines[i++] = reader.readLine();
            lines[i++] = reader.readLine();
        }


        assertEquals("<name>test1</name>", lines[1]);
        assertEquals("<name>test2</name>", lines[2]);

    }
}
