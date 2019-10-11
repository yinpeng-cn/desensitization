/*
 * Copyright 2019 the original author or authors.
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
package red.zyc.desensitization;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import red.zyc.desensitization.annotation.ChineseNameSensitive;
import red.zyc.desensitization.annotation.EmailSensitive;
import red.zyc.desensitization.metadata.MapSensitiveDescriptor;
import red.zyc.desensitization.metadata.SensitiveDescriptor;
import red.zyc.desensitization.model.Child;
import red.zyc.desensitization.model.Father;
import red.zyc.desensitization.model.Mother;
import red.zyc.desensitization.util.CallerUtil;

import java.util.*;

/**
 * @author zyc
 */
public class Example {

    private Logger log = LoggerFactory.getLogger(Example.class);

    /**
     * 对象内部域值脱敏
     */
    @Test
    public void desensitizeObject() {
        Child child = new Child();
        child.getParents().add(new Father());
        child.getParents().add(new Mother());
        log.info("before:{}", child.toString());
        Child c = SensitiveUtil.desensitize(child);
        log.info("after:{}", c);
    }

    /**
     * 单个值脱敏
     */
    @Test
    public void desensitizeValue() {

        // 使用Lambda表达式
        String email1 = SensitiveUtil.desensitize("123456@qq.com", (@EmailSensitive String value) -> {
        });
        log.info("after使用Lambda表达式指定敏感信息描述者:{}", email1);


        // 使用匿名内部类
        String email2 = SensitiveUtil.
                desensitize("123456@qq.com", new SensitiveDescriptor<String>() {
                    @Override
                    public void describe(@EmailSensitive String value) {

                    }
                });
        log.info("after使用匿名类指定敏感信息描述者:{}", email2);
    }

    /**
     * 集合内部值脱敏
     */
    @Test
    public void desensitizeList() {

        // 使用Lambda表达式
        Collection<String> emails1 = SensitiveUtil.
                desensitizeCollection(new ArrayList<>(Arrays.asList("123456@qq.com", "1234567@qq.com", "12345678@qq.com")),
                        (@EmailSensitive String value) -> {
                        });
        log.info("after使用Lambda表达式指定敏感信息描述者:{}", emails1);

        // 使用匿名内部类
        Collection<String> emails2 = SensitiveUtil.
                desensitizeCollection(new ArrayList<>(Arrays.asList("123456@qq.com", "1234567@qq.com", "12345678@qq.com")),
                        new SensitiveDescriptor<String>() {
                            @Override
                            public void describe(@EmailSensitive String value) {

                            }
                        });
        log.info("after使用匿名类指定敏感信息描述者:{}", emails2);
    }

    /**
     * 数组内部值脱敏
     */
    @Test
    public void desensitizeArray() {

        // 使用Lambda表达式
        String[] emails1 = SensitiveUtil.desensitizeArray(new String[]{"123456@qq.com", "1234567@qq.com", "12345678@qq.com"},
                (@EmailSensitive String value) -> {
                });
        log.info("after使用Lambda表达式指定敏感信息描述者:{}", Arrays.toString(emails1));

        // 使用匿名内部类
        String[] emails2 = SensitiveUtil.
                desensitizeArray(new String[]{"123456@qq.com", "1234567@qq.com", "12345678@qq.com"},
                        new SensitiveDescriptor<String>() {
                            @Override
                            public void describe(@EmailSensitive String element) {

                            }
                        });
        log.info("after使用匿名类指定敏感信息描述者:{}", Arrays.toString(emails2));
    }

    /**
     * Map内部值脱敏
     */
    @Test
    public void desensitizeMap() {
        HashMap<String, String> map = new HashMap<>();
        map.put("张三", "123456@qq.com");
        map.put("小明", "1234567@qq.com");
        map.put("李四", "1234568@qq.com");

        log.info("before:{}", map);
        // 使用Lambda表达式
        Map<String, String> emails1 = SensitiveUtil.desensitizeMap(map,
                (@ChineseNameSensitive String key, @EmailSensitive String value) -> {
                });
        log.info("after使用Lambda表达式指定敏感信息描述者:{}", emails1);


        // 使用匿名内部类
        Map<String, String> emails2 = SensitiveUtil.
                desensitizeMap(map,
                        new MapSensitiveDescriptor<String, String>() {
                            @Override
                            public void describe(@ChineseNameSensitive String key, String value) {

                            }
                        });
        log.info("after使用匿名类指定敏感信息描述者:{}", emails2);
    }

    @Test
    public void printStackTrace() {
        log.info(CallerUtil.getCaller().toString());
        CallerUtil.printStackTrace();
    }


}
