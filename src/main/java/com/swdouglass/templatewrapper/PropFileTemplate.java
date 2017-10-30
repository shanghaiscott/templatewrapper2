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

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;

/**
 * TemplateWrapper implementation which uses a properties file.
 * 
 * @author scott
 */
public class PropFileTemplate extends ClassTemplate {

  private static final String D_PROP_PATH = "app.properties";
  private String propertiesPath = D_PROP_PATH;
  private Properties properties = new Properties();

  public PropFileTemplate() {
  }

  /**
   *
   * @param inTemplatePath
   * @param inPropertiesPath
   */
  public PropFileTemplate(String inTemplatePath, String inPropertiesPath) {
    setTemplatePath(inTemplatePath);
    setPropertiesPath(inPropertiesPath);
    init();
  }

  /**
   * Constructor.
   * @param inTemplatePath path (in class path) of template file
   * @param inTemplateEncoding encoding of template file
   */
  public PropFileTemplate(String inTemplatePath, String inTemplateEncoding,
    String inPropertiesPath) {
    setTemplatePath(inTemplatePath);
    setTemplateEncoding(inTemplateEncoding);
    setPropertiesPath(inPropertiesPath);
    init();
  }

  /**
   * Load the properties file, and the template. Add the properties to
   * the VelocityContext.
   */
  @Override
  public final void init() {
    logger.log(Level.FINE, "Intializing");
    try {
      properties.load(new FileReader(propertiesPath));
      data = properties;
      super.init();
    } catch (IOException e) {
      logger.log(Level.SEVERE, "Properties initialization failed", e);
    }
  }

  /**
   * @return the propertiesPath
   */
  public String getPropertiesPath() {
    return propertiesPath;
  }

  /**
   * @param propertiesPath the propertiesPath to set
   */
  public final void setPropertiesPath(String propertiesPath) {
    this.propertiesPath = propertiesPath;
  }

  /**
   * @return the properties
   */
  public Properties getProperties() {
    return properties;
  }

  /**
   * @param properties the properties to set
   */
  public void setProperties(Properties properties) {
    this.properties = properties;
  }
}
