package com.xweisoft.wx.family.service.upload;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.params.HttpMethodParams;

/**
 * <一句话功能简述>
 * 重新多文件上传entity类, 增加上传读取进度条
 * 
 * @author  administrator
 * @version  [版本号, 2013-5-16]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class ProgressMultipartEntity extends MultipartRequestEntity
{
    
    private ProgressListener listener;
    
    public ProgressMultipartEntity(Part[] parts, HttpMethodParams params)
    {
        super(parts, params);
    }
    
    public void setProgressListener(ProgressListener listener)
    {
        this.listener = listener;
    }
    
    @Override
    public void writeRequest(final OutputStream outstream) throws IOException
    {
        super.writeRequest(new CountingOutputStream(outstream, this.listener));
    }
    
    public static interface ProgressListener
    {
        void transferred(long num);
    }
    
    public static class CountingOutputStream extends FilterOutputStream
    {
        
        private final ProgressListener listener;
        
        private long transferred;
        
        public CountingOutputStream(final OutputStream out,
                final ProgressListener listener)
        {
            super(out);
            this.listener = listener;
            this.transferred = 0;
        }
        
        public void write(byte[] b, int off, int len) throws IOException
        {
            out.write(b, off, len);
            this.transferred += len;
            this.listener.transferred(this.transferred);
        }
        
        public void write(int b) throws IOException
        {
            out.write(b);
            this.transferred++;
            this.listener.transferred(this.transferred);
        }
    }
}