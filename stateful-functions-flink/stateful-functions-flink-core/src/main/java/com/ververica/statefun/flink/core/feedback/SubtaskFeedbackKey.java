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

package com.ververica.statefun.flink.core.feedback;

import java.io.Serializable;
import java.util.Objects;

/** A FeedbackKey bounded to a subtask index. */
@SuppressWarnings("unused")
public final class SubtaskFeedbackKey<V> implements Serializable {

  private static final long serialVersionUID = 1;

  private final String pipelineName;
  private final int subtaskIndex;
  private final long invocationId;

  SubtaskFeedbackKey(String pipeline, long invocationId, int subtaskIndex) {
    this.pipelineName = Objects.requireNonNull(pipeline);
    this.invocationId = invocationId;
    this.subtaskIndex = subtaskIndex;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SubtaskFeedbackKey<?> that = (SubtaskFeedbackKey<?>) o;
    return subtaskIndex == that.subtaskIndex
        && invocationId == that.invocationId
        && Objects.equals(pipelineName, that.pipelineName);
  }

  @Override
  public int hashCode() {
    return Objects.hash(pipelineName, subtaskIndex, invocationId);
  }
}
