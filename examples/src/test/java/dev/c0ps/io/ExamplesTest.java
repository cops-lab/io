/*
 * Copyright 2022 Delft University of Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package dev.c0ps.io;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

public class ExamplesTest {

    // clients usually only see the API, not the implementations
    private JsonUtils jsonUtil;
    private IoUtils io;

    @TempDir
    private File baseDir;

    @BeforeEach
    public void setup() {
        // explore the ObjectMapperBuilder API for configuration options
        var omb = new ObjectMapperBuilder();
        // omb.addBuilderOptions(...)
        // omb.addMapperOptions(...)
        var om = omb.build();

        // instantiation is usually done with a dependency injector of your choice
        jsonUtil = new JsonUtilsImpl(om);
        io = new IoUtilsImpl(baseDir, jsonUtil, om);
    }

    @Test
    public void serializationOfNonGenericTypes() {

        var in = new Date(1234567890123L);

        var json = jsonUtil.toJson(in);
        assertEquals("1234567890123", json);

        var out = jsonUtil.fromJson(json, Date.class);
        assertEquals(in, out);
    }

    @Test
    public void serializationOfGenericTypes() {

        var in = List.of("foo", "bar");

        var json = jsonUtil.toJson(in);
        assertEquals("[\"foo\",\"bar\"]", json);

        var out = jsonUtil.fromJson(json, new TRef<List<String>>() {});
        assertEquals(in, out);
    }

    @Test
    public void writingTextFile() {

        var f = new File(io.getBaseFolder(), "some-file.txt");

        var in = List.of("a", "b", "c");
        io.writeToFile(in, f);
        var out = io.readFromFile(f, new TRef<List<String>>() {});

        assertEquals(in, out);
    }
}