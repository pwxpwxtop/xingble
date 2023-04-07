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
package com.xingble.api.log;

/**
 * @description:
 * @author: pwx
 * @data: 2023/1/14 12:22
 * @version: 1.0
 */
public class Print {

    public static void log(Object ... obj){
        if (obj != null){
            for (Object o : obj) {
                System.out.println(o);
            }
        }else {
            System.out.print("null");
        }
    }

}
