/**
 * Copyright 2009-2017 the original author or authors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.javacrumbs.shedlock.spring.aop;


import net.javacrumbs.shedlock.core.DefaultLockingTaskExecutor;
import net.javacrumbs.shedlock.core.LockProvider;
import net.javacrumbs.shedlock.core.SchedulerLock;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.IOException;

import static org.mockito.Mockito.mock;

@Configuration
@EnableScheduling
@EnableAspectJAutoProxy
public class AopConfig {
    @Bean
    public LockProvider lockProvider() {
        return mock(LockProvider.class);
    }

    @Bean
    public ScheduledLockAopConfiguration scheduledLockAopConfiguration(LockProvider lockProvider) {
        return new ScheduledLockAopConfiguration(new DefaultLockingTaskExecutor(lockProvider));
    }

    @Bean
    public TestBean testBean() {
        return new TestBean();
    }

    static class TestBean {

        public void noAnnotation() {
        }

        @SchedulerLock(name = "normal")
        public void normal() {
        }

        @SchedulerLock(name = "runtimeException")
        public Void throwsRuntimeException() {
            throw new RuntimeException();
        }

        @SchedulerLock(name = "exception")
        public void throwsException() throws Exception {
            throw new IOException();
        }

        @SchedulerLock(name = "returnsValue")
        public int returnsValue() {
            return 0;
        }
    }
}