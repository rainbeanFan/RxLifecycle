/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.trello.rxlifecycle3;

import org.junit.Before;
import org.junit.Test;

import io.reactivex.Maybe;
import io.reactivex.observers.TestObserver;
import io.reactivex.subjects.PublishSubject;

public class UntilLifecycleTransformerMaybeTest {

    PublishSubject<String> stream;
    Maybe maybe;
    PublishSubject<String> lifecycle;

    @Before
    public void setup() {
        stream = PublishSubject.create();
        maybe = stream.firstElement();
        lifecycle = PublishSubject.create();
    }

    @Test
    public void noEvent() {
        TestObserver<String> testObserver = maybe
            .compose(RxLifecycle.<String, String>bind(lifecycle))
            .test();

        stream.onNext("1");
        testObserver.assertValue("1");
        testObserver.assertComplete();
    }

    @Test
    public void oneEvent() {
        TestObserver<String> testObserver = maybe
            .compose(RxLifecycle.<String, String>bind(lifecycle))
            .test();

        lifecycle.onNext("stop");
        stream.onNext("1");

        testObserver.assertNoValues();
        testObserver.assertComplete();
    }
}