/**
 * Copyright 2023 xingble PuWeiXiang
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
package com.xingble.api.utils;


import java.util.Date;

/**
 * @description:
 * @author: pwx
 * @data: 2023/3/20 18:48
 * @version: 1.0
 */
public  class  ApiId  {

    private  static  final  long  EPOCH  =  1288834974657L;
    private  static  final  long  SEQUENCE_BITS  =  12L;
    private  static  final  long  SEQUENCE_MASK  =  (1  <<  SEQUENCE_BITS)  -  1;
    private  static  final  long  WORKER_ID_BITS  =  10L;
    private  static  final  long  DATACENTER_ID_BITS  =  12L;
    private  static  final  long  MAX_WORKER_ID  =  (1  <<  WORKER_ID_BITS)  -  1;
    private  static  final  long  MAX_DATACENTER_ID  =  (1  <<  DATACENTER_ID_BITS)  -  1;
    private  static  final  long  WORKER_ID_SHIFT  =  SEQUENCE_BITS;
    private  static  final  long  DATACENTER_ID_SHIFT  =  SEQUENCE_BITS  +  WORKER_ID_BITS;
    private  static  final  long  TIMESTAMP_LEFT_SHIFT  =  SEQUENCE_BITS  +  WORKER_ID_BITS  +  DATACENTER_ID_BITS;
    private  static  final  long  SEQUENCE_OFFSET  =  1L;

    private  final  long  workerId;
    private  final  long  datacenterId;
    private  long  sequence  =  0L;
    private  long  lastTimestamp  =  -1L;

    public  ApiId(long  workerId,  long  datacenterId)  throws  IllegalArgumentException  {
        if  (workerId  >  MAX_WORKER_ID  ||  workerId  <  0)  {
            throw  new  IllegalArgumentException(String.format("worker  Id  can't  be  greater  than  %d  or  less  than  0",  MAX_WORKER_ID));
        }
        if  (datacenterId  >  MAX_DATACENTER_ID  ||  datacenterId  <  0)  {
            throw  new  IllegalArgumentException(String.format("datacenter  Id  can't  be  greater  than  %d  or  less  than  0",  MAX_DATACENTER_ID));
        }
        this.workerId  =  workerId;
        this.datacenterId  =  datacenterId;
    }

    public  synchronized  long  next()  {
        long  timestamp  =  System.currentTimeMillis();
        if  (timestamp  <  lastTimestamp)  {
            throw  new  RuntimeException("Clock  moved  backwards.    Refusing  to  generate  id");
        }
        if  (lastTimestamp  ==  timestamp)  {
            sequence  =  (sequence  +  SEQUENCE_OFFSET)  &  SEQUENCE_MASK;
            if  (sequence  ==  0)  {
                timestamp  =  tilNextMillis(lastTimestamp);
            }
        }  else  {
            sequence  =  0L;
        }
        lastTimestamp  =  timestamp;
        return  ((timestamp  -  EPOCH)  <<  TIMESTAMP_LEFT_SHIFT)  |
                (datacenterId  <<  DATACENTER_ID_SHIFT)  |
                (workerId  <<  WORKER_ID_SHIFT)  |
                sequence;
    }

    private  long  tilNextMillis(long  lastTimestamp)  {
        long  timestamp  =  System.currentTimeMillis();
        while  (timestamp  <=  lastTimestamp)  {
            timestamp  =  System.currentTimeMillis();
        }
        return  timestamp;
    }

    public static long getId(){
        ApiId apiId = new ApiId(10, 2);
        return apiId.tilNextMillis(new Date().getTime());
    }

    public static String getIds(){
        return String.valueOf(getId());
    }

}