package net.ioncannon.ap4j.command;

import java.util.Map;
import java.util.HashMap;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.net.URLEncoder;
import java.io.UnsupportedEncodingException;

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
 */

public abstract class DeviceCommand
{
  private static Logger logger = Logger.getLogger(DeviceCommand.class.getName());

  private Map<String, String> parameterMap = new HashMap<String, String>();

  protected String requestType = "POST";

  protected void addParameter(String name, Object value)
  {
    parameterMap.put(name, value.toString());
  }

  protected String constructCommand(String commandName, String content)
  {
    String parameterValue = parameterMap.keySet().size() == 0 ? "" : "?";
    for(String key : parameterMap.keySet())
    {
      try
      {
        parameterValue += key + "=" + URLEncoder.encode(parameterMap.get(key), "utf-8");
      }
      catch (UnsupportedEncodingException e)
      {
        logger.log(Level.SEVERE, e.getMessage(), e);
        throw new RuntimeException(e.getMessage(), e);
      }
    }

    String headerPart = String.format("%s /%s%s HTTP/1.1\n" +
                                      "Content-Length: %d\n" +
                                      "User-Agent: MediaControl/1.0\n", requestType, commandName, parameterValue, content == null ? 0 : content.length());
    if(content == null || content.length() == 0)
    {
      return headerPart;
    }
    else
    {
      return headerPart + "\n" + content;
    }
  }

  public abstract String getCommandString();
}
