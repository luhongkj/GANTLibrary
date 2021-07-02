package com.luhong.locwithlibrary.net.cache;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.SequenceInputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 本地文件缓存工具，以一个key对应多个value的形式缓存
 * <p>
 * ----key1 ->  index0
 * index1
 * index2
 * index3
 * index4
 * <p>
 * ----key2 ->  index0
 * index1
 * index2
 * index3
 * index4
 * <p>
 * 缓存文件，将文件分块缓存起来，可追加，可覆盖。
 * 读取时，针对key，将所有的块整合成同一个文件流，再次读取
 */
public class DiskCache
{
    private static DiskCache diskCache;
    private File dir = null;
    private String version;
    private Comparator comparator = new Comparator<File>()
    {
        @Override
        public int compare(File f1, File f2)
        {
            String firstName = f1.getName();
            String secendName = f2.getName();
            String fNames[] = firstName.split("\\.");
            String sNames[] = secendName.split("\\.");
            int fnum = Integer.parseInt(fNames[1]);
            int snum = Integer.parseInt(sNames[1]);
            return fnum - snum;
        }
    };
    private LinkedBlockingQueue<CacheWrap> diskPool = new LinkedBlockingQueue<>();

    /**
     * @param dir     缓存文件目录
     * @param version 版本控制
     */
    private DiskCache(File dir, String version)
    {
        this.version = version;
        if (!dir.exists())
        {
            boolean result = dir.mkdirs();
        }
        this.dir = dir;
        new Thread() // 用一个死线程，循环对阻塞队列进行异步监控
        {
            @Override
            public void run()
            {
                while (true)
                {
                    try
                    {
                        CacheWrap wrap = diskPool.take();
                        putOrAppend(wrap);
                    } catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    /**
     * 删除原先key，作为新key存数据
     *
     * @param key
     * @param string 需要保存的字符串
     */
    public void put(String key, String string)
    {
        put(key, new ByteArrayInputStream(string.getBytes()));
    }

    /**
     * 删除原先key，作为新key存数据
     *
     * @param key
     * @param in
     */
    public void put(final String key, InputStream in)
    {
        try
        {
            diskPool.put(new CacheWrap(key, in, CacheWrap.Action.PUT));
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 获取原先key，有就从index后+1，没有就index=0
     *
     * @param key
     * @param in
     */
    public void append(final String key, InputStream in)
    {
        try
        {
            diskPool.put(new CacheWrap(key, in, CacheWrap.Action.APPEND));
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 根据条件，自动选择是覆盖添加或者，追加
     *
     * @param wrap
     */
    private synchronized void putOrAppend(CacheWrap wrap)
    {
        final String key = wrap.key;
        final InputStream in = wrap.is;
        File files[] = dir.listFiles(new FileFilter()
        {
            @Override
            public boolean accept(File pathname)
            {
                return pathname.isFile() && pathname.getName().matches(key + "\\.[0-9]+" + "\\." + version);
            }
        });

        if (files != null)
        {
            switch (wrap.action)
            {
                case PUT:
                {
                    for (File file : files)
                    {
                        boolean deleteResult = file.delete();
                    }
                    File file = new File(dir, key + "." + 0 + "." + version);
                    createFileByStream(file, in);
                }
                break;
                case APPEND:
                {
                    int maxIndex = -1;
                    for (File file : files)
                    {
                        String indexStr = file.getName().split("\\.")[1];
                        int index = -1;
                        try
                        {
                            index = Integer.parseInt(indexStr);
                        } catch (Exception e)
                        {
                            index = -1;
                        }
                        maxIndex = (index >= maxIndex) ? index : maxIndex;
                    }
                    File file = new File(dir, key + "." + (++maxIndex) + "." + version);
                    createFileByStream(file, in);
                }
                break;
            }
        }

    }

    /**
     * 获取原先key，有就从index后+1，没有就index=0
     *
     * @param key
     * @param string
     */
    public void append(String key, String string)
    {
        append(key, new ByteArrayInputStream(string.getBytes()));
    }

    /**
     * 这个key所有的文件，按照index的顺序，整合成同一个流
     *
     * @param key
     * @return
     */
    public synchronized InputStream get(final String key)
    {
        SequenceInputStream inputStream = null;
        File files[] = dir.listFiles(new FileFilter()
        {
            @Override
            public boolean accept(File pathname)
            {
                return pathname.isFile() && pathname.getName().matches(key + "\\.[0-9]+" + "\\." + version);
            }
        });
        if (files != null)
        {
            try
            {
                List<File> fileList = Arrays.asList(files);
                Collections.sort(fileList, comparator);
                Vector<InputStream> vector = new Vector<>();
                for (File file : fileList)
                {
                    FileInputStream fis = new FileInputStream(file);
                    vector.add(fis);
                }
                Enumeration<InputStream> e = vector.elements();
                inputStream = new SequenceInputStream(e);
            } catch (FileNotFoundException e)
            {
                e.printStackTrace();
            }
        }
        return inputStream;
    }

    /**
     * 获取指定index的key的流
     *
     * @param key
     * @param index
     * @return
     */
    public synchronized InputStream get(String key, int index)
    {
        InputStream inputStream = null;
        File file = new File(dir, key + "." + index + "." + version);
        if (file.exists() && file.isFile())
        {
            try
            {
                inputStream = new FileInputStream(file);
            } catch (FileNotFoundException e)
            {
                e.printStackTrace();
            }
        }
        return inputStream;
    }

    /**
     * 已知文件内容是文字的情况下，直接转换成文字返回
     *
     * @param key
     * @param index
     * @return
     */
    public synchronized String getString(String key, int index)
    {
        String result = null;
        InputStream is = get(key, index);
        if (is != null)
        {
            result = turnStream2String(is);
        }
        return result;
    }

    /**
     * 已知文件内容是文字的情况下,把所有的文件，整合流的形式，转换成字符串
     *
     * @param key
     * @return
     */
    public synchronized String getString(String key)
    {
        String result = null;
        InputStream is = get(key);
        if (is != null)
        {
            result = turnStream2String(is);
        }
        return result;
    }

    /**
     * 将输入流转换成字符串
     *
     * @param is
     * @return
     */
    public String turnStream2String(InputStream is)
    {
        String result = null;
        try
        {
            InputStreamReader isReader = new InputStreamReader(is);
            BufferedReader reader = new BufferedReader(isReader);
            StringBuffer buffer = new StringBuffer();
            String line = null;
            while ((line = reader.readLine()) != null)
            {
                buffer.append(line);
            }
            is.close();
            reader.close();
            result = buffer.toString();
            if (buffer.length() > 0)
            {
                buffer.delete(0, buffer.length() - 1);
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 删除指定key的所有index文件
     *
     * @param key
     */
    public synchronized void delete(final String key)
    {
        File files[] = dir.listFiles(new FileFilter()
        {
            @Override
            public boolean accept(File pathname)
            {
                return pathname.isFile() && pathname.getName().matches(key + "\\.[0-9]+" + "\\." + version);
            }
        });
        if (files != null)
        {
            for (File file : files)
            {
                file.delete();
            }
        }
    }

    /**
     * 删除指定key的，指定index的文件
     *
     * @param key
     * @param index
     */
    public synchronized void delete(String key, int index)
    {
        File file = new File(dir, key + "." + index + "." + version);
        if (file.exists() && file.isFile())
        {
            file.delete();
        }
    }

    /**
     * 清除所有文件
     */
    public synchronized void clean()
    {
        File files[] = dir.listFiles(new FileFilter()
        {
            @Override
            public boolean accept(File pathname)
            {
                return pathname.isFile() && pathname.getName().matches("[a-zA-Z]+\\.[0-9]+" + "\\." + version);
            }
        });
        if (files != null)
        {
            for (File file : files)
            {
                file.delete();
            }
        }
    }

    /**
     * 获取这个key下，一共有几个index
     *
     * @param key
     * @return
     */
    public synchronized int[] getKeyIndexs(final String key)
    {
        int indexs[] = new int[0];
        File files[] = dir.listFiles(new FileFilter()
        {
            @Override
            public boolean accept(File pathname)
            {
                return pathname.isFile() && pathname.getName().matches(key + "\\.[0-9]+" + "\\." + version);
            }
        });

        if (files != null)
        {
            Arrays.sort(files, comparator);
            indexs = new int[files.length];
            for (int a = 0; a < files.length; a++)
            {
                File file = files[a];
                String index = file.getName().split("\\.")[1];
                indexs[a] = Integer.parseInt(index);
            }
        }

        return indexs;
    }

    /**
     * 是否存在这个key
     *
     * @param key
     * @return
     */
    public synchronized boolean containsKey(final String key)
    {
        File files[] = dir.listFiles(new FileFilter()
        {
            @Override
            public boolean accept(File pathname)
            {
                return pathname.isFile() && pathname.getName().matches(key + "\\.[0-9]+" + "\\." + version);
            }
        });
        return files != null && files.length > 0;
    }

    /**
     * @param dir     缓存文件目录
     * @param version 版本控制
     */
    public synchronized static DiskCache open(File dir, String version)
    {
        if (diskCache == null)
        {
            diskCache = new DiskCache(dir, version);
        }
        return diskCache;
    }

    /**
     * 把流写入新建文件
     *
     * @param file
     * @param in
     */
    private void createFileByStream(File file, InputStream in)
    {
        try
        {
            if (!file.exists())
            {
                boolean createResult = file.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(file);
            byte buff[] = new byte[1024];
            int len = 0;
            while ((len = in.read(buff)) > 0)
            {
                fos.write(buff, 0, len);
            }
            fos.flush();
            fos.close();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }


    static class CacheWrap
    {
        InputStream is;
        String key;
        Action action;

        enum Action
        {
            PUT, APPEND
        }

        CacheWrap(String key, InputStream is, Action action)
        {
            this.key = key;
            this.is = is;
            this.action = action;
        }
    }
}
