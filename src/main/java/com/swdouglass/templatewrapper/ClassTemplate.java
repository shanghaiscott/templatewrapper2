/*
 * Copyright 2010, Scott Douglass <scott@swdouglass.com>
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License
 * on the World Wide Web for more details:
 * http://www.fsf.org/licensing/licenses/gpl.txt
 */
package com.swdouglass.templatewrapper;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Properties;
import java.util.logging.Level;

/**
 * This class populates a VelocityContext from the public data or "getters"
 * of any object. To use this, you would create a Velocity template which has
 * variables with names matching the data or (lower case minus "get")
 * method names.
 * 
 * @author scott
 */
public class ClassTemplate extends AbstractTemplate {

  public Object data;

  public ClassTemplate() {
  }

  public ClassTemplate(Object inAttributes, String inTemplatePath) {
    setData(inAttributes);
    setTemplatePath(inTemplatePath);
    init();
  }

  public ClassTemplate(Object inAttributes, String inTemplatePath,
    String inTemplateEncoding) {
    setData(inAttributes);
    setTemplatePath(inTemplatePath);
    setTemplateEncoding(inTemplateEncoding);
    init();
  }

  public void init() {
    try {
      initVelocity();
      if (data instanceof Properties) {
        for (String name : ((Properties) data).stringPropertyNames()) {
          getVelocityContext().put(name, ((Properties) data).getProperty(name));
        }
      } else {
        // First, look for public fields
        Field[] fields = data.getClass().getFields();
        // If we have them, use them.
        if (fields.length > 0) {
          logger.log(Level.FINE, "Number of fields: {0}", fields.length);
          for (int counter = 0; counter < fields.length; counter++) {
            Object value = fields[counter].get(data);
            if (value != null) {
              getVelocityContext().put(
                fields[counter].getName(), value.toString());
            }
          }
        }
        // then also look for public getters
        Method[] methods = data.getClass().getMethods();
        if (methods.length > 0) {
          logger.log(Level.FINE, "Number of methods: {0}", methods.length);
          for (int counter = 0; counter < methods.length; counter++) {
            String name = methods[counter].getName();
            if (name.startsWith("get")) {
              try {
                Object value = methods[counter].invoke(data);
                if (value != null) {
                  String key =
                    name.substring(3, name.length()).toLowerCase();
                  logger.log(Level.FINE, "Method name translated to: {0}", key);
                  // if we haven't got an attribute already
                  if (!getVelocityContext().containsKey(key)) {
                    getVelocityContext().put(key, value.toString());
                  }
                }
              } catch (IllegalArgumentException e) {
                // Will be hit if a "getter" is called which takes arguments.
                // Ignore it.
                logger.log(Level.WARNING, "Getter takes arguments. Ignoring.", e);
              }
            }
          }
        }
        // if we have no keys, we might be messed up
        if (getVelocityContext().getKeys().length == 0) {
          logger.log(Level.WARNING,
            "Object has no public attributes or get methods: {0}",
            data.getClass().getName());
        }
      }

    } catch (IllegalAccessException e) {
      logger.log(Level.SEVERE, "Failed to access Class attribute", e);
    } catch (InvocationTargetException e) {
      logger.log(Level.SEVERE, "Failed to invoke Class method", e);
    } catch (Exception e) {
      logger.log(Level.SEVERE, "Velocity initialization failed", e);
    }
  }

  /**
   * @return the properties
   */
  public Object getData() {
    return data;
  }

  /**
   * @param properties the properties to set
   */
  public final void setData(Object attributes) {
    this.data = attributes;
  }
}
