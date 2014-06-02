/**
 * Copyright (C) 2014 Microsoft Corporation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.microsoft.reef.tests.fail.task;

import com.microsoft.reef.task.Task;
import com.microsoft.reef.task.events.TaskStart;
import com.microsoft.reef.tests.exceptions.SimulatedTaskFailure;
import com.microsoft.wake.EventHandler;

import javax.inject.Inject;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A basic task that just fails when we invoke it.
 */
public final class FailTaskStart implements Task, EventHandler<TaskStart> {

  private static final Logger LOG = Logger.getLogger(FailTaskStart.class.getName());

  private transient boolean isRunning = true;

  @Inject
  public FailTaskStart() {
    LOG.info("FailTaskStart created.");
  }

  @Override
  public byte[] call(final byte[] memento) {
    synchronized (this) {
      LOG.info("FailTaskStart.call() invoked. Waiting for the message.");
      while (this.isRunning) {
        try {
          this.wait();
        } catch (final InterruptedException ex) {
          LOG.log(Level.WARNING, "wait() interrupted.", ex);
        }
      }
    }
    return new byte[0];
  }

  @Override
  public void onNext(final TaskStart event) throws SimulatedTaskFailure {
    final SimulatedTaskFailure ex = new SimulatedTaskFailure("FailTaskStart.send() invoked.");
    LOG.log(Level.FINE, "FailTaskStart.onNext() invoked: {0}", ex);
    throw ex;
  }
}
