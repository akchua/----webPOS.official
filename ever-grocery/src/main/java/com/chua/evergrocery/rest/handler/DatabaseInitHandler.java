package com.chua.evergrocery.rest.handler;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   22 Mar 2019
 */
public interface DatabaseInitHandler extends ApplicationListener<ContextRefreshedEvent> {
}
