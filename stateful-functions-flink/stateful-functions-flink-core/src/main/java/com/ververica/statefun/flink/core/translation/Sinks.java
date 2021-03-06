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

package com.ververica.statefun.flink.core.translation;

import com.ververica.statefun.flink.core.StatefulFunctionsUniverse;
import com.ververica.statefun.sdk.io.EgressIdentifier;
import java.util.Map;
import java.util.Objects;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSink;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;
import org.apache.flink.util.OutputTag;

final class Sinks {
  private final Map<EgressIdentifier<?>, OutputTag<Object>> sideOutputs;
  private final Map<EgressIdentifier<?>, DecoratedSink> sinks;

  private Sinks(
      Map<EgressIdentifier<?>, OutputTag<Object>> sideOutputs,
      Map<EgressIdentifier<?>, DecoratedSink> sinks) {

    this.sideOutputs = Objects.requireNonNull(sideOutputs);
    this.sinks = Objects.requireNonNull(sinks);
  }

  static Sinks create(StatefulFunctionsUniverse universe) {
    return new Sinks(sideOutputs(universe), sinkFunctions(universe));
  }

  private static Map<EgressIdentifier<?>, DecoratedSink> sinkFunctions(
      StatefulFunctionsUniverse universe) {
    EgressToSinkTranslator egressTranslator = new EgressToSinkTranslator(universe);
    return egressTranslator.translate();
  }

  private static Map<EgressIdentifier<?>, OutputTag<Object>> sideOutputs(
      StatefulFunctionsUniverse universe) {
    SideOutputTranslator sideOutputTranslator = new SideOutputTranslator(universe);
    return sideOutputTranslator.translate();
  }

  Map<EgressIdentifier<?>, OutputTag<Object>> sideOutputTags() {
    return sideOutputs;
  }

  void consumeFrom(SingleOutputStreamOperator<?> mainOutput) {
    sideOutputs.forEach(
        (id, tag) -> {
          final DataStream<Object> sideOutputStream = mainOutput.getSideOutput(tag);

          DecoratedSink decoratedSink = sinks.get(id);
          @SuppressWarnings("unchecked")
          SinkFunction<Object> sink = (SinkFunction<Object>) decoratedSink.sink;

          DataStreamSink<Object> streamSink = sideOutputStream.addSink(sink);
          streamSink.name(decoratedSink.name);
          streamSink.uid(decoratedSink.uid);
        });
  }
}
