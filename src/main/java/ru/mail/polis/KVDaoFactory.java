/*
 * Copyright 2018 (c) Vadim Tsesko <incubos@yandex.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ru.mail.polis;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.NoSuchElementException;

/**
 * Custom {@link KVDao} factory
 *
 * @author Vadim Tsesko <incubos@yandex.com>
 */
final class KVDaoFactory {
    private static final long MAX_HEAP = 128 * 1024 * 1024;

    private KVDaoFactory() {
        // Not instantiable
    }

    /**
     * Construct a {@link KVDao} instance.
     *
     * @param data local disk folder to persist the data to
     * @return a storage instance
     */
    @NotNull
    static KVDao create(@NotNull final File data) throws IOException {
        if (Runtime.getRuntime().maxMemory() > MAX_HEAP) {
            throw new IllegalStateException("The heap is too big. Consider setting Xmx.");
        }

        if (!data.exists()) {
            throw new IllegalArgumentException("Path doesn't exist: " + data);
        }

        if (!data.isDirectory()) {
            throw new IllegalArgumentException("Path is not a directory: " + data);
        }

        try (RandomAccessFile lockRAF = new RandomAccessFile(new File(data, "dao.lock"), "rw");
             FileChannel lockChannel = lockRAF.getChannel();
             FileLock lock = lockChannel.lock()) {

            return new KVDao() {
                @Override
                public void close() throws IOException {
                    lock.release();
                    lockChannel.close();
                    lockRAF.close();
                }

                @Override
                public byte[] get(@NotNull byte[] key) throws NoSuchElementException, IOException {
                    File file = new File(data, encodeKey(key));
                    if (!file.exists()) {
                        throw new NoSuchElementException();
                    }
                    try (InputStream is = new FileInputStream(file)) {
                        return is.readAllBytes();
                    }
                }

                @Override
                public void upsert(@NotNull byte[] key, @NotNull byte[] value) throws IOException {
                    try (OutputStream os = new FileOutputStream(new File(data, encodeKey(key)))) {
                        os.write(value);
                    }
                }

                @Override
                public void remove(@NotNull byte[] key) throws IOException {
                    File file = new File(data, encodeKey(key));
                    if (!file.delete() && file.exists()) {
                        throw new IOException("Failed to delete file: " + file);
                    }
                }
                private String encodeKey(byte[] key) {
                    return new String(key); // Simple encoding, replace with Base64 or Hex if needed
                }
            };
        }
    }
}
