package com.luhong.locwithlibrary.net.cache;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.LinkedList;

/**
 * 合并各个流当成同一个流处理的自定义流
 */
public class MergeInputStream extends InputStream
{
    private InputStream inputStream = null;
    private final LinkedList<InputStream> streams = new LinkedList<>();

    public MergeInputStream(Collection<InputStream> streams)
    {
        this.streams.addAll(streams);
    }

    @Override
    public int read() throws IOException
    {
        if (inputStream == null)
        {
            inputStream = streams.poll();
        }
        if (inputStream != null)
        {
            int value = inputStream.read();
            if (value == -1)
            {
                inputStream = null;
                return read();
            } else
            {
                return value;
            }
        }
        return -1;
    }

    @Override
    public int available() throws IOException
    {
        int count = 0;
        for (InputStream stream : streams)
        {
            count += stream.available();
        }
        return count;
    }

    @Override
    public void close() throws IOException
    {
        inputStream = null;
        InputStream stream;
        while ((stream = streams.poll()) != null)
        {
            stream.close();
        }
    }
}
