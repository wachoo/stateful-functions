/*
 * Copyright 2019 Ververica GmbH.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ververica.statefun.docs.io.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ververica.statefun.docs.models.User;
import com.ververica.statefun.sdk.kafka.KafkaEgressSerializer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserSerializer implements KafkaEgressSerializer<User> {

  private static final Logger LOG = LoggerFactory.getLogger(UserSerializer.class);

  private static final String TOPIC = "user-topic";

  private final ObjectMapper mapper = new ObjectMapper();

  @Override
  public ProducerRecord<byte[], byte[]> serialize(User user) {
    try {
      byte[] key = user.getUserId().getBytes();
      byte[] value = mapper.writeValueAsBytes(user);

      return new ProducerRecord<>(TOPIC, key, value);
    } catch (JsonProcessingException e) {
      LOG.info("Failed to serializer user", e);
      return null;
    }
  }
}
