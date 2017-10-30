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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

/**
 * Base class for TemplateWrapper implementations.
 * 
 * @author scott
 */
public abstract class AbstractTemplate implements TemplateWrapper {

  protected static final Logger logger =
    Logger.getLogger(AbstractTemplate.class.getName());
  // VelocityEngine is initialized on first getVelocityEngine or via injection
  private VelocityEngine velocityEngine;
  private VelocityContext velocityContext;
  /** Can be overridden by -Dtemplate.encoding=... */
  private static final String P_ENCODING = "template.encoding";
  private static final String D_ENCODING = 
    System.getProperty(P_ENCODING, "UTF-8");
  /** Can be overridden by -Dvelocity.properties=... */
  private static final String P_PROPERTIES_FILE = "velocity.properties";
  private static final String D_PROPERTIES_FILE =
    System.getProperty(P_PROPERTIES_FILE, "velocity.properties");
  private String templatePath;
  private String templateEncoding = D_ENCODING;

  public void initVelocity() throws Exception {
    Properties velocityProperties =
      getPropertiesFromClasspath(D_PROPERTIES_FILE);
    Iterator vPropKeys = velocityProperties.keySet().iterator();
    while (vPropKeys.hasNext()) {
      String key = (String)vPropKeys.next();
      getVelocityEngine().setProperty(key, velocityProperties.get(key));
    }
    try {
      URL testURL = new URL(getTemplatePath());
      StringBuilder templateDirPath = new StringBuilder();
      templateDirPath.append(testURL.getProtocol());
      templateDirPath.append("://");
      templateDirPath.append(testURL.getHost());
      templateDirPath.append(":");
      templateDirPath.append(testURL.getPort());
      templateDirPath.append(
        testURL.getFile().substring(0, testURL.getFile().lastIndexOf("/")));
      templateDirPath.append("/");
      getVelocityEngine().setProperty(
        "url.resource.loader.root",templateDirPath.toString());
      setTemplatePath (
        testURL.getFile().substring(testURL.getFile().lastIndexOf("/") + 1));
    } catch (MalformedURLException e) {
      logger.log(Level.FINE, "Template path is not a valid URL", e);
    }

    getVelocityEngine().init();
    setVelocityContext(new VelocityContext());
    getVelocityEngine().getTemplate(getTemplatePath(), getTemplateEncoding());
  }

  @Override
  public String merge() {
    logger.log(Level.FINE, "Merging");
    StringWriter stringWriter = new StringWriter();
    try {
      getVelocityEngine().mergeTemplate(
        getTemplatePath(), getTemplateEncoding(), getVelocityContext(),
        stringWriter);
    } catch (Exception e) {
      logger.log(Level.SEVERE, "Velocity merge failed", e);
    }
    logger.log(Level.FINE, "merge result: {0}", stringWriter.toString());
    return stringWriter.toString();
  }

  private Properties getPropertiesFromClasspath(String propFileName)
    throws IOException {
    Properties props = new Properties();
    InputStream inputStream =
      this.getClass().getClassLoader().getResourceAsStream(propFileName);
    if (inputStream == null) {
      throw new FileNotFoundException("property file '" + propFileName
        + "' not found in the classpath");
    }
    props.load(inputStream);
    return props;
  }

  @Override
  public Boolean write() {
    return false;
  }

  /**
   * Initialize a new VelocityEngine if not previously initialized.
   * @return the velocityEngine
   */
  public VelocityEngine getVelocityEngine() {
    if (velocityEngine == null) {
      velocityEngine = new VelocityEngine();
    }
    return velocityEngine;
  }

  /**
   * @param velocityEngine the velocityEngine to set
   */
  public void setVelocityEngine(VelocityEngine velocityEngine) {
    this.velocityEngine = velocityEngine;
  }

  /**
   * @return the velocityContext
   */
  public VelocityContext getVelocityContext() {
    return velocityContext;
  }

  /**
   * @param velocityContext the velocityContext to set
   */
  public void setVelocityContext(VelocityContext velocityContext) {
    this.velocityContext = velocityContext;
  }

  /**
   * @return the templatePath
   */
  public String getTemplatePath() {
    return templatePath;
  }

  /**
   * @param templatePath the templatePath to set
   */
  public void setTemplatePath(String templatePath) {
    this.templatePath = templatePath;
  }

  /**
   * @return the templateEncoding
   */
  public String getTemplateEncoding() {
    return templateEncoding;
  }

  /**
   * @param templateEncoding the templateEncoding to set
   */
  public void setTemplateEncoding(String templateEncoding) {
    this.templateEncoding = templateEncoding;
  }
}
