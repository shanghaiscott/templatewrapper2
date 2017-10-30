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

import gnu.getopt.Getopt;

/**
 * Enable use of TemplateWrappers via the command line.
 * 
 * 
 * @author scott
 */
public class Main {

  public static void main(String[] argv) {

    String templatePath = null;
    String templateEncoding = "UTF-8";
    String propertiesPath = "app.properties";

    Getopt options = new Getopt("templates", argv, "t:e:p:");

    int c;
    while ((c = options.getopt()) != -1) {
      switch (c) {
        case 't':
          templatePath = options.getOptarg();
          break;
        case 'p':
          propertiesPath = options.getOptarg();
          break;
        case 'e':
          templateEncoding = options.getOptarg();
          break;
      }
    }

    if (templatePath != null && propertiesPath != null) {
      TemplateWrapper template = null;

      template = new PropFileTemplate(
        templatePath, templateEncoding, propertiesPath);

      if (template != null) {
        System.out.println(template.merge());
      } else {
        System.out.print("Failed to create template.");
      }
    } else {
      usage();
    }
  }

  public static void usage() {
    StringBuilder usage = new StringBuilder();
    usage.append("java -jar TemplateWrapper.jar \\\n");
    usage.append("\t-t template \\\n");
    usage.append("\t-p properties \\\n");
    usage.append("\t-e template encoding \\\n");
    System.out.println(usage.toString());
    System.exit(-1);
  }
}
