package net.ioncannon.ap4j.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;

import java.io.IOException;
import java.io.File;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.util.logging.Logger;
import java.util.logging.Level;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import net.ioncannon.ap4j.main.Main;

/**
 * Copyright (c) 2011 Carson McDonald
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 *
 *
 * This handler will find files in the /resource directory.
 *
 */
public class JettyResourceService extends AbstractHandler
{
  private static Logger logger = Logger.getLogger(JettyResourceService.class.getName());

  private File resourceDirectory;

  public JettyResourceService()
  {
    resourceDirectory = new File("resource");

    if(!resourceDirectory.exists() || !resourceDirectory.isDirectory())
    {
      logger.log(Level.SEVERE, "Could not find resource directory.");
      throw new RuntimeException("Could not find resource directory.");
    }
  }

  public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
  {
    try
    {
      handleFromResources(baseRequest, request, response);
    }
    catch (RuntimeException e)
    {
      logger.log(Level.SEVERE, e.getMessage(), e);
      Main.shutdown();
    }
  }

  private void handleFromResources(Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
  {
    String resourceName = request.getRequestURI().replace("../", "").replace('/', File.separatorChar);
    if(resourceName.trim().equals("") || resourceName.trim().equals("/"))
    {
      resourceName = "/index.html";
    }

    if(resourceName.trim().equals("/favicon.ico"))
    {
      response.setStatus(HttpServletResponse.SC_NOT_FOUND);
      baseRequest.setHandled(true);
    }
    else
    {
      sendFile(new File(resourceDirectory.getAbsolutePath() + resourceName), baseRequest, response);
    }
  }

  private void sendFile(File fileToSend, Request baseRequest, HttpServletResponse response) throws IOException
  {
    String contentType = "text/html;charset=utf-8";
    if(fileToSend.getName().endsWith(".js"))
    {
      contentType = "application/javascript;charset=utf-8";
    }
    else if(fileToSend.getName().endsWith(".css"))
    {
      contentType = "text/css;charset=utf-8";
    }
    else if(fileToSend.getName().endsWith(".png"))
    {
      contentType = "image/png";
    }

    response.setStatus(HttpServletResponse.SC_OK);
    baseRequest.setHandled(true);

    response.setContentType(contentType);

    BufferedInputStream inputBuffer = new BufferedInputStream(new FileInputStream(fileToSend));
    byte buffer[] = new byte[1024];
    int read;
    while((read = inputBuffer.read(buffer)) != -1)
    {
      response.getOutputStream().write(buffer, 0, read);
    }
  }
}