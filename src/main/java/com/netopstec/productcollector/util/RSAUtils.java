package com.netopstec.productcollector.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.io.InputStreamReader;

/**
 *
 */
@Slf4j
public final class RSAUtils {

    private static final Invocable INVOCABLE;

    static {
        ScriptEngineManager sem = new ScriptEngineManager();
        ScriptEngine scriptEngine = sem.getEngineByName("js");

        Resource resource = new ClassPathResource("rsa.js");

        try (InputStreamReader reader = new InputStreamReader(resource.getInputStream())) {
            scriptEngine.eval(reader);
        } catch (Exception e) {
            log.error("初始化js引擎失败：", e);
        }
        INVOCABLE = (Invocable) scriptEngine;
    }

    public static String getRealPwd(String pwd, String pbk) throws Exception {
        return (String) INVOCABLE.invokeFunction("getPwd", pbk, pwd);
    }
}
