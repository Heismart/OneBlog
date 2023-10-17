package com.zyd.blog.admin;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public abstract class BaseRestController {

    public final void printLog(String s,Object ...objects){
        log.info(s,objects);
    }

    public final void printWarnLog(String s,Object ...objects){
        log.warn(s,objects);
    }
    public final void printErrLog(String s,Throwable throwable){
        log.error(s,throwable);
    }
}
