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

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtilsImplTest {

    private ObjectMapper om;
    private JsonUtilsImpl sut;

    @BeforeEach
    public void setup() {
        om = mock(ObjectMapper.class);
        sut = new JsonUtilsImpl(om);
    }

    @Test
    public void delegatesToJson() throws JsonProcessingException {
        var in = "in";
        var out = "out";
        when(om.writeValueAsString(eq(in))).thenReturn(out);
        var actual = sut.toJson(in);
        assertSame(out, actual);
    }

    @Test
    public void delegatesFromJsonClass() throws JsonProcessingException {
        var in = "in";
        var out = new Object();
        when(om.readValue(eq(in), eq(Object.class))).thenReturn(out);
        var actual = sut.fromJson(in, Object.class);
        assertSame(out, actual);
    }

    @Test
    public void delegatesFromJsonTRef() throws JsonProcessingException {
        var in = "in";
        var out = new Object();
        var tref = new TRef<Object>() {};
        when(om.readValue(eq(in), eq(tref))).thenReturn(out);
        var actual = sut.fromJson(in, tref);
        assertSame(out, actual);
    }

    @Test
    public void jsonExceptionsAreCaughtAndWrapped_toJson() throws JsonProcessingException {
        var in = "in";
        when(om.writeValueAsString(eq(in))).thenThrow(JsonProcessingException.class);
        assertThrows(RuntimeException.class, () -> {
            sut.toJson(in);
        });
    }

    @Test
    public void jsonExceptionsAreCaughtAndWrapped_fromJsonClass() throws JsonProcessingException {
        var in = "in";
        when(om.readValue(eq(in), eq(Object.class))).thenThrow(JsonProcessingException.class);
        assertThrows(RuntimeException.class, () -> {
            sut.fromJson(in, Object.class);
        });
    }

    @Test
    public void jsonExceptionsAreCaughtAndWrapped_fromJsonTRef() throws JsonProcessingException {
        var in = "in";
        var tref = new TRef<Object>() {};
        when(om.readValue(eq(in), eq(tref))).thenThrow(JsonProcessingException.class);
        assertThrows(RuntimeException.class, () -> {
            sut.fromJson(in, tref);
        });
    }
}