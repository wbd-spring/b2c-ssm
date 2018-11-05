package com.wbd.item.web.mq;

import org.springframework.stereotype.Service;
import org.springframework.util.ErrorHandler;

@Service
public class SomeHandler implements ErrorHandler{
	@Override
    public void handleError(Throwable t) {
       System.out.println(t.getMessage());
       t.printStackTrace();
    }
}
